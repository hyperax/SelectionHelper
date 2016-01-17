package tanya.arthur.selectionhelper.view.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import tanya.arthur.selectionhelper.R;
import tanya.arthur.selectionhelper.data.model.Variant;
import tanya.arthur.selectionhelper.helpers.NpeUtils;
import tanya.arthur.selectionhelper.view.helpers.AfterTextChangedWatcher;

public class VariantsAdapter extends RecyclerView.Adapter<VariantsAdapter.ItemViewHolder> {

    public interface Callback {
        void onClick(Variant variant);
    }

    private List<Variant> items = Collections.emptyList();

    private Callback listener;

    public void setListener(Callback listener) {
        this.listener = listener;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View root = inflater.inflate(R.layout.item_variant, parent, false);
        return new ItemViewHolder(root, this::onClickVariant);
    }

    private void onClickVariant(View view) {
        Variant variant = (Variant) view.getTag();
        NpeUtils.call(listener, Callback.class, cb -> cb.onClick(variant));
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Variant variant = getItem(position);
        holder.setItem(variant);
    }

    private Variant getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<Variant> items) {
        this.items = new ArrayList<>();
        if (!NpeUtils.isEmpty(items)) {
            this.items.addAll(items);
        }
    }

    public void addItem(Variant variant) {
        items.add(variant);
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.name)
        EditText nameTextView;

        public ItemViewHolder(View v, View.OnClickListener cl) {
            super(v);
            ButterKnife.bind(this, v);
            v.setOnClickListener(cl);
            nameTextView.addTextChangedListener(new AfterTextChangedWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    Variant variant = (Variant) itemView.getTag();
                    if (variant != null) {
                        variant.setName(s.toString());
                    }
                }
            });
        }

        public void setItem(Variant item) {
            this.itemView.setTag(item);
            nameTextView.setText(item.getName());
        }
    }
}
