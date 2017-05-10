package com.example.notificationcontroller;

import java.io.IOException;
import java.io.InputStream;
import java.util.TimerTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

public class PollTask extends TimerTask {
	
	private String ip;
	private String notification;
	private String acknowledge;
	private boolean dinging = false;
	Context context;
	
	public PollTask(String ip, String notification, String acknowledge, Context context){
		this.ip = ip;
		this.notification = notification;
		this.context = context;
		this.acknowledge = acknowledge;
	}

	@Override
	public void run() {
		if (!dinging){
			Log.d("hi", "in run");
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			HttpGet httpGet = new HttpGet(ip + notification);
			String text = null;
			try {
				HttpResponse response = httpClient.execute(httpGet, localContext);
	
	
				HttpEntity entity = response.getEntity();
	
	
				text = getASCIIContentFromEntity(entity);
				
				Log.d("yolo", text);
				
				if (text.equals("true")){
					dinging = true;
					try {
					    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
					    Ringtone r = RingtoneManager.getRingtone(context, notification);
					    r.play();
					    while (dinging){
					    	sendAcknowledge();
					    }
					} catch (Exception e) {
					    e.printStackTrace();
					}
				}
	
			} catch (Exception e) {
				Log.e("oops", e.getLocalizedMessage());
			}
		}
}
	
	public void sendAcknowledge(){
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		HttpGet httpGet = new HttpGet(ip + acknowledge);
		String text = null;
		try {
			HttpResponse response = httpClient.execute(httpGet, localContext);


			HttpEntity entity = response.getEntity();


			text = getASCIIContentFromEntity(entity);
			
			if (text.equals("true")){
				dinging = false;
			}
			
			Log.d("response", text);
		} catch (Exception ex){
			Log.e("oops", ex.getMessage());
		}
	}
	
	protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
		InputStream in = entity.getContent();


		StringBuffer out = new StringBuffer();
		int n = 1;
		while (n>0) {
			byte[] b = new byte[4096];
			n =  in.read(b);


			if (n>0) out.append(new String(b, 0, n));
		}


		return out.toString();
	}

}
