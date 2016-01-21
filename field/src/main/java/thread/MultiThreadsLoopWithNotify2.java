package thread;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function：thread
 *
 * @author jinjy
 * @since 2016/1/21 0021
 */
public class MultiThreadsLoopWithNotify2 {

    public static void main(String[] args) {

        LockHolder lockHolder = new LockHolder(0);
        int threadSize = 3;
        Reporter reporter1 = new Reporter(lockHolder, 0, threadSize);
        Reporter reporter2 = new Reporter(lockHolder, 1, threadSize);
        Reporter reporter3 = new Reporter(lockHolder, 2, threadSize);
        Thread t1 = new Thread(reporter1);
        Thread t2 = new Thread(reporter2);
        Thread t3 = new Thread(reporter3);

        t1.start();
        t2.start();
        t3.start();
    }

}

class Reporter implements Runnable {

    private volatile LockHolder lockHolder;
    private int number;
    private int threadSize;

    public Reporter(LockHolder lockHolder, int number, int threadSize) {
        this.lockHolder = lockHolder;
        this.number = number;
        this.threadSize = threadSize;
    }

    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        try {
            while (true) {
                Thread.sleep(1000);
                synchronized (lockHolder) {
                    if (lockHolder.getHolder() == number) {
                        System.out.println("Thread: [".concat(threadName).concat("]..."));
                        lockHolder.setHolder((number + 1) % threadSize);
                    } else {
                        lockHolder.wait(100);
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class LockHolder {
    private volatile int holder;

    public LockHolder(int holder) {
        this.holder = holder;
    }

    public int getHolder() {
        return holder;
    }

    public void setHolder(int holder) {
        this.holder = holder;
    }
}



