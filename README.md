# simple-actor
The logic is refer akka actor. We use disruptor ring buffer as the message queue instead of BlockingQueue. Actor are belongs to a fixed RingBuffer (it means it bind with a thread).

Different with akka actor and other actor systems, my actor don't have a mailbox. Actually, if you to send a message to an actor, the message would be wrap into an object like Runnable, the 'run' method is invoke the actor receive the message. And the Runnable object would be push into RingBuffer, and the consumer thread would consume the Runnable objects from RingBuffer and "run" it one by one. Because the Actor only bind with ONE RingBuffer, then only one thread operate the Actor and avoid thread safe issues. We don't support transfor the Actor from one thread to another currently. We support the multple RingBuffer(thread) for a kind of actor, if the actor is high CPU load case, the the new actor and dispatch to RingBuffers with round-robin response-time and least policy. 

The performance test is 2500M TPS for send and integer to actor and the actor receive the integer successful. This performance depends on the advantage of RingBuffer. 

This is only a prototype, we should add more functionality (error handler, message timeout handler, avoid stack-overflow  etc.)
