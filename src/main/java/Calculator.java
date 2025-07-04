import java.util.ArrayList;
import java.util.Arrays;

public class Calculator {

    public Double input(String excerpt) {
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
        boolean isPow = false;
        StringBuilder newStr = new StringBuilder();

        for (int i = 0; i < excerpt.length(); i++) {
            if (i + 1 < excerpt.length() && excerpt.charAt(i) == '-' && excerpt.charAt(i + 1) != ' ') {

                for (int j = i; j < excerpt.length(); j++) {
                    if (excerpt.charAt(j) == ' ' && excerpt.charAt(j + 1) == '^') {
                        isPow = true;
                        break;
                    } else if (excerpt.charAt(j) == ' ' && excerpt.charAt(j + 1) != '^') {
                        break;
                    }
                }

                if (isPow) {
                    newStr.append(excerpt.charAt(i));
                    if ((i - 1 >= 0 && i + 1 < excerpt.length() && excerpt.charAt(i - 1) != '(' && excerpt.charAt(i + 1) != '(') || i == 0) {
                        newStr.append('(');
                    }
                } else if (i != 0 && excerpt.charAt(i - 1) == '(') {
                    newStr.append(excerpt.charAt(i));
                } else {
                    newStr.append(excerpt.charAt(i));
                }
            } else {
                if (isPow) {
                    for (int j = i; j < excerpt.length(); j++) {
                        if ((excerpt.charAt(j) == ' ' && isOperator(excerpt.charAt(j + 1)) && excerpt.charAt(j + 2) == ' ')) {
                            i = j;
                            isPow = false;
                            newStr.append(')');
                            newStr.append(excerpt.charAt(j));
                            break;
                        } else if (excerpt.charAt(j) == ')') {
                            i = j;
                            newStr.append(excerpt.charAt(j));

                            int openBracket = 0;
                            int closeBracket = 0;

                            for (int k = 0; k < newStr.length(); k++) {
                                char current = newStr.charAt(k);
                                if (current == '(')
                                    openBracket++;
                                else if (current == ')')
                                    closeBracket++;
                            }

                            isPow = !(openBracket == closeBracket);
                            break;
                        } else if (j == excerpt.length() - 1) {
                            i = j;
                            isPow = false;
                            newStr.append(excerpt.charAt(j));
                            newStr.append(')');
                            break;
                        } else
                            newStr.append(excerpt.charAt(j));
                    }
                } else
                    newStr.append(excerpt.charAt(i));
            }
        }


        return newStr.toString();
    }

    private boolean isMathOperation(String str) {
        return str.contains("log") || str.contains("log10") || str.contains("sin") || str.contains("cos")
                || str.contains("sqrt") || str.contains("abs") || str.contains("floor") || str.contains("ceil")
                || str.contains("exp") || str.contains("tan");
    }

    private ArrayList<Object> tokenises(String excerpt) {
        if ((excerpt.contains("(") && !excerpt.contains(")")) || (!excerpt.contains("(") && excerpt.contains(")")))
            throw new IllegalArgumentException("Invalid format");

        excerpt = convertMathConstant(excerpt);
        excerpt = mathOperation(excerpt);
        excerpt = openBrackets(excerpt);

        return new ArrayList<>(Arrays.asList(excerpt.split("\\s+")));
    }

    private String mathOperation(String excerpt) {

        while (isMathOperation(excerpt)) {
            int indexFirst = -1;
            int openBracket = -1;
            int closeBrackets = -1;
            int step = 0;

            for (int i = 0; i < excerpt.length(); i++) {
                char current = excerpt.charAt(i);
                if (current == 'l' || current == 's' || current == 'c' || current == 'a'
                        || current == 't' || current == 'e' || current == 'f') {
                    indexFirst = i;
                    break;
                }
            }

            for (int i = indexFirst; i < excerpt.length(); i++) {
                char current = excerpt.charAt(i);
                if (current == '(') {
                    openBracket = i;
                    for (int j = openBracket + 1; j < excerpt.length(); j++) {
                        char currentReverse = excerpt.charAt(j);
                        if (currentReverse == '(') {
                            step++;
                        } else if (currentReverse == ')') {
                            if (step > 0)
                                step--;
                            else {
                                closeBrackets = j;
                                break;
                            }
                        }
                    }
                    break;
                }
            }

            String operation = excerpt.substring(indexFirst, openBracket);
            double number;
            try {
                number = Double.parseDouble(excerpt.substring(openBracket + 1, closeBrackets));
            } catch (NumberFormatException e) {
                number = input(excerpt.substring(openBracket + 1, closeBrackets));
            }

            if (number < 0 && !operation.equals("abs"))
                throw new IllegalArgumentException("Math method does not supported number less that zero!");

            double result = applyMathOperator(operation, number);

            excerpt = excerpt.substring(0, indexFirst) + result + excerpt.substring(closeBrackets + 1);
        }

        return excerpt;
    }

    private String convertMathConstant(String excerpt) {
        if (excerpt.contains("pi")) {
            int index = excerpt.indexOf("pi");
            if (index + 2 >= excerpt.length() || excerpt.charAt(index + 2) == ' ' || excerpt.charAt(index + 2) == ')') {
                excerpt = excerpt.substring(0, index) + Math.PI + excerpt.substring(index + 2);
            } else
                throw new IllegalArgumentException("Does not math constant!");

        } else if (excerpt.contains("e")) {
            int index = excerpt.indexOf("e");
            if (index + 1 >= excerpt.length() || excerpt.charAt(index + 1) == ' ' || excerpt.charAt(index + 1) == ')') {
                excerpt = excerpt.substring(0, index) + Math.E + excerpt.substring(index + 1);
            } else
                throw new IllegalArgumentException("Does not math constant!");
        }

        return excerpt;
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
            double result;
            try {
                result = Double.parseDouble(newExcerpt);
            } catch (NumberFormatException e) {
                result = input(newExcerpt);
            }

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

    private ArrayList<Object> convertObjects(ArrayList<Object> term) {
        ArrayList<Object> newTerm = new ArrayList<>();
        for (Object object : term) {
            if (object instanceof String str) {
                try {
                    newTerm.add(Double.parseDouble(str));
                } catch (NumberFormatException e) {
                    newTerm.add(object);
                }
            } else
                newTerm.add(object);
        }

        return newTerm;
    }

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
