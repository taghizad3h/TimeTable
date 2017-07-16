package al.taghizadeh.me.csp;

import al.taghizadeh.csp.Assignment;
import al.taghizadeh.csp.Constraint;
import al.taghizadeh.csp.Variable;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created by Ali Asghar on 04/07/2017.
 */
public class ArcConstraint<VAR extends Variable, VAL> implements Constraint<VAR, VAL> {
    Logger logger = Logger.getLogger(ArcConstraint.class);
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
        Object value2 = assignment.getValue(var2);
        if (value1 == null || value2 == null)
            return true;
        Course c1 = (Course) var1;
        Course c2 = (Course) var2;
        RoomTimeSlot r1 = (RoomTimeSlot) value1;
        RoomTimeSlot r2 = (RoomTimeSlot) value2;

        if (r1.equals(r2))
            return false;

        if ((interfereTime(r1, r2)
                && (c1.getMasterId().equals(c2.getMasterId())
                || c1.getGroupId() == c2.getGroupId())))
            return false;
        return true;
    }

    private boolean equalTime(RoomTimeSlot value1, RoomTimeSlot value2) {
        return value2.getDay() == value1.getDay() && value2.getTimeSlot() == value1.getTimeSlot();
    }

    private boolean interfereTime(RoomTimeSlot value1, RoomTimeSlot value2) {
        if (value1.getType().equals(value2.getType()))
            return equalTime(value1, value2);
        if (value2.getDay() == value1.getDay()) {
            int twoLecture;
            int oneLecture;
            if (value1.getType().equals(RoomTimeSlot.RTSType.ForTwoLecture)) {
                twoLecture = value1.getTimeSlot();
                oneLecture = value2.getTimeSlot();
            } else {
                twoLecture = value2.getTimeSlot();
                oneLecture = value1.getTimeSlot();
            }
            if (twoLecture == 0 && oneLecture == 0
                    || twoLecture == 1 && oneLecture == 0
                    || twoLecture == 2 && oneLecture == 1
                    || twoLecture == 3 && oneLecture == 2
                    || twoLecture == 4 && oneLecture == 3)
                return true;
        }
        return false;
    }
}
