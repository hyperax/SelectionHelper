package tanya.arthur.selectionhelper.data.events;

import android.support.annotation.NonNull;

import java.util.List;

import tanya.arthur.selectionhelper.helpers.ConvertUtils;
import tanya.arthur.selectionhelper.helpers.NpeUtils;

public class DataUpdatedEvent extends BaseEvent {
    private List<Class> updatedEntities;

    public DataUpdatedEvent(List<Class> updatedEntities) {
        this.updatedEntities = updatedEntities;
    }

    public DataUpdatedEvent(Class... updatedEntities) {
        this.updatedEntities = ConvertUtils.toList(updatedEntities);
    }

    @NonNull
    public List<Class> getUpdatedEntities() {
        return NpeUtils.getNonNull(updatedEntities);
    }

    public <T> boolean contain(T entityClass) {
        return getUpdatedEntities().contains(entityClass.getClass());
    }
}
