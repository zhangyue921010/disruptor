package TestDisruptordemo;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.dsl.Disruptor;

public class NewPublisher{
    private Disruptor disruptor;
    private EventTranslatorOneArg eventTranslatorOneArg;
    public NewPublisher(Disruptor disruptor,EventTranslatorOneArg eventTranslatorOneArg){
        this.disruptor=disruptor;
        this.eventTranslatorOneArg=eventTranslatorOneArg;
    }
    public void publish(Object o){
        disruptor.publishEvent(eventTranslatorOneArg,o);
    }
}
