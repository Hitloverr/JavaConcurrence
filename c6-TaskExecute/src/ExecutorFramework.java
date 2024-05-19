import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;

/**
public interface Executor {
    void execute(Runnable command);
}
 任务提交、任务执行分离。
 */
public class ExecutorFramework {

    public void fixedExecutorDemo() throws IOException {
        Executor exec = Executors.newFixedThreadPool(100);
        ServerSocket serverSocket = new ServerSocket(50);
        while (true) {
            Socket connection = serverSocket.accept();
            Runnable r = new Runnable(){
                @Override
                public void run() {
                    //handleSome(connection);
                }
            };
            exec.execute(r);
        }
    }

    // 你可以很方便替换Executor执行框架的逻辑
    class ThreadPerTaskPerRequest implements  Executor{

        @Override
        public void execute(Runnable command) {
            new Thread(command).start();
        }
    }

    // 串行执行。
    class SerialExec implements Executor {

        @Override
        public void execute(Runnable command) {
            command.run();;
        }
    }

    {
        Executors.newFixedThreadPool(10);
        Executors.newSingleThreadExecutor();
        Executors.newCachedThreadPool();
        Executors.newScheduledThreadPool(2);
    }

    // 为了管理线程池的生命周期，ExecutorService扩展了Executor
    public void esDemo() throws InterruptedException {
        // 运行、关闭、已经终止 三种状态。
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        // 关闭，执行完还没执行任务，不接收新任务
        executorService.shutdown();
        // 关闭，不执行没执行玩的任务。由拒绝执行处理器处理：抛弃任务或者抛出异常或者让原来的线程来执行。
        executorService.shutdownNow();
        while (!executorService.isShutdown()) {
            try {
                executorService.execute(() -> {

                });
            }catch (RejectedExecutionException e) {
                e.printStackTrace();
            }
        }
        // stop就调用shutDown方法。
        // 等待ExecutorService到达终止状态。
        executorService.awaitTermination(200, TimeUnit.HOURS);
        executorService.isTerminated();
    }
    // 记住定时、或者延迟任务不要用Timer（不准、不能恢复、会报错），用ScheduledExecutorService
    public void timerDemo() {
        Timer timer = new Timer();
        timer.schedule(new Throwtask(),1);
        timer.schedule(new Throwtask(),5);
    }

    class Throwtask extends TimerTask{

        @Override
        public void run() {
            throw new Error();
        }
    }
}
