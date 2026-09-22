package lab1.Toys;

public class Car extends Toy implements Sized {
    private final ToySize size;

    public Car(String name, int price, int minAge, int maxAge, ToySize size) {
        super(name, Sized.calculatePrice(price, size), minAge, maxAge);
        this.size = size;
    }

    public Car(String name, int price, int minAge, int maxAge) {
        super(name, price, minAge, maxAge);
        this.size = ToySize.SMALL;
    }

    public Car(int price, int minAge, int maxAge) {
        super("Машинка", price, minAge, maxAge);
        this.size = ToySize.SMALL;
    }

    public Car(int price) {
        super("Машинка", price, 2, 7);
        this.size = ToySize.SMALL;
    }

    @Override
    public ToySize getSize() {
        return size;
    }

    @Override
    public void setPrice(int price) {
        if (size == null) {
            super.setPrice(price);
        } else {
            super.setPrice(Sized.calculatePrice(price, size));
        }
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
