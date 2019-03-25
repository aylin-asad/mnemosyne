package com.example.user.application1;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    EditText textBox1, textBox2, textBox3;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        textBox1 = (EditText)findViewById(R.id.latVal);
        textBox2 = (EditText)findViewById(R.id.longVal);
        textBox3 = (EditText)findViewById(R.id.phoneNumber);
        submitButton = (Button)findViewById(R.id.submitButton);


        Button homeBttn = (Button)findViewById(R.id.buttonHome);
        homeBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), MainActivity.class);
                //how to pass information to the second screen/activity
                startActivity(startIntent);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Globals.FTlatitude = Double.valueOf(textBox1.getText().toString());
                //String str = textBox.getText().toString();
                Globals.FTlongitude = Double.valueOf(textBox2.getText().toString());
                Globals.phoneNumber = textBox3.getText().toString();

                SharedPreferences settings = getSharedPreferences("PREFS", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putFloat("homeLatValue", (float)Globals.FTlatitude);
                editor.putFloat("homeLongValue", (float)Globals.FTlongitude);
                editor.putString("phoneNumber", Globals.phoneNumber);
                editor.apply();


                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                //intent.putExtra("message", d1);

                startActivity(intent);
            }
        });



    }
}
