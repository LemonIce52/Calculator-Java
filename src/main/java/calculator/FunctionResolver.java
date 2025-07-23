package calculator;

import java.util.Arrays;
import java.util.regex.Matcher;


class FunctionResolver {

    public String applyMathFunctions(String excerpt) {
        return normalizeUnaryMinusInPowers(excerpt);
    }

    // makes unary precedence explicit for further processing
    private String normalizeUnaryMinusInPowers(String excerpt) {
        Matcher matcher = Constants.UNARY_OOPERATOR_PATTERN.matcher(excerpt);

        while (matcher.find())
            excerpt = excerpt.substring(0, matcher.start()+1) + "(" + excerpt.substring(matcher.start()+1, matcher.end()) + ")" + excerpt.substring(matcher.end());


        return mathOperation(excerpt);
    }

    //processes mathematical functions that are described in the class documentation
    private String mathOperation(String excerpt) {

        while (isMathOperation(excerpt)) {
            int indexFirst = -1;
            String operation = "";

            for (String func : Constants.MATH_FUNCTIONS) {
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

    private boolean isMathOperation(String str) {
        return Constants.MATH_FUNCTIONS.stream().anyMatch(func -> str.contains(func + "("));
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
                .mapToDouble(TokenConvert::convertVariables)
                .toArray();

        if (variables.length < 1)
            throw new IllegalArgumentException("Math method accepts only one or two arguments!");

        if (variables[0] < 0 && (operation.equals("log") || operation.equals("log10") || operation.equals("sqrt")))
            throw new IllegalArgumentException("Math method does not supported number less that zero!");

        double result;

        if (variables.length == 2)
            result = MathFunctionsAndOperatorExecutor.applyMathOperatorTooArguments(operation, variables);
        else
            result = MathFunctionsAndOperatorExecutor.applyMathOperator(operation, variables[0]);

        return result;
    }

    //replaces the verbal representation of mathematical constants with a digital representation
    private String convertMathConstant(String excerpt) {

        BracketsResolution brackets = new BracketsResolution();

        excerpt = excerpt.replaceAll("(?<![\\da-z])pi(?![\\da-z])", String.valueOf(Math.PI));
        excerpt = excerpt.replaceAll("(?<![\\da-z])e(?![\\da-z])", String.valueOf(Math.E));

        return brackets.openBrackets(excerpt);
    }

}
