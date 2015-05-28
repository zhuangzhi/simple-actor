package simpleactor.common.actor;

public interface IActor {
    void onEvent(ActorRef sender, Object event) ;
    boolean isWorking();

    boolean isAvailable();
}
