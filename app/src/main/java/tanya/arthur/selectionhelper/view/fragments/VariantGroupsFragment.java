package tanya.arthur.selectionhelper.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import tanya.arthur.selectionhelper.data.model.VariantGroup;
import tanya.arthur.selectionhelper.helpers.NpeUtils;
import tanya.arthur.selectionhelper.helpers.Savable;
import tanya.arthur.selectionhelper.view.adapters.VariantGroupsAdapter;
import tanya.arthur.selectionhelper.view.notification.Letter;
import tanya.arthur.selectionhelper.view.widgets.RecyclerViewEmptySupport;

@EFragment(R.layout.fragment_variant_groups)
public class VariantGroupsFragment extends DataEventFragment
        implements Savable, VariantGroupsAdapter.Callback {

    private static final String STATE_SELECTED_VARIANT_GROUP = "state_selected_var_group";

    public interface Callback {
        void onCreateVariantGroups(VariantGroupsFragment f);
        void onClickVariantGroup(VariantGroupsFragment f, long variantGroupId);
    }

    @ViewById(R.id.recycler_view)
    RecyclerViewEmptySupport recyclerView;

    @ViewById(R.id.empty_view)
    TextView emptyTextView;

    private VariantGroupsAdapter adapter;

    public static VariantGroupsFragment newInstance() {
        return VariantGroupsFragment_.builder().build();
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

        adapter = new VariantGroupsAdapter();
        adapter.setListener(this);

        Bundle state = restoreSavedState();
        if (state != null && state.containsKey(STATE_SELECTED_VARIANT_GROUP)) {
            adapter.setSelectedVariantGroup(state.getLong(STATE_SELECTED_VARIANT_GROUP));
        }
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected Class[] getTrackedEntities() {
        return new Class[] {VariantGroup.class};
    }

    @Override
    protected void onDataChanged() {
        query.getVariantGroups()
                .compose(RxLifecycle.bindUntilFragmentEvent(lifecycle(), FragmentEvent.STOP))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onDataLoaded,
                        error -> showToast(Letter.alert().setText(error.toString())));
    }

    private void onDataLoaded(List<VariantGroup> variantGroups) {
        adapter.setItems(variantGroups);
        adapter.notifyDataSetChanged();
        updateDataTimestamp();
    }

    @Click(R.id.fab)
    void onCreateVariantGroup() {
        NpeUtils.call(getCallback(Callback.class)   ,
                cb -> cb.onCreateVariantGroups(VariantGroupsFragment.this));
    }

    @Override
    public void onClick(VariantGroup variantGroup) {
        NpeUtils.call(getCallback(Callback.class),
                cb -> cb.onClickVariantGroup(this, NpeUtils.getNonNull(variantGroup.getId())));
    }

    @Override
    protected String getInstanceTitle() {
        return getString(R.string.variant_groups);
    }

    @Nullable
    @Override
    public Bundle getBundleSaveState() {
        Bundle bundle = new Bundle();
        bundle.putLong(STATE_SELECTED_VARIANT_GROUP, adapter.getSelectedVariantGroup());
        return bundle;
    }
}
