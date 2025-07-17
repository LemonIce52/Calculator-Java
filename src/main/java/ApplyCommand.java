import java.util.HashMap;

public class ApplyCommand {

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