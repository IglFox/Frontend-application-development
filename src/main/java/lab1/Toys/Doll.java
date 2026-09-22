package lab1.Toys;

public class Doll extends Toy {
    public Doll(String name, int price, int minAge, int maxAge) {
        super(name, price, minAge, maxAge);
    }

    public Doll(int price, int minAge, int maxAge) {
        super("Кукла", price, minAge, maxAge);
    }

    public Doll(int price) {
        super("Кукла", price, 3, 9);
    }

}
