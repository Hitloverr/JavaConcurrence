import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

/**
 * 原有的线程安全的类有时候不满足我们需要，这个时候就要扩展。
 * 1. 直接修改代码，这个一般做不到。
 * 2. 扩展,注意，后续如果父类更改了实现的同步策略 并且使用了其他的锁，扩展的子类可能会失效。（使用了不正确的锁来控制）
 * 3. 组合，注意要使用正确的锁~！！！！
 */
public class AddFunction {
}

class MyVector extends Vector {
    public synchronized void addIfAbsent(Object e) {
        if (contains(e)) {
            return;
        }
        add(e);
    }
}



// 如果这里只是在addIfAbsent方法上加一个synchronized，会导致不是一个锁，这个方法相较于List的其他操作不是原子的。
// 但是这种客户端加锁的行为，破坏了同步策略的封装性。
class ListHelper<E> {
    public List<E> list = Collections.synchronizedList(new ArrayList<E>());

    public void addIfAbsent(E e) {
        synchronized (list) {
            if (list.contains(e)) {
                return;
            }
            list.add(e);
        }
    }
}

/**
 * 最好的方式,通过自身的内置锁增加了一层额外加锁，将底层的List实现封装了起来，用了一致的加锁策略
 *
 */
abstract class ImprovedList<E> implements List<E> {
    private final List<E> list;

    public ImprovedList(List<E> list) {
        this.list = list;
    }

    public synchronized void addIfAbsent(E e) {
        if (list.contains(e)) {
            return;
        }
        list.add(e);
    }
}

// 最后文档化说明那些类是线程安全的，如果需要客户端加锁，你要指出应该加的是什么锁。不要让客户去猜
