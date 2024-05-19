import java.util.concurrent.BlockingQueue;

/**
 * BLOCKED、WAITING、TIMED_WAITING.
 * 阻塞时可能抛出InterruptedException，表示该方法是一个阻塞方法。中断、响应。
 * 1. 传递InterruptException
 * 2. 恢复中断。总之，最不应该做的是捕获他但不做出任何响应。这会使得高层代码无法采取处理措施。
 */
public class AwaitAndInterrupt {
}

class TaskRunnable implements Runnable {
    BlockingQueue<Object> queue;

    @Override
    public void run() {
//        try{
//            //doSomething(queue.take());
//        } catch(InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
    }
}

