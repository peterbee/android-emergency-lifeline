package com.paulnpete.emergency.lifeline;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;


public class DangerMode extends Activity{
	
	Intent main;
	ImageButton SAFE_Button;
	final Context context = this; 

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danger_mode);
        main = new Intent(this, Main.class);
		SAFE_Button = (ImageButton) findViewById(R.id.SAFEButton);
		SAFE_Button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(context, "Push and Hold to Activate", Toast.LENGTH_LONG).show();
			}
		});
		SAFE_Button.setOnLongClickListener(new View.OnLongClickListener() {
			public boolean onLongClick(View v) {
				return onSafeButton();
			}
		});
		Button exitButton = (Button) findViewById(R.id.ExitButton);
		exitButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				onExitButton();
			}
		});
    }

    public boolean onSafeButton() {
    	return true;
    	//this is where the data collection and such should be
    }
    
    public void onExitButton() {
    	startActivity(main);
    }
}

//imgButton.setBackgroundResource(R.drawable.ic_launcher);
