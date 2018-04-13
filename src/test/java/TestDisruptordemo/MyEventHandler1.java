package TestDisruptordemo;


import com.lmax.disruptor.EventHandler;

/**
 *  构造处理Event的handler，消费者的消费逻辑
 */
public class MyEventHandler1 implements EventHandler<Element> {
    public void onEvent(Element element, long l, boolean b){
        System.out.println("MyEventHandler1 : " + element.getValue());
    }
}
