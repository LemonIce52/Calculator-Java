import java.util.HashMap;

public class ApplyCommand {

    public void printList() {
        CalcVariables comm = new CalcVariables();
        HashMap<String, Double> listVariables = comm.getListVariables();
        for (String key : listVariables.keySet())
            System.out.println(key + " = " + listVariables.get(key));
    }

    public void clearList() {
        CalcVariables comm = new CalcVariables();
        comm.clearList();
        System.out.println("Clear successfully!");
    }
}