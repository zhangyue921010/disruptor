package TestDisruptordemo;

import com.lmax.disruptor.EventHandler;

public class MyEventHandler2 implements EventHandler<Element> {
    public void onEvent(Element event, long sequence, boolean endOfBatch) throws Exception {
        System.out.println("MyEventHandler2 : " + event.getValue());
    }
}
