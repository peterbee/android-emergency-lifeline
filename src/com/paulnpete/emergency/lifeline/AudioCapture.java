package com.paulnpete.emergency.lifeline;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

public class AudioCapture extends Service {
	protected MediaRecorder audioRecorder = null;
	protected String audioFilename = null;
	Intent uploadService;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.v("AudioCapture","onCreate");
		uploadService = new Intent(this, UploadFile.class);
		audioRecorder = new MediaRecorder();
		audioFilename = String.format(
			"%s/EmergencyLifeline/%s.3gp",
			Environment.getExternalStorageDirectory(),
			System.currentTimeMillis()
		);
	}

	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
		Log.v("AudioCapture","onStartCommand");
		captureAudio();
        return START_STICKY;
	}
	
	private void captureAudio() {
		Log.i("AudioCapture","Starting audio recording");
		try {
			audioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
	        audioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
	        audioRecorder.setOutputFile(audioFilename);
	        audioRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

	        Timer audioTimer = new Timer("audioTimer",true);
	        audioTimer.schedule(new AudioTimer(),3000);
		} catch(Exception e) {
			Log.e("AudioCapture",e.getMessage());
			stopSelf();
		}
        
	}
	
	private class AudioTimer extends TimerTask {
		public void run(){
			Log.i("AudioCapture","Stopping audio recording");
			try {
				audioRecorder.stop();
		        audioRecorder.reset();
	
		        uploadService.putExtra("filepath",audioFilename);
				startService(uploadService);
				
				// TODO: run this in a loop
			} catch(Exception e) {
				Log.e("AudioCapture",e.getMessage());
			}
	        audioRecorder.release();
			stopSelf();
		}
	}
	
}
