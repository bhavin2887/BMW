package com.example.garbagecollection.bmw;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;


/**
 * Here Global Variables are Defined which are being used throughout the application.
 */
public class GlobalVariables {

	/*1) Manual entry while from scanning part.
	2) Collect bag which has no qr code.*/
	/** Checking the internet is available or not */
	public static boolean isConnected = false;
	public static String Server = "QEE"; //SAMVEDNA //QEE

	//Vadodara LatLon
	public static String vadLat = "22.280347" , vadLon = "73.167942";
	//public static String vadId = "NoReply@quantumenvironment.in" , vadPassword = "Corner@308";
	public static String vadId = "qeevadodara@gmail.com" , vadPassword = "Bmw@1977";

	//Halol LatLon
	public static String halLat = "22.546134", halLon = "73.428585";
	//public static String halId = "service.samvedna@gmail.com" , halPassword = "1511@2171";
	public static String halId = "NoReply@quantumenvironment.in" , halPassword = "Corner@308";

	//Morbi LatLon
	public static String morLat = "22.47955", morLon = "72.6099";
	public static String morId = "sbmwianand@gmail.com" , morPassword = "moraj@2015";

	public static String vadServer = "http://60.254.38.13/WebService1.asmx";
	public static String halServer = "http://60.254.38.13:8082/WebService1.asmx";


	/**
	 * This is a broadcast receiver that will be registered for receiving the network status broadcast from the OS.
	 * and will set the <strong>isConnected</strong> flag to according value.
	 */
	public static BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        	ConnectivityManager cm=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo ni=cm.getActiveNetworkInfo();
			Log.i("BroadCastReceiver", "Connected ="+isConnected);
			if(ni!=null && ni.isConnected()){
				isConnected=true;
			}else{
				isConnected=false;
			}
        }
	};

	public static String getLat(){
		if(Server == "QEE"){
			return vadLat;
		}else{
			return halLat;
		}
	}

	public static String getLon(){
		if(Server == "QEE"){
			return vadLon;
		}else{
			return halLat;
		}
	}

	public static String getLatStart(String val){
		if(val.equals("0")){
			return halLat;
		}else{
			return morLat;
		}
	}

	public static String getLonStart(String val){
		if(val.equals("0")){
			return halLon;
		}else{
			return morLon;
		}
	}

	public static String getGmailID(){
		if(Server == "QEE"){
			return vadId;
		}else{
			return halId;
		}
	}

	public static String getGmailPwd(){
		if(Server == "QEE"){
			return vadPassword;
		}else{
			return halPassword;
		}
	}

	public static String getServerURL(){
		if(Server == "QEE"){
			return vadServer;
		}else{
			return halServer;
		}
	}

	public static String getHostURL(){
		if(Server == "QEE"){
			return "QEE";
		}else{
			return "SBMWI";
		}
	}

	public static int getLogo(){
		if(Server == "QEE"){
			return R.drawable.q_logo;
		}else{
			return R.mipmap.sam_worker;
		}
	}
}
