package com.paulnpete.emergency.lifeline;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Main extends Activity {
	
	Intent passcodeActivity;
	Intent dangerModeActivity;
	String passCode;
	static String passCodeString;
	String passCode1;
	String passCode2;
	String currPassCode;
	static final int NEW_PASSCODE_REQUEST_1 = 1;
	static final int NEW_PASSCODE_REQUEST_2 = 2;
	static final int VERIFY_PASSCODE_REQUEST = 0;
	Button dangerModeButton;
	Button setPasscodeButton;
	Context context = this;
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        passcodeActivity = new Intent(this, PassCode.class);
        dangerModeActivity = new Intent(this, DangerMode.class);
        
        dangerModeButton = (Button) findViewById(R.id.ButtonDangerMode);
		dangerModeButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				passcodeActivity.putExtra("message", "Enter your Pass Code");
				passcodeActivity.putExtra("requestID", VERIFY_PASSCODE_REQUEST);
				startActivityForResult(passcodeActivity, VERIFY_PASSCODE_REQUEST);
			}
		});
		
        setPasscodeButton = (Button) findViewById(R.id.ButtonPassCode);
		setPasscodeButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				passcodeActivity.putExtra("message", "Create a Pass Code");
				startActivityForResult(passcodeActivity, NEW_PASSCODE_REQUEST_1);
			}
		});
    }
    
    public void onStart() {
    	super.onStart();
    	SharedPreferences pref = getSharedPreferences("passCode", 1);
    	passCode = pref.getString("passCode", "");
        Log.v("Main","have set passcode string to saved SharedPreference" + "'" + passCode + "'");
    	    	
    	if (passCode == "" || passCode == null || passCode == " ") {
    		Log.v("Main","passcode is null");
    		passcodeActivity.putExtra("message", "Create a Pass Code");
    		startActivityForResult(passcodeActivity, NEW_PASSCODE_REQUEST_1);
    	}
    	Log.v("Main", "have verified passcode existence");

    
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_pass_code, menu);
        return true;
    }
    
	public void setPassCode(String string) {
		Editor editor = getSharedPreferences("passCode", 0).edit();
		editor.remove("passCode");
		editor.commit();
		editor.putString("passCode", string);
		editor.commit();
		Log.v("Main","saved passCode: " + string);
		
		SharedPreferences pref = getSharedPreferences("passCode", 1);
    	passCode = pref.getString("passCode", "");
        Log.v("Main","have reloaded passcode string to saved SharedPreference" + "'" + passCode + "'");
	}
    
    public void setNewPasscode() {
    	if (passCode1.equals(passCode2)) {
    		setPassCode(passCode1);
    		// TODO: passcode saved -- message appropriately
    	} else {
    		//change message text to WRONG and stuff
    		passcodeActivity.putExtra("message", "They don't match.  Try again.");
    		startActivityForResult(passcodeActivity, NEW_PASSCODE_REQUEST_1);
    	}
    }
    
    
    public static void pushPassCode(String string) {
    	passCodeString = string;
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if(requestCode == NEW_PASSCODE_REQUEST_1){
    		if(resultCode == RESULT_OK){
   				passCode1 = data.getStringExtra("passCode");
   				Log.v("Main","received passcode 1: " + passCode1);
   	    		passcodeActivity.putExtra("message", "Re-enter new Pass Code");
   				startActivityForResult(passcodeActivity, NEW_PASSCODE_REQUEST_2);
    		}
    	} else if(requestCode == NEW_PASSCODE_REQUEST_2){
    		if(resultCode == RESULT_OK){
   				passCode2 = data.getStringExtra("passCode");
   				Log.v("Main","received passcode 2: " + passCode2);
   				setNewPasscode();
    		}
    	} else if(requestCode == VERIFY_PASSCODE_REQUEST){
    		if(resultCode == RESULT_OK){
    			if (passCode.equals(data.getStringExtra("passCode")))
    				dangerModeActivity.putExtra("passCode", passCode);
    				startActivity(dangerModeActivity);
    		} else if(resultCode == RESULT_CANCELED){
    			Toast.makeText(context, "Incorrect Pass Code", Toast.LENGTH_SHORT).show();
    			passcodeActivity.putExtra("message", "Incorrect Pass Code");
    			passcodeActivity.putExtra("requestID", VERIFY_PASSCODE_REQUEST);
    			startActivityForResult(passcodeActivity, VERIFY_PASSCODE_REQUEST);
    		} else {
    			Toast.makeText(context, "Danger Mode not activated", Toast.LENGTH_SHORT).show();
				passcodeActivity.putExtra("message", "Enter your Pass Code");
    			passcodeActivity.putExtra("requestID", VERIFY_PASSCODE_REQUEST);
        		startActivityForResult(passcodeActivity, VERIFY_PASSCODE_REQUEST);
    			// TODO: danger mode not activated -- message this
    		}
    	}

    }
}
