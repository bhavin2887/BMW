package com.example.garbagecollection.bmw;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class Settings  extends Activity{
	
	Button btn,btn_server;
    SharedPreferences userDetails;
	public static final String MyPREFERENCES = "MyPrefs" ;
	SharedPreferences sharedpreferences;
	private GarbageDataStorage garbageDataStorageObject = new GarbageDataStorage(this);
	TextView txt_emp,txt_veh,txt_route,txt_detail,txt_imei;
	Cursor cursor1, cursor2, cursor3;
	ListView listView ;
	//public static String vadId = "NoReply@quantumenvironment.in" , vadPassword = "Corner@308";

	public static String vadId = "qeevadodara@gmail.com" , vadPassword = "Bmw@1977";

    //public static String vadId = "service.samvedna@gmail.com", vadPassword = "1511@2171";

	SharedPreferences.Editor editor;
	String android_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		
		//getDatabase();
		btn = (Button)findViewById(R.id.btn_sync);
		btn_server = (Button)findViewById(R.id.btn_server);
		txt_emp = (TextView)findViewById(R.id.txt_emp);
		txt_veh = (TextView)findViewById(R.id.txt_veh);
		txt_route = (TextView)findViewById(R.id.txt_route);
		txt_detail = (TextView)findViewById(R.id.txt_detail);
		txt_imei = (TextView)findViewById(R.id.txt_imei);

		userDetails = getApplicationContext().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
		sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		editor = sharedpreferences.edit();

		TelephonyManager mngr = (TelephonyManager) getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
		android_id = mngr.getDeviceId();//"22222222222";//"867168031746172";//
		//4444444444


		editor.putString("appCode", GlobalVariables.getHostURL());
		//editor.commit();

		editor.putString("device_id", android_id);

		editor.commit();

		txt_imei.setText(""+android_id);
		new WebserviceStarted().execute();

		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
	        	if(isConnectingToInternet()){
					new WebServiceSetServer().execute();
	        	}else{
	        		Toast.makeText(getApplicationContext(), "Please check your Internet Connection.", Toast.LENGTH_SHORT).show();
	        	}
			}
		});

		btn.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {

				garbageDataStorageObject.open();
				garbageDataStorageObject.deleteAllData(GarbageDataStorage.EMPLOYEE);
				garbageDataStorageObject.deleteAllData(GarbageDataStorage.VEHICLE);
				garbageDataStorageObject.deleteAllData(GarbageDataStorage.ROUTE);
				garbageDataStorageObject.deleteAllData(GarbageDataStorage.HANDSET);
				garbageDataStorageObject.deleteAllData(GarbageDataStorage.DATABASE_DAILY_VISIT);
				garbageDataStorageObject.close();
				
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

				editor.putString("start_id", "");
				editor.putString("supervisorID","");

				editor.putString("companyID", "");
				editor.putString("branchID", "");
				editor.putString("city", "");
				editor.putString("appCode", "");

				editor.putString("Latitude", "");
				editor.putString("Longitude", "");

				editor.commit();
				finish();
				System.exit(0);
			    //System.exit(0);
			    
				Toast.makeText(getApplicationContext(), "Data got Empty.", Toast.LENGTH_SHORT).show();
				return false;
			}
		});

		btn_server.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isConnectingToInternet()){
					new WebServiceGetServer().execute();
				}else{
					Toast.makeText(getApplicationContext(), "Please check your Internet Connection.", Toast.LENGTH_SHORT).show();
				}
			}
		});

		//new WebserviceCheck().execute();
		/*WebService.syncData(getApplicationContext(), userDetails, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject jsonarr) {
				if (jsonarr != null) {

				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				if (error.getClass() == AuthFailureError.class) {
					Toast.makeText(getApplicationContext(), "Issue in Sync Data", Toast.LENGTH_SHORT).show();
				}
			}
		});*/
	}

	private void getDatabase(){
		try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "/data/data/" + getPackageName() + "/databases/BMW.db";
                String backupDBPath = "BMW.db";
                File currentDB = new File(currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception e) {

        }
	}

	private class WebServiceSetServer extends AsyncTask<String, String, String> {

		  private String resp;
		  String soap;
		  private ProgressDialog dialog;

		  @Override
		  protected void onPreExecute() {
			  dialog = new ProgressDialog(Settings.this);
			  garbageDataStorageObject.open();
			  dialog.setMessage("Please wait...");
			  dialog.show();
		  }

		  @Override
		  protected String doInBackground(String... params) {
		   publishProgress("Sleeping..."); // Calls onProgressUpdate()
		   try {
			   soap = WebService.webService_SetCompanyByServerName.callWebserviceCheck(getApplicationContext(),userDetails.getString("appCode","1"));
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
		        }//1|Samvedna BMWI|1|Halol
			  Log.i("WebServiceSetServer","WebServiceSetServer"+soapObject);
			  String []break_value = soapObject.split(Pattern.quote("|"));
              Log.i("==","== WebServiceSetServer"+break_value);

			  editor.putString("companyID", break_value[0]);
			  editor.putString("branchID", break_value[2]);
			  editor.putString("city", break_value[3]);
              editor.commit();

			  new WebserviceSetting().execute();
              /*if(break_value[3].equals("Vadodara")){
				  new WebserviceSetting().execute();
			  }else{
              	  new WebServiceGetServer().execute();
			  }*/

		 }
	}

	private class WebServiceGetServer extends AsyncTask<String, String, SoapObject> {

		private String resp;
		SoapObject soap;
		private ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(Settings.this);
			garbageDataStorageObject.open();
			dialog.setMessage("Please wait...");
			dialog.show();
		}

		@Override
		protected SoapObject doInBackground(String... params) {
			publishProgress("Sleeping..."); // Calls onProgressUpdate()
			try {
				soap = WebService.webService_GetAppCodeList.callWebserviceCheck();
			} catch (Exception e) {
				e.printStackTrace();
				resp = e.getMessage();
			}
			return soap;
		}

		@Override
		protected void onPostExecute(SoapObject soapObject) {
			final ArrayList<String> myList;
			if(soapObject!= null) {
				myList = new ArrayList<String>();
				SoapObject myResult = (SoapObject) soapObject.getProperty("GetAppCodeListResult");

				for (int i = 0; i < myResult.getPropertyCount(); i++) {
					SoapObject lstEmpSoap = (SoapObject) myResult.getProperty(i);

					Object objectResponse = (Object) myResult.getProperty(i);
					SoapObject r =(SoapObject) objectResponse;

					myList.add(lstEmpSoap.getPropertyAsString(4));
				}

				listView = (ListView) findViewById(R.id.list);
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, myList);

				// Assign adapter to ListView
				listView.setAdapter(adapter);

				// ListView Item Click Listener
				listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						int itemPosition     = position;
						String  itemValue    = (String) listView.getItemAtPosition(position);

						editor.putString("appCode", itemValue);
						editor.commit();

						Toast.makeText(getApplicationContext(),"Selected = " +itemValue , Toast.LENGTH_LONG).show();
					}
				});

			}
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
		}
	}

	private class WebserviceSetting extends AsyncTask<String, String, SoapObject> {

		private String resp;
		SoapObject soap;
		private ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(Settings.this);
			garbageDataStorageObject.open();
			dialog.setMessage("Please wait...");
			dialog.show();
		}

		@Override
		protected SoapObject doInBackground(String... params) {
			publishProgress("Sleeping..."); // Calls onProgressUpdate()
			try {
				soap = WebService.webService_Setting.callWebservice(userDetails.getString("companyID","1"),userDetails.getString("branchID","1"),  "0");//userDetails.getString("route_storeID","1"));
			} catch (Exception e) {
				e.printStackTrace();
				resp = e.getMessage();
			}
			return soap;
		}
//anyType{Handset=anyType{HandsetID=1018; BrandName=ALTAF_SAMSUNG; MobileNo=9081918003; IMEI=358271065544665; }; }
		@Override
		protected void onPostExecute(SoapObject soap) {
			if(soap!= null){
				garbageDataStorageObject.deleteAllData(GarbageDataStorage.EMPLOYEE);
				garbageDataStorageObject.deleteAllData(GarbageDataStorage.ROUTE);
				garbageDataStorageObject.deleteAllData(GarbageDataStorage.VEHICLE);
				SoapObject myResult =  (SoapObject)soap.getProperty("GetAllActiveDataResult");
				Log.i("==","myResult"+myResult);

				if(myResult.getProperty("isSuccess").toString().equals("true")){

					SoapObject lstHandset = (SoapObject) myResult.getProperty("lstHandset");
//867168031746172
					for (int j = 0; j< lstHandset.getPropertyCount(); j++) {
						Object objectResponseHandset = (Object) lstHandset.getProperty(j);
						SoapObject rHandset =(SoapObject) objectResponseHandset;

						if(userDetails.getString("device_id","1").equals((String) rHandset.getProperty("IMEI").toString())){

							editor.putString("HandsetID", rHandset.getProperty("HandsetID").toString());
							editor.commit();

							SoapObject lstEmpSoap = (SoapObject) myResult.getProperty("lstAsoNetUser");
							SoapObject lstVehicleSoap = (SoapObject) myResult.getProperty("lstVehicle");
							SoapObject lstRouteSoap = (SoapObject) myResult.getProperty("lstRoute");

							for (int i = 0; i < lstEmpSoap.getPropertyCount(); i++) {
								Object objectResponse = (Object) lstEmpSoap.getProperty(i);
								SoapObject r =(SoapObject) objectResponse;
								ContentValues cv = new ContentValues();
								cv.put("employee_id", (String) r.getProperty("Id").toString());
								cv.put("employee_name", (String) r.getProperty("UserName").toString());

								cv.put("isActive", "true");
								garbageDataStorageObject.insert(GarbageDataStorage.EMPLOYEE, cv);
							}

							for (int i = 0; i < lstVehicleSoap.getPropertyCount(); i++) {
								Object objectResponse = (Object) lstVehicleSoap.getProperty(i);
								SoapObject r =(SoapObject) objectResponse;
								ContentValues cv = new ContentValues();
								cv.put("vehicle_id", (String) r.getProperty("VehicleID").toString());
								cv.put("vehicle_regno", (String) r.getProperty("VehicleRegNo").toString());

								cv.put("isActive", "true");
								garbageDataStorageObject.insert(GarbageDataStorage.VEHICLE, cv);
							}

							for (int i = 0; i < lstRouteSoap.getPropertyCount(); i++) {
								Object objectResponse = (Object) lstRouteSoap.getProperty(i);
								SoapObject r =(SoapObject) objectResponse;
								ContentValues cv = new ContentValues();
								cv.put("route_id", (String) r.getProperty("RouteID").toString());
								cv.put("route_name", (String) r.getProperty("RouteName").toString());

								cv.put("isActive", "true");
								garbageDataStorageObject.insert(GarbageDataStorage.ROUTE, cv);
							}
						}
					}

					String appValue =  userDetails.getString("appCode", "1");
					if(appValue.contains("QEE")) {
						editor.putString("Latitude", GlobalVariables.getLat());
						editor.putString("Longitude", GlobalVariables.getLon());
					}else if(appValue.contains("SBMWI")) {
						editor.putString("Latitude", GlobalVariables.getLat());
						editor.putString("Longitude", GlobalVariables.getLon());
					}else if(appValue.contains("SBMWMoraj")) {
						editor.putString("Latitude", GlobalVariables.getLat());
						editor.putString("Longitude", GlobalVariables.getLon());
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
	private class WebserviceStarted extends AsyncTask<String, String, SoapObject> {

		  private String resp;
		  SoapObject soap;
		  private ProgressDialog dialog;

		  @Override
		  protected void onPreExecute() {
			  dialog = new ProgressDialog(Settings.this);
			  garbageDataStorageObject.open();
			  dialog.setMessage("Please wait...");
			  dialog.show();
		  }

		  @Override
		  protected SoapObject doInBackground(String... params) {
		   publishProgress("Sleeping..."); // Calls onProgressUpdate()
		   return soap;
		  }
		  
		  @Override
		  protected void onPostExecute(SoapObject soapObject) {
			  
			  try {
					cursor1 = garbageDataStorageObject.getTableData(GarbageDataStorage.EMPLOYEE);
					if(cursor1.getCount()>=1)
						txt_emp.setText(""+cursor1.getCount());
						
					cursor2 = garbageDataStorageObject.getTableData(GarbageDataStorage.VEHICLE);
					if(cursor2.getCount()>=1)
						txt_veh.setText(""+cursor2.getCount());
					
					cursor3 = garbageDataStorageObject.getTableData(GarbageDataStorage.ROUTE);
					if(cursor3.getCount()>=1)
						txt_route.setText(""+cursor3.getCount());

				  txt_detail.setText(userDetails.getString("companyID","1") + "/" + userDetails.getString("city","1"));
				   } catch (Exception e) {
				    e.printStackTrace();
				   } finally{
					   garbageDataStorageObject.close();
					   cursor1.close();
					   cursor2.close();
					   cursor3.close();
				   }
			  
			  if (dialog.isShowing()) {
		            dialog.dismiss();
		        }
		 }
	}
	public boolean isConnectingToInternet(){
		ConnectivityManager connectivity = (ConnectivityManager) Settings.this.getSystemService(Context.CONNECTIVITY_SERVICE);
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
