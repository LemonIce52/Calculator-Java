import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        CommandCalculator calc = new CommandCalculator();
        String deepNesting = "(".repeat(1000) + "1 + 1" + ")".repeat(1000);


        System.out.println(calc.input(deepNesting));

        String heavyFunctions = "sin(";
        heavyFunctions += "cos(";
        heavyFunctions += "log10(";
        heavyFunctions += "sqrt(";
        heavyFunctions += "abs(";
        heavyFunctions += "1.23456789";
        heavyFunctions += ")".repeat(4);  // Закрываем abs, sqrt, log10, cos
        heavyFunctions += ")";

        System.out.println(calc.input(heavyFunctions));
    }
}
