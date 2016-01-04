package tanya.arthur.selectionhelper.helpers;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.annimon.stream.Optional;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import tanya.arthur.selectionhelper.R;

public class ViewUtils {

    private ViewUtils() {
    }

    public static void setEnabledWithChild(ViewGroup parentView, boolean isEnabled) {
        parentView.setEnabled(isEnabled);
        int childCount = parentView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parentView.getChildAt(i);
            if (child instanceof ViewGroup) {
                setEnabledWithChild((ViewGroup) child, isEnabled);
            } else {
                child.setEnabled(isEnabled);
            }
        }
    }

    public static void disableSoftInputFromAppearing(EditText editText) {
        if (Build.VERSION.SDK_INT >= 21) {
            editText.setShowSoftInputOnFocus(false);
        } else {
            try {
                final Method method = EditText.class.getMethod("setShowSoftInputOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(editText, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void hideSoftKeyboard(View view) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) view.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void enableIconsForPopupMenu(PopupMenu popupMenu) {
        try {
            Field[] fields = popupMenu.getClass().getDeclaredFields();
            for (Field field : fields) {
                if ("mPopup".equals(field.getName())) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popupMenu);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper
                            .getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod(
                            "setForceShowIcon", boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    public static int getColor(Activity activity, int colorResId) {
        int resultColor;
        if(Build.VERSION.SDK_INT >= 23){
            resultColor = activity.getResources().getColor(colorResId, activity.getTheme());
        } else {
            resultColor = activity.getResources().getColor(colorResId);
        }

        return resultColor;
    }

    public static void showOKDialog(Context context, int title, int message) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.ok, null)
                .show();
    }

    @NonNull
    public static <T> Optional<T> findRecurs(@NonNull View rootView, @IdRes int viewId, @NonNull Class<T> classType) {
        List<ViewGroup> searchList = new ArrayList<>();

        if (rootView instanceof ViewGroup) {
            searchList.add((ViewGroup) rootView);
        }

        while (searchList.size() != 0) {
            ViewGroup groupToCheck = searchList.get(0);
            int childCount = groupToCheck.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = groupToCheck.getChildAt(i);
                if (child.getId() == viewId && classType.isInstance(child)) {
                    return Optional.of(classType.cast(child));
                } else if (child instanceof ViewGroup) {
                    searchList.add((ViewGroup) child);
                }
            }
            searchList.remove(0);
        }

        return Optional.empty();
    }

    @NonNull
    public static Optional<View> findRecurs(@NonNull View rootView, @IdRes int viewId) {
        return findRecurs(rootView, viewId, View.class);
    }

    @NonNull
    public static Optional<MenuItem> findItem(@NonNull Menu menu, @IdRes int itemId) {
        return Optional.ofNullable(menu.findItem(itemId));
    }

    @NonNull
    public static String getString(@NonNull Context context, @StringRes int str, @StringRes int... argRes) {
        if (!NpeUtils.isEmpty(argRes)) {
            int size = argRes.length;
            Object argString[] = new String[size];
            for (int i=0;i<size;i++) {
                argString[i] = context.getString(argRes[i]);
            }
            return context.getString(str, argString);
        }
        return context.getString(str);
    }

    @NonNull
    public static String extractText(@Nullable TextView textView) {
        if (textView == null) {
            return NpeUtils.EMPTY_STRING;
        } else {
            return textView.getText().toString().trim();
        }
    }

    public static long extractLong(@Nullable TextView textView) {
        return ConvertUtils.safeParseLong(extractText(textView));
    }
}
