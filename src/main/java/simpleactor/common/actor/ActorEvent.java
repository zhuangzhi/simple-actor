package simpleactor.common.actor;

import com.lmax.disruptor.EventFactory;

public class ActorEvent {
    private ActorRef sender;
    private IActor actor ;
    private Object msg ;


    public void set(ActorRef sender, IActor actor, Object msg) {
        this.sender = sender;
        this.actor = actor;
        this.msg = msg;
    }

    public void apply() {
        if (actor!=null&&msg!=null) {
            actor.onEvent(sender, msg);
        }
        this.actor=null;
        this.msg=null;
    }

    static class ActorEventFactory implements EventFactory<ActorEvent> {
        @Override
        public ActorEvent newInstance() {
            return new ActorEvent();
        }
    }
}
