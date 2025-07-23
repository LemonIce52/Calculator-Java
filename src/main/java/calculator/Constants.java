package calculator;

import java.util.Set;
import java.util.regex.Pattern;

final class Constants {

    private Constants() {}

    public static final Pattern TOKEN_PATTERN =  Pattern.compile("(((\\d+\\.\\d*)|(\\.\\d+)|(\\d+))([eE][+-]?\\d+)?)|(\\b[a-z]+\\b)|.");
    public static final Pattern UNARY_OOPERATOR_PATTERN = Pattern.compile("-((\\d+\\.\\d*)|(\\.\\d+)|(\\d+))([eE][+-]?\\d+)?(\\^((\\d+\\.\\d*)|(\\.\\d+)|(\\d+))([eE][+-]?\\d+)?)+");

    public static final Set<String> MATH_OPERATORS = Set.of(
            "*", "/", "+", "-", "^"
    );
    public static final Set<String> MATH_FUNCTIONS = Set.of(
            "log", "log10", "sin", "cos", "sqrt", "abs", "floor", "ceil", "exp", "tan", "pow", "min", "max"
    );
}
