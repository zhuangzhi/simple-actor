package simpleactor;

import org.testng.annotations.Test;
import simpleactor.common.actor.Actor;
import simpleactor.common.actor.ActorRef;
import simpleactor.common.actor.ActorSystem;
import simpleactor.common.util.PartialFunction;
import simpleactor.common.util.ReceiveBuilder;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

/**
 * @author <a href="mailto:zhuangzhi.liu@gmail.com">Zhuangzhi Liu</a>
 *         Created on: 2015/4/28
 */
public class TestActor {

    class MyActor extends Actor{
        CountDownLatch cdl ;
        public MyActor(CountDownLatch cdl) {
            this.cdl = cdl;
            receive(ReceiveBuilder.
                            match(String.class, s -> {
//                                log.info("Received String message: {}", s);
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


    class MyActor2 extends Actor{
        final int size ;
        int to = 0;
        CountDownLatch cdl;
        public MyActor2(int size, CountDownLatch cdl) {
            this.size = size;
            this.cdl = cdl;
            receive(ReceiveBuilder
//                            .match(String.class, s -> {
////                                log.info("Received String message: {}", s);
//                                count++;
//                            })
                            .match(Integer.class, s -> {
                                to++;
                                if (to >= size) {
                                    cdl.countDown();
                                }

                            })
                            .matchAny(o -> System.out.println("received unknown message - " + o)).build()
            );
        }
    }

    @Test
    public void test() throws  Exception {
        ActorSystem system = ActorSystem.single("test",1024, Executors.newSingleThreadExecutor());
        CountDownLatch cdl = new CountDownLatch(2);
        ActorRef actor = system.actorOf(new MyActor(cdl));
        actor.send("Hello");
        actor.send(10);
        actor.send(100);
        actor.send(true);
        cdl.await();
    }

//    @Test
    public void testmore() throws  Exception {
        final int count = 1<<29;
        CountDownLatch cdl = new CountDownLatch(1);
        ActorSystem system = ActorSystem.single("test",1<<20, Executors.newSingleThreadExecutor());
        ActorRef actor = system.actorOf(new MyActor2(count, cdl));
        Timer t = new Timer();
        t.begin();
        Integer kkk = 1000;
        for (int i=0;i<count;i++) {
            actor.send(kkk);
        }
        cdl.await();
        t.end();
        t.print("test actor", count);
    }

    static void print(String msg) {
        System.out.println(msg);
    }
    @Test
    public void testbecome() throws Exception {
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
        ActorSystem system = ActorSystem.single("test",1<<10, Executors.newSingleThreadExecutor());
        ActorRef actor = system.actorOf(new TheActor());
        actor.send("Hello");
        actor.send("start");
        actor.send("Hello");
        actor.send("suspend");
        actor.send("Hello");
        actor.send("resume");
        actor.send("Hello");
        actor.send("stop");
        actor.send("Hello");

        Thread.sleep(1000);

    }


}
