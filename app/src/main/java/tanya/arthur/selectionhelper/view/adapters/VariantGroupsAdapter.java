package tanya.arthur.selectionhelper.view.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import tanya.arthur.selectionhelper.R;
import tanya.arthur.selectionhelper.data.model.VariantGroup;
import tanya.arthur.selectionhelper.helpers.NpeUtils;

public class VariantGroupsAdapter extends RecyclerView.Adapter<VariantGroupsAdapter.ItemViewHolder> {

    public interface Callback {
        void onClick(VariantGroup variantGroup);
    }

    private List<VariantGroup> items = Collections.emptyList();

    private long selectedItemId;

    private Callback listener;

    public void setListener(Callback listener) {
        this.listener = listener;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View root = inflater.inflate(R.layout.item_variant_group, parent, false);
        return new ItemViewHolder(root, this::onClickVariantGroup);
    }

    private void onClickVariantGroup(View view) {
        VariantGroup variantGroup = (VariantGroup) view.getTag();
        setSelectedVariantGroup(NpeUtils.getNonNull(variantGroup.getId()));
        NpeUtils.call(listener, Callback.class, cb->cb.onClick(variantGroup));
    }

    public void setSelectedVariantGroup(long id) {
        if (selectedItemId != id) {
            long prevSelectedItemId = selectedItemId;
            selectedItemId = id;
            notifyItemChanged(getItemPosition(prevSelectedItemId));
            notifyItemChanged(getItemPosition(selectedItemId));
        }
    }

    public long getSelectedVariantGroup() {
        return selectedItemId;
    }

    private int getItemPosition(long itemId) {
        int itemPosition = -1;
        int size = items.size();
        for (int i = 0; i < size; i++) {
            if (NpeUtils.getNonNull(items.get(i).getId()) == itemId) {
                itemPosition = i;
                break;
            }
        }
        return itemPosition;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        VariantGroup variantGroup = getItem(position);
        holder.setItem(variantGroup, NpeUtils.equals(variantGroup.getId(), selectedItemId));
    }

    private VariantGroup getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<VariantGroup> items) {
        this.items = new ArrayList<>();
        if (!NpeUtils.isEmpty(items)) {
            this.items.addAll(items);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.name)
        RadioButton nameTextView;

        public ItemViewHolder(View v, View.OnClickListener clickListener) {
            super(v);
            ButterKnife.bind(this, v);
            v.setOnClickListener(clickListener);
        }

        public void setItem(VariantGroup item, boolean isSelected) {
            this.itemView.setTag(item);
            nameTextView.setText(item.getName());
            nameTextView.setChecked(isSelected);
        }
    }
}
