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

        LockHolder lockHolder = new LockHolder(1);
        Reporter1 reporter1 = new Reporter1(lockHolder);
        Reporter2 reporter2 = new Reporter2(lockHolder);
        Reporter3 reporter3 = new Reporter3(lockHolder);
        Thread t1 = new Thread(reporter1);
        Thread t2 = new Thread(reporter2);
        Thread t3 = new Thread(reporter3);

        t1.start();
        t2.start();
        t3.start();
    }

}

class Reporter1 implements Runnable {

    private volatile LockHolder lockHolder;

    public Reporter1(LockHolder lockHolder) {
        this.lockHolder = lockHolder;
    }

    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        try {
            while (true) {
                Thread.sleep(1000);
                synchronized (lockHolder) {
                    if (lockHolder.getHolder() == 1) {
                        System.out.println("Thread: [".concat(threadName).concat("]..."));
                        lockHolder.setHolder(2);
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

class Reporter2 implements Runnable {

    private volatile LockHolder lockHolder;

    public Reporter2(LockHolder lockHolder) {
        this.lockHolder = lockHolder;
    }

    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        try {
            while (true) {
                Thread.sleep(1000);
                synchronized (lockHolder) {
                    if (lockHolder.getHolder() == 2) {
                        System.out.println("Thread: [".concat(threadName).concat("]..."));
                        lockHolder.setHolder(3);
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

class Reporter3 implements Runnable {

    private volatile LockHolder lockHolder;

    public Reporter3(LockHolder lockHolder) {
        this.lockHolder = lockHolder;
    }

    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        try {
            while (true) {
                Thread.sleep(1000);
                synchronized (lockHolder) {
                    if (lockHolder.getHolder() == 3) {
                        System.out.println("Thread: [".concat(threadName).concat("]..."));
                        lockHolder.setHolder(1);
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



