package com.example.downloadmanager;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class MainActivity extends Activity implements OnClickListener {

	Button btnDownload1, btnDownload2;
	private DownloadManager mgr = null;
	private long lastDownload = -1L;
	String status;
	private String url1 = "http://mobioapp.net/bardhaman_house/pdf/anginay_joshnate.pdf";
	private String url2 = "http://mobioapp.net/bardhaman_house/pdf/JosnaRaterJonaki.pdf";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btnDownload1 = (Button) findViewById(R.id.button1);
		btnDownload2 = (Button) findViewById(R.id.button2);

		btnDownload1.setOnClickListener(this);
		btnDownload2.setOnClickListener(this);
		
		mgr = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		//String status = queryStatus();
		try{
		status = queryStatus();
		} catch(Exception E) {
			E.printStackTrace();
		}
		switch (v.getId()) {
		case R.id.button1:
			if(status == "Download Running") {
				Toast.makeText(getApplicationContext(), "Try later!", Toast.LENGTH_SHORT).show();
			} else {
				startDownload(url1, "Anginay.pdf");
			}
			//
			break;
		case R.id.button2:
			if(status == "Download Running") {
				Toast.makeText(getApplicationContext(), "Try later!", Toast.LENGTH_SHORT).show();
			} else {
				startDownload(url2, "Jonaki.pdf");
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		IntentFilter f = new IntentFilter(
				DownloadManager.ACTION_DOWNLOAD_COMPLETE);

//		f.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED);
//		f.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
		registerReceiver(onEvent, f);
	}

	@Override
	public void onPause() {
		unregisterReceiver(onEvent);
		super.onPause();
	}

	private BroadcastReceiver onEvent = new BroadcastReceiver() {
		public void onReceive(Context ctxt, Intent i) {
			if (DownloadManager.ACTION_NOTIFICATION_CLICKED.equals(i
					.getAction())) {
				Toast.makeText(ctxt, "Hiiiii!", Toast.LENGTH_LONG).show();
			} else if(DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(i.getAction())){
				// start.setEnabled(true);
			}
		}
	};

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	private String queryStatus() {
		String status="";
		Cursor c = mgr.query(new DownloadManager.Query()
				.setFilterById(lastDownload));

		if (c == null) {
			Toast.makeText(getApplicationContext(), "Download not Found!",
					Toast.LENGTH_LONG).show();
		} else {
			c.moveToFirst();

			Log.d(getClass().getName(),
					"COLUMN_ID: "
							+ c.getLong(c
									.getColumnIndex(DownloadManager.COLUMN_ID)));
			Log.d(getClass().getName(),
					"COLUMN_BYTES_DOWNLOADED_SO_FAR: "
							+ c.getLong(c
									.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)));
			Log.d(getClass().getName(),
					"COLUMN_LAST_MODIFIED_TIMESTAMP: "
							+ c.getLong(c
									.getColumnIndex(DownloadManager.COLUMN_LAST_MODIFIED_TIMESTAMP)));
			Log.d(getClass().getName(),
					"COLUMN_LOCAL_URI: "
							+ c.getString(c
									.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)));
			Log.d(getClass().getName(),
					"COLUMN_STATUS: "
							+ c.getInt(c
									.getColumnIndex(DownloadManager.COLUMN_STATUS)));
			Log.d(getClass().getName(),
					"COLUMN_REASON: "
							+ c.getInt(c
									.getColumnIndex(DownloadManager.COLUMN_REASON)));

			status = statusMessage(c);
//			Toast.makeText(getApplicationContext(), statusMessage(c),
//					Toast.LENGTH_LONG).show();

			c.close();
		}
		return status;
	}

	private String statusMessage(Cursor c) {
		String msg = "???";

		switch (c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
		case DownloadManager.STATUS_FAILED:
			msg = "Download Failed!";
			break;

		case DownloadManager.STATUS_PAUSED:
			msg = "Download Paused!";
			break;

		case DownloadManager.STATUS_PENDING:
			msg = "Download Pending!";
			break;

		case DownloadManager.STATUS_RUNNING:
			msg = "Download Running";
			break;

		case DownloadManager.STATUS_SUCCESSFUL:
			msg = "Download Successful";
			break;

		default:
			msg = "Download out of sight!";
			break;
		}

		return (msg);
	}

	  @SuppressLint("NewApi")
	private void startDownload(String URL, String file_name) {
		    Uri uri=Uri.parse(URL);

		    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
		               .mkdirs();

		    DownloadManager.Request req=new DownloadManager.Request(uri);

		    req.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI
		                                   | DownloadManager.Request.NETWORK_MOBILE)
		       .setAllowedOverRoaming(false)
		       .setTitle("Demo")
		       .setDescription("Something useful. No, really.")
		       .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
		                                          file_name)
		    	.setNotificationVisibility(Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		    lastDownload=mgr.enqueue(req);

		   // v.setEnabled(false);
		    //query.setEnabled(true);
		  }
	
}
