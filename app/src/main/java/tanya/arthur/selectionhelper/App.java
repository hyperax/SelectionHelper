package tanya.arthur.selectionhelper;

import android.app.Application;
import android.support.annotation.NonNull;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import org.androidannotations.annotations.EApplication;

@EApplication
public class App extends Application {

    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        refWatcher = LeakCanary.install(this);
    }

    @NonNull
    public RefWatcher getRefWatcher() {
        return refWatcher;
    }
}
