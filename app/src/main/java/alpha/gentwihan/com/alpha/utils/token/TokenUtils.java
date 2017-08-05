package alpha.gentwihan.com.alpha.utils.token;

/**
 * Created by se780 on 2017-08-06.
 */

public class TokenUtils {
    private static TokenUtils mInstance;
    private String mToken;

    public static TokenUtils getInstance() {
        if (mInstance == null) {
            synchronized (TokenUtils.class) {
                if (mInstance == null) {
                    mInstance = new TokenUtils();
                }
            }
        }
        return mInstance;
    }

    public String getToken() {
        return mToken;
    }

    public void setToken(String mToken) {
        this.mToken = mToken;
    }
}
