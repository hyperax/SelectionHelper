package tanya.arthur.selectionhelper.view.adapters;

import android.support.annotation.NonNull;
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
import tanya.arthur.selectionhelper.data.model.Criteria;
import tanya.arthur.selectionhelper.helpers.NpeUtils;
import tanya.arthur.selectionhelper.view.adapters.touch.ItemTouchHelperAdapter;

public class CriteriasAdapter extends RecyclerView.Adapter<CriteriasAdapter.ItemViewHolder>
        implements ItemTouchHelperAdapter {

    public interface Callback {
        void onClick(Criteria criteria);
        void onStartDrag(RecyclerView.ViewHolder viewHolder);
    }

    private int requestFocusPosition = -1;

    private List<Criteria> items = Collections.emptyList();

    private Callback listener;

    public void setListener(Callback listener) {
        this.listener = listener;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View root = inflater.inflate(R.layout.item_criteria, parent, false);
        ItemViewHolder holder = new ItemViewHolder(root);

        root.setOnClickListener(this::onClickItem);

        holder.handleView.setOnTouchListener((view, event) -> {
            if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                listener.onStartDrag(holder);
            }
            return false;
        });

        return holder;
    }

    private void onClickItem(View view) {
        Criteria criteria = (Criteria) view.getTag();
        NpeUtils.call(listener, Callback.class, cb -> cb.onClick(criteria));
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Criteria criteria = getItem(position);
        holder.setItem(criteria);
        holder.itemView.setTag(criteria);
        if (position == requestFocusPosition) {
            holder.nameTextView.requestFocus();
            requestFocusPosition = -1;
        }
    }

    private Criteria getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @NonNull
    public List<Criteria> getItems() {
        return items;
    }

    public void setItems(List<Criteria> items) {
        this.items = new ArrayList<>();
        if (!NpeUtils.isEmpty(items)) {
            this.items.addAll(items);
        }
    }

    public void addItem(Criteria criteria) {
        items.add(criteria);
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

        public ItemViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            nameTextView.setOnFocusChangeListener(
                    (view, hasFocus) -> {
                        Criteria criteria = (Criteria) itemView.getTag();
                        criteria.setName(nameTextView.getText().toString());
                    });
        }

        public void setItem(Criteria item) {
            nameTextView.setText(item.getName());
        }
    }
}
