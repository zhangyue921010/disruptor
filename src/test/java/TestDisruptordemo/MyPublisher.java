package TestDisruptordemo;

import com.lmax.disruptor.EventTranslatorOneArg;

/**
 * EventTranslator即为向Event推送数据的逻辑
 * implements EventTranslatorXxx 具体根据Element中有几个成员变量而定
 */

public class MyPublisher implements EventTranslatorOneArg<Element,Object> {
    public void translateTo(Element element, long sequence, Object arg0) {
        element.setValue(arg0);
    }
}
