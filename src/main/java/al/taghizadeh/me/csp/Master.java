package al.taghizadeh.me.csp;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ali Asghar on 02/07/2017.
 */

public class Master {
    private String masterId;
    private String name;
    private boolean compress;
    List<Integer> preferedDays = new ArrayList<>();

    public List<Integer> getPreferedDays() {
        return preferedDays;
    }

    public void setPreferedDays(List<Integer> preferedDays) {
        this.preferedDays = preferedDays;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Master && ((Master) obj).getMasterId().equals(this.getMasterId());
    }

    @Override
    public int hashCode() {
        return masterId.hashCode();
    }


    public String getMasterId() {
        return masterId;
    }

    public void setMasterId(String masterId) {
        this.masterId = masterId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCompress() {
        return compress;
    }

    public void setCompress(boolean compress) {
        this.compress = compress;
    }

}
