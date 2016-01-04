package tanya.arthur.selectionhelper.view.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import org.androidannotations.annotations.EActivity;

import tanya.arthur.selectionhelper.App;
import tanya.arthur.selectionhelper.view.fragments.BaseFragment;
import tanya.arthur.selectionhelper.view.helpers.TitleUpdateListener;
import tanya.arthur.selectionhelper.view.notification.Letter;
import tanya.arthur.selectionhelper.view.notification.Notice;

@EActivity
public class BaseActivity extends RxAppCompatActivity
        implements FragmentManager.OnBackStackChangedListener, TitleUpdateListener {

    @org.androidannotations.annotations.App
    protected App app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().addOnBackStackChangedListener(this);
    }

    @Override
    public void onBackStackChanged() {

    }

    @Override
    public boolean updateTitleRequest(BaseFragment fragment) {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        app.getRefWatcher().watch(this);
    }

    public void showSnackbar(Letter mes) {
        Notice.showSnackbar(this, null, mes);
    }

    public void showToast(Letter mes) {
        Notice.showToast(this, mes);
    }
}
