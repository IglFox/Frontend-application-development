package lab2;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


/*
Ввести строки из файла, записать в список ArrayList. Выполнить сортировку строк, используя метод sort() из класса Collections.
 */


public class Main {
    public static void main(String[] args) {
        Path path = Path.of(IO.readln("Введите имя файла:\n-> "));
        ArrayList<String> file_content = new ArrayList<>();

        try {
            file_content = new ArrayList<>(Files.readAllLines(path, StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.out.println("Файл по пути: " + path + " не найден.");
            System.exit(0);
        }

        ArrayList<String> start_content = new ArrayList<>(file_content);

        Collections.sort(file_content);
        ArrayList<String> simple_sort_content = new ArrayList<>(file_content);

        Collections.sort(file_content, Comparator.comparingInt(String::length));
        ArrayList<String> length_sort_content = new ArrayList<>(file_content);

        String name = IO.readln("Введите имя файла для записи:\n-> ");
        try {
            Files.write(Path.of(name + "_start.txt"), start_content, StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW);
            Files.write(Path.of(name + "_simple_sort.txt"), simple_sort_content, StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW);
            Files.write(Path.of(name + "_length_sort.txt"), length_sort_content, StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW);

            System.out.println("Успешно созданы 3 файла.");
        } catch (IOException e) {
            System.out.println("Во время записи массивов в файл" + name + " произошла ошибка");
        }
    }
}


