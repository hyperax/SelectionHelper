package tanya.arthur.selectionhelper.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.ViewById;

import tanya.arthur.selectionhelper.R;
import tanya.arthur.selectionhelper.view.fragments.BaseFragment;
import tanya.arthur.selectionhelper.view.fragments.CriteriaGroupFragment;

@EActivity
public class CriteriaGroupActivity extends BaseActivity implements CriteriaGroupFragment.Callback {

    public static final String EXTRA_CRITERIA_GROUP_ID = "extra_criteria_group_id";

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @Extra(EXTRA_CRITERIA_GROUP_ID)
    @InstanceState
    long criteriaGroupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_fragment);
    }

    @AfterViews
    void init() {
        initToolbar();
        initFragments();
    }

    private void initFragments() {
        if (getMainFragment() == null) {
            replaceMainFragment(createCriteriaGroupFragment());
        }
    }

    private CriteriaGroupFragment createCriteriaGroupFragment() {
        return CriteriaGroupFragment.newInstance(criteriaGroupId);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
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

    @Override
    public void onDone(CriteriaGroupFragment f, long criteriaGroupId) {
        this.criteriaGroupId = criteriaGroupId;
        setResult(RESULT_OK, new Intent().putExtra(EXTRA_CRITERIA_GROUP_ID, criteriaGroupId));
        finish();
    }
}
