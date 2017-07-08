package al.taghizadeh.me.csp;

import al.taghizadeh.csp.Variable;

/**
 * Created by Ali Asghar on 04/07/2017.
 */
public class Course extends Variable {
    private int daysPerWeek;
    private int duration;
    private int capacity;
    private String masterId;
    private int groupId;
    private RoomTimeSlot roomTimeSlots;
    private String equipmentId;
    private CourseType courseType;
    enum CourseType{ONE_LECTURE, TWO_LECTURE};

    public String getEquipmentId() {
        return equipmentId;
    }

    public Course(String name) {
        super(name);
    }

    public int getDaysPerWeek() {
        return daysPerWeek;
    }

    public void setDaysPerWeek(int daysPerWeek) {
        this.daysPerWeek = daysPerWeek;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public RoomTimeSlot getRoomTimeSlots() {
        return roomTimeSlots;
    }

    public void setRoomTimeSlots(RoomTimeSlot roomTimeSlots) {
        this.roomTimeSlots = roomTimeSlots;
    }

    public String getMasterId() {
        return masterId;
    }

    public void setMasterId(String masterId) {
        this.masterId = masterId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public CourseType getCourseType() {
        return courseType;
    }

    public void setCourseType(CourseType courseType) {
        this.courseType = courseType;
    }

    @Override
    public String toString() {
        return "name:" + getName() + " group:" + groupId + " masterId:" + masterId + " capacity:" + capacity + " requirement:" + equipmentId;
    }
}
