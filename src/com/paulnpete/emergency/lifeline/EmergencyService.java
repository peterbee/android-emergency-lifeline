package com.paulnpete.emergency.lifeline;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class EmergencyService extends Service {
	Intent trackGeolocationService;
	Intent photoCaptureService;
	Intent audioCaptureService;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		Log.i("EmergencyService","Created service for the first time");
		trackGeolocationService = new Intent(this,TrackGeolocation.class);
		photoCaptureService = new Intent(this,PhotoCapture.class);
		audioCaptureService = new Intent(this,AudioCapture.class);
	}
	
	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("EmergencyService", "Received start id " + startId + ": " + intent);
        startService(trackGeolocationService);
		//startService(audioCaptureService);
        startService(photoCaptureService);
	    // We want this service to continue running until it is explicitly
	    // stopped, so return sticky.
        return START_STICKY;
	}
	
	@Override
	public void onDestroy() {
		Log.i("EmergencyService","Stopping service process");
		stopService(trackGeolocationService);
		stopService(photoCaptureService);
		//stopService(audioCaptureService);
		super.onDestroy();
	}
	
}
