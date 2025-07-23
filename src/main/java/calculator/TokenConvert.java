package calculator;

import java.util.ArrayList;

class TokenConvert {

    //performs a complete conversion of numbers into numbers, variables also into numbers if variables are present
    public ArrayList<Object> convert(ArrayList<Object> term) {
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

        return resolveUnaryOperators(term);
    }

    //separate conversion and application of unary plus and minus
    private ArrayList<Object> resolveUnaryOperators(ArrayList<Object> term) {
        ArrayList<Object> newTerm = new ArrayList<>();

        for (int i = 0; i < term.size(); i++){
            String current = String.valueOf(term.get(i));

            switch (current) {
                case "-", "+" -> {
                    if (i == 0 && i+1 < term.size() && term.get(i+1) instanceof Double)
                        newTerm.add(Double.parseDouble(current + term.get(++i)));
                    else if (i == 0 && i+2 < term.size() && term.get(i+1) instanceof String op && op.equals("-"))
                        newTerm.add(Double.parseDouble(current + term.get(i += 2)));
                    else if (term.get(i-1) instanceof String && i + 1 < term.size() && term.get(i + 1) instanceof Double)
                        newTerm.add(Double.parseDouble(current + term.get(++i)));
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

    //converts a string into a double number, if you pass an expression,
    // then in case of a parsing error, a recursion occurs where the expression is passed
    public static double convertVariables(String variables) {
        try {
            return Double.parseDouble(variables);
        } catch (NumberFormatException e) {
            return new Calculator().parseAndCalculate(variables);
        }
    }

}
