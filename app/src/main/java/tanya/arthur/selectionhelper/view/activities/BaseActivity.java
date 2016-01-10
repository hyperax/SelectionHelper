package tanya.arthur.selectionhelper.view.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

import tanya.arthur.selectionhelper.SHApp;
import tanya.arthur.selectionhelper.data.Logic;
import tanya.arthur.selectionhelper.data.sqlite.DataQuery;
import tanya.arthur.selectionhelper.view.fragments.BaseFragment;
import tanya.arthur.selectionhelper.view.helpers.TitleUpdateListener;
import tanya.arthur.selectionhelper.view.notification.Letter;
import tanya.arthur.selectionhelper.view.notification.Notice;

@EActivity
public class BaseActivity extends RxAppCompatActivity
        implements FragmentManager.OnBackStackChangedListener, TitleUpdateListener {

    @App
    protected SHApp app;

    @Bean
    protected DataQuery query;

    @Bean
    protected Logic logic;

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
