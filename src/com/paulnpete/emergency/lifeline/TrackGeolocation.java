package com.paulnpete.emergency.lifeline;

import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class TrackGeolocation extends Service {
	String postUrl = "http://sites.limetreecreative.com/lifeline/emergencyPost.php";
	String postQuery = "";
	LocationManager locationManager;
	LocationListener locationListener;
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		startLocationListener();
		return START_STICKY;
	}
	
	@Override
	public void onDestroy(){
		stopLocationListener();
	}
	
	protected void startLocationListener(){
		Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		new SendLocationTask().execute(lastKnownLocation);
		locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				new SendLocationTask().execute(location);
			}
			public void onStatusChanged(String provider, int status, Bundle extras) {}
			public void onProviderEnabled(String provider) {}
			public void onProviderDisabled(String provider) {}
		};
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 10, locationListener);
	}
	
	protected void stopLocationListener(){
		locationManager.removeUpdates(locationListener);
	}
	
	private class SendLocationTask extends AsyncTask<Location, Void, Void> {

		protected Void doInBackground(Location...locations) {
			if(locations.length < 1) return null;
			
			HttpURLConnection connection = null;
			Location geolocation = locations[0];
			String urlServer = postUrl;

			try {
				// Add geolocation to post
				if(geolocation != null){
					postQuery += "&latitude="+geolocation.getLatitude();
					postQuery += "&longitude="+geolocation.getLongitude();
				}
				
				// Upload the file
				URL url = new URL(urlServer + "?" + postQuery);
				connection = (HttpURLConnection) url.openConnection();
				Log.i("TrackGeolocation",url.toString());
				
				// Allow Inputs & Outputs
				connection.setDoInput(true);
				connection.setDoOutput(true);
				connection.setUseCaches(false);

				// Enable POST method
				connection.setRequestMethod("GET");

				// Responses from the server (code and message)
				int serverResponseCode = connection.getResponseCode();
				String serverResponseMessage = connection.getResponseMessage();

				connection.getOutputStream().flush();

				Log.v("TrackGeolocation","Response Code: "+serverResponseCode);
				Log.v("TrackGeolocation","Response Message: "+serverResponseMessage);

				stopSelf();
			} catch (Exception e) {
				Log.e("TrackGeolocation","Request error: "+e.toString());
			}
			return null;
		}

		// This is called each time you call publishProgress()
		protected void onProgressUpdate(Integer... progress) {
			//setProgressPercent(progress[0]);
		}

		// This is called when doInBackground() is finished
		protected void onPostExecute(Long result) {
			Log.i("TrackGeolocation","Request complete");
		}
	}
	

}
