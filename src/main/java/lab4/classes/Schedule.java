package lab4.classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Schedule implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String groupName;
    private final ArrayList<Lesson> lessons = new ArrayList<>();

    public Schedule(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void addLesson(Lesson lesson) {
        lessons.add(lesson);
    }

    public boolean removeLessonById(int id) {
        return lessons.removeIf(l -> l.getId() == id);
    }

    @Override
    public String toString() {
        if (lessons.isEmpty()) {
            return "Группа " + groupName + ": занятий нет.";
        }
        StringBuilder sb = new StringBuilder("Расписание группы ").append(groupName).append(":\n");
        for (Lesson lesson : lessons) {
            sb.append("  ").append(lesson).append("\n");
        }
        return sb.toString();
    }
}