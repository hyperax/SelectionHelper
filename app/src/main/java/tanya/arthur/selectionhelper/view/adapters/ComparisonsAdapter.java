package tanya.arthur.selectionhelper.view.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.annimon.stream.Optional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import tanya.arthur.selectionhelper.R;
import tanya.arthur.selectionhelper.data.model.ComparisonInfo;
import tanya.arthur.selectionhelper.helpers.NpeUtils;

public class ComparisonsAdapter extends RecyclerView.Adapter<ComparisonsAdapter.ItemViewHolder> {

    public interface Callback {
        void onClick(ComparisonInfo comparison);
    }

    private List<ComparisonInfo> items = Collections.emptyList();

    private Optional<Callback> listener = Optional.empty();

    public void setListener(Callback listener) {
        this.listener = Optional.ofNullable(listener);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View root = inflater.inflate(R.layout.item_comparison_info, parent, false);
        return new ItemViewHolder(root, this::onClickComparison);
    }

    private void onClickComparison(View view) {
        listener.ifPresent(l->l.onClick((ComparisonInfo) view.getTag()));
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.setItem(getItem(position));
    }

    private ComparisonInfo getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<ComparisonInfo> items) {
        this.items = new ArrayList<>();
        if (!NpeUtils.isEmpty(items)) {
            this.items.addAll(items);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.name)
        TextView nameTextView;

        public ItemViewHolder(View v, View.OnClickListener clickListener) {
            super(v);
            ButterKnife.bind(this, v);
            v.setOnClickListener(clickListener);
        }

        public void setItem(ComparisonInfo item) {
            this.itemView.setTag(item);
            nameTextView.setText(item.getName());
        }
    }
}
