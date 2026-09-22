package lab1.Toys;

public class Ball extends Toy {
    public Ball(String name, int price, int minAge, int maxAge) {
        super(name, price, minAge, maxAge);
    }
    public Ball(int price, int minAge, int maxAge) {
        super("Мячик", price, minAge, maxAge);
    }
    public Ball(String name, int price) {
        super(name, price, 0, 18);
    }
    public Ball(int price) {
        super("Мячик", price, 0, 4);
    }
}
