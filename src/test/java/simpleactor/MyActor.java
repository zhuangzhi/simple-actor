package simpleactor;


import simpleactor.common.actor.Actor;
import simpleactor.common.util.ReceiveBuilder;

public class MyActor extends Actor {


    public MyActor() {
        receive(ReceiveBuilder.
                        match(String.class, s -> {
                            System.out.println("Received String message: {}"+ s);
                        }).
                        matchAny(o -> System.out.println("received unknown message")).build()
        );
    }

}
