import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* {@link Calculator} this class implements string processing and performs mathematical calculations. It has functions such as
 * <ul>
 *     <li>log(double x >= 0)</li>
 *     <li>log10(double x >= 0)</li>
 *     <li>sin(double x)</li>
 *     <li>cos(double x)</li>
 *     <li>sqrt(double x > 0)</li>
 *     <li>abs(double x)</li>
 *     <li>floor(double x)</li>
 *     <li>ceil(double x)</li>
 *     <li>exp(double x)</li>
 *     <li>tan(double x)</li>
 *     <li>pow(double x, double y)</li>
 *     <li>min(double x, double y)</li>
 *     <li>max(double x, double y)</li>
 * </ul>
* as well as:
 * <ul>
    *  <li>"+" - addition,</li>
    *  <li>"-" - subtraction,</li>
    *  <li>"*" - multiply,</li>
    *  <li>"/"- divide,</li>
    *  <li>"^" - raise to a power</li>
 * </ul>
* calculations are performed in a clear sequence of priorities, expressions in
* brackets and unary +/- are also supported
 * <blockquote><pre>
 *     Calculator calc = new Calculator();
 *     System.out.println(calc.input("2 + 2")); // return 4
 *     System.out.println(calc.input("-2^2")); // return -4
 *     System.out.println(calc.input("(-2) ^ 2")); // return 4
 *</blockquote></pre>
**/
public final class Calculator {

    private final Set<String> _mathFunctions = Set.of(
            "log", "log10", "sin", "cos", "sqrt", "abs", "floor", "ceil", "exp", "tan", "pow", "min", "max"
    );

    private final Set<String> _mathOperation = Set.of(
            "*", "/", "+", "-", "^"
    );

    private final Pattern _unaryOperatorPattern = Pattern.compile("-((\\d+\\.\\d*)|(\\.\\d+)|(\\d+))([eE][+-]?\\d+)?(\\^((\\d+\\.\\d*)|(\\.\\d+)|(\\d+))([eE][+-]?\\d+)?)+");
    private final Pattern _tokenizePattern =  Pattern.compile("(((\\d+\\.\\d*)|(\\.\\d+)|(\\d+))([eE][+-]?\\d+)?)|(\\b[a-z]+\\b)|.");

    /**
     * <p>This method accepts a string with an expression and performs calculations
     * (what calculations it performs can be found in the class documentation).</p>
     *
     * @param excerpt expression represented as a string
     *
     * @return result type double
     *
     * @throws IllegalArgumentException the string contains an extra bracket
     * @throws IllegalArgumentException the brackets are incorrectly positioned
     * @throws IllegalArgumentException if non-existent mathematical function is entered
     * @throws IllegalArgumentException if small or large number of parameters are passed to the function
     * @throws IllegalArgumentException if the function log, log10 and sqrt passes an argument less than zero
     * @throws IllegalArgumentException if the string is empty or contains one space
     * @throws IllegalArgumentException if the string ends with a sign or there are two signs in a row
     * (minus and plus coming under or at the beginning of the string are perceived as unary)
     * @throws IllegalArgumentException if there are 2 numbers with a remainder in a row
     * (if 2 numbers go in a row they are perceived as a single number)
     * **/

    public Double input(String excerpt) {
        if (excerpt.isEmpty() || excerpt.equals(" "))
            throw new IllegalArgumentException("Invalid format excerpt!");


        excerpt = excerpt.trim().toLowerCase().replaceAll("[ \t\n]", "");
        excerpt = normalizeUnaryMinusInPowers(excerpt);
        ArrayList<Object> term = tokenises(excerpt);
        term = convertObjects(term);

        if (!validTokens(term))
            throw new IllegalArgumentException("Invalid format excerpt!");

        term = topOperator(term);

        return calculatingTheResult(term);
    }

    // makes unary precedence explicit for further processing
    private String normalizeUnaryMinusInPowers(String excerpt) {
        Matcher matcher = _unaryOperatorPattern.matcher(excerpt);

        while (matcher.find())
            excerpt = excerpt.substring(0, matcher.start()+1) + "(" + excerpt.substring(matcher.start()+1, matcher.end()) + ")" + excerpt.substring(matcher.end());


        return excerpt;
    }

    /**
     * <p>this method splits a string into tokens of the type "sign" - "number",
     * before splitting some calculations are performed,
     * i.e. processing of mathematical functions, constants, variables if any,
     * and also opening brackets, further calculations are performed with a "pure" expression of the type "2+2*3/1^4".</p>
     *
     * @throws IllegalArgumentException if the number of opening and closing parentheses does not match
     * @throws IllegalArgumentException if the opening parenthesis is before the closing one
     * @throws IllegalArgumentException if a negative value is passed to a mathematical function (log, log10, sqrt)
     * @throws IllegalArgumentException if no arguments are passed to a mathematical function
     * @throws IllegalArgumentException if a mathematical function has more or less arguments than the function requires
     * */

    private ArrayList<Object> tokenises(String excerpt) {
        if ((excerpt.contains("(") && !excerpt.contains(")")) || (!excerpt.contains("(") && excerpt.contains(")")))
            throw new IllegalArgumentException("Invalid format");
        else {
            ArrayList<String> symbols = new ArrayList<>(Arrays.asList(excerpt.split("")));
            int openBrackets = symbols.stream().filter(str -> str.equals("(")).toList().size();
            int closeBrackets = symbols.stream().filter(str -> str.equals(")")).toList().size();

            int[] indexBrackets = indexBrackets(excerpt);

            if (openBrackets != closeBrackets)
                throw new IllegalArgumentException("Invalid format");

            if (indexBrackets[0] > indexBrackets[1])
                throw new IllegalArgumentException("Invalid Format!");
        }

        excerpt = mathOperation(excerpt);

        ArrayList<Object> term = new ArrayList<>();
        Matcher matcher = _tokenizePattern.matcher(excerpt);

        while (matcher.find())
            term.add(excerpt.substring(matcher.start(), matcher.end()));

        return term;
    }

    //processes mathematical functions that are described in the class documentation
    private String mathOperation(String excerpt) {

        while (isMathOperation(excerpt)) {
            int indexFirst = -1;
            String operation = "";

            for (String func : _mathFunctions) {
                int index = excerpt.indexOf(func + "(");
                if (index != -1) {
                    indexFirst = index;
                    operation = func;
                    break;
                }
            }

            int openBracket = indexFirst + operation.length();
            int closeBrackets = indexCloseBracketMathFunction(excerpt, openBracket);

            String arguments = excerpt.substring(openBracket + 1, closeBrackets);

            if (isMathOperation(arguments))
                arguments = mathOperation(arguments);

            double result = applyMathFunction(operation, arguments);

            excerpt = excerpt.substring(0, indexFirst) + result + excerpt.substring(closeBrackets + 1);
        }

        return convertMathConstant(excerpt);
    }

    // finds the indices of the opening and closing parentheses of a mathematical function
    private int indexCloseBracketMathFunction(String excerpt, int openBracket) {
        int closeBrackets = -1;
        int step = 0;

        for (int i = openBracket + 1; i < excerpt.length(); i++) {
            char currentReverse = excerpt.charAt(i);
            if (currentReverse == '(') {
                step++;
            } else if (currentReverse == ')') {
                if (step > 0)
                    step--;
                else {
                    closeBrackets = i;
                    break;
                }
            }
        }

        return closeBrackets;
    }

    //the arguments of a mathematical function are broken down and applied, the method returns the solution result
    private double applyMathFunction(String operation, String arguments) {
        double[] variables = Arrays.stream(arguments.split(","))
                .mapToDouble(this::convertVariables)
                .toArray();

        if (variables.length < 1)
            throw new IllegalArgumentException("Math method accepts only one or two arguments!");

        if (variables[0] < 0 && (operation.equals("log") || operation.equals("log10") || operation.equals("sqrt")))
            throw new IllegalArgumentException("Math method does not supported number less that zero!");

        double result;

        if (variables.length == 2)
            result = applyMathOperatorTooArguments(operation, variables);
        else
            result = applyMathOperator(operation, variables[0]);

        return result;
    }

    private boolean isMathOperation(String str) {
        return _mathFunctions.stream().anyMatch(func -> str.contains(func + "("));
    }

    //converts a string into a double number, if you pass an expression,
    // then in case of a parsing error, a recursion occurs where the expression is passed
    private double convertVariables(String variables) {
        double result;
        try {
            result = Double.parseDouble(variables);
        } catch (NumberFormatException e) {
            result = input(variables);
        }
        return result;
    }

    //replaces the verbal representation of mathematical constants with a digital representation
    private String convertMathConstant(String excerpt) {

        excerpt = excerpt.replaceAll("(?<!\\d)pi(?!\\d)", String.valueOf(Math.PI));
        excerpt = excerpt.replaceAll("(?<!\\d)e(?!\\d)", String.valueOf(Math.E));

        return openBrackets(excerpt);
    }

    private String openBrackets(String excerpt) {
        while (excerpt.contains("(") && excerpt.contains(")")) {

            int[] indexBrackets = indexBrackets(excerpt);
            excerpt = replaceBrackets(excerpt, indexBrackets);
        }

        return excerpt;
    }

    // finds the opening and closing parenthesis,
    // if there is no opening parenthesis then 0 will be processed in the loop and not -1
    private int[] indexBrackets(String excerpt) {
        int indexFirst = excerpt.indexOf('(');
        int indexLast = 0;

        for (int i = indexFirst + 1; i < excerpt.length(); i++) {
            if (excerpt.charAt(i) == '(')
                indexFirst = i;
            if (excerpt.charAt(i) == ')') {
                indexLast = i;
                break;
            }
        }

        return new int[]{indexFirst, indexLast};
    }

    //replaces the parentheses expression with the calculated result, unary plus and minus are also handled
    private String replaceBrackets(String excerpt, int[] indexBrackets) {
        String startExcerpt = excerpt.substring(0, indexBrackets[0]);
        String expression = excerpt.substring(indexBrackets[0] + 1, indexBrackets[1]);
        String endExcerpt = excerpt.substring(indexBrackets[1] + 1);

        double result = convertVariables(expression);
        int reverseStepDigitOrOperator = 2;

        if (indexBrackets[0] - reverseStepDigitOrOperator >= 0 && result < 0) {
            if (excerpt.charAt(indexBrackets[0] - 1) == '+' && !Character.isDigit(excerpt.charAt(indexBrackets[0] - reverseStepDigitOrOperator)))
                excerpt = (startExcerpt + (result * -1.0) + endExcerpt).trim();
            else
                excerpt = (startExcerpt + result + endExcerpt).trim();

        } else
            excerpt = (startExcerpt + result + endExcerpt).trim();

        return excerpt;
    }

    //performs a complete conversion of numbers into numbers, variables also into numbers if variables are present
    private ArrayList<Object> convertObjects(ArrayList<Object> term) {
        CalcVariables var = new CalcVariables();

        for (int i = 0; i < term.size(); i++) {
            String current = (String) term.get(i);

            try {
                term.set(i, Double.parseDouble(current));
            } catch (NumberFormatException e) {
                double result = var.getVariables(current);
                if (Double.isNaN(result))
                    term.set(i, current);
                else
                    term.set(i, result);
            }
        }

        return convertUnaryMinusOrPlus(term);
    }

    //separate conversion and application of unary plus and minus
    private ArrayList<Object> convertUnaryMinusOrPlus(ArrayList<Object> term) {
        ArrayList<Object> newTerm = new ArrayList<>();

        for (int i = 0; i < term.size(); i++){
            String current = String.valueOf(term.get(i));

            switch (current) {
                case "-" -> {
                    if (i == 0 && i+1 < term.size() && term.get(i+1) instanceof Double)
                        newTerm.add(Double.parseDouble(current + term.get(++i)));
                    else if (i == 0 && i+2 < term.size() && term.get(i+1) instanceof String op && op.equals("-"))
                        newTerm.add(Double.parseDouble(current + term.get(i += 2)));
                    else if (term.get(i-1) instanceof Double && i+1 < term.size() && term.get(i+1) instanceof Double)
                        newTerm.add(current);
                    else if (term.get(i-1) instanceof String && i + 1 < term.size() && term.get(i + 1) instanceof Double)
                        newTerm.add(Double.parseDouble(current + term.get(++i)));
                    else
                        newTerm.add(current);
                }
                case "+" -> {
                    if (i == 0 && i+1 < term.size() && term.get(i+1) instanceof Double)
                        newTerm.add(Math.abs((double) term.get(++i)));
                    else if (i == 0 && i+2 < term.size() && term.get(i+1) instanceof String op && (op.equals("-") || op.equals("+"))) {
                        newTerm.add(Math.abs((double) term.get(i += 2)));
                    }
                    else if (term.get(i-1) instanceof Double && i+1 < term.size() && term.get(i+1) instanceof Double)
                        newTerm.add(current);
                    else if (term.get(i-1) instanceof String && i + 1 < term.size() && term.get(i + 1) instanceof Double)
                        newTerm.add(Math.abs((double) term.get(++i)));
                    else
                        newTerm.add(current);
                }
                default -> {
                    try {
                        newTerm.add(Double.parseDouble(current));
                    } catch (NumberFormatException e) {
                        newTerm.add(current);
                    }
                }
            }
        }

        return newTerm;
    }

    //checks the "purity" of the expression after processing
    private boolean validTokens(ArrayList<Object> term) {
        if (term.isEmpty()) return false;
        if (term.getFirst() instanceof String || term.getLast() instanceof String) return false;

        for (int i = 0; i < term.size(); i++) {

            Object current = term.get(i);
            Object previous = i > 0 ? term.get(i - 1) : null;

            if (current instanceof String op) {
                if (!isOperator(op) || previous instanceof String)
                    return false;
            } else if (current instanceof Double && previous instanceof Double) {
                return false;
            }
        }

        return true;
    }

    private boolean isOperator(String op) {
        return _mathOperation.stream().anyMatch(str -> str.equals(op));
    }

    //performs calculations with the highest precedence operators (*, /, ^)
    private ArrayList<Object> topOperator(ArrayList<Object> term) {

        ArrayList<Object> changedTerm = raisingToPower(term);

        ArrayList<Object> newTerm = new ArrayList<>();
        for (int i = 0; i < changedTerm.size(); i++) {
            Object current = changedTerm.get(i);
            if (current instanceof String op && (op.equals("*") || op.equals("/"))) {
                double left = (double) newTerm.removeLast();
                double right = (double) changedTerm.get(++i);
                double res = applyOperator(op, left, right);

                newTerm.add(res);
            } else {
                newTerm.add(current);
            }
        }

        return newTerm;
    }

    //performs exponentiation taking into account the rules from right to left
    private ArrayList<Object> raisingToPower(ArrayList<Object> term) {

        for (int i = term.size() - 1; i >= 0; i--) {
            if (term.get(i) instanceof String op && op.equals("^")) {
                double left = (double) term.get(i - 1);
                double right = (double) term.get(i + 1);
                double result = applyOperator("^", left, right);

                term.set(i - 1, result);
                term.remove(i + 1);
                term.remove(i);
            }
        }

        return term;
    }

    //performs calculations of low-priority operators (+, -)
    private double calculatingTheResult(ArrayList<Object> term) {
        if (term.size() == 1 && term.getFirst() instanceof Double num)
            return num;

        double result = Double.parseDouble(String.valueOf(term.getFirst()));

        for (int i = 1; i < term.size(); i += 2) {
            String op = (String) term.get(i);
            double rightNumber = (double) term.get(i + 1);
            result = applyOperator(op, result, rightNumber);
        }

        return result;
    }

    private double applyMathOperator(String op, double number) {
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

    private double applyMathOperatorTooArguments(String op, double... numbers) {
        return switch (op) {
            case "pow" -> Math.pow(numbers[0], numbers[1]);
            case "max" -> Math.max(numbers[0], numbers[1]);
            case "min" -> Math.min(numbers[0], numbers[1]);
            default -> throw new IllegalArgumentException("Invalid operator! " + op);
        };
    }

    private double applyOperator(String op, double firstNumber, double secondNumber) {
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
