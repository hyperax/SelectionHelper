package tanya.arthur.selectionhelper.view.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.TextView;

import com.trello.rxlifecycle.FragmentEvent;
import com.trello.rxlifecycle.RxLifecycle;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tanya.arthur.selectionhelper.R;
import tanya.arthur.selectionhelper.data.model.VariantGroup;
import tanya.arthur.selectionhelper.helpers.NpeUtils;
import tanya.arthur.selectionhelper.helpers.Savable;
import tanya.arthur.selectionhelper.view.activities.VariantGroupActivity;
import tanya.arthur.selectionhelper.view.activities.VariantGroupActivity_;
import tanya.arthur.selectionhelper.view.adapters.choice.NamedItem;
import tanya.arthur.selectionhelper.view.adapters.choice.SingleChoiceAdapter;
import tanya.arthur.selectionhelper.view.notification.Letter;
import tanya.arthur.selectionhelper.view.widgets.RecyclerViewEmptySupport;

@EFragment(R.layout.fragment_variant_groups)
public class VariantGroupsFragment extends DataEventFragment
        implements Savable, SingleChoiceAdapter.Callback {

    private static final int REQUEST_CREATE_VARIANT_GROUP = 1;

    private static final String STATE_SELECTED_VARIANT_GROUP = "state_selected_var_group";

    public interface Callback {
        void onClickVariantGroup(VariantGroupsFragment f, long variantGroupId);
    }

    @ViewById(R.id.recycler_view)
    RecyclerViewEmptySupport recyclerView;

    @ViewById(R.id.empty_view)
    TextView emptyTextView;

    @FragmentArg
    long argVariantGroup;

    private SingleChoiceAdapter adapter;

    public static VariantGroupsFragment newInstance(long selectedVariantGroupId) {
        return VariantGroupsFragment_.builder()
                .argVariantGroup(selectedVariantGroupId)
                .titleRes(R.string.variant_groups)
                .build();
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

        adapter = new SingleChoiceAdapter();
        adapter.setListener(this);

        Bundle state = restoreSavedState();
        if (state != null && state.containsKey(STATE_SELECTED_VARIANT_GROUP)) {
            adapter.setSelectedItem(state.getLong(STATE_SELECTED_VARIANT_GROUP));
        } else {
            adapter.setSelectedItem(argVariantGroup);
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

    private void onDataLoaded(List<? extends NamedItem> variantGroups) {
        adapter.setItems(variantGroups);
        adapter.notifyDataSetChanged();
        updateDataTimestamp();
    }

    @Click(R.id.fab)
    void onCreateVariantGroup() {
        VariantGroupActivity_.intent(this).startForResult(REQUEST_CREATE_VARIANT_GROUP);
    }

    @Override
    public void onClick(NamedItem item) {
        NpeUtils.call(getCallback(Callback.class),
                cb -> cb.onClickVariantGroup(this, NpeUtils.getNonNull(item.getId())));
    }

    @Nullable
    @Override
    public Bundle getBundleSaveState() {
        Bundle bundle = new Bundle();
        bundle.putLong(STATE_SELECTED_VARIANT_GROUP, adapter.getSelectedItem());
        return bundle;
    }

    @OnActivityResult(REQUEST_CREATE_VARIANT_GROUP)
    protected void onActivityResult(int resultCode, Intent data){
        if (resultCode == Activity.RESULT_OK){
            long createdVariantGroupId =
                    data.getExtras().getLong(VariantGroupActivity.EXTRA_VARIANT_GROUP_ID);
            adapter.setSelectedItem(createdVariantGroupId);
            NpeUtils.call(getCallback(Callback.class),
                    cb -> cb.onClickVariantGroup(this, createdVariantGroupId));
        }
    }
}
