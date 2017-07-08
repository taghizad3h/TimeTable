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
        List<RoomTimeSlot> values = new ArrayList<>();
        List<RoomTimeSlot> zmayesgahValues = new ArrayList<>();
        List<Room> rooms = new ArrayList<>();
        Map<String, List<RoomTimeSlot>> eqToRTS = new HashMap<>();
        try {
            File input = new File(file);
            Scanner scanner = new Scanner(input);
            String name = scanner.nextLine().replace("Name: ", "");
            int courseCount = Integer.valueOf(scanner.nextLine().replace("Courses: ", ""));
            int roomCount = Integer.valueOf(scanner.nextLine().replace("Rooms: ", ""));
            int masterCount = Integer.valueOf(scanner.nextLine().replace("Masters: ", ""));
            int groupCount = Integer.valueOf(scanner.nextLine().replace("Groups: ", ""));
            int days = Integer.valueOf(scanner.nextLine().replace("Weekdays: ", ""));
            int timeSlots = Integer.valueOf(scanner.nextLine().replace("Timeslots: ", ""));
            int unAvailables = Integer.valueOf(scanner.nextLine().replace("Unavailablities: ", ""));
            scanner.nextLine();
            for (int i = 0; i < courseCount; i++) {
                String sCourse[] = scanner.nextLine().split(" ");
                String courseName = sCourse[0];
                int daysPerWeek = Integer.valueOf(sCourse[1]);
                String masterId = sCourse[2];
                int groupId = Integer.valueOf(sCourse[3]);
                int capacity = Integer.valueOf(sCourse[4]);
                String equimentId = null;
                if (sCourse.length > 5)
                    equimentId = sCourse[5];
                if (daysPerWeek > 1) {
                    for (int j = 0; j < daysPerWeek; j++) {
                        Course c = new Course(courseName + "-" + j);
                        c.setCapacity(capacity);
                        c.setDaysPerWeek(daysPerWeek);
                        c.setMasterId(masterId);
                        c.setGroupId(groupId);
                        c.setEquipmentId(equimentId);
                        c.setCourseType(Course.CourseType.TWO_LECTURE);
                        courses.add(c);
                    }
                } else {
                    Course c = new Course(courseName);
                    c.setCapacity(capacity);
                    c.setDaysPerWeek(daysPerWeek);
                    c.setMasterId(masterId);
                    c.setGroupId(groupId);
                    c.setCourseType(Course.CourseType.ONE_LECTURE);
                    c.setEquipmentId(equimentId);
                    courses.add(c);
                }


            }
            scanner.nextLine();
            for (int i = 0; i < roomCount; i++) {
                String sRoom[] = scanner.nextLine().split(" ");
                Room room = new Room();
                room.setName(sRoom[0]);
                room.setCapacity(Integer.valueOf(sRoom[1]));
                if (sRoom.length > 2) {
                    String eq = sRoom[2];
                    room.setEquipmentId(eq);
                }
                rooms.add(room);
            }
            int id = 0;
            for (int i = 0; i < days; i++) {//weekdays
                for (int j = 0; j < timeSlots; j++) {//time slots
                    for (int k = 0; k < roomCount; k++) {//rooms
                        if(rooms.get(k).getEquipmentId() == null) {
                            RoomTimeSlot r = new RoomTimeSlot();
                            r.setDay(i);
                            r.setTimeSlot(j);
                            r.setRoom(rooms.get(k));
                            values.add(r);
                        }
                    }
                }
            }

            for (int i = 0; i < days; i++) {//weekdays
                for (int j = 0; j < 3; j++) {//time slots
                    for (int k = 0; k < roomCount; k++) {//rooms
                        if (rooms.get(k).getEquipmentId() != null) {
                            RoomTimeSlot r = new RoomTimeSlot();
                            r.setDay(i);
                            r.setTimeSlot(j);
                            r.setRoom(rooms.get(k));
                            String eq = r.getRoom().getEquipmentId();
                            if (eqToRTS.containsKey(eq)) {
                                eqToRTS.get(eq).add(r);
                            } else {
                                eqToRTS.put(eq, new ArrayList<>(Collections.singletonList(r)));
                            }

                        }
                    }
                }
            }
            scanner.nextLine();
            return new TimeTable(courses, values, eqToRTS);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
