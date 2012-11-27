package com.paulnpete.emergency.lifeline;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;


public class PassCode extends Activity {
	
	static boolean resetTrue = false;
	static boolean setNewTrue = false;
	static boolean timerTrue = false;
	Button buttonReset;
	static Button buttonOk;
	static EditText passCodeText;
	Chronometer timer;
	static TextView enterPassCodeText;
	String passCode;
	
	static String passCodeString;
	String newPassCode;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_code);
        buttonReset = (Button) findViewById(R.id.ButtonReset);
        passCodeText = (EditText) findViewById(R.id.editText1);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(passCodeText, InputMethodManager.SHOW_IMPLICIT);
        
        buttonOk = (Button) findViewById(R.id.buttonOK);
		buttonOk.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				passCodeString = passCodeText.getText().toString();
				Log.v("PassCode","ok button was clicked and here's what it got: " + passCodeString);
				if (getIntent().getIntExtra("requestID", 0) == 0) {
					if (checkPassCodes()) {
						getIntent().putExtra("passCode", passCodeString);
						setResult(RESULT_OK, getIntent());
						finish();
					} else {
						setResult(RESULT_CANCELED, getIntent());
						finish();
					}
				}  else {
					getIntent().putExtra("passCode", passCodeString);
					setResult(RESULT_OK, getIntent());
					finish();
				}
			}
		});
        
        timer = (Chronometer) findViewById(R.id.chronometer1);
        enterPassCodeText = (TextView) findViewById(R.id.textView1);
        
        if (resetTrue) {
        	buttonReset.setVisibility(View.VISIBLE);
        	enterPassCodeText.setVisibility(View.VISIBLE);
        }
        if (timerTrue) timer.setVisibility(View.VISIBLE);
        //if (setNewTrue) setNewPass();
        
        String displayMessage = getIntent().getStringExtra("message");
        setMessage(displayMessage);
    }
	
	public static String getPassCode() {

		return passCodeString;

	}
	
	public void setPassCode() {
		passCodeString = passCodeText.getText().toString();
		SharedPreferences settings = getSharedPreferences(passCodeString, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("passCode", passCodeString);
		
		editor.commit();
	}
	
	public static void setMessage(String string) {
		if(string != null)
			enterPassCodeText.setText(string);
		Log.v("PassCode","string was set to message");
	}
	
	//code intended to make a check of the saved and the entered passCodes
	public boolean checkPassCodes() {
		Log.v("PassCode","passCodeString = " + passCodeString);
		SharedPreferences pref = getSharedPreferences("passCode", 1);
    	passCode = pref.getString("passCode", "");
        Log.v("PassCode","SharedPreferences passCode = " + "'" + passCode + "'");
        if (passCode.equals(passCodeString))
        	return true;
        return false;
	}

}
