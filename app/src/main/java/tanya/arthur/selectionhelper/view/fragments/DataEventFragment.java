package tanya.arthur.selectionhelper.view.fragments;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import tanya.arthur.selectionhelper.data.events.DataUpdatedEvent;
import tanya.arthur.selectionhelper.helpers.DateUtils;

public abstract class DataEventFragment extends EventSupportFragment {

    private long dataTimestamp;

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void handleEvent(DataUpdatedEvent event) {
        checkIsDataChanged();
    }

    private void checkIsDataChanged() {
        if (dbQuery.isDataChanged(getTrackedEntities(), dataTimestamp)) {
            onDataChanged();
            updateDataTimestamp();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        checkIsDataChanged();
    }

    abstract protected Class[] getTrackedEntities();

    protected void updateDataTimestamp() {
        dataTimestamp = DateUtils.getCurrentMillis();
    }

    abstract protected void onDataChanged();

}
