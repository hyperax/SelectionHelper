package tanya.arthur.selectionhelper.view.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import tanya.arthur.selectionhelper.R;
import tanya.arthur.selectionhelper.view.fragments.BaseFragment;
import tanya.arthur.selectionhelper.view.fragments.ComparisonCreateFragment;
import tanya.arthur.selectionhelper.view.fragments.VariantGroupsFragment;

@EActivity
public class ComparisonBuildActivity extends BaseActivity
        implements VariantGroupsFragment.Callback {

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.back)
    Button backButton;

    @ViewById(R.id.next)
    Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comparison_build);
        if (savedInstanceState == null) {
            initStartFragment();
        }
    }

    @AfterViews
    void init() {
        initToolbar();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void initStartFragment() {
        replaceMainFragment(createVariantGroupsFragment());
    }

    private VariantGroupsFragment createVariantGroupsFragment() {
        return VariantGroupsFragment.newInstance();
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
    public void onCreateVariantGroups(VariantGroupsFragment f) {

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
        boolean canGoNext = mainFragment instanceof ComparisonCreateFragment;
        backButton.setText(hasBackStack? R.string.back : R.string.cancel);
        nextButton.setText(canGoNext? R.string.next : R.string.finish);
    }

    private BaseFragment getMainFragment() {
        return (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
    }

    @Override
    public void onClickVariantGroup(VariantGroupsFragment f, long variantGroupId) {

    }

    @Click(R.id.back)
    void onClickBack() {
        onBackPressed();
    }

    @Click(R.id.next)
    void onClickNext() {
        BaseFragment mainFragment = getMainFragment();
    }
}
