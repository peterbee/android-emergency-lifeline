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
				Log.v("ok button was clicked!","ok button was clicked and here's what it got: " + passCodeString);
				getIntent().putExtra("passcode", passCodeString);//.pushPassCode(passCodeText.getText().toString());
				//getPassCode();
				finish();
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
	
	public static void setNewBool() {
		setNewTrue = true;
	}
	
	public static void resetBool() {
		resetTrue = true;
	}
	
	public static void timerBool() {
		timerTrue = true;
	}
}
