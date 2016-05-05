package com.example.mugenklaus.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;

import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.pass.Spass;
import com.samsung.android.sdk.pass.SpassFingerprint;

import java.util.ArrayList;
import java.util.List;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity  {


    private SpassFingerprint mSpassFingerprint;
    private Context mContext;
    public static String TAG = "@@@@@@ DEBUG @@@@@@";
    private Spass mSpass;
    private boolean isFeatureEnabled;
    Intent in;
    private int backPressed = 0;

    private SpassFingerprint.IdentifyListener listener = new SpassFingerprint.IdentifyListener() {
        @Override
        public void onFinished(int eventStatus) {
            //THIS METHOD IS CALLED AFTER CORRRECT AUTHENTICATION OF USER
            if(eventStatus == SpassFingerprint.STATUS_AUTHENTIFICATION_SUCCESS){
                // called after the fingerprint has been authenticated.

                Log.d(TAG,String.format("\nAuthentication Success\nFingerprintNames: %s\nFinger Index: %s\nFingerPrint Uiniqe ID: %s",
                        mSpassFingerprint.getRegisteredFingerprintName(), mSpassFingerprint.getIdentifiedFingerprintIndex(), mSpassFingerprint.getRegisteredFingerprintUniqueID()));

                in = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(in);
                //calls the OnFishsh of this activity after going to the next intent
                finish();

            }else if(eventStatus == SpassFingerprint.STATUS_AUTHENTIFICATION_PASSWORD_SUCCESS){
                Log.d(TAG, String.format("\nAuthentication Success with password\nFingerprintNames: %s\nFinger Index: %s\nFingerPrint Uiniqe ID: %s",
                        mSpassFingerprint.getRegisteredFingerprintName(), mSpassFingerprint.getIdentifiedFingerprintIndex(), mSpassFingerprint.getRegisteredFingerprintUniqueID()));
            }else{
                Log.d(TAG,"SOMETHING HAPPENDED");
                finish();
                //mSpassFingerprint.startIdentifyWithDialog(LoginActivity.this, listener, false);
            }

        }

        @Override
        public void onReady() {
            //method is used when fingerprint scanner is ready
            Log.d(TAG, "Identity state is ready bring dat finger over");
        }

        @Override
        public void onStarted() {
            //sensor touched
            Log.d(TAG, "Finger print Sensor touched");

        }

        @Override
        public void onCompleted() {
            //called when the process of authenticaiton is complete
            Log.d(TAG,"The identify is compledted");
        }
    };



    @Override
    public void onBackPressed(){
        Toast.makeText(getApplicationContext(), "Click back again", Toast.LENGTH_LONG).show();

    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //new context
        mContext = this;
        mSpass = new Spass();
        try{
            Log.d(TAG,"Initalizing sPass.....");

            mSpass.initialize(LoginActivity.this);

        }catch (SsdkUnsupportedException e){
            Log.d(TAG, "Errpr: " + e);
        }
        isFeatureEnabled = mSpass.isFeatureEnabled(Spass.DEVICE_FINGERPRINT);

        if(isFeatureEnabled){
            mSpassFingerprint = new SpassFingerprint(LoginActivity.this);
            Log.d(TAG, "THIS deivce IS SUPPORTED");
        } else{
            Log.d(TAG, "Fingerprint service is not suppored on this device");
        }

        //This changes the Title text of the Fingerprint UI box
        if(mSpass.isFeatureEnabled(mSpass.DEVICE_FINGERPRINT_CUSTOMIZED_DIALOG)){
            //changes the title String and Color
            mSpassFingerprint.setDialogTitle("Requires Biometric Auth.", 0xff0000);

            //changes the transparency
            mSpassFingerprint.setDialogBgTransparency(0);

            //Stop UI dismissing
            mSpassFingerprint.setCanceledOnTouchOutside(false);


            Log.d(TAG,"The dialog cust IS ENABLED");
        }else{
            Log.d(TAG,"The cust dialog is not enabled");
        }



        if(mSpass.isFeatureEnabled(mSpass.DEVICE_FINGERPRINT_AVAILABLE_PASSWORD)){
            Log.d(TAG,"Password abaliable");
            mSpassFingerprint.setDialogButton("OWN BUTTON");
        }else{
            Log.d(TAG,"THis function is NOT activated");
        }



        //call the fingerprint service and diaplay a dialog, calling the sPay listerneer
        mSpassFingerprint.startIdentifyWithDialog(LoginActivity.this, listener, false);
        //mSpassFingerprint.startIdentify(listener);

    }

}

