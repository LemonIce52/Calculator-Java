package calculator;

public class MathFunctionsAndOperatorExecutor {

    public static double applyMathOperator(String op, double number) {
        return switch (op) {
            case "log" -> Math.log(number);
            case "log10" -> Math.log10(number);
            case "cos" -> Math.cos(number);
            case "sin" -> Math.sin(number);
            case "sqrt" -> Math.sqrt(number);
            case "abs" -> Math.abs(number);
            case "tan" -> Math.tan(number);
            case "exp" -> Math.exp(number);
            case "floor" -> Math.floor(number);
            case "ceil" -> Math.ceil(number);
            default -> throw new IllegalArgumentException("Invalid operator! " + op);
        };
    }

    public static double applyMathOperatorTooArguments(String op, double... numbers) {
        return switch (op) {
            case "pow" -> Math.pow(numbers[0], numbers[1]);
            case "max" -> Math.max(numbers[0], numbers[1]);
            case "min" -> Math.min(numbers[0], numbers[1]);
            default -> throw new IllegalArgumentException("Invalid operator! " + op);
        };
    }

    public static double applyOperator(String op, double firstNumber, double secondNumber) {
        return switch (op) {
            case "*" -> firstNumber * secondNumber;
            case "/" -> {
                if (secondNumber == 0)
                    throw new IllegalArgumentException("Division by zero!");
                yield firstNumber / secondNumber;
            }
            case "+" -> firstNumber + secondNumber;
            case "-" -> firstNumber - secondNumber;
            case "^" -> Math.pow(firstNumber, secondNumber);
            default -> throw new IllegalArgumentException("Invalid operator! " + op);
        };
    }

}
