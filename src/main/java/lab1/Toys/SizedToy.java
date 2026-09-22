package lab1.Toys;

public abstract class SizedToy extends Toy implements Sized {
    private final ToySize size;

    protected SizedToy(String name, int price, int minAge, int maxAge, ToySize size) {
        super(name, Sized.calculatePrice(price, size), minAge, maxAge);
        this.size = size;
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
}
