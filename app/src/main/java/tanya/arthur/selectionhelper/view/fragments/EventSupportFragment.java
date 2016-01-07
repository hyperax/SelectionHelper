package tanya.arthur.selectionhelper.view.fragments;

import de.greenrobot.event.EventBus;

public class EventSupportFragment extends BaseFragment {

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

}
