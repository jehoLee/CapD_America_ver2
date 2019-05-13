package ajou.com.skechip.Fragment.bean;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MeetingEntity {
    public static final String DATE_FORMAT_PATTERN = "yyyy년 MM월 dd일";

    private String title;
    private String location;
    private SimpleDateFormat date;

    public MeetingEntity () {

        date = new SimpleDateFormat(DATE_FORMAT_PATTERN);
    }

}
