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
import tanya.arthur.selectionhelper.data.model.Variant;
import tanya.arthur.selectionhelper.data.model.VariantGroup;
import tanya.arthur.selectionhelper.helpers.NpeUtils;
import tanya.arthur.selectionhelper.helpers.Savable;
import tanya.arthur.selectionhelper.view.adapters.VariantsAdapter;
import tanya.arthur.selectionhelper.view.adapters.touch.SimpleItemTouchHelperCallback;
import tanya.arthur.selectionhelper.view.widgets.RecyclerViewEmptySupport;

@OptionsMenu(R.menu.confirm_options)
@EFragment(R.layout.fragment_group)
public class VariantGroupFragment extends BaseFragment
        implements Savable, VariantsAdapter.Callback {

    public interface Callback {
        void onDone(VariantGroupFragment f, long variantGroupId);
    }

    private static final String STATE_VARIANTS = "state_variants";
    private static final String STATE_VARIANT_GROUP_ID = "state_variant_group_id";
    private static final String STATE_VARIANT_GROUP_NAME = "state_variant_group_name";

    @ViewById(R.id.recycler_view)
    RecyclerViewEmptySupport recyclerView;

    @ViewById(R.id.empty_view)
    TextView emptyTextView;

    @ViewById(R.id.name)
    EditText nameEditText;

    @FragmentArg
    long variantGroupId;

    private ItemTouchHelper itemTouchHelper;

    private VariantsAdapter adapter;

    public static VariantGroupFragment newInstance(long variantGroupId) {
        return VariantGroupFragment_.builder()
                .titleRes(R.string.variant_group)
                .variantGroupId(variantGroupId)
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
            nameEditText.setText(state.getString(STATE_VARIANT_GROUP_NAME));
        }
    }

    @SuppressWarnings("unchecked")
    private void setAdapterItems() {
        Bundle state = restoreSavedState();
        if (state != null) {
            variantGroupId = state.getLong(STATE_VARIANT_GROUP_ID);
            adapter.setItems((ArrayList <Variant>) state.getSerializable(STATE_VARIANTS));
        } else {
            adapter.setItems(query.getVariants(variantGroupId));
        }
    }

    private void initRecyclerView() {
        recyclerView.setEmptyView(emptyTextView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new VariantsAdapter();
        adapter.setListener(this);
        setAdapterItems();
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Click(R.id.fab)
    void onCreateVariantGroup() {
        Variant variant = logic.createVariant();
        variant.setGroupId(variantGroupId);
        adapter.addItem(variant);
        recyclerView.smoothScrollToPosition(adapter.getItemCount());
        adapter.notifyItemInserted(adapter.getItemCount() - 1);
    }

    @OptionsItem(R.id.action_done)
    void onDone() {

        VariantGroup variantGroup = query.getVariantGroup(variantGroupId);
        if (variantGroup == null) {
            variantGroup = logic.createVariantGroup();
        }
        variantGroup.setName(nameEditText.getText().toString());

        logic.saveVariantGroup(variantGroup);
        variantGroupId = NpeUtils.getNonNull(variantGroup.getId());

        List<Variant> variants = adapter.getItems();
        int size = variants.size();
        for (int i = 0; i < size; i++) {
            variants.get(i).setSortOrder(i);
        }

        logic.saveVariants(variantGroupId, variants);
        NpeUtils.call(getHostParent(), Callback.class, cb -> cb.onDone(this, variantGroupId));
    }

    @Override
    public void onClick(Variant variant) {
        // TODO on click variant start to edit it
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }

    @Nullable
    @Override
    public Bundle getBundleSaveState() {
        Bundle state = new Bundle();
        state.putSerializable(STATE_VARIANTS, new ArrayList<>(adapter.getItems()));
        state.putSerializable(STATE_VARIANT_GROUP_NAME, nameEditText.getText().toString());
        state.putLong(STATE_VARIANT_GROUP_ID, variantGroupId);
        return state;
    }
}
