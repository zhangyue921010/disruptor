package TestDisruptordemo;

import com.lmax.disruptor.dsl.Disruptor;

public class TestDisruptor {
    public static void main(String []args){
        // 所有Sequence值均为一直增大的long值
        MyThreadFactory myThreadFactory = new MyThreadFactory();
        //ExecutorService executorService = Executors.newCachedThreadPool();
        MyEventFactory myEventFactory = new MyEventFactory();
        //设定阻塞策略---默认为BlockingWaitStrategy类型
        //指定RingBuffer的大小
        int ringBufferSize=16;
        //创建Disruptor，默认Sequencer类型为MultiProducerSequencer
        final Disruptor<Element> elementDisruptor = new Disruptor<Element>(myEventFactory,ringBufferSize,myThreadFactory);
        //设置EventHandler---消费逻辑,并初始化好EventProcessorInfo，以供Disruptor的start

        //以BatchEventProcessor的方式消费数据:多个消费者平行消费所有数据
        MyEventHandler1 myEventHandler1 = new MyEventHandler1();
        MyEventHandler2 myEventHandler2 = new MyEventHandler2();
        elementDisruptor.handleEventsWith(myEventHandler1,myEventHandler2);

//        //以WorkProcessor的方式消费数据 多个消费进行并行消费同一份数据
//        MyWorkHandler1 myWorkHandler1 = new MyWorkHandler1();
//        MyWorkHandler2 myWorkHandler2 = new MyWorkHandler2();
//        elementDisruptor.handleEventsWithWorkerPool(myWorkHandler1,myWorkHandler2);


        //启动Disruptor线程，同时启动消费者线程
        elementDisruptor.start();

        //final RingBuffer<Element> ringBuffer = elementDisruptor.getRingBuffer();

        Thread thread1 = new Thread() {
            NewPublisher newPublisher = new NewPublisher(elementDisruptor, new MyPublisher());
            @Override
            public void run() {
                String string = "";
                for (int i = 0; i < 50; i++) {
                    newPublisher.publish(string);
                    string += "a";
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread1.start();

        Thread thread = new Thread() {
            NewPublisher newPublisher = new NewPublisher(elementDisruptor, new MyPublisher());
            @Override
            public void run() {
                for (int j = 0; j < 50; j++) {
                    newPublisher.publish(j);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();

        while(thread.isAlive()||thread1.isAlive()){
            System.out.println("standing by");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("killing");
        //executorService.shutdown();
        elementDisruptor.shutdown();
    }
}
