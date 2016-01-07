package tanya.arthur.selectionhelper.view.fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.widget.TextView;

import com.trello.rxlifecycle.FragmentEvent;
import com.trello.rxlifecycle.RxLifecycle;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tanya.arthur.selectionhelper.R;
import tanya.arthur.selectionhelper.data.model.ComparisonInfo;
import tanya.arthur.selectionhelper.view.adapters.ComparisonsAdapter;
import tanya.arthur.selectionhelper.view.notification.Letter;
import tanya.arthur.selectionhelper.view.widgets.RecyclerViewEmptySupport;

@EFragment(R.layout.fragment_comparison_list)
public class ComparisonListFragment extends DataEventFragment implements ComparisonsAdapter.Callback{

    public interface Callback {
        void onCreateComparison(ComparisonListFragment f);
    }

    @ViewById(R.id.comparison_recycler_view)
    RecyclerViewEmptySupport recyclerView;

    @ViewById(R.id.empty_view)
    TextView emptyTextView;

    private ComparisonsAdapter adapter;

    public static ComparisonListFragment newInstance() {
        return ComparisonListFragment_.builder().build();
    }

    @AfterViews
    void init() {
        initRecyclerView();
        onDataChanged();
        updateDataTimestamp();
    }

    private void initRecyclerView() {
        recyclerView.setEmptyView(emptyTextView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new ComparisonsAdapter();
        adapter.setListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected Class[] getTrackedEntities() {
        return new Class[] {ComparisonInfo.class};
    }

    @Override
    protected void onDataChanged() {
        dbQuery.getComparisonInfos()
                .compose(RxLifecycle.bindUntilFragmentEvent(lifecycle(), FragmentEvent.STOP))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onDataLoaded,
                        error -> showToast(Letter.alert().setText(error.toString())));
    }

    private void onDataLoaded(List<ComparisonInfo> comparisonInfos) {
        adapter.setItems(comparisonInfos);
        adapter.notifyDataSetChanged();
        updateDataTimestamp();
    }

    @Click(R.id.empty_view)
    void onClickEmptyView() {
        Object hostParent = getHostParent();
        if (hostParent instanceof Callback) {
            ((Callback) hostParent).onCreateComparison(this);
        }
    }

    @Override
    public void onClick(ComparisonInfo comparison) {
        showToast(Letter.get().setText(comparison.getName()));
    }

    @Override
    protected String getInstanceTitle() {
        return getString(R.string.comparison_history);
    }
}
