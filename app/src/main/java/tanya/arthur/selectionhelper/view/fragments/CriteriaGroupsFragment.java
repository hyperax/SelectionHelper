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
import tanya.arthur.selectionhelper.data.model.CriteriaGroup;
import tanya.arthur.selectionhelper.helpers.NpeUtils;
import tanya.arthur.selectionhelper.helpers.Savable;
import tanya.arthur.selectionhelper.view.activities.CriteriaGroupActivity;
import tanya.arthur.selectionhelper.view.activities.CriteriaGroupActivity_;
import tanya.arthur.selectionhelper.view.adapters.choice.NamedItem;
import tanya.arthur.selectionhelper.view.adapters.choice.SingleChoiceAdapter;
import tanya.arthur.selectionhelper.view.notification.Letter;
import tanya.arthur.selectionhelper.view.widgets.RecyclerViewEmptySupport;

@EFragment(R.layout.fragment_criteria_list)
public class CriteriaGroupsFragment extends DataEventFragment
        implements Savable, SingleChoiceAdapter.Callback {

    private static final int REQUEST_CREATE_CRITERIA_GROUP = 1;

    private static final String STATE_SELECTED_CRITERIA_GROUP = "state_selected_criteria_group";

    public interface Callback {
        void onClickCriteriaGroup(CriteriaGroupsFragment f, long criteriaGroupId);
    }

    @ViewById(R.id.recycler_view)
    RecyclerViewEmptySupport recyclerView;

    @ViewById(R.id.empty_view)
    TextView emptyTextView;

    @FragmentArg
    long argCriteriaGroup;

    private SingleChoiceAdapter adapter;

    public static CriteriaGroupsFragment newInstance(long selectedCriteriaGroupId) {
        return CriteriaGroupsFragment_.builder()
                .argCriteriaGroup(selectedCriteriaGroupId)
                .titleRes(R.string.criteria_groups)
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
        if (state != null && state.containsKey(STATE_SELECTED_CRITERIA_GROUP)) {
            adapter.setSelectedItem(state.getLong(STATE_SELECTED_CRITERIA_GROUP));
        } else {
            adapter.setSelectedItem(argCriteriaGroup);
        }
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected Class[] getTrackedEntities() {
        return new Class[] {CriteriaGroup.class};
    }

    @Override
    protected void onDataChanged() {
        query.getCriteriaGroups()
                .compose(RxLifecycle.bindUntilFragmentEvent(lifecycle(), FragmentEvent.STOP))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onDataLoaded,
                        error -> showToast(Letter.alert(error.toString())));
    }

    private void onDataLoaded(List<? extends NamedItem> variantGroups) {
        adapter.setItems(variantGroups);
        adapter.notifyDataSetChanged();
        updateDataTimestamp();
    }

    @Click(R.id.fab)
    void onCreateVariantGroup() {
        CriteriaGroupActivity_.intent(this).startForResult(REQUEST_CREATE_CRITERIA_GROUP);
    }

    @Override
    public void onClick(NamedItem item) {
        NpeUtils.call(getCallback(Callback.class),
                cb -> cb.onClickCriteriaGroup(this, NpeUtils.getNonNull(item.getId())));
    }

    @Nullable
    @Override
    public Bundle getBundleSaveState() {
        Bundle bundle = new Bundle();
        bundle.putLong(STATE_SELECTED_CRITERIA_GROUP, adapter.getSelectedItem());
        return bundle;
    }

    @OnActivityResult(REQUEST_CREATE_CRITERIA_GROUP)
    protected void onActivityResult(int resultCode, Intent data){
        if (resultCode == Activity.RESULT_OK){
            long createdCriteriaGroupId =
                    data.getExtras().getLong(CriteriaGroupActivity.EXTRA_CRITERIA_GROUP_ID);
            adapter.setSelectedItem(createdCriteriaGroupId);
            NpeUtils.call(getCallback(Callback.class),
                    cb -> cb.onClickCriteriaGroup(this, createdCriteriaGroupId));
        }
    }
}
