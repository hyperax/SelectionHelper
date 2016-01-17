package tanya.arthur.selectionhelper.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import tanya.arthur.selectionhelper.R;
import tanya.arthur.selectionhelper.data.model.Variant;
import tanya.arthur.selectionhelper.helpers.NpeUtils;
import tanya.arthur.selectionhelper.helpers.Savable;
import tanya.arthur.selectionhelper.view.adapters.VariantsAdapter;
import tanya.arthur.selectionhelper.view.widgets.RecyclerViewEmptySupport;

@OptionsMenu(R.menu.variant_group)
@EFragment(R.layout.fragment_variant_group)
public class VariantGroupFragment extends BaseFragment
        implements Savable, VariantsAdapter.Callback {

    public interface Callback {
        void onDone(VariantGroupFragment f);
    }

    private static final String STATE_VARIANTS = "state_variants";

    @ViewById(R.id.recycler_view)
    RecyclerViewEmptySupport recyclerView;

    @ViewById(R.id.empty_view)
    TextView emptyTextView;

    @FragmentArg
    long variantGroupId;

    private ArrayList<Variant> variants;

    private VariantsAdapter adapter;

    public static VariantGroupFragment newInstance(long variantGroupId) {
        return VariantGroupFragment_.builder().variantGroupId(variantGroupId).build();
    }

    @AfterViews
    void init() {
        initVariants();
        initRecyclerView();
    }

    @SuppressWarnings("unchecked")
    private void initVariants() {
        Bundle state = restoreSavedState();
        if (state != null) {
            variants = (ArrayList <Variant>) state.getSerializable(STATE_VARIANTS);
        } else {
            variants = new ArrayList<>();
            variants.addAll(query.getVariants(variantGroupId));
        }
    }

    private void initRecyclerView() {
        recyclerView.setEmptyView(emptyTextView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new VariantsAdapter();
        adapter.setListener(this);
        adapter.setItems(variants);
        recyclerView.setAdapter(adapter);
    }

    @Click(R.id.fab)
    void onCreateVariantGroup() {
        Variant variant = logic.createVariant();
        variant.setGroupId(variantGroupId);
        variants.add(variant);
        adapter.addItem(variant);
        adapter.notifyItemInserted(adapter.getItemCount() - 1);
        recyclerView.scrollToPosition(adapter.getItemCount() - 1);
    }

    @OptionsItem(R.id.action_done)
    void onDone() {
        int size = variants.size();
        for (int i = 0; i < size; i++) {
            variants.get(i).setSortOrder(i);
        }
        logic.saveVariants(variantGroupId, variants);
        NpeUtils.call(getHostParent(), Callback.class, cb -> cb.onDone(this));
    }

    @Override
    protected String getInstanceTitle() {
        return getString(R.string.variant_group);
    }

    @Override
    public void onClick(Variant variant) {
        // TODO on click variant start to edit it
    }

    @Nullable
    @Override
    public Bundle getBundleSaveState() {
        Bundle state = new Bundle();
        state.putSerializable(STATE_VARIANTS, variants);
        return state;
    }
}
