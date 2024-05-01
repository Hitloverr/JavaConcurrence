import java.util.concurrent.atomic.AtomicLong;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }
}


/**
 * 写一个线程安全的计数器。
 */
class CountSafe{
//    private long count;
    // 只有一个线程安全的状态类。
    private AtomicLong count = new AtomicLong(0);
    public Long getCount() {
        return count.get();
    }

    public Long service() {
        return count.getAndIncrement();
    }
}

// 竟态条件、先检查再执行、复合操作、原子操作

// 安全性、简单性、性能。

// 千万不能简单地就在方法中加个synchronized修饰符就万事大吉了，你应该尽可能缩小同步代码块的范围