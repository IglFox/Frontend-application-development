package lab1.Toys;

public abstract class Toy {
    private int price;
    private final int minAge;
    private final int maxAge;
    private final String name;

    protected Toy(String name, int price, int minAge, int maxAge) {
        this.name = name;
        setPrice(price);
        this.minAge = minAge;
        this.maxAge = maxAge;
    }

    public boolean isSuit(int age) {
        return age >= minAge && age <= maxAge;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        if (price < 0) {
            throw new IllegalArgumentException("Цена не может быть отрицательной");
        }

        this.price = price;
    }

    public int getMinAge() {
        return minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return "%s: цена: %d руб."
                .formatted(
                        getName(),
                        getPrice()
                );
    }
}