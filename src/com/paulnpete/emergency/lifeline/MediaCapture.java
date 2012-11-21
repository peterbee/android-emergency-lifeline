package com.paulnpete.emergency.lifeline;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.SurfaceView;
import android.widget.Toast;

public class MediaCapture extends Activity {
	protected ArrayList<Camera> mCameras = new ArrayList<Camera>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pass_code);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_pass_code, menu);
		return true;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		openCameras();
		capturePhotos();
	}
	@Override
	public void onPause() {
		releaseCameras();
		super.onPause();
	}
	
	private int openCameras() {
		int openedCams = 0;
		releaseCameras();
		int numCams = Camera.getNumberOfCameras();
		for(int i=0;i<numCams;i++) {
			try {
				Camera mCamera = Camera.open(i);
				openedCams++;
				mCameras.add(i,mCamera);
				Log.v(getString(R.string.app_name), "successfully opened Camera "+i);
			} catch (Exception e) {
				Log.e(getString(R.string.app_name), "failed to open Camera "+i);
				Log.e(getString(R.string.app_name), e.getMessage());
			}
		}
		return openedCams;
	}
	
	private void releaseCameras() {
		int numCams = mCameras.size();
		for(int i=0;i<numCams;i++) {
			Camera mCamera = mCameras.get(i);
			if (mCamera != null) {
				mCamera.release();
				mCamera = null;
			}
		}
	}
	
	private void capturePhotos() {
		int numCams = mCameras.size();
		for(int i=0;i<numCams;i++){
			try {
				Camera mCamera = mCameras.get(i);
				SurfaceView view = new SurfaceView(this);
	            //addContentView(view, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	            Log.v("surface view",view.toString());
				mCamera.setPreviewDisplay(view.getHolder());
				mCamera.startPreview();
				mCamera.takePicture(null, null, jpegCallback);
			} catch (Exception e) {}
		}
	}

	PictureCallback jpegCallback = new PictureCallback() {
		public void onPictureTaken(byte[] imageData, Camera c) {
			if (imageData != null) {
				FileOutputStream outStream = null;
				try {
					File imageDir = new File(String.format(
							"%s/EmergencyLifeline/",
							Environment.getExternalStorageDirectory()
						));
					imageDir.mkdirs();
					File imageFile = new File(imageDir,
							String.format("%d.jpg", System.currentTimeMillis())
						);
					outStream = new FileOutputStream(imageFile);
					outStream.write(imageData);
					outStream.close();
					Log.d("TestApp", "onPictureTaken - wrote bytes: "
							+ imageData.length);

					c.startPreview();
					Toast.makeText(getApplicationContext(), String.format("%s written", imageFile), Toast.LENGTH_SHORT).show();

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
				}
			}
		}
	};
	
}
