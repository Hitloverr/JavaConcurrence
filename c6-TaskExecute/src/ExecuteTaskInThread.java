import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 清晰的任务边界【客户请求】 + 明确的任务执行策略。
 */
public class ExecuteTaskInThread {

    // 串行化执行，会阻塞
    public void serialzeTask() throws IOException {
        ServerSocket serverSocket = new ServerSocket(50);
        while(true) {
            Socket connection = serverSocket.accept();
            //handleSomeThing(connection);
        }
    }

    // 为每个请求创建一个线程【不要这样做】
    /*
    1. 线程生命周期的开销很高
    2. 资源消耗，如果线程数 > cpu核数，那就没用了，还会竞争。
    3. 稳定性 OOM，
     */
    public void execute2() throws IOException {
        ServerSocket serverSocket = new ServerSocket(80);
        while(true) {
            Socket connection = serverSocket.accept();
            Runnable r = new Runnable(){
                @Override
                public void run() {
                    // 这段代码必须是线程安全的
//                    handleSomething(connection);
                }
            };
            new Thread(r).start();
        }
    }


}
