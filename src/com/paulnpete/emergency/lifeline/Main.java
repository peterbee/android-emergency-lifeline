package com.paulnpete.emergency.lifeline;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Main extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_code);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_pass_code, menu);
        return true;
    }
}
