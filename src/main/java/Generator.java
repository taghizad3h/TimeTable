import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * Created by Ali Asghar on 05/07/2017.
 */
public class Generator {
    public static void main(String[] args) throws IOException {
        Random r = new Random();
        String name = "Toy";
        int courseCount = 64;
        int roomCount = 32;
        int masterCount = 8;
        int weekDays = 5;
        int groupCount = 4;
        int timeSlots = 7;
        int unavailbalityCount = 4;
        System.out.println("Input Generator");
        File f = new File("input.txt");
        FileWriter writer = new FileWriter(f);
        writer.write("Name: " + name + "\n");
        writer.write("Courses: " + courseCount + "\n");
        writer.write("Rooms: " + roomCount + "\n");
        writer.write("Masters: " + masterCount + "\n");
        writer.write("Groups: " + groupCount + "\n");
        writer.write("Weekdays: " + weekDays + "\n");
        writer.write("Timeslots: " + timeSlots + "\n");
        writer.write("Unavailablities: " + unavailbalityCount + "\n");
        writer.write("CourseName:DaysPerWeek:MasterId:GroupId:Students:EquipmentId" + "\n");
        int groupId = -1;
        int masterId = 0;
        int students = 0;
        int reqirementId = 0;
        boolean requirement = false;
        for (int i = 0; i < courseCount; i++) {
            if(i%16 == 0) {
                groupId++;
                requirement = true;
            }else{
                requirement = false;
            }
            if(i%masterCount == 0)
                masterId = 0;
            if(i%64 == 0)
                reqirementId = 0;
            students = 20 + r.nextInt(30);
            if(requirement)
                writer.write("Course" + i + " " + 1 + " " + masterId++ + " " + groupId + " " + students + " eq"+reqirementId++ + "\n");
            else
                writer.write("Course" + i + " " + 2 + " " + masterId++ + " " + groupId + " " + students+ "\n");
        }
        writer.write("RoomName:Capacity:EquipmentId"+ "\n");
        reqirementId = 0;
        for (int i = 0; i < roomCount; i++) {
            requirement = i % 4 == 0;
            students = 30 + r.nextInt(30);
            if(requirement)
                writer.write("Room" + i + " " + students + " eq" + reqirementId++ + "\n");
            else
                writer.write("Room" + i + " " + students + "\n");
        }
        writer.write("MasterName:PreferredDays:Compress" + "\n");
        for (int i = 0; i < masterCount; i++) {
            writer.write(i + " 1 1 1 1 1 " + "true" + "\n");
        }
        writer.close();
    }
}
