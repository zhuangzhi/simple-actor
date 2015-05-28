# simple-actor
The logic is refer akka actor. We use disruptor ring buffer as the message queue instead of BlockingQueue. Actor are belongs to a fixed RingBuffer (it means it bind with a thread).

This is only a prototype, we should add more functionality (error handler, message timeout handler, avoid stack-overflow  etc.)
