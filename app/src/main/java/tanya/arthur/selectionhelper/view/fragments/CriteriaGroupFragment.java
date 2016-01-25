package tanya.arthur.selectionhelper.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.EditText;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import tanya.arthur.selectionhelper.R;
import tanya.arthur.selectionhelper.data.model.Criteria;
import tanya.arthur.selectionhelper.data.model.CriteriaGroup;
import tanya.arthur.selectionhelper.helpers.NpeUtils;
import tanya.arthur.selectionhelper.helpers.Savable;
import tanya.arthur.selectionhelper.view.adapters.CriteriasAdapter;
import tanya.arthur.selectionhelper.view.adapters.touch.SimpleItemTouchHelperCallback;
import tanya.arthur.selectionhelper.view.widgets.RecyclerViewEmptySupport;

@OptionsMenu(R.menu.group)
@EFragment(R.layout.fragment_group)
public class CriteriaGroupFragment extends BaseFragment
        implements Savable, CriteriasAdapter.Callback {

    public interface Callback {
        void onDone(CriteriaGroupFragment f, long criteriaGroupId);
    }

    private static final String STATE_CRITERIAS = "state_criterias";
    private static final String STATE_CRITERIA_GROUP_ID = "state_criteria_group_id";
    private static final String STATE_CRITERIA_GROUP_NAME = "state_criteria_group_name";

    @ViewById(R.id.recycler_view)
    RecyclerViewEmptySupport recyclerView;

    @ViewById(R.id.empty_view)
    TextView emptyTextView;

    @ViewById(R.id.name)
    EditText nameEditText;

    @FragmentArg
    long criteriaGroupId;

    private ItemTouchHelper itemTouchHelper;

    private CriteriasAdapter adapter;

    public static CriteriaGroupFragment newInstance(long criteriaGroupId) {
        return CriteriaGroupFragment_.builder()
                .titleRes(R.string.criteria_group)
                .criteriaGroupId(criteriaGroupId)
                .build();
    }

    @AfterViews
    void init() {
        initRecyclerView();
        initViews();
    }

    private void initViews() {
        Bundle state = restoreSavedState();
        if (state != null) {
            nameEditText.setText(state.getString(STATE_CRITERIA_GROUP_NAME));
        }
    }

    @SuppressWarnings("unchecked")
    private void setAdapterItems() {
        Bundle state = restoreSavedState();
        if (state != null) {
            criteriaGroupId = state.getLong(STATE_CRITERIA_GROUP_ID);
            adapter.setItems((ArrayList <Criteria>) state.getSerializable(STATE_CRITERIAS));
        } else {
            adapter.setItems(query.getCriterias(criteriaGroupId));
        }
    }

    private void initRecyclerView() {
        recyclerView.setEmptyView(emptyTextView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new CriteriasAdapter();
        adapter.setListener(this);
        setAdapterItems();
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Click(R.id.fab)
    void onCreateCriteriaGroup() {
        Criteria criteria = logic.createCriteria();
        criteria.setGroupId(criteriaGroupId);
        adapter.addItem(criteria);
        recyclerView.smoothScrollToPosition(adapter.getItemCount());
        adapter.notifyItemInserted(adapter.getItemCount() - 1);
    }

    @OptionsItem(R.id.action_done)
    void onDone() {

        CriteriaGroup criteriaGroup = query.getCriteriaGroup(criteriaGroupId);
        if (criteriaGroup == null) {
            criteriaGroup = logic.createCriteriaGroup();
        }
        criteriaGroup.setName(nameEditText.getText().toString());

        logic.saveCriteriaGroup(criteriaGroup);
        criteriaGroupId = NpeUtils.getNonNull(criteriaGroup.getId());

        List<Criteria> criterias = adapter.getItems();
        int size = criterias.size();
        for (int i = 0; i < size; i++) {
            criterias.get(i).setSortOrder(i);
        }

        logic.saveCriterias(criteriaGroupId, criterias);
        NpeUtils.call(getHostParent(), Callback.class, cb -> cb.onDone(this, criteriaGroupId));
    }

    @Override
    public void onClick(Criteria criteria) {
        // TODO on click criteria start to edit it
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }

    @Nullable
    @Override
    public Bundle getBundleSaveState() {
        Bundle state = new Bundle();
        state.putSerializable(STATE_CRITERIAS, new ArrayList<>(adapter.getItems()));
        state.putSerializable(STATE_CRITERIA_GROUP_NAME, nameEditText.getText().toString());
        state.putLong(STATE_CRITERIA_GROUP_ID, criteriaGroupId);
        return state;
    }
}
