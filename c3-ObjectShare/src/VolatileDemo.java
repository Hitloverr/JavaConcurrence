/*
volatile修饰的变量总会返回最新被写入的值，共享的变量，解决了可见性与指令重排性的问题，并不能解决原子性和同步性的问题
- 从内存可见性的角度来说，写volatile变量相当于退出同步代码块，读volatile变量相当于进入同步代码块。
* */
public class VolatileDemo {

    // 一个常见的例子,如果不设置成volatile，可能出现其他线程改了flag为true，这里还是没有发现，仍然在无限循环1

    volatile boolean flag;

    {

        while (flag) {
            Thread.yield();
        }
    }
}



