package com.example.user.application1;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String mPerms[] = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Ask for permision
            ActivityCompat.requestPermissions(this,new String[] { Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        else {
// Permission has already been granted
        }

        Button mapActivityBttn = (Button)findViewById(R.id.Map);
        mapActivityBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), MapsActivity.class);
                //how to pass information to the second screen/activity
                startActivity(startIntent);
            }
        });
        //Attempt to launch an activity outside our App

        Button googleBttn = (Button)findViewById(R.id.Google);
        googleBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String google = "https://hakunamatata2019.wixsite.com/website-1";
                Uri webaddress = Uri.parse(google);

                Intent gotoGoogle = new Intent(Intent.ACTION_VIEW, webaddress);
                if(gotoGoogle.resolveActivity((getPackageManager())) != null){
                    startActivity(gotoGoogle);
                }

            }
        });

        Button settingsBttn = (Button) findViewById(R.id.Settings);

        SharedPreferences settings = getSharedPreferences("PREFS", 0);
        password = settings.getString("password", "");

        settingsBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(password.equals("")) {
                    Intent intent  = new Intent(getApplicationContext(), CreatePasswordActivity.class);
                    startActivity(intent);
                    //finish();
                } else {
                    Intent intent = new Intent(getApplicationContext(), EnterPasswordActivity.class);
                    startActivity(intent);
                    //finish();
                }
            }
//                }, 2000);
//
//
//            }
        });

        Button emergencyButton = (Button)findViewById(R.id.emergencyButton);
        emergencyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = (String)("+" + Globals.phoneNumber);
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);

            }
        });

    }
}
