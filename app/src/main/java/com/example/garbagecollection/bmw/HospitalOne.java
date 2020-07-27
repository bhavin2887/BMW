package com.example.garbagecollection.bmw;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

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
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HospitalOne extends Activity {

    EditText edit_hospital_name;
    TextView text_hospital_name, text_isactive, text_qee_name;
    Button btn_manual_entry, btn_ok, btn_doctor_scan;
    String hospital_id;
    String email_id;
    private Pattern pattern;
    private Matcher matcher;
    boolean flagTop = false;
    String currentDateandTime;
    public static final String MyPREFERENCES = "MyPrefs";
    String valBase64, valBase64Sign = null;
    boolean signBool = false;
    Bitmap bitm;
    LocationManager manager;
    boolean isGPS, sameBag = false, checkBoxAndScan = false;
    GPSTracker gps;
    double lat = 0;
    double lon = 0;
    String isOpenOrClose;
    private Bitmap mBitmap;
    LinearLayout mContent;

    signature mSignature;
    View mView;
    String ColorValue;
    boolean onlyOneVisit = true;
    EditText edt2, edit_bagID;
    TableRow tableRow0, tableRow1;
    CheckBox cb;
    ListView list_collection;
    SharedPreferences sharedpreferences;
    SharedPreferences userDetails;
    CollectionAdapter collAdapter;
    ArrayList<WasteScanningAtHospitals> waCount;
    ArrayList<String> bagsArray = new ArrayList<String>();
    ImageView img_photo;
    TextView text_select;
    String radioColor = null;

    /**
     * Database object for saving data of device details into Database
     */
    private GarbageDataStorage garbageDataStorageObject = new GarbageDataStorage(this);

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    Bundle bd;
    ///TextView one, two, three;
    Button btn_color_ok, btn_color_cancel;
    Button btn_camera, btn_bag_scan;
    boolean camera_flag = false;

    double TotalWeight = 0.00, whiteWeight = 0.00, redWeight = 0.00, yellowWeight = 0.00, blueWeight = 0.00;
    int TotalBags = 0, redBag = 0, blueBag = 0, whiteBag = 0, yellowBag = 0;
    int TotalRedBag = 0, TotalBlueBag = 0, TotalWhiteBag = 0, TotalYellowBag = 0;

    private Handler handler = new Handler();
    private Runnable r;

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
    public void onBackPressed() {
        if (true) {
            if (text_isactive.getText().toString().equalsIgnoreCase("Is Not-Active")) {
                super.onBackPressed();
            } else {
                AlertDialog alertDialog = new AlertDialog.Builder(HospitalOne.this).create();
                alertDialog.setTitle("Please submit the data.");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        });
                alertDialog.show();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TotalBags = 0;
        TotalWeight = 0.0;
        whiteWeight = 0.0;
        redWeight = 0.0;
        yellowWeight = 0.0;
        blueWeight = 0.0;
        redBag = 0;
        blueBag = 0;
        whiteBag = 0;
        yellowBag = 0;
        TotalRedBag = 0;
        TotalBlueBag = 0;
        TotalWhiteBag = 0;
        TotalYellowBag = 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hospitel_one);

        pattern = Pattern.compile(EMAIL_PATTERN);
        btn_camera = (Button) findViewById(R.id.btn_camera);
        btn_bag_scan = (Button) findViewById(R.id.btn_bag_scan);
        userDetails = getApplicationContext().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        cb = (CheckBox) findViewById(R.id.check_bags);
        edit_hospital_name = (EditText) findViewById(R.id.edit_hospital_name);

        text_qee_name = (TextView) findViewById(R.id.text_qee_name);
        text_hospital_name = (TextView) findViewById(R.id.text_hospital_name);
        text_isactive = (TextView) findViewById(R.id.text_isactive);
        btn_doctor_scan = (Button) findViewById(R.id.btn_doctor_scan);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_manual_entry = (Button) findViewById(R.id.btn_manual_entry);
        list_collection = (ListView) findViewById(R.id.list_collection);
        text_select = (TextView) findViewById(R.id.text_select);

        //img_photo = (ImageView) findViewById(R.id.img_capture4);

        bd = getIntent().getExtras();
        bagsArray.clear();

        if (bd != null) {
            if (bd.getString("hospital_id") != null) {
                isOpenOrClose = "open";
                flagTop = true;
                hospital_id = bd.getString("hospital_id");
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.putString("MembershipNo", hospital_id);
                editor.commit();
                text_hospital_name.setText(hospital_id);
                edit_hospital_name.setVisibility(View.GONE);
                btn_camera.setVisibility(View.GONE);
                text_qee_name.setVisibility(View.GONE);
                btn_doctor_scan.setVisibility(View.GONE);

                text_hospital_name.setVisibility(View.VISIBLE);
                text_isactive.setVisibility(View.VISIBLE);

                if (isConnectingToInternet()) {
                    new WebserviceCheckHospital().execute(hospital_id);
                } else {
                    //callDailyWS();
                    Toast.makeText(getApplicationContext(), "Please check your Internet Connection.", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            isOpenOrClose = "close";
            edit_hospital_name.setSelection(edit_hospital_name.getText().length());
            btn_camera.setVisibility(View.VISIBLE);
            edit_hospital_name.setVisibility(View.VISIBLE);
            text_qee_name.setVisibility(View.VISIBLE);
            btn_doctor_scan.setVisibility(View.VISIBLE);

            if (isConnectingToInternet()) {
                btn_ok.setEnabled(false);
            } else {
                btn_ok.setEnabled(true);
            }

            text_hospital_name.setVisibility(View.GONE);
            text_isactive.setVisibility(View.GONE);
        }

        btn_manual_entry.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final JSONArray jObjectOptionData1 = new JSONArray();
                //convert parameters into JSON object

                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(getApplicationContext());
                View promptsView = li.inflate(R.layout.prompt_edit, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HospitalOne.this);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);
                radioColor = "";
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
                                    public void onClick(final DialogInterface dialog,int id) {


                                        if(!radioColor.equalsIgnoreCase("") && userInput.getText().length()>=3) {
                                            try {
                                                JSONObject data = new JSONObject();
                                                data.put("OperationName", "CheckValidBagManually");
                                                data.put("DoctorID", hospital_id);
                                                data.put("QEEID", hospital_id);
                                                data.put("BagQRCodeDetalID", 0);
                                                data.put("ImageTitle", radioColor+"-"+userInput.getText().toString());

                                                jObjectOptionData1.put(data);

                                                String abl = jObjectOptionData1.toString();
                                                final String  bag_id;
                                                if (isConnectingToInternet()) {
                                                    new WebServiceCheckValidBagManually(hospital_id, userInput.getText().toString(), radioColor).execute(abl);
                                                } else {
                                                    bag_id = userInput.getText().toString();
                                                    String color_param = radioColor;

                                                        sameBag = true;
                                                        String onlyID = userDetails.getString("MembershipNo", "1");
                                                        final Dialog alertDialog = new Dialog(HospitalOne.this);
                                                        LayoutInflater inflater = HospitalOne.this.getLayoutInflater();
                                                        //final View dialogColor = inflater.inflate(R.layout.dialog_color, null);
                                                        alertDialog.setContentView(R.layout.dialog_color);
                                                        //alertDialog.setView(dialogColor);

                                                        edt2 = (EditText) alertDialog.findViewById(R.id.edit_kg_1);
                                                        edit_bagID = (EditText) alertDialog.findViewById(R.id.edit_kg_bagid);
                                                        edit_bagID.setText(""+bag_id);

                                                        tableRow0 = (TableRow)alertDialog.findViewById(R.id.tableRow0);
                                                        tableRow1 = (TableRow)alertDialog.findViewById(R.id.tableRow1);
                                                        tableRow0.setVisibility(View.VISIBLE);
                                                        tableRow1.setVisibility(View.VISIBLE);

                                                        btn_color_ok = (Button) alertDialog.findViewById(R.id.btn_color_ok);
                                                        btn_color_cancel = (Button) alertDialog.findViewById(R.id.btn_color_cancel);

                                                        TableLayout tblRow = (TableLayout) alertDialog.findViewById(R.id.tableLayout1);
                                                        if (color_param.equalsIgnoreCase("BLUE")) {
                                                            tblRow.setBackgroundColor(getResources().getColor(R.color.blue));
                                                            color_param = "BLUE";
                                                        } else if (color_param.equalsIgnoreCase("RED")) {
                                                            tblRow.setBackgroundColor(getResources().getColor(R.color.red));
                                                            color_param = "RED";
                                                        } else if (color_param.equalsIgnoreCase("WHITE")) {
                                                            tblRow.setBackgroundColor(getResources().getColor(R.color.white));
                                                            color_param = "WHITE";
                                                        } else if (color_param.equalsIgnoreCase("YELLOW")) {
                                                            tblRow.setBackgroundColor(getResources().getColor(R.color.yellow));
                                                            color_param = "YELLOW";
                                                        } else {
                                                            Toast.makeText(getApplicationContext(), "Problem in Scanning, Please scan again.", Toast.LENGTH_LONG).show();
                                                        }
                                                        mContent = (LinearLayout) alertDialog.findViewById(R.id.layout_sign);
                                                        alertDialog.setTitle("Add Bag Weight.");
                                                        alertDialog.setCancelable(false);
                                                    final String finalColor_param = color_param;
                                                    btn_color_ok.setOnClickListener(new OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {

                                                                if (edt2.getText().length() != 0) {
                                                                    if (Double.parseDouble(edt2.getText().toString()) <= 999) {
                                                                        if (sameBag) {
                                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                                            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                                                                            //callDailyWS();

                                                                            Log.i("0=TotalWeight=", "==" + TotalWeight);
                                                                            Log.i("0=TotalBags=", "==" + TotalBags);

                                                                            if (finalColor_param.equalsIgnoreCase("Blue")) {
                                                                                TotalWeight += Double.parseDouble(edt2.getText().toString());
                                                                                blueWeight += Double.parseDouble(edt2.getText().toString());
                                                                                blueBag = 1;//Integer.parseInt(edt1.getText().toString());
                                                                                TotalBlueBag += blueBag;
                                                                                TotalBags += blueBag;
                                                                            } else if (finalColor_param.equalsIgnoreCase("Red")) {
                                                                                TotalWeight += Double.parseDouble(edt2.getText().toString());
                                                                                redWeight += Double.parseDouble(edt2.getText().toString());
                                                                                redBag = 1;//Integer.parseInt(edt1.getText().toString());
                                                                                TotalRedBag += redBag;
                                                                                TotalBags += redBag;
                                                                            } else if (finalColor_param.equalsIgnoreCase("White")) {
                                                                                TotalWeight += Double.parseDouble(edt2.getText().toString());
                                                                                whiteWeight += Double.parseDouble(edt2.getText().toString());
                                                                                whiteBag = 1;//Integer.parseInt(edt1.getText().toString());
                                                                                TotalWhiteBag += whiteBag;
                                                                                TotalBags += whiteBag;
                                                                            } else if (finalColor_param.equalsIgnoreCase("Yellow")) {
                                                                                TotalWeight += Double.parseDouble(edt2.getText().toString());
                                                                                yellowWeight += Double.parseDouble(edt2.getText().toString());
                                                                                yellowBag = 1;//Integer.parseInt(edt1.getText().toString());
                                                                                TotalYellowBag += yellowBag;
                                                                                TotalBags += yellowBag;
                                                                            } else {
                                                                                Toast.makeText(getApplicationContext(), "Problem in bagscanning", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                            Log.i("=TotalWeight=", "==" + TotalWeight);
                                                                            Log.i("=TotalBags=", "==" + TotalBags);

                                                                            try {

                                                                                final JSONArray jObjectOptionData1 = new JSONArray();

                                                                                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault());
                                                                                String currentDateandTime = sdf.format(new Date());

                                                                                JSONObject data = new JSONObject();
                                                                                String abl;
                                                                                data.put("OperationName", "BMWBagsCollectionDetailsManually");
                                                                                data.put("DailyVehicleRouteAssignmentID", userDetails.getString("start_id", "ABC123"));
                                                                                data.put("CompanyID", userDetails.getString("companyID", "1"));
                                                                                data.put("BranchID", userDetails.getString("branchID", "1"));
                                                                                data.put("HandsetID", userDetails.getString("HandsetID", "1"));
                                                                                data.put("VehicleID", userDetails.getString("vehicle_storeID", ""));
                                                                                data.put("DriverID", userDetails.getString("employee_storeID", ""));
                                                                                data.put("MembershipNo", userDetails.getString("MembershipNo", "1"));
                                                                                data.put("RouteID", userDetails.getString("route_storeID", ""));
                                                                                data.put("CollectionDateTime", currentDateandTime);
                                                                                data.put("BagQRCodeDetalID", 0);
                                                                                data.put("QEEID", hospital_id);
                                                                                data.put("ImageTitle", finalColor_param +"-"+bag_id);
                                                                                data.put("NoOfBags", "1");
                                                                                data.put("Weight", edt2.getText().toString());
                                                                                data.put("Latitude", lat);
                                                                                data.put("Longitude", lon);
                                                                                jObjectOptionData1.put(data);
                                                                                abl = jObjectOptionData1.toString();
                                                                                Log.i("Data", "BMWBagsCollectionDetailsManually==" + abl);
                                                                                Toast.makeText(getApplicationContext(), "Database Saved : Weight = " + edt2.getText().toString(), Toast.LENGTH_SHORT).show();

                                                                                if (isConnectingToInternet()) {
                                                                                    new BMWBagsCollectionDetailsManuallyWS(bag_id).execute(abl);
                                                                                } else {
                                                                                    garbageDataStorageObject.open();
                                                                                    ContentValues cv = new ContentValues();
                                                                                    cv.put(GarbageDataStorage.DailyData, abl);
                                                                                    cv.put(GarbageDataStorage.DATATYPE, GarbageDataStorage.BAGS);
                                                                                    cv.put(GarbageDataStorage.IS_ON_SERVER, "False");
                                                                                    garbageDataStorageObject.insert(GarbageDataStorage.DATABASE_BAGS_VISIT, cv);
                                                                                    garbageDataStorageObject.close();
                                                                                }
                                                                            } catch (Exception e) {
                                                                                e.printStackTrace();
                                                                            } finally {
                                                                                alertDialog.dismiss();
                                                                            }
                                                                        } else {
                                                                            Toast.makeText(getApplicationContext(), "SAME BAG SCANNED, So Data not Sent.", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    } else {
                                                                        Toast.makeText(getApplicationContext(), "વજન ચકાસો.", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                } else {
                                                                    Toast.makeText(getApplicationContext(), "વજન ચકાસો.", Toast.LENGTH_SHORT).show();
                                                                }
                                                                alertDialog.dismiss();
                                                                dialog.cancel();
                                                            }
                                                        });
                                                        btn_color_cancel.setOnClickListener(new OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                // Write your code here to invoke NO event
                                                                Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                                                                alertDialog.dismiss();
                                                                dialog.cancel();
                                                            }
                                                        });
                                                        alertDialog.show();
                        /*} else {
                            dialog.cancel();
                            Toast.makeText(getApplicationContext(), "આ ડૉક્ટર ને ફાળવેલી બેગ નથી.", Toast.LENGTH_SHORT).show();
                        }*/

                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }else{
                                            Toast.makeText(getApplicationContext(), "Select Bag Color and Enter Bag Id.", Toast.LENGTH_SHORT).show();
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

        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                          @Override
                                          public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                              if (!checkBoxAndScan) {
                                                  checkBoxAndScan = false;
                                                  if (buttonView.isChecked()) {
                                                      btn_bag_scan.setEnabled(false);
                                                      btn_manual_entry.setEnabled(false);
                                                  } else {
                                                      btn_bag_scan.setEnabled(true);
                                                      btn_manual_entry.setEnabled(true);
                                                  }
                                              } else {
                                                  checkBoxAndScan = true;
                                              }
                                          }
                                      }
        );

        btn_doctor_scan.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                edit_hospital_name.setCursorVisible(false);
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();

                if (isConnectingToInternet()) {
                    if (edit_hospital_name.getText().toString().matches(".*\\d+.*")) {
                        flagTop = true;
                        hospital_id = edit_hospital_name.getText().toString(); //WebService.HOSPITALPREFIX
                        editor.putString("MembershipNo", hospital_id);
                        editor.commit();
                        new WebserviceCheckHospital().execute(hospital_id);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please check your Internet Connection.", Toast.LENGTH_SHORT).show();
                    if (edit_hospital_name.getText().toString().matches(".*\\d+.*")) {
                        flagTop = true;
                        hospital_id = edit_hospital_name.getText().toString(); //WebService.HOSPITALPREFIX
                        editor.putString("MembershipNo", hospital_id);
                        editor.commit();

                        //callDailyWS();

                        text_isactive.setText("Active");
                        btn_ok.setText("Submit Offline");
                        btn_ok.setEnabled(true);

                    }
                }
            }
        });

        btn_bag_scan.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isOpenOrClose.equalsIgnoreCase("open") || isOpenOrClose.equalsIgnoreCase("close") && camera_flag == true) {

                    new IntentIntegrator(HospitalOne.this).initiateScan(); // `this` is the current Activity

                    /*Intent i = new Intent();
                    i.setClass(HospitalOne.this, MainScreen.class);
                    i.putExtra("HospitalOne","HospitalOne");
                    startActivityForResult(i, 2);*/
                } else {
                    Toast.makeText(getApplicationContext(), "Please Take a Picture.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btn_camera.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("0=WEIGHT=", "==" + TotalWeight);
                Log.i("0=Bags=", "==" + TotalBags);

                try {
                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                    StrictMode.setVmPolicy(builder.build());
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(HomeActivity.path.toString() + "/DP.png")));
                    startActivityForResult(intent, 1337);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


        edit_hospital_name.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (isConnectingToInternet()) {
                    if (!hasFocus) {
                        if (edit_hospital_name.getText().toString().matches(".*\\d+.*")) {
                            flagTop = true;
                            new WebserviceCheckHospital().execute(edit_hospital_name.getText().toString());
                            hospital_id = edit_hospital_name.getText().toString(); // WebService.HOSPITALPREFIX+
                        }
                    }
                } else {
                    if (!hasFocus) {
                        if (edit_hospital_name.getText().toString().matches(".*\\d+.*")) {
                            flagTop = true;
                            hospital_id = edit_hospital_name.getText().toString(); //WebService.HOSPITALPREFIX+
                        }
                    }
                    Toast.makeText(getApplicationContext(), "Please check your Internet Connection.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_ok.setEnabled(false);
                Log.i("==", "==" + isOpenOrClose);
                Log.i("==", "==" + camera_flag);
                try {
                    //if(waCount.size()==0) {
                    if (isOpenOrClose.equalsIgnoreCase("open") || isOpenOrClose.equalsIgnoreCase("close") && camera_flag == true) {

                        /*if(cb.isChecked()){
                            callDailyWS();
                        }*/

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(HospitalOne.this);
                        LayoutInflater inflater = HospitalOne.this.getLayoutInflater();
                        final View dialogView = inflater.inflate(R.layout.dialog_sign, null);
                        alertDialog.setView(dialogView);

                        mContent = (LinearLayout) dialogView.findViewById(R.id.layout_sign);

                        mSignature = new signature(HospitalOne.this, null);
                        mSignature.setBackgroundColor(Color.WHITE);
                        mContent.addView(mSignature, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
                        mView = mContent;

                        alertDialog.setTitle("Confirm Submission");
                        StringBuilder sb = new StringBuilder();

                        if (yellowWeight > 0.0f) {
                            final String localYellow = String.format("%.02f", yellowWeight);
                            sb.append("Total Yellow Weight: " + localYellow + " & Bags: " + TotalYellowBag + "\n");
                        }
                        if (redWeight > 0.0f) {
                            final String localRed = String.format("%.02f", redWeight);
                            sb.append("Total Red Weight: " + localRed + " & Bags: " + TotalRedBag + "\n");
                        }
                        if (whiteWeight > 0.0f) {
                            final String localWhite = String.format("%.02f", whiteWeight);
                            sb.append("Total White Weight: " + localWhite + " & Bags: " + TotalWhiteBag + "\n");
                        }
                        if (blueWeight > 0.0f) {
                            final String localBlue = String.format("%.02f", blueWeight);
                            sb.append("Total Blue Weight: " + localBlue + " & Bags: " + TotalBlueBag + "\n");
                        }
                        final String localTotalWeight = String.format("%.02f", TotalWeight);
                        alertDialog.setMessage(sb + "And Total Weight =" + localTotalWeight + " Kg \n" + "Total Bags =" + TotalBags);
                        alertDialog.setIcon(GlobalVariables.getLogo());
                        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to invoke YES event
                                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault());
                                currentDateandTime = sdf.format(new Date());
                                if (signBool) {
                                    try {

                                        mView.setDrawingCacheEnabled(true);
                                        mSignature.save(mView);

                                        final JSONArray jObjectOptionData1 = new JSONArray();
                                        //convert parameters into JSON object
                                        JSONObject data = new JSONObject();
                                        data.put("DailyVehicleRouteAssignmentID", userDetails.getString("start_id", "ABC123"));
                                        data.put("OperationName", "BMWBagsCollectionSummary");
                                        data.put("CompanyID", userDetails.getString("companyID", "1"));
                                        data.put("BranchID", userDetails.getString("branchID", "1"));
                                        data.put("HandsetID", userDetails.getString("HandsetID", "1"));
                                        data.put("VehicleID", userDetails.getString("vehicle_storeID", ""));
                                        data.put("DriverID", userDetails.getString("employee_storeID", ""));
                                        data.put("MembershipNo", userDetails.getString("MembershipNo", "1"));
                                        data.put("RouteID", userDetails.getString("route_storeID", ""));
                                        data.put("CollectionDateTime", currentDateandTime);
                                        data.put("TotalYellowBags", TotalYellowBag);
                                        data.put("TotalYellowBagWeight", yellowWeight);
                                        data.put("TotalWhiteBags", TotalWhiteBag);
                                        data.put("TotalWhiteBagWeight", whiteWeight);
                                        data.put("TotalRedBags", TotalRedBag);
                                        data.put("TotalRedBagWeight", redWeight);
                                        data.put("TotalBlueBags", TotalBlueBag);
                                        data.put("TotalBlueBagWeight", blueWeight);
                                        data.put("TotalBagCount", TotalBags);
                                        data.put("TotalBagWeight", localTotalWeight);
                                        data.put("DoctorSignature", valBase64Sign);

                                        jObjectOptionData1.put(data);

                                        String abl = jObjectOptionData1.toString();
                                        Log.i("Data", "BMWBagsCollectionSummary==" + abl);

                                        Toast.makeText(getApplicationContext(), "Total Weight = " + localTotalWeight, Toast.LENGTH_SHORT).show();

                                        if (isConnectingToInternet()) {
                                            new BMWBagsCollectionSummaryWS(abl).execute(abl);
                                        } else {
                                            if (flagTop) {

                                                callDailyWS(false);
                                                garbageDataStorageObject.open();
                                                ContentValues cv = new ContentValues();
                                                cv.put(GarbageDataStorage.DailyData, abl);
                                                cv.put(GarbageDataStorage.DATATYPE, GarbageDataStorage.SUMMARY);
                                                cv.put(GarbageDataStorage.IS_ON_SERVER, "False");
                                                garbageDataStorageObject.insert(GarbageDataStorage.DATABASE_SUMMARY_VISIT, cv);
                                                garbageDataStorageObject.close();

                                                Toast.makeText(getApplicationContext(), "Data Stored in Database.", Toast.LENGTH_SHORT).show();
                                                Intent ite = new Intent(HospitalOne.this, HomeActivity.class);
                                                ite.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(ite);
                                                finish();

                                            } else {
                                                Toast.makeText(getApplicationContext(), "Enter Hospital id.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "હોસ્પિટલ ની સહી ફરજીયાત કરવો.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        // Showing Alert Message
                        alertDialog.show();

                    } else {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(HospitalOne.this);
                        alertDialog.setTitle("Message");
                        alertDialog.setMessage("Please take a picture...");
                        alertDialog.setIcon(R.drawable.q_logo);
                        alertDialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        // Showing Alert Message
                        alertDialog.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("BMWBagCollSummary", "==BMWBagsCollectionSummary :" + e.getMessage(), e);
                } finally {
                    btn_ok.setEnabled(true);
                }
            }
        });

        list_collection.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HospitalOne.this);
                alertDialogBuilder.setMessage("Please Confirm Below Records.\n Weight :" + waCount.get(position).getWeight() + " for BagID : " + waCount.get(position).getBagQRCodeDetalID())
                        .setCancelable(false)
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        String val[] = waCount.get(position).getQRCodeContent().split("-");
                                        if (val[0].equalsIgnoreCase("BLUE")) {
                                            TotalWeight += Double.parseDouble(waCount.get(position).getWeight());
                                            blueWeight += Double.parseDouble(waCount.get(position).getWeight());
                                            blueBag = 1;
                                            TotalBlueBag += blueBag;
                                            TotalBags += blueBag;
                                        } else if (val[0].equalsIgnoreCase("RED")) {
                                            TotalWeight += Double.parseDouble(waCount.get(position).getWeight());
                                            redWeight += Double.parseDouble(waCount.get(position).getWeight());
                                            redBag = 1;
                                            TotalRedBag += redBag;
                                            TotalBags += redBag;
                                        } else if (val[0].equalsIgnoreCase("WHITE")) {
                                            TotalWeight += Double.parseDouble(waCount.get(position).getWeight());
                                            whiteWeight += Double.parseDouble(waCount.get(position).getWeight());
                                            whiteBag = 1;
                                            TotalWhiteBag += whiteBag;
                                            TotalBags += whiteBag;
                                        } else if (val[0].equalsIgnoreCase("YELLOW")) {
                                            TotalWeight += Double.parseDouble(waCount.get(position).getWeight());
                                            yellowWeight += Double.parseDouble(waCount.get(position).getWeight());
                                            yellowBag = 1;
                                            TotalYellowBag += yellowBag;
                                            TotalBags += yellowBag;
                                        }

                                        String wei = waCount.get(position).getWeight();
                                        String bagID = waCount.get(position).getBagQRCodeDetalID();
                                        callBagWS(bagID, wei);
                                        new WebServiceGetHospitalWasteCollectionList().execute(hospital_id);
                                    }
                                });
                alertDialogBuilder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = alertDialogBuilder.create();
                alert.show();

            }

        });

        manager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        isGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        gps = new GPSTracker(getApplicationContext());

        // check if GPS enabled
        if (gps.canGetLocation()) {
            lat = gps.getLatitude();
            lon = gps.getLongitude();
            if (lat == 0) {
                gps = new GPSTracker(getApplicationContext());
                // check if GPS enabled
                if (gps.canGetLocation()) {
                    lat = gps.getLatitude();
                    lon = gps.getLongitude();
                }
            }
        } else {
            //Toast.makeText(getApplicationContext(), "Location not detected", Toast.LENGTH_SHORT).show();
        }
    }


    private class CheckForUsedBagQRCodeWS extends AsyncTask<String, String, SoapObject> {

        SoapObject soap;
        private ProgressDialog dialog;
        String bagMemberID;
        String bagId;

        public CheckForUsedBagQRCodeWS(String colorValue, String val, String bagID) {
            ColorValue = colorValue;
            bagMemberID = val;
            bagId = bagID;
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(HospitalOne.this);
            dialog.setMessage("Please wait...");
            dialog.show();
        }

        @Override
        protected SoapObject doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()
            try {
                Log.i("Data", "CheckForUsedBagQRCode==" + params[0]);
                soap = WebService.webService_CheckForUsedBagQRCode.callWebservice(getApplicationContext(), params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return soap;
        }

        @Override
        protected void onPostExecute(SoapObject soapObject) {
            Log.i("", "==myResultCheckForUsedBagQRCode " + soapObject);

            if (soapObject != null && soapObject.hasProperty("CheckForUsedBagQRCodeResult")) {
                if (soapObject.getProperty("CheckForUsedBagQRCodeResult").toString().equals("0")) {
                    sameBag = true;
                    //REQUEST 477|Y|QEEVADODARA|2|13021800001,DrName:DR.ABC,HospName:Trial Hosp,Area:,Pin:000000,Phone:0,Mobile:0,GPCBID:0
                    //RESPONSE CheckForUsedBagQRCodeResponse{CheckForUsedBagQRCodeResult=0; }
                    String onlyID = userDetails.getString("MembershipNo", "1");
                    if (onlyID.equalsIgnoreCase(bagMemberID)) {
                        final Dialog alertDialog = new Dialog(HospitalOne.this);
                        LayoutInflater inflater = HospitalOne.this.getLayoutInflater();
                        //final View dialogColor = inflater.inflate(R.layout.dialog_color, null);
                        alertDialog.setContentView(R.layout.dialog_color);
                        //alertDialog.setView(dialogColor);

                        edt2 = (EditText) alertDialog.findViewById(R.id.edit_kg_1);

                        btn_color_ok = (Button) alertDialog.findViewById(R.id.btn_color_ok);
                        btn_color_cancel = (Button) alertDialog.findViewById(R.id.btn_color_cancel);


                        /*one = (TextView) alertDialog.findViewById(R.id.text_enter_bags);
                        two = (TextView) alertDialog.findViewById(R.id.text_enter_kg);

                        Typeface font = Typeface.createFromAsset(getAssets(), "SHRUTI.TTF");
                        one.setTypeface(font);
                        one.setTypeface(one.getTypeface(), Typeface.BOLD);
                        one.setText("ભરેલી બેગ લેવાની નંગ ");
                        two.setTypeface(font);
                        two.setTypeface(two.getTypeface(), Typeface.BOLD);
                        two.setText("લીધેલી બેગ વજન kg");*/

                        TableLayout tblRow = (TableLayout) alertDialog.findViewById(R.id.tableLayout1);

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
                        mContent = (LinearLayout) alertDialog.findViewById(R.id.layout_sign);
                        alertDialog.setTitle("Add Bag Weight.");
                        alertDialog.setCancelable(false);
                        btn_color_ok.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (edt2.getText().length() != 0) {
                                    if (Double.parseDouble(edt2.getText().toString()) <= 999) {
                                        if (sameBag) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                                            //callDailyWS();

                                            Log.i("0=TotalWeight=", "==" + TotalWeight);
                                            Log.i("0=TotalBags=", "==" + TotalBags);

                                            if (ColorValue.equalsIgnoreCase("Blue")) {
                                                TotalWeight += Double.parseDouble(edt2.getText().toString());
                                                blueWeight += Double.parseDouble(edt2.getText().toString());
                                                blueBag = 1;//Integer.parseInt(edt1.getText().toString());
                                                TotalBlueBag += blueBag;
                                                TotalBags += blueBag;
                                            } else if (ColorValue.equalsIgnoreCase("Red")) {
                                                TotalWeight += Double.parseDouble(edt2.getText().toString());
                                                redWeight += Double.parseDouble(edt2.getText().toString());
                                                redBag = 1;//Integer.parseInt(edt1.getText().toString());
                                                TotalRedBag += redBag;
                                                TotalBags += redBag;
                                            } else if (ColorValue.equalsIgnoreCase("White")) {
                                                TotalWeight += Double.parseDouble(edt2.getText().toString());
                                                whiteWeight += Double.parseDouble(edt2.getText().toString());
                                                whiteBag = 1;//Integer.parseInt(edt1.getText().toString());
                                                TotalWhiteBag += whiteBag;
                                                TotalBags += whiteBag;
                                            } else if (ColorValue.equalsIgnoreCase("Yellow")) {
                                                TotalWeight += Double.parseDouble(edt2.getText().toString());
                                                yellowWeight += Double.parseDouble(edt2.getText().toString());
                                                yellowBag = 1;//Integer.parseInt(edt1.getText().toString());
                                                TotalYellowBag += yellowBag;
                                                TotalBags += yellowBag;
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Problem in bagscanning", Toast.LENGTH_SHORT).show();
                                            }
                                            Log.i("=TotalWeight=", "==" + TotalWeight);
                                            Log.i("=TotalBags=", "==" + TotalBags);

                                            try {

                                                final JSONArray jObjectOptionData1 = new JSONArray();

                                                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault());
                                                String currentDateandTime = sdf.format(new Date());

                                                JSONObject data = new JSONObject();
                                                String abl;
                                                data.put("OperationName", "BMWBagsCollectionDetails");
                                                data.put("DailyVehicleRouteAssignmentID", userDetails.getString("start_id", "ABC123"));
                                                data.put("CompanyID", userDetails.getString("companyID", "1"));
                                                data.put("BranchID", userDetails.getString("branchID", "1"));
                                                data.put("HandsetID", userDetails.getString("HandsetID", "1"));
                                                data.put("VehicleID", userDetails.getString("vehicle_storeID", ""));
                                                data.put("DriverID", userDetails.getString("employee_storeID", ""));
                                                data.put("MembershipNo", userDetails.getString("MembershipNo", "1"));
                                                data.put("RouteID", userDetails.getString("route_storeID", ""));
                                                data.put("CollectionDateTime", currentDateandTime);
                                                data.put("BagQRCodeDetalID", bagId);
                                                data.put("NoOfBags", "1");
                                                data.put("Weight", edt2.getText().toString());
                                                data.put("Latitude", lat);
                                                data.put("Longitude", lon);
                                                jObjectOptionData1.put(data);
                                                abl = jObjectOptionData1.toString();
                                                Log.i("Data", "BMWBagsCollectionDetails==" + abl);
                                                Toast.makeText(getApplicationContext(), "Database Saved : Weight = " + edt2.getText().toString(), Toast.LENGTH_SHORT).show();

                                                if (isConnectingToInternet()) {
                                                    new BMWBagsCollectionDetailsWS(bagId).execute(abl);
                                                } else {
                                                    garbageDataStorageObject.open();
                                                    ContentValues cv = new ContentValues();
                                                    cv.put(GarbageDataStorage.DailyData, abl);
                                                    cv.put(GarbageDataStorage.DATATYPE, GarbageDataStorage.BAGS);
                                                    cv.put(GarbageDataStorage.IS_ON_SERVER, "False");
                                                    garbageDataStorageObject.insert(GarbageDataStorage.DATABASE_BAGS_VISIT, cv);
                                                    garbageDataStorageObject.close();
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            } finally {
                                                alertDialog.dismiss();
                                            }
                                        } else {
                                            Toast.makeText(getApplicationContext(), "SAME BAG SCANNED, So Data not Sent.", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(), "વજન ચકાસો.", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "વજન ચકાસો.", Toast.LENGTH_SHORT).show();
                                }
                                alertDialog.dismiss();
                                dialog.cancel();
                            }
                        });
                        btn_color_cancel.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Write your code here to invoke NO event
                                Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                                dialog.cancel();
                            }
                        });
                        alertDialog.show();
                    } else {
                        dialog.cancel();
                        Toast.makeText(getApplicationContext(), "આ ડૉક્ટર ને ફાળવેલી બેગ નથી.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    dialog.cancel();
                    sameBag = false;
                    Toast.makeText(getApplicationContext(), "બેગ અગાઉં લેવાયેલ છે.", Toast.LENGTH_SHORT).show();
                }
            } else {
                dialog.cancel();
                sameBag = false;
                Toast.makeText(getApplicationContext(), "Problem in CheckForUsedBagQRCodeResponseResult", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private class DailyHospitalVisitDetailsWS extends AsyncTask<String, String, SoapObject> {

        SoapObject soap;
        //private ProgressDialog dialog;

        public DailyHospitalVisitDetailsWS() {
        }

        @Override
        protected void onPreExecute() {
            // Things to be done before execution of long running operation. For
            // example showing ProgessDialog
            /*dialog = new ProgressDialog(HospitalOne.this);
            dialog.setMessage("Please wait...");
            dialog.show();*/
        }

        @Override
        protected SoapObject doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()
            try {
                Log.i("Data", "DailyHospitalVisitDetails==" + params[0]);
                soap = WebService.webService_DailyHospitalVisitDetails.callWebservice(getApplicationContext(), params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return soap;
        }

        @Override
        protected void onPostExecute(SoapObject soapObject) {
            Log.i("", "==myResultDailyHospitalVisitDetailsResult " + soapObject);
//soapObject.getProperty("CheckForUsedBagQRCodeResult").toString().equals("0")
            onlyOneVisit = false;
            cb.setEnabled(false);
            if (soapObject != null && soapObject.hasProperty("DailyHospitalVisitDetailsResult")) {
                if (Integer.parseInt(soapObject.getProperty("DailyHospitalVisitDetailsResult").toString()) >= 0) {
                    Toast.makeText(getApplicationContext(), "Success for DailyHospitalVisitDetailsResult", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "<=0", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Problem in DailyHospitalVisitDetailsWS", Toast.LENGTH_SHORT).show();
            }
            /*if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }*/
        }

    }

    private class BMWBagsCollectionDetailsManuallyWS extends AsyncTask<String, String, SoapObject> {

        SoapObject soap;
        private ProgressDialog dialog;
        String bagId;

        public BMWBagsCollectionDetailsManuallyWS (String bagID) {
            bagId = bagID;
        }

        @Override
        protected void onPreExecute() {
            // Things to be done before execution of long running operation. For
            // example showing ProgessDialog
            dialog = new ProgressDialog(HospitalOne.this);
            dialog.setMessage("Please wait...");
            dialog.show();
        }

        @Override
        protected SoapObject doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()
            try {
                soap = WebService.webService_BMWBagsCollectionDetailsManually.callWebservice(getApplicationContext(), params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return soap;
        }

        @Override
        protected void onPostExecute(SoapObject soapObject) {
            Log.i("", "==myResult BMWBagsCollectionDetailsResultManually " + soapObject);//DailyHospitalVisitDetailsResponse{DailyHospitalVisitDetailsResult=42; }
//BMWBagsCollectionDetailsManuallyResponse{BMWBagsCollectionDetailsManuallyResult=84225; }
            if (soapObject != null && soapObject.hasProperty("BMWBagsCollectionDetailsManuallyResult")) {
                if (Integer.parseInt(soapObject.getProperty("BMWBagsCollectionDetailsManuallyResult").toString()) >= 0) {
                    bagsArray.add(bagId);
                    Toast.makeText(getApplicationContext(), "Success for BMWBagsCollectionDetailsResult", Toast.LENGTH_SHORT).show();
                    btn_ok.setEnabled(true);
                } else {
                    Toast.makeText(getApplicationContext(), "<=0", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Problem in BMWBagsCollectionDetailsResultManually", Toast.LENGTH_SHORT).show();
            }
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }

    }

    private class BMWBagsCollectionDetailsWS extends AsyncTask<String, String, SoapObject> {

        SoapObject soap;
        private ProgressDialog dialog;
        String bagId;

        public BMWBagsCollectionDetailsWS(String bagID) {
            bagId = bagID;
        }

        @Override
        protected void onPreExecute() {
            // Things to be done before execution of long running operation. For
            // example showing ProgessDialog
            dialog = new ProgressDialog(HospitalOne.this);
            dialog.setMessage("Please wait...");
            dialog.show();
        }

        @Override
        protected SoapObject doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()
            try {
                soap = WebService.webService_BMWBagsCollectionDetails.callWebservice(getApplicationContext(), params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return soap;
        }

        @Override
        protected void onPostExecute(SoapObject soapObject) {
            Log.i("", "==myResult BMWBagsCollectionDetailsResult " + soapObject);//DailyHospitalVisitDetailsResponse{DailyHospitalVisitDetailsResult=42; }

            if (soapObject != null && soapObject.hasProperty("BMWBagsCollectionDetailsResult")) {
                if (Integer.parseInt(soapObject.getProperty("BMWBagsCollectionDetailsResult").toString()) >= 0) {
                    bagsArray.add(bagId);
                    Toast.makeText(getApplicationContext(), "Success for BMWBagsCollectionDetailsResult", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "<=0", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Problem in BMWBagsCollectionDetailsResult", Toast.LENGTH_SHORT).show();
            }
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }

    }

    private class BMWBagsCollectionSummaryWS extends AsyncTask<String, String, SoapObject> {

        SoapObject soap;
        private ProgressDialog dialog;
        String summaryVal;

        public BMWBagsCollectionSummaryWS(String val) {
            summaryVal = val;
        }

        @Override
        protected void onPreExecute() {
            // Things to be done before execution of long running operation. For
            // example showing ProgessDialog
            dialog = new ProgressDialog(HospitalOne.this);
            dialog.setMessage("Please wait...");
            dialog.show();
        }

        @Override
        protected SoapObject doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()
            try {
                soap = WebService.webService_BMWBagsCollectionSummary.callWebservice(getApplicationContext(), params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return soap;
        }

        @Override
        protected void onPostExecute(SoapObject soapObject) {
            Log.i("", "==myResult" + soapObject);
            try {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (soapObject != null && soapObject.hasProperty("BMWBagsCollectionSummaryResult")) {
                    /*if (email_id != null) {
                        if(cb.isChecked()){ // NO BAGS FOUND
                            new Thread(new Runnable() {
                                public void run() {
                                    try {
                                        GMailSender sender = new GMailSender(GlobalVariables.getGmailID(), GlobalVariables.getGmailPwd()); //Vadodara2003
                                        sender.sendMail("BioMedical Collection Report",
                                                "Dear Dr. Member, [" + hospital_id + "]\n\nThank you for being Eco-Friendly Member of Quantum Environment Engineers (QEE).\n\n Kindly note that membership QR code of your health care unit has been scanned successfully on "+ currentDateandTime + ".But there was no Bio Medical Waste for disposal so Nil Bio-Medical Waste Collected.\n\nIf you have any query on above transaction, please call our Helpline number mentioned on your Record Keeping-Book. You may also visit www.quantumenvironment.in for the same.\nSincerely,\n\nQuantum Environment Engineers\nVadodara\n\nThe alert has been sent to the registered e-mail ID against your aforementioned enrolment with QEE. In case you wish to register an alternate e-mail ID, please call our Helpline Number.\n\nThis is an automated e-mail alert to help you keep track of your BMW handled. Hence, please do not reply to this e-mail.\nCurrently we are under development phase.\n\nQuantum Environment Engineers. \n\nVadodara.",
                                                GlobalVariables.getGmailID(),
                                                email_id);
                                    } catch (Exception e) {
                                        Log.e("SendMail", e.getMessage(), e);
                                    }
                                }
                            }).start();
                        }else {
                            if (isOpenOrClose == "open") {
                                new Thread(new Runnable() {
                                    public void run() {
                                        try {
                                            GMailSender sender = new GMailSender(GlobalVariables.getGmailID(), GlobalVariables.getGmailPwd()); //Vadodara2003
                                            sender.sendMail("BioMedical Collection Report",
                                                    "Dear Dr. Member, [" + hospital_id + "]\n\nThank you for being Eco-Friendly Member of Quantum Environment Engineers (QEE).\n\nYour Membership QR Code has been scanned by our staff from your Health Care Unit and Bio Medical Waste (BMW) Collected category wise as under:\n\nYellow = " + yellowWeight + " Kg.\nBlue = " + blueWeight + " Kg.\nRed = " + redWeight + " Kg.\nWhite= " + whiteWeight + "Kg.\nOn Date = " + currentDateandTime + " \n\nIf you have any query on above transaction, please call our Helpline number mentioned on your Record Keeping-Book. You may also visit www.quantumenvironment.in for the same.\nSincerely,\n\nQuantum Environment Engineers\nVadodara\n\nThe alert has been sent to the registered e-mail ID against your aforementioned enrolment with QEE. In case you wish to register an alternate e-mail ID, please call our Helpline Number.\n\nThis is an automated e-mail alert to help you keep track of your BMW handled. Hence, please do not reply to this e-mail.\nCurrently we are under development phase.\n\nQuantum Environment Engineers.",
                                                    GlobalVariables.getGmailID(),
                                                    email_id);
                                        } catch (Exception e) {
                                            Log.e("SendMail", e.getMessage(), e);
                                        }
                                    }
                                }).start();
                            } else {
                                new Thread(new Runnable() {
                                    public void run() {

                                        try {
                                            GMailSender sender = new GMailSender(GlobalVariables.getGmailID(), GlobalVariables.getGmailPwd()); //Vadodara2003
                                            sender.sendMail("BioMedical Collection Report",
                                                    "Dear Dr. Member, [" + hospital_id + "]\n\nThank you for being Eco-Friendly Member of Quantum Environment Engineers (QEE).\n\nKindly note that your Health Care Unit found closed either the Scannig of QR Code was not completed On Date =" + currentDateandTime + " and Bio Medical Waste (BMW) Collected category wise as under::\n\nYellow = " + yellowWeight + " Kg.\nBlue = " + blueWeight + " Kg.\nRed = " + redWeight + " Kg.\nWhite= " + whiteWeight + "Kg.\nOn Date = " + currentDateandTime + " \n\nIf you have any query on above transaction, please call our Helpline number mentioned on your Record Keeping-Book. You may also visit www.quantumenvironment.in for the same.\nSincerely,\n\nQuantum Environment Engineers\nVadodara\n\nThe alert has been sent to the registered e-mail ID against your aforementioned enrolment with QEE. In case you wish to register an alternate e-mail ID, please call our Helpline Number.\n\nThis is an automated e-mail alert to help you keep track of your BMW handled. Hence, please do not reply to this e-mail.\nCurrently we are under development phase.\n\nQuantum Environment Engineers.",
                                                    GlobalVariables.getGmailID(),
                                                    email_id);
                                        } catch (Exception e) {
                                            Log.e("SendMail", e.getMessage(), e);
                                        }
                                    }
                                }).start();
                            }
                        }
                    }else {
                        Toast.makeText(getApplicationContext(),"Email Not Registered.",Toast.LENGTH_SHORT).show();
                    }*/
                } else {
                    Toast.makeText(getApplicationContext(), "Problem in BMWBagsCollectionSummaryResult", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {

            } finally {

                if (soapObject != null && soapObject.hasProperty("BMWBagsCollectionSummaryResult")) {
                    callDailyWS(false);
                } else {
                    callDailyWS(true);
                    garbageDataStorageObject.open();
                    ContentValues cv = new ContentValues();
                    cv.put(GarbageDataStorage.DailyData, summaryVal);
                    cv.put(GarbageDataStorage.DATATYPE, GarbageDataStorage.SUMMARY);
                    cv.put(GarbageDataStorage.IS_ON_SERVER, "False");
                    garbageDataStorageObject.insert(GarbageDataStorage.DATABASE_SUMMARY_VISIT, cv);
                    garbageDataStorageObject.close();

                    Toast.makeText(getApplicationContext(), "Data Stored in Database.", Toast.LENGTH_SHORT).show();
                    Intent ite = new Intent(HospitalOne.this, HomeActivity.class);
                    ite.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(ite);
                    finish();
                }


                Intent ite = new Intent(HospitalOne.this, HomeActivity.class);
                ite.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(ite);
                finish();
            }
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }

    }

    private class WebserviceCheckHospital extends AsyncTask<String, String, String> {

        private String resp;
        String soap;
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            // Things to be done before execution of long running operation. For
            // example showing ProgessDialog
            dialog = new ProgressDialog(HospitalOne.this);
            dialog.setMessage("Please wait...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()
            try {
                Log.i("=params[0]", "params[0]" + params[0]);
                //TODO
                soap = WebService.webService_CheckHospital.callWebserviceCheck(getApplicationContext(), Integer.parseInt(userDetails.getString("companyID", "1")), Integer.parseInt(userDetails.getString("branchID", "1")), params[0]);
            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return soap;
        }

        @Override
        protected void onPostExecute(String stringVal) {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            if (stringVal != null && stringVal.length() >= 1 && stringVal.contains("|")) { //anyType{emailID=anyType{}; }
                String[] tokens = stringVal.split(Pattern.quote("|"));
                if (tokens[2].contains("Active") || tokens[2].contains("ACTIVE") || tokens[2].contains("active")) { //anyType{emailID=2|hemalsvyas@gmail.com|Active; }
                    text_isactive.setText("Is Active");
                    btn_ok.setText("Submit");
                    btn_ok.setEnabled(true);
                    Toast.makeText(getApplicationContext(), "Active", Toast.LENGTH_SHORT).show();
                    if (validate(tokens[1])) {
                        email_id = tokens[1];
                    }
                    //callDailyWS();
                    new WebServiceGetHospitalWasteCollectionList().execute(hospital_id);
                } else if (tokens[2].contains("Closed") || tokens[2].contains("CLOSED")) {
                    text_isactive.setText("Is Not-Active");
                    Toast.makeText(getApplicationContext(), "Closed", Toast.LENGTH_SHORT).show();
                    btn_ok.setText("Hospital is not Active");
                    btn_ok.setEnabled(false);
                } else {
                    text_isactive.setText("Is Not-Active");
                    Toast.makeText(getApplicationContext(), "Not-Active", Toast.LENGTH_SHORT).show();
                    btn_ok.setText("Hospital is not Active");
                    btn_ok.setEnabled(false);
                }
            } else {
                btn_bag_scan.setEnabled(false);
                text_isactive.setText("Is Not-Active");
                btn_ok.setText("Hospital is not Active");
                btn_ok.setEnabled(false);
            }
        }
    }

    private class WebServiceGetHospitalWasteCollectionList extends AsyncTask<String, String, SoapObject> {

        private String resp;
        SoapObject soap;
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(HospitalOne.this);
            dialog.setMessage("Please wait...");
            dialog.show();
        }

        @Override
        protected SoapObject doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()
            try {
                Log.i("=params[0]", "== WebserviceGetHospitalWasteCollectionList[0]" + params[0]);
                soap = WebService.webService_GetHospitalWasteCollectionList.callWebserviceCheck(Integer.parseInt(params[0]));
            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return soap;
        }

        @Override
        protected void onPostExecute(SoapObject soapObject) {

            if (soapObject != null) {
                SoapObject myResult = (SoapObject) soapObject.getProperty("GetHospitalWasteCollectionListResult");
                waCount = new ArrayList<WasteScanningAtHospitals>();
                SoapObject lstEmpSoap = (SoapObject) soapObject.getProperty(0);

                if (lstEmpSoap.getPropertyCount() >= 1) {
                    cb.setEnabled(false);
                    btn_bag_scan.setEnabled(false);
                    btn_ok.setEnabled(false);
                    text_select.setVisibility(View.VISIBLE);
                } else {
                    cb.setEnabled(true);
                    btn_bag_scan.setEnabled(true);
                    btn_ok.setEnabled(true);
                    text_select.setVisibility(View.GONE);
                }


                for (int i = 0; i < lstEmpSoap.getPropertyCount(); i++) {
                    WasteScanningAtHospitals wah = new WasteScanningAtHospitals();
                    SoapObject soapValue = (SoapObject) lstEmpSoap.getProperty(i);
                    wah.setWasteScanningAtHospitalDetailID(soapValue.getPrimitivePropertyAsString("WasteScanningAtHospitalDetailID").toString());
                    wah.setBagQRCodeDetalID(soapValue.getProperty("BagQRCodeDetalID").toString());
                    wah.setWeight(soapValue.getProperty("Weight").toString());
                    wah.setQRCodeContent(soapValue.getProperty("QRCodeContent").toString());
                    wah.setScannedDateTime(soapValue.getProperty("ScannedDateTime").toString());
                    waCount.add(wah);
                }
                collAdapter = new CollectionAdapter(getApplicationContext(), waCount);
                list_collection.setAdapter(collAdapter);
            }
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    public class WebServiceCheckValidBagManually extends AsyncTask<String, String, SoapObject> {

        private String resp;
        private String hos_id, bag_id, color_param;
        SoapObject soap;
        private ProgressDialog dialog;

        public WebServiceCheckValidBagManually(String hospital_idd, String bag_idd, String colorP) {
            hos_id = hospital_id;
            bag_id = bag_idd;
            color_param = colorP;
        }


        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(HospitalOne.this);
            dialog.setMessage("Please wait...");
            dialog.show();
        }

        @Override
        protected SoapObject doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()
            try {
                Log.i("=params[0]", "== WebServiceCheckValidBagManually[0]" + params[0]);
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
                //SoapObject myResult = (SoapObject) soapObject.getProperty("CheckValidBagManuallyResult");

                if (soapObject.getProperty("CheckValidBagManuallyResult").toString().equals("0")) {
                    sameBag = true;
                    //REQUEST 477|Y|QEEVADODARA|2|13021800001,DrName:DR.ABC,HospName:Trial Hosp,Area:,Pin:000000,Phone:0,Mobile:0,GPCBID:0
                    //RESPONSE CheckForUsedBagQRCodeResponse{CheckForUsedBagQRCodeResult=0; }
                    String onlyID = userDetails.getString("MembershipNo", "1");
                    //if (onlyID.equalsIgnoreCase(bagMemberID)) {
                        final Dialog alertDialog = new Dialog(HospitalOne.this);
                        LayoutInflater inflater = HospitalOne.this.getLayoutInflater();
                        //final View dialogColor = inflater.inflate(R.layout.dialog_color, null);
                        alertDialog.setContentView(R.layout.dialog_color);
                        //alertDialog.setView(dialogColor);

                        edt2 = (EditText) alertDialog.findViewById(R.id.edit_kg_1);
                        edit_bagID = (EditText) alertDialog.findViewById(R.id.edit_kg_bagid);
                        edit_bagID.setText(""+bag_id);

                        tableRow0 = (TableRow)alertDialog.findViewById(R.id.tableRow0);
                        tableRow1 = (TableRow)alertDialog.findViewById(R.id.tableRow1);
                        tableRow0.setVisibility(View.VISIBLE);
                        tableRow1.setVisibility(View.VISIBLE);

                        btn_color_ok = (Button) alertDialog.findViewById(R.id.btn_color_ok);
                        btn_color_cancel = (Button) alertDialog.findViewById(R.id.btn_color_cancel);


                        /*one = (TextView) alertDialog.findViewById(R.id.text_enter_bags);
                        two = (TextView) alertDialog.findViewById(R.id.text_enter_kg);

                        Typeface font = Typeface.createFromAsset(getAssets(), "SHRUTI.TTF");
                        one.setTypeface(font);
                        one.setTypeface(one.getTypeface(), Typeface.BOLD);
                        one.setText("ભરેલી બેગ લેવાની નંગ ");
                        two.setTypeface(font);
                        two.setTypeface(two.getTypeface(), Typeface.BOLD);
                        two.setText("લીધેલી બેગ વજન kg");*/

                        TableLayout tblRow = (TableLayout) alertDialog.findViewById(R.id.tableLayout1);

                        if (color_param.equalsIgnoreCase("BLUE")) {
                            tblRow.setBackgroundColor(getResources().getColor(R.color.blue));
                            color_param = "BLUE";
                        } else if (color_param.equalsIgnoreCase("RED")) {
                            tblRow.setBackgroundColor(getResources().getColor(R.color.red));
                            color_param = "RED";
                        } else if (color_param.equalsIgnoreCase("WHITE")) {
                            tblRow.setBackgroundColor(getResources().getColor(R.color.white));
                            color_param = "WHITE";
                        } else if (color_param.equalsIgnoreCase("YELLOW")) {
                            tblRow.setBackgroundColor(getResources().getColor(R.color.yellow));
                            color_param = "YELLOW";
                        } else {
                            Toast.makeText(getApplicationContext(), "Problem in Scanning, Please scan again.", Toast.LENGTH_LONG).show();
                        }
                        mContent = (LinearLayout) alertDialog.findViewById(R.id.layout_sign);
                        alertDialog.setTitle("Add Bag Weight.");
                        alertDialog.setCancelable(false);
                        btn_color_ok.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (edt2.getText().length() != 0) {
                                    if (Double.parseDouble(edt2.getText().toString()) <= 999) {
                                        if (sameBag) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                                            //callDailyWS();

                                            Log.i("0=TotalWeight=", "==" + TotalWeight);
                                            Log.i("0=TotalBags=", "==" + TotalBags);

                                            if (color_param.equalsIgnoreCase("Blue")) {
                                                TotalWeight += Double.parseDouble(edt2.getText().toString());
                                                blueWeight += Double.parseDouble(edt2.getText().toString());
                                                blueBag = 1;//Integer.parseInt(edt1.getText().toString());
                                                TotalBlueBag += blueBag;
                                                TotalBags += blueBag;
                                            } else if (color_param.equalsIgnoreCase("Red")) {
                                                TotalWeight += Double.parseDouble(edt2.getText().toString());
                                                redWeight += Double.parseDouble(edt2.getText().toString());
                                                redBag = 1;//Integer.parseInt(edt1.getText().toString());
                                                TotalRedBag += redBag;
                                                TotalBags += redBag;
                                            } else if (color_param.equalsIgnoreCase("White")) {
                                                TotalWeight += Double.parseDouble(edt2.getText().toString());
                                                whiteWeight += Double.parseDouble(edt2.getText().toString());
                                                whiteBag = 1;//Integer.parseInt(edt1.getText().toString());
                                                TotalWhiteBag += whiteBag;
                                                TotalBags += whiteBag;
                                            } else if (color_param.equalsIgnoreCase("Yellow")) {
                                                TotalWeight += Double.parseDouble(edt2.getText().toString());
                                                yellowWeight += Double.parseDouble(edt2.getText().toString());
                                                yellowBag = 1;//Integer.parseInt(edt1.getText().toString());
                                                TotalYellowBag += yellowBag;
                                                TotalBags += yellowBag;
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Problem in bagscanning", Toast.LENGTH_SHORT).show();
                                            }
                                            Log.i("=TotalWeight=", "==" + TotalWeight);
                                            Log.i("=TotalBags=", "==" + TotalBags);

                                            try {

                                                final JSONArray jObjectOptionData1 = new JSONArray();

                                                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault());
                                                String currentDateandTime = sdf.format(new Date());

                                                JSONObject data = new JSONObject();
                                                String abl;
                                                data.put("OperationName", "BMWBagsCollectionDetailsManually");
                                                data.put("DailyVehicleRouteAssignmentID", userDetails.getString("start_id", "ABC123"));
                                                data.put("CompanyID", userDetails.getString("companyID", "1"));
                                                data.put("BranchID", userDetails.getString("branchID", "1"));
                                                data.put("HandsetID", userDetails.getString("HandsetID", "1"));
                                                data.put("VehicleID", userDetails.getString("vehicle_storeID", ""));
                                                data.put("DriverID", userDetails.getString("employee_storeID", ""));
                                                data.put("MembershipNo", userDetails.getString("MembershipNo", "1"));
                                                data.put("RouteID", userDetails.getString("route_storeID", ""));
                                                data.put("CollectionDateTime", currentDateandTime);
                                                data.put("BagQRCodeDetalID", 0);
                                                data.put("QEEID", hospital_id);
                                                data.put("ImageTitle", color_param+"-"+bag_id);
                                                data.put("NoOfBags", "1");
                                                data.put("Weight", edt2.getText().toString());
                                                data.put("Latitude", lat);
                                                data.put("Longitude", lon);
                                                jObjectOptionData1.put(data);
                                                abl = jObjectOptionData1.toString();
                                                Log.i("Data", "BMWBagsCollectionDetailsManually==" + abl);
                                                Toast.makeText(getApplicationContext(), "Database Saved : Weight = " + edt2.getText().toString(), Toast.LENGTH_SHORT).show();

                                                if (isConnectingToInternet()) {
                                                    new BMWBagsCollectionDetailsManuallyWS(bag_id).execute(abl);
                                                } else {
                                                    garbageDataStorageObject.open();
                                                    ContentValues cv = new ContentValues();
                                                    cv.put(GarbageDataStorage.DailyData, abl);
                                                    cv.put(GarbageDataStorage.DATATYPE, GarbageDataStorage.BAGS);
                                                    cv.put(GarbageDataStorage.IS_ON_SERVER, "False");
                                                    garbageDataStorageObject.insert(GarbageDataStorage.DATABASE_BAGS_VISIT, cv);
                                                    garbageDataStorageObject.close();
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            } finally {
                                                alertDialog.dismiss();
                                            }
                                        } else {
                                            Toast.makeText(getApplicationContext(), "SAME BAG SCANNED, So Data not Sent.", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(), "વજન ચકાસો.", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "વજન ચકાસો.", Toast.LENGTH_SHORT).show();
                                }
                                alertDialog.dismiss();
                                dialog.cancel();
                            }
                        });
                        btn_color_cancel.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Write your code here to invoke NO event
                                Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                                dialog.cancel();
                            }
                        });
                        alertDialog.show();
                    /*} else {
                        dialog.cancel();
                        Toast.makeText(getApplicationContext(), "આ ડૉક્ટર ને ફાળવેલી બેગ નથી.", Toast.LENGTH_SHORT).show();
                    }*/
                }
                /*else if (soapObject.getProperty("CheckValidBagManuallyResult").toString().equals("0")) {
                    dialog.cancel();
                    sameBag = false;
                    Toast.makeText(getApplicationContext(), "Bag is collected", Toast.LENGTH_SHORT).show();
                }*/ else {
                    dialog.cancel();
                    sameBag = false;
                    Toast.makeText(getApplicationContext(), "Any other issue", Toast.LENGTH_SHORT).show();
                }
            }
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    private class WS_UpdateWastePickedUpStatusFromHospital extends AsyncTask<String, String, String> {

        private String resp;
        String soap;
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            // Things to be done before execution of long running operation. For
            // example showing ProgessDialog
            dialog = new ProgressDialog(HospitalOne.this);
            dialog.setMessage("Please wait...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()
            try {
                Log.i("=params[0]", "params[0]" + params[0]);
                soap = WebService.webService_UpdateWastePickedUpStatusFromHospital.callWebserviceCheck("", hospital_id);
            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return soap;
        }

        @Override
        protected void onPostExecute(String stringVal) {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            if (stringVal != null && stringVal.length() >= 1 && stringVal.contains("|")) { //anyType{emailID=anyType{}; }

            }
        }
    }

    private class WS_UpdateBagWeightWastePickUpFromHospital extends AsyncTask<String, String, String> {

        private String resp;
        String soap;
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            // Things to be done before execution of long running operation. For
            // example showing ProgessDialog
            dialog = new ProgressDialog(HospitalOne.this);
            dialog.setMessage("Please wait...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()
            try {
                Log.i("=params[0]", "params[0]" + params[0]);
                //soap = WebService.webService_UpdateBagWeightWastePickUpFromHospital.callWebserviceCheck("","","");
            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return soap;
        }

        @Override
        protected void onPostExecute(String stringVal) {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            if (stringVal != null && stringVal.length() >= 1 && stringVal.contains("|")) { //anyType{emailID=anyType{}; }

            }
        }
    }

    public boolean validate(final String hex) {

        matcher = pattern.matcher(hex);
        return matcher.matches();

    }

    /**
     * Decoding the file and returning Bitmap's Object
     *
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
     *
     * @param bit : Bitmap selected by User
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
     * Used for converting bitmap to byte Array
     *
     * @param bitmap
     * @return byte[]
     */
    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, 90, stream);
        return stream.toByteArray();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        //if(data != null) {
        if (requestCode == 1337) {
            if (resultCode != 0) {
                if (data == null) {
                    camera_flag = true;
                    File path = new File(Environment.getExternalStorageState() + "/BMWPhoto/");
                    if (!path.exists())
                        path.mkdirs();

                    Log.i("==", "==1337==" + HomeActivity.path.toString());
                    File fileObject = new File(HomeActivity.path.toString() + "/DP.png");
                    bitm = decodeFile(fileObject);
                    Bitmap workingBitmap = Bitmap.createBitmap(bitm);
                    Bitmap mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);

                    try {
                        FileOutputStream out = new FileOutputStream(HomeActivity.path.toString() + "/DP1.png");

                        Canvas canvas = new Canvas(mutableBitmap);

                        Paint paint = new Paint();
                        paint.setColor(Color.RED); // Text Color
                        paint.setStrokeWidth(45); // Text Size
                        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)); // Text Overlapping Pattern
                        // some more settings...

                        canvas.drawBitmap(mutableBitmap, 0, 0, paint);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
                        String currentDateandTime = sdf.format(new Date());
                        canvas.drawText(currentDateandTime, 10, 25, paint);

                        mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

                        out.flush();
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    valBase64 = getBase64Value(mutableBitmap);

                    if (isConnectingToInternet()) {
                        new WebserviceCheckHospital().execute(hospital_id);
                    } else {
                        Toast.makeText(getApplicationContext(), "Please check your Internet Connection.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } else {//) if(requestCode == 2 && data!=null){
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (isOpenOrClose.equalsIgnoreCase("open") || isOpenOrClose.equalsIgnoreCase("close") && camera_flag == true) {
                if (result.getContents() != null) {
                    //final String[] tokens = data.getStringExtra("ColorValue").split(Pattern.quote("|"));
                    final String[] tokens = result.getContents().split(Pattern.quote("|"));//YELLOW-01071900093-SHUBH390020GJBH100|77108
                    final String[] colorToken = tokens[0].split(Pattern.quote("-"));
                    final String membershipFullNo = tokens[0].substring(tokens[0].length() - 5);//BH100
                    final String[] part = membershipFullNo.split("(?<=\\D)(?=\\d)"); //BH 100
                    //System.out.println(part[0]);
                    //System.out.println(part[1]);
                    String onlyID = userDetails.getString("MembershipNo", "1");
                    checkBoxAndScan = true;

                    if (isConnectingToInternet()) {
                        if (!bagsArray.contains(tokens[1])) {
                            if (onlyID.equalsIgnoreCase(part[1])) {
                                cb.setEnabled(false);
                                //YELLOW       100       77108              77108
                                new CheckForUsedBagQRCodeWS(colorToken[0], part[1], tokens[1]).execute(tokens[1]); //MembershipNo
                            } else {
                                Toast.makeText(getApplicationContext(), "આ ડૉક્ટર ને ફાળવેલી બેગ નથી.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Bag already scanned", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        sameBag = true;
                        if (onlyID.equalsIgnoreCase(part[1])) { //HospitalID
                            ColorValue = String.valueOf(colorToken[0]);
                            final Dialog alertDialog = new Dialog(HospitalOne.this);
                            LayoutInflater inflater = HospitalOne.this.getLayoutInflater();
                            //final View dialogColor = inflater.inflate(R.layout.dialog_color, null);
                            alertDialog.setContentView(R.layout.dialog_color);
                            //alertDialog.setView(dialogColor);

                            //edt1 = (EditText) dialogColor.findViewById(R.id.edit_bag1);
                            edt2 = (EditText) alertDialog.findViewById(R.id.edit_kg_1);
                            btn_color_ok = (Button) alertDialog.findViewById(R.id.btn_color_ok);
                            btn_color_cancel = (Button) alertDialog.findViewById(R.id.btn_color_cancel);

                            /*one = (TextView) alertDialog.findViewById(R.id.text_enter_bags);
                            two = (TextView) alertDialog.findViewById(R.id.text_enter_kg);

                            Typeface font = Typeface.createFromAsset(getAssets(), "SHRUTI.TTF");
                            one.setTypeface(font);
                            one.setTypeface(one.getTypeface(), Typeface.BOLD);
                            one.setText("ભરેલી બેગ લેવાની નંગ ");
                            two.setTypeface(font);
                            two.setTypeface(two.getTypeface(), Typeface.BOLD);
                            two.setText("લીધેલી બેગ વજન kg");*/

                            TableLayout tblRow = (TableLayout) alertDialog.findViewById(R.id.tableLayout1);

                            if (ColorValue.equalsIgnoreCase("Blue")) {
                                tblRow.setBackgroundColor(getResources().getColor(R.color.blue));
                                ColorValue = "Blue";
                            } else if (ColorValue.equalsIgnoreCase("Red")) {
                                tblRow.setBackgroundColor(getResources().getColor(R.color.red));
                                ColorValue = "Red";
                            } else if (ColorValue.equalsIgnoreCase("White")) {
                                tblRow.setBackgroundColor(getResources().getColor(R.color.white));
                                ColorValue = "White";
                            } else if (ColorValue.equalsIgnoreCase("Yellow")) {
                                tblRow.setBackgroundColor(getResources().getColor(R.color.yellow));
                                ColorValue = "Yellow";
                            } else {
                                tblRow.setBackgroundColor(getResources().getColor(R.color.black));
                            }

                            mContent = (LinearLayout) alertDialog.findViewById(R.id.layout_sign);
                            alertDialog.setTitle("Add Bag Weight.");
                            alertDialog.setCancelable(false);
                            btn_color_ok.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (edt2.getText().length() != 0) {
                                        if (Double.parseDouble(edt2.getText().toString()) <= 999) {
                                            if (sameBag) {
                                                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                                                //callDailyWS();

                                                Log.i("0=TotalWeight Before=", "==" + TotalWeight);
                                                Log.i("0=TotalBags=", "==" + TotalBags);

                                                if (edt2.getText().toString().length() == 0)
                                                    edt2.setText("0");

                                                if (ColorValue.equalsIgnoreCase("Blue")) {
                                                    TotalWeight += Double.parseDouble(edt2.getText().toString());
                                                    blueWeight += Double.parseDouble(edt2.getText().toString());
                                                    blueBag = 1;//Integer.parseInt(edt1.getText().toString());
                                                    TotalBlueBag += blueBag;
                                                    TotalBags += blueBag;
                                                } else if (ColorValue.equalsIgnoreCase("Red")) {
                                                    TotalWeight += Double.parseDouble(edt2.getText().toString());
                                                    redWeight += Double.parseDouble(edt2.getText().toString());
                                                    redBag = 1;//Integer.parseInt(edt1.getText().toString());
                                                    TotalRedBag += redBag;
                                                    TotalBags += redBag;
                                                } else if (ColorValue.equalsIgnoreCase("White")) {
                                                    TotalWeight += Double.parseDouble(edt2.getText().toString());
                                                    whiteWeight += Double.parseDouble(edt2.getText().toString());
                                                    whiteBag = 1;//Integer.parseInt(edt1.getText().toString());
                                                    TotalWhiteBag += whiteBag;
                                                    TotalBags += whiteBag;
                                                } else if (ColorValue.equalsIgnoreCase("Yellow")) {
                                                    TotalWeight += Double.parseDouble(edt2.getText().toString());
                                                    yellowWeight += Double.parseDouble(edt2.getText().toString());
                                                    yellowBag = 1;//Integer.parseInt(edt1.getText().toString());
                                                    TotalYellowBag += yellowBag;
                                                    TotalBags += yellowBag;
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "Problem in bag scanning", Toast.LENGTH_SHORT).show();
                                                }
                                                Log.i("=TotalWeight After=", "==" + TotalWeight);
                                                Log.i("=TotalBags=", "==" + TotalBags);

                                                try {

                                                    final JSONArray jObjectOptionData1 = new JSONArray();

                                                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault());
                                                    String currentDateandTime = sdf.format(new Date());

                                                    JSONObject data = new JSONObject();
                                                    String ablNew;
                                                    data.put("OperationName", "BMWBagsCollectionDetails");
                                                    data.put("DailyVehicleRouteAssignmentID", userDetails.getString("start_id", "ABC123"));
                                                    data.put("CompanyID", userDetails.getString("companyID", "1"));
                                                    data.put("BranchID", userDetails.getString("branchID", "1"));
                                                    data.put("HandsetID", userDetails.getString("HandsetID", "1"));
                                                    data.put("VehicleID", userDetails.getString("vehicle_storeID", ""));
                                                    data.put("DriverID", userDetails.getString("employee_storeID", ""));
                                                    data.put("MembershipNo", userDetails.getString("MembershipNo", "1"));
                                                    data.put("RouteID", userDetails.getString("route_storeID", ""));
                                                    data.put("CollectionDateTime", currentDateandTime);
                                                    data.put("BagQRCodeDetalID", tokens[1]);
                                                    data.put("NoOfBags", "1");
                                                    data.put("Weight", edt2.getText().toString());
                                                    data.put("Latitude", lat);
                                                    data.put("Longitude", lon);
                                                    jObjectOptionData1.put(data);
                                                    ablNew = jObjectOptionData1.toString();
                                                    Log.i("Data", "BMWBagsCollectionDetails==" + ablNew);
                                                    Toast.makeText(getApplicationContext(), "Weight = " + edt2.getText().toString(), Toast.LENGTH_SHORT).show();

                                                    garbageDataStorageObject.open();
                                                    ContentValues cv = new ContentValues();
                                                    cv.put(GarbageDataStorage.DailyData, ablNew);
                                                    cv.put(GarbageDataStorage.DATATYPE, GarbageDataStorage.BAGS);
                                                    cv.put(GarbageDataStorage.IS_ON_SERVER, "False");
                                                    garbageDataStorageObject.insert(GarbageDataStorage.DATABASE_BAGS_VISIT, cv);
                                                    garbageDataStorageObject.close();

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                } finally {
                                                    alertDialog.dismiss();
                                                }
                                            } else {
                                                Toast.makeText(getApplicationContext(), "બેગ અગાઉં લેવાયેલ છે.", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(getApplicationContext(), "વજન ચકાસો.", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(), "વજન ચકાસો.", Toast.LENGTH_SHORT).show();
                                    }
                                    alertDialog.dismiss();

                                }
                            });
                            btn_color_cancel.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog.dismiss();
                                }
                            });

                            // Showing Alert Message
                            alertDialog.show();
                        } else {
                            Toast.makeText(getApplicationContext(), "આ ડૉક્ટર ને ફાળવેલી બેગ નથી.", Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Error Occured!!!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Please Take a Picture.", Toast.LENGTH_SHORT).show();
            }
        }
        //}else{        }
    }

    public boolean isConnectingToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) HospitalOne.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }

    public void callDailyWS(boolean offlineFlag) {
        try {
            String abl = null;
            if (offlineFlag) {
                if (onlyOneVisit && isOpenOrClose.equals("open")) {
                    final JSONArray jObjectOptionData1 = new JSONArray();
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault());
                    SimpleDateFormat Week = new SimpleDateFormat("EEEE");
                    String currentDateandTime = sdf.format(new Date());

                    JSONObject data = new JSONObject();
                    data.put("OperationName", "DailyHospitalVisitDetails");
                    data.put("DailyVehicleRouteAssignmentID", userDetails.getString("start_id", "ABC123"));
                    data.put("CompanyID", userDetails.getString("companyID", "1"));
                    data.put("BranchID", userDetails.getString("branchID", "1"));
                    data.put("HandsetID", userDetails.getString("HandsetID", "1"));
                    data.put("VehicleID", userDetails.getString("vehicle_storeID", "1"));
                    data.put("DriverID", userDetails.getString("employee_storeID", "1"));
                    data.put("MembershipNo", userDetails.getString("MembershipNo", "1"));
                    data.put("RouteID", userDetails.getString("route_storeID", ""));
                    data.put("VisitDay", Week.format(new Date()));
                    data.put("CollectionDateTime", currentDateandTime);
                    data.put("IsHandsetStartDay", "true");
                    data.put("IsHandsetEndDay", "false");
                    data.put("IsHospitalOpen", "true");
                    data.put("ClosedHospitalPicture", "false");
                    data.put("Latitude", lat);
                    data.put("Longitude", lon);
                    jObjectOptionData1.put(data);
                    abl = jObjectOptionData1.toString();

                    garbageDataStorageObject.open();
                    ContentValues cv = new ContentValues();
                    cv.put(GarbageDataStorage.DailyData, abl);
                    cv.put(GarbageDataStorage.DATATYPE, GarbageDataStorage.DAILY);
                    cv.put(GarbageDataStorage.IS_ON_SERVER, "False");
                    garbageDataStorageObject.insert(GarbageDataStorage.DATABASE_DAILY_VISIT, cv);
                    garbageDataStorageObject.close();

                } else {
                    final JSONArray jObjectOptionData1 = new JSONArray();
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault());
                    SimpleDateFormat Week = new SimpleDateFormat("EEEE");
                    String currentDateandTime = sdf.format(new Date());

                    JSONObject data = new JSONObject();
                    data.put("OperationName", "DailyHospitalVisitDetails");
                    data.put("DailyVehicleRouteAssignmentID", userDetails.getString("start_id", "ABC123"));
                    data.put("CompanyID", userDetails.getString("companyID", "1"));
                    data.put("BranchID", userDetails.getString("branchID", "1"));
                    data.put("HandsetID", userDetails.getString("HandsetID", "1"));
                    data.put("VehicleID", userDetails.getString("vehicle_storeID", "1"));
                    data.put("DriverID", userDetails.getString("employee_storeID", "1"));
                    data.put("MembershipNo", userDetails.getString("MembershipNo", "1"));
                    data.put("RouteID", userDetails.getString("route_storeID", ""));
                    data.put("VisitDay", Week.format(new Date()));
                    data.put("CollectionDateTime", currentDateandTime);
                    data.put("IsHandsetStartDay", "true");
                    data.put("IsHandsetEndDay", "false");
                    data.put("IsHospitalOpen", "false");
                    data.put("ClosedHospitalPicture", valBase64);
                    data.put("Latitude", lat);
                    data.put("Longitude", lon);
                    jObjectOptionData1.put(data);
                    abl = jObjectOptionData1.toString();

                    garbageDataStorageObject.open();
                    ContentValues cv = new ContentValues();
                    cv.put(GarbageDataStorage.DailyData, abl);
                    cv.put(GarbageDataStorage.DATATYPE, GarbageDataStorage.DAILY);
                    cv.put(GarbageDataStorage.IS_ON_SERVER, "False");
                    garbageDataStorageObject.insert(GarbageDataStorage.DATABASE_DAILY_VISIT, cv);
                    garbageDataStorageObject.close();

                }
            } else {
                if (onlyOneVisit && isOpenOrClose.equals("open")) {
                    final JSONArray jObjectOptionData1 = new JSONArray();
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault());
                    SimpleDateFormat Week = new SimpleDateFormat("EEEE");
                    String currentDateandTime = sdf.format(new Date());

                    JSONObject data = new JSONObject();
                    data.put("OperationName", "DailyHospitalVisitDetails");
                    data.put("DailyVehicleRouteAssignmentID", userDetails.getString("start_id", "ABC123"));
                    data.put("CompanyID", userDetails.getString("companyID", "1"));
                    data.put("BranchID", userDetails.getString("branchID", "1"));
                    data.put("HandsetID", userDetails.getString("HandsetID", "1"));
                    data.put("VehicleID", userDetails.getString("vehicle_storeID", "1"));
                    data.put("DriverID", userDetails.getString("employee_storeID", "1"));
                    data.put("MembershipNo", userDetails.getString("MembershipNo", "1"));
                    data.put("RouteID", userDetails.getString("route_storeID", ""));
                    data.put("VisitDay", Week.format(new Date()));
                    data.put("CollectionDateTime", currentDateandTime);
                    data.put("IsHandsetStartDay", "true");
                    data.put("IsHandsetEndDay", "false");

                    data.put("IsHospitalOpen", "true");
                    data.put("ClosedHospitalPicture", "false");

                    data.put("Latitude", lat);
                    data.put("Longitude", lon);

                    jObjectOptionData1.put(data);
                    abl = jObjectOptionData1.toString();
                    if (isConnectingToInternet()) {
                        new DailyHospitalVisitDetailsWS().execute(abl); //MembershipNo
                    } else {
                        garbageDataStorageObject.open();
                        ContentValues cv = new ContentValues();
                        cv.put(GarbageDataStorage.DailyData, abl);
                        cv.put(GarbageDataStorage.DATATYPE, GarbageDataStorage.DAILY);
                        cv.put(GarbageDataStorage.IS_ON_SERVER, "False");
                        garbageDataStorageObject.insert(GarbageDataStorage.DATABASE_DAILY_VISIT, cv);
                        garbageDataStorageObject.close();
                    }
                } else {
                    final JSONArray jObjectOptionData1 = new JSONArray();
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault());
                    SimpleDateFormat Week = new SimpleDateFormat("EEEE");
                    String currentDateandTime = sdf.format(new Date());

                    JSONObject data = new JSONObject();
                    data.put("OperationName", "DailyHospitalVisitDetails");
                    data.put("DailyVehicleRouteAssignmentID", userDetails.getString("start_id", "ABC123"));
                    data.put("CompanyID", userDetails.getString("companyID", "1"));
                    data.put("BranchID", userDetails.getString("branchID", "1"));
                    data.put("HandsetID", userDetails.getString("HandsetID", "1"));
                    data.put("VehicleID", userDetails.getString("vehicle_storeID", "1"));
                    data.put("DriverID", userDetails.getString("employee_storeID", "1"));
                    data.put("MembershipNo", userDetails.getString("MembershipNo", "1"));
                    data.put("RouteID", userDetails.getString("route_storeID", ""));
                    data.put("VisitDay", Week.format(new Date()));
                    data.put("CollectionDateTime", currentDateandTime);
                    data.put("IsHandsetStartDay", "true");
                    data.put("IsHandsetEndDay", "false");

                    data.put("IsHospitalOpen", "false");
                    data.put("ClosedHospitalPicture", valBase64);


                    data.put("Latitude", lat);
                    data.put("Longitude", lon);

                    jObjectOptionData1.put(data);
                    abl = jObjectOptionData1.toString();
                    if (isConnectingToInternet()) {
                        new DailyHospitalVisitDetailsWS().execute(abl); //MembershipNo
                    } else {
                        garbageDataStorageObject.open();
                        ContentValues cv = new ContentValues();
                        cv.put(GarbageDataStorage.DailyData, abl);
                        cv.put(GarbageDataStorage.DATATYPE, GarbageDataStorage.DAILY);
                        cv.put(GarbageDataStorage.IS_ON_SERVER, "False");
                        garbageDataStorageObject.insert(GarbageDataStorage.DATABASE_DAILY_VISIT, cv);
                        garbageDataStorageObject.close();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void callBagWS(String BagQRCodeDetailID, String weight) {
        try {

            final JSONArray jObjectOptionData1 = new JSONArray();

            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date());

            JSONObject data = new JSONObject();
            String ablNew;
            data.put("OperationName", "BMWBagsCollectionDetails");
            data.put("DailyVehicleRouteAssignmentID", userDetails.getString("start_id", "ABC123"));
            data.put("CompanyID", userDetails.getString("companyID", "1"));
            data.put("BranchID", userDetails.getString("branchID", "1"));
            data.put("HandsetID", userDetails.getString("HandsetID", "1"));
            data.put("VehicleID", userDetails.getString("vehicle_storeID", ""));
            data.put("DriverID", userDetails.getString("employee_storeID", ""));
            data.put("MembershipNo", userDetails.getString("MembershipNo", "1"));
            data.put("RouteID", userDetails.getString("route_storeID", ""));
            data.put("CollectionDateTime", currentDateandTime);
            data.put("BagQRCodeDetalID", BagQRCodeDetailID);
            data.put("NoOfBags", "1");
            data.put("Weight", weight);
            data.put("Latitude", lat);
            data.put("Longitude", lon);
            jObjectOptionData1.put(data);
            ablNew = jObjectOptionData1.toString();
            Log.i("Data", "BMWBagsCollectionDetails==" + ablNew);
            Toast.makeText(getApplicationContext(), "Weight = " + weight, Toast.LENGTH_SHORT).show();

            if (isConnectingToInternet()) {
                new BMWBagsCollectionDetailsWS(BagQRCodeDetailID).execute(ablNew);
            } else {
                garbageDataStorageObject.open();
                ContentValues cv = new ContentValues();
                cv.put(GarbageDataStorage.DailyData, ablNew);
                cv.put(GarbageDataStorage.DATATYPE, GarbageDataStorage.BAGS);
                cv.put(GarbageDataStorage.IS_ON_SERVER, "False");
                garbageDataStorageObject.insert(GarbageDataStorage.DATABASE_BAGS_VISIT, cv);
                garbageDataStorageObject.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class signature extends View {
        private static final float STROKE_WIDTH = 5f;
        private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
        private Paint paint = new Paint();
        private Path path = new Path();

        private float lastTouchX;
        private float lastTouchY;
        private final RectF dirtyRect = new RectF();

        public signature(Context context, AttributeSet attrs) {
            super(context, attrs);
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(STROKE_WIDTH);
        }

        public void save(View v) {
            Log.v("log_tag", "Width: " + v.getWidth());
            Log.v("log_tag", "Height: " + v.getHeight());
            if (mBitmap == null) {
                mBitmap = Bitmap.createBitmap(mContent.getWidth(), mContent.getHeight(), Bitmap.Config.RGB_565);
                ;
            }
            Canvas canvas = new Canvas(mBitmap);
            try {
                File path = new File(Environment.getExternalStorageDirectory() + "/BMWPhoto/");
                FileOutputStream mFileOutStream = new FileOutputStream(path + "/DP3.png");
                v.draw(canvas);
                mBitmap.compress(Bitmap.CompressFormat.PNG, 90, mFileOutStream);
                mFileOutStream.flush();
                mFileOutStream.close();
                String url = MediaStore.Images.Media.insertImage(getContentResolver(), mBitmap, "title", null);
                Log.v("log_tag", "url: " + url);
                valBase64Sign = getBase64Value(mBitmap);
                //signBool = true;

                //In case you want to delete the file
                //boolean deleted = mypath.delete();
                //Log.v("log_tag","deleted: " + mypath.toString() + deleted);
                //If you want to convert the image to string use base64 converter

            } catch (Exception e) {
                Log.v("log_tag", e.toString());
            }
        }

        public void clear() {
            path.reset();
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawPath(path, paint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float eventX = event.getX();
            float eventY = event.getY();
            //mGetSign.setEnabled(true);
            signBool = true;

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    path.moveTo(eventX, eventY);
                    lastTouchX = eventX;
                    lastTouchY = eventY;
                    return true;

                case MotionEvent.ACTION_MOVE:

                case MotionEvent.ACTION_UP:

                    resetDirtyRect(eventX, eventY);
                    int historySize = event.getHistorySize();
                    for (int i = 0; i < historySize; i++) {
                        float historicalX = event.getHistoricalX(i);
                        float historicalY = event.getHistoricalY(i);
                        expandDirtyRect(historicalX, historicalY);
                        path.lineTo(historicalX, historicalY);
                    }
                    path.lineTo(eventX, eventY);
                    break;

                default:
                    debug("Ignored touch event: " + event.toString());
                    return false;
            }

            invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.top - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.right + HALF_STROKE_WIDTH),
                    (int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

            lastTouchX = eventX;
            lastTouchY = eventY;

            return true;
        }

        private void debug(String string) {
        }

        private void expandDirtyRect(float historicalX, float historicalY) {
            if (historicalX < dirtyRect.left) {
                dirtyRect.left = historicalX;
            } else if (historicalX > dirtyRect.right) {
                dirtyRect.right = historicalX;
            }

            if (historicalY < dirtyRect.top) {
                dirtyRect.top = historicalY;
            } else if (historicalY > dirtyRect.bottom) {
                dirtyRect.bottom = historicalY;
            }
        }

        private void resetDirtyRect(float eventX, float eventY) {
            dirtyRect.left = Math.min(lastTouchX, eventX);
            dirtyRect.right = Math.max(lastTouchX, eventX);
            dirtyRect.top = Math.min(lastTouchY, eventY);
            dirtyRect.bottom = Math.max(lastTouchY, eventY);
        }
    }
}