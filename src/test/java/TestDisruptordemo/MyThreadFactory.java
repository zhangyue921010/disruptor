package TestDisruptordemo;

import java.util.concurrent.ThreadFactory;

/**
 * 构造生产者线程工厂 implements ThreadFactory
 * Disruptor基于此创建一个Executor
 * 不建议在创建Disruptor的时候传入一个用户自定义的线程池
 */
public class MyThreadFactory implements ThreadFactory{
    public Thread newThread(Runnable r) {
        return new Thread(r);
    }
}
