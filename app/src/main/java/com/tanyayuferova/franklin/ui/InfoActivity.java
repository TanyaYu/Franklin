package com.tanyayuferova.franklin.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.tanyayuferova.franklin.R;
import com.tanyayuferova.franklin.utils.PreferencesUtils;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        PreferencesUtils.setHasInfoBeenShown(this, true);
    }

    public void onClickCloseBtn(View view) {
        this.finish();
    }
}
