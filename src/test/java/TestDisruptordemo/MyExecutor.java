package TestDisruptordemo;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;

public class MyExecutor implements Executor {
    ThreadFactory threadFactory;
    public MyExecutor(ThreadFactory threadFactory){
        this.threadFactory=threadFactory;
    }
    public void execute(Runnable command) {
        threadFactory.newThread(command).start();
    }
}
