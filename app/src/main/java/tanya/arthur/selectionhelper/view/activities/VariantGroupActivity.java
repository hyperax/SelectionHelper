package tanya.arthur.selectionhelper.view.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.ViewById;

import tanya.arthur.selectionhelper.R;
import tanya.arthur.selectionhelper.data.model.VariantGroup;
import tanya.arthur.selectionhelper.helpers.NpeUtils;
import tanya.arthur.selectionhelper.view.fragments.BaseFragment;
import tanya.arthur.selectionhelper.view.fragments.VariantGroupFragment;
import tanya.arthur.selectionhelper.view.notification.Letter;

@EActivity
public class VariantGroupActivity extends BaseActivity {

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @Extra
    @InstanceState
    long variantGroupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_fragment);
    }

    @AfterViews
    void init() {
        initToolbar();
        initData();
        initFragments();
    }

    private void initFragments() {
        if (getMainFragment() == null) {
            replaceMainFragment(createVariantGroupFragment());
        }
    }

    private VariantGroupFragment createVariantGroupFragment() {
        return VariantGroupFragment.newInstance(variantGroupId);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void initData() {
        if (variantGroupId == 0L) {
            VariantGroup vg = logic.createVariantGroup();
            if (logic.saveVariantGroup(vg)) {
                variantGroupId = NpeUtils.getNonNull(vg.getId());
            } else {
                showToast(Letter.alert().setText(getString(R.string.unable_create_variant_group)));
                finish();
            }
        }
    }

    private void replaceMainFragment(BaseFragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                        android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private void replaceMainFragment(Fragment fragment, String backStackName) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                        android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(backStackName)
                .commit();
    }

    @Override
    public boolean updateTitleRequest(BaseFragment fragment) {
        toolbar.setTitle(fragment.getTitle());
        return true;
    }

    private BaseFragment getMainFragment() {
        return (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
    }
}
