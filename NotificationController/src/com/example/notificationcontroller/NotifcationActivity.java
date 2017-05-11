package com.example.notificationcontroller;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.notificationcontroller.util.SystemUiHider;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class NotifcationActivity extends Activity {
	
	private WakeLock screenWakeLock;
	DevicePolicyManager mDPM;
	ComponentName mAdminName;
	private boolean started = false;
	
	private final int REQUEST_ENABLE = 1;
	/**
	 * Whether or not the system UI should be auto-hidden after
	 * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
	 */
	private static final boolean AUTO_HIDE = true;

	/**
	 * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
	 * user interaction before hiding the system UI.
	 */
	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

	/**
	 * If set, will toggle the system UI visibility upon interaction. Otherwise,
	 * will show the system UI visibility upon interaction.
	 */
	private static final boolean TOGGLE_ON_CLICK = true;

	/**
	 * The flags to pass to {@link SystemUiHider#getInstance}.
	 */
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	private SystemUiHider mSystemUiHider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_notifcation);

		Log.d("test", "test3");
		//Takes care of device admin (this is what allows us to lock the screen programmatically
				mDPM = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
				mAdminName = new ComponentName(this, MyAdmin.class);
				
				//Acquires a lock that bypasses HTC's lock screen. This way when the app receives a signal it displays the call screen and not the lock screen
				KeyguardManager keyguardManager = (KeyguardManager)getSystemService(Activity.KEYGUARD_SERVICE);
				KeyguardLock lock = keyguardManager.newKeyguardLock(KEYGUARD_SERVICE);
				lock.disableKeyguard();
				screenWakeLock = ((PowerManager)getSystemService(POWER_SERVICE)).newWakeLock(
					     PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
				//Wakes up the screen and bypasses lock screen
				screenWakeLock.acquire();
				
				//Ensures that the app is full screen so that the notification bar cannot be viewed
				getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
				getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		                WindowManager.LayoutParams.FLAG_FULLSCREEN);
				

				deviceAdminSetup();
				setUpButton();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		
	}
	
	public void setUpButton(){
		Button startButton = (Button) findViewById(R.id.button1);
		Button stopButton = (Button) findViewById(R.id.button2);
		
		startButton.setOnClickListener(new View.OnClickListener() {

	        @Override
	        public void onClick(View v) {
	           startProcess();
	           lockScreen();
	        }
	    });
		
		stopButton.setOnClickListener(new View.OnClickListener() {

	        @Override
	        public void onClick(View v) {
	           stopProcess();
	           lockScreen();
	        }
	    });
	}
	
	public void startProcess(){
		if (started){
			stopService(new Intent(this, PollService.class));
		}
		startService(new Intent(this, PollService.class));
		started = true;
		Log.d("yolo", "starting process");
	}
	
	public void stopProcess(){
		if (started){
			stopService(new Intent(this, PollService.class));
			started = false;
		}
	}
	
	/*
	 * Sets up device admin
	 */
	public void deviceAdminSetup(){
		//if (!mDPM.isAdminActive(mAdminName)) {
			// try to become active – must happen here in this activity, to get result
			Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
			intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mAdminName);
			intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "text stuff");
			startActivityForResult(intent, REQUEST_ENABLE);

		//}
	}
	
	/**
	 * Locks the screen
	 */
	public void lockScreen(){
		try {
			screenWakeLock.release();
		} catch (Exception e){
			
		}
		mDPM.lockNow();
		//}
	}

	public static class MyAdmin extends DeviceAdminReceiver {
		// implement onEnabled(), onDisabled(), …
	}
}
