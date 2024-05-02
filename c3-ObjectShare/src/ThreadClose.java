import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ThreadClose {
    /**
     * 解决同步性的安全问题的一大方法就是将变量封闭在各个线程内部。
     * 1. 单线程。对于volatile变量来说，只要确保只有单个线程对他修改，就相当于修改封闭在单个线程中，防止竟态条件的发生。
     * 2. 栈封闭：常见的就是方法中的局部变量。（注意，不要让引用类型的局部变量逸出，不然安全性就没有办法保证了）
     * 3. 使用类似全局变量的ThreadLocal
     */

    private static ThreadLocal<Connection> connectionHolder = new ThreadLocal<Connection>(){
        @Override
        protected Connection initialValue() {
            try {
                return DriverManager.getConnection("");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    };

    public static Connection getConnection() {
        return connectionHolder.get();
    }
}



