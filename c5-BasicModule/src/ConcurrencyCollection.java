import java.util.ArrayDeque;
import java.util.concurrent.*;

/**
 * ConcurrentHashMap ConcurrentSkipListMap【SortedMap】 CocurrentSkipListSet(SortedSet)
 * CopyOnWriteXXX ConcurrentLinkedQueue Blockingqueue
 */
public class ConcurrencyCollection {

    // 采用分段锁。任意数量读线程可以并发访问Map。
    // 执行读操作的线程和写入的线程可以并发访问
    // 一定数量的写入线程可以并发修改Map。弱一致性，创建迭代器时遍历已有元素，不保证迭代器创建后修改反映
    // size 和 isEmpty只是个大概得结果，并不一定准确。
    // HashTable SynchronizedMap是对整个Map加锁,只有当必须独占访问时，才不用ConcurrentHashMap
    {
        ConcurrentHashMap<String, Object> csm = new ConcurrentHashMap<>();
        //csm.putIfAbsent();
        //csm.replace();
        //csm.remove(1,1);
    }
}

/**
 * CopyOnWriteArrayList、CopuOnWriteArraySet。
 * 每次修改时，都创建并且重新发布一个新的容器副本，实现可变性。仅仅当迭代操作远大于写入修改操作时，
 * 才使用写入时复制容器。一个常见的例子是：事件通知系统。
 */

/**
 * 阻塞队列和生产者、消费者模式。put take| offer poll。BlockingQueue。
 * 生产者消费者阻塞的时候，一种情况是正常的，另外一种情况是它反映了你该调整生产者和消费者线程数量。
 * offer： 数据项不能添加到队列中，就返回一个失败状态=> 你可以减轻负载，降低生产者数量，抑制生产者等。
 * 思想就是分解为单独的组件，独立的操作。解耦。
 *
 * 会带来性能优势，如果一个是IO密集型、一个是CPU密集型，并发执行，能提高运行效率。
 * 还可以使用Exector框架来执行
 */

class BlockingQueueDemo{
    {
        new LinkedBlockingQueue();
        new ArrayBlockingQueue<String>(1);
        new PriorityBlockingQueue<String>();
        // 并没有队列，相当于生产者直接把东西给消费者
        // 仅挡有足够多的消费者，并且总有一个消费者准备好获取交付工作的时候，采用
        new SynchronousQueue<String>();

        class Producer implements Runnable{

            private BlockingQueue<String> blockingQueue;

            public Producer(BlockingQueue<String> blockingQueue) {
                this.blockingQueue = blockingQueue;
            }

            @Override
            public void run() {

            }
        }
        class Consumer implements Runnable{

            private BlockingQueue<String> blockingQueue;

            public Consumer(BlockingQueue<String> blockingQueue) {
                this.blockingQueue = blockingQueue;
            }

            @Override
            public void run() {

            }
        }


    }
}


/**
 * 串行线程封闭：
 * 1. concurrent包中的阻塞队列有足够的内部同步机制，保证对象安全从生产者线程-> 消费者线程
 * 线程封闭对象只能由单个线程持有，但可以安全发布该对象来转移所有权。
 *
 * 对象状态对于新的所有者是可见的，最初所有者不会访问，对象封闭在新的线程中，也就是拥有独占的访问权。
 *
 * 总之，要保证发布机制能转移可变对象的所有权，确保只有一个线程能接收被转移的对象。
 * - 阻塞队列、对象池、ConcurrentMap的remove或者 AtomicReference的compareAndSet
 *
 */

/**
 * 双端队列和工作密取
 * Deque。工作密取：消费者完成了双端队列的所有工作，就可以从其他消费者队列末端秘密获取工作。确保每个线程保持忙碌状态。
 * 网页爬虫、搜索图、垃圾回收算法
 */
class DequeDemo{
    {
        new ArrayDeque<>();
        new LinkedBlockingDeque<>();
    }
}