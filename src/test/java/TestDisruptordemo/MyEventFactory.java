package TestDisruptordemo;

import com.lmax.disruptor.EventFactory;

/**
 * 构造RingBuffer 元素生产工厂 implements EventFactory
 * Disruptor内部维护一个RingBuffer-->private final Object[] entries;
 * 基于用户指定的事件工厂为entries初始化
 */
public class MyEventFactory implements EventFactory<Element> {
    public Element newInstance() {
        return new Element();
    }
}
