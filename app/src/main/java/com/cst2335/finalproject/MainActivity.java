package com.cst2335.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonStart = (Button) findViewById(R.id.button_start);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // your handler code here
                Intent ticketMasterMainPage = new Intent(MainActivity.this, TicketMasterMainActivity.class);

                //EditText userName = (EditText)  findViewById(R.id.editTextUserName);
                //profilePage.putExtra("EMAIL", userName.getText().toString());

                startActivity(ticketMasterMainPage);
            }
        });
    }
}