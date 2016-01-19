package tanya.arthur.selectionhelper.data;

import android.content.Context;

import com.annimon.stream.Stream;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import tanya.arthur.selectionhelper.R;
import tanya.arthur.selectionhelper.data.events.DataUpdatedEvent;
import tanya.arthur.selectionhelper.data.model.ComparisonInfo;
import tanya.arthur.selectionhelper.data.model.Variant;
import tanya.arthur.selectionhelper.data.model.VariantGroup;
import tanya.arthur.selectionhelper.data.sqlite.DataQuery;
import tanya.arthur.selectionhelper.helpers.NpeUtils;

@EBean(scope = EBean.Scope.Singleton)
public class Logic {

    @RootContext
    Context context;

    @Bean
    DataQuery query;

    public ComparisonInfo createComparisonInfo() {
        return new ComparisonInfo()
                .setName(context.getString(R.string.new_comparison));
    }

    public boolean saveComparisonInfo(ComparisonInfo comparisonInfo) {
        return saveWithNotification(comparisonInfo);
    }

    public VariantGroup createVariantGroup() {
        return new VariantGroup()
                .setName(context.getString(R.string.new_variant_group));
    }

    public boolean saveVariantGroup(VariantGroup variantGroup) {
        return saveWithNotification(variantGroup);
    }

    public Variant createVariant() {
        return new Variant();
    }

    public boolean saveVariant(Variant variant) {
        return saveWithNotification(variant);
    }

    public boolean saveWithNotification(Object entity) {
        if (query.put(entity) > 0) {
            notifyDataUpdated(entity.getClass());
            return true;
        }

        return false;
    }

    private void notifyDataUpdated(Class... updatedEntities) {
        if (!NpeUtils.isEmpty(updatedEntities)) {
            EventBus.getDefault().post(new DataUpdatedEvent(updatedEntities));
        }
    }

    public void saveVariants(long variantGroupId, ArrayList<Variant> variants) {
        query.deleteVariants(variantGroupId);

        Stream.of(variants)
                .forEach(variant -> variant.setGroupId(variantGroupId));
        query.put(variants);
        notifyDataUpdated(Variant.class);
    }
}
