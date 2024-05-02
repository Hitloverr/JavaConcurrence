import java.util.HashMap;
import java.util.Map;

public class ObjectPublish {
    // 最简单的发布一个对象的方式；
    public static Map<String,String> maps = new HashMap<>();

    // 发布一个引用是什么危险的，因为你可以随意改动它要指向的东西。

    // 发布一个内部的类实例，由于内部类隐含包含了外部类实例的引用，所以也相当于发布了外部类的实例。


    /*
    * 不要在构造过程中让this引用逸出。确保其他线程不会再构造函数执行完之前调用它。
    * 1. 构造函数中启动一个线程，导致this引用被新创建的线程共享
    * 2. 构造函数中调用一个可改写的实例方法（非private 非final），导致this引用在构造过程中逸出。
    *
    * 如果想要在构造函数中注册一个事件监听器或者启动线程，可以使用一个私有的构造器 + 公共的工厂方法。从而避免不正确的构造过程
    * */
//    public static Object getObject(){
//        Object obj = new Object();
//        obj.setEventListener();
//        return obj;
//    }

}

