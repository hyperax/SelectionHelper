package tanya.arthur.selectionhelper.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.trello.rxlifecycle.components.support.RxFragment;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;

import tanya.arthur.selectionhelper.SHApp;
import tanya.arthur.selectionhelper.data.Logic;
import tanya.arthur.selectionhelper.data.sqlite.DataQuery;
import tanya.arthur.selectionhelper.helpers.Savable;
import tanya.arthur.selectionhelper.view.helpers.TitleUpdateListener;
import tanya.arthur.selectionhelper.view.notification.Letter;
import tanya.arthur.selectionhelper.view.notification.Notice;

@EFragment
public class BaseFragment extends RxFragment {

    private static final String SAVE_STATE = "save_state";

    @App
    protected SHApp app;

    @Bean
    protected Logic logic;

    @Bean
    protected DataQuery query;

    @FragmentArg
    int iconRes = 0;
    @FragmentArg
    String title = "";
    @FragmentArg
    int titleRes = 0;
    @FragmentArg
    String subTitle = "";
    @FragmentArg
    int subTitleRes = 0;

    protected Object getHostParent() {
        Object hostParent = null;
        if (getParentFragment() != null) {
            hostParent = getParentFragment();
        } else if (getActivity() != null) {
            hostParent = getActivity();
        }
        return hostParent;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateTitleRequest();
    }

    protected boolean updateTitleRequest() {
        boolean titleUpdated = false;
        Object parent = getHostParent();
        if (parent instanceof TitleUpdateListener) {
            titleUpdated = ((TitleUpdateListener) parent).updateTitleRequest(this);
        }

        return titleUpdated;
    }

    public final String getTitle() {
        if (titleRes != 0) {
            return getString(titleRes);
        } else if (!TextUtils.isEmpty(title)) {
            return title;
        }
        return getInstanceTitle();
    }

    protected String getInstanceTitle() {
        return "";
    }

    public final String getSubtitle() {
        if (subTitleRes != 0) {
            return getString(subTitleRes);
        } else if (!TextUtils.isEmpty(subTitle)) {
            return subTitle;
        }
        return getInstanceSubTitle();
    }

    protected String getInstanceSubTitle() {
        return "";
    }

    public final int getIcon() {
        if (iconRes != 0) {
            return iconRes;
        }
        return getInstanceIcon();
    }

    protected int getInstanceIcon() {
        return 0;
    }

    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onPause() {
        super.onPause();
        saveState();
    }

    private void saveState() {
        if (this instanceof Savable) {
            getArguments().putBundle(SAVE_STATE, ((Savable) this).getBundleSaveState());
        }
    }

    @Nullable
    protected Bundle restoreSavedState() {
        return getArguments().getBundle(SAVE_STATE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        app.getRefWatcher().watch(this);
    }

    protected void showSnackbar(Letter mes) {
        if (getActivity() != null) {
            Notice.showSnackbar(getActivity(), null, mes);
        }
    }

    protected void showToast(Letter mes) {
        if (getActivity() != null) {
            Notice.showToast(getActivity(), mes);
        }
    }

    public boolean hasBackStack() {
        return false;
    }

}
