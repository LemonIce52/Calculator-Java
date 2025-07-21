package calculator;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenize {

    private final Pattern TOKENIZE_PATTERN =  Pattern.compile("(((\\d+\\.\\d*)|(\\.\\d+)|(\\d+))([eE][+-]?\\d+)?)|(\\b[a-z]+\\b)|.");


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
    public ArrayList<Object> tokenises(String excerpt) {

        ConvertObjects convert = new ConvertObjects();
        ArrayList<Object> term = new ArrayList<>();
        Matcher matcher = TOKENIZE_PATTERN.matcher(excerpt);

        while (matcher.find())
            term.add(excerpt.substring(matcher.start(), matcher.end()));

        return convert.convertObjects(term);
    }

}
