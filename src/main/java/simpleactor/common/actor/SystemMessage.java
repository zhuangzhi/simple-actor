package simpleactor.common.actor;

import simpleactor.common.util.Option;

public class SystemMessage {

    static class Create {
        Option<ActorInitializationException> failure;

        public Create(Option<ActorInitializationException> failure) {
            this.failure = failure;
        }

        public Option<ActorInitializationException> getFailure() {
            return failure;
        }
    }

    static class Failed extends SystemMessage {
    }

    static class Start extends  SystemMessage {

    }

    static class Stop extends SystemMessage {

    }

    static class Resume extends SystemMessage {

    }



    static class ActorStoped extends Exception {
        final ActorRef actor ;

        public ActorStoped(ActorRef actor) {
            this.actor = actor;
        }

        public ActorRef getActor() {
            return actor;
        }
    }


}
