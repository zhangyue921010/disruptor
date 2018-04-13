package TestDisruptordemo;

import com.lmax.disruptor.EventTranslatorOneArg;

public class MyCharPublisher implements EventTranslatorOneArg<Element,Object> {
    public void translateTo(Element event, long sequence, Object arg0) {
        event.setValue(arg0);
    }
}
