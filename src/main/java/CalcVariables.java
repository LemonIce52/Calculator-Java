import java.util.HashMap;

public class CalcVariables{

    private final static HashMap<String, Double> _listVariables = new HashMap<>();

    public void setVariables(String key, double value) {
        _listVariables.put(key, value);
    }

    public double getVariables(String key) {
        if (_listVariables.containsKey(key))
            return _listVariables.get(key);

        return Double.NaN;
    }
}
