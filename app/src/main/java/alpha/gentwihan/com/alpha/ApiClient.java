package alpha.gentwihan.com.alpha;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by se780 on 2017-08-05.
 */

public interface ApiClient {

    @POST("/api/users/")
    Call<User> getUser(@Body LoginUser loginuser);
}
