package calculator;

import java.util.HashMap;

/**
 * This class provides storage of variables that are created and also performs
 * actions related to commands or receiving variables
 *
 * <blockquote><pre>
 *     calculator.CalcVariables var = new calculator.CalcVariables();
 *     var.setVariables("x", 2); //set variables
 *     var.getVariables("x"); // return 2
 *     var.getVariables("y"); // return NaN because there is no variable with such a name
 *     var.getListVariables(); //return list variables
 *     var.clearList(); //clear list variables
 *     var.getVariables("x"); //now it will return NaN because the list of variables was cleared
 * </blockquote></pre>
 * */
class CalcVariables{

    private final static HashMap<String, Double> _listVariables = new HashMap<>();

    //lists the variable name and its value
    public void setVariables(String key, double value) {
        CalcVariables._listVariables.put(key, value);
    }

    //returns a variable by its name, if such a variable does not exist, NaN is returned
    public double getVariables(String key) {
        if (CalcVariables._listVariables.containsKey(key))
            return CalcVariables._listVariables.get(key);

        return Double.NaN;
    }

    //returns the entire list for further processing by the print command
    public HashMap<String, Double> getListVariables() {
        return CalcVariables._listVariables;
    }

    //clears the list (command clear)
    public void clearList() {
        CalcVariables._listVariables.clear();
    }
}
