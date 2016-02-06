package tanya.arthur.selectionhelper.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.TextView;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tanya.arthur.selectionhelper.R;
import tanya.arthur.selectionhelper.helpers.MathUtils;
import tanya.arthur.selectionhelper.helpers.NpeUtils;
import tanya.arthur.selectionhelper.helpers.Savable;
import tanya.arthur.selectionhelper.view.adapters.choice.NamedItem;
import tanya.arthur.selectionhelper.view.adapters.choice.SingleChoiceAdapter;
import tanya.arthur.selectionhelper.view.notification.Letter;
import tanya.arthur.selectionhelper.view.widgets.RecyclerViewEmptySupport;

@EFragment(R.layout.fragment_combinations_selection)
public class CombinationsSelectionFragment extends BaseFragment
        implements Savable, SingleChoiceAdapter.Callback {

    private static final String STATE_SELECTED_COUNT = "state_selected_count";

    public interface Callback {
        void onCountChanged(CombinationsSelectionFragment f, int count);
    }

    @ViewById(R.id.recycler_view)
    RecyclerViewEmptySupport recyclerView;

    @ViewById(R.id.empty_view)
    TextView emptyTextView;

    @FragmentArg
    int count;

    @FragmentArg
    int variantsCount;

    @FragmentArg
    int min;

    @FragmentArg
    int max;

    private SingleChoiceAdapter adapter;

    public static Fragment newInstance(int variantsCount, int selectedCount, int min, int max) {
        return CombinationsSelectionFragment_.builder()
                .titleRes(R.string.comparison_count)
                .variantsCount(variantsCount)
                .count(selectedCount)
                .min(min)
                .max(max)
                .build();
    }

    @AfterViews
    void init() {
        restoreState();
        initRecyclerView();
        initData();
    }

    private void restoreState() {
        Bundle state = restoreSavedState();
        if (state != null) {
            count = state.getInt("count");
        }
    }

    private void initRecyclerView() {
        recyclerView.setEmptyView(emptyTextView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new SingleChoiceAdapter();
        adapter.setListener(this);

        Bundle state = restoreSavedState();
        if (state != null && state.containsKey(STATE_SELECTED_COUNT)) {
            adapter.setSelectedItem((long) count);
        } else {
            adapter.setSelectedItem(count);
        }
        recyclerView.setAdapter(adapter);
    }

    private void initData() {
        buildItems()
                .compose(bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(items -> {
                            adapter.setItems(items);
                            adapter.notifyDataSetChanged();
                        },
                        error->showToast(Letter.alert(getString(R.string.error, error.getMessage()))));
    }

    private Observable<List<NamedCount>> buildItems() {
        final String pattern = getString(R.string.comparison_count_pattern);
        return Observable.create(new Observable.OnSubscribe<List<NamedCount>>() {
            @Override
            public void call(Subscriber<? super List<NamedCount>> subscriber) {
                List<NamedCount> items = Stream.ofRange(min, max)
                        .map(i -> new NamedCount()
                                .setCount(i)
                                .setName(String.format(pattern,i, MathUtils.binom(variantsCount, i))))
                        .collect(Collectors.toList());
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(items);
                    subscriber.onCompleted();
                }
            }
        });
    }

    @Nullable
    @Override
    public Bundle getBundleSaveState() {
        Bundle bundle = new Bundle();
        bundle.putInt("count", count);
        return bundle;
    }

    @Override
    public void onClick(NamedItem item) {
        count = item.getId().intValue();
        NpeUtils.call(getCallback(Callback.class), cb->cb.onCountChanged(this, count));
    }

    private class NamedCount implements NamedItem {

        private int count;

        private String name;

        @Override
        public String getName() {
            return NpeUtils.getNonNull(name);
        }

        @Override
        public Long getId() {
            return (long) count;
        }

        public NamedCount setCount(int count) {
            this.count = count;
            return this;
        }

        public NamedCount setName(String name) {
            this.name = name;
            return this;
        }
    }
}
