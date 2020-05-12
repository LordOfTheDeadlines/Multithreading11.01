/**
 * В чем заключалась проблема:
 * один из потоков не заканчивает работу с общим ресурсом,
 * а система переключается на другой поток, использующий этот же ресурс
 * Как решить проблему:
 * нужно сделать так, чтобы ресурс использовался одним потоком одновременно
 */

public class Lucky {
    private static int x = 0;
    private static int count = 0;

    static class LuckyThread extends Thread {
        @Override
        public void run() {
            while (true) {
                int copyX;//лучше создать копию эл-та и обеспечить ее целостность, чем синхронизировать цикл
                synchronized (Lucky.class) {
                    if (x < 999999) copyX = x++;
                    else break;
                }
                if ((copyX % 10) + (copyX / 10) % 10 + (copyX / 100) % 10 == (copyX / 1000)
                        % 10 + (copyX / 10000) % 10 + (copyX / 100000) % 10) {
                    System.out.println(copyX);
                    synchronized (Lucky.class) {
                        count++;
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new LuckyThread();
        Thread t2 = new LuckyThread();
        Thread t3 = new LuckyThread();
        t1.start();
        t2.start();
        t3.start();
        t1.join();
        t2.join();
        t3.join();
        System.out.println("Total: " + count);
    }
}