package lab3;


/*
14 Написать секундомер – класс Stopwatch – для замера времени в отдельном потоке выполнения. В классе должны быть реализованы следующие методы:
- start – начинает отсчет времени;
- stop – прерывает отсчет времени;
- reset – сбрасывает текущее значение секундомера;
- getTime – возвращает отсчитанное время в миллисекундах.
Для демонстрации работы секундомера написать консольное приложение.
 */

public class Stopwatch implements Runnable {
    private volatile boolean running = false;
    private volatile long elapsedTime = 0L;
    private Thread workerThread;

    @Override
    public void run() {
        long lastTime = System.currentTimeMillis();
        while (running) {
            try {
                Thread.sleep(50);
                long now = System.currentTimeMillis();
                elapsedTime += (now - lastTime);
                lastTime = now;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public synchronized void start() {
        if (!running) {
            running = true;
            workerThread = new Thread(this);
            workerThread.setDaemon(true);
            workerThread.start();
        }
    }

    public synchronized void stop() {
        if (running) {
            running = false;
            if (workerThread != null) {
                workerThread.interrupt();
            }
        }
    }

    public synchronized void reset() {
        stop();
        elapsedTime = 0L;
    }

    public long getTime() {
        return elapsedTime;
    }
}
