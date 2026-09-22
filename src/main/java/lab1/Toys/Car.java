package lab1.Toys;

public class Car extends SizedToy {

    public Car(String name, int price, int minAge, int maxAge, ToySize size) {
        super(name, price, minAge, maxAge, size);
    }

    public Car(String name, int price, int minAge, int maxAge) {
        super(name, price, minAge, maxAge, ToySize.SMALL);
    }

    public Car(int price, int minAge, int maxAge) {
        super("Машинка", price, minAge, maxAge, ToySize.SMALL);
    }

    public Car(int price) {
        super("Машинка", price, 2, 7, ToySize.SMALL);
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
