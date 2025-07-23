package calculator;

import java.util.ArrayList;
import java.util.Set;

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
 *     calculator.Calculator calc = new calculator.Calculator();
 *     System.out.println(calc.input("2 + 2")); // return 4
 *     System.out.println(calc.input("-2^2")); // return -4
 *     System.out.println(calc.input("(-2) ^ 2")); // return 4
 *</blockquote></pre>
**/
final class Calculator implements ExpressionParser {

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

    @Override
    public double parseAndCalculate(String excerpt) {
        if (excerpt.isEmpty() || excerpt.equals(" "))
            throw new IllegalArgumentException("Invalid format excerpt!");

        ArrayList<Object> term = tokenizeAndTransform(excerpt);

        if (!validTokens(term))
            throw new IllegalArgumentException("Invalid format excerpt!");

        return calculatingTheResult(term);
    }

    private ArrayList<Object> tokenizeAndTransform(String excerpt) {
        Tokenize tokenize = new Tokenize();
        FunctionResolver applyHeavyMath = new FunctionResolver();

        excerpt = applyHeavyMath.applyMathFunctions(excerpt);

        return tokenize.tokenises(excerpt);
    }

    private boolean isMathFunctions(String excerpt) {
        return Constants.MATH_FUNCTIONS.stream().anyMatch(excerpt::contains);
    }

    //checks the "purity" of the expression after processing
    @Override
    public boolean validTokens(ArrayList<Object> term) {
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
        return Constants.MATH_OPERATORS.stream().anyMatch(str -> str.equals(op));
    }

    //performs calculations of low-priority operators (+, -)
    @Override
    public double calculatingTheResult(ArrayList<Object> term) {
        if (term.size() == 1 && term.getFirst() instanceof Double num)
            return num;

        term = topOperator(term);

        double result = Double.parseDouble(String.valueOf(term.getFirst()));

        for (int i = 1; i < term.size(); i += 2) {
            String op = (String) term.get(i);
            double rightNumber = (double) term.get(i + 1);
            result = MathFunctionsAndOperatorExecutor.applyOperator(op, result, rightNumber);
        }

        return result;
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
                double res = MathFunctionsAndOperatorExecutor.applyOperator(op, left, right);

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
                double result = MathFunctionsAndOperatorExecutor.applyOperator("^", left, right);

                term.set(i - 1, result);
                term.remove(i + 1);
                term.remove(i);
            }
        }

        return term;
    }
}
