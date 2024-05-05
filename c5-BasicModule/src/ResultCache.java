import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

public class ResultCache {
}

interface Computable<A,V> {
    V compute(A args) throws ExecutionException, InterruptedException;
}
class MemoCache<A,V> implements Computable<A,V> {
    private final Map<A,V> cache = new HashMap<>();

    private Computable<A,V> c;


//    // 1.并发性很垃圾
//    @Override
//    public synchronized V compute(A args) {
//        V result = cache.get(args);
//        if (result == null) {
//            result = c.compute(args);
//            cache.put(args,result);
//        }
//        return result;
//    }

    private final Map<A,V> cache2 = new ConcurrentHashMap<>();


    // 2. 并发性好，但是可能重复计算。
//    @Override
//    public V compute(A args) {
//        V result = cache2.get(args);
//        if (result == null) {
//            result = c.compute(args);
//            cache.put(args, result);
//        }
//        return result;
//    }
    private final Map<A, Future<V>> cache3 = new ConcurrentHashMap<>();
    // 这个好,但仍然可能计算两次。
    @Override
    public V compute(A args) throws ExecutionException, InterruptedException {
        while(true) {
            Future<V> f = cache3.get(args);

            if (f == null) {
                Callable<V> eval = new Callable<V>() {
                    @Override
                    public V call() throws Exception {
                        return c.compute(args);
                    }
                };
                FutureTask<V> ft = new FutureTask<>(eval);
//            f = ft;
//            cache3.put(args,ft);
                // 检查并执行的原子操作。
                f = cache3.putIfAbsent(args, ft);
                if (f == null) {
                    f = ft;
                    ft.run();
                }
            }
            try {
                return f.get();
            } catch (Exception e) {
                // 计算被取消，那么就移除Future，这样未来的Future才能计算成功。
                cache3.remove(args);
            }
        }
    }
}


// 最后，缓存逾期，可以用FutureTask的子类，为每个结果指定过期时间，定期扫描过期元素。
