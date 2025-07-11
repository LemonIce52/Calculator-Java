import java.util.HashMap;

public class CalcVariables{

    private final static HashMap<String, Double> _listVariables = new HashMap<>();

    public void setVariables(String key, double value) {
        CalcVariables._listVariables.put(key, value);
    }

    public double getVariables(String key) {
        if (CalcVariables._listVariables.containsKey(key))
            return CalcVariables._listVariables.get(key);

        return Double.NaN;
    }

    public HashMap<String, Double> getListVariables() {
        return CalcVariables._listVariables;
    }

    public void clearList() {
        CalcVariables._listVariables.clear();
    }
}
