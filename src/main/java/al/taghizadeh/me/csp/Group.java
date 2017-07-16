package al.taghizadeh.me.csp;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Ali Asghar on 02/07/2017.
 */
public class Group {
    int groupId;
    private Set<Course> totalCourses = new HashSet<>();
    private Set<Course> assignedCourses = new HashSet<>();
    public Group(int groupId){
        this.groupId = groupId;
    }
    public int getWeight(){
        return  totalCourses.size()/(totalCourses.size() - assignedCourses.size());
    }

    @Override
    public int hashCode() {
        return this.groupId;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Group && ((Group)obj).groupId == this.groupId;
    }
}
