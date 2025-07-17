import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        CommandCalculator calc = new CommandCalculator();

        System.out.println(calc.input("2 + (2 * 2) + (1 + 0)"));
    }
}
