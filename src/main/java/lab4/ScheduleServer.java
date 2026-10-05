package lab4;

/*
Разработать приложение, в котором серверная часть хранит информацию о расписании занятий студентов.
Клиентская часть имеет возможность просматривать, редактировать и удалять необходимую информацию.
 */

import lab4.classes.Lesson;
import lab4.classes.Schedule;

import java.io.*;
import java.net.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ScheduleServer {
    private final int port;
    private final Map<String, Schedule> schedules = new ConcurrentHashMap<>();
    private final AtomicInteger lessonIdGen = new AtomicInteger(1);

    public ScheduleServer(int port) {
        this.port = port;
        initMockData();
    }

    private void initMockData() {
        Schedule s1 = new Schedule("ИТ-21");
        s1.addLesson(new Lesson(lessonIdGen.getAndIncrement(), "ПН", "ООП", "09:00"));
        s1.addLesson(new Lesson(lessonIdGen.getAndIncrement(), "ВТ", "Базы данных", "10:45"));
        schedules.put(s1.getGroupName(), s1);
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер запущен на порту " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("Ошибка сервера: " + e.getMessage());
        }
    }

    private void handleClient(Socket socket) {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String line;
            while ((line = in.readLine()) != null) {
                String response = processCommand(line.trim());
                out.println(response);
                out.println("---END---");
                if ("exit".equalsIgnoreCase(line.trim())) {
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Клиент отключился: " + socket.getRemoteSocketAddress());
        } finally {
            try {
                socket.close();
            } catch (IOException ignored) {}
        }
    }

    private synchronized String processCommand(String input) {
        if (input.isEmpty()) return "Пустая команда";

        String[] parts = input.split(" ");
        String cmd = parts[0].toLowerCase();

        return switch (cmd) {
            case "groups" -> schedules.isEmpty() ? "Групп нет" : "Группы: " + String.join(", ", schedules.keySet());

            case "show" -> {
                if (parts.length < 2) {
                    yield "Формат: show <group> [day]";
                }
                String group = parts[1];
                Schedule sch = schedules.get(group);
                if (sch == null) {
                    yield "Группа не найдена: " + group;
                }

                if (parts.length == 2) {
                    yield sch.toString();
                } else {
                    String day = parts[2].toUpperCase();
                    var filtered = sch.getLessons().stream()
                            .filter(l -> l.getDay().equalsIgnoreCase(day))
                            .map(Lesson::toString)
                            .collect(Collectors.joining("\n  "));
                    yield filtered.isEmpty() ? "На день " + day + " занятий нет." : "Расписание " + group + " (" + day + "):\n  " + filtered;
                }
            }

            case "add" -> {
                // Формат: add <group> <day> <time> <subject>
                if (parts.length < 5) yield "Формат: add <group> <day> <time> <subject>";
                String group = parts[1];
                String day = parts[2].toUpperCase();
                String time = parts[3];

                StringBuilder subj = new StringBuilder();
                for (int i = 4; i < parts.length; i++) {
                    subj.append(parts[i]).append(" ");
                }

                Schedule sch = schedules.computeIfAbsent(group, Schedule::new);
                Lesson l = new Lesson(lessonIdGen.getAndIncrement(), day, subj.toString().trim(), time);
                sch.addLesson(l);
                yield "Успешно добавлено: " + l;
            }

            case "del" -> {
                // Формат: del <group> <day> <lesson_id>
                if (parts.length < 3) {
                    yield "Формат: del <group> <lesson_id>";
                }
                String group = parts[1];
                Schedule sch = schedules.get(group);
                if (sch == null) yield "Группа не найдена.";
                try {
                    int id = Integer.parseInt(parts[2]);
                    yield sch.removeLessonById(id) ? "Занятие удалено." : "Занятие с таким ID не найдено.";
                } catch (NumberFormatException e) {
                    yield "ID должен быть целым числом.";
                }
            }

            case "exit" -> "Завершение соединения.";

            default -> "Неизвестная команда. Доступны: groups, show, add, del, exit";
        };
    }

    public static void main(String[] args) {
        new ScheduleServer(8080).start();
    }
}