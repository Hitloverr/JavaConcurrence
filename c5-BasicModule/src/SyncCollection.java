import java.util.Vector;

/**
 * 同步容器类通过加上同步操作来保证线程安全，但是对于复合操作，常见的先检查再执行等一系列操作，可能就会失去线程安全性了。
 *
 * 另外一种可能的情况就是迭代，在迭代的时候其他线程修改了容器。java的处理是Iteartor的fail-fast快速失败模式。、
 * 除了客户端加锁外，另外一种处理方式就是克隆这个容器，CopyOnWriteXXX的思想
 *
 * 警惕隐含的迭代，比如打印集合、hashCode、equals、containsAll、removeAll、retainAll这些操作都会对集合进行迭代，可能出现并发错误
 *
 * 最好将同步机制封装在类中，以免忽略。
 * */

public class SyncCollection {

    public static Object getLast(Vector last) {
        synchronized (last) {
            return last.get(last.size() - 1);
        }
    }

    public static void removeLast(Vector list) {
        synchronized (list) {
            list.remove(list.size() - 1);
        }
    }

    // 通过客户端加锁的方式保证了迭代的安全性，但是降低了并发性。
    public void iterate(Vector list) {
        synchronized (list) {
            for (int i = 0;  i < list.size() ; i++) {

            }
        }
    }
}


