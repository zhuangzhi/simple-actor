package simpleactor.common.actor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;



public abstract class ActorSystem {
    final String name ;
    ActorPath rootPath ;
    AtomicLong id = new AtomicLong(0);

    protected ActorSystem(String name) {
        this.name = name;
        rootPath = new RootActorPath(new Address("simple",name));
    }


    abstract ActorExecutor getExecutor();

    public static ActorSystem single(String name, int bufferSize, Executor executor) {
        return new SingleExecutorActorSystem(name,bufferSize,executor);
    }

    public static ActorSystem multiple(String name, int num, int bufferSize, Supplier<Executor> factory) {
        return new MultiExecutorActorSystem(name,num, bufferSize, factory);
    }

    public ActorRef actorOf(Actor actor) {

        return actorOf(Long.toHexString(id.incrementAndGet()),actor);
    }

    public ActorRef actorOf(String path, Actor actor) {
        ActorPath actorPath = rootPath.child(path);
        ActorContext context = new ActorContext(actorPath, this,getExecutor());
        actor.setContext(context);
        return new ActorRef(actor, context);
    }

}



class SingleExecutorActorSystem extends ActorSystem {
    final ActorExecutor executor ;

    public SingleExecutorActorSystem(String name, int bufferSize, Executor executor) {
        super(name);
        this.executor = new ActorExecutor(bufferSize,executor);
    }

    @Override
    ActorExecutor getExecutor() {
        return executor;
    }
}

class MultiExecutorActorSystem extends ActorSystem {
    List<ActorExecutor> set = new ArrayList<>();
    Dispatcher dispatcher;
    MultiExecutorActorSystem(String name, int num, int bufferSize, Supplier<Executor> factory) {
        super(name);
        for (int i=0;i<num;i++) {
            set.add(new ActorExecutor(bufferSize,factory.get()));
        }
        dispatcher = new RoundRobin(set);
    }
    @Override
    ActorExecutor getExecutor() {
        return dispatcher.getExecutor();
    }
}


abstract class Dispatcher {
    List<ActorExecutor> set ;

    public Dispatcher(List<ActorExecutor> set) {
        this.set = set;
    }
    public abstract ActorExecutor getExecutor() ;
}

class RoundRobin extends Dispatcher{

    public RoundRobin(List<ActorExecutor> set) {
        super(set);
    }

    AtomicInteger current = new AtomicInteger(0);


    final static int max = 1<<30;
    @Override
    public ActorExecutor getExecutor() {
        int val = current.incrementAndGet();
        if (val>max) {
            current.set(0);
        }
        return set.get(val%set.size());
    }
}

class ResponseTime extends Dispatcher {

    public ResponseTime(List<ActorExecutor> set) {
        super(set);
    }

    @Override
    public ActorExecutor getExecutor() {
        return null;
    }
}

class Least extends Dispatcher {

    public Least(List<ActorExecutor> set) {
        super(set);
    }

    @Override
    public ActorExecutor getExecutor() {
        int tasks = -1;
        ActorExecutor r = null;
        for (ActorExecutor ae : set) {
            int v = ae.remainingCapacity();
            if (v>tasks) {
                tasks = v;
                r = ae;
            }
        }
        return r;
    }
}