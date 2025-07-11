import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class Calculator implements ICalculate {

    private static final Set<String> SUPPORTED_FUNCTIONS = Set.of(
            "log", "log10", "sin", "cos", "sqrt", "abs", "floor", "ceil", "exp", "tan"
    );

    @Override
    public Double input(String excerpt) {
        if (excerpt.isEmpty() || excerpt.equals(" "))
            throw new IllegalArgumentException("Invalid format excerpt!");


        excerpt = excerpt.trim().toLowerCase();
        excerpt = normalizeUnaryMinusInPowers(excerpt);
        ArrayList<Object> term = tokenises(excerpt);
        term = convertObjects(term);

        if (!validTokens(term))
            throw new IllegalArgumentException("Invalid format excerpt!");

        term = topOperator(term);

        return calculatingTheResult(term);
    }

    private String normalizeUnaryMinusInPowers(String excerpt) {
        String[] tokens = excerpt.split("\\s+");
        StringBuilder result = new StringBuilder();
        boolean isPower = false;

        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];

            if (isUnaryMinus(token) && i + 1 < tokens.length && tokens[i + 1].equals("^")) {
                result.append("-(").append(token.substring(1)).append(" ");
                isPower = true;
            } else if (isNumeric(token) && i - 1 >= 0 && tokens[i - 1].equals("^")) {
                if (isPower && i + 2 < tokens.length && !tokens[i + 2].equals("^"))
                    result.append(token).append(")").append(" ");
                else if (i == tokens.length - 1 && isPower)
                    result.append(token).append(")");
                else
                    result.append(token).append(" ");
            } else {
                result.append(token).append(" ");
            }
        }

        return result.toString().trim();
    }

    private boolean isUnaryMinus(String token) {
        return token.charAt(0) != '(' && token.charAt(0) == '-' && 1 < token.length() && token.charAt(1) != '(';
    }

    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private ArrayList<Object> tokenises(String excerpt) {
        if ((excerpt.contains("(") && !excerpt.contains(")")) || (!excerpt.contains("(") && excerpt.contains(")")))
            throw new IllegalArgumentException("Invalid format");

        excerpt = mathOperation(excerpt);
        excerpt = convertMathConstant(excerpt);
        excerpt = openBrackets(excerpt);

        return new ArrayList<>(Arrays.asList(excerpt.split("\\s+")));
    }

    private String mathOperation(String excerpt) {

        while (isMathOperation(excerpt)) {
            int indexFirst = -1;
            int closeBrackets = -1;
            int step = 0;
            String operation = "";

            for (String func : SUPPORTED_FUNCTIONS) {
                int index = excerpt.indexOf(func + "(");
                if (index != -1 && (indexFirst == -1 || index < indexFirst)) {
                    indexFirst = index;
                    operation = func;
                }
            }

            int openBracket = indexFirst + operation.length();

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


            String variables = excerpt.substring(openBracket + 1, closeBrackets);
            double number = convertVariables(variables);

            if (number < 0 && (operation.equals("log") || operation.equals("log10") || operation.equals("sqrt")))
                throw new IllegalArgumentException("Math method does not supported number less that zero!");

            double result = applyMathOperator(operation, number);

            excerpt = excerpt.substring(0, indexFirst) + result + excerpt.substring(closeBrackets + 1);
        }

        return excerpt;
    }

    private boolean isMathOperation(String str) {
        return str.contains("log(") || str.contains("log10(") || str.contains("sin(") || str.contains("cos(")
                || str.contains("sqrt(") || str.contains("abs(") || str.contains("floor(") || str.contains("ceil(")
                || str.contains("exp(") || str.contains("tan(");
    }

    private String openBrackets(String excerpt) {

        while (excerpt.contains("(") && excerpt.contains(")")) {
            int indexFirst = excerpt.indexOf('(') + 1;
            int indexLast = 0;

            for (int i = indexFirst; i < excerpt.length(); i++) {
                if (excerpt.charAt(i) == '(')
                    indexFirst = i + 1;
                if (excerpt.charAt(i) == ')') {
                    indexLast = i;
                    break;
                }
            }

            String newExcerpt = excerpt.substring(indexFirst, indexLast);

            double result = convertVariables(newExcerpt);

            String endExcerpt = " " + excerpt.substring(indexLast + 1);
            int step = 2;

            for (int i = indexFirst; i >= 0; i--) {
                if (excerpt.charAt(i) == ' ') {
                    step = i + 1;
                    break;
                } else if (excerpt.charAt(i) == '(') {
                    break;
                }
            }

            if (indexFirst - 2 >= 0 && result < 0) {
                excerpt = (switch (excerpt.charAt(indexFirst - step)) {
                    case '+' -> excerpt.substring(0, indexFirst - step) + result + endExcerpt;
                    case '-' -> excerpt.substring(0, indexFirst - step) + (result * -1.0) + endExcerpt;
                    default -> excerpt.substring(0, indexFirst - 1) + result + endExcerpt;
                }).trim();
            } else
                excerpt = (excerpt.substring(0, indexFirst - 1) + result + endExcerpt).trim();
        }

        return excerpt;
    }

    private double convertVariables(String variables) {
        double result;
        try {
            result = Double.parseDouble(variables);
        } catch (NumberFormatException e) {
            result = input(variables);
        }
        return result;
    }

    private String convertMathConstant(String excerpt) {

        excerpt = excerpt.replaceAll("pi", String.valueOf(Math.PI));
        excerpt = excerpt.replaceAll("(?<!\\d)e(?!\\d)", String.valueOf(Math.E));

        return excerpt;
    }

    private ArrayList<Object> convertObjects(ArrayList<Object> term) {
        CalcVariables var = new CalcVariables();
        ArrayList<Object> newTerm = new ArrayList<>();
        for (Object object : term) {
            if (object instanceof String str) {
                try {
                    newTerm.add(Double.parseDouble(str));
                } catch (NumberFormatException e) {
                    double result = var.getVariables(str);
                    if (Double.isNaN(result))
                        newTerm.add(object);
                    else
                        newTerm.add(result);
                }
            } else
                newTerm.add(object);
        }

        return newTerm;
    }

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
        return op.equals("*") || op.equals("/") || op.equals("+") || op.equals("-") || op.equals("^");
    }

    private boolean isOperator(char op) {
        return op == '*' || op == '/' || op == '+' || op == '-';
    }

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

    private double calculatingTheResult(ArrayList<Object> term) {
        if (term.size() == 1 && term.getFirst() instanceof Double)
            return Double.parseDouble(String.valueOf(term.getFirst()));

        double result = Double.parseDouble(String.valueOf(term.getFirst()));

        for (int i = 1; i < term.size(); i += 2) {
            if (term.get(i) instanceof String) {
                result = applyOperator((String) term.get(i), result, (double) term.get(i + 1));
            }
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
