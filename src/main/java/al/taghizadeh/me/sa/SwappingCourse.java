package al.taghizadeh.me.sa;

import al.taghizadeh.csp.Variable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ali Asghar on 07/07/2017.
 */
public class SwappingCourse<VAR extends Variable, VAL> {
    List<VAR> vars = new ArrayList<>();
    Map<VAR, VAL> varsToVals = new HashMap<>();

    public SwappingCourse(List<VAR> vars, Map<VAR, VAL> varsToVals) {
        this.vars = vars;
        this.varsToVals = varsToVals;
    }
    public void add(VAR var, VAL val){
        vars.add(var);
        varsToVals.put(var, val);
    }
}
