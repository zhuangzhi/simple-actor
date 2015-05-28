# simple-actor
The logic is refer akka actor. We use disruptor ring buffer as the message queue instead of BlockingQueue. Actor are belongs to a fixed RingBuffer (it means it bind with a thread).

Different with akka actor and other actor systems, my actor don't have a mailbox. Actually, if you to send a message to an actor, the message would be wrap into an object like Runnable, the 'run' method is invoke the actor receive the message. And the Runnable object would be push into RingBuffer, and the consumer thread would consume the Runnable objects from RingBuffer and "run" it one by one. Because the Actor only bind with ONE RingBuffer, then only one thread operate the Actor and avoid thread safe issues. We don't support transfor the Actor from one thread to another currently. We support the multple RingBuffer(thread) for a kind of actor, if the actor is high CPU load case, the the new actor and dispatch to RingBuffers with round-robin response-time and least policy. 

The performance test is 2500M TPS for send and integer to actor and the actor receive the integer successful. This performance depends on the advantage of RingBuffer. 

This is only a prototype, we should add more functionality (error handler, message timeout handler, avoid stack-overflow  etc.)

How to use it
Just like akka actor, all message handling work should be initilized at the constructor:

    class MyActor extends Actor{
        CountDownLatch cdl ;
        public MyActor(CountDownLatch cdl) {
            this.cdl = cdl;
            receive(ReceiveBuilder.
                            match(String.class, s -> {
                                log.info("Received String message: {}", s);
                                cdl.countDown();
                                System.out.printf("Received String message: %s\n", s);
                            })
                            .match(Integer.class, x -> x > 50, s -> {
                                System.out.println("Receive INT " + s);
                                cdl.countDown();
                            })
                            .matchAny(o -> System.out.println("received unknown message - " + o)).build()
            );
        }
    }

We support state machine also use become:

        class TheActor extends Actor{
            PartialFunction r1 ;
            PartialFunction r2 ;
            PartialFunction r3 ;
            PartialFunction r4 ;

            public TheActor() {
                r1 = ReceiveBuilder.match(String.class, x->x.equals("start"), x->{become(r2);print(x);})
                        .matchAny(x->print("Not started, con't consume:"+x))
                        .build();
                r2 = ReceiveBuilder.match(String.class, x->x.equals("suspend"), x->{print(x);become(r3);})
                        .matchEquals("stop", x->{become(r4);print("stopped");})
                        .match(String.class, x->print("msg:"+x))
                        .build();
                r3 = ReceiveBuilder.match(String.class, x->x.equals("resume"), x->{become(r2);print(x);})
                        .matchEquals("stop", x->{become(r4);print("stopped");})
                        .matchAny(x -> print("suspended, can't consumer msg:" + x))
                        .build();
                r4 = ReceiveBuilder.matchAny(x -> print("Stopped, can't consume:" + x)).build();
                receive(r1);
            }

        }
        
        
