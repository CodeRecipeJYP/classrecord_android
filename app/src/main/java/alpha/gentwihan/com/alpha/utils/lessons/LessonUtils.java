package alpha.gentwihan.com.alpha.utils.lessons;

import java.util.List;

import alpha.gentwihan.com.alpha.Lesson;

/**
 * Created by se780 on 2017-08-06.
 */

public class LessonUtils {
    private static LessonUtils mInstance;
    private List<Lesson> lessons;

    public static LessonUtils getInstance() {
        if (mInstance == null) {
            synchronized (LessonUtils.class) {
                if (mInstance == null) {
                    mInstance = new LessonUtils();
                }
            }
        }
        return mInstance;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }
}
