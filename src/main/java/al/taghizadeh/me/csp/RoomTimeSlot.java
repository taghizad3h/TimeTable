package al.taghizadeh.me.csp;

/**
 * Created by Ali Asghar on 04/07/2017.
 */
public class RoomTimeSlot {
    private int day;
    private int timeSlot;
    private Room room;
    private RTSType type;

    public RTSType getType() {
        return type;
    }

    public void setType(RTSType type) {
        this.type = type;
    }

    enum RTSType{ForOneLecture, ForTwoLecture}

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
                && other.getRoom().equals(this.getRoom())
                && other.getTimeSlot() == this.getTimeSlot()
                && other.getType().equals(this.getType());
    }

    @Override
    public int hashCode() {
        int result = day;
        result = 31 * result + timeSlot;
        result = 31 * result + room.hashCode();
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "day:" + day + " timeslot:" + timeSlot + " room:" + room;
    }
}
