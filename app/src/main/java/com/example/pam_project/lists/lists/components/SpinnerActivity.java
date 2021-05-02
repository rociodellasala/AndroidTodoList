package com.example.pam_project.lists.lists.components;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;

public class SpinnerActivity extends Activity implements AdapterView.OnItemSelectedListener {

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // Maybe put category color on spinner for user to check if category is ok
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Complete if categories can disappear while on view
    }
}
