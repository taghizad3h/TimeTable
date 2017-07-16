package al.taghizadeh.me.sa;

import al.taghizadeh.csp.Assignment;
import al.taghizadeh.csp.Variable;
import org.apache.log4j.Logger;

/**
 * Created by Ali Asghar on 06/07/2017.
 */
public class SwapTimeSlotAction<VAR extends Variable, VAL, S extends Assignment<VAR, VAL>> {
    private Logger logger = Logger.getLogger(SwapTimeSlotAction.class);
    private SwappingCourse<VAR, VAL> swappingVars;

    public SwapTimeSlotAction(SwappingCourse<VAR, VAL> swappingVars) {
        this.swappingVars = swappingVars;
    }

    public S doAction(Assignment assignment) {
        Assignment newAssignment = assignment.clone();
        for (VAR var : swappingVars.vars) {
            newAssignment.remove(var);
            newAssignment.add(var, swappingVars.varsToVals.get(var));
        }
        return (S) newAssignment;
    }
}
