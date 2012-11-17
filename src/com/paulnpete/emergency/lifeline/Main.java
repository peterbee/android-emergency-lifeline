package com.paulnpete.emergency.lifeline;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class Main extends Activity {
	
	Intent dangerMode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dangerMode = new Intent(this, DangerMode.class);
    }
    
    public void onStart() {
    	super.onStart();
    	//this is where the check for a pass code should be
    	startActivity(dangerMode);//hard-coded to test the SAFE button, to be changed to pass code
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_pass_code, menu);
        return true;
    }
}
