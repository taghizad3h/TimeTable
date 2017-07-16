package al.taghizadeh.me.csp;

import al.taghizadeh.csp.Assignment;
import al.taghizadeh.csp.Constraint;
import al.taghizadeh.csp.Variable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ali Asghar on 07/07/2017.
 */
public class SameClassTimeConstraint <VAR extends Variable, VAL> implements Constraint<VAR, VAL> {
    VAR var1;
    VAR var2;
    List<VAR> scope;

    public SameClassTimeConstraint(VAR var1, VAR var2) {
        this.var1 = var1;
        this.var2 = var2;
        scope = new ArrayList<>();
        scope.add(var1);
        scope.add(var2);
    }

    @Override
    public List<VAR> getScope() {
        return scope;
    }

    @Override
    public boolean isSatisfiedWith(Assignment<VAR, VAL> assignment) {
        RoomTimeSlot val1 = (RoomTimeSlot) assignment.getValue(var1);
        RoomTimeSlot val2 = (RoomTimeSlot) assignment.getValue(var2);
        return
                val1 == null || val2 == null
                        || val1.getTimeSlot() == val2.getTimeSlot()
                        && val1.getRoom() == val2.getRoom()
                        && Math.abs(val1.getDay() - val2.getDay()) > 1;
    }
}
