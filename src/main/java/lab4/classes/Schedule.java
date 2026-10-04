package lab4.classes;


import java.io.Serializable;
import java.util.ArrayList;

public class Schedule implements Serializable {
    private String groupName;
    private ArrayList<Lesson> lessons;

    public ArrayList<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(ArrayList<Lesson> lessons) {
        this.lessons = lessons;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Группа: %s | Всего предметов: %d |\n".formatted(groupName, lessons.size()));
        for (Lesson lesson: lessons) {
            sb.append(lesson.toString());
        }
        return sb.toString();
    }

}




