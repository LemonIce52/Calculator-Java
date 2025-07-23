package calculator;

import java.util.ArrayList;

public interface ExpressionParser {
    double parseAndCalculate(String excerpt);
    boolean validTokens(ArrayList<Object> term);
    double calculatingTheResult(ArrayList<Object> term);
}
