package al.taghizadeh.me.csp;

import al.taghizadeh.csp.Assignment;
import al.taghizadeh.csp.Constraint;
import al.taghizadeh.csp.Variable;

import java.util.Collections;
import java.util.List;

/**
 * Created by Ali Asghar on 05/07/2017.
 */
public class UnaryConstraint <VAR extends Variable, VAL> implements Constraint<VAR, VAL> {
    private VAR var;

    public UnaryConstraint(VAR var) {
        this.var = var;
    }

    @Override
    public List<VAR> getScope() {
        return Collections.singletonList(var);
    }

    @Override
    public boolean isSatisfiedWith(Assignment<VAR, VAL> assignment) {
        VAL val = assignment.getValue(var);
        if (val == null)
            return true;
        Course c = (Course) var;
        Room r = ((RoomTimeSlot) val).getRoom();
        return c.getCapacity() <= r.getCapacity() && (c.getEquipmentId() == null || c.getEquipmentId().equals(r.getEquipmentId()));
    }
}
