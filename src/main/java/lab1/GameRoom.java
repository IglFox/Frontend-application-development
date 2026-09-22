package lab1;
import lab1.Toys.*;

import java.util.ArrayList;
import java.util.Comparator;

public class GameRoom {
    private final int maxToyCount;
    private final int budget;
    private final ArrayList<Toy> toys = new ArrayList<Toy>();

    public GameRoom(int maxToyCount, int budget) {
        this.maxToyCount = maxToyCount;
        this.budget = budget;
    }

    public boolean addToy(Toy toy) {
        if (toys.size() >= maxToyCount) {
            System.out.println("Нельзя добавить: достигнуто максимальное количество игрушек.");
            return false;
        }

        if (getTotalPrice() + toy.getPrice() > budget) {
            System.out.println("Нельзя добавить: бюджет будет превышен.");
            return false;
        }

        toys.add(toy);
        return true;
    }

    public int getTotalPrice() {
        int result = 0;
        for (Toy toy: toys) {
            result += toy.getPrice();
        }

        return result;
    }

    public void sortByPrice() {
        toys.sort(Comparator.comparingInt(Toy::getPrice));
    }

    public ArrayList<Toy> findByAgeRange(int Age) {
        ArrayList<Toy> resultArray = new ArrayList<Toy>();
        for (Toy toy: toys) {
            if (toy.isSuit(Age)) {
                resultArray.add(toy);
            }
        }

        return resultArray;
    }

    public ArrayList<Toy> getToys() {
        return toys;
    }

    @Override
    public String toString() {
        return "Игрушек: %d/%d, сумма: %d/%d руб."
                .formatted(toys.size(), maxToyCount, getTotalPrice(), budget);
    }
}