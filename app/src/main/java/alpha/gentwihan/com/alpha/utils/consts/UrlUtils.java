package alpha.gentwihan.com.alpha.utils.consts;

import alpha.gentwihan.com.alpha.BuildConfig;

/**
 * Created by jaeyoung on 8/6/17.
 */

public class UrlUtils {
    public static String getApiUrl() {
        return BuildConfig.DEBUG ?
                "http://52.198.142.127/" :
                "http://52.198.142.127/";
    }
}
