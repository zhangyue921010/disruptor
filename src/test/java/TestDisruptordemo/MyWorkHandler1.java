package TestDisruptordemo;

import com.lmax.disruptor.WorkHandler;

public class MyWorkHandler1 implements WorkHandler<Element> {
    public void onEvent(Element event) throws Exception {
        System.out.println("MyWorkHanlder1 : "+ event.getValue());
    }
}
