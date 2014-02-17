package com.example.downloadmanager;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class DownloadManagerHelper {

	private DownloadManager mgr = null;
	private Context mContext;
	private long lastDownload = -1L;
	
	public DownloadManagerHelper(Context context){
		this.mContext = context;
		mgr = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
	}
	
	public String statusMessage(Cursor c) {
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
	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public String queryStatus() {
		String status="";
		Cursor c = mgr.query(new DownloadManager.Query()
				.setFilterById(lastDownload));

		if (c == null) {
			Toast.makeText(mContext, "Download not Found!",
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
	
	  @SuppressLint("NewApi")
	public void startDownload(String URL, String file_name) {
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
