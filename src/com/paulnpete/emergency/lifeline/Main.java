package com.paulnpete.emergency.lifeline;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.Menu;

public class Main extends Activity {
	
	Intent passcodeActivity;
	Intent dangerModeActivity;
	String passCode;
	static String passCodeString;
	String passCode1;
	String passCode2;
	String currPassCode;
	Boolean wrong = false;
	static final int NEW_PASSCODE_REQUEST_1 = 0;
	static final int NEW_PASSCODE_REQUEST_2 = 1;
	static final int VERIFY_PASSCODE_REQUEST = 2;
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        passcodeActivity = new Intent(this, PassCode.class);
        dangerModeActivity = new Intent(this, DangerMode.class);
    }
    
    public void onStart() {
    	super.onStart();
    	SharedPreferences pref = getSharedPreferences("passCode", 1);
    	passCode = pref.getString("passCode", "");
        Log.v("Main","have set passcode string to saved SharedPreference" + "'" + passCode + "'");
    	//set a check here for presence of SharedPreference
    	//PassCode.resetBool(); //this is temp
    	
    	if (passCode == "" || passCode == null || passCode == " ") {
    		Log.v("Main","passcode is null");
    		//passCode = "1234";
    		passcodeActivity.putExtra("message", "Create a passcode");
    		startActivityForResult(passcodeActivity, NEW_PASSCODE_REQUEST_1);
    	} else {
//    		PassCode.setMessage("@string/enterPassCode");
    		passcodeActivity.putExtra("message", "@string/enterPassCode");
    		passcodeActivity.putExtra("verifyCode", passCode);
    		startActivityForResult(passcodeActivity, VERIFY_PASSCODE_REQUEST);
    		/*currPassCode = passCodeString;
    		Log.v("Main","received pass code from PassCode");
           	while (currPassCode != passCode) {
           		PassCode.setMessage("@string/PassCodeW");
           		startActivityForResult(passcodeActivity, PASSCODE_REQUEST);
           	}
           		startActivity(dangerModeActivity);*/
    	}
    	Log.v("Main", "have verified passcode existence");
    	//startActivity(passcodeActivity);//hard-coded to test the pass code button
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_pass_code, menu);
        return true;
    }
    
	public void setPassCode(String string) {
		Editor editor = getSharedPreferences("passCode", 0).edit();
		editor.putString("passCode", string);
		editor.commit();
		Log.v("Main","saved passCode " + passCode);
	}
    
    public void setNewPasscode() {
    	/*if (!wrong) {
    		PassCode.setMessage("Enter new Pass Code: f");  //("@string/newPassCode1");
    	} else {
    		PassCode.setMessage("@string/newPassCode1w");
    	}
    	startActivityForResult(passcodeActivity, PASSCODE_REQUEST);
    	passCode1 = passCode;
    	Log.v("passCode1","1 is " + passCode1);
    	
    	PassCode.setMessage("@string/newPassCode2");
    	startActivityForResult(passcodeActivity, PASSCODE_REQUEST);
    	passCode2 = passCode;
    	Log.v("passCode2","2 is " + passCode1);*/
    	
    	if (passCode1 == passCode2) {
    		setPassCode(passCode1);
    		// TODO: passcode saved -- message appropriately
    	} else {
    		//change message text to WRONG and stuff
    		passcodeActivity.putExtra("message", "They don't match.  Try again.");
    		startActivityForResult(passcodeActivity, NEW_PASSCODE_REQUEST_1);
    	}
    }
    
    /*public void resetPasscode() {
    	startActivityForResult(passcodeActivity, NEW_PASSCODE_REQUEST_1);
       	currPassCode = passCodeString;
       	if (currPassCode == passCode) {
       		setNewPasscode();
       	} else {
       		//change message text to WRONG and stuff
       		resetPasscode();
       	}
    }*/
    
    public static void pushPassCode(String string) {
    	passCodeString = string;
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if(requestCode == NEW_PASSCODE_REQUEST_1){
    		if(resultCode == RESULT_OK){
   				passCode1 = data.getStringExtra("passCode");
   				Log.v("Main","received passcode 1: " + passCode1);
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
    			startActivity(dangerModeActivity);
    		} else {
    			// TODO: danger mode not activated -- message this
    		}
    	}

    }
}
