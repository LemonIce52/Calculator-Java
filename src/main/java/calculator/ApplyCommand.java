package calculator;

import java.util.HashMap;

/**
 * This class provides command execution and returns the result as a string.
 * <blockquote><pre>
 *     calculator.ApplyCommand comm = new calculator.ApplyCommand();
 *     comm.printList(); //return list variables in type String
 *     comm.clearList(); //return "Clear successes" and clear list variables
 * </blockquote></pre>
 * */
class ApplyCommand {

    public String printList() {
        CalcVariables comm = new CalcVariables();
        HashMap<String, Double> listVariables = comm.getListVariables();
        StringBuilder stringBuild = new StringBuilder();
        for (String key : listVariables.keySet())
            stringBuild.append(key).append(" = ").append(listVariables.get(key)).append("\n");

        return stringBuild.toString();
    }

    public String clearList() {
        CalcVariables comm = new CalcVariables();
        comm.clearList();
        return "Clear successes";
    }
}