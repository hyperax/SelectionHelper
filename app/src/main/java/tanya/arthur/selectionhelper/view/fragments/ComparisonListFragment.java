package tanya.arthur.selectionhelper.view.fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

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
import tanya.arthur.selectionhelper.helpers.NpeUtils;
import tanya.arthur.selectionhelper.view.adapters.ComparisonsAdapter;
import tanya.arthur.selectionhelper.view.notification.Letter;
import tanya.arthur.selectionhelper.view.widgets.RecyclerViewEmptySupport;

@EFragment(R.layout.fragment_comparison_list)
public class ComparisonListFragment extends DataEventFragment implements ComparisonsAdapter.Callback{

    public interface Callback {
        void onCreateComparison(ComparisonListFragment f);
        void onClickComparison(ComparisonListFragment f, long comparisonInfoId);
    }

    @ViewById(R.id.recycler_view)
    RecyclerViewEmptySupport recyclerView;

    @ViewById(R.id.empty_view)
    View emptyView;

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
        recyclerView.setEmptyView(emptyView);
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
        query.getComparisonInfos()
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

    @Click(R.id.fab)
    void onClickEmptyView() {
        NpeUtils.call(getHostParent(), Callback.class, cb -> cb.onCreateComparison(this));
    }

    @Override
    public void onClick(ComparisonInfo comparison) {
        NpeUtils.call(getHostParent(), Callback.class,
                cb -> cb.onClickComparison(this, NpeUtils.getNonNull(comparison.getId())));
    }

    @Override
    protected String getInstanceTitle() {
        return getString(R.string.comparison_history);
    }
}
