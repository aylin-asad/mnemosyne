package com.example.user.application1;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
    }
}
