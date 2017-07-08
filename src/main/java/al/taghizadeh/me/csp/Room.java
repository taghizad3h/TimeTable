package al.taghizadeh.me.csp;

/**
 * Created by Ali Asghar on 02/07/2017.
 */

public class Room {
    String name;
    int capacity;
    String equipmentId;

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Room && this.getName().equals(((Room)obj).getName());
    }

    @Override
    public String toString() {
        return "name: " + name + " capacity:" + capacity + " equipmentId:" + equipmentId;
    }
}
