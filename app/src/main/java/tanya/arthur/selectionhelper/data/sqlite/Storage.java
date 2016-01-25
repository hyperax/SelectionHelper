package tanya.arthur.selectionhelper.data.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Collection;
import java.util.List;

import nl.nl2312.rxcupboard.RxCupboard;
import nl.nl2312.rxcupboard.RxDatabase;
import nl.qbusict.cupboard.DatabaseCompartment;
import rx.Observable;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class Storage {

    private SQLiteDatabase database;
    private RxDatabase rxDatabase;
    private DatabaseCompartment dataCompartment;

    private static Storage instance;
    private static final Object INIT_LOCK = new Object();

    private Storage(Context context) {
        SQLiteOpenHelper mOpenHelper = new SHSQLiteOpenHelper(context);
        database = mOpenHelper.getWritableDatabase();
        dataCompartment = cupboard().withDatabase(database);
        rxDatabase = RxCupboard.with(cupboard(), database);
    }

    public static void init(Context context) {
        if (instance == null) {
            synchronized (INIT_LOCK) {
                if (instance == null) {
                    instance = new Storage(context.getApplicationContext());
                }
            }
        }
    }

    @NonNull
    public static Storage get() {
        if (instance == null) {
            throw new NullPointerException("To initialize an instance of the object to obtain.");
        }
        return instance;
    }

    public long put(Object entity) {
        return dataCompartment.put(entity);
    }

    public void put(Collection<?> entities) {
        dataCompartment.put(entities);
    }

    public int clearTable(Class classId) {
        return dataCompartment.delete(classId, null);
    }

    public boolean delete(Object entity) {
        return dataCompartment.delete(entity);
    }

    public boolean delete(Class<?> className, long id) {
        return dataCompartment.delete(className, id);
    }

    public int delete(Class<?> className, String selection, String... args) {
        return dataCompartment.delete(className, selection, args);
    }

    @NonNull
    public <T> List<T> get(Class<T> className) {
        return dataCompartment.query(className).list();
    }

    public <T> Observable<List<T>> getObservable(Class<T> className) {
        return Observable.create(subscriber -> {
            if (!subscriber.isUnsubscribed()) {
                subscriber.onNext(Storage.get().get(className));
                subscriber.onCompleted();
            }
        });
    }

    @Nullable
    public <T> T get(Class<T> className, long id) {
        return dataCompartment.get(className, id);
    }

    @NonNull
    public <T> DatabaseCompartment.QueryBuilder<T> getQuery(Class<T> className) {
        return dataCompartment.query(className);
    }

    public int update(Class<?> entityClass, ContentValues values,
                      String selection, String... selectionArgs) {
        return dataCompartment.update(entityClass, values, selection, selectionArgs);
    }

    public void beginTransaction() {
        database.beginTransactionNonExclusive();
    }

    public void setTransactionSuccessful() {
        database.setTransactionSuccessful();
    }

    public void endTransaction() {
        database.endTransaction();
    }

}
