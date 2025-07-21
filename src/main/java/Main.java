import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        CommandCalculator calc = new CommandCalculator();

// Генерируем выражение: ((((...((((pi + e))))...)))) + ((((...((((pi + e))))...))))
        String subExpr = "(".repeat(30000) + "pi + e" + ")".repeat(30000);
        String bigExpr = subExpr + " + " + subExpr + "+" + subExpr + " + " + subExpr;

// Пробуем вычислить
        System.out.println(calc.input(bigExpr));
    }
}
