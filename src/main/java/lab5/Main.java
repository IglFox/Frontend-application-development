package lab5;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final TestResultRepository repository = new TestResultRepository();

    public static void main(String[] args) {
        repository.initDatabase();
        printMenu();

        boolean running = true;
        while (running) {
            System.out.print("\nВведите команду (? для меню): ");
            String choice = scanner.nextLine().trim().toLowerCase();

            switch (choice) {
                case "?", "h", "help" -> printMenu();
                case "1" -> showAll();
                case "2" -> addSingle();
                case "3" -> addBatch();
                case "4" -> findById();
                case "5" -> findBySubject();
                case "6" -> showPassed();
                case "7" -> showAverage();
                case "8" -> updateScore();
                case "9" -> deleteById();
                case "10" -> repository.printMetadata();
                case "11" -> dropTable();
                case "12" -> repository.initDatabase();
                case "0" -> {
                    System.out.println("Выход из программы.");
                    running = false;
                }
                default -> System.out.println("Неизвестная команда. Введите ? или help для списка команд.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n========== СИСТЕМА УЧЕТА РЕЗУЛЬТАТОВ ТЕСТИРОВАНИЯ ==========");
        System.out.println("1.  Показать все результаты");
        System.out.println("2.  Добавить результат теста");
        System.out.println("3.  Пакетное добавление результатов (Batch)");
        System.out.println("4.  Найти результат по ID");
        System.out.println("5.  Найти результаты по предмету");
        System.out.println("6.  Показать только сдавших студентов (балл >= 60)");
        System.out.println("7.  Рассчитать средний балл по предмету");
        System.out.println("8.  Изменить балл студента (пересдача)");
        System.out.println("9.  Удалить запись по ID");
        System.out.println("10. Показать метаданные БД и таблицы");
        System.out.println("11. Удалить таблицу (DROP TABLE)");
        System.out.println("12. Создать / восстановить таблицу (CREATE TABLE)");
        System.out.println("?, h, help — показать это меню");
        System.out.println("0.  Выход");
        System.out.println("============================================================");
    }

    private static void showAll() {
        List<TestResult> list = repository.findAll();
        printTable(list);
    }

    private static void addSingle() {
        System.out.println("\n--- Добавление результата теста ---");
        TestResult result = inputTestResult();
        TestResult saved = repository.save(result);
        System.out.println("Успешно сохранено! Присвоен ID: " + saved.id());
    }

    private static void addBatch() {
        System.out.println("\n--- Пакетное добавление результатов (Batch) ---");
        int count = readInt("Сколько записей вы хотите ввести в пакете: ");
        if (count <= 0) {
            System.out.println("Количество должно быть больше 0.");
            return;
        }

        List<TestResult> batch = new ArrayList<>(count);
        for (int i = 1; i <= count; i++) {
            System.out.println("\nВвод записи #" + i + " из " + count + ":");
            batch.add(inputTestResult());
        }

        repository.saveAll(batch);
    }

    private static TestResult inputTestResult() {
        System.out.print("ФИО студента: ");
        String name = scanner.nextLine().trim();

        System.out.print("Название дисциплины: ");
        String title = scanner.nextLine().trim();

        int score = readInt("Количество баллов (0-100): ");

        System.out.print("Дата (ГГГГ-ММ-ДД, Enter - сегодня): ");
        String dateStr = scanner.nextLine().trim();
        LocalDate date = parseDateOrDefault(dateStr);

        return new TestResult(name, title, score, date);
    }

    private static void findById() {
        int id = readInt("\nВведите ID для поиска: ");
        repository.findById(id).ifPresentOrElse(
                r -> printTable(List.of(r)),
                () -> System.out.println("Запись с ID " + id + " не найдена.")
        );
    }

    private static void findBySubject() {
        System.out.print("\nВведите название предмета: ");
        String subject = scanner.nextLine().trim();
        List<TestResult> list = repository.findByTitle(subject);
        printTable(list);
    }

    private static void showPassed() {
        System.out.println("\n--- Студенты, успешно сдавшие тестирование ---");
        List<TestResult> list = repository.findPassed();
        printTable(list);
    }

    private static void showAverage() {
        System.out.print("\nВведите название предмета для расчета среднего балла: ");
        String subject = scanner.nextLine().trim();
        double avg = repository.getAverageScoreByTitle(subject);
        System.out.printf("Средний балл по дисциплине '%s': %.2f%n", subject, avg);
    }

    private static void updateScore() {
        int id = readInt("\nВведите ID записи для обновления: ");
        if (repository.findById(id).isEmpty()) {
            System.out.println("Запись с ID " + id + " не существует.");
            return;
        }

        int newScore = readInt("Введите новый балл: ");
        if (repository.updateScore(id, newScore)) {
            System.out.println("Оценка успешно обновлена!");
            repository.findById(id).ifPresent(r -> printTable(List.of(r)));
        } else {
            System.out.println("Не удалось обновить запись.");
        }
    }

    private static void deleteById() {
        int id = readInt("\nВведите ID записи для удаления: ");
        if (repository.deleteById(id)) {
            System.out.println("Запись с ID " + id + " успешно удалена.");
        } else {
            System.out.println("Запись с ID " + id + " не найдена.");
        }
    }

    private static void dropTable() {
        System.out.print("\nВы уверены, что хотите удалить таблицу 'test_results'? (y/n): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        if (confirm.equals("y") || confirm.equals("yes") || confirm.equals("да")) {
            repository.dropTable();
        } else {
            System.out.println("Удаление отменено.");
        }
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите целое число.");
            }
        }
    }

    private static LocalDate parseDateOrDefault(String input) {
        if (input.isEmpty()) {
            return LocalDate.now();
        }
        try {
            return LocalDate.parse(input);
        } catch (DateTimeParseException e) {
            System.out.println("Неверный формат даты, установлена сегодняшняя дата.");
            return LocalDate.now();
        }
    }

    private static void printTable(List<TestResult> list) {
        if (list.isEmpty()) {
            System.out.println("(Записей не найдено)");
            return;
        }

        System.out.println("+----+----------------------+---------------------+-------+--------+------------+");
        System.out.println("| ID | ФИО Студента         | Дисциплина          | Балл  | Статус | Дата       |");
        System.out.println("+----+----------------------+---------------------+-------+--------+------------+");
        for (TestResult r : list) {
            System.out.printf("| %-2d | %-20s | %-19s | %-5d | %-6s | %-10s |%n",
                    r.id(),
                    r.studentName(),
                    r.testTitle(),
                    r.score(),
                    r.passed() ? "СДАЛ" : "НЕ СДАЛ",
                    r.testDate()
            );
        }
        System.out.println("+----+----------------------+---------------------+-------+--------+------------+");
    }
}
