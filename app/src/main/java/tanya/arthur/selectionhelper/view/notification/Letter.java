package tanya.arthur.selectionhelper.view.notification;

import android.support.annotation.IntDef;
import android.support.design.widget.Snackbar;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Letter implements Serializable {

    @IntDef({INFO, CONFIRM, ALERT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {}
    public static final int INFO = 1;
    public static final int CONFIRM = 2;
    public static final int ALERT = 4;

    @IntDef({SHORT, LONG, INDEFINITE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Duration {}
    public static final int SHORT = Snackbar.LENGTH_SHORT;
    public static final int LONG = Snackbar.LENGTH_LONG;
    public static final int INDEFINITE = Snackbar.LENGTH_INDEFINITE;

    @Type
    private int type;

    @Duration
    private int duration;

    private String text;

    private Letter() {
        setType(INFO);
        setDuration(LONG);
    }

    public static Letter get() {
        return new Letter();
    }

    @Type
    public int getType() {
        return type;
    }

    public Letter setType(@Type int type) {
        this.type = type;
        return this;
    }

    @Duration
    public int getDuration() {
        return duration;
    }

    public Letter setDuration(@Duration int duration) {
        this.duration = duration;
        return this;
    }

    public String getText() {
        return text;
    }

    public Letter setText(String text) {
        this.text = text;
        return this;
    }
}
