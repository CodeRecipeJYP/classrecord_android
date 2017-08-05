package alpha.gentwihan.com.alpha;

import java.util.List;

import alpha.gentwihan.com.alpha.data.models.RecordDto;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by se780 on 2017-08-05.
 */

public interface ApiClient {

    @POST("/api/users/")
    Call<User> getUser(@Body LoginUser loginuser);


    @Multipart
    @POST("/api/records/")
    Call<okhttp3.ResponseBody> createRecord(
            @Header("Authorization") String token,
            @Part("courseId") RequestBody course_id,
//            @Part("duration") String duration,
            @Part("filename") RequestBody filename,
            @Part MultipartBody.Part file
    );

    @GET("/api/initializecourse/")
    Call<List<Lesson>> getLesson(@Query("portalaccount") String account,
                                 @Query("password") String pw, @Header("Authorization") String auth);

}
