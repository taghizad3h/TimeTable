package al.taghizadeh.me.sa;

import al.taghizadeh.csp.*;
import al.taghizadeh.framework.Node;
import al.taghizadeh.framework.problem.Problem;
import al.taghizadeh.me.csp.Course;
import al.taghizadeh.me.csp.Room;
import al.taghizadeh.me.csp.RoomTimeSlot;
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
        while (notConflicting.size() == 0) {
//            logger.info("no feasible trying again");
            v = Util.selectRandomlyFromList(state.getVariables());
            notConflicting = getNotConflictingSwapVals(v);
        }
        for (SwappingCourse sc : notConflicting) {
            actions.add((A) new SwapTimeSlotAction(sc));
        }
        System.out.println(state.getVariables().size());
        return actions;
    }

    public Node<S, A> getRandomNeighbour(S state){
        VAR v = Util.selectRandomlyFromList(state.getVariables());
        List<SwappingCourse> notConflicting = getNotConflictingSwapVals(v);
        while (notConflicting.size() == 0) {
//            logger.info("no feasible trying again");
            v = Util.selectRandomlyFromList(state.getVariables());
            notConflicting = getNotConflictingSwapVals(v);
        }

        return new Node(new SwapTimeSlotAction<>(notConflicting.get(0)).doAction(state));
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
        Assignment<VAR, VAL> assignment = state.clone();
        VAL val1 = assignment.getValue(var1);
        for (int i = 0; i < 1; i++) {
            VAL val2 = Util.selectRandomlyFromList(domain.asList());
            if (val1 != val2) {
                if (!assignment.containsVal(val2)) {// value not occupied
                    assignment.remove(var1);
                    assignment.add(var1, val2);
                    if (assignment.isConsistent(constraints)) {
                        List<VAR> vars = Arrays.asList(var1);
                        HashMap<VAR, VAL> varToVal = new HashMap<>();
                        varToVal.put(var1, val2);
                        SwappingCourse sc = new SwappingCourse(vars, varToVal);
                        swappingCourses.add(sc);
                    }
                    assignment.remove(var1);
                    assignment.add(var1, val1);
                } else {
                    VAR var2 = assignment.getVariable(val2);
                    constraints.addAll(csp.getConstraints(var2));
                    assignment.remove(var2);
                    assignment.remove(var1);
                    assignment.add(var1, val2);
                    assignment.add(var2, val1);
                    if (assignment.isConsistent(constraints)) {
                        List<VAR> vars = Arrays.asList(var1, var2);
                        HashMap<VAR, VAL> varToVal = new HashMap<>();
                        varToVal.put(var1, val2);
                        varToVal.put(var2, val1);
                        SwappingCourse sc = new SwappingCourse(vars, varToVal);
                        swappingCourses.add(sc);
                    }
                    assignment.remove(var1);
                    assignment.remove(var2);
                    assignment.add(var1, val1);
                    assignment.add(var2, val2);
                }
            }
        }
        assignment = null;
        return swappingCourses;
    }

    private List<SwappingCourse> getNotConflictingSwapVarsBy2LectureClass(VAR var1_1) {
        List<SwappingCourse> swappingCourses = new LinkedList<>();
        List<Constraint<VAR, VAL>> constraints = csp.getConstraints();
        Domain<VAL> domain = csp.getDomain(var1_1);
        Assignment<VAR, VAL> assignment = state.clone();
        VAL val1_1 = assignment.getValue(var1_1);
        Course c1 = (Course) var1_1;
        Course c2 = null;
        if (c1.getName().endsWith("-0")) {
            c2 = new Course(c1.getName().replace("-0", "-1"));
        } else {
            c2 = new Course(c1.getName().replace("-1", "-0"));
        }
        VAR var1_2 = (VAR) c2;
        VAL val1_2 = assignment.getValue((VAR) c2);
        var1_2 = assignment.getVariable(val1_2);
        for (int i = 0; i < 1; i++) {
            VAL val2_1 = Util.selectRandomlyFromList(domain.asList());
            if (!assignment.containsVal(val2_1)) {//first val not occupied
                VAL val2_2 = Util.selectRandomlyFromList(getDomainForTimeSlot(var1_2, val2_1));
                if (val2_2 != null)
                    if (!assignment.containsVal(val2_2)) {//first val not occupied, second val not occupied
                        assignment.remove(var1_1);
                        assignment.remove(var1_2);
                        assignment.add(var1_1, val2_1);
                        assignment.add(var1_2, val2_2);
                        if (assignment.isConsistent(constraints)) {
                            List<VAR> vars = Arrays.asList(var1_1, var1_2);
                            HashMap<VAR, VAL> varToVal = new HashMap<>();
                            varToVal.put(var1_1, val2_1);
                            varToVal.put(var1_2, val2_2);
                            SwappingCourse sc = new SwappingCourse(vars, varToVal);
                            swappingCourses.add(sc);
                        }
                        assignment.remove(var1_1);
                        assignment.remove(var1_2);
                        assignment.add(var1_1, val1_1);
                        assignment.add(var1_2, val1_2);
                    } else {//first val not occupied, second var occupied
                        VAR var2_2 = assignment.getVariable(val2_2);
                        assignment.remove(var1_1);
                        assignment.remove(var1_2);
                        assignment.remove(var2_2);
                        assignment.add(var1_1, val2_2);
                        assignment.add(var1_2, val2_1);
                        assignment.add(var2_2, val1_1);
                        if (assignment.isConsistent(constraints)) {
                            List<VAR> vars = Arrays.asList(var1_1, var1_2, var2_2);
                            HashMap<VAR, VAL> varToVal = new HashMap<>();
                            varToVal.put(var1_1, val2_1);
                            varToVal.put(var1_2, val2_2);
                            varToVal.put(var2_2, val1_1);
                            SwappingCourse sc = new SwappingCourse(vars, varToVal);
                            swappingCourses.add(sc);
                        }
                        assignment.remove(var1_1);
                        assignment.remove(var1_2);
                        assignment.remove(var2_2);
                        assignment.add(var1_1, val1_1);
                        assignment.add(var1_2, val1_2);
                        assignment.add(var2_2, val2_2);
                    }
            } else {//first var occupied
                VAL val2_2 = Util.selectRandomlyFromList(getDomainForTimeSlot(var1_2, val2_1));
                if (val2_2 != null)
                    if (!assignment.containsVal(val2_2)) {//first val occupied, second val not occupied
                        VAR var2_1 = assignment.getVariable(val1_2);
                        assignment.remove(var1_1);
                        assignment.remove(var1_2);
                        assignment.remove(var2_1);
                        assignment.add(var1_1, val2_2);
                        assignment.add(var1_2, val2_1);
                        assignment.add(var2_1, val1_1);
                        if (assignment.isConsistent(constraints)) {
                            List<VAR> vars = Arrays.asList(var1_1, var1_2, var2_1);
                            HashMap<VAR, VAL> varToVal = new HashMap<>();
                            varToVal.put(var1_1, val2_1);
                            varToVal.put(var1_2, val2_2);
                            varToVal.put(var2_1, val1_1);
                            SwappingCourse sc = new SwappingCourse(vars, varToVal);
                            swappingCourses.add(sc);
                        }
                        assignment.remove(var1_1);
                        assignment.remove(var1_2);
                        assignment.remove(var2_1);
                        assignment.add(var1_1, val1_1);
                        assignment.add(var1_2, val1_2);
                        assignment.add(var2_1, val2_1);
                    } else {//first val  occupied, second var occupied
                        VAR var2_2 = assignment.getVariable(val2_2);
                        VAR var2_1 = assignment.getVariable(val2_1);
                        assignment.remove(var1_1);
                        assignment.remove(var1_2);
                        assignment.remove(var2_1);
                        assignment.remove(var2_2);
                        assignment.add(var1_1, val2_1);
                        assignment.add(var1_2, val2_2);
                        assignment.add(var2_1, val1_1);
                        assignment.add(var2_2, val1_2);
                        if (assignment.isConsistent(constraints)) {
                            List<VAR> vars = Arrays.asList(var1_1, var1_2, var2_1, var2_2);
                            HashMap<VAR, VAL> varToVal = new HashMap<>();
                            varToVal.put(var1_1, val2_1);
                            varToVal.put(var1_2, val2_2);
                            varToVal.put(var2_1, val1_1);
                            varToVal.put(var2_2, val1_2);
                            SwappingCourse sc = new SwappingCourse(vars, varToVal);
                            swappingCourses.add(sc);
                        }
                        assignment.remove(var1_1);
                        assignment.remove(var1_2);
                        assignment.remove(var2_1);
                        assignment.remove(var2_2);
                        assignment.add(var1_1, val1_1);
                        assignment.add(var1_2, val1_2);
                        assignment.add(var2_1, val2_1);
                        assignment.add(var2_2, val2_2);
                    }
            }
        }
        assignment = null;
        return swappingCourses;
    }

    public List<VAL> getDomainForTimeSlot(VAR var, VAL val) {
        RoomTimeSlot r1 = (RoomTimeSlot) val;
        int timeslot = r1.getTimeSlot();
        String roomName = r1.getRoom().getName();
        List<VAL> smallDomain = new ArrayList<>();
        for (VAL val2 : csp.getDomain(var)) {
            RoomTimeSlot r = (RoomTimeSlot) val2;
            if (r.getTimeSlot() == timeslot && r.getRoom().getName().equals(roomName))
                smallDomain.add(val2);
        }
        return smallDomain;
    }
}