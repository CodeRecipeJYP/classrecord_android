package alpha.gentwihan.com.alpha.utils.permissions;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

/**
 * Created by jaeyoung on 8/6/17.
 */

public class PermissionUtils {

    public static void chkAudioNWriteRecordPermission(Activity activity) {
        chkPermissions(activity, new String[]{Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE});
    }

    private static void chkPermission(Activity activity, String permission) {
        if (ActivityCompat.checkSelfPermission(activity,
                permission) !=
                        PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{permission}, 0);
        }
    }

    private static void chkPermissions(Activity activity, String[] permissions) {
        for (String permission :
                permissions) {
            if (ActivityCompat.checkSelfPermission(activity,
                    permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, permissions, 0);
                return;
            }
        }
    }

}
