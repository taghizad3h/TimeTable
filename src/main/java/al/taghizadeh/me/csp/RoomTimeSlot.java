package al.taghizadeh.me.csp;

/**
 * Created by Ali Asghar on 04/07/2017.
 */
public class RoomTimeSlot {
    private int day;
    private int timeSlot;
    private Room room;
    TimeSlotType timeSlotType;
    enum TimeSlotType{REGULAR, TWO_HOUR}

    public TimeSlotType getTimeSlotType() {
        return timeSlotType;
    }

    public void setTimeSlotType(TimeSlotType timeSlotType) {
        this.timeSlotType = timeSlotType;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(int timeSlot) {
        this.timeSlot = timeSlot;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof RoomTimeSlot))
            return false;
        RoomTimeSlot other = (RoomTimeSlot)obj;

        return other.getDay() == this.getDay()
                && other.getRoom() == this.getRoom()
                && other.getTimeSlot() == this.getTimeSlot()
                && other.getTimeSlotType().equals(this.getTimeSlotType());
    }



    @Override
    public String toString() {
        return "day:" + day + " timeslot:" + timeSlot + " room:" + room;
    }

    public boolean equalTime(RoomTimeSlot value) {
        if(value == null)
            return false;
        return this.getDay() == value.getDay() && this.getTimeSlot() == value.getTimeSlot();
    }
}
