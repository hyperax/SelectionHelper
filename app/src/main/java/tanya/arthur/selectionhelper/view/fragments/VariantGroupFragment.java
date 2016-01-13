package tanya.arthur.selectionhelper.view.fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.widget.TextView;

import com.trello.rxlifecycle.FragmentEvent;
import com.trello.rxlifecycle.RxLifecycle;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tanya.arthur.selectionhelper.R;
import tanya.arthur.selectionhelper.data.model.Variant;
import tanya.arthur.selectionhelper.data.model.VariantGroup;
import tanya.arthur.selectionhelper.view.adapters.VariantsAdapter;
import tanya.arthur.selectionhelper.view.notification.Letter;
import tanya.arthur.selectionhelper.view.widgets.RecyclerViewEmptySupport;

@EFragment(R.layout.fragment_variant_group)
public class VariantGroupFragment extends DataEventFragment
        implements VariantsAdapter.Callback {

    @ViewById(R.id.recycler_view)
    RecyclerViewEmptySupport recyclerView;

    @ViewById(R.id.empty_view)
    TextView emptyTextView;

    @FragmentArg
    long variantGroupId;

    private VariantsAdapter adapter;

    public static VariantGroupFragment newInstance(long variantGroupId) {
        return VariantGroupFragment_.builder().variantGroupId(variantGroupId).build();
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

        adapter = new VariantsAdapter();
        adapter.setListener(this);

        recyclerView.setAdapter(adapter);
    }

    @Override
    protected Class[] getTrackedEntities() {
        return new Class[] {VariantGroup.class};
    }

    @Override
    protected void onDataChanged() {
        query.getVariants(variantGroupId)
                .compose(RxLifecycle.bindUntilFragmentEvent(lifecycle(), FragmentEvent.STOP))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onDataLoaded,
                        error -> showToast(Letter.alert().setText(error.toString())));
    }

    private void onDataLoaded(List<Variant> variants) {
        adapter.setItems(variants);
        adapter.notifyDataSetChanged();
        updateDataTimestamp();
    }

    @Click(R.id.fab)
    void onCreateVariantGroup() {
        Variant variant = logic.createVariant();
        variant.setGroupId(variantGroupId);
        logic.saveVariant(variant);
    }

    @Override
    protected String getInstanceTitle() {
        return getString(R.string.variant_group);
    }

    @Override
    public void onClick(Variant variant) {
        // TODO on click variant start to edit it
    }
}
