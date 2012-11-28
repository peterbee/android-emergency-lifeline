package com.paulnpete.emergency.lifeline;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class DangerMode extends Activity{
	
	Intent main;
	Intent emergencyService;
	Intent passcodeActivity;
	ImageButton SAFE_Button;
	final int STATE_WAITING = 1;
	final int STATE_SAFE = 2;
	final int STATE_EMERGENCY = 3;
	static final int VERIFY_PASSCODE_REQUEST = 0;
	int appState = STATE_WAITING;
	final Context context = this; 
	String passCode;
	TextView textView;
	TextView danger;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danger_mode);
        main = new Intent(this, Main.class);
        emergencyService = new Intent(this, EmergencyService.class);
        passcodeActivity = new Intent(this, PassCode.class);
        textView = (TextView) findViewById(R.id.textView1);
        danger = (TextView) findViewById(R.id.textView2);
		SAFE_Button = (ImageButton) findViewById(R.id.SAFEButton);
		SAFE_Button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(context, "Push and Hold to Activate", Toast.LENGTH_LONG).show();
			}
		});
		SAFE_Button.setOnLongClickListener(new View.OnLongClickListener() {
			public boolean onLongClick(View v) {
				// long click during waiting state
				if(appState == STATE_WAITING) {
					return enterSafeState();
				}
				return false;
			}
		});
		SAFE_Button.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				// ACTION_UP during safe state
				if(appState == STATE_SAFE && event.getAction() == MotionEvent.ACTION_UP) {
					enterEmergencyState();
				}
				return false;
			}
		});
		Button exitButton = (Button) findViewById(R.id.ExitButton);
		exitButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				onExitButton();
			}
		});
		
		SharedPreferences pref = getSharedPreferences("passCode", 1);
    	passCode = pref.getString("passCode", "");
        Log.v("DangerMode","set verification passCode to " + "'" + passCode + "'");
    }
    
    public boolean enterSafeState() {
		appState = STATE_SAFE;
    	return true;
    }
    
    public boolean enterEmergencyState() {
		appState = STATE_EMERGENCY;
    	startService(emergencyService);
    	emergencyView();
    	passcodeActivity.putExtra("requestID", VERIFY_PASSCODE_REQUEST);
    	startActivityForResult(passcodeActivity, VERIFY_PASSCODE_REQUEST);
    	// TODO: change button or remove it?
    	// TODO: display passcode view
    	return true;
    }
    
    public void exitDangerMode() {
    	appState = STATE_WAITING;
    	stopService(emergencyService);
    	Log.i("DangerMode","Emergency service deactivated");
    	finish();
    }

    public void onExitButton() {
    	//passcodeActivity.putExtra("requestID", VERIFY_PASSCODE_REQUEST);
    	//startActivityForResult(passcodeActivity, VERIFY_PASSCODE_REQUEST);
    	exitDangerMode(); // TODO: this should be actually happen when correct passcode is entered to disable emergency state
    	//startActivity(main);
    }

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == VERIFY_PASSCODE_REQUEST){
			if(resultCode == RESULT_OK){
				if (passCode.equals(data.getStringExtra("passCode")))
					exitDangerMode();
					//finish(); //startActivity(main);
			} else if(resultCode == RESULT_CANCELED){
    			Toast.makeText(context, "Incorrect Pass Code", Toast.LENGTH_SHORT).show();
    			passcodeActivity.putExtra("message", "Incorrect Pass Code");
    			passcodeActivity.putExtra("requestID", VERIFY_PASSCODE_REQUEST);
    			startActivityForResult(passcodeActivity, VERIFY_PASSCODE_REQUEST);
			} else {
    			Toast.makeText(context, "Danger Mode not deactivated", Toast.LENGTH_SHORT).show();
				passcodeActivity.putExtra("message", "Enter your Pass Code");
    			passcodeActivity.putExtra("requestID", VERIFY_PASSCODE_REQUEST);
	    		startActivityForResult(passcodeActivity, VERIFY_PASSCODE_REQUEST);
			}
		}
	}
	
	public void emergencyView() {
    	textView.setText("EMERGENCY!");
    	textView.setTextSize(46);
    	textView.setTextColor(Color.RED);
    	danger.setVisibility(View.VISIBLE);
	}

}

//imgButton.setBackgroundResource(R.drawable.ic_launcher);
