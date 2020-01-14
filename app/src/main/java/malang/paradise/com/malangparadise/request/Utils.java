package malang.paradise.com.malangparadise.request;

import android.app.Activity;
import android.os.Build;
import androidx.core.content.ContextCompat;

public class Utils {

    public static void darkenStatusBar(Activity activity, int color) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            activity.getWindow().setStatusBarColor(
                            ContextCompat.getColor(activity, color));
        }

    }

}
