package TestDisruptordemo;

import com.lmax.disruptor.WorkHandler;

public class MyWorkHandler2 implements WorkHandler<Element> {
    public void onEvent(Element event) throws Exception {
        System.out.println("MyWorkHandler2 : " + event.getValue());
    }
}
