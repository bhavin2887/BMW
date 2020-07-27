package com.example.garbagecollection.doctor;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.garbagecollection.bmw.GarbageDataStorage;
import com.example.garbagecollection.bmw.R;
import com.example.garbagecollection.bmw.WebService;

import java.util.ArrayList;

public class DoctorVerifyActivity extends Activity{
	
	Button btn,btn_server;
    SharedPreferences userDetails;
	public static final String MyPREFERENCES = "MyPrefs" ;
	SharedPreferences sharedpreferences;
	private GarbageDataStorage garbageDataStorageObject = new GarbageDataStorage(this);
	TextView txt_emp,txt_veh,txt_route,txt_detail,txt_imei;
	Cursor cursor1, cursor2, cursor3;
	ListView listView ;
	public static int btn_Hospital_scanCode = 11;
	SharedPreferences.Editor editor;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.doctor_verify);
		
		btn = (Button)findViewById(R.id.btn_sync);

		userDetails = getApplicationContext().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
		sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		editor = sharedpreferences.edit();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			ActivityCompat.requestPermissions(DoctorVerifyActivity.this, new String[]{Manifest.permission.CAMERA},1);
		}

		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
	        	if(isConnectingToInternet()){
					//new WebServiceGetServer("QEEID_850|DrName:DR. BHAVESH M PATEL|HospitalName:MEERA CLINIC|Area:ELLORA PARK|Pincode 390023|PhoneNo:3119953|MobileNo:0|GPCBId:0").execute();
					Intent i = new Intent();
					i.setClass(DoctorVerifyActivity.this, MainScreenDoctor.class);
					startActivityForResult(i, btn_Hospital_scanCode);
	        	}else{
	        		Toast.makeText(getApplicationContext(), "Please check your Internet Connection.", Toast.LENGTH_SHORT).show();
	        	}
			}
		});

	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
		switch (requestCode) {
			case 1: {

				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

					// permission was granted, yay! Do the
					// contacts-related task you need to do.
				} else {

					// permission denied, boo! Disable the
					// functionality that depends on this permission.
					Toast.makeText(DoctorVerifyActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
				}
				return;
			}

			// other 'case' lines to check for other
			// permissions this app might request
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if(requestCode == btn_Hospital_scanCode && data!=null){
			if(resultCode != 0)
			{
					new WebServiceGetServer(data.getStringExtra("hospital_id"), data.getStringExtra("hospitalContent")).execute(); //MembershipNo
			}
		}
	}

	private class WebServiceGetServer extends AsyncTask<String, String, String> {

		String data, id;
		String err;
		String soap;
		private ProgressDialog dialog;

		WebServiceGetServer(String id, String data){
			this.id = id;
			this.data = data;
		}

		@Override
		protected void onPreExecute() {
			//dialog = new ProgressDialog(DoctorVerifyActivity.this);
			garbageDataStorageObject.open();
			//dialog.setMessage("Please wait...");
			//dialog.show();
			Toast.makeText(DoctorVerifyActivity.this, "Please wait!!!", Toast.LENGTH_SHORT).show();

		}

		@Override
		protected String doInBackground(String... params) {
			publishProgress("Sleeping..."); // Calls onProgressUpdate()
			try {
				soap = WebService.webService_AuthenticateDoctor.callWebserviceCheck(data);
			} catch (Exception e) {
				e.printStackTrace();
				err = e.getMessage();
			}
			return soap;
		}

		@Override
		protected void onPostExecute(String soapObject) {
			final ArrayList<String> myList;
			if(soapObject.equalsIgnoreCase("true")) {
				Intent ite = new Intent(DoctorVerifyActivity.this, HospitalDoctor.class);
				ite.putExtra("hospitalContent",  data);
				ite.putExtra("hospital_id",  id);
				startActivity(ite);
				finish();
			}else{
				Toast.makeText(DoctorVerifyActivity.this, "Doctor ID did not match", Toast.LENGTH_SHORT).show();
			}
			//if (dialog.isShowing()) {
			//	dialog.dismiss();
			//}
		}
	}

	public boolean isConnectingToInternet(){
		ConnectivityManager connectivity = (ConnectivityManager) DoctorVerifyActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
		  if (connectivity != null) 
		  {
			  NetworkInfo[] info = connectivity.getAllNetworkInfo();
			  if (info != null) 
				  for (int i = 0; i < info.length; i++) 
					  if (info[i].getState() == NetworkInfo.State.CONNECTED)
					  {
						  return true;
					  }
		  }
		  return false;
	}
}
