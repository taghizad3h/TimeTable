package al.taghizadeh.me.sa;

import al.taghizadeh.csp.*;
import al.taghizadeh.framework.problem.Problem;
import al.taghizadeh.me.csp.Course;
import al.taghizadeh.util.Util;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by Ali Asghar on 06/07/2017.
 */
public class SATimeTableProblem<VAR extends Variable, VAL, S extends Assignment<VAR, VAL>,
        A extends SwapTimeSlotAction<VAR, VAL, S>>
        implements Problem<S, A> {
    private Assignment<VAR, VAL> state;
    private CSP<VAR, VAL> csp;

    private Logger logger = Logger.getLogger(SATimeTableProblem.class);

    public SATimeTableProblem(Assignment<VAR, VAL> state, CSP<VAR, VAL> csp) {
        this.state = state;
        this.csp = csp;
    }

    @Override
    public S getInitialState() {
        return (S) state;
    }

    @Override
    public List<A> getActions(S state) {
        return getFeasibleActions();
    }

    @Override
    public S getResult(S state, A action) {
        return action.doAction(state);
    }

    @Override
    public boolean testGoal(S state) {
        return false;
    }

    @Override
    public double getStepCosts(S state, A action, S stateDelta) {
        return 0;
    }

    private List<A> getFeasibleActions() {
        logger.info("finding feasable");
        List<A> actions = new LinkedList<>();
        List<VAR> randomVars = new ArrayList<>();
        VAR v = Util.selectRandomlyFromList(state.getVariables());
        List<SwappingCourse> notConflicting = getNotConflictingSwapVals(v);
        while (notConflicting.size() == 0){
//            logger.info("no feasible trying again");
            notConflicting = getNotConflictingSwapVals(v);
        }
        for (SwappingCourse sc : notConflicting) {
            actions.add((A) new SwapTimeSlotAction(sc));
        }
        return actions;
    }

    /**
     * find the vars with which if we swap current var
     * no hard constraint will be violated.
     */
    private List<SwappingCourse> getNotConflictingSwapVals(VAR var) {
        Course c = (Course) var;
        //check if this course has two lectures in a week or not
//        logger.info("finding swap");
        if (c.getName().endsWith("-1") || c.getName().endsWith("-0")) {
            return getNotConflictingSwapVarsBy2LectureClass(var);
        } else {
            return getNotConflictingSwapValsBy1LectureClass(var);
        }
    }

    private List<SwappingCourse> getNotConflictingSwapValsBy1LectureClass(VAR var1) {
        List<SwappingCourse> swappingCourses = new LinkedList<>();
        List<Constraint<VAR, VAL>> constraints = csp.getConstraints(var1);
        Domain<VAL> domain = csp.getDomain(var1);
        VAL val1 = state.getValue(var1);
        for (int i = 0; i < 100; i++) {
            VAL val2 = Util.selectRandomlyFromList(domain.asList());
            if (val1 != val2) {
                if (!state.containsVal(val2)) {// value not occupied
                    state.remove(var1);
                    state.add(var1, val2);
                    if (state.isConsistent(constraints)) {
                        List<VAR> vars = Arrays.asList(var1);
                        HashMap<VAR, VAL> varToVal = new HashMap<>();
                        varToVal.put(var1, val2);
                        SwappingCourse sc = new SwappingCourse(vars, varToVal);
                        swappingCourses.add(sc);
                    }
                    state.remove(var1);
                    state.add(var1, val1);
                } else {
                    VAR var2 = state.getVariable(val2);
                    constraints.addAll(csp.getConstraints(var2));
                    state.remove(var2);
                    state.remove(var1);
                    state.add(var1, val2);
                    state.add(var2, val1);
                    if (state.isConsistent(constraints)) {
                        List<VAR> vars = Arrays.asList(var1, var2);
                        HashMap<VAR, VAL> varToVal = new HashMap<>();
                        varToVal.put(var1, val2);
                        varToVal.put(var2, val1);
                        SwappingCourse sc = new SwappingCourse(vars, varToVal);
                        swappingCourses.add(sc);
                    }
                    state.remove(var1);
                    state.remove(var2);
                    state.add(var1, val1);
                    state.add(var2, val2);
                }
            }
        }
        return swappingCourses;
    }

    private List<SwappingCourse> getNotConflictingSwapVarsBy2LectureClass(VAR var1_1) {
        List<SwappingCourse> swappingCourses = new LinkedList<>();
        List<Constraint<VAR, VAL>> constraints = csp.getConstraints();
        Domain<VAL> domain = csp.getDomain(var1_1);
        VAL val1_1 = state.getValue(var1_1);
        Course c1 = (Course) var1_1;
        Course c2 = null;
        if (c1.getName().endsWith("-0")) {
            c2 = new Course(c1.getName().replace("-0", "-1"));
        } else {
            c2 = new Course(c1.getName().replace("-1", "-0"));
        }
        VAR var1_2 = (VAR) c2;
        VAL val1_2 = state.getValue((VAR) c2);
        for (int i = 0; i < 100; i++) {
            VAL val2_1 = Util.selectRandomlyFromList(domain.asList());
            if (!state.containsVal(val2_1)) {//first val not occupied
                VAL val2_2 = Util.selectRandomlyFromList(domain.asList());
                if (!state.containsVal(val2_2)) {//first val not occupied, second val not occupied
                    state.remove(var1_1);
                    state.remove(var1_2);
                    state.add(var1_1, val2_1);
                    state.add(var1_2, val2_2);
                    if (state.isConsistent(constraints)) {
                        List<VAR> vars = Arrays.asList(var1_1, var1_2);
                        HashMap<VAR, VAL> varToVal = new HashMap<>();
                        varToVal.put(var1_1, val2_1);
                        varToVal.put(var1_2, val2_2);
                        SwappingCourse sc = new SwappingCourse(vars, varToVal);
                        swappingCourses.add(sc);
                    }
                    state.remove(var1_1);
                    state.remove(var1_2);
                    state.add(var1_1, val1_1);
                    state.add(var1_2, val1_2);
                } else {//first val not occupied, second var occupied
                    VAR var2_2 = state.getVariable(val2_2);
                    state.remove(var1_1);
                    state.remove(var1_2);
                    state.remove(var2_2);
                    state.add(var1_1, val2_2);
                    state.add(var1_2, val2_1);
                    state.add(var2_2, val1_1);
                    if (state.isConsistent(constraints)) {
                        List<VAR> vars = Arrays.asList(var1_1, var1_2, var2_2);
                        HashMap<VAR, VAL> varToVal = new HashMap<>();
                        varToVal.put(var1_1, val2_1);
                        varToVal.put(var1_2, val2_2);
                        varToVal.put(var2_2, val1_1);
                        SwappingCourse sc = new SwappingCourse(vars, varToVal);
                        swappingCourses.add(sc);
                    }
                    state.remove(var1_1);
                    state.remove(var1_2);
                    state.remove(var2_2);
                    state.add(var1_1, val1_1);
                    state.add(var1_2, val1_2);
                    state.add(var2_2, val2_2);
                }
            } else {//first var occupied
                VAL val2_2 = Util.selectRandomlyFromList(domain.asList());
                if (!state.containsVal(val2_2)) {//first val occupied, second val not occupied
                    VAR var2_1 = state.getVariable(val1_2);
                    state.remove(var1_1);
                    state.remove(var1_2);
                    state.remove(var2_1);
                    state.add(var1_1, val2_2);
                    state.add(var1_2, val2_1);
                    state.add(var2_1, val1_1);
                    if (state.isConsistent(constraints)) {
                        List<VAR> vars = Arrays.asList(var1_1, var1_2, var2_1);
                        HashMap<VAR, VAL> varToVal = new HashMap<>();
                        varToVal.put(var1_1, val2_1);
                        varToVal.put(var1_2, val2_2);
                        varToVal.put(var2_1, val1_1);
                        SwappingCourse sc = new SwappingCourse(vars, varToVal);
                        swappingCourses.add(sc);
                    }
                    state.remove(var1_1);
                    state.remove(var1_2);
                    state.remove(var2_1);
                    state.add(var1_1, val1_1);
                    state.add(var1_2, val1_2);
                    state.add(var2_1, val2_1);
                } else {//first val  occupied, second var occupied
                    VAR var2_2 = state.getVariable(val2_2);
                    VAR var2_1 = state.getVariable(val2_1);
                    state.remove(var1_1);
                    state.remove(var1_2);
                    state.remove(var2_1);
                    state.remove(var2_2);
                    state.add(var1_1, val2_1);
                    state.add(var1_2, val2_2);
                    state.add(var2_1, val1_1);
                    state.add(var2_2, val1_2);
                    if (state.isConsistent(constraints)) {
                        List<VAR> vars = Arrays.asList(var1_1, var1_2, var2_1, var2_2);
                        HashMap<VAR, VAL> varToVal = new HashMap<>();
                        varToVal.put(var1_1, val2_1);
                        varToVal.put(var1_2, val2_2);
                        varToVal.put(var2_1, val1_1);
                        varToVal.put(var2_2, val1_2);
                        SwappingCourse sc = new SwappingCourse(vars, varToVal);
                        swappingCourses.add(sc);
                    }
                    state.remove(var1_1);
                    state.remove(var1_2);
                    state.remove(var2_1);
                    state.remove(var2_2);
                    state.add(var1_1, val1_1);
                    state.add(var1_2, val1_2);
                    state.add(var2_1, val2_1);
                    state.add(var2_2, val2_2);
                }
            }
        }

        return swappingCourses;
    }
}