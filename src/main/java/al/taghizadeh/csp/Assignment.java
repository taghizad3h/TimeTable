package al.taghizadeh.csp;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * An assignment assigns values to some or all variables of a CSP.
 *
 * @author Ruediger Lunde
 */
public class Assignment<VAR extends Variable, VAL> implements Cloneable {
    /**
     * Maps variables to their assigned values.
     */
    private LinkedHashMap<VAR, VAL> variableToValueMap = new LinkedHashMap<>();
    private LinkedHashMap<VAL,VAR> valueToVariableMap = new LinkedHashMap<>();

    public List<VAR> getVariables() {
        return new ArrayList<>(variableToValueMap.keySet());
    }

    public List<VAL> getValues(){
        return new ArrayList<>(valueToVariableMap.keySet());
    }

    public VAL getValue(VAR var) {
        return variableToValueMap.get(var);
    }

    public VAR getVariable(VAL val){
        return valueToVariableMap.get(val);
    }

    public void add(VAR var, VAL value) {
        assert value != null;
        variableToValueMap.put(var, value);
        valueToVariableMap.put(value, var);
    }

    public void remove(VAR var) {
        valueToVariableMap.remove(getValue(var));
        variableToValueMap.remove(var);
    }

    public boolean contains(VAR var) {
        return variableToValueMap.containsKey(var);
    }

    public boolean containsVal(VAL val) {
        return variableToValueMap.containsKey(val);
    }

    /**
     * Returns true if this assignment does not violate any constraints of
     * <code>constraints</code>.
     */
    public boolean isConsistent(List<Constraint<VAR, VAL>> constraints) {
        for (Constraint<VAR, VAL> cons : constraints)
            if (!cons.isSatisfiedWith(this))
                return false;
        return true;
    }

    /**
     * Returns true if this assignment assigns values to every variable of
     * <code>vars</code>.
     */
    public boolean isComplete(List<VAR> vars) {
        for (VAR var : vars)
            if (!contains(var))
                return false;
        return true;
    }

    /**
     * Returns true if this assignment is consistent as well as complete with
     * respect to the given CSP.
     */
    public boolean isSolution(CSP<VAR, VAL> csp) {
        return isConsistent(csp.getConstraints()) && isComplete(csp.getVariables());
    }

    @SuppressWarnings("unchecked")
    @Override
    public Assignment<VAR, VAL> clone() {
        Assignment<VAR, VAL> result;
        try {
            result = (Assignment<VAR, VAL>) super.clone();
            result.variableToValueMap = new LinkedHashMap<>(variableToValueMap);
            result.valueToVariableMap = new LinkedHashMap<>(valueToVariableMap);
        } catch (CloneNotSupportedException e) {
            throw new UnsupportedOperationException("Could not clone assignment."); // should never happen!
        }
        return result;
    }

    @Override
    public String toString() {
        boolean comma = false;
        StringBuilder result = new StringBuilder("{");
        for (Map.Entry<VAR, VAL> entry : variableToValueMap.entrySet()) {
            if (comma)
                result.append(", ");
            result.append(entry.getKey()).append("=").append(entry.getValue());
            comma = true;
        }
        result.append("}");
        return result.toString();
    }
}