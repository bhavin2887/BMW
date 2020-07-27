package com.example.garbagecollection.doctor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.garbagecollection.bmw.R;
import com.example.garbagecollection.bmw.ZBarConstants;
import com.example.garbagecollection.bmw.ZBarScannerActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.garbagecollection.doctor.DoctorVerifyActivity.btn_Hospital_scanCode;
import static com.example.garbagecollection.doctor.HospitalDoctor.btn_bag_scanCode;

public class MainScreenDoctor extends Activity {

	private static final int ZBAR_SCANNER_REQUEST = 0;
	private static final int ZBAR_QR_SCANNER_REQUEST = 1;
	Boolean exit = false;
	SharedPreferences sharedpreferences;
	public static final String MyPREFERENCES = "MyPrefs" ;
	SharedPreferences userDetails;
	Intent in_bundle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_screen);
		in_bundle = getIntent();
	}

	 /** To scan QR Code **/ 
	public void launchQRScanner(View v) {
		if (isCameraAvailable()) {
			Intent intent = new Intent(this, ZBarScannerActivity.class);
			startActivityForResult(intent, ZBAR_SCANNER_REQUEST);
			exit = true;
		} else {
			Toast.makeText(this, "Rear Facing Camera Unavailable", Toast.LENGTH_SHORT).show();
		}
	}

	/** CHECK CAMERA AVAILABLITY **/
	public boolean isCameraAvailable() {
		PackageManager pm = getPackageManager();
		return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
	}

	/** CALLED AFTER SCANNING THE QR CODE **/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case ZBAR_SCANNER_REQUEST:
		case ZBAR_QR_SCANNER_REQUEST:

			if (resultCode == RESULT_OK) {

				sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = sharedpreferences.edit();
				userDetails = getApplicationContext().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
				userDetails.getString("employee_store", "1");

				String stringVal = data.getStringExtra(ZBarConstants.SCAN_RESULT);
				String[] DocCompare = stringVal.split(Pattern.quote("|"));
				String token = null;
				if(DocCompare[1].contains("DrName")){
					if(DocCompare[0].matches("\\d+")){
						token = DocCompare[0];
					}else if(DocCompare[0].contains("-")){
						token = DocCompare[0].substring(DocCompare[0].indexOf("-")+1,DocCompare[0].length());
					}else if(DocCompare[0].contains("_")){
						token = DocCompare[0].substring(DocCompare[0].indexOf("_")+1,DocCompare[0].length());
					}
					Intent intent=new Intent();
					intent.putExtra("hospital_id",token);
					intent.putExtra("hospitalContent",stringVal);
					setResult(btn_Hospital_scanCode,intent);
					finish();
				}else if(stringVal.contains("YELLOW") || stringVal.contains("RED") || stringVal.contains("WHITE") || stringVal.contains("BLUE")){
						Log.i("==","=="+stringVal);
						Intent intent=new Intent();
						intent.putExtra("ColorValue",stringVal);
						setResult(btn_bag_scanCode,intent);
						finish();
				}else{
					this.finish();
					//onBackPressed();
				}


				/*String[] DocCompare = stringVal.split(Pattern.quote("|"));
					if(DocCompare.length >= 2 && DocCompare[1].contains("DrName")){
						//if(DocCompare[1].contains("DrName")){
//6|DrName:DR. RAMAN T.HANDA|HospitalName:DR. Raman  Hospital|Area:Makarpura|Pincode 390009|PhoneNo:2644466|MobileNo:9988998899|GPCBid:365116
//SMA-10|DrName:DR. RAMAN T.HANDA|HospitalName:DR. Raman  Hospital|Area:Makarpura|Pincode 390009|PhoneNo:2644466|MobileNo:9988998899|GPCBid:365116
							String token = null;
							if(DocCompare[0].matches("\\d+")){
								token = DocCompare[0];
							}else if(DocCompare[0].contains("-")){
								token = DocCompare[0].substring(DocCompare[0].indexOf("-")+1,DocCompare[0].length());
							}else if(DocCompare[0].contains("_")){
								token = DocCompare[0].substring(DocCompare[0].indexOf("_")+1,DocCompare[0].length());
							}

							Intent ite = new Intent(MainScreenDoctor.this, HospitalOne.class);
							ite.putExtra("hospital_id", "" + token);
							startActivity(ite);
							finish();
						//}
					}else{
						String stringData = data.getStringExtra(ZBarConstants.SCAN_RESULT);
						try {
							if(stringData.contains("RoleName")){
	//CompanyID:1|CompnayName:Quantum Environment Engineer,BranchID:1|BranchName:QEE Vadodara,RoleID:55ae219c-a76c-4c9e-9904-2ba252f6586c|RoleName:Supervisor,UserID:129c54cc-6519-4158-b048-d2df3f0c3d34|UserName:dipakup.qee
								String superVID = stringData.substring(stringData.indexOf("UserID:")+7,stringData.indexOf("|UserName"));
								Intent intent=new Intent();
								intent.putExtra("fromCamera", ""+superVID);
								setResult(3,intent);
								finish();
							}else if(stringData.contains("YELLOW") || stringData.contains("RED") || stringData.contains("WHITE") || stringData.contains("BLUE")){
								if(in_bundle.getStringExtra("HospitalOne")!=null && in_bundle.getStringExtra("HospitalOne").equals("HospitalOne")){
//477|Y|QEEVADODARA|2|13021800001,DrName:DR.ABC,HospName:Trial Hosp,Area:,Pin:000000,Phone:0,Mobile:0,GPCBID:0
									Log.i("==","=="+stringVal);
									Intent intent=new Intent();
									intent.putExtra("ColorValue",stringVal);
									setResult(2,intent);
									finish();
								} else{
									Toast.makeText(getApplicationContext(), "Scan DoctorID First.", Toast.LENGTH_SHORT).show();
									//finish();
								}
							} else{
								Toast.makeText(getApplicationContext(), "Please Scan Proper QR-Code.", Toast.LENGTH_SHORT).show();
								//finish();
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//onBackPressed();
					}*/
			} else if(resultCode == RESULT_CANCELED && data != null) {
				String error = data.getStringExtra(ZBarConstants.ERROR_INFO);
				if(!TextUtils.isEmpty(error)) {
				//	Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
				}
				//onBackPressed();
			}else{
				this.finish();
				//onBackPressed();
			}
			break;
		}
	}

	/** This is used to check the given URL is valid or not **/
	private boolean isValidUrl(String url) {
		Pattern p = Patterns.WEB_URL;
		Matcher m = p.matcher(url);
		if(m.matches())
			return true;
		else
			return false;
	}


	@Override
	protected void onResume() {
		super.onResume();
		SharedPreferences prefs = getSharedPreferences("IDvalue", 0);
		//	preferences = getSharedPreferences("IDvalue", 0);
		String strResult = prefs.getString("result", "");
		String strUrl = prefs.getString("url", "");
		
		/*if(strResult.equals("result")){
			SharedPreferences.Editor editor = prefs.edit();
			editor.putString("result", "");
			editor.commit();
			if(!exit){
				launchQRScanner(getCurrentFocus());
			}else{
				if(strUrl.equals("url")){
					editor.putString("url", "");
					editor.commit();
				}
			}
		}else{*/
			if(!exit){
				launchQRScanner(getCurrentFocus());
			}else{
				launchQRScanner(getCurrentFocus());
			}
		//}
		
	}

	/** EXITING THE APPLICATION **/
	@Override
	public void onBackPressed() {
		finish();
		System.exit(0);
	}
}
