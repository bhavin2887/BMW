package com.example.garbagecollection.bmw;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends Activity {

	SharedPreferences sharedpreferences;

	TextView text_view;

	public static final String MyPREFERENCES = "MyPrefs" ;
	Button scan, home_entry_exit, home_settings;
	LinearLayout linear_status;
	private GarbageDataStorage garbageDataStorageObject = new GarbageDataStorage(this);
	TextView text_employee_no,text_vehicle_no,text_route;
	Button btn_employee,home_Start_Date, home_details;
	String str_emp, str_veh, str_rou;
	String str_rouID, str_vehID, str_empID;
	int str_emp_pos, str_veh_pos, str_rou_pos;
	public static File path;
	SharedPreferences userDetails;
	SharedPreferences.Editor editor;
	int valCount = 0;
    AlertDialog.Builder alertDialogBuilder;
	private static final String TAG = "HomeActivity";

    @Override
	protected void onResume() {
		super.onResume();
		setTitle("Home");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		text_view = (TextView)findViewById(R.id.textView1);
		scan = (Button)findViewById(R.id.home_scan);
		home_entry_exit = (Button)findViewById(R.id.home_entry_exit);
		linear_status =(LinearLayout)findViewById(R.id.linear_status);
		text_employee_no = (TextView)findViewById(R.id.text_employee_no);
		text_vehicle_no = (TextView)findViewById(R.id.text_vehicle_no);
		text_route = (TextView)findViewById(R.id.text_route);
		home_settings = (Button)findViewById(R.id.home_settings);
		btn_employee = (Button)findViewById(R.id.btn_employee);
		home_Start_Date = (Button)findViewById(R.id.home_Start_Date);
		home_details = (Button)findViewById(R.id.home_details);

		path = new File(Environment.getExternalStorageDirectory()+"/BMWPhoto/");
		if(!path.exists())
			path.mkdirs();

		userDetails = getApplicationContext().getSharedPreferences( MyPREFERENCES, MODE_PRIVATE);

		sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		editor = sharedpreferences.edit();

		text_employee_no.setText(userDetails.getString("employee_store", ""));
		text_vehicle_no.setText(userDetails.getString("vehicle_store", ""));
		text_route.setText(userDetails.getString("route_store", ""));

		str_emp = userDetails.getString("employee_store", "");
		str_veh = userDetails.getString("vehicle_store", "");
		str_rou = userDetails.getString("route_store", "");

		displayLocationSettingsRequest(getApplicationContext());

		btn_employee.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if(home_Start_Date.getText().toString().equalsIgnoreCase("Up-To-Date")) {
					new IntentIntegrator(HomeActivity.this).initiateScan(); // `this` is the current Activity
				}else{
					Toast.makeText(getApplicationContext(), "Please update the Sync records.", Toast.LENGTH_SHORT).show();
				}

				/*Intent i = new Intent();
				i.setClass(HomeActivity.this, MainScreen.class);
				startActivityForResult(i, 3);*/
			}
		});

		home_details.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				i.setClass(HomeActivity.this, CollectionDetails.class);
				startActivityForResult(i, 3);
			}
		});


		/*new Thread(new Runnable() {
			public void run() {
				try {
					GMailSender sender = new GMailSender(Settings.vadId, Settings.vadPassword); //Vadodara2003
					sender.sendMail("BioMedical Collection Report",
							"Dear Dr. Member, []\n\nThank you for being Eco-Friendly Member of Quantum Environment Engineers (QEE).",
							Settings.vadId,
							"bhavin2887@gmail.com");
				} catch (Exception e) {
					Log.e("SendMail", e.getMessage(), e);
				}
			}
		}).start();*/

		home_Start_Date.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Cursor curR, curD, curB, curS;
				try {
					garbageDataStorageObject.open();

					curR = garbageDataStorageObject.getDataByDataType(GarbageDataStorage.DATABASE_ROUTE_VISIT,GarbageDataStorage.ROUTEID);
					curD = garbageDataStorageObject.getDataByDataType(GarbageDataStorage.DATABASE_DAILY_VISIT,GarbageDataStorage.DAILY);
					curB = garbageDataStorageObject.getDataByDataType(GarbageDataStorage.DATABASE_BAGS_VISIT,GarbageDataStorage.BAGS);
					curS = garbageDataStorageObject.getDataByDataType(GarbageDataStorage.DATABASE_SUMMARY_VISIT,GarbageDataStorage.SUMMARY);

					Log.v("ava", "==== Count" + curR.getCount());
					if (isConnectingToInternet() && curR.getCount() >= 1) {
						if (curR.moveToFirst()) {
							String startData = curR.getString(curR.getColumnIndex(GarbageDataStorage.DailyData));
							new WebserviceCheck().execute(startData);
						}else{
							Toast.makeText(getApplicationContext(), "Internet not available.", Toast.LENGTH_SHORT).show();
						}
					} else if(isConnectingToInternet() && curD.getCount() >= 1) {
						if (curD.moveToFirst()) {
							//String startData = curD.getString(curD.getColumnIndex(GarbageDataStorage.DailyData));
							//new DailyHospitalVisitDetailsWSLoop().execute();
							new DailyHospitalVisitDetailsWSLoopNew().execute();
						}else{
							Toast.makeText(getApplicationContext(), "Internet not available.", Toast.LENGTH_SHORT).show();
						}
					} else if(isConnectingToInternet() && curB.getCount() >= 1 ) {
						if (curB.moveToFirst()) {
							//String startData = curB.getString(curB.getColumnIndex(GarbageDataStorage.DailyData));
							new BMWBagsCollectionDetailsWSLoop().execute();
						}else{
							Toast.makeText(getApplicationContext(), "Internet not available.", Toast.LENGTH_SHORT).show();
						}
					} else if(isConnectingToInternet() &&  curS.getCount() >= 1) {
						if (curS.moveToFirst()) {
							//String startData = curS.getString(curS.getColumnIndex(GarbageDataStorage.DailyData));
							//new BMWBagsCollectionSummaryWSLoop().execute();
							new BMWBagsCollectionSummaryWSLoopNew().execute();
						}else{
							Toast.makeText(getApplicationContext(), "Internet not available.", Toast.LENGTH_SHORT).show();
						}
					} else{
						Toast.makeText(getApplicationContext(), "Internet not available.", Toast.LENGTH_SHORT).show();
					}
					curR.close();
					curD.close();
					curB.close();
					curS.close();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					garbageDataStorageObject.close();
				}

			}
		});

		home_settings.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

                LayoutInflater inflater = LayoutInflater.from(HomeActivity.this);
                View subView = inflater.inflate(R.layout.dialog_password, null);
                final EditText subEditText = (EditText)subView.findViewById(R.id.dialogEditText);

                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setTitle("Authentication Required");
                builder.setMessage("Enter Password!!!");
                builder.setView(subView);
                AlertDialog alertDialog = builder.create();

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(subEditText.getText().toString().equalsIgnoreCase("bmw789")){
                            Intent i = new Intent(HomeActivity.this, Settings.class);
                            startActivity(i);
                        }else{
                            Toast.makeText(getApplicationContext(), "Enter Valid Password.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(HomeActivity.this, "Cancel", Toast.LENGTH_LONG).show();
                    }
                });
                builder.show();
			}
		});

		scan.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(text_employee_no.getText().length()>=1 && text_vehicle_no.getText().length()>=1 && text_route.getText().length()>=1 ){
					HomeActivity.this.finish();
					Intent i = new Intent();
					i.setClass(HomeActivity.this, MainScreen.class);
					startActivity(i);
				}else{
					Toast.makeText(getApplicationContext(), "Please fill All the Information", Toast.LENGTH_SHORT).show();
				}
			}
		});

		home_entry_exit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(text_employee_no.getText().length()>=1 && text_vehicle_no.getText().length()>=1 && text_route.getText().length()>=1 ){
					Intent ite= new Intent(HomeActivity.this, HospitalOne.class);
					startActivity(ite);
				}else{
					Toast.makeText(getApplicationContext(), "Please fill All the Information", Toast.LENGTH_SHORT).show();
				}
			}
		});

		updateButtonSync();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,
					Manifest.permission.CAMERA,Manifest.permission.ACCESS_FINE_LOCATION,
					Manifest.permission.ACCESS_NETWORK_STATE,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.READ_PHONE_STATE},1);
		}

        /*LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
			network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            showGPSDisabledAlertToUser();
        }*/
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
					Toast.makeText(HomeActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
				}
				return;
			}

			// other 'case' lines to check for other
			// permissions this app might request
		}
	}

	public void updateButtonSync(){
		Cursor curROut, curDOut, curBOut, curSOut;
		try {
			garbageDataStorageObject.open();
			valCount = 0;

			curROut = garbageDataStorageObject.getDataByDataType(GarbageDataStorage.DATABASE_ROUTE_VISIT,GarbageDataStorage.ROUTEID);
			curDOut = garbageDataStorageObject.getDataByDataType(GarbageDataStorage.DATABASE_DAILY_VISIT,GarbageDataStorage.DAILY);
			curBOut = garbageDataStorageObject.getDataByDataType(GarbageDataStorage.DATABASE_BAGS_VISIT,GarbageDataStorage.BAGS);
			curSOut = garbageDataStorageObject.getDataByDataType(GarbageDataStorage.DATABASE_SUMMARY_VISIT,GarbageDataStorage.SUMMARY);

			if (curROut.getCount() >= 1) {
				valCount +=1;
			}
			if(curDOut.getCount() >= 1) {
				valCount +=1;
			}
			if(curBOut.getCount() >= 1 ) {
				valCount +=1;
			}
			if(curSOut.getCount() >= 1) {
				valCount +=1;
			}


			final Handler handler = new Handler();
			handler.postDelayed(new Runnable(){
				public void run(){
					if(valCount>=1){
						home_Start_Date.setText("Sync-Data (" + valCount + ")");
					}else{
						home_Start_Date.setEnabled(false);
						home_Start_Date.setText("Up-To-Date");
					}
				}
			}, 1000);

			curROut.close();
			curDOut.close();
			curBOut.close();
			curSOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			garbageDataStorageObject.close();
		}
	}

	private class DailyHospitalVisitDetailsWSLoopNew extends AsyncTask<String, String, SoapObject> {

		SoapObject soap;
		private ProgressDialog dialog;
		Cursor cur;
		public DailyHospitalVisitDetailsWSLoopNew() {
		}

		@Override
		protected void onPreExecute() {
			// Things to be done before execution of long running operation. For
			// example showing ProgessDialog
			dialog = new ProgressDialog(HomeActivity.this);
			dialog.setMessage("Please wait...");
			dialog.show();
		}

		@Override
		protected SoapObject doInBackground(String... params) {
			publishProgress("Sleeping..."); // Calls onProgressUpdate()
			try {
				garbageDataStorageObject.open();
				cur = garbageDataStorageObject.getDataByDataType(GarbageDataStorage.DATABASE_DAILY_VISIT,GarbageDataStorage.DAILY);
				String replaceString = null;
				String newString = null;

				if(cur.getCount()>=1){
					cur.moveToFirst();
					newString = cur.getString(cur.getColumnIndex(GarbageDataStorage.DailyData));
					replaceString = newString.replace("ABC123",userDetails.getString("start_id", "0"));
					soap = WebService.webService_DailyHospitalVisitDetails.callWebservice(getApplicationContext(), replaceString);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return soap;
		}

		@Override
		protected void onPostExecute(SoapObject soapObject) {
			if(soapObject!=null && soapObject.hasProperty("DailyHospitalVisitDetailsResult")){
				garbageDataStorageObject.open();
				garbageDataStorageObject.UpdateStartID(GarbageDataStorage.DATABASE_DAILY_VISIT, GarbageDataStorage.DAILY);
				garbageDataStorageObject.UpdateOneID(GarbageDataStorage.DATABASE_DAILY_VISIT, GarbageDataStorage.DAILY);

				Cursor curBLoop = garbageDataStorageObject.getDataByDataType(GarbageDataStorage.DATABASE_DAILY_VISIT,GarbageDataStorage.DAILY);
				if(curBLoop.getCount()>=1){
					new DailyHospitalVisitDetailsWSLoopNew().execute();
				}else{
					updateButtonSync();
				}

			} else {
				Toast.makeText(getApplicationContext(),"Problem in DailyHospitalVisitDetailsResult",Toast.LENGTH_SHORT).show();
			}
			//cur.close();
			garbageDataStorageObject.close();
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
		}
	}

	/*private class DailyHospitalVisitDetailsWSLoop extends AsyncTask<String, String, SoapObject> {

		SoapObject soap;
		private ProgressDialog dialog;

		public DailyHospitalVisitDetailsWSLoop() {
		}

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(HomeActivity.this);
			dialog.setMessage("Please wait...");
			dialog.show();
		}

		@Override
		protected SoapObject doInBackground(String... params) {
			publishProgress("Sleeping..."); // Calls onProgressUpdate()
			try {

				garbageDataStorageObject.open();
				Cursor cur = garbageDataStorageObject.getDataByDataType(GarbageDataStorage.DATABASE_DAILY_VISIT,GarbageDataStorage.DAILY);
				StringBuffer sb = new StringBuffer();
				String replaceString = null, tempString = null;
				String newString = null;
				if(cur.getCount()==1 && cur.moveToFirst()){
					newString = cur.getString(cur.getColumnIndex(GarbageDataStorage.DailyData));
					replaceString = newString.replace("0",userDetails.getString("start_id", "0"));
				}else if (cur.getCount() >= 1 && cur.moveToFirst()) {
					do {
						sb.append(cur.getString(cur.getColumnIndex(GarbageDataStorage.DailyData)));
					} while (cur.moveToNext());

					newString = sb.toString();
					tempString = newString.replace("}][{","},{");
					replaceString = tempString.replace("0", userDetails.getString("start_id", "0"));
				}
				cur.close();
				soap = WebService.webService_DailyHospitalVisitDetails.callWebservice(getApplicationContext(), replaceString);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return soap;
		}

		@Override
		protected void onPostExecute(SoapObject soapObject) {
			//Log.v("","==myResult DailyHospitalVisitDetailsResultLoop "+soapObject);

			if(soapObject!=null && soapObject.hasProperty("DailyHospitalVisitDetailsResult")){

				garbageDataStorageObject.open();
				Cursor value_Ret = garbageDataStorageObject.UpdateStartID(GarbageDataStorage.DAILY);
				garbageDataStorageObject.close();
				Log.v("==Post "+value_Ret, "==onPostExecute SaveDailyA=");
				Toast.makeText(getApplicationContext(),"Success for DailyHospitalVisitDetailsResult",Toast.LENGTH_SHORT).show();
				updateButtonSync();
			}else{
				Toast.makeText(getApplicationContext(),"Problem in BMWBagsCollectionDetailsResult",Toast.LENGTH_SHORT).show();
			}
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
		}
	}*/

	private class BMWBagsCollectionDetailsWSLoop extends AsyncTask<String, String, SoapObject> {

		SoapObject soap;
		private ProgressDialog dialog;
		Cursor cur;
		public BMWBagsCollectionDetailsWSLoop() {
		}

		@Override
		protected void onPreExecute() {
			// Things to be done before execution of long running operation. For
			// example showing ProgessDialog
			dialog = new ProgressDialog(HomeActivity.this);
			dialog.setMessage("Please wait...");
			dialog.show();
		}

		@Override
		protected SoapObject doInBackground(String... params) {
			publishProgress("Sleeping..."); // Calls onProgressUpdate()
			try {
				garbageDataStorageObject.open();
				cur = garbageDataStorageObject.getDataByDataType(GarbageDataStorage.DATABASE_BAGS_VISIT,GarbageDataStorage.BAGS);
				String replaceString = null;
				String newString = null;
				/*if(cur.getCount()==1 && cur.moveToFirst()){
					newString = cur.getString(cur.getColumnIndex(GarbageDataStorage.DailyData));
					replaceString = newString.replace("0",userDetails.getString("start_id", "0"));
				}else if (cur.getCount() >= 1 && cur.moveToFirst()) {
					do {
						sb.append(cur.getString(cur.getColumnIndex(GarbageDataStorage.DailyData)));
						//Log.v("=== ava", "==== list" + sb);
					} while (cur.moveToNext());

					newString = sb.toString();
					//Log.v("=== ava", "==== 1list" + newString);
					tempString = newString.replace("}][{","},{");

					replaceString = tempString.replace("0", userDetails.getString("start_id", "0"));
					//Log.v("=== ava", "==== 2list" + replaceString);
				}*/
				if(cur.getCount()>=1){
					cur.moveToFirst();
					newString = cur.getString(cur.getColumnIndex(GarbageDataStorage.DailyData));
					replaceString = newString.replace("ABC123",userDetails.getString("start_id", "0"));
					soap = WebService.webService_BMWBagsCollectionDetails.callWebservice(getApplicationContext(), replaceString);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return soap;
		}

		@Override
		protected void onPostExecute(SoapObject soapObject) {
			if(soapObject!=null && soapObject.hasProperty("BMWBagsCollectionDetailsResult")){

				/*garbageDataStorageObject.open();
				Cursor value_Ret = garbageDataStorageObject.UpdateStartID(GarbageDataStorage.BAGS);
				garbageDataStorageObject.close();*/

				garbageDataStorageObject.open();
				garbageDataStorageObject.UpdateStartID(GarbageDataStorage.DATABASE_BAGS_VISIT, GarbageDataStorage.BAGS);
				garbageDataStorageObject.UpdateOneID(GarbageDataStorage.DATABASE_BAGS_VISIT, GarbageDataStorage.BAGS);
				//garbageDataStorageObject.close();


				//garbageDataStorageObject.open();
				Cursor curBLoop = garbageDataStorageObject.getDataByDataType(GarbageDataStorage.DATABASE_BAGS_VISIT,GarbageDataStorage.BAGS);
				if(curBLoop.getCount()>=1){
					new BMWBagsCollectionDetailsWSLoop().execute();
				}else{
					//Log.v("==Post Bags "+value_Ret, "==onPostExecute SaveDailyA=");
					Toast.makeText(getApplicationContext(),"Success for BMWBagsCollectionDetailsResult",Toast.LENGTH_SHORT).show();
					updateButtonSync();
				}

			} else {
				Toast.makeText(getApplicationContext(),"Problem in BMWBagsCollectionDetailsResult",Toast.LENGTH_SHORT).show();
			}
			//cur.close();
			garbageDataStorageObject.close();
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
		}
	}

	private class BMWBagsCollectionSummaryWSLoopNew extends AsyncTask<String, String, SoapObject> {

		SoapObject soap;
		private ProgressDialog dialog;
		Cursor cur;
		public BMWBagsCollectionSummaryWSLoopNew() {
		}

		@Override
		protected void onPreExecute() {
			// Things to be done before execution of long running operation. For
			// example showing ProgessDialog
			dialog = new ProgressDialog(HomeActivity.this);
			dialog.setMessage("Please wait...");
			dialog.show();
		}

		@Override
		protected SoapObject doInBackground(String... params) {
			publishProgress("Sleeping..."); // Calls onProgressUpdate()
			try {
				garbageDataStorageObject.open();
				cur = garbageDataStorageObject.getDataByDataType(GarbageDataStorage.DATABASE_SUMMARY_VISIT,GarbageDataStorage.SUMMARY);
				String replaceString = null;
				String newString = null;

				if(cur.getCount()>=1){
					cur.moveToFirst();
					newString = cur.getString(cur.getColumnIndex(GarbageDataStorage.DailyData));
					replaceString = newString.replace("ABC123",userDetails.getString("start_id", "0"));
					soap = WebService.webService_BMWBagsCollectionSummary.callWebservice(getApplicationContext(), replaceString);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return soap;
		}

		@Override
		protected void onPostExecute(SoapObject soapObject) {
			if(soapObject!=null && soapObject.hasProperty("BMWBagsCollectionSummaryResult")){

				garbageDataStorageObject.open();
				garbageDataStorageObject.UpdateStartID(GarbageDataStorage.DATABASE_SUMMARY_VISIT, GarbageDataStorage.SUMMARY);
				garbageDataStorageObject.UpdateOneID(GarbageDataStorage.DATABASE_SUMMARY_VISIT, GarbageDataStorage.SUMMARY);

				Cursor curBLoop = garbageDataStorageObject.getDataByDataType(GarbageDataStorage.DATABASE_SUMMARY_VISIT,GarbageDataStorage.SUMMARY);
				if(curBLoop.getCount()>=1){
					new BMWBagsCollectionSummaryWSLoopNew().execute();
				}else{
					//Toast.makeText(getApplicationContext(),"Success for BMWBagsCollectionDetailsResult"+ value_Ret,Toast.LENGTH_SHORT).show();
					updateButtonSync();
				}

			} else {
				Toast.makeText(getApplicationContext(),"Problem in BMWBagsCollectionDetailsResult",Toast.LENGTH_SHORT).show();
			}
			//cur.close();
			garbageDataStorageObject.close();
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
		}
	}

	/*private class BMWBagsCollectionSummaryWSLoop extends AsyncTask<String, String, SoapObject> {

		SoapObject soap;
		private ProgressDialog dialog;

		public BMWBagsCollectionSummaryWSLoop() {
		}

		@Override
		protected void onPreExecute() {
			// Things to be done before execution of long running operation. For
			// example showing ProgessDialog
			dialog = new ProgressDialog(HomeActivity.this);
			dialog.setMessage("Please wait...");
			dialog.show();
		}

		@Override
		protected SoapObject doInBackground(String... params) {
			publishProgress("Sleeping..."); // Calls onProgressUpdate()
			try {
				garbageDataStorageObject.open();
				Cursor curSum = garbageDataStorageObject.getDataByDataType(GarbageDataStorage.DATABASE_DAILY_VISIT,GarbageDataStorage.SUMMARY);
				//Log.v("=== ava", "==== BMWBagsCollectionSummaryWSLoop");

				StringBuffer sb = new StringBuffer();
				String replaceString = null, tempString = null;
				String newString = null;
				if(curSum.getCount()==1 && curSum.moveToFirst()){
					newString = curSum.getString(curSum.getColumnIndex(GarbageDataStorage.DailyData));
					replaceString = newString.replace("0",userDetails.getString("start_id", "0"));
				}else if (curSum.getCount() >= 1 && curSum.moveToFirst()) {
					do {
						sb.append(curSum.getString(curSum.getColumnIndex(GarbageDataStorage.DailyData)));
						//Log.v("=== ava", "==== list" + sb);
					} while (curSum.moveToNext());

					newString = sb.toString();
					//Log.v("=== ava", "==== 1list" + newString);
					tempString = newString.replace("}][{","},{");

					replaceString = tempString.replace("0", userDetails.getString("start_id", "0"));
					//Log.v("=== ava", "==== 2list" + replaceString);
				}
				curSum.close();
				soap = WebService.webService_BMWBagsCollectionSummary.callWebservice(getApplicationContext(), replaceString);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return soap;
		}

		@Override
		protected void onPostExecute(SoapObject soapObject) {
			Log.v("","==myResult"+soapObject);
			try {
				if (dialog.isShowing()) {
					dialog.dismiss();
				}
				if (soapObject!=null && soapObject.hasProperty("BMWBagsCollectionSummaryResult")) {

					garbageDataStorageObject.open();
					Cursor value_Ret = garbageDataStorageObject.UpdateStartID(GarbageDataStorage.SUMMARY);
					garbageDataStorageObject.close();

					Log.v("==Post Bags "+value_Ret, "==onPostExecute SaveDailyA=");
					Toast.makeText(getApplicationContext(),"Success in BMWBagsCollectionSummaryResult",Toast.LENGTH_SHORT).show();
					updateButtonSync();
				} else {
					Toast.makeText(getApplicationContext(),"Problem in BMWBagsCollectionSummaryResult",Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {

			} finally {
			}
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
		}
	}*/

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		// Save the user's current game state
		//savedInstanceState.putParcelable("camaraData", camaraData);
		// Always call the superclass so it can save the view hierarchy state
		super.onSaveInstanceState(savedInstanceState);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

		IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

		if(result != null) {

			if(result.getContents() == null) {
//cancel
			} else {
				if (result.getContents().contains("RoleName")) {

					String qrContent = result.getContents();
					String superVID = qrContent.substring(qrContent.indexOf("UserID:")+7,qrContent.indexOf("|UserName"));

					sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
					final SharedPreferences.Editor editor = sharedpreferences.edit();
					editor.putString("supervisorID", superVID);
					editor.commit();

					LayoutInflater li = LayoutInflater.from(HomeActivity.this);
					View promptsView = li.inflate(R.layout.prompt_dialog, null);
					garbageDataStorageObject.open();

					alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);
					alertDialogBuilder.setView(promptsView);

					final Spinner spin1 = (Spinner) promptsView.findViewById(R.id.spinner1);
					final Spinner spin2 = (Spinner) promptsView.findViewById(R.id.spinner2);
					final Spinner spin3 = (Spinner) promptsView.findViewById(R.id.spinner3);

					Cursor cursor = garbageDataStorageObject.getTableData(GarbageDataStorage.EMPLOYEE);
					List<String> list1 = new ArrayList<String>();
					if (cursor.moveToFirst()) {
						do {
							list1.add(cursor.getString(cursor.getColumnIndex("employee_name")));
						} while (cursor.moveToNext());
					}

					ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list1);
					dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spin1.setAdapter(dataAdapter);
					spin1.setOnItemSelectedListener(new CustomOnItemSelectedListener1());
					spin1.setSelection(userDetails.getInt("employee_store_p", 0));

					Cursor cursor2 = garbageDataStorageObject.getTableData(GarbageDataStorage.VEHICLE);
					List<String> list2 = new ArrayList<String>();
					if (cursor2.moveToFirst()) {
						do {
							list2.add(cursor2.getString(cursor2.getColumnIndex("vehicle_regno")));
						} while (cursor2.moveToNext());
					}

					ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list2);
					dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spin2.setAdapter(dataAdapter2);
					spin2.setOnItemSelectedListener(new CustomOnItemSelectedListener2());
					spin2.setSelection(userDetails.getInt("vehicle_store_p", 0));


					Cursor cursor3 = garbageDataStorageObject.getTableData(GarbageDataStorage.ROUTE);
					List<String> list3 = new ArrayList<String>();
					if (cursor3.moveToFirst()) {
						do {
							list3.add(cursor3.getString(cursor3.getColumnIndex("route_name")));
						} while (cursor3.moveToNext());
					}

					ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list3);
					dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spin3.setAdapter(dataAdapter3);
					spin3.setOnItemSelectedListener(new CustomOnItemSelectedListener3());
					spin3.setSelection(userDetails.getInt("route_store_p", 0));

					// set dialog message
					if (text_employee_no.getText().length() >= 1) {
						spin1.setEnabled(false);
						spin2.setEnabled(false);
						spin3.setEnabled(false);
					}
					if (text_employee_no.getText().length() >= 1) {
						alertDialogBuilder
								.setCancelable(true)
								.setNegativeButton("End Day",
										new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog, int id) {
												dialog.dismiss();

												if (isConnectingToInternet())
													new SaveDailyAssignmentEnd().execute();
												else {
													sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
													SharedPreferences.Editor editor = sharedpreferences.edit();
													editor.putString("employee_store", "");
													editor.putString("vehicle_store", "");
													editor.putString("route_store", "");
													editor.putString("employee_storeID", "");
													editor.putString("vehicle_storeID", "");
													editor.putString("route_storeID", "");
													editor.putInt("employee_store_p", 0);
													editor.putInt("vehicle_store_p", 0);
													editor.putInt("route_store_p", 0);
													editor.commit();
													text_employee_no.setText("");
													text_vehicle_no.setText("");
													text_route.setText("");
												}
											}
										});
					} else {
						alertDialogBuilder
								.setCancelable(true)
								.setPositiveButton("Start Day",
										new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog, int id) {
												dialog.dismiss();

												final JSONArray jObjectOptionData1 = new JSONArray();
												String abl;
												try {
													SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault());
													String currentDateandTime = sdf.format(new Date());

													JSONObject data = new JSONObject();
													data.put("OperationName", "DailyVehicleRouteAssignment");
													data.put("CompanyID", userDetails.getString("companyID", "1"));
													data.put("BranchID", userDetails.getString("branchID", "1"));
													data.put("VehicleID", str_vehID);
													data.put("HandsetID", userDetails.getString("HandsetID", "1"));
													data.put("DriverID", str_empID);
													data.put("RouteID", str_rouID);
													data.put("StartDateTime", currentDateandTime);
													data.put("StartDayBy", userDetails.getString("supervisorID", "1"));
													data.put("Latitude", GlobalVariables.getLat());//userDetails.getString("Latitude", "22.280347"));//"22.280347");
													data.put("Longitude", GlobalVariables.getLon());//userDetails.getString("Longitude", "73.167942"));// "73.167942");

													jObjectOptionData1.put(data);

													abl = jObjectOptionData1.toString();
													Log.v("Data", "Data==" + abl);

													if (isConnectingToInternet())
														new WebserviceCheck().execute(abl);
													else {
														garbageDataStorageObject.open();
														ContentValues cv = new ContentValues();
														//cv.put(GarbageDataStorage.DailyDataID, "1");
														cv.put(GarbageDataStorage.DailyData, abl);
														cv.put(GarbageDataStorage.DATATYPE, GarbageDataStorage.ROUTEID);
														cv.put(GarbageDataStorage.IS_ON_SERVER, "False");
														garbageDataStorageObject.insert(GarbageDataStorage.DATABASE_ROUTE_VISIT, cv);
														garbageDataStorageObject.close();

														text_employee_no.setText(str_emp);
														text_vehicle_no.setText(str_veh);
														text_route.setText(str_rou);

														sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
														SharedPreferences.Editor editor = sharedpreferences.edit();

														editor.putString("start_id", "ABC123");
														editor.commit();
													}

												} catch (Exception e) {

												}
											}
										});
					}
					alertDialogBuilder.show();
					//AlertDialog alertDialog = alertDialogBuilder.create();
					//alertDialog.show();
					garbageDataStorageObject.close();
	//Scanned successfully
				}

			}

		} else {

		    if((requestCode == 214) && (resultCode == -1)){
                Toast.makeText(HomeActivity.this, "Success" ,Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(HomeActivity.this, "Failure" ,Toast.LENGTH_SHORT).show();
                finish();
            }
			super.onActivityResult(requestCode, resultCode, data);

		}

		if (requestCode == 3 && data != null) {
			sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
			final SharedPreferences.Editor editor = sharedpreferences.edit();
			editor.putString("supervisorID", data.getStringExtra("fromCamera"));
			editor.commit();

			LayoutInflater li = LayoutInflater.from(HomeActivity.this);
			View promptsView = li.inflate(R.layout.prompt_dialog, null);
			garbageDataStorageObject.open();

            alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);
			alertDialogBuilder.setView(promptsView);

			final Spinner spin1 = (Spinner) promptsView.findViewById(R.id.spinner1);
			final Spinner spin2 = (Spinner) promptsView.findViewById(R.id.spinner2);
			final Spinner spin3 = (Spinner) promptsView.findViewById(R.id.spinner3);

			Cursor cursor = garbageDataStorageObject.getTableData(GarbageDataStorage.EMPLOYEE);
			List<String> list1 = new ArrayList<String>();
			if (cursor.moveToFirst()) {
				do {
					list1.add(cursor.getString(cursor.getColumnIndex("employee_name")));
				} while (cursor.moveToNext());
			}

			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list1);
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spin1.setAdapter(dataAdapter);
			spin1.setOnItemSelectedListener(new CustomOnItemSelectedListener1());
			spin1.setSelection(userDetails.getInt("employee_store_p", 0));

			Cursor cursor2 = garbageDataStorageObject.getTableData(GarbageDataStorage.VEHICLE);
			List<String> list2 = new ArrayList<String>();
			if (cursor2.moveToFirst()) {
				do {
					list2.add(cursor2.getString(cursor2.getColumnIndex("vehicle_regno")));
				} while (cursor2.moveToNext());
			}

			ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list2);
			dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spin2.setAdapter(dataAdapter2);
			spin2.setOnItemSelectedListener(new CustomOnItemSelectedListener2());
			spin2.setSelection(userDetails.getInt("vehicle_store_p", 0));

			Cursor cursor3 = garbageDataStorageObject.getTableData(GarbageDataStorage.ROUTE);
			List<String> list3 = new ArrayList<String>();
			if (cursor3.moveToFirst()) {
				do {
					list3.add(cursor3.getString(cursor3.getColumnIndex("route_name")));
				} while (cursor3.moveToNext());
			}

			ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list3);
			dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spin3.setAdapter(dataAdapter3);
			spin3.setOnItemSelectedListener(new CustomOnItemSelectedListener3());
			spin3.setSelection(userDetails.getInt("route_store_p", 0));

			// set dialog message
			if(text_employee_no.getText().length()>=1){
				spin1.setEnabled(false);
				spin2.setEnabled(false);
				spin3.setEnabled(false);
			}
			if(text_employee_no.getText().length()>=1){
				alertDialogBuilder
						.setCancelable(true)
						.setNegativeButton("End Day",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int id) {
										dialog.dismiss();

										if(isConnectingToInternet())
											new SaveDailyAssignmentEnd().execute();
										else{
											sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
											SharedPreferences.Editor editor = sharedpreferences.edit();
											editor.putString("employee_store", "");
											editor.putString("vehicle_store", "");
											editor.putString("route_store","");
											editor.putString("employee_storeID", "");
											editor.putString("vehicle_storeID", "");
											editor.putString("route_storeID","");
											editor.putInt("employee_store_p", 0);
											editor.putInt("vehicle_store_p", 0);
											editor.putInt("route_store_p",0);
											editor.commit();
											text_employee_no.setText("");
											text_vehicle_no.setText("");
											text_route.setText("");
										}
									}
								});
			}else{
				alertDialogBuilder
						.setCancelable(true)
						.setPositiveButton("Start Day",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int id) {
										dialog.dismiss();

										final JSONArray jObjectOptionData1 = new JSONArray();
										String abl;
										try{
											SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault());
											String currentDateandTime = sdf.format(new Date());

											JSONObject data = new JSONObject();
											data.put("OperationName", "DailyVehicleRouteAssignment");
											data.put("CompanyID", userDetails.getString("companyID", "1"));
											data.put("BranchID", userDetails.getString("branchID", "1"));
											data.put("VehicleID", str_vehID);
											data.put("HandsetID", userDetails.getString("HandsetID", "1"));
											data.put("DriverID", str_empID);
											data.put("RouteID", str_rouID);
											data.put("StartDateTime", currentDateandTime);
											data.put("StartDayBy", userDetails.getString("supervisorID", "1"));
											data.put("Latitude", GlobalVariables.getLat());//userDetails.getString("Latitude", "22.280347"));//"22.280347");
											data.put("Longitude", GlobalVariables.getLon());//userDetails.getString("Longitude", "73.167942"));// "73.167942");
											jObjectOptionData1.put(data);

											abl = jObjectOptionData1.toString();
											Log.v("Data", "Data=="+abl);

											if(isConnectingToInternet())
												new WebserviceCheck().execute(abl);
											else{
												garbageDataStorageObject.open();
												ContentValues cv = new ContentValues();
												//cv.put(GarbageDataStorage.DailyDataID, "1");
												cv.put(GarbageDataStorage.DailyData, abl);
												cv.put(GarbageDataStorage.DATATYPE, GarbageDataStorage.ROUTEID);
												cv.put(GarbageDataStorage.IS_ON_SERVER, "False");
												garbageDataStorageObject.insert(GarbageDataStorage.DATABASE_ROUTE_VISIT, cv);
												garbageDataStorageObject.close();

												text_employee_no.setText(str_emp);
												text_vehicle_no.setText(str_veh);
												text_route.setText(str_rou);

												sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
												SharedPreferences.Editor editor = sharedpreferences.edit();

												editor.putString("start_id", "ABC123");
												editor.commit();
											}

										}catch(Exception e){

										}
									}
								});
			}
            alertDialogBuilder.show();
			//AlertDialog alertDialog = alertDialogBuilder.create();
			//alertDialog.show();
			garbageDataStorageObject.close();

		}else{
			//Toast.makeText(HomeActivity.this, "Please try again." ,Toast.LENGTH_SHORT).show();
		}
	}

	private void showGPSDisabledAlertToUser(){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setMessage("GPS બંધ છે.")
				.setCancelable(false)
				.setPositiveButton("Settings માં જવું.",
						new DialogInterface.OnClickListener(){
							public void onClick(DialogInterface dialog, int id){
								Intent callGPSSettingIntent = new Intent(
										android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
								startActivity(callGPSSettingIntent);
							}
						});
		alertDialogBuilder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int id){
						finish();
						dialog.cancel();
					}
				});
		AlertDialog alert = alertDialogBuilder.create();
		alert.show();
	}

	private class WebserviceCheck extends AsyncTask<String, String, String> {

		private String resp;
		String soap;
		private ProgressDialog dialog;
		String param;
		@Override
		protected String doInBackground(String... params) {
			publishProgress("Please Wait..."); // Calls onProgressUpdate()
			try {
				param = params[0];
				soap = WebService.webService_HelloWorld.callWebserviceCheck(getApplicationContext());
			} catch (Exception e) {
				e.printStackTrace();
				resp = e.getMessage();
			}
			return soap;
		}

		@Override
		protected void onPostExecute(String soapObject) {
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
			if(soapObject.equalsIgnoreCase("Hello World"))
			{
				new SaveDailyAssignment().execute(param);
			}else{
				//TODO Webservice not working
				garbageDataStorageObject.open();
				ContentValues cv = new ContentValues();
				cv.put(GarbageDataStorage.DailyData, param);
				cv.put(GarbageDataStorage.DATATYPE, GarbageDataStorage.DAILY);
				cv.put(GarbageDataStorage.IS_ON_SERVER, "False");
				garbageDataStorageObject.insert(GarbageDataStorage.DATABASE_DAILY_VISIT, cv);
				garbageDataStorageObject.close();

				text_employee_no.setText(str_emp);
				text_vehicle_no.setText(str_veh);
				text_route.setText(str_rou);

				/*sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = sharedpreferences.edit();

				editor.putString("employee_store", str_emp);
				editor.putInt("employee_store_p", str_emp_pos);

				editor.putString("vehicle_store", str_veh);
				editor.putInt("vehicle_store_p", str_veh_pos);

				editor.putString("route_store",str_rou);
				editor.putInt("route_store_p",str_rou_pos);
				editor.commit();*/
			}
		}

		@Override
		protected void onPreExecute() {
			// Things to be done before execution of long running operation. For
			// example showing ProgessDialog
			dialog = new ProgressDialog(HomeActivity.this);
			dialog.setMessage("Please wait...");
			dialog.show();
		}
	}

	public boolean isConnectingToInternet(){
		ConnectivityManager connectivity = (ConnectivityManager) HomeActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
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

	private class SaveDailyAssignment extends AsyncTask<String, String, SoapObject> {

		SoapObject soap;
		private ProgressDialog dialog;

		public SaveDailyAssignment() {
		}

		@Override
		protected void onPreExecute() {
			// Things to be done before execution of long running operation. For
			// example showing ProgessDialog
			dialog = new ProgressDialog(HomeActivity.this);
			dialog.setMessage("Please wait...");
			dialog.show();
		}

		@Override
		protected SoapObject doInBackground(String... params) {
			publishProgress("Sleeping..."); // Calls onProgressUpdate()
			try {
				soap = WebService.webService_DailyVehicleRouteAssignment.callWebservice(getApplicationContext(), params[0]);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return soap;
		}

		@Override
		protected void onPostExecute(SoapObject soapObject) {

			if(soapObject!= null && soapObject.hasProperty("DailyVehicleRouteAssignmentResult")){
				text_employee_no.setText(str_emp);
				text_vehicle_no.setText(str_veh);
				text_route.setText(str_rou);

				sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = sharedpreferences.edit();

				editor.putString("start_id", soapObject.getProperty("DailyVehicleRouteAssignmentResult").toString());

				/*editor.putString("employee_store", str_emp);
				editor.putInt("employee_store_p", str_emp_pos);

				editor.putString("vehicle_store", str_veh);
				editor.putInt("vehicle_store_p", str_veh_pos);

				editor.putString("route_store",str_rou);
				editor.putInt("route_store_p",str_rou_pos);*/
				editor.commit();

				Log.v("=====","===== SaveDailyAssignment myResult"+soapObject);

				garbageDataStorageObject.open();
				garbageDataStorageObject.UpdateStartID(GarbageDataStorage.DATABASE_ROUTE_VISIT, GarbageDataStorage.ROUTEID);
				//garbageDataStorageObject.UpdateStartID(GarbageDataStorage.ROUTEID);
				garbageDataStorageObject.close();

				//Log.v("==SaveDailyA"+value_Ret, "==onPostExecute SaveDailyA=");
				updateButtonSync();
			}else{
				Toast.makeText(HomeActivity.this, "Problem with starting date" ,Toast.LENGTH_SHORT).show();
			}
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
		}

	}

	private class SaveDailyAssignmentEnd extends AsyncTask<String, String, SoapObject> {

		SoapObject soap;
		private ProgressDialog dialog;

		public SaveDailyAssignmentEnd() { }

		@Override
		protected void onPreExecute() {
			// Things to be done before execution of long running operation. For
			// example showing ProgessDialog
			dialog = new ProgressDialog(HomeActivity.this);
			dialog.setMessage("Please wait...");
			dialog.show();
		}

		@Override
		protected SoapObject doInBackground(String... params) {
			publishProgress("Sleeping..."); // Calls onProgressUpdate()
			try {

				final JSONArray jObjectOptionData1 = new JSONArray();
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault());
				String currentDateandTime = sdf.format(new Date());

				JSONObject data = new JSONObject();
				String abl;
				data.put("OperationName", "EndDayNotificaionEndRoute");
				data.put("DailyVehicleRouteAssignmentID", userDetails.getString("start_id", "ABC123"));
				data.put("EndDateTime", currentDateandTime);
				data.put("IsEndDay", true);
				data.put("EndDayBy", userDetails.getString("supervisorID", "1"));
				data.put("Latitude",userDetails.getString("Latitude", GlobalVariables.getLat()));//"22.280347");
				data.put("Longitude",userDetails.getString("Longitude", GlobalVariables.getLon()));// "73.167942");


				data.put("Latitude", GlobalVariables.getLat());//userDetails.getString("Latitude", "22.280347"));//"22.280347");
				data.put("Longitude", GlobalVariables.getLon());//userDetails.getString("Longitude", "73.167942"));// "73.167942");

				jObjectOptionData1.put(data);
				abl = jObjectOptionData1.toString();
				Log.v("Data", "Data=="+abl);

				soap = WebService.webService_EndDayNotificaionEndRoute.callWebservice(getApplicationContext(), abl);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return soap;
		}

		@Override
		protected void onPostExecute(SoapObject soapObject) {
			Log.v("==","==EndDayNotificaionEndRouteResult "+soapObject);//DailyHospitalVisitDetailsResponse{DailyHospitalVisitDetailsResult=42; }

			if(soapObject!=null && soapObject.hasProperty("EndDayNotificaionEndRouteResult")){
				sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = sharedpreferences.edit();
				editor.putString("employee_store", "");
                editor.putString("vehicle_store", "");
                editor.putString("route_store","");
                editor.putString("employee_storeID", "");
                editor.putString("vehicle_storeID", "");
                editor.putString("route_storeID","");
				editor.putInt("employee_store_p", 0);
				editor.putInt("vehicle_store_p", 0);
				editor.putInt("route_store_p",0);
				editor.commit();

				text_employee_no.setText("");
				text_vehicle_no.setText("");
				text_route.setText("");

				updateButtonSync();
			}else{
				Toast.makeText(getApplicationContext(),"Problem in EndDayNotificaionEndRouteResult",Toast.LENGTH_SHORT).show();
			}
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
		}
	}

	public class CustomOnItemSelectedListener1 implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
			str_emp = parent.getItemAtPosition(pos).toString();
			str_emp_pos = pos;
			try {
				Cursor curE;
				garbageDataStorageObject.open();
				curE = garbageDataStorageObject.getEmployeeData(str_emp);
				if (curE.moveToFirst()) {
					str_empID = curE.getString(curE.getColumnIndex("employee_id"));
					editor.putString("employee_store", str_emp);
					editor.putString("employee_storeID", str_empID);
					editor.putInt("employee_store_p", str_emp_pos);

				}
				editor.commit();
				curE.close();

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				garbageDataStorageObject.close();
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
		}

	}

	public class CustomOnItemSelectedListener2 implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
			str_veh = parent.getItemAtPosition(pos).toString();
			str_veh_pos = pos;

			try {
				Cursor  curV;
				garbageDataStorageObject.open();
				curV = garbageDataStorageObject.getVehicleData(str_veh);
				if (curV.moveToFirst()) {
					str_vehID = curV.getString(curV.getColumnIndex("vehicle_id"));
					editor.putString("vehicle_store", str_veh);
					editor.putString("vehicle_storeID", str_vehID);
					editor.putInt("vehicle_store_p", str_veh_pos);
				}
				editor.commit();
				curV.close();

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				garbageDataStorageObject.close();
			}

		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
		}

	}

	public class CustomOnItemSelectedListener3 implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
			str_rou = parent.getItemAtPosition(pos).toString();
			str_rou_pos = pos;

			try {
				Cursor curR;
				garbageDataStorageObject.open();
				curR = garbageDataStorageObject.getRouteData(str_rou);
				if (curR.moveToFirst()) {
					str_rouID = curR.getString(curR.getColumnIndex("route_id"));
					editor.putString("route_store",str_rou);
					editor.putString("route_storeID", str_rouID);
					editor.putInt("route_store_p",str_rou_pos);
				}
				editor.commit();
				curR.close();

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				garbageDataStorageObject.close();
			}


		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
		}

	}

	private void displayLocationSettingsRequest(Context context) {
		GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
				.addApi(LocationServices.API).build();
		googleApiClient.connect();

		LocationRequest locationRequest = LocationRequest.create();
		locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		locationRequest.setInterval(10000);
		locationRequest.setFastestInterval(10000 / 2);

		LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
		builder.setAlwaysShow(true);

		PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
		result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
			@Override
			public void onResult(LocationSettingsResult result) {
				final Status status = result.getStatus();
				switch (status.getStatusCode()) {
					case LocationSettingsStatusCodes.SUCCESS:
						Log.i(TAG, "All location settings are satisfied.");
						break;
					case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
						Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

						try {
							// Show the dialog by calling startResolutionForResult(), and check the result
							// in onActivityResult().
							status.startResolutionForResult(HomeActivity.this, 214);
						} catch (IntentSender.SendIntentException e) {
							Log.i(TAG, "PendingIntent unable to execute request.");
						}
						break;
					case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
						Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
						break;
				}
			}
		});
	}
}