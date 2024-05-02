import java.util.Arrays;

/**
 * 不可变对象天然就是线程安全的，可以安全地共享和发布，不需要创建保护性的副本
 * 1. 将域设置成final的能确保初始化过程的安全性，不受限制地访问不可变对象，并且在共享这些对象时不需要同步。
 * - 除非有必要，不然最好将所有域声明为private final的
 * 2. 一个例子就是通过使用包含多个状态变量的容器对象来维持不可变性 + volatile的引用来确保可见性 ==> 线程安全
 */
public class ImmutableClass {
}

class OneValueCache{
    private final Integer lastNumber;
    private final Integer[] lastFactors;

    public OneValueCache(Integer lastNumber,Integer[] lastFactors) {
        this.lastNumber = lastNumber;
        this.lastFactors = Arrays.copyOf(lastFactors,lastFactors.length);
    }

    public Integer[] getLastFactors(Integer i) {
        if (i == lastNumber) {
            return Arrays.copyOf(lastFactors,lastFactors.length);
        } else {
            return null;
        }
    }

    private static volatile OneValueCache cache = new OneValueCache(null,null);
    public static void main(String[] args) {
        int someI = 2;
//        Integer[] lastFactors1 = cache.getLastFactors(someI);
//        if (lastFactors1 == null) {
//            Integer[] factors = getFactors(someI);
        // 这里确保了两个变量之间一定是一致性的。相当于实现了一个原子性的操作
//            cache = new OneValueCache(someI,factors);
//        }
    }
}
