package lab3;


/*
14 Написать секундомер – класс Stopwatch – для замера времени в отдельном потоке выполнения. В классе должны быть реализованы следующие методы:
- start – начинает отсчет времени;
- stop – прерывает отсчет времени;
- reset – сбрасывает текущее значение секундомера;
- getTime – возвращает отсчитанное время в миллисекундах.
Для демонстрации работы секундомера написать консольное приложение.
 */



public class Stopwatch extends Thread {
    private boolean active = false;
    private long startTime = 0L;
    private long elapsedTime = 0L;

    @Override
    public void run() {
        while (active) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public synchronized void startTimer() {
        if (!active) {
            active = true;
            startTime = System.currentTimeMillis();

            if (getState() == State.NEW) {
                super.start();
            } else {
                new Thread(this::run).start();
            }
        }
    }

    public synchronized void stopTimer() {
        if (active) {
            elapsedTime += System.currentTimeMillis() - startTime;
            active = false;
        }
    }

    public synchronized void resetTimer() {
        active = false;
        startTime = 0L;
        elapsedTime = 0L;
    }

    public synchronized long getTime() {
        if (active) {
            return elapsedTime + (System.currentTimeMillis() - startTime);
        }
        return elapsedTime;
    }
}
