package tanya.arthur.selectionhelper.data.sqlite;


import android.support.v4.util.SimpleArrayMap;

import com.annimon.stream.Stream;

import org.androidannotations.annotations.EBean;

import java.util.List;

import rx.Observable;
import tanya.arthur.selectionhelper.data.model.ComparisonInfo;
import tanya.arthur.selectionhelper.data.model.Criteria;
import tanya.arthur.selectionhelper.data.model.CriteriaGroup;
import tanya.arthur.selectionhelper.data.model.Variant;
import tanya.arthur.selectionhelper.data.model.VariantGroup;
import tanya.arthur.selectionhelper.data.model.contract.Contract;
import tanya.arthur.selectionhelper.helpers.DateUtils;
import tanya.arthur.selectionhelper.helpers.NpeUtils;

import static tanya.arthur.selectionhelper.helpers.SqlUtils.param;

@EBean(scope = EBean.Scope.Singleton)
public class DataQuery {

    private final SimpleArrayMap<Class, Long> updatedEntities = new SimpleArrayMap<>();

    public <T> void put(List<T> data) {
        if (!NpeUtils.isEmpty(data)) {
            Storage.get().put(data);
            onTableUpdated(data.get(0).getClass());
        }
    }

    public <T> long put(T data) {
        if (data != null) {
            onTableUpdated(data.getClass());
            return Storage.get().put(data);
        }
        return 0L;
    }

    private void onTableUpdated(Class... entityClass) {
        long timestamp = DateUtils.getCurrentMillis();
        for (Class clazz : entityClass) {
            updatedEntities.put(clazz, timestamp);
        }
    }

    public boolean isDataChanged(Class[] entities, long date) {
        return Stream.of(entities)
                .anyMatch(clazz -> NpeUtils.getNonNull(updatedEntities.get(clazz), 0L) > date);
    }

    public void clearData(Class... clazz) {
        if (!NpeUtils.isEmpty(clazz)) {
            Storage storage = Storage.get();
            Stream.of(clazz)
                    .filter(c -> storage.clearTable(c) > 0)
                    .forEach(this::onTableUpdated);
        }
    }

    public void beginTransaction() {
        Storage.get().beginTransaction();
    }

    public void endTransaction() {
        Storage.get().endTransaction();
    }

    public void setTransactionSuccessful() {
        Storage.get().setTransactionSuccessful();
    }

    public Observable<List<ComparisonInfo>> getComparisonInfos() {
        return Storage.get().getObservable(ComparisonInfo.class);
    }

    public Observable<List<VariantGroup>> getVariantGroups() {
        return Storage.get().getObservable(VariantGroup.class);
    }

    public ComparisonInfo getComparisonInfo(long comparisonInfoId) {
        return Storage.get().get(ComparisonInfo.class, comparisonInfoId);
    }

    public List<Variant> getVariants(long variantGroupId) {
        return Storage.get().getQuery(Variant.class)
                .withSelection(param(Contract.Variant.GROUP_ID, variantGroupId))
                .orderBy(Contract.ID)
                .list();
    }

    public VariantGroup getVariantGroup(long variantGroupId) {
        return Storage.get().get(VariantGroup.class, variantGroupId);
    }

    public boolean deleteVariants(long variantGroupId) {
        return Storage.get()
                .delete(Variant.class, param(Contract.Variant.GROUP_ID, variantGroupId)) > 0;
    }

    public Observable<List<CriteriaGroup>> getCriteriaGroups() {
        return Storage.get().getObservable(CriteriaGroup.class);
    }

    public List<Criteria> getCriterias(long criteriaGroupId) {
        return Storage.get().getQuery(Criteria.class)
                .withSelection(param(Contract.Criteria.GROUP_ID, criteriaGroupId))
                .orderBy(Contract.ID)
                .list();
    }

    public CriteriaGroup getCriteriaGroup(long criteriaGroupId) {
        return Storage.get().get(CriteriaGroup.class, criteriaGroupId);
    }

    public boolean deleteCriterias(long criteriaGroupId) {
        return Storage.get()
                .delete(Criteria.class, param(Contract.Criteria.GROUP_ID, criteriaGroupId)) > 0;
    }
}
