package alpha.gentwihan.com.alpha;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by se780 on 2017-08-05.
 */

public interface ApiClient {

    @POST("/api/users/")
    Call<User> getUser(@Body LoginUser loginuser);

    @GET("/api/initializecourse/")
    Call<List<Lesson>> getLesson(@Query("portalaccount") String account,
                                 @Query("password") String pw, @Header("Authorization") String auth);
}
