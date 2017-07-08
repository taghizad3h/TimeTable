package al.taghizadeh.me.csp;


import java.util.HashSet;
import java.util.Set;

/**
 * Created by Ali Asghar on 02/07/2017.
 */

public class Master {
    private int masterId;
    private String name;
    private boolean busy;
    private Set<Course> totalCourses = new HashSet<>();
    private Set<Course> assignedCourses = new HashSet<>();

    public Master(int masterId, String name, boolean busy) {
        this.masterId = masterId;
        this.name = name;
        this.busy = busy;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Master && ((Master) obj).getMasterId() == this.getMasterId();
    }

    @Override
    public int hashCode() {
        return getMasterId();
    }

    public int getWeight(){
        return  totalCourses.size()/(totalCourses.size() - assignedCourses.size());
    }

    public int getMasterId() {
        return masterId;
    }

    public void setMasterId(int masterId) {
        this.masterId = masterId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isBusy() {
        return busy;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }

    public Set<Course> getTotalCourses() {
        return totalCourses;
    }

    public void setTotalCourses(Set<Course> totalCourses) {
        this.totalCourses = totalCourses;
    }

    public Set<Course> getAssignedCourses() {
        return assignedCourses;
    }

    public void setAssignedCourses(Set<Course> assignedCourses) {
        this.assignedCourses = assignedCourses;
    }
}
