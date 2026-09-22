package lab1.Toys;

public class Cube extends Toy implements Sized {
    private final ToySize size;

    public Cube(String name, int price, int minAge, int maxAge, ToySize size) {
        super(name, Sized.calculatePrice(price, size), minAge, maxAge);
        this.size = size;
    }

    public Cube(String name, int price, int minAge, int maxAge) {
        super(name, Sized.calculatePrice(price, ToySize.MEDIUM), minAge, maxAge);
        this.size = ToySize.MEDIUM;
    }

    public Cube(int price, int minAge, int maxAge) {
        super("Кубик", Sized.calculatePrice(price, ToySize.MEDIUM), minAge, maxAge);
        this.size = ToySize.MEDIUM;
    }

    public Cube(int price) {
        super("Кубик", Sized.calculatePrice(price, ToySize.MEDIUM), 0, 4);
        this.size = ToySize.MEDIUM;
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
    public ToySize getSize() {
        return size;
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
