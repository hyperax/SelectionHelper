package tanya.arthur.selectionhelper.view.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import org.androidannotations.annotations.EActivity;

import tanya.arthur.selectionhelper.R;
import tanya.arthur.selectionhelper.view.fragments.BaseFragment;
import tanya.arthur.selectionhelper.view.fragments.ComparisonListFragment;
import tanya.arthur.selectionhelper.view.notification.Letter;

@EActivity
public class MainActivity extends BaseActivity implements ComparisonListFragment.Callback {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            initStartFragment();
        }
    }

    private void initStartFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, ComparisonListFragment.newInstance())
                .commit();
    }

    @Override
    public void onCreateComparison(ComparisonListFragment f) {
        showToast(Letter.get().setText("TODO Create new comparison!"));
    }

    @Override
    public boolean updateTitleRequest(BaseFragment fragment) {
        toolbar.setTitle(fragment.getTitle());
        return true;
    }
}
