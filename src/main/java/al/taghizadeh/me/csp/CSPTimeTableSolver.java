package al.taghizadeh.me.csp;

import al.taghizadeh.csp.*;
import al.taghizadeh.util.Tasks;
import al.taghizadeh.util.Util;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.function.ToDoubleFunction;

/**
 * Created by Ali Asghar on 04/07/2017.
 */
public class CSPTimeTableSolver<VAR extends Variable, VAL> extends CspSolver<VAR, VAL> {

    private int maxSteps;
    private static Logger logger = Logger.getLogger(CSPTimeTableSolver.class);
    ToDoubleFunction function;

    /**
     * Constructs a min-conflicts strategy with a given number of steps allowed
     * before giving up.
     *
     * @param maxSteps the number of steps allowed before giving up
     */
    public CSPTimeTableSolver(int maxSteps) {
        this.function = new ToDouble<VAR, VAL>();
        this.maxSteps = maxSteps;
    }

    public Optional<Assignment<VAR, VAL>> solve(CSP<VAR, VAL> csp) {

        logger.info("Finding a feasible timetable");
        Assignment<VAR, VAL> current = generateRandomAssignment(csp);
        fireStateChanged(csp, current, null);
        for (int i = 0; i < maxSteps && !Tasks.currIsCancelled(); i++) {
            if (current.isSolution(csp)) {
                logger.info("ans answer found with distance " + function.applyAsDouble(current));
                return Optional.of(current);
            } else
                {
                Set<VAR> vars = getConflictedVariables(current, csp);
                VAR var = Util.selectRandomlyFromSet(vars);
                VAL value = getMinConflictValueFor(var, current, csp);
                current.add(var, value);
                fireStateChanged(csp, current, var);
            }
        }
        return Optional.empty();
    }

    private Assignment<VAR, VAL> generateRandomAssignment(CSP<VAR, VAL> csp) {
        Assignment<VAR, VAL> result = new Assignment<>();
        for (VAR var : csp.getVariables()) {
            VAL randomValue = Util.selectRandomlyFromList(csp.getDomain(var).asList());
            result.add(var, randomValue);
        }
        return result;
    }

    private Set<VAR> getConflictedVariables(Assignment<VAR, VAL> assignment, CSP<VAR, VAL> csp) {
        Set<VAR> result = new LinkedHashSet<>();
        csp.getConstraints().stream().filter(constraint -> !constraint.isSatisfiedWith(assignment)).
                forEach(constraint -> constraint.getScope().stream().filter(var -> !result.contains(var)).
                        forEach(result::add));
        return result;
    }

    private VAL getMinConflictValueFor(VAR var, Assignment<VAR, VAL> assignment, CSP<VAR, VAL> csp) {
        List<Constraint<VAR, VAL>> constraints = csp.getConstraints(var);
        Assignment<VAR, VAL> testAssignment = assignment.clone();
        int minConflict = Integer.MAX_VALUE;
        List<VAL> resultCandidates = new ArrayList<>();
        for (VAL value : csp.getDomain(var)) {
            testAssignment.add(var, value);
            int currConflict = countConflicts(testAssignment, constraints);
            if (currConflict <= minConflict) {
                if (currConflict < minConflict) {
                    resultCandidates.clear();
                    minConflict = currConflict;
                }
                resultCandidates.add(value);
            }
        }
        return (!resultCandidates.isEmpty()) ? Util.selectRandomlyFromList(resultCandidates) : null;
    }

    private int countConflicts(Assignment<VAR, VAL> assignment,
                               List<Constraint<VAR, VAL>> constraints) {
        int result = 0;
        for (Constraint<VAR, VAL> constraint : constraints)
            if (!constraint.isSatisfiedWith(assignment))
                result++;
        return result;
    }

    private class ToDouble <VAR extends Variable, VAL> implements ToDoubleFunction{
        Assignment<VAR, VAL> assignment;

        public double mastersCompactnessCost() {
            double distance = 0;
            Map<String, List<Integer>> mastersToDays = new HashMap<>();
            for (VAR var : assignment.getVariables()) {
                String masterId = ((Course) var).getMasterId();
                int day = ((RoomTimeSlot) assignment.getValue(var)).getDay();
                if (mastersToDays.containsKey(masterId)) {
                    mastersToDays.get(masterId).add(day);
                } else {
                    mastersToDays.put(masterId, new ArrayList<>(Collections.singleton(day)));
                }
            }

            for (String key : mastersToDays.keySet()) {
                int max;
                int min;
                List<Integer> days = mastersToDays.get(key);
                if (days.size() == 1)
                    continue;
                max = min = days.get(0);
                for (int i = 1; i < days.size(); i++) {
                    if (days.get(i) > max)
                        max = days.get(i);
                    else
                        min = days.get(i);
                }
                distance += (max - min) * 10;
            }
            return distance;
        }

        @Override
        public double applyAsDouble(Object value) {
            this.assignment = (Assignment<VAR, VAL>)value;
            return mastersCompactnessCost();
        }
    }
}
