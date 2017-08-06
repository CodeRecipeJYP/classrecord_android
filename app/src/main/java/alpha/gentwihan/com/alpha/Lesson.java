package alpha.gentwihan.com.alpha;

/**
 * Created by se780 on 2017-08-06.
 */

public class Lesson {
    private int id;
    private int year;
    private String term;
    private String courseCode;
    private String courseName;
    private int day;
    private int startTime;
    private int endTime;
    private String buildingName;
    private String roomType;
    private String roomName;
    private String profName;
    private String created;

    public int getDay() {
        return day;
    }

    public int getEndTime() {
        return endTime;
    }

    public int getStartTime() {
        return startTime;
    }

    public String getTerm() {
        return term;
    }

    public int getYear() {
        return year;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCreated() {
        return created;
    }

    public String getProfName() {
        return profName;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getRoomType() {
        return roomType;
    }
}
