package simpleactor.common.actor;

/**
 * @author <a href="mailto:zhuangzhi.liu@gmail.com">Zhuangzhi Liu</a>
 *         Created on: 2015/4/29
 */
public class ActorInitializationException extends Exception {
    ActorRef actor;

    public ActorInitializationException(String message, Throwable cause, ActorRef actor) {
        super(message, cause);
        this.actor = actor;
    }

    public ActorRef getActor() {
        return actor;
    }
}
