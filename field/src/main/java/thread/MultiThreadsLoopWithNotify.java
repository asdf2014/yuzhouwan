package thread;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function：thread
 *
 * @author jinjy
 * @since 2016/1/21 0021
 */
public class MultiThreadsLoopWithNotify {


    public static void main(String[] args) {

        LockHolder lockHolder = new LockHolder("goahead");
        Reporter reporter1 = new Reporter(lockHolder);
//        Reporter reporter2 = new Reporter(lockHolder);
        Leader leader = new Leader(lockHolder);
        Thread t1 = new Thread(reporter1);
//        Thread t2 = new Thread(reporter2);
        Thread t3 = new Thread(leader);

        t1.start();
//        t2.start();
        t3.start();
    }

    static class Leader implements Runnable {

        private volatile LockHolder lockHolder;

        public Leader(LockHolder lockHolder) {
            this.lockHolder = lockHolder;
        }

        @Override
        public void run() {
            while (true) {
                String threadName = Thread.currentThread().getName();
                try {
                    Thread.sleep(3000);
                    synchronized (lockHolder) {
                        lockHolder.notify();
                        System.out.println("Thread: [".concat(threadName).concat("] sent the message is ").concat(lockHolder.getHolder()).concat("."));
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Reporter implements Runnable {

        private volatile LockHolder lockHolder;

        public Reporter(LockHolder lockHolder) {
            this.lockHolder = lockHolder;
        }

        @Override
        public void run() {
            while (true) {
                String threadName = Thread.currentThread().getName();
                try {
                    Thread.sleep(1000);
                    synchronized (lockHolder) {

                        System.out.println("Thread: [".concat(threadName).concat("] is waiting the message..."));
                        lockHolder.wait();
                        System.out.println("Thread: [".concat(threadName).concat("] got the message is").concat(lockHolder.getHolder()).concat("."));
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class LockHolder {
        private String holder;

        public LockHolder(String holder) {
            this.holder = holder;
        }

        public String getHolder() {
            return holder;
        }
    }

}


