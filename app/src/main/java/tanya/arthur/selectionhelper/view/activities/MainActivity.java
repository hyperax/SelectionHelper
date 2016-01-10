package tanya.arthur.selectionhelper.view.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import tanya.arthur.selectionhelper.R;
import tanya.arthur.selectionhelper.view.fragments.BaseFragment;
import tanya.arthur.selectionhelper.view.fragments.ComparisonListFragment;

@EActivity
public class MainActivity extends BaseActivity implements ComparisonListFragment.Callback {

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_fragment);
        if (savedInstanceState == null) {
            initStartFragment();
        }
    }

    @AfterViews
    void init() {
        setSupportActionBar(toolbar);
    }

    private void initStartFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, ComparisonListFragment.newInstance())
                .commit();
    }

    @Override
    public void onCreateComparison(ComparisonListFragment f) {
        ComparisonBuildActivity_.intent(this).start();
    }

    @Override
    public void onClickComparison(ComparisonListFragment f, long comparisonInfoId) {
        ComparisonBuildActivity_.intent(this).comparisonInfoId(comparisonInfoId).start();
    }

    @Override
    public boolean updateTitleRequest(BaseFragment fragment) {
        toolbar.setTitle(fragment.getTitle());
        return true;
    }
}
