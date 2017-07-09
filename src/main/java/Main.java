import al.taghizadeh.csp.Assignment;
import al.taghizadeh.framework.problem.Problem;
import al.taghizadeh.me.csp.*;
import al.taghizadeh.me.sa.*;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

public class Main {
    static Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) throws IOException {
        logger.info("Running App");
        new Main().run();
    }

    public void run() throws IOException {
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


            Assignment<Course, RoomTimeSlot> assignment2 = (Assignment<Course, RoomTimeSlot>) x.get();
            makeHtml(assignment2, "sa");

        } else {
            logger.info("There is no feasable answer");
        }


    }

    public static void makeHtml(Assignment<Course, RoomTimeSlot> assignment, String name) throws IOException {
        String input = null;
        try {
            input = (IOUtils.toString(new FileInputStream("TimeTable.html"), "UTF-8"));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        org.jsoup.nodes.Document doc = Jsoup.parse(input);
        Element body = doc.body();

        String header = "<tr>" +
                "<th>شماره کلاس</th>" +
                "<th>نام استاد</th>" +
                "<th>نام درس</th>" +
                "<th>ردیف</th>" +
                "</tr>";

        for (int k = 0; k < 5; k++) {//days
            StringBuilder htm = new StringBuilder();
            Element el = body.select("#" + k + "shanbeh").get(0);
            for (int i = 4; i >= 0; i--) {//time slots
                htm.append("<td lass=\"innerCell\"> <table class=\"innerTable\"> ").append(header);
                int j = 0;
                for (Course c : assignment.getVariables()) {
                    RoomTimeSlot r = assignment.getValue(c);
                    if (r.getDay() == k && r.getTimeSlot() == i) {
                        htm.append("<tr style=\"width:150px\">")
                                .append("<td>").append(r.getRoom().getName()).append("</td>")
                                .append("<td>").append(c.getMasterId()).append("</td>")
                                .append("<td>").append(c.getName()).append("</td>")
                                .append("<td>").append(j++).append("</td>")
                                .append("</tr>");
                    }
                    j++;
                }
                htm.append("</td> </table>");
            }
            el.html(htm.toString());
        }

        IOUtils.write(doc.outerHtml().getBytes(), new FileOutputStream(name + ".html"));

    }


}
