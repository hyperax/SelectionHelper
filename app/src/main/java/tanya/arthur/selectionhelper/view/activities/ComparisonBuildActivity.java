package tanya.arthur.selectionhelper.view.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.ViewById;

import tanya.arthur.selectionhelper.R;
import tanya.arthur.selectionhelper.data.model.ComparisonInfo;
import tanya.arthur.selectionhelper.helpers.NpeUtils;
import tanya.arthur.selectionhelper.view.fragments.BaseFragment;
import tanya.arthur.selectionhelper.view.fragments.ComparisonCreateFragment;
import tanya.arthur.selectionhelper.view.fragments.CriteriaGroupsFragment;
import tanya.arthur.selectionhelper.view.fragments.VariantGroupsFragment;
import tanya.arthur.selectionhelper.view.notification.Letter;

@EActivity
public class ComparisonBuildActivity extends BaseActivity
        implements VariantGroupsFragment.Callback {

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.back)
    Button backButton;

    @ViewById(R.id.next)
    Button nextButton;

    @Extra
    @InstanceState
    long comparisonInfoId;

    private ComparisonInfo comparisonInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comparison_build);
        if (savedInstanceState == null) {
            replaceMainFragment(createVariantGroupsFragment());
        }
    }

    @AfterViews
    void init() {
        initToolbar();
        initData();
    }

    private void initData() {
        if (comparisonInfoId == 0) {
            setComparisonInfo(logic.createComparisonInfo());
            if (logic.saveComparisonInfo(comparisonInfo)) {
                comparisonInfoId = NpeUtils.getNonNull(comparisonInfo.getId());
            } else {
                showToast(Letter.alert().setText(getString(R.string.unable_create_comparison)));
                finish();
            }
        } else {
            setComparisonInfo(query.getComparisonInfo(comparisonInfoId));
        }
    }

    private void setComparisonInfo(ComparisonInfo comparisonInfo) {
        this.comparisonInfo = comparisonInfo;
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateNavigationButtons();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private VariantGroupsFragment createVariantGroupsFragment() {
        return VariantGroupsFragment.newInstance(comparisonInfo.getVariantGroupId());
    }

    private CriteriaGroupsFragment createCriteriaGroupsFragment() {
        return CriteriaGroupsFragment.newInstance(comparisonInfo.getCriteriaGroupId());
    }

    private void replaceMainFragment(Fragment fragment) {
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

    @Override
    public void onBackStackChanged() {
        super.onBackStackChanged();
        updateNavigationButtons();
    }

    private void updateNavigationButtons() {
        BaseFragment mainFragment = getMainFragment();
        boolean hasBackStack = getSupportFragmentManager().getBackStackEntryCount() > 0
                || mainFragment.hasBackStack();
        boolean isFinishStep = mainFragment instanceof ComparisonCreateFragment;
        backButton.setText(hasBackStack ? R.string.back : R.string.cancel);
        nextButton.setText(isFinishStep ? R.string.finish : R.string.next);
    }

    private BaseFragment getMainFragment() {
        return (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
    }

    @Override
    public void onClickVariantGroup(VariantGroupsFragment f, long variantGroupId) {
        comparisonInfo.setVariantGroupId(variantGroupId);
        logic.saveComparisonInfo(comparisonInfo);
    }

    @Click(R.id.back)
    void onClickBack() {
        onBackPressed();
    }

    @Click(R.id.next)
    void onClickNext() {
        Fragment currentFragment = getMainFragment();
        if (currentFragment instanceof VariantGroupsFragment) {
            if (comparisonInfo.getVariantGroupId() > 0) {
                replaceMainFragment(createCriteriaGroupsFragment(), null);
            } else {
                showToast(Letter.alert().setText(getString(R.string.setup_variant_group)));
            }
        } else if (currentFragment instanceof CriteriaGroupsFragment) {
            if (comparisonInfo.getCriteriaGroupId() > 0) {
                // TODO replace to variants quantity fragment
            } else {
                showToast(Letter.alert().setText(getString(R.string.setup_critera_group)));
            }
        }
    }
}
