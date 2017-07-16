package al.taghizadeh.me.csp;

import al.taghizadeh.csp.CSP;
import al.taghizadeh.csp.Domain;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by Ali Asghar on 04/07/2017.
 */
public class TimeTable extends CSP<Course, RoomTimeSlot> {
    static Logger logger = Logger.getLogger(TimeTable.class);
    private List<Master> masters = new ArrayList<>();

    public List<Master> getMasters() {
        return masters;
    }

    public TimeTable(List<Course> courses, List<RoomTimeSlot> values, List<RoomTimeSlot> vlsForOneLecture, Map<String, List<RoomTimeSlot>> eqToRTS, List<Master> masters) {
        this.masters = masters;
        logger.info("Adding Variables");
        for (Course c : courses) {
            addVariable(c);
        }
        Domain<RoomTimeSlot> domain = new Domain<>(values);
        Domain<RoomTimeSlot> domain1 = new Domain<>(vlsForOneLecture);
        logger.info("Adding Domains");
        for (Course var : getVariables()) {
            if (var.getCourseType().equals(Course.CourseType.TWO_LECTURE)) {
                setDomain(var, domain);
            } else if (var.getEquipmentId() != null) {
                setDomain(var, new Domain<>(eqToRTS.get(var.getEquipmentId())));
            } else {
                setDomain(var, domain1);
            }
        }
        logger.info("Adding Constraints");
        int size = getVariables().size();
        for (int i = 0; i < size; i++) {
            Course var1 = getVariables().get(i);
            addConstraint(new UnaryConstraint<>(var1));
            if (var1.getName().endsWith("-1")) {
                addConstraint(new SameClassTimeConstraint<>(var1, getVariables().get(i - 1)));
            }
            for (int j = i + 1; j < size; j++) {
                Course var2 = getVariables().get(j);
                addConstraint(new ArcConstraint<>(var1, var2, Arrays.asList(var1, var2)));
            }
        }
    }
}
