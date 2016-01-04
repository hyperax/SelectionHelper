package tanya.arthur.selectionhelper.view.notification;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import tanya.arthur.selectionhelper.R;
import tanya.arthur.selectionhelper.helpers.ViewUtils;

public class Notice {

    private Notice() {
    }

    public static void showSnackbar(Activity activity, @Nullable View view, Letter mes) {
        @SuppressWarnings("ResourceType")
        Snackbar sb = Snackbar.make(
                view != null ? view : activity.findViewById(android.R.id.content),
                mes.getText(),
                mes.getDuration());

        View snackBarView = sb.getView();
        int messageColor = ViewUtils.getColor(activity, getMessageColorRes(mes.getType()));
        snackBarView.setBackgroundColor(messageColor);
        sb.show();
    }

    public static void showToast(Context context, Letter mes) {
        int duration;
        switch (mes.getDuration()) {
            case Letter.INDEFINITE:
                // fail through
            case Letter.LONG:
                duration = Toast.LENGTH_LONG;
                break;
            default:
                duration = Toast.LENGTH_SHORT;
                break;
        }
        Toast toast = Toast.makeText(context, mes.getText(), duration);
        int messageColor = getMessageColorRes(mes.getType());
        toast.getView().setBackgroundResource(messageColor);
        toast.show();
    }

    private static int getMessageColorRes(@Letter.Type int type) {
        switch (type) {
            case Letter.ALERT:
                return R.color.message_alert;
            case Letter.CONFIRM:
                return R.color.message_confirm;
            default:
                return R.color.message_info;
        }
    }
}
