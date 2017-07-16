//package al.taghizadeh.me;
//
//import al.taghizadeh.csp.Assignment;
//import al.taghizadeh.csp.CSP;
//import al.taghizadeh.csp.Constraint;
//import al.taghizadeh.csp.Variable;
//
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
///**
// * Created by Ali Asghar on 04/07/2017.
// */
//public class BinaryAssignment<VAR extends Variable, VAL> extends Assignment<VAR, VAL> implements CSPListener {
//    private Set<VAL> consistentAssignedValues = new HashSet<>();
//    private Set<VAL> unconistentAssigments = new HashSet<>();
//    private boolean consistencyStatus = true;
//
//    public boolean isConsistent() {
//        return false;
//    }
//
//    @Override
//    public boolean isConsistent(List<Constraint<VAR, VAL>> constraints) {
//        return super.isConsistent(constraints);
//    }
//
//    @Override
//    public boolean isComplete(List<VAR> vars) {
//        return super.isComplete(vars);
//    }
//
//    @Override
//    public boolean isSolution(CSP<VAR, VAL> csp) {
//        return super.isSolution(csp);
//    }
//
//    @Override
//    public void onAssign(Assignment assignment, Variable variable, Object val) {
//        if (consistentAssignedValues.contains(val)) {
//            consistencyStatus = false;
//            unconistentAssigments.add((VAL) val);
//        } else {
//            consistentAssignedValues.add((VAL) val);
//        }
//    }
//
//    @Override
//    public void onDelete(Assignment assignment, Variable variable, Object val) {
//        unconistentAssigments.remove(val);
//    }
//
//}
