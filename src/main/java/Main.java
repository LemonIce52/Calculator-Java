import calculator.CommandCalculator;

public class Main {

    public static void main(String[] args) {
        CommandCalculator calc = new CommandCalculator();
        String e = "1";
        for (int i = 0; i < 10000; i++) {
            e = "(" + e + " + 1)";
        }
        String e2 = "1";
        for (int i = 0; i < 30000; i++) {
            e2 = "(" + e2 + " + 1)";
        }
        String[] expr = {"log10(1000) + pow(2, 3 ^ 2) + abs(-10) * sin(1)",
        "abs(floor(ceil(sqrt(exp(2 + log(100)))))) + max(1, min(3, 2))", e,
        "-2 ^ 2 + (-2) ^ 2 + log10(100) + sqrt(16)", "max(pow(2, 4), min(10, sqrt(144))) + floor(3.9) + ceil(2.1)", e2
        };

// прогрев JVM
        for (int i = 0; i < 100000; i++) {
            calc.input(expr[0]);
        }

// измерение времени
        for (String ex: expr) {
            System.out.println(ex);
            long start = System.nanoTime();
            double result = calc.input(ex);
            long end = System.nanoTime();

            System.out.println("Result: " + result);
            System.out.println("Time: " + (end - start) / 1_000_000.0 + " ms");
            System.out.println();
        }
    }
}
