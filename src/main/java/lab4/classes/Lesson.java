package lab4.classes;

import java.io.Serializable;

public class Lesson implements Serializable {

    private int id;
    private String subject;
    private String time;

    public Lesson() {
    }

    public Lesson(int id, String subject, String time) {
        this.id = id;
        this.subject = subject;
        this.time = time;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    @Override
    public String toString() {
        return "[ID:%d] Предмет: %s | Время: %s\n".formatted(id, subject, time);
    }
}