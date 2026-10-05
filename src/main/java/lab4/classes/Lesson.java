package lab4.classes;

import java.io.Serializable;

public class Lesson implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String day;
    private String subject;
    private String time;

    public Lesson(int id, String day, String subject, String time) {
        this.id = id;
        this.day = day;
        this.subject = subject;
        this.time = time;
    }

    public int getId() { return id; }
    public String getDay() { return day; }
    public String getSubject() { return subject; }
    public String getTime() { return time; }

    public void setSubject(String subject) { this.subject = subject; }
    public void setTime(String time) { this.time = time; }

    @Override
    public String toString() {
        return String.format("[ID: %d] День: %s | Предмет: %s | Время: %s", id, day, subject, time);
    }
}