package tanya.arthur.selectionhelper.view.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import org.androidannotations.annotations.EActivity;

import tanya.arthur.selectionhelper.R;

@EActivity
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            initStartFragment();
        }
    }

    private void initStartFragment() {
        // TODO instantiate start fragment
    }

}
