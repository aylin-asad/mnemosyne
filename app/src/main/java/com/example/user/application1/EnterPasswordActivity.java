package com.example.user.application1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EnterPasswordActivity extends AppCompatActivity {

    EditText editText;
    Button button;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_password);

        SharedPreferences settings = getSharedPreferences("PREFS", 0);
        password = settings.getString("password", "");

        editText = (EditText)findViewById(R.id.editText);
        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String text = editText.getText().toString();

                if(text.equals(password)) {
                    Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(EnterPasswordActivity.this, "Wrong Password!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button homeScreenBttn = (Button)findViewById(R.id.buttonHome);
        homeScreenBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), MainActivity.class);
                //how to pass information to the second screen/activity
                startActivity(startIntent);
            }
        });


    }
}
