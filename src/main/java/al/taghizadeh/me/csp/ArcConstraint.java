package al.taghizadeh.me.csp;

import al.taghizadeh.csp.Assignment;
import al.taghizadeh.csp.Constraint;
import al.taghizadeh.csp.Variable;

import java.util.List;
import java.util.Objects;

/**
 * Created by Ali Asghar on 04/07/2017.
 */
public class ArcConstraint<VAR extends Variable, VAL> implements Constraint<VAR, VAL> {
    private VAR var1;
    private VAR var2;
    private List<VAR> scope;
    private boolean conflict = false;

    public ArcConstraint(VAR var1, VAR var2, List<VAR> scope) {
        this.var1 = var1;
        this.var2 = var2;
        this.scope = scope;
    }

    @Override
    public List<VAR> getScope() {
        return scope;
    }

    @Override
    public boolean isSatisfiedWith(Assignment<VAR, VAL> assignment) {
        Object value1 = assignment.getValue(var1);
        if(value1 == null || !value1.equals(assignment.getValue(var2)))
            return true;
        Course c1 = (Course)var1;
        Course c2 = (Course)var2;
        if((((RoomTimeSlot)value1).equalTime((RoomTimeSlot)assignment.getValue(var2)))
                && (Objects.equals(c1.getMasterId(), c2.getMasterId()) || c1.getGroupId() == c2.getGroupId()))
            return false;
        return false;
    }
}
