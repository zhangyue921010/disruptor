/*
 * Copyright 2011 LMAX Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lmax.disruptor;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.lmax.disruptor.util.ThreadHints;

/**
 * Blocking strategy that uses a lock and condition variable for {@link EventProcessor}s waiting on a barrier.
 * <p>
 * This strategy can be used when throughput and low-latency are not as important as CPU resource.
 */
public final class BlockingWaitStrategy implements WaitStrategy
{
    private final Lock lock = new ReentrantLock();
    private final Condition processorNotifyCondition = lock.newCondition();

    @Override
    /**
     * 获得最大可消费需要的原则：
     * 1. 消费序号不能大于生产者序号 即 消费者不能超过生产者
     * 2. 消费者序号不能大于上级消费者序号 即 下级消费者只能消费上级消费者消费过的数据
     */
    public long waitFor(long sequence, Sequence cursorSequence, Sequence dependentSequence, SequenceBarrier barrier)
        throws AlertException, InterruptedException
    {
        long availableSequence;
        /**
         * 如果生产者游标序号小于消费起点序号  即 要消费还没有生产的位置 则进入等待 等待生产者生产数据并更新生产者游标
         */
        if (cursorSequence.get() < sequence)
        {
            lock.lock();
            try
            {
                while (cursorSequence.get() < sequence)
                {
                    barrier.checkAlert();
                    processorNotifyCondition.await();
                }
            }
            finally
            {
                lock.unlock();
            }
        }//此时状态： 生产者游标大于消费者起点序号 即 生产者快于消费者

        // 1. 获取上级消费者的消费序号 赋值给availableSequence
        // 2. 若 消费序号大于上级消费者的消费序号 则需要等待
        while ((availableSequence = dependentSequence.get()) < sequence)
        {
            barrier.checkAlert();
            ThreadHints.onSpinWait();
        }

        return availableSequence;
    }

    @Override
    public void signalAllWhenBlocking()
    {
        lock.lock();
        try
        {
            processorNotifyCondition.signalAll();
        }
        finally
        {
            lock.unlock();
        }
    }

    @Override
    public String toString()
    {
        return "BlockingWaitStrategy{" +
            "processorNotifyCondition=" + processorNotifyCondition +
            '}';
    }
}
