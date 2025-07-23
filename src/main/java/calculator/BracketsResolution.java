package calculator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class BracketsResolution {

    public String openBrackets(String excerpt) {

        validation(excerpt);

        while (excerpt.contains("(") && excerpt.contains(")")) {

            int[] indexBrackets = indexBrackets(excerpt);
            excerpt = replaceBrackets(excerpt, indexBrackets);
        }

        return excerpt;
    }

    private void validation(String excerpt) {
        if ((excerpt.contains("(") && !excerpt.contains(")")) || (!excerpt.contains("(") && excerpt.contains(")")))
            throw new IllegalArgumentException("Invalid format");
        else {
            ArrayList<String> symbols = new ArrayList<>(Arrays.asList(excerpt.split("")));
            int openBrackets = symbols.stream().filter(str -> str.equals("(")).toList().size();
            int closeBrackets = symbols.stream().filter(str -> str.equals(")")).toList().size();

            int[] indexBrackets = indexBrackets(excerpt);

            if (openBrackets != closeBrackets)
                throw new IllegalArgumentException("Invalid format");

            if (indexBrackets[1] < indexBrackets[0])
                throw new IllegalArgumentException("Invalid Format!");
        }
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

        double result = TokenConvert.convertVariables(expression);
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

}
