package al.taghizadeh.me.csp;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by Ali Asghar on 03/07/2017.
 */
public class InputParser {

    /**
     * This method reads the input file and create a @link{Model} instance
     * which is used in later steps to build timeTable upon it.
     */
    static Logger logger = Logger.getLogger(InputParser.class);

    public TimeTable parse(String file) {
        logger.info("reading input");
        List<Course> courses = new ArrayList<>();
        List<RoomTimeSlot> vlsForTwoLecture = new ArrayList<>();
        List<RoomTimeSlot> vlsForOneLecture = new ArrayList<>();
        List<Master> masters = new ArrayList<>();
        List<RoomTimeSlot> zmayesgahValues = new ArrayList<>();
        List<Room> rooms = new ArrayList<>();
        Map<String, List<RoomTimeSlot>> eqToRTS = new HashMap<>();
        try {
            File input = new File(file);
            Scanner scanner = new Scanner(input);
            String name = scanner.nextLine().split("\t")[1];
            int courseCount = Integer.valueOf(scanner.nextLine().split("\t")[1]);
            int roomCount = Integer.valueOf(scanner.nextLine().split("\t")[1]);
            int masterCount = Integer.valueOf(scanner.nextLine().split("\t")[1]);
            int days = Integer.valueOf(scanner.nextLine().split("\t")[1]);
            int twoLectureTimeSlots = Integer.valueOf(scanner.nextLine().split("\t")[1]);
            int oneLectureTimeSlots = Integer.valueOf(scanner.nextLine().split("\t")[1]);
            logger.info("building a timetable for " + name);
            logger.info("number of courses " + courseCount);
            logger.info("number of masters " + masterCount);
            logger.info("weekdays " + days);
            logger.info("two lecture course time slots " + twoLectureTimeSlots);
            logger.info("one lecture course time slots " + oneLectureTimeSlots);
            scanner.nextLine();
            logger.info("reading courses info");
            for (int i = 0; i < courseCount; i++) {
                String sCourse[] = scanner.nextLine().split("\t");
                String id = sCourse[0];
                String courseName = sCourse[1];
                int daysPerWeek = Integer.valueOf(sCourse[2]);
                String masterId = sCourse[3];
                int groupId = Integer.valueOf(sCourse[4]);
                int capacity = Integer.valueOf(sCourse[5]);
                String equimentId = null;
                if (sCourse.length > 6)
                    equimentId = sCourse[6];
                if (daysPerWeek > 1) {
                    for (int j = 0; j < daysPerWeek; j++) {
                        Course c = new Course(id + "-" + courseName+ "-" + j);
//                        c.setName(courseName);
                        c.setCapacity(capacity);
                        c.setDaysPerWeek(daysPerWeek);
                        c.setMasterId(masterId);
                        c.setGroupId(groupId);
                        c.setEquipmentId(equimentId);
                        c.setCourseType(Course.CourseType.TWO_LECTURE);
                        courses.add(c);
                    }
                } else {
                    Course c = new Course(id + "-" + courseName);
//                    c.setName(courseName);
                    c.setCapacity(capacity);
                    c.setDaysPerWeek(daysPerWeek);
                    c.setMasterId(masterId);
                    c.setGroupId(groupId);
                    c.setCourseType(Course.CourseType.ONE_LECTURE);
                    c.setEquipmentId(equimentId);
                    courses.add(c);
                }
            }
            logger.info("reading masters info");
            scanner.nextLine();
            for (int i = 0; i < masterCount; i++) {
                String sMaster[] = scanner.nextLine().split("\t");
                Master m = new Master();
                m.setMasterId(sMaster[0]);
                m.setName(sMaster[1]);
                for (int j = 0; j < sMaster[2].length(); j++) {
                    m.getPreferedDays().add(sMaster[2].charAt(j) - 49);
                }
                if (sMaster[0].equalsIgnoreCase("1"))
                    m.setCompress(true);
                masters.add(m);
            }

            logger.info("reading rooms info");
            scanner.nextLine();
            for (int i = 0; i < roomCount; i++) {
                String sRoom[] = scanner.nextLine().split("\t");
                Room room = new Room();
                room.setName(sRoom[1]);
                room.setCapacity(Integer.valueOf(sRoom[2]));
                if (sRoom.length > 3) {
                    room.setEquipmentId(sRoom[3]);
                }
                rooms.add(room);
            }

            for (int i = 0; i < days; i++) {//weekdays
                for (int j = 0; j < twoLectureTimeSlots; j++) {//time slots
                    for (int k = 0; k < roomCount; k++) {//rooms
                        if(rooms.get(k).getEquipmentId() != null)//skip for az classes
                            continue;
                        RoomTimeSlot r = new RoomTimeSlot();
                        r.setDay(i);
                        r.setTimeSlot(j);
                        r.setRoom(rooms.get(k));
                        r.setType(RoomTimeSlot.RTSType.ForTwoLecture);
                        vlsForTwoLecture.add(r);
                    }
                }

            }
            for (int i = 0; i < days; i++) {//weekdays
                for (int j = 0; j < oneLectureTimeSlots; j++) {//time slots
                    for (int k = 0; k < roomCount; k++) {//rooms
                        RoomTimeSlot r = new RoomTimeSlot();
                        r.setDay(i);
                        r.setTimeSlot(j);
                        r.setRoom(rooms.get(k));
                        r.setType(RoomTimeSlot.RTSType.ForOneLecture);
                        vlsForOneLecture.add(r);
                        String eq = rooms.get(k).getEquipmentId();
                        if (eq != null) {
                            if (eqToRTS.containsKey(eq)) {
                                eqToRTS.get(eq).add(r);
                            } else {
                                eqToRTS.put(eq, new ArrayList<>(Collections.singletonList(r)));
                            }
                        }
                    }
                }

            }
            return new TimeTable(courses, vlsForTwoLecture, vlsForOneLecture,eqToRTS, masters);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
