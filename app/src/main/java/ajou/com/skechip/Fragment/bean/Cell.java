package ajou.com.skechip.Fragment.bean;

//import android.os.Parcel;
//import android.os.Parcelable;

/**
 * Created by zhouchaoyuan on 2017/1/14.
 */

public class Cell {

    private int status;// 과목
    private String PlaceName;//
    private String SubjectName;//
    private String startTime;
    private String weekofday;


    //weekDay
    //cellPosition

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPlaceName() {
        return PlaceName;
    }

    public void setPlaceName(String PlaceName) {
        this.PlaceName = PlaceName;
    }

    public String getSubjectName() { return SubjectName; }

    public void setSubjectName(String SubjectName) {
        this.SubjectName =SubjectName;
    }

    public String getStartTime() { return startTime; }

    public void setStartTime(String StartTime) { this.startTime = StartTime; }

    public String getWeekofday() { return weekofday; }

    public void setWeekofday(String weekofday) { this.weekofday = weekofday; }

}
