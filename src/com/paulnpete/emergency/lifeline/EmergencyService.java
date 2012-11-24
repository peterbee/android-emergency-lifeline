package com.paulnpete.emergency.lifeline;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class EmergencyService extends Service {
	Intent photoCaptureService;
	Intent audioCaptureService;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		Log.i("EmergencyService","Created service for the first time");
		photoCaptureService = new Intent(this,PhotoCapture.class);
	}
	
	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("EmergencyService", "Received start id " + startId + ": " + intent);
	    // We want this service to continue running until it is explicitly
	    // stopped, so return sticky.
        nextMediaEvent();
        return START_STICKY;
	}
	
	@Override
	public void onDestroy() {
		Log.i("EmergencyService","Stopping service process");
		stopService(photoCaptureService);
		super.onDestroy();
	}
	
	public void nextMediaEvent() { // TODO: call this method on a timer
		startService(photoCaptureService);
	}
}
