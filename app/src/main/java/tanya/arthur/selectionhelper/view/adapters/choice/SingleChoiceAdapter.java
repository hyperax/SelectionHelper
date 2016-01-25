package tanya.arthur.selectionhelper.view.adapters.choice;

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
import tanya.arthur.selectionhelper.helpers.NpeUtils;

public class SingleChoiceAdapter extends RecyclerView.Adapter<SingleChoiceAdapter.ItemViewHolder> {

    public interface Callback {
        void onClick(NamedItem item);
    }

    private List<NamedItem> items = Collections.emptyList();

    private long selectedItemId;

    private Callback listener;

    public void setListener(Callback listener) {
        this.listener = listener;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View root = inflater.inflate(R.layout.item_single_choice, parent, false);
        return new ItemViewHolder(root, this::onClickItem);
    }

    private void onClickItem(View view) {
        NamedItem item = (NamedItem) view.getTag();
        setSelectedItem(NpeUtils.getNonNull(item.getId()));
        NpeUtils.call(listener, Callback.class, cb->cb.onClick(item));
    }

    public void setSelectedItem(long id) {
        if (selectedItemId != id) {
            long prevSelectedItemId = selectedItemId;
            selectedItemId = id;
            notifyItemChanged(getItemPosition(prevSelectedItemId));
            notifyItemChanged(getItemPosition(selectedItemId));
        }
    }

    public long getSelectedItem() {
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
        NamedItem item = getItem(position);
        holder.setItem(item, NpeUtils.equals(item.getId(), selectedItemId));
    }

    private NamedItem getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<? extends NamedItem> items) {
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

        public void setItem(NamedItem item, boolean isSelected) {
            this.itemView.setTag(item);
            nameTextView.setText(item.getName());
            nameTextView.setChecked(isSelected);
        }
    }
}
