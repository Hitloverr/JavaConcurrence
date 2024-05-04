import java.util.concurrent.atomic.AtomicLong;

/**
 * 当一个类的状态就是一个线程安全的类的状态（ConcurrentHashMap、CopyOnWriteArrayList）时，或者有多个线程安全的类，但是他们是独立的，它就是线程安全的。
 * 但如果相互关联，就不好说了
 */
public class ThreadSafetyDelegate {
}
class NumberRange {
    private final AtomicLong lower = new AtomicLong(0);
    private final AtomicLong upper = new AtomicLong(0);

    /**
     * 这个先检查再执行的操作，没有执行足够的加锁机制保证原子性。
     * 也就是说，这两个线程安全的类，因为彼此之间不是独立的，导致了整个类不是线程安全的，可能会有不一致的状态，两个线程都绕过了检查。
     *
     * 解决方法一：加锁
     * 解决方法二：不发布upper、lower，外界不能设置。
     * @param i
     */
    public void setLower(int i) {
        if (i > upper.get()) {
            return;
        }
        lower.set(i);
    }

    public void setUpper(int i) {
        if (i < lower.get()) {
            return;
        }
        upper.set(i);
    }

    public boolean inRange(int i) {
        return i > lower.get() && i < upper.get();
    }
}


/**
 * 如果一个状态变量是线程安全的，并且没有任何不变形条件来约束值，变量的操作上也不存在任何不允许的状态转换，就可以安全发布这个变量。
 *
 */