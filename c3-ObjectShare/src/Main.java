public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }
}

// 上一章临界区，原子性。这一章讨论可见性。同步。


// 下面这个例子可能出现的情况下，ReaderThread一直在无限循环（可见性）；也可能打印出来number的值是0（指令重排）
class NoVisible {
    private static boolean isReady;

    private static int number;

    private static class ReaderThread extends Thread {
        @Override
        public void run() {
            while (!isReady) {
                Thread.yield();
            }
            System.out.println(number);
        }
    }

    public static void main(String[] args) {
        new ReaderThread().start();
        number = 148;
        isReady = true;
    }
}

// 避免一个线程对这个对象赋值了，另外一个线程读到的值是错的
class SynchronizedInterger {
    private int number;

    public synchronized int getNumber() {
        return number;
    }

    public synchronized void setNumber(int number) {
        this.number = number;
    }

}

// 最低安全性：虽然读到的是失效值，但至少读的这个值是完全由之前某个线程设置的。但是不适用于long和double。64位的操作可以分解成两个32位的操作==》可能读取到某个值的高32
// 位 和 另外一个值的低32位。。。因此对于多线程共享且可变的long 和 double，最好用volatile或者锁保护。


// 加锁（并且不同线程用的是同一把锁），能保证数据在不同线程的可见性。==》 原子性、互斥性、可见性都满足。
