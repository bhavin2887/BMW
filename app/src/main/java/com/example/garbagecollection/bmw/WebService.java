package com.example.garbagecollection.bmw;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Response;

import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class WebService {

	
	private static final String NAMESPACE="http://tempuri.org/";
	
	//private static final String GlobalVariables.getServerURL()="http://60.254.38.13/WebService1.asmx";

	//private static final String GlobalVariables.getServerURL()="http://60.254.38.13:8082/WebService1.asmx";
    //private static final String GlobalVariables.getServerURL()="http://quantumenvironment.in/StorageWebService.asmx";


	public static void syncData(Context context, final SharedPreferences spEditor, JSONObject jsonObj, Response.Listener<JSONObject> LoginSuccessListener, Response.ErrorListener FailureListener) {
		VolleyClient v = new VolleyClient();
		v.sendPostWithBearerData(context, spEditor, "http://60.254.38.13/WebService1.asmx?op=HelloWorld", jsonObj, LoginSuccessListener, FailureListener);
	}

	public static class webService_DailyVehicleRouteAssignment{

		private static final String SOAP_ACTION="http://tempuri.org/DailyVehicleRouteAssignment";
		private static final String METHOD_NAME="DailyVehicleRouteAssignment";
		public static SoapObject soapObject;

		public static SoapObject callWebservice(Context con,String val){

			SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.implicitTypes = true;
			envelope.dotNet=true;

			SoapObject requestSoap=new SoapObject(NAMESPACE, METHOD_NAME);
			try {
				Object response = envelope.getResponse();
			}catch(Exception ee){

			}

			requestSoap.addProperty("jSONString", val);

			envelope.setOutputSoapObject(requestSoap);

			HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalVariables.getServerURL(),60000);
			try{
				androidHttpTransport.call(SOAP_ACTION, envelope);
				if (androidHttpTransport.debug){
					Log.d("ws", androidHttpTransport.requestDump);
				}//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=0; isSuccess=false; errorMsg=Object reference not set to an instance of an object.,InnerEx:; }; }
				return deserializeSoap((SoapObject)envelope.bodyIn);
			}catch(Exception excp){
				excp.printStackTrace();
			}
			return null;
			//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=0; isSuccess=false; errorMsg= is not a valid value for Int32.,InnerEx:System.IndexOutOfRangeException: Index was outside the bounds of the array.
		}
		//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=1; isSuccess=true; errorMsg=All Records Saved Successfully; }; }
		public static SoapObject deserializeSoap(SoapObject paramObject){
			if(paramObject.hasProperty("DailyVehicleRouteAssignmentResponse")){
				soapObject = (SoapObject) paramObject.getProperty("DailyVehicleRouteAssignmentResponse");
			}
			return paramObject;
		}

	}

	public static class webService_DailyHospitalVisitDetails{

		private static final String SOAP_ACTION="http://tempuri.org/DailyHospitalVisitDetails";
		private static final String METHOD_NAME="DailyHospitalVisitDetails";
		public static SoapObject soapObject;

		public static SoapObject callWebservice(Context con,String val){

			SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.implicitTypes = true;
			envelope.dotNet=true;

			SoapObject requestSoap=new SoapObject(NAMESPACE, METHOD_NAME);
			try {
				Object response = envelope.getResponse();
			}catch(Exception ee){

			}

			requestSoap.addProperty("jSONString", val);

			envelope.setOutputSoapObject(requestSoap);

			HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalVariables.getServerURL(),60000);
			try{
				androidHttpTransport.call(SOAP_ACTION, envelope);
				if (androidHttpTransport.debug){
					Log.d("ws", androidHttpTransport.requestDump);
				}//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=0; isSuccess=false; errorMsg=Object reference not set to an instance of an object.,InnerEx:; }; }
				return deserializeSoap((SoapObject)envelope.bodyIn);
			}catch(Exception excp){
				excp.printStackTrace();
			}
			return null;
			//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=0; isSuccess=false; errorMsg= is not a valid value for Int32.,InnerEx:System.IndexOutOfRangeException: Index was outside the bounds of the array.
		}
		//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=1; isSuccess=true; errorMsg=All Records Saved Successfully; }; }
		public static SoapObject deserializeSoap(SoapObject paramObject){
			Log.i("Data", "DailyHospitalVisitDetails==");
			if(paramObject.hasProperty("DailyHospitalVisitDetailsResponse")){
				Log.i("Data", "DailyHospitalVisitDetails=="+paramObject);
				soapObject = (SoapObject) paramObject.getProperty("DailyHospitalVisitDetailsResponse");
					/*String count =  soapObject.getProperty("respCount").toString();
					String isSucess =  soapObject.getProperty("isSuccess").toString();
					String msg =  soapObject.getProperty("errorMsg").toString();*/
			}
			return paramObject;
		}
	}

	public static class webService_BMWBagsCollectionDetails{

		private static final String SOAP_ACTION="http://tempuri.org/BMWBagsCollectionDetails";
		private static final String METHOD_NAME="BMWBagsCollectionDetails";
		public static SoapObject soapObject;
		public static SoapObject soapObjectInner;
		public static SoapObject soapObjectInnerTable;


		public static SoapObject callWebservice(Context con,String val){

			SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.implicitTypes = true;
			envelope.dotNet=true;

			SoapObject requestSoap=new SoapObject(NAMESPACE, METHOD_NAME);
			try {
				Object response = envelope.getResponse();
			}catch(Exception ee){

			}

			requestSoap.addProperty("jSONString", val);

			envelope.setOutputSoapObject(requestSoap);

			HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalVariables.getServerURL(),60000);
			try{
				androidHttpTransport.call(SOAP_ACTION, envelope);
				if (androidHttpTransport.debug){
					Log.d("ws", androidHttpTransport.requestDump);
				}//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=0; isSuccess=false; errorMsg=Object reference not set to an instance of an object.,InnerEx:; }; }
				return deserializeSoap((SoapObject)envelope.bodyIn);
			}catch(Exception excp){
				excp.printStackTrace();
			}
			return null;
			//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=0; isSuccess=false; errorMsg= is not a valid value for Int32.,InnerEx:System.IndexOutOfRangeException: Index was outside the bounds of the array.
		}
		//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=1; isSuccess=true; errorMsg=All Records Saved Successfully; }; }
		public static SoapObject deserializeSoap(SoapObject paramObject){
			if(paramObject.hasProperty("BMWBagsCollectionDetailsResponse")){
				soapObject = (SoapObject) paramObject.getProperty("BMWBagsCollectionDetailsResponse");
					/*String count =  soapObject.getProperty("respCount").toString();
					String isSucess =  soapObject.getProperty("isSuccess").toString();
					String msg =  soapObject.getProperty("errorMsg").toString();*/
			}
			return paramObject;
		}
	}

	public static class webService_BMWBagsCollectionDetailsManually{

		private static final String SOAP_ACTION="http://tempuri.org/BMWBagsCollectionDetailsManually";
		private static final String METHOD_NAME="BMWBagsCollectionDetailsManually";
		public static SoapObject soapObject;
		public static SoapObject soapObjectInner;
		public static SoapObject soapObjectInnerTable;


		public static SoapObject callWebservice(Context con,String val){

			SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.implicitTypes = true;
			envelope.dotNet=true;

			SoapObject requestSoap=new SoapObject(NAMESPACE, METHOD_NAME);
			try {
				Object response = envelope.getResponse();
			}catch(Exception ee){

			}

			requestSoap.addProperty("jSONString", val);

			envelope.setOutputSoapObject(requestSoap);

			HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalVariables.getServerURL(),60000);
			try{
				androidHttpTransport.call(SOAP_ACTION, envelope);
				if (androidHttpTransport.debug){
					Log.d("ws", androidHttpTransport.requestDump);
				}//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=0; isSuccess=false; errorMsg=Object reference not set to an instance of an object.,InnerEx:; }; }
				return deserializeSoap((SoapObject)envelope.bodyIn);
			}catch(Exception excp){
				excp.printStackTrace();
			}
			return null;
			//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=0; isSuccess=false; errorMsg= is not a valid value for Int32.,InnerEx:System.IndexOutOfRangeException: Index was outside the bounds of the array.
		}
		//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=1; isSuccess=true; errorMsg=All Records Saved Successfully; }; }
		public static SoapObject deserializeSoap(SoapObject paramObject){
			if(paramObject.hasProperty("BMWBagsCollectionDetailsResponse")){
				soapObject = (SoapObject) paramObject.getProperty("BMWBagsCollectionDetailsResponse");
					/*String count =  soapObject.getProperty("respCount").toString();
					String isSucess =  soapObject.getProperty("isSuccess").toString();
					String msg =  soapObject.getProperty("errorMsg").toString();*/
			}
			return paramObject;
		}
	}


	public static class webService_BMWBagsCollectionSummary{

		private static final String SOAP_ACTION="http://tempuri.org/BMWBagsCollectionSummary";
		private static final String METHOD_NAME="BMWBagsCollectionSummary";
		public static SoapObject soapObject;
		public static SoapObject soapObjectInner;
		public static SoapObject soapObjectInnerTable;


		public static SoapObject callWebservice(Context con,String val){

			SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.implicitTypes = true;
			envelope.dotNet=true;

			SoapObject requestSoap=new SoapObject(NAMESPACE, METHOD_NAME);
			try {
				Object response = envelope.getResponse();
			}catch(Exception ee){

			}

			requestSoap.addProperty("jSONString", val);

			envelope.setOutputSoapObject(requestSoap);

			HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalVariables.getServerURL(),60000);
			try{
				androidHttpTransport.call(SOAP_ACTION, envelope);
				if (androidHttpTransport.debug){
					Log.d("ws", androidHttpTransport.requestDump);
				}//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=0; isSuccess=false; errorMsg=Object reference not set to an instance of an object.,InnerEx:; }; }
				return deserializeSoap((SoapObject)envelope.bodyIn);
			}catch(Exception excp){
				excp.printStackTrace();
			}
			return null;
			//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=0; isSuccess=false; errorMsg= is not a valid value for Int32.,InnerEx:System.IndexOutOfRangeException: Index was outside the bounds of the array.
		}
		//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=1; isSuccess=true; errorMsg=All Records Saved Successfully; }; }
		public static SoapObject deserializeSoap(SoapObject paramObject){
			if(paramObject.hasProperty("BMWBagsCollectionSummaryResponse")){
				soapObject = (SoapObject) paramObject.getProperty("BMWBagsCollectionSummaryResponse");
					/*String count =  soapObject.getProperty("respCount").toString();
					String isSucess =  soapObject.getProperty("isSuccess").toString();
					String msg =  soapObject.getProperty("errorMsg").toString();*/
			}
			return paramObject;
		}
	}


	public static class webService_CheckForUsedBagQRCode{

		private static final String SOAP_ACTION="http://tempuri.org/CheckForUsedBagQRCode";
		private static final String METHOD_NAME="CheckForUsedBagQRCode";
		public static SoapObject soapObject;
		public static SoapObject soapObjectInner;
		public static SoapObject soapObjectInnerTable;


		public static SoapObject callWebservice(Context con,String val){

			SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.implicitTypes = true;
			envelope.dotNet=true;

			SoapObject requestSoap=new SoapObject(NAMESPACE, METHOD_NAME);
			try {
				Object response = envelope.getResponse();
			}catch(Exception ee){

			}

			requestSoap.addProperty("BagQRCodeDetalID", val);

			envelope.setOutputSoapObject(requestSoap);

			HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalVariables.getServerURL(),60000);
			try{
				androidHttpTransport.call(SOAP_ACTION, envelope);
				if (androidHttpTransport.debug){
					Log.d("ws", androidHttpTransport.requestDump);
				}//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=0; isSuccess=false; errorMsg=Object reference not set to an instance of an object.,InnerEx:; }; }
				return deserializeSoap((SoapObject)envelope.bodyIn);
			}catch(Exception excp){
				excp.printStackTrace();
			}
			return null;
			//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=0; isSuccess=false; errorMsg= is not a valid value for Int32.,InnerEx:System.IndexOutOfRangeException: Index was outside the bounds of the array.
		}
		//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=1; isSuccess=true; errorMsg=All Records Saved Successfully; }; }
		public static SoapObject deserializeSoap(SoapObject paramObject){
			if(paramObject.hasProperty("CheckForUsedBagQRCodeResponse")){
				soapObject = (SoapObject) paramObject.getProperty("CheckForUsedBagQRCodeResponse");
			}
			return paramObject;
		}
	}

	public static class webService_EndDayNotificaionEndRoute{

		private static final String SOAP_ACTION="http://tempuri.org/EndDayNotificaionEndRoute";
		private static final String METHOD_NAME="EndDayNotificaionEndRoute";
		public static SoapObject soapObject;
		public static SoapObject soapObjectInner;
		public static SoapObject soapObjectInnerTable;


		public static SoapObject callWebservice(Context con,String val){

			SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.implicitTypes = true;
			envelope.dotNet=true;

			SoapObject requestSoap=new SoapObject(NAMESPACE, METHOD_NAME);
			try {
				Object response = envelope.getResponse();
			}catch(Exception ee){

			}

			requestSoap.addProperty("jSONString", val);

			envelope.setOutputSoapObject(requestSoap);

			HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalVariables.getServerURL(),60000);
			try{
				androidHttpTransport.call(SOAP_ACTION, envelope);
				if (androidHttpTransport.debug){
					Log.d("ws", androidHttpTransport.requestDump);
				}//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=0; isSuccess=false; errorMsg=Object reference not set to an instance of an object.,InnerEx:; }; }
				return deserializeSoap((SoapObject)envelope.bodyIn);
			}catch(Exception excp){
				excp.printStackTrace();
			}
			return null;
			//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=0; isSuccess=false; errorMsg= is not a valid value for Int32.,InnerEx:System.IndexOutOfRangeException: Index was outside the bounds of the array.
		}
		//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=1; isSuccess=true; errorMsg=All Records Saved Successfully; }; }
		public static SoapObject deserializeSoap(SoapObject paramObject){
			if(paramObject.hasProperty("EndDayNotificaionEndRouteResult")){
				//soapObject = (SoapObject) paramObject.getProperty("EndDayNotificaionEndRouteResult");
					/*String count =  soapObject.getProperty("respCount").toString();
					EndDayNotificaionEndRouteResponse{EndDayNotificaionEndRouteResult=55; }
					String isSucess =  soapObject.getProperty("isSuccess").toString();
					String msg =  soapObject.getProperty("errorMsg").toString();*/
			}
			return paramObject;
		}
	}
	
	public static class webService_Setting{
		
		private static final String SOAP_ACTION="http://tempuri.org/GetAllActiveData";
		private static final String METHOD_NAME="GetAllActiveData";
		public static SoapObject soapObject;
		public static SoapObject soapObjectInner;
		public static SoapObject soapObjectInnerTable;

		public static SoapObject callWebservice(String cmp, String brn, String route){
			
			SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.implicitTypes = true;
			envelope.dotNet=true;
			
			SoapObject requestSoap=new SoapObject(NAMESPACE, METHOD_NAME);

			requestSoap.addProperty("companyID", cmp);
			requestSoap.addProperty("branchID", brn);
			requestSoap.addProperty("routeID", route);

			envelope.setOutputSoapObject(requestSoap);
			
			HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalVariables.getServerURL(),60000);
			try{
				androidHttpTransport.call(SOAP_ACTION, envelope);
				if (androidHttpTransport.debug){
					Log.d("ws", androidHttpTransport.requestDump);
				}
				return (SoapObject)envelope.bodyIn;
			}catch(Exception excp){
				excp.printStackTrace();
			}
			return null;
		}
	}

	public static class webService_CheckHospital{
		
		private static final String SOAP_ACTION="http://tempuri.org/CheckHospitalIsActive";
		private static final String METHOD_NAME="CheckHospitalIsActive";
		public static SoapObject soapObject;
		public static SoapObject soapObjectInner;
		public static SoapObject soapObjectInnerTable;
		public static String soapString;

		public static String callWebserviceCheck(Context con,int cID,int bID,String mID){

			SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.implicitTypes = true;
			envelope.dotNet=true;

			SoapObject requestSoap=new SoapObject(NAMESPACE, METHOD_NAME);

			requestSoap.addProperty("companyID", cID);
			requestSoap.addProperty("branchID", bID);
			requestSoap.addProperty("membershipNo", mID);

			envelope.setOutputSoapObject(requestSoap);

			HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalVariables.getServerURL(),60000);
			try{
				androidHttpTransport.call(SOAP_ACTION, envelope);
				if (androidHttpTransport.debug){
					Log.d("ws", androidHttpTransport.requestDump);
				}//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=0; isSuccess=false; errorMsg=Object reference not set to an instance of an object.,InnerEx:; }; }
				return deserializeSoap((SoapObject)envelope.bodyIn);
			}catch(Exception excp){
				excp.printStackTrace();
			}
			return null;
		}
		public static String deserializeSoap(SoapObject paramObject){
			if(paramObject.hasProperty("CheckHospitalIsActiveResult")){
				soapString =  paramObject.getPropertyAsString("CheckHospitalIsActiveResult");
			}
			return soapString;
		}
	}

	public static class webService_HelloWorld{
	
	private static final String SOAP_ACTION="http://tempuri.org/HelloWorld";
	private static final String METHOD_NAME="HelloWorld";
	public static String soapString;
	public static SoapObject soapObjectInner;
	public static SoapObject soapObjectInnerTable;


	public static String callWebserviceCheck(Context con){
		
		SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.implicitTypes = true;
		envelope.dotNet=true;
		
		SoapObject requestSoap=new SoapObject(NAMESPACE, METHOD_NAME);
		
		envelope.setOutputSoapObject(requestSoap);
		
		HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalVariables.getServerURL(),60000);
		try{
			androidHttpTransport.call(SOAP_ACTION, envelope);
			if (androidHttpTransport.debug){
				Log.d("ws", androidHttpTransport.requestDump);
			}//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=0; isSuccess=false; errorMsg=Object reference not set to an instance of an object.,InnerEx:; }; }
			return deserializeSoap((SoapObject)envelope.bodyIn);
		}catch(Exception excp){
			excp.printStackTrace();
		}
		return null;
		//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=0; isSuccess=false; errorMsg= is not a valid value for Int32.,InnerEx:System.IndexOutOfRangeException: Index was outside the bounds of the array.
	}
	//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=1; isSuccess=true; errorMsg=All Records Saved Successfully; }; }
	public static String deserializeSoap(SoapObject paramObject){
		if(paramObject.hasProperty("HelloWorldResult")){
			soapString =  paramObject.getPropertyAsString("HelloWorldResult");
		}
		return soapString;
	}
}

	public static class webService_SetCompanyByServerName{

		private static final String SOAP_ACTION="http://tempuri.org/SetCompanyByServerName";
		private static final String METHOD_NAME="SetCompanyByServerName";
		public static String soapString;
		public static SoapObject soapObjectInner;
		public static SoapObject soapObjectInnerTable;


		public static String callWebserviceCheck(Context con,String serverName){

			SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.implicitTypes = true;
			envelope.dotNet=true;

			SoapObject requestSoap=new SoapObject(NAMESPACE, METHOD_NAME);

			requestSoap.addProperty("strServerName", serverName);

			envelope.setOutputSoapObject(requestSoap);

			HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalVariables.getServerURL(),60000);
			try{
				androidHttpTransport.call(SOAP_ACTION, envelope);
				if (androidHttpTransport.debug){
					Log.d("ws", androidHttpTransport.requestDump);
				}//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=0; isSuccess=false; errorMsg=Object reference not set to an instance of an object.,InnerEx:; }; }
				return deserializeSoap((SoapObject)envelope.bodyIn);
			}catch(Exception excp){
				excp.printStackTrace();
			}
			return null;
			//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=0; isSuccess=false; errorMsg= is not a valid value for Int32.,InnerEx:System.IndexOutOfRangeException: Index was outside the bounds of the array.
		}
		//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=1; isSuccess=true; errorMsg=All Records Saved Successfully; }; }
		public static String deserializeSoap(SoapObject paramObject){
			if(paramObject.hasProperty("SetCompanyByServerNameResult")){
				//String SObject = (String) paramObject.getProperty("HelloWorldResult");
				soapString =  paramObject.getPropertyAsString("SetCompanyByServerNameResult");
			}
			return soapString;
		}
	}

	public static class webService_AuthenticateSupervisor{

		private static final String SOAP_ACTION="http://tempuri.org/AuthenticateSupervisor";
		private static final String METHOD_NAME="AuthenticateSupervisor";
		public static String soapString;
		public static SoapObject soapObjectInner;
		public static SoapObject soapObjectInnerTable;


		public static String callWebserviceCheck(Context con,String codeContent){

			SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.implicitTypes = true;
			envelope.dotNet=true;

			SoapObject requestSoap=new SoapObject(NAMESPACE, METHOD_NAME);

			requestSoap.addProperty("QRCodeContent", codeContent);

			envelope.setOutputSoapObject(requestSoap);

			HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalVariables.getServerURL(),60000);
			try{
				androidHttpTransport.call(SOAP_ACTION, envelope);
				if (androidHttpTransport.debug){
					Log.d("ws", androidHttpTransport.requestDump);
				}//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=0; isSuccess=false; errorMsg=Object reference not set to an instance of an object.,InnerEx:; }; }
				return deserializeSoap((SoapObject)envelope.bodyIn);
			}catch(Exception excp){
				excp.printStackTrace();
			}
			return null;
			//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=0; isSuccess=false; errorMsg= is not a valid value for Int32.,InnerEx:System.IndexOutOfRangeException: Index was outside the bounds of the array.
		}
		//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=1; isSuccess=true; errorMsg=All Records Saved Successfully; }; }
		public static String deserializeSoap(SoapObject paramObject){
			if(paramObject.hasProperty("AuthenticateSupervisorResult")){
				//String SObject = (String) paramObject.getProperty("HelloWorldResult");
				soapString =  paramObject.getPropertyAsString("AuthenticateSupervisorResult");
			}
			return soapString;
		}
	}

	public static class webService_GetAppCodeList{

		private static final String SOAP_ACTION="http://tempuri.org/GetAppCodeList";
		private static final String METHOD_NAME="GetAppCodeList";
		public static String soapString;
		public static SoapObject soapObjectInner;
		public static SoapObject soapObjectInnerTable;


		public static SoapObject callWebserviceCheck(){

			SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.implicitTypes = true;
			envelope.dotNet=true;

			SoapObject requestSoap=new SoapObject(NAMESPACE, METHOD_NAME);

			//requestSoap.addProperty("QRCodeContent", codeContent);

			envelope.setOutputSoapObject(requestSoap);

			HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalVariables.getServerURL(),60000);
			try{
				androidHttpTransport.call(SOAP_ACTION, envelope);
				if (androidHttpTransport.debug){
					Log.d("ws", androidHttpTransport.requestDump);
				}//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=0; isSuccess=false; errorMsg=Object reference not set to an instance of an object.,InnerEx:; }; }
				//return deserializeSoap((SoapObject)envelope.bodyIn);
				return (SoapObject)envelope.bodyIn;

			}catch(Exception excp){
				excp.printStackTrace();
			}
			return null;
			//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=0; isSuccess=false; errorMsg= is not a valid value for Int32.,InnerEx:System.IndexOutOfRangeException: Index was outside the bounds of the array.
		}
		//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=1; isSuccess=true; errorMsg=All Records Saved Successfully; }; }
		public static String deserializeSoap(SoapObject paramObject){
			if(paramObject.hasProperty("GetAppCodeListResult")){
				//String SObject = (String) paramObject.getProperty("HelloWorldResult");
				soapString =  paramObject.getPropertyAsString("GetAppCodeListResult");
			}
			return soapString;
		}
	}

	public static class webService_AuthenticateDoctor{

		private static final String SOAP_ACTION="http://tempuri.org/AuthenticateDoctor";
		private static final String METHOD_NAME="AuthenticateDoctor";
		public static String soapString;
		public static SoapObject soapObjectInner;
		public static SoapObject soapObjectInnerTable;


		public static String callWebserviceCheck(String codeContent){

			SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.implicitTypes = true;
			envelope.dotNet=true;

			SoapObject requestSoap=new SoapObject(NAMESPACE, METHOD_NAME);

			requestSoap.addProperty("QRCodeContent", codeContent);

			envelope.setOutputSoapObject(requestSoap);

			HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalVariables.getServerURL(),60000);
			try{
				androidHttpTransport.call(SOAP_ACTION, envelope);
				if (androidHttpTransport.debug){
					Log.d("ws", androidHttpTransport.requestDump);
				}//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=0; isSuccess=false; errorMsg=Object reference not set to an instance of an object.,InnerEx:; }; }
				return deserializeSoap((SoapObject)envelope.bodyIn);
			}catch(Exception excp){
				excp.printStackTrace();
			}
			return null;
			//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=0; isSuccess=false; errorMsg= is not a valid value for Int32.,InnerEx:System.IndexOutOfRangeException: Index was outside the bounds of the array.
		}
		//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=1; isSuccess=true; errorMsg=All Records Saved Successfully; }; }
		public static String deserializeSoap(SoapObject paramObject){
			if(paramObject.hasProperty("AuthenticateDoctorResult")){
				//String SObject = (String) paramObject.getProperty("HelloWorldResult");
				soapString =  paramObject.getPropertyAsString("AuthenticateDoctorResult");
			}
			return soapString;
		}
	}

	public static class webService_WasteScanningAtHospitals {

		private static final String SOAP_ACTION="http://tempuri.org/WasteScanningAtHospitals ";
		private static final String METHOD_NAME="WasteScanningAtHospitals ";
		public static String soapString;
		public static SoapObject soapObjectInner;
		public static SoapObject soapObjectInnerTable;


		public static String callWebserviceCheck(Context con,String codeContent){

			SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.implicitTypes = true;
			envelope.dotNet=true;

			SoapObject requestSoap=new SoapObject(NAMESPACE, METHOD_NAME);

			requestSoap.addProperty("jSONString", codeContent);

			envelope.setOutputSoapObject(requestSoap);

			HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalVariables.getServerURL(),60000);
			try{
				androidHttpTransport.call(SOAP_ACTION, envelope);
				if (androidHttpTransport.debug){
					Log.d("ws", androidHttpTransport.requestDump);
				}//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=0; isSuccess=false; errorMsg=Object reference not set to an instance of an object.,InnerEx:; }; }
				return deserializeSoap((SoapObject)envelope.bodyIn);
			}catch(Exception excp){
				excp.printStackTrace();
			}
			return null;
			//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=0; isSuccess=false; errorMsg= is not a valid value for Int32.,InnerEx:System.IndexOutOfRangeException: Index was outside the bounds of the array.
		}
		//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=1; isSuccess=true; errorMsg=All Records Saved Successfully; }; }
		public static String deserializeSoap(SoapObject paramObject){
			if(paramObject.hasProperty("WasteScanningAtHospitalsResult")){
				//String SObject = (String) paramObject.getProperty("HelloWorldResult");
				soapString =  paramObject.getPropertyAsString("WasteScanningAtHospitalsResult");
			}
			return soapString;
		}
	}

	public static class webService_WasteScanningAtHospitalsManually {

		private static final String SOAP_ACTION="http://tempuri.org/WasteScanningAtHospitalsManually ";
		private static final String METHOD_NAME="WasteScanningAtHospitalsManually ";
		public static String soapString;
		public static SoapObject soapObjectInner;
		public static SoapObject soapObjectInnerTable;


		public static String callWebserviceCheck(Context con,String codeContent){

			SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.implicitTypes = true;
			envelope.dotNet=true;

			SoapObject requestSoap=new SoapObject(NAMESPACE, METHOD_NAME);

			requestSoap.addProperty("jSONString", codeContent);

			envelope.setOutputSoapObject(requestSoap);

			HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalVariables.getServerURL(),60000);
			try{
				androidHttpTransport.call(SOAP_ACTION, envelope);
				if (androidHttpTransport.debug){
					Log.d("ws", androidHttpTransport.requestDump);
				}//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=0; isSuccess=false; errorMsg=Object reference not set to an instance of an object.,InnerEx:; }; }
				return deserializeSoap((SoapObject)envelope.bodyIn);
			}catch(Exception excp){
				excp.printStackTrace();
			}
			return null;
			//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=0; isSuccess=false; errorMsg= is not a valid value for Int32.,InnerEx:System.IndexOutOfRangeException: Index was outside the bounds of the array.
		}
		//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=1; isSuccess=true; errorMsg=All Records Saved Successfully; }; }
		public static String deserializeSoap(SoapObject paramObject){
			if(paramObject.hasProperty("WasteScanningAtHospitalsResult")){
				//String SObject = (String) paramObject.getProperty("HelloWorldResult");
				soapString =  paramObject.getPropertyAsString("WasteScanningAtHospitalsResult");
			}
			return soapString;
		}
	}



	public static class webService_GetRouteWiseHospitalWasteCollectionList{

		private static final String SOAP_ACTION="http://tempuri.org/GetRouteWiseHospitalWasteCollectionList";
		private static final String METHOD_NAME="GetRouteWiseHospitalWasteCollectionList";
		public static String soapString;


		public static String callWebserviceCheck(int routID){

			SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.implicitTypes = true;
			envelope.dotNet=true;

			SoapObject requestSoap=new SoapObject(NAMESPACE, METHOD_NAME);

			requestSoap.addProperty("QRCodeContent", routID);

			envelope.setOutputSoapObject(requestSoap);

			HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalVariables.getServerURL(),60000);
			try{
				androidHttpTransport.call(SOAP_ACTION, envelope);
				if (androidHttpTransport.debug){
					Log.d("ws", androidHttpTransport.requestDump);
				}//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=0; isSuccess=false; errorMsg=Object reference not set to an instance of an object.,InnerEx:; }; }
				return deserializeSoap((SoapObject)envelope.bodyIn);
			}catch(Exception excp){
				excp.printStackTrace();
			}
			return null;
			//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=0; isSuccess=false; errorMsg= is not a valid value for Int32.,InnerEx:System.IndexOutOfRangeException: Index was outside the bounds of the array.
		}
		//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=1; isSuccess=true; errorMsg=All Records Saved Successfully; }; }
		public static String deserializeSoap(SoapObject paramObject){
			if(paramObject.hasProperty("GetAppCodeListResult")){
				//String SObject = (String) paramObject.getProperty("HelloWorldResult");
				soapString =  paramObject.getPropertyAsString("GetAppCodeListResult");
			}
			return soapString;
		}
	}

	public static class webService_GetHospitalWasteCollectionList{

		private static final String SOAP_ACTION="http://tempuri.org/GetHospitalWasteCollectionList";
		private static final String METHOD_NAME="GetHospitalWasteCollectionList";
		public static String soapString;


		public static SoapObject callWebserviceCheck(int docID){

			SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.implicitTypes = true;
			envelope.dotNet=true;

			SoapObject requestSoap=new SoapObject(NAMESPACE, METHOD_NAME);

			requestSoap.addProperty("doctorID", docID);

			envelope.setOutputSoapObject(requestSoap);

			HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalVariables.getServerURL(),60000);
			try{
				androidHttpTransport.call(SOAP_ACTION, envelope);
				if (androidHttpTransport.debug){
					Log.d("ws", androidHttpTransport.requestDump);
				}//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=0; isSuccess=false; errorMsg=Object reference not set to an instance of an object.,InnerEx:; }; }
				return (SoapObject)envelope.bodyIn;
			}catch(Exception excp){
				excp.printStackTrace();
			}
			return null;
			//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=0; isSuccess=false; errorMsg= is not a valid value for Int32.,InnerEx:System.IndexOutOfRangeException: Index was outside the bounds of the array.
		}
		//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=1; isSuccess=true; errorMsg=All Records Saved Successfully; }; }
		public static String deserializeSoap(SoapObject paramObject){
			if(paramObject.hasProperty("GetHospitalWasteCollectionListResult")){
				//String SObject = (String) paramObject.getProperty("HelloWorldResult");
				soapString =  paramObject.getPropertyAsString("GetHospitalWasteCollectionListResult");
			}
			return soapString;
		}
	}

	public static class webService_CheckValidBagManually{

		private static final String SOAP_ACTION="http://tempuri.org/CheckValidBagManually";
		private static final String METHOD_NAME="CheckValidBagManually";
		public static String soapString;


		public static SoapObject callWebserviceCheck(String docID){

			SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.implicitTypes = true;
			envelope.dotNet=true;

			SoapObject requestSoap=new SoapObject(NAMESPACE, METHOD_NAME);

			requestSoap.addProperty("jSONString", docID);

			envelope.setOutputSoapObject(requestSoap);

			HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalVariables.getServerURL(),60000);
			try{
				androidHttpTransport.call(SOAP_ACTION, envelope);
				if (androidHttpTransport.debug){
					Log.d("ws", androidHttpTransport.requestDump);
				}//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=0; isSuccess=false; errorMsg=Object reference not set to an instance of an object.,InnerEx:; }; }
				return (SoapObject)envelope.bodyIn;
			}catch(Exception excp){
				excp.printStackTrace();
			}
			return null;
			//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=0; isSuccess=false; errorMsg= is not a valid value for Int32.,InnerEx:System.IndexOutOfRangeException: Index was outside the bounds of the array.
		}
		//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=1; isSuccess=true; errorMsg=All Records Saved Successfully; }; }
		public static String deserializeSoap(SoapObject paramObject){
			if(paramObject.hasProperty("GetHospitalWasteCollectionListResult")){
				//String SObject = (String) paramObject.getProperty("HelloWorldResult");
				soapString =  paramObject.getPropertyAsString("GetHospitalWasteCollectionListResult");
			}
			return soapString;
		}
	}

	public static class webService_UpdateWastePickedUpStatusFromHospital{

		private static final String SOAP_ACTION="http://tempuri.org/UpdateWastePickedUpStatusFromHospital";
		private static final String METHOD_NAME="UpdateWastePickedUpStatusFromHospital";
		public static String soapString;

		public static String callWebserviceCheck(String cID,String mID){

			SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.implicitTypes = true;
			envelope.dotNet=true;

			SoapObject requestSoap=new SoapObject(NAMESPACE, METHOD_NAME);

			requestSoap.addProperty("wasteScanningAtHospitalDetailID", cID);
			requestSoap.addProperty("modifiedBy", mID);

			envelope.setOutputSoapObject(requestSoap);

			HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalVariables.getServerURL(),60000);
			try{
				androidHttpTransport.call(SOAP_ACTION, envelope);
				if (androidHttpTransport.debug){
					Log.d("ws", androidHttpTransport.requestDump);
				}//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=0; isSuccess=false; errorMsg=Object reference not set to an instance of an object.,InnerEx:; }; }
				return deserializeSoap((SoapObject)envelope.bodyIn);
			}catch(Exception excp){
				excp.printStackTrace();
			}
			return null;
		}
		public static String deserializeSoap(SoapObject paramObject){
			if(paramObject.hasProperty("UpdateWastePickedUpStatusFromHospitalResult")){
				soapString =  paramObject.getPropertyAsString("UpdateWastePickedUpStatusFromHospitalResult");
			}
			return soapString;
		}
	}

	public static class webService_UpdateBagWeightWastePickUpFromHospital{

		private static final String SOAP_ACTION="http://tempuri.org/UpdateBagWeightWastePickUpFromHospital";
		private static final String METHOD_NAME="UpdateBagWeightWastePickUpFromHospital";
		public static SoapObject soapObject;
		public static SoapObject soapObjectInner;
		public static SoapObject soapObjectInnerTable;
		public static String soapString;

		public static String callWebserviceCheck(int cID, float weight, String mID){

			SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.implicitTypes = true;
			envelope.dotNet=true;

			SoapObject requestSoap=new SoapObject(NAMESPACE, METHOD_NAME);

			requestSoap.addProperty("wasteScanningAtHospitalDetailID", cID);
			requestSoap.addProperty("weight", weight);
			requestSoap.addProperty("modifiedBy", mID);

			envelope.setOutputSoapObject(requestSoap);

			HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalVariables.getServerURL(),60000);
			try{
				androidHttpTransport.call(SOAP_ACTION, envelope);
				if (androidHttpTransport.debug){
					Log.d("ws", androidHttpTransport.requestDump);
				}//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=0; isSuccess=false; errorMsg=Object reference not set to an instance of an object.,InnerEx:; }; }
				return deserializeSoap((SoapObject)envelope.bodyIn);
			}catch(Exception excp){
				excp.printStackTrace();
			}
			return null;
		}
		public static String deserializeSoap(SoapObject paramObject){
			if(paramObject.hasProperty("UpdateBagWeightWastePickUpFromHospitalResult")){
				soapString =  paramObject.getPropertyAsString("UpdateBagWeightWastePickUpFromHospitalResult");
			}
			return soapString;
		}
	}
}