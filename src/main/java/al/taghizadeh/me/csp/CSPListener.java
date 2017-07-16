package al.taghizadeh.me.csp;

import al.taghizadeh.csp.Assignment;
import al.taghizadeh.csp.Variable;

/**
 * Created by Ali Asghar on 04/07/2017.
 */
public interface CSPListener<VAR extends Variable, VAL> {

    void onAssign(Assignment<VAR, VAL> assignment, VAR var, Object val);
    void onDelete(Assignment<VAR, VAL> assignment, VAR var, Object val);

}
