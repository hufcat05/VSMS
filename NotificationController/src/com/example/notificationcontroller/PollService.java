package com.example.notificationcontroller;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class PollService extends Service{

	private static final String TAG = "MyService";
	private static final String IP = "http://172.16.56.10:8080";
	private static final String NOTIFICATION = "/api/v1/isnotificationqueued";
	private static final String ACKNOWLEDGE = "/api/v1/igotthenotificationqueue";

	private PollTask pollTask;
	
	public PollService(){
		
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		Toast.makeText(this, "Congrats! PollService Created", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onCreate");
	}

	@Override
	public void onStart(Intent intent, int startId) {
		Log.d("yolo", "Started");
		
		TimerTask timerTask = new PollTask(IP, NOTIFICATION, ACKNOWLEDGE, this);
        //running timer task as daemon thread
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(timerTask, 0, 1000);

		Log.d(TAG, "onStart");
	//Note: You can start a new thread and use it for long background processing from here.
	}



	@Override
	public void onDestroy() {
		Toast.makeText(this, "PollService Stopped", Toast.LENGTH_LONG).show();
		
		Log.d(TAG, "onDestroy");
	}

	
}
