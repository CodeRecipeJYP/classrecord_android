package alpha.gentwihan.com.alpha.utils.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by jaeyoung on 8/6/17.
 */

public class GsonUtils {
    private static Gson sGson;

    public static Gson getGson() {
        if (sGson == null) {
            sGson = new GsonBuilder()
                    .create();
        }

        return sGson;
    }
}