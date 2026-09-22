package lab0;
/*
Вариант 14
Ввести 3 массива, содержащих соответственно 5, 7 и 3 компонента целого типа. Поменять местами первый и последний
элементы в массивах. Четные элементы массивов заменить нулями. Вычислить количество элементов массива, принадлежащих
отрезку [1, 4]. Операции ввода - вывода массивов и формирования новых массивов, вычисление количества элементов массива,
принадлежащих отрезку [1, 4] выполнить с помощью методов.
 */

import static java.lang.IO.*;

public class Main {
    public static void main(String[] args) {
        int[] a = new int[5];
        int[] b = new int[7];
        int[] c = new int[3];

        println("--- Ввод массивов ---");
        inputArray("Массив 1 (5 эл.)", a);
        inputArray("Массив 2 (7 эл.)", b);
        inputArray("Массив 3 (3 эл.)", c);

        println("\n--- Результаты ---");
        processArray("Массив 1", a);
        processArray("Массив 2", b);
        processArray("Массив 3", c);
    }

    static void inputArray(String name, int[] arr) {
        println(name + ":");
        for (int i = 0; i < arr.length; i++) {
            arr[i] = Integer.parseInt(readln("  [" + i + "]: "));
        }
    }

    static void processArray(String name, int[] arr) {
        print(name + " (исходный): ");
        printArray(arr);

        transformArray(arr);

        print(name + " (измененный): ");
        printArray(arr);

        println("Количество в [1, 4]: " + countInRange(arr));
        println();
    }

    static void printArray(int[] arr) {
        for (int val : arr) {
            print(val + " ");
        }
        println();
    }

    static void transformArray(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] % 2 == 0) {
                arr[i] = 0;
            }
        }

        if (arr.length > 1) {
            int temp = arr[0];
            arr[0] = arr[arr.length - 1];
            arr[arr.length - 1] = temp;
        }
    }

    static int countInRange(int[] arr) {
        int count = 0;
        for (int val : arr) {
            if (val >= 1 && val <= 4) {
                count++;
            }
        }

        return count;
    }


}


