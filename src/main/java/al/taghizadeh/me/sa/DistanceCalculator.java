package al.taghizadeh.me.sa;

import al.taghizadeh.csp.Assignment;
import al.taghizadeh.csp.Variable;
import al.taghizadeh.framework.Node;
import al.taghizadeh.me.csp.Course;
import al.taghizadeh.me.csp.RoomTimeSlot;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.function.ToDoubleFunction;

/**
 * Created by Ali Asghar on 06/07/2017.
 * <p>
 * This class is used to find the optimality of an answer
 * based on soft constraints. Each soft constraint is calculated
 * in its method and sum of them is returned.
 */
public class DistanceCalculator<VAR extends Variable, VAL, S extends Assignment<VAR, VAL>, A>
        implements ToDoubleFunction<Node<S, A>> {
    private Logger logger = Logger.getLogger(DistanceCalculator.class);
    private Node<S, A> current = null;

    @Override
    public double applyAsDouble(Node<S, A> value) {
        if (value == null)
            return -1000000000;
        this.current = value;
        return mastersCompactnessCost() + lateHourDistance();
    }


    /**
     * This constraint is applied as a hard constraint and
     * currently is not used.
     */
    @Deprecated
    private double sameTimeAndRoomDistance() {
        double total = 0;
        Assignment<VAR, VAL> assignment = current.getState();
        for (VAR var : assignment.getVariables()) {
            Course c = (Course) var;
            RoomTimeSlot r = (RoomTimeSlot) assignment.getValue(var);
            if (c.getName().endsWith("-1")) {
                Course cc = new Course(c.getName().replace("-1", "-0"));
                RoomTimeSlot rr = (RoomTimeSlot) assignment.getValue((VAR) cc);
                if (rr.getRoom() != r.getRoom())
                    total += 20;
                if (rr.getTimeSlot() != r.getTimeSlot())
                    total += 20;
            }
        }
        logger.info("distance " + total);
        return total;
    }

    public double mastersCompactnessCost() {
        double distance = 0;
        Map<String, List<Integer>> mastersToDays = new HashMap<>();
        for (VAR var : current.getState().getVariables()) {
            String masterId = ((Course) var).getMasterId();
            int day = ((RoomTimeSlot) current.getState().getValue(var)).getDay();
            if (mastersToDays.containsKey(masterId)) {
                if (!mastersToDays.get(masterId).contains(day))
                    mastersToDays.get(masterId).add(day);
            } else {
                mastersToDays.put(masterId, new ArrayList<>(Collections.singleton(day)));
            }
        }

        for (String key : mastersToDays.keySet()) {
            List<Integer> days = mastersToDays.get(key);
            if (days.size() > 2)
                distance += 10;
        }
        return distance;
    }

    private double groupCompactnessDistance(){
        double distance = 0;
        for (VAR var : current.getState().getVariables()){

        }
        return distance;
    }

    private double lateHourDistance(){
        double distance = 0;
        for (VAR var : current.getState().getVariables()) {
            RoomTimeSlot r =(RoomTimeSlot)current.getState().getValue(var);
            if(r.getTimeSlot() >= 6)
                distance += 5;
        }
        return  distance;
    }
}
