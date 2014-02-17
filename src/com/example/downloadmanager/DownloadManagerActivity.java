package com.example.downloadmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class DownloadManagerActivity extends Activity implements OnClickListener{

	Button btnDownload1, btnDownload2, btnGotoMainPage;
	String status;
	private String url1 = "http://mobioapp.net/bardhaman_house/pdf/anginay_joshnate.pdf";
	private String url2 = "http://mobioapp.net/bardhaman_house/pdf/JosnaRaterJonaki.pdf";
	private DownloadManagerHelper downloadManagerHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		downloadManagerHelper = new DownloadManagerHelper(DownloadManagerActivity.this);
		
		btnDownload1 = (Button) findViewById(R.id.button1);
		btnDownload2 = (Button) findViewById(R.id.button2);
		btnGotoMainPage = (Button) findViewById(R.id.button3);

		btnDownload1.setOnClickListener(this);
		btnDownload2.setOnClickListener(this);
		btnGotoMainPage.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		try{
			status = downloadManagerHelper.queryStatus();
			} catch(Exception E) {
				E.printStackTrace();
			}
			switch (v.getId()) {
			case R.id.button1:
				if(status == "Download Running") {
					Toast.makeText(getApplicationContext(), "Try later!", Toast.LENGTH_SHORT).show();
				} else {
					downloadManagerHelper.startDownload(url1, "File1.pdf");
				}
				//
				break;
			case R.id.button2:
				if(status == "Download Running") {
					Toast.makeText(getApplicationContext(), "Try later!", Toast.LENGTH_SHORT).show();
				} else {
					downloadManagerHelper.startDownload(url2, "File2.pdf");
				}
				break;
			case R.id.button3:
				Intent i = new Intent(this, MainActivity.class);
				startActivity(i);
				break;
			}
	}

	
//	private BroadcastReceiver onDownloadReceiver = new BroadcastReceiver() {
//
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			// TODO Auto-generated method stub
//			if (DownloadManager.ACTION_NOTIFICATION_CLICKED.equals(intent
//					.getAction())) {
//				Toast.makeText(context, "Hiiiii!", Toast.LENGTH_LONG).show();
//			} else if(DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())){
//				// start.setEnabled(true);
//			}
//		}
//		
//	}; 
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

}
