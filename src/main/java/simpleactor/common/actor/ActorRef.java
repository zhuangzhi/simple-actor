package simpleactor.common.actor;

import com.lmax.disruptor.RingBuffer;

public class ActorRef implements Comparable<ActorRef>{
    IActor actor ;
    ActorContext context;
    RingBuffer<ActorEvent> ringBuffer;
    ActorPath path ;

    final static ActorRef noSender = null;


    public int uid() {return path.uid();}

    void _$() {

    }

    @Override
    public int compareTo(ActorRef other) {
        int x = path.compareTo(other.path);
        if (x==0) {
            if (this.path.uid()<other.path.uid()) return -1;
            else if(this.path.uid()==other.path.uid()) return 0;
            else return 1;
        } else {
            return x;
        }

    }


    public ActorRef(IActor actor, ActorContext context) {
        this.actor = actor;
        this.context = context;
        if (context!=null)
            ringBuffer = context.getExecutor().getRingBuffer();
    }

    public void send(Object msg) {
        send11(noSender,msg);
    }

    public void onError(String message, Throwable cause) {

    }

    public void send11(ActorRef sender, Object msg) {
        if (!actor.isAvailable()) {
            if (sender!=noSender)
                sender.onError("Actor stoped", new SystemMessage.ActorStoped(this));
        }
        if (this.context.getExecutor().isInnerEvent()) {
            actor.onEvent(sender, msg);
        } else {
            long sequence = ringBuffer.next();
            ActorEvent event = ringBuffer.get(sequence);
            event.set(sender, actor, msg);
            ringBuffer.publish(sequence);
        }
    }



}


