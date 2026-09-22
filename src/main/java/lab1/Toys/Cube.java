package lab1.Toys;

public class Cube extends SizedToy {

    public Cube(String name, int price, int minAge, int maxAge, ToySize size) {
        super(name, price, minAge, maxAge, size);
    }

    public Cube(String name, int price, int minAge, int maxAge) {
        super(name, price, minAge, maxAge, ToySize.MEDIUM);
    }

    public Cube(int price, int minAge, int maxAge) {
        super("Кубик", price, minAge, maxAge, ToySize.MEDIUM);
    }

    public Cube(int price) {
        super("Кубик", price, 0, 4, ToySize.MEDIUM);
    }

    @Override
    public String toString() {
        return "%s: размер: %s, цена: %d руб.".formatted(
                getName(),
                getSize(),
                getPrice()
        );
    }
}
