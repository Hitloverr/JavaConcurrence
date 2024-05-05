import javax.management.ObjectName;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

/**
 * 同步工具类：能根据自身状态协调线程的控制流。
 * 比如阻塞队列、信号量、栅栏、闭锁。
 *
 * 封装了一些状态，决定执行同步工具类的线程是继续执行还是等待。
 * 改变这些状态的方法。
 */
public class SyncUtil {
    /**
     * 闭锁，相当于有一个锁，在条件达成之前谁也不能通过。达到之后所有人都可以通过。
     * 可以用来确保某些活动直到其他活动都完成后才继续执行。
     * 1. 资源初始化后才继续执行。
     * 2. 依赖的其他服务都启动。
     * 3. 游戏的所有参与者都准备就绪。
     */

    // 启动门：确保所有工作线程都就绪才执行。所有线程都在同一起点开始
    // 结束门：主线程高效等待所有工作线程都执行完成。
    public long timeTasks(int nThreads,final Runnable task) throws InterruptedException {
        final CountDownLatch startGate = new CountDownLatch(1);
        final CountDownLatch endGate = new CountDownLatch(nThreads);
        for (int i = 0; i < nThreads; i++) {
            Thread t = new Thread() {
                @Override
                public void run() {
                    try {
                        startGate.await();
                        task.run();
                        endGate.countDown();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            };
            t.start();
        }
        long start = System.currentTimeMillis();
        System.nanoTime();
        startGate.countDown();
        endGate.await();
        long end = System.currentTimeMillis();
        return end - start;

    }


    /**
     * FutureTask: 实现了Future语义，表示一种抽象的可生成结果的计算。Callable。
     * 1. 等待运行。2. 正在运行 3.运行完成【正常结束、取消结束、异常结束】
     *
     * future.get 要么返回结果，要么阻塞直到结果返回。futuretask保证了结果的安全发布。
     * 异步任务。通过提前启动计算，减少等待结果时间。
     */
    class PreLoader {
        private final FutureTask<Object> future = new FutureTask<Object>(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                return null;
            }
        });

        private final Thread thread = new Thread(future);

        // first
        public void start() {
            thread.start();
        }

        // then
        // 抛出的异常：callable抛出的受检异常，RuntimeException、Error
        public Object get()  {
            try {
                return future.get();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }

        public static RuntimeException convertE(Throwable t) {
            if (t instanceof RuntimeException) {
                return (RuntimeException) t;
            } else if (t instanceof Error) {
                throw (Error)t;
            } else {
                throw new IllegalStateException();
            }
        }
    }

    /**
     * 信号量：控制同时访问某个特定资源的操作数量，同时执行某个制定操作的数量。资源池、边界等。
     * acquire、release；资源量只有1的时候，就是互斥。
     *
     */
    public class BoundedHashSet<T> {
        private final Set<T> set;
        private final Semaphore semaphore;

        public BoundedHashSet(int bound) {
            this.set = Collections.synchronizedSet(new HashSet<T>());
            this.semaphore = new Semaphore(bound);
        }

        public boolean add(T t) throws InterruptedException {
            semaphore.acquire();
            boolean wasAdded = false;
            try {
                wasAdded = set.add(t);
                return wasAdded;
            } finally {
                if (!wasAdded) {
                    semaphore.release();
                }
            }
        }

        public boolean remove(Object o) {
            boolean wasRemovbed = set.remove(o);
            if (wasRemovbed) {
                semaphore.release();;
            }
            return wasRemovbed;
        }
    }

    /**
     * 栅栏：闭锁用于等待事件，栅栏用于等待其他线程，用于实现一些协议。
     * 可以反复汇集，当所有线程到达时，执行。
     * 将问题分解成一定数量的子问题，分配线程，然后合并计算结果。执行完毕后，还嫩开始下一轮的计算。
     */

    class CellAutom {
        private final Object board;
        private final CyclicBarrier barrier;
        private final Worker[] workers;

        public CellAutom(Object board) {

            this.board = board;
            int count = Runtime.getRuntime().availableProcessors();
            this.barrier = new CyclicBarrier(count, new Runnable() {
                @Override
                public void run() {
                    // 通过栅栏之后要做的事情。
                    //board.commitNewValues();
                }
            });
            this.workers = new Worker[count];
            for (int i = 0; i < count; i++) {
//                workers[i] = new Worker(board.getSubBoard(count,i));
            }

        }

        public void start() {
            for (int i = 0; i < workers.length; i++) {
                new Thread(workers[i]).start();
            }
//            board.waitFor();
        }

        private class Worker implements Runnable {
            private final Object board;

            public Worker(Object o) {
                board = o;

            }
            @Override
            public void run() {
                try {
                    barrier.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (BrokenBarrierException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * 另外一种形式是Exchanger，交换数据，将满的缓冲区和空的缓冲区兑换，对象安全发布给另外一方
     * 当缓冲区被填满时，填充任务就交换；
     * 当缓冲区是空的时候，清空任务交换。
     * 当缓冲被填充到一定程序并且保持一定时间后，交换。
     */

}
