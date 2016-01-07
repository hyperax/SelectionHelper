package tanya.arthur.selectionhelper.data.sqlite;


import android.support.v4.util.SimpleArrayMap;

import com.annimon.stream.Stream;

import org.androidannotations.annotations.EBean;

import java.util.List;

import rx.Observable;
import tanya.arthur.selectionhelper.data.model.ComparisonInfo;
import tanya.arthur.selectionhelper.helpers.DateUtils;
import tanya.arthur.selectionhelper.helpers.NpeUtils;

@EBean(scope = EBean.Scope.Singleton)
public class DbQuery {

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

    private void onTableUpdated(Class entityClass) {
        updatedEntities.put(entityClass, DateUtils.getCurrentMillis());
    }

    public boolean isDataChanged(Class[] entities, long date) {
        return Stream.of(entities)
                .anyMatch(clazz -> NpeUtils.getNonNull(updatedEntities.get(clazz), 0L) > date);
    }

    public void clearData(Class... clazz) {
        if (!NpeUtils.isEmpty(clazz)) {
            Storage storage = Storage.get();
            Stream.of(clazz)
                    .forEach(storage::clearTable);
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
        return Observable.create(subscriber -> {
            if (!subscriber.isUnsubscribed()) {
                subscriber.onNext(Storage.get().get(ComparisonInfo.class));
            }
        });
    }
}
