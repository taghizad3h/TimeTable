import al.taghizadeh.csp.Assignment;
import al.taghizadeh.framework.problem.Problem;
import al.taghizadeh.me.csp.*;
import al.taghizadeh.me.sa.*;
import org.apache.log4j.Logger;

import java.util.Optional;

public class Main {
    static Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Running App");
        new Main().run();
    }

    public void run() {
        TimeTable timeTable = new InputParser().parse("input.txt");
//        new TreeCspSolver<Course, RoomTimeSlot>().solve(timeTable);

//        FlexibleBacktrackingSolver solver = new FlexibleBacktrackingSolver<Course, RoomTimeSlot>();
//        solver.setAll();
//        solver.solve(timeTable);
        Optional<Assignment<Course, RoomTimeSlot>> assignment
                = new CSPTimeTableSolver<Course, RoomTimeSlot>(1000000).solve(timeTable);
        if (assignment.isPresent()) {
            Assignment<Course, RoomTimeSlot> assignment1 = assignment.get();
            logger.info("the answer found by csp:");
            for (Course c : assignment1.getVariables()) {
                RoomTimeSlot roomTimeSlot = assignment1.getValue(c);
                logger.info(c + " " + roomTimeSlot);
            }

            SimulatedAnnealingSearch<Assignment, SwapTimeSlotAction<Course, RoomTimeSlot, Assignment<Course, RoomTimeSlot>>>
                    sa = new SimulatedAnnealingSearch<>(new DistanceCalculator<>(timeTable.getMasters()), new Scheduler(), new MyNodeExpander<>());

            Problem<Assignment, SwapTimeSlotAction<Course, RoomTimeSlot, Assignment<Course, RoomTimeSlot>>> problem1
                    = new SATimeTableProblem(assignment1, timeTable);
            Optional x = sa.findState(problem1);
            if (x.isPresent()) {
                Assignment<Course, RoomTimeSlot> assignment2 = (Assignment<Course, RoomTimeSlot>) x.get();
                logger.info("found a time table " + assignment2.isComplete(assignment2.getVariables()));
                for (Course c : assignment2.getVariables()) {
                    RoomTimeSlot roomTimeSlot = assignment1.getValue(c);
                    logger.info(c + " " + roomTimeSlot);
                }
            }
        } else {
            logger.info("There is no feasable answer");
        }


    }


}
