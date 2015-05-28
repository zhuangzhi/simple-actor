package simpleactor.common.actor;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ActorExecutor {
    final int bufferSize ;
    final Executor executor ;
    final Disruptor<ActorEvent> disruptor ;
    final RingBuffer<ActorEvent> ringBuffer ;
    final ThreadLocal<ActorExecutor> local = new ThreadLocal<>();
    final ConcurrentHashMap<String, ActorRef> actorMap = new ConcurrentHashMap<>();
    public ActorExecutor(int bufferSize, Executor executor) {
        this.bufferSize = bufferSize;
        this.executor   = executor;
        disruptor = new Disruptor<>(new ActorEvent.ActorEventFactory(),
                bufferSize, executor
                ,ProducerType.SINGLE, new PhasedBackoffWaitStrategy(2,2, TimeUnit.MILLISECONDS,new YieldingWaitStrategy()));
        ringBuffer = disruptor.getRingBuffer();
        disruptor.handleEventsWith(new ActorHandler(this));
        disruptor.start();
    }

    public RingBuffer<ActorEvent> getRingBuffer() {
        return ringBuffer;
    }

    public ActorExecutor(int bufferSize) {
        this(bufferSize, Executors.newSingleThreadExecutor());
    }


    public boolean isInnerEvent() {
        return this == local.get();
    }

    public int remainingCapacity() {
        return (int)ringBuffer.remainingCapacity();
    }
}
