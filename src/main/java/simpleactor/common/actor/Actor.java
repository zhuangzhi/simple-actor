package simpleactor.common.actor;

import simpleactor.common.util.PartialFunction;
import simpleactor.common.util.ReceiveBuilder;

import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

public abstract class Actor implements  IActor {
//    protected static Logger log = LoggerFactory.getLogger(Actor.class);
    protected ActorContext context;
    protected ActorRef self;
    protected ActorRef sender=noSender;
    private boolean working = false;
    private LinkedList<PartialFunction> behaviorStack = new LinkedList<>();
    protected PartialFunction receive = ReceiveBuilder.matchAny(x -> System.out.println("receive:" + x)).build();
    private boolean available = true ;

    public static int newUid() {
        return ThreadLocalRandom.current().nextInt();
    }

    public Actor() {
    }

    public void setContext(ActorContext context) {
        this.context = context;
    }

    public boolean isAvailable() {
        return available;
    }

    protected void receive(PartialFunction behavior) {
        this.receive = behavior;
//        become(behavior);
    }

    protected void become(PartialFunction behavior, boolean discardOld) {
        if (discardOld&&!behaviorStack.isEmpty()) {
            behaviorStack.clear();
        } else {
            behaviorStack.push(receive);
            this.receive = behavior;
        }
    }

    protected void become(PartialFunction behavior) {
        become(behavior, true);
    }

    protected void become(Consumer<Object> behavior, boolean discardOle) {
        become(o -> {
            behavior.accept(o);
            return null;
        }, discardOle);
    }

    // TODO: ??
    protected void unbecome() {
        if (!behaviorStack.isEmpty()) {
            receive = behaviorStack.pop();
        }
    }



    @Override
    final public void onEvent(ActorRef sender, Object event) {
        working = true;
        this.sender = sender;
        this.receive.apply(event);
        working = false;
    }

    @Override
    final public boolean isWorking() {
        return working;
    }

//    public abstract void receive(ActorRef sender,Object msg);
    static final ActorRef noSender  = new ActorRef(null,null);


    public void start() {

    }

    public void stop() {
        available = false;
    }

    public void resume() {

    }
}
