package com.example.garbagecollection.doctor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.garbagecollection.bmw.Base64;
import com.example.garbagecollection.bmw.CollectionAdapter;
import com.example.garbagecollection.bmw.CollectionDoctorAdapter;
import com.example.garbagecollection.bmw.GarbageDataStorage;
import com.example.garbagecollection.bmw.HomeActivity;
import com.example.garbagecollection.bmw.HospitalOne;
import com.example.garbagecollection.bmw.R;
import com.example.garbagecollection.bmw.WasteScanningAtHospitals;
import com.example.garbagecollection.bmw.WebService;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HospitalDoctor extends Activity{

    TextView text_hospital_name, text_isactive, text_scanned_Bags, text_qee_name;
    Button btn_ok,btn_doctor_scan;
    Float val_kg;
    int val_bag;
    String hospital_id;
    String hospital_data;
    private Pattern pattern;
    private Matcher matcher;
    boolean flagTop = false;
    String currentDateandTime;
    public static final String MyPREFERENCES = "MyPrefs" ;
    boolean sameBag = false;
    String isOpenOrClose;
    CollectionDoctorAdapter collAdapter;
    ListView list_collection;

    String ColorValue;
    EditText edt1,edt2;
    EditText edit_bagID;
    TableRow tableRow0, tableRow1;

    public static Float TotalWeight=0.0f,whiteWeight=0.0f,redWeight=0.0f,yellowWeight=0.0f,blueWeight=0.0f;
    public static int TotalBags = 0,redBag,blueBag,whiteBag,yellowBag;
    SharedPreferences sharedpreferences;
    SharedPreferences userDetails;
    ArrayList<WasteScanningAtHospitals> wasteCount = new ArrayList<WasteScanningAtHospitals>();
    public static int btn_bag_scanCode = 10;
    ArrayList<String>  bagArray = new ArrayList<String>();
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    Bundle bd;
    Button btn_manual_entry;
    String radioColor = null;

    //TextView one,two,three;
    Button btn_bag_scan;
    Button btn_color_ok, btn_color_cancel;

    @Override
    protected void onResume() {
        super.onResume();
        setTitle("Bags Collection");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("message", "This is my message to be reloaded");
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TotalBags = 0;
        TotalWeight=0.0f;
        whiteWeight=0.0f;
        redWeight=0.0f;
        yellowWeight=0.0f;
        blueWeight=0.0f;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.hospitel_doctor);

        pattern = Pattern.compile(EMAIL_PATTERN);
        btn_bag_scan = (Button)findViewById(R.id.btn_bag_scan);
        userDetails = getApplicationContext().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);

        text_qee_name = (TextView)findViewById(R.id.text_qee_name);
        text_hospital_name = (TextView)findViewById(R.id.text_hospital_name);
        text_isactive = (TextView)findViewById(R.id.text_isactive);
        text_scanned_Bags = (TextView)findViewById(R.id.text_scanned_Bags);
        btn_doctor_scan= (Button)findViewById(R.id.btn_doctor_scan);
        btn_ok = (Button)findViewById(R.id.btn_ok);
        list_collection = (ListView) findViewById(R.id.list_collection);
        btn_manual_entry = (Button) findViewById(R.id.btn_manual_entry);

        bd = getIntent().getExtras();
        bagArray.clear();
        wasteCount.clear();

        if(bd!=null){
            if(bd.getString("hospital_id")!=null) {
                isOpenOrClose = "open";
                flagTop = true;
                hospital_id = bd.getString("hospital_id");
                hospital_data = bd.getString("hospitalContent");

                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.putString("MembershipNo", hospital_id);
                editor.commit();
                text_hospital_name.setText(hospital_id);
                text_qee_name.setVisibility(View.GONE);
                btn_doctor_scan.setVisibility(View.GONE);

                text_hospital_name.setVisibility(View.VISIBLE);
                text_isactive.setVisibility(View.VISIBLE);

            }
        }

        btn_bag_scan.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isOpenOrClose.equalsIgnoreCase("open")){

                    new IntentIntegrator(HospitalDoctor.this).initiateScan(); // `this` is the current Activity


                    /*Intent i = new Intent();
                    i.setClass(HospitalDoctor.this, MainScreenDoctor.class);
                    i.putExtra("HospitalOne","HospitalOne");
                    startActivityForResult(i, btn_bag_scanCode);*/
                }else{
                    Toast.makeText(getApplicationContext(), "Please Take a Picture.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btn_ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if(wasteCount.size()>=1){
                    if(isOpenOrClose.equalsIgnoreCase("open")){

                        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                        val_kg = (float) 0.0;
                        val_bag = 0;

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(HospitalDoctor.this);
                        LayoutInflater inflater = HospitalDoctor.this.getLayoutInflater();
                        //final View dialogView = inflater.inflate(R.layout.dialog_sign, null);
                        //alertDialog.setView(dialogView);

                        alertDialog.setTitle("Confirm Submission");
                        //alertDialog.setMessage("Total Weight is ="+TotalWeight +" Kg \n"+"Total Bags is ="+TotalBags);

                        StringBuilder sb = new StringBuilder();

                        if (yellowWeight > 0.0f) {
                            final String localYellow = String.format("%.02f", yellowWeight);
                            sb.append("Total Yellow Weight: " + localYellow + "\n");
                        }
                        if (redWeight > 0.0f) {
                            final String localRed = String.format("%.02f", redWeight);
                            sb.append("Total Red Weight: " + localRed + "\n");
                        }
                        if (whiteWeight > 0.0f) {
                            final String localWhite = String.format("%.02f", whiteWeight);
                            sb.append("Total White Weight: " + localWhite + "\n");
                        }
                        if (blueWeight > 0.0f) {
                            final String localBlue = String.format("%.02f", blueWeight);
                            sb.append("Total Blue Weight: " + localBlue + "\n");
                        }
                        final String localTotalWeight = String.format("%.02f", TotalWeight);
                        alertDialog.setMessage(sb + "And Total Weight =" + localTotalWeight + " Kg \n" + "Total Bags =" + TotalBags);

                        alertDialog.setIcon(R.drawable.q_logo);
                        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {
                                // Write your code here to invoke YES event
                                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault());
                                final JSONArray jObjectOptionData1 = new JSONArray();
                                final JSONArray jObjectOptionDataManually1 = new JSONArray();

                                boolean flag1= false, flagManually = false;
                                currentDateandTime = sdf.format(new Date());
                                    try {
                                        /*dataJson.put("DoctorID", waste);
                                        dataJson.put("BagQRCodeDetalID", userDetails.getString("companyID", "1"));
                                        dataJson.put("QRCodeContent", userDetails.getString("branchID", "1"));
                                        dataJson.put("NoOfBags", TotalBags);
                                        dataJson.put("Weight", TotalWeight);
                                        dataJson.put("ScannedBy", userDetails.getString("employee_storeID", "1"));
                                        dataJson.put("ScannedDateTime", userDetails.getString("MembershipNo", "1"));
                                        dataJson.put("IsCollected", userDetails.getString("route_storeID", ""));*/

                                        for(int i =0; i<wasteCount.size();i++){
                                            JSONObject dataJson = new JSONObject();
                                            dataJson.put("DoctorID",wasteCount.get(i).getDoctorID());
                                            dataJson.put("NoOfBags","1");
                                            dataJson.put("Weight",wasteCount.get(i).getWeight());
                                            dataJson.put("ScannedBy","staff");
                                            dataJson.put("ScannedDateTime",wasteCount.get(i).getScannedDateTime());
                                            dataJson.put("IsCollected","false");

                                            if(wasteCount.get(i).getBagQRCodeDetalID() == "0" || wasteCount.get(i).getBagQRCodeDetalID() == null){
                                                dataJson.put("OperationName", "WasteScanningAtHospitalsManually");
                                                dataJson.put("ImageTitle", wasteCount.get(i).getImageTitle());
                                                dataJson.put("QEEID",wasteCount.get(i).getDoctorID());
                                                jObjectOptionDataManually1.put(dataJson);
                                                flagManually = true;
                                            }else{
                                                dataJson.put("OperationName", "WasteScanningAtHospitals");
                                                dataJson.put("BagQRCodeDetalID",wasteCount.get(i).getBagQRCodeDetalID());
                                                dataJson.put("QRCodeContent",wasteCount.get(i).getQRCodeContent());
                                                jObjectOptionData1.put(dataJson);
                                                flag1 = true;
                                            }
                                        }

                                        String jsonString = jObjectOptionData1.toString();
                                        String jsonStringManually = jObjectOptionDataManually1.toString();

                                        if (isConnectingToInternet()) {
                                            Log.i("Data", "== BMWBagsCollectionSummaryWS== "+jsonString);
                                            Toast.makeText(getApplicationContext(), "Submitting Data to Server!!!", Toast.LENGTH_SHORT).show();
                                            if(flag1)
                                                new BMWBagsCollectionSummaryWS().execute(jsonString);

                                            if(flagManually)
                                                new BMWBagsCollectionSummaryManuallyWS().execute(jsonStringManually);

                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                            }
                        });

                        // Showing Alert Message
                        alertDialog.show();

                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Minimum 1 bag is required.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btn_manual_entry.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final JSONArray jObjectOptionData1 = new JSONArray();
                //convert parameters into JSON object

                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(getApplicationContext());
                View promptsView = li.inflate(R.layout.prompt_edit, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HospitalDoctor.this);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
                final RadioGroup radioColorGroup = (RadioGroup) promptsView.findViewById(R.id.radioColor);
                radioColorGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        // find which radio button is selected
                        if(checkedId == R.id.radioBlue) {
                            radioColor = "BLUE";
                        } else if(checkedId == R.id.radioRed) {
                            radioColor = "RED";
                        } else if(checkedId == R.id.radioWhite) {
                            radioColor = "WHITE";
                        } else if(checkedId == R.id.radioYellow) {
                            radioColor = "YELLOW";
                        }
                    }

                });

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {

                                        if(radioColor!=null) {
                                            try {
                                                JSONObject data = new JSONObject();
                                                data.put("OperationName", "CheckValidBagManually");
                                                data.put("DoctorID", hospital_id);
                                                data.put("QEEID", hospital_id);
                                                data.put("BagQRCodeDetalID", 0);
                                                data.put("ImageTitle", radioColor+"-"+userInput.getText().toString());

                                                jObjectOptionData1.put(data);
                                                String abl = jObjectOptionData1.toString();

                                                if (isConnectingToInternet()) {
                                                    new WebServiceCheckValidBagManually(hospital_id, userInput.getText().toString(), radioColor).execute(abl);
                                                } else {
                                                    ColorValue = radioColor;
                                                    final String bagHeader = userInput.getText().toString();
                                                    sameBag = true;
                                                    String onlyID = userDetails.getString("MembershipNo", "1");
                                                    if (onlyID.equalsIgnoreCase(hospital_id)) {
                                                        //AlertDialog.Builder alertDialog = new AlertDialog.Builder(HospitalDoctor.this);
                                                        final Dialog dialogColor = new Dialog(HospitalDoctor.this);
                                                        dialogColor.setContentView(R.layout.dialog_color);


                                                        edt1 = (EditText) dialogColor.findViewById(R.id.edit_bag1);
                                                        edt2 = (EditText) dialogColor.findViewById(R.id.edit_kg_1);
                                                        btn_color_ok = (Button)dialogColor.findViewById(R.id.btn_color_ok);
                                                        btn_color_cancel = (Button)dialogColor.findViewById(R.id.btn_color_cancel);

                                                        edit_bagID = (EditText) dialogColor.findViewById(R.id.edit_kg_bagid);
                                                        edit_bagID.setText(""+ColorValue+"-"+bagHeader);
                                                        edit_bagID.setEnabled(false);

                                                        tableRow0 = (TableRow)dialogColor.findViewById(R.id.tableRow0);
                                                        tableRow1 = (TableRow)dialogColor.findViewById(R.id.tableRow1);
                                                        tableRow0.setVisibility(View.VISIBLE);
                                                        tableRow1.setVisibility(View.VISIBLE);


                                                        TableLayout tblRow = (TableLayout) dialogColor.findViewById(R.id.tableLayout1);

                                                        if (ColorValue.equalsIgnoreCase("BLUE")) {
                                                            tblRow.setBackgroundColor(getResources().getColor(R.color.blue));
                                                            ColorValue = "BLUE";
                                                        } else if (ColorValue.equalsIgnoreCase("RED")) {
                                                            tblRow.setBackgroundColor(getResources().getColor(R.color.red));
                                                            ColorValue = "RED";
                                                        } else if (ColorValue.equalsIgnoreCase("WHITE")) {
                                                            tblRow.setBackgroundColor(getResources().getColor(R.color.white));
                                                            ColorValue = "WHITE";
                                                        } else if (ColorValue.equalsIgnoreCase("YELLOW")) {
                                                            tblRow.setBackgroundColor(getResources().getColor(R.color.yellow));
                                                            ColorValue = "YELLOW";
                                                        } else {
                                                            Toast.makeText(getApplicationContext(), "Problem in Scanning, Please scan again.", Toast.LENGTH_LONG).show();
                                                        }
                                                        //mContent = (LinearLayout) dialogColor.findViewById(R.id.layout_sign);
                                                        dialogColor.setTitle("Add Details.");
                                                        btn_color_ok.setOnClickListener(new OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                if (edt2.getText().toString().length()>=1 && Float.parseFloat(edt2.getText().toString()) <= 999) {
                                                                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                                                                    if(!bagArray.contains(ColorValue+"-"+bagHeader)) {
                                                                        try {

                                                                            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault());
                                                                            String currentDateandTime = sdf.format(new Date());

                                                                            WasteScanningAtHospitals wasteObject = new WasteScanningAtHospitals();
                                                                            wasteObject.setDoctorID(hospital_id);
                                                                            //wasteObject.setBagQRCodeDetalID(QRBagCode);
                                                                            //wasteObject.setQRCodeContent(QRCodeValue);
                                                                            wasteObject.setImageTitle(ColorValue+"-"+bagHeader);
                                                                            wasteObject.setNoofBags("1");
                                                                            wasteObject.setWeight(edt2.getText().toString());
                                                                            wasteObject.setScannedBy("staff");
                                                                            wasteObject.setScannedDateTime(currentDateandTime);
                                                                            wasteObject.setIsCollected("false");

                                                                            wasteCount.add(wasteObject);
                                                                            bagArray.add(ColorValue+"-"+bagHeader);

                                                                            text_scanned_Bags.setText("Scanned Bags :" + wasteCount.size());

                                                                            collAdapter = new CollectionDoctorAdapter(getApplicationContext(), wasteCount);
                                                                            list_collection.setAdapter(collAdapter);

                                                                        } catch (Exception e) {
                                                                            e.printStackTrace();
                                                                        }

                                                                        Log.i("0=TotalWeight=", "==" + TotalWeight);
                                                                        Log.i("0=TotalBags=", "==" + TotalBags);

                                                                        if (edt1.getText().toString().length() == 0)
                                                                            edt1.setText("0");
                                                                        if (edt2.getText().toString().length() == 0)
                                                                            edt2.setText("0");

                                                                        if (ColorValue.equalsIgnoreCase("Blue")) {
                                                                            TotalWeight += Float.parseFloat(edt2.getText().toString());
                                                                            blueWeight += Float.parseFloat(edt2.getText().toString());
                                                                            blueBag = Integer.parseInt(edt1.getText().toString());
                                                                            TotalBags += blueBag;
                                                                        } else if (ColorValue.equalsIgnoreCase("Red")) {
                                                                            TotalWeight += Float.parseFloat(edt2.getText().toString());
                                                                            redWeight += Float.parseFloat(edt2.getText().toString());
                                                                            redBag = Integer.parseInt(edt1.getText().toString());
                                                                            TotalBags += redBag;
                                                                        } else if (ColorValue.equalsIgnoreCase("White")) {
                                                                            TotalWeight += Float.parseFloat(edt2.getText().toString());
                                                                            whiteWeight += Float.parseFloat(edt2.getText().toString());
                                                                            whiteBag = Integer.parseInt(edt1.getText().toString());
                                                                            TotalBags += whiteBag;
                                                                        } else if (ColorValue.equalsIgnoreCase("Yellow")) {
                                                                            TotalWeight += Float.parseFloat(edt2.getText().toString());
                                                                            yellowWeight += Float.parseFloat(edt2.getText().toString());
                                                                            yellowBag = Integer.parseInt(edt1.getText().toString());
                                                                            TotalBags += yellowBag;
                                                                        }else{
                                                                            Toast.makeText(getApplicationContext(), "Problem in Color bag, Please scan again.", Toast.LENGTH_LONG).show();
                                                                        }
                                                                        Log.i("=TotalWeight=", "==" + TotalWeight);
                                                                        Log.i("=TotalBags=", "==" + TotalBags);

                                                                    } else {
                                                                        Toast.makeText(getApplicationContext(), "Bag already scanned.", Toast.LENGTH_SHORT).show();
                                                                    }

                                                                } else {
                                                                    Toast.makeText(getApplicationContext(), "વજન ચકાસો.", Toast.LENGTH_SHORT).show();
                                                                }
                                                                dialogColor.dismiss();
                                                            }
                                                        });
                                                        btn_color_cancel.setOnClickListener(new OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                // Write your code here to invoke NO event
                                                                Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                                                                dialogColor.dismiss();
                                                            }
                                                        });
                                                        dialogColor.show();
                                                    } else {
                                                        Toast.makeText(getApplicationContext(), "આ ડૉક્ટર ને ફાળવેલી બેગ નથી.", Toast.LENGTH_SHORT).show();
                                                    }

                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }else{
                                            Toast.makeText(getApplicationContext(), "Which Color?", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();
            }
        });
    }


    public class WebServiceCheckValidBagManually extends AsyncTask<String, String, SoapObject> {

        private String resp;
        SoapObject soap;
        private ProgressDialog dialog;
        String memberID, bagHeader;
        //String QRCodeValue, QRBagCode;

        public WebServiceCheckValidBagManually(String val,String id, String colorValue) {
            memberID = val;
            bagHeader = id;
            ColorValue = colorValue;
            //QRCodeValue = QRCode;
            //QRBagCode = QRBag;
        }


        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(HospitalDoctor.this);
            dialog.setMessage("Please wait...");
            dialog.show();
        }

        @Override
        protected SoapObject doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()
            try {
                Log.i("=params[0]", "== WebserviceGetHospitalWasteCollectionList[0]" + params[0]);
                soap = WebService.webService_CheckValidBagManually.callWebserviceCheck(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return soap;
        }

        @Override
        protected void onPostExecute(SoapObject soapObject) {

            if (soapObject != null) {
                SoapObject myResult = (SoapObject) soapObject.getProperty("CheckValidBagManuallyResult");

                if (soapObject.getProperty("CheckValidBagManuallyResult").toString().equals("0")) {

                    sameBag = true;
                    //REQUEST 477|Y|QEEVADODARA|2|13021800001,DrName:DR.ABC,HospName:Trial Hosp,Area:,Pin:000000,Phone:0,Mobile:0,GPCBID:0
                    //RESPONSE CheckForUsedBagQRCodeResponse{CheckForUsedBagQRCodeResult=0; }
                    String onlyID = userDetails.getString("MembershipNo", "1");
                    if (onlyID.equalsIgnoreCase(memberID)) {
                        //AlertDialog.Builder alertDialog = new AlertDialog.Builder(HospitalDoctor.this);
                        final Dialog dialogColor = new Dialog(HospitalDoctor.this);
                        dialogColor.setContentView(R.layout.dialog_color);


                        edt1 = (EditText) dialogColor.findViewById(R.id.edit_bag1);
                        edt2 = (EditText) dialogColor.findViewById(R.id.edit_kg_1);
                        btn_color_ok = (Button)dialogColor.findViewById(R.id.btn_color_ok);
                        btn_color_cancel = (Button)dialogColor.findViewById(R.id.btn_color_cancel);

                        edit_bagID = (EditText) dialogColor.findViewById(R.id.edit_kg_bagid);
                        edit_bagID.setText(""+ColorValue+"-"+bagHeader);
                        edit_bagID.setEnabled(false);

                        tableRow0 = (TableRow)dialogColor.findViewById(R.id.tableRow0);
                        tableRow1 = (TableRow)dialogColor.findViewById(R.id.tableRow1);
                        tableRow0.setVisibility(View.VISIBLE);
                        tableRow1.setVisibility(View.VISIBLE);


                        TableLayout tblRow = (TableLayout) dialogColor.findViewById(R.id.tableLayout1);

                        if (ColorValue.equalsIgnoreCase("BLUE")) {
                            tblRow.setBackgroundColor(getResources().getColor(R.color.blue));
                            ColorValue = "BLUE";
                        } else if (ColorValue.equalsIgnoreCase("RED")) {
                            tblRow.setBackgroundColor(getResources().getColor(R.color.red));
                            ColorValue = "RED";
                        } else if (ColorValue.equalsIgnoreCase("WHITE")) {
                            tblRow.setBackgroundColor(getResources().getColor(R.color.white));
                            ColorValue = "WHITE";
                        } else if (ColorValue.equalsIgnoreCase("YELLOW")) {
                            tblRow.setBackgroundColor(getResources().getColor(R.color.yellow));
                            ColorValue = "YELLOW";
                        } else {
                            Toast.makeText(getApplicationContext(), "Problem in Scanning, Please scan again.", Toast.LENGTH_LONG).show();
                        }
                        //mContent = (LinearLayout) dialogColor.findViewById(R.id.layout_sign);
                        dialogColor.setTitle("Add Details.");
                        btn_color_ok.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (edt2.getText().toString().length()>=1 && Float.parseFloat(edt2.getText().toString()) <= 999) {
                                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                                    if(!bagArray.contains(ColorValue+"-"+bagHeader)) {
                                        try {

                                            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault());
                                            String currentDateandTime = sdf.format(new Date());

                                            WasteScanningAtHospitals wasteObject = new WasteScanningAtHospitals();
                                            wasteObject.setDoctorID(memberID);
                                            //wasteObject.setBagQRCodeDetalID(QRBagCode);
                                            //wasteObject.setQRCodeContent(QRCodeValue);
                                            wasteObject.setImageTitle(ColorValue+"-"+bagHeader);
                                            wasteObject.setNoofBags("1");
                                            wasteObject.setWeight(edt2.getText().toString());
                                            wasteObject.setScannedBy("staff");
                                            wasteObject.setScannedDateTime(currentDateandTime);
                                            wasteObject.setIsCollected("false");

                                            wasteCount.add(wasteObject);
                                            bagArray.add(ColorValue+"-"+bagHeader);

                                            text_scanned_Bags.setText("Scanned Bags :" + wasteCount.size());

                                            collAdapter = new CollectionDoctorAdapter(getApplicationContext(), wasteCount);
                                            list_collection.setAdapter(collAdapter);

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        Log.i("0=TotalWeight=", "==" + TotalWeight);
                                        Log.i("0=TotalBags=", "==" + TotalBags);

                                        if (edt1.getText().toString().length() == 0)
                                            edt1.setText("0");
                                        if (edt2.getText().toString().length() == 0)
                                            edt2.setText("0");

                                        if (ColorValue.equalsIgnoreCase("Blue")) {
                                            TotalWeight += Float.parseFloat(edt2.getText().toString());
                                            blueWeight += Float.parseFloat(edt2.getText().toString());
                                            blueBag = Integer.parseInt(edt1.getText().toString());
                                            TotalBags += blueBag;
                                        } else if (ColorValue.equalsIgnoreCase("Red")) {
                                            TotalWeight += Float.parseFloat(edt2.getText().toString());
                                            redWeight += Float.parseFloat(edt2.getText().toString());
                                            redBag = Integer.parseInt(edt1.getText().toString());
                                            TotalBags += redBag;
                                        } else if (ColorValue.equalsIgnoreCase("White")) {
                                            TotalWeight += Float.parseFloat(edt2.getText().toString());
                                            whiteWeight += Float.parseFloat(edt2.getText().toString());
                                            whiteBag = Integer.parseInt(edt1.getText().toString());
                                            TotalBags += whiteBag;
                                        } else if (ColorValue.equalsIgnoreCase("Yellow")) {
                                            TotalWeight += Float.parseFloat(edt2.getText().toString());
                                            yellowWeight += Float.parseFloat(edt2.getText().toString());
                                            yellowBag = Integer.parseInt(edt1.getText().toString());
                                            TotalBags += yellowBag;
                                        }else{
                                            Toast.makeText(getApplicationContext(), "Problem in Color bag, Please scan again.", Toast.LENGTH_LONG).show();
                                        }
                                        Log.i("=TotalWeight=", "==" + TotalWeight);
                                        Log.i("=TotalBags=", "==" + TotalBags);

                                    } else {
                                        Toast.makeText(getApplicationContext(), "Bag already scanned.", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    Toast.makeText(getApplicationContext(), "વજન ચકાસો.", Toast.LENGTH_SHORT).show();
                                }
                                dialogColor.dismiss();
                            }
                        });
                        btn_color_cancel.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Write your code here to invoke NO event
                                Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                                dialogColor.dismiss();
                            }
                        });
                        dialogColor.show();
                    } else {
                        Toast.makeText(getApplicationContext(), "આ ડૉક્ટર ને ફાળવેલી બેગ નથી.", Toast.LENGTH_SHORT).show();
                    }
                } /*else if (soapObject.getProperty("CheckValidBagManuallyResult").toString().equals("0")) {
                    dialog.cancel();
                    sameBag = false;
                    Toast.makeText(getApplicationContext(), "Bag is collected", Toast.LENGTH_SHORT).show();
                }*/ else {
                    dialog.cancel();
                    sameBag = false;
                    Toast.makeText(getApplicationContext(), "Bag issue", Toast.LENGTH_SHORT).show();
                }
            }
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    private class CheckForUsedBagQRCodeWS extends AsyncTask<String, String, SoapObject> {

        SoapObject soap;
        private ProgressDialog dialog;
        String memberID;
        String QRCodeValue, QRBagCode;

        public CheckForUsedBagQRCodeWS(String colorValue,String val, String QRCode, String QRBag) {
            ColorValue = colorValue;
            memberID = val;
            QRCodeValue = QRCode;
            QRBagCode = QRBag;
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(HospitalDoctor.this);
            dialog.setMessage("Please wait...");
            dialog.show();
        }

        @Override
        protected SoapObject doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()
            try {
                Log.i("Data", "CheckForUsedBagQRCode=="+QRBagCode);
                soap = WebService.webService_CheckForUsedBagQRCode.callWebservice(getApplicationContext(), QRBagCode);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return soap;
        }

        @Override
        protected void onPostExecute(SoapObject soapObject) {
            Log.i("","==myResultCheckForUsedBagQRCode "+soapObject);

            if(soapObject!=null && soapObject.hasProperty("CheckForUsedBagQRCodeResult")){
                if(soapObject.getProperty("CheckForUsedBagQRCodeResult").toString().equals("0")){
                        sameBag = true;
                        //REQUEST 477|Y|QEEVADODARA|2|13021800001,DrName:DR.ABC,HospName:Trial Hosp,Area:,Pin:000000,Phone:0,Mobile:0,GPCBID:0
                        //RESPONSE CheckForUsedBagQRCodeResponse{CheckForUsedBagQRCodeResult=0; }
                        String onlyID = userDetails.getString("MembershipNo", "1");
                        if (onlyID.equalsIgnoreCase(memberID)) {
                            //AlertDialog.Builder alertDialog = new AlertDialog.Builder(HospitalDoctor.this);
                            final Dialog dialogColor = new Dialog(HospitalDoctor.this);
                            dialogColor.setContentView(R.layout.dialog_color);

                            //LayoutInflater inflater = HospitalDoctor.this.getLayoutInflater();
                            //final View dialogColor = inflater.inflate(R.layout.dialog_color, null);
                            //alertDialog.setView(dialogColor);

                            edt1 = (EditText) dialogColor.findViewById(R.id.edit_bag1);
                            edt2 = (EditText) dialogColor.findViewById(R.id.edit_kg_1);
                            btn_color_ok = (Button)dialogColor.findViewById(R.id.btn_color_ok);
                            btn_color_cancel = (Button)dialogColor.findViewById(R.id.btn_color_cancel);

                            /*one = (TextView) dialogColor.findViewById(R.id.text_enter_bags);
                            two = (TextView) dialogColor.findViewById(R.id.text_enter_kg);


                            Typeface font = Typeface.createFromAsset(getAssets(), "SHRUTI.TTF");
                            one.setTypeface(font);
                            one.setTypeface(one.getTypeface(), Typeface.BOLD);
                            one.setText("      ભરેલી બેગ લેવાની નંગ       ");
                            two.setTypeface(font);
                            two.setTypeface(two.getTypeface(), Typeface.BOLD);
                            two.setText("       લીધેલી બેગ વજન kg       ");*/

                            TableLayout tblRow = (TableLayout) dialogColor.findViewById(R.id.tableLayout1);

                            if (ColorValue.equalsIgnoreCase("BLUE")) {
                                tblRow.setBackgroundColor(getResources().getColor(R.color.blue));
                                ColorValue = "Blue";
                            } else if (ColorValue.equalsIgnoreCase("RED")) {
                                tblRow.setBackgroundColor(getResources().getColor(R.color.red));
                                ColorValue = "Red";
                            } else if (ColorValue.equalsIgnoreCase("WHITE")) {
                                tblRow.setBackgroundColor(getResources().getColor(R.color.white));
                                ColorValue = "White";
                            } else if (ColorValue.equalsIgnoreCase("YELLOW")) {
                                tblRow.setBackgroundColor(getResources().getColor(R.color.yellow));
                                ColorValue = "Yellow";
                            } else {
                                Toast.makeText(getApplicationContext(), "Problem in Scanning, Please scan again.", Toast.LENGTH_LONG).show();
                            }
                            //mContent = (LinearLayout) dialogColor.findViewById(R.id.layout_sign);
                            dialogColor.setTitle("Add Details.");
                            btn_color_ok.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (Float.parseFloat(edt2.getText().toString()) <= 999) {
                                        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                                        try {

                                            /*if(bagArray.contains(QRBagCode)){
                                                Toast.makeText(getApplicationContext(), "Contains", Toast.LENGTH_LONG).show();
                                            }else{
                                                Toast.makeText(getApplicationContext(), "Does not Contain", Toast.LENGTH_LONG).show();
                                            }*/

                                            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault());
                                            String currentDateandTime = sdf.format(new Date());

                                            WasteScanningAtHospitals wasteObject = new WasteScanningAtHospitals();
                                            wasteObject.setDoctorID(memberID);
                                            wasteObject.setQRCodeContent(QRCodeValue);
                                            wasteObject.setBagQRCodeDetalID(QRBagCode);
                                            wasteObject.setNoofBags("1");
                                            wasteObject.setWeight(edt2.getText().toString());
                                            wasteObject.setScannedBy("staff");
                                            wasteObject.setScannedDateTime(currentDateandTime);
                                            wasteObject.setIsCollected("false");

                                            wasteCount.add(wasteObject);
                                            bagArray.add(QRBagCode);

                                            text_scanned_Bags.setText("Scanned Bags :" + wasteCount.size());


                                            collAdapter = new CollectionDoctorAdapter(getApplicationContext(), wasteCount);
                                            list_collection.setAdapter(collAdapter);

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        Log.i("0=TotalWeight=", "==" + TotalWeight);
                                        Log.i("0=TotalBags=", "==" + TotalBags);

                                        if (edt1.getText().toString().length() == 0)
                                            edt1.setText("0");
                                        if (edt2.getText().toString().length() == 0)
                                            edt2.setText("0");

                                        if (ColorValue.equalsIgnoreCase("Blue")) {
                                            TotalWeight += Float.parseFloat(edt2.getText().toString());
                                            blueWeight += Float.parseFloat(edt2.getText().toString());
                                            blueBag = Integer.parseInt(edt1.getText().toString());
                                            TotalBags += blueBag;
                                        } else if (ColorValue.equalsIgnoreCase("Red")) {
                                            TotalWeight += Float.parseFloat(edt2.getText().toString());
                                            redWeight += Float.parseFloat(edt2.getText().toString());
                                            redBag = Integer.parseInt(edt1.getText().toString());
                                            TotalBags += redBag;
                                        } else if (ColorValue.equalsIgnoreCase("White")) {
                                            TotalWeight += Float.parseFloat(edt2.getText().toString());
                                            whiteWeight += Float.parseFloat(edt2.getText().toString());
                                            whiteBag = Integer.parseInt(edt1.getText().toString());
                                            TotalBags += whiteBag;
                                        } else if (ColorValue.equalsIgnoreCase("Yellow")) {
                                            TotalWeight += Float.parseFloat(edt2.getText().toString());
                                            yellowWeight += Float.parseFloat(edt2.getText().toString());
                                            yellowBag = Integer.parseInt(edt1.getText().toString());
                                            TotalBags += yellowBag;
                                        }else{
                                            Toast.makeText(getApplicationContext(), "Problem in Color bag, Please scan again.", Toast.LENGTH_LONG).show();
                                        }
                                        Log.i("=TotalWeight=", "==" + TotalWeight);
                                        Log.i("=TotalBags=", "==" + TotalBags);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "વજન ચકાસો.", Toast.LENGTH_SHORT).show();
                                    }
                                    dialogColor.dismiss();
                                }
                            });
                            btn_color_cancel.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // Write your code here to invoke NO event
                                    Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                                    dialogColor.dismiss();
                                }
                            });
                            dialogColor.show();
                        } else {
                            Toast.makeText(getApplicationContext(), "આ ડૉક્ટર ને ફાળવેલી બેગ નથી.", Toast.LENGTH_SHORT).show();
                        }
                }else{
                    sameBag = false;
                    Toast.makeText(getApplicationContext(),"બેગ અગાઉં લેવાયેલ છે.",Toast.LENGTH_SHORT).show();
                }
            }else{
                sameBag = false;
                Toast.makeText(getApplicationContext(),"Problem in CheckForUsedBagQRCodeResponseResult",Toast.LENGTH_SHORT).show();
            }
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    private class BMWBagsCollectionSummaryWS extends AsyncTask<String, String, String> {

        String soap;
        private ProgressDialog dialog;

        public BMWBagsCollectionSummaryWS() {
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(HospitalDoctor.this);
            dialog.setMessage("Please wait...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()
            try {
                soap = WebService.webService_WasteScanningAtHospitals.callWebserviceCheck(getApplicationContext(), params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return soap;
        }

        @Override
        protected void onPostExecute(String soapObject) {
            Log.i("","==myResult"+soapObject);
            try {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }

            } catch (Exception e) {

            } finally {
                finish();
            }
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }

    }
    private class BMWBagsCollectionSummaryManuallyWS extends AsyncTask<String, String, String> {

        String soap;
        private ProgressDialog dialog;

        public BMWBagsCollectionSummaryManuallyWS() {
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(HospitalDoctor.this);
            dialog.setMessage("Please wait...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()
            try {
                soap = WebService.webService_WasteScanningAtHospitalsManually.callWebserviceCheck(getApplicationContext(), params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return soap;
        }

        @Override
        protected void onPostExecute(String soapObject) {
            Log.i("","==myResult"+soapObject);
            try {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }

            } catch (Exception e) {

            } finally {
                finish();
            }
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }

    }

    public boolean validate(final String hex) {

        matcher = pattern.matcher(hex);
        return matcher.matches();

    }
    /**
     * Getting the Path of the image selected
     *
     * @param uri : Getting uri from OnActivityResult()
     * @return path : Path of the string from SD Card.
     */
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = this.managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    /**
     * Decoding the file and returning Bitmap's Object
     * @param file
     * @return Bitmap
     */
    private static Bitmap decodeFile(File file) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(file), null, o);

            // The new size we want to scale to
            final int REQUIRED_SIZE = 150;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            o.inJustDecodeBounds = false;
            return BitmapFactory.decodeStream(new FileInputStream(file), null, o2);
        } catch (FileNotFoundException e) {
        }
        return null;
    }

    /**
     * Used for converting the Bitmap to String base64
     * @param bit
     *            : Bitmap selected by User
     * @return Base64 in String
     */
    public String getBase64Value(Bitmap bit) {
        String sBase64Value = null;
        byte[] byteObj = null;
        if (bit != null) {
            byteObj = getBytesFromBitmap(bit);
            sBase64Value = Base64.encodeBytes(byteObj);
        }
        return sBase64Value;
    }

    /**
     *Used for converting bitmap to byte Array
     * @param bitmap
     * @return byte[]
     */
    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, 50, stream);
        return stream.toByteArray();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        //if(requestCode == btn_bag_scanCode && data!=null){ //{who=null, request=2, result=0, data=null}
                //477|Y|QEEVADODARA|2|13021800001,DrName:DR.ABC,HospName:Trial Hosp,Area:,Pin:000000,Phone:0,Mobile:0,GPCBID:0
                //YELLOW-04071800001-HSNNA390023GJNA111|25

                //String fullColorString = data.getStringExtra("ColorValue");
                //final String[] tokens = data.getStringExtra("ColorValue").split(Pattern.quote("|"));
                if (result.getContents() != null) {
                    final String[] tokens = result.getContents().split(Pattern.quote("|"));
                        final String[] colorToken = tokens[0].split(Pattern.quote("-"));
                        final String membershipFullNo = tokens[0].substring(tokens[0].length()-5);
                        final String[] part = membershipFullNo.split("(?<=\\D)(?=\\d)");
                        System.out.println(part[0]);
                        System.out.println(part[1]);
                        String onlyID = userDetails.getString("MembershipNo", "1");

                        if(isConnectingToInternet()) {
                            if(!bagArray.contains(tokens[1])) {
                                new CheckForUsedBagQRCodeWS(colorToken[0], onlyID, result.getContents(), tokens[1]).execute(); //MembershipNo
                            } else {
                                Toast.makeText(getApplicationContext(), "Bag already scanned.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Internet not available !!!", Toast.LENGTH_SHORT).show();
                        }
                } else {
                    Toast.makeText(getApplicationContext(), "Error Occured!!!", Toast.LENGTH_SHORT).show();
                }
        //}
    }
    public boolean isConnectingToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager) HospitalDoctor.this.getSystemService(Context.CONNECTIVITY_SERVICE);
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