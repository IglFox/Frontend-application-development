package lab3;

/*
Пользователю должны быть доступными следующие команды:
- start N – запустить секундомер и дать ему идентификатор N;
- stop N – остановить секундомер с идентификатором N;
- reset N – сбросить время у секундомера с идентификатором N;
- time N – показать время у секундомера с идентификатором N;
- help – список команд;
- exit – выход.
 */


import java.util.HashMap;
import java.util.Map;

public class StopwatchManager {
    private static final String HELP_TXT = """
            Команды:
            - start N  – запустить секундомер с идентификатором N;
            - stop N   – остановить секундомер с идентификатором N;
            - reset N  – сбросить время секундомера с идентификатором N;
            - time N   – показать время секундомера с идентификатором N;
            - timers   – список всех секундомеров;
            - help     – список команд;
            - exit     – выход.
            """;

    private final Map<Integer, Stopwatch> instances = new HashMap<>();

    public void showTimers() {
        if (instances.isEmpty()) {
            IO.println("Список секундомеров пуст.");
            return;
        }
        instances.forEach((id, sw) ->
            IO.println("- Секундомер [ID:%d]: %d ms".formatted(id, sw.getTime()))
        );
    }

    public void start() {
        IO.println(HELP_TXT);

        while (true) {
            String rawLine = IO.readln("-> ");
            if (rawLine == null) {
                break;
            }

            String trimmed = rawLine.trim();
            if (trimmed.isEmpty()) {
                continue;
            }

            String[] parts = trimmed.split(" ");
            String command = parts[0].toLowerCase();

            if (command.equals("exit")) {
                shutdownAll();
                break;
            }

            executeCommand(command, parts);
        }
    }

    private void executeCommand(String command, String[] parts) {
        switch (command) {
            case "help" -> IO.println(HELP_TXT);
            case "timers" -> showTimers();
            case "start", "stop", "reset", "time" -> handleTargetedCommand(command, parts);
            default -> IO.println("Неизвестная команда. Введите 'help' для справки.");
        }
    }

    private void handleTargetedCommand(String command, String[] parts) {
        if (parts.length < 2) {
            IO.println("Ошибка: не указан идентификатор секундомера (N). Пример: " + command + " 1");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            IO.println("Ошибка: идентификатор секундомера должен быть целым числом.");
            return;
        }

        switch (command) {
            case "start" -> {
                Stopwatch sw = instances.computeIfAbsent(id, k -> new Stopwatch());
                sw.start();
                IO.println("Секундомер [ID:%d] запущен. Время: %d ms.".formatted(id, sw.getTime()));
            }
            case "stop" -> {
                Stopwatch sw = instances.get(id);
                if (sw == null) {
                    IO.println("Секундомер с ID:%d не найден.".formatted(id));
                } else {
                    sw.stop();
                    IO.println("Секундомер [ID:%d] остановлен. Итоговое время: %d ms.".formatted(id, sw.getTime()));
                }
            }
            case "reset" -> {
                Stopwatch sw = instances.get(id);
                if (sw == null) {
                    IO.println("Секундомер с ID:%d не найден.".formatted(id));
                } else {
                    sw.reset();
                    IO.println("Секундомер [ID:%d] сброшен.".formatted(id));
                }
            }
            case "time" -> {
                Stopwatch sw = instances.get(id);
                if (sw == null) {
                    IO.println("Секундомер с ID:%d не найден.".formatted(id));
                } else {
                    IO.println("Секундомер [ID:%d]: %d ms.".formatted(id, sw.getTime()));
                }
            }
        }
    }

    private void shutdownAll() {
        for (Stopwatch sw : instances.values()) {
            sw.stop();
        }
        instances.clear();
    }
}