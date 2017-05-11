package camino.solutions.songcontroller;

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
	private static final String SONGSTART = "/api/v1/issongstartqueued";
	private static final String ACKNOWLEDGE = "/api/v1/igotthesongqueue";
	private static final String LAUNCH = "/api/v1/startsong";
	private static final String INSURANCE = "/api/v1/didyougetit";
	private SongTask songTask;
	private PollTask pollTask;
	private Timer timer;
	
	public PollService(){
		songTask = new SongTask();
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
		
		pollTask = new PollTask(IP, SONGSTART, LAUNCH, ACKNOWLEDGE, songTask);
        //running timer task as daemon thread
        timer = new Timer(true);
        timer.scheduleAtFixedRate(pollTask, 0, 1000);

		Log.d(TAG, "onStart");
	//Note: You can start a new thread and use it for long background processing from here.
	}



	@Override
	public void onDestroy() {
		Toast.makeText(this, "PollService Stopped", Toast.LENGTH_LONG).show();
		songTask.stopSong();
		timer.cancel();
		Log.d(TAG, "onDestroy");
	}

	
}
