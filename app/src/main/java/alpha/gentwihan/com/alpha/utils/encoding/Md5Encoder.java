package alpha.gentwihan.com.alpha.utils.encoding;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by jaeyoung on 8/6/17.
 */

public class Md5Encoder {
    public static String encodeMd5(String rawstring) {
        byte[] bytesOfMessage = new byte[0];
        try {
            bytesOfMessage = rawstring.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        byte[] result = null;
        try {
            result = MessageDigest.getInstance("MD5").digest(bytesOfMessage);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        BigInteger bigint = new BigInteger(1, result);
        String hashtext = bigint.toString(16);

        return hashtext;
    }
}
