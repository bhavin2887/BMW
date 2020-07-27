package com.example.garbagecollection.bmw;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

public class CollectionDetails extends Activity{
	
    SharedPreferences userDetails;
	public static final String MyPREFERENCES = "MyPrefs" ;
	SharedPreferences sharedpreferences;
	private GarbageDataStorage garbageDataStorageObject = new GarbageDataStorage(this);
	SharedPreferences.Editor editor;
	ListView lv;
	ArrayList<WasteScanningAtHospitals> wasteCount = new ArrayList<WasteScanningAtHospitals>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_collection);
		
		lv = (ListView) findViewById(R.id.list_collection);

		userDetails = getApplicationContext().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
		sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		editor = sharedpreferences.edit();

		if(isConnectingToInternet()){
			new WebserviceSetting().execute();
		}else{
			Toast.makeText(getApplicationContext(), "Please check your Internet Connection.", Toast.LENGTH_SHORT).show();
		}

	}


	private class WebserviceSetting extends AsyncTask<String, String, SoapObject> {

		private String resp;
		SoapObject soap;
		private ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(CollectionDetails.this);
			garbageDataStorageObject.open();
			dialog.setMessage("Please wait...");
			dialog.show();
		}

		@Override
		protected SoapObject doInBackground(String... params) {
			publishProgress("Sleeping..."); // Calls onProgressUpdate()
			try {
				soap = WebService.webService_Setting.callWebservice(userDetails.getString("companyID","1"),userDetails.getString("branchID","1"),  userDetails.getString("route_storeID","1"));
			} catch (Exception e) {
				e.printStackTrace();
				resp = e.getMessage();
			}
			return soap;
		}

		@Override
		protected void onPostExecute(SoapObject soap) {
			if(soap!= null){
				garbageDataStorageObject.deleteAllData(GarbageDataStorage.EMPLOYEE);
				garbageDataStorageObject.deleteAllData(GarbageDataStorage.ROUTE);
				garbageDataStorageObject.deleteAllData(GarbageDataStorage.VEHICLE);
				SoapObject myResult =  (SoapObject)soap.getProperty("GetAllActiveDataResult");
				Log.i("==","myResult"+myResult);

				if(myResult.getProperty("isSuccess").toString().equals("true")){

					SoapObject lstHandset = (SoapObject) myResult.getProperty("RouteWiseHospitalWasteCollection");

					for (int j = 0; j< lstHandset.getPropertyCount(); j++) {
						WasteScanningAtHospitals waScan = new WasteScanningAtHospitals();

						waScan.setWasteScanningAtHospitalDetailID(lstHandset.getProperty("WasteScanningAtHospitalDetailID").toString());//long
						waScan.setDoctorName(lstHandset.getProperty("DoctorName").toString());
						waScan.setMembershipNo(lstHandset.getProperty("MembershipNo").toString());
						waScan.setHospitalName(lstHandset.getProperty("HospitalName").toString());
						waScan.setAreaName(lstHandset.getProperty("AreaName").toString());
						waScan.setBagQRCodeDetalID(lstHandset.getProperty("BagQRCodeDetalID").toString());//int
						waScan.setQRCodeContent(lstHandset.getProperty("QRCodeContent").toString());
						waScan.setNoofBags(lstHandset.getProperty("NoOfBags").toString());//int
						waScan.setWeight(lstHandset.getProperty("Weight").toString());
						waScan.setIsCollected(lstHandset.getProperty("IsCollected").toString());//bool
						waScan.setScannedBy(lstHandset.getProperty("ScannedBy").toString());
						waScan.setScannedDateTime(lstHandset.getProperty("ScannedDateTime").toString());

					}
					editor.commit();
				}
			}

			if (dialog.isShowing()) {
				dialog.dismiss();
			}
			garbageDataStorageObject.close();
			finish();
		}
	}

	public boolean isConnectingToInternet(){
		ConnectivityManager connectivity = (ConnectivityManager) CollectionDetails.this.getSystemService(Context.CONNECTIVITY_SERVICE);
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
