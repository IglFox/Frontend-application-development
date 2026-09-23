import java.util.HashMap;
import java.util.Map.Entry;

public class CollectionTest {
    public static void main(String[] args) {
        String txt = " лабораторная работа ";
        HashMap<Character, Integer> map = new HashMap<Character, Integer>(40);

        for (int i = 0; i < txt.length(); ++i) {
            char c = txt.charAt(i);

            if (Character.isLetter(c)) {
                if (map.containsKey(c)) {
                    map.put(c, map.get(c) + 1);
                } else {
                    map.put(c, 1);
                }
            }

        }
        for (Entry<Character, Integer> entry : map.entrySet()) {
            System.out.println("буква: " + entry.getKey()+" количество: " + entry.getValue());
        }

    }
}