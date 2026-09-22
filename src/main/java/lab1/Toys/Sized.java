package lab1.Toys;

public interface Sized {
    public static final int addPrice = 350;

    static int calculatePrice(int price, ToySize size) {
        if (price < 0) {
            throw new IllegalArgumentException(
                    "Цена не может быть отрицательной"
            );
        }

        return price + addPrice * size.ordinal();
    }

    public ToySize getSize();
}
