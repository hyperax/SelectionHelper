package tanya.arthur.selectionhelper.view.fragments;

import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import tanya.arthur.selectionhelper.R;
import tanya.arthur.selectionhelper.view.widgets.RecyclerViewEmptySupport;

@EFragment(R.layout.fragment_comparison_list)
public class ComparisonListFragment extends BaseFragment{

    public interface Callback {
        void onCreateComparison(ComparisonListFragment f);
    }

    @ViewById(R.id.comparison_recycler_view)
    RecyclerViewEmptySupport recyclerView;

    @ViewById(R.id.empty_view)
    TextView emptyTextView;

    public static ComparisonListFragment newInstance() {
        return ComparisonListFragment_.builder().build();
    }

    @AfterViews
    void init() {
        initEmptyView();
    }

    private void initEmptyView() {
        emptyTextView.setText(R.string.create_comparison);
        emptyTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add_circle_outline_24dp, 0, 0, 0);
        recyclerView.setEmptyView(emptyTextView);
    }

    @Click(R.id.empty_view)
    void onClickEmptyView() {
        Object hostParent = getHostParent();
        if (hostParent instanceof Callback) {
            ((Callback) hostParent).onCreateComparison(this);
        }
    }

    @Override
    protected String getInstanceTitle() {
        return getString(R.string.comparison_history);
    }
}
