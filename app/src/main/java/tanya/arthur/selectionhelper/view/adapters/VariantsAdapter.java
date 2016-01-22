package tanya.arthur.selectionhelper.view.adapters;

import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import tanya.arthur.selectionhelper.R;
import tanya.arthur.selectionhelper.data.model.Variant;
import tanya.arthur.selectionhelper.helpers.NpeUtils;
import tanya.arthur.selectionhelper.view.adapters.helper.ItemTouchHelperAdapter;

public class VariantsAdapter extends RecyclerView.Adapter<VariantsAdapter.ItemViewHolder>
        implements ItemTouchHelperAdapter {

    public interface Callback {
        void onClick(Variant variant);
        void onStartDrag(RecyclerView.ViewHolder viewHolder);
    }

    private int requestFocusPosition = -1;

    private List<Variant> items = Collections.emptyList();

    private Callback listener;

    public void setListener(Callback listener) {
        this.listener = listener;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View root = inflater.inflate(R.layout.item_variant, parent, false);
        ItemViewHolder holder = new ItemViewHolder(root, this::onClickVariant);

        root.setOnClickListener(this::onClickVariant);

        holder.handleView.setOnTouchListener((view, event) -> {
            if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                listener.onStartDrag(holder);
            }
            return false;
        });

        return holder;
    }

    private void onClickVariant(View view) {
        Variant variant = (Variant) view.getTag();
        NpeUtils.call(listener, Callback.class, cb -> cb.onClick(variant));
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Variant variant = getItem(position);
        holder.setItem(variant);
        holder.itemView.setTag(variant);
        if (position == requestFocusPosition) {
            holder.nameTextView.requestFocus();
            requestFocusPosition = -1;
        }
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
        requestFocusPosition = getItemCount() - 1;
    }

    @Override
    public void onItemDismiss(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(items, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.name)
        EditText nameTextView;

        @Bind(R.id.handle)
        ImageView handleView;

        public ItemViewHolder(View v, View.OnClickListener cl) {
            super(v);
            ButterKnife.bind(this, v);
            nameTextView.setOnFocusChangeListener(
                    (view, hasFocus) -> {
                        Variant variant = (Variant) itemView.getTag();
                        variant.setName(nameTextView.getText().toString());
                    });
        }

        public void setItem(Variant item) {
            nameTextView.setText(item.getName());
        }
    }
}
