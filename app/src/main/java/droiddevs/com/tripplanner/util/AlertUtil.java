package droiddevs.com.tripplanner.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by jared.manfredi on 4/22/17.
 */

public class AlertUtil {
    private Context mContext;

    public AlertUtil(Context context) {
        mContext = context;
    }

    public void showAlert(String title, String message) {
        AlertDialog alert = new AlertDialog.Builder(mContext)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }
}
