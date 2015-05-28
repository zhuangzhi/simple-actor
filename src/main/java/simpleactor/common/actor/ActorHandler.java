package simpleactor.common.actor;

import com.lmax.disruptor.EventHandler;

public class ActorHandler implements EventHandler<ActorEvent> {
    final public ActorExecutor context;
    boolean initialized = false;
    ThreadLocal<ActorExecutor> localContext = new ThreadLocal<>();
    public ActorHandler(ActorExecutor context) {
        this.context = context;
    }

    @Override
    public void onEvent(ActorEvent event, long l, boolean b) throws Exception {
        if (!initialized) {
            localContext.set(context);
        }
        event.apply();
    }

    public static void main(String[] args) {
        ActorExecutor context = new ActorExecutor(1<<20);

    }
}
