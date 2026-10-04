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

public class StopwatchManager {
    final private String HELP_TXT = "Команды:\n" +
            "- start N – запустить секундомер и дать ему идентификатор N;\n" +
            "- stop N – остановить секундомер с идентификатором N;\n" +
            "- reset N – сбросить время у секундомера с идентификатором N;\n" +
            "- time N – показать время у секундомера с идентификатором N;\n" +
            "- help – список команд;\n" +
            "- timers - список секундомеров\n" +
            "- exit – выход.";

    private HashMap<Integer, Stopwatch> instances = new HashMap<>();

    public void getInstances() {
        instances.forEach(
            (id, stopwatch) -> {
                IO.println("- Секундомер[ID:%d]: %dms".formatted(id, stopwatch.getTime()));
        });
    }

    public void start() {
        IO.println(HELP_TXT);

        String input_raw, input_command;

        do {
            int input_digit = -1;
            input_raw = IO.readln("-> ");
            String[] input_array = input_raw.trim().split(" ");
            try {
                input_digit = Integer.parseInt(input_array[1]);
            } catch (ArrayIndexOutOfBoundsException | NumberFormatException _) {

            } finally {
                input_command = input_array[0];
                command_route(input_command, input_digit);
            }
        } while (!input_raw.equals("exit"));

    }

    private void command_route(String command, int N) {
        if ( instances.isEmpty() && "stoptimersreset".contains(command)) {
            IO.println(
                "Пока что нет активных секундомеров."
            );
            return;
        } else if ( N == -1 && "stoptimeresetstart".contains(command) ) {
            IO.println(
                "Правильно укажите номер секундомера в виде целового числа."
            );
            return;
        }

        switch (command) {
            case "start" -> {
                if (instances.containsKey(N)) {
                    IO.println(
                        "Секундомер с ID:%d уже создан. Текущее время: %dms.".formatted(
                            N, instances.get(N).getTime()
                        )
                    );
                } else {
                    Stopwatch sw = new Stopwatch();
                    sw.startTimer();
                    instances.put(N, sw);
                    IO.println("Секундомер с ID:%d создан.".formatted(N));
                }
            }

            case "stop" -> {
                if (instances.containsKey(N)) {
                    instances.get(N).stopTimer();
                    IO.println(
                        "Секундомер с ID:%d остановлен. Прошло времени: %dms.".formatted(
                                N, instances.get(N).getTime()
                        )
                    );
                    instances.remove(N);
                } else {
                    IO.println(
                        "Секундомера с ID:%d не существует.".formatted(
                            N
                        )
                    );
                }
            }

            case "reset" -> {
                if (instances.containsKey(N)) {
                    instances.get(N).resetTimer();
                    IO.println(
                        "Секундомер с ID:%d перезапущен.".formatted(
                            N
                        )
                    );
                } else {
                    IO.println(
                            "Секундомера с ID:%d не существует.".formatted(
                                    N
                            )
                    );
                }
            }

            case "time" -> {
                if (instances.containsKey(N)) {
                    IO.println(
                            "Секундомер[ID:%d]: %dms.".formatted(
                                    N, instances.get(N).getTime()
                            )
                    );
                } else {
                    IO.println(
                        "Секундомера с ID:%d не существует.".formatted(
                                N
                        )
                    );
                }
            }

            case "help", " " -> {
                IO.println(HELP_TXT);
            }

            case "exit" -> {
                System.exit(0);
            }

            case "timers" -> {
                getInstances();
            }
        }
    }
}


