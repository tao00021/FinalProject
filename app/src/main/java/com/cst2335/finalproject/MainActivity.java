package com.cst2335.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Below is to show how Both API works. Please note you only need to use the two API classes
         * to retrieve items from ticketMaster web server, and to save data to database.
         */
        //the following is used to get events from ticket master

        /*
        List<TicketMasterEvent> eventFromTicketMasterFromOttawa=TicketMasterAPI.getEventsFromTicketmaster("Ottawa","25","1");
        List<TicketMasterEvent> eventFromTicketMasterFromToronto=TicketMasterAPI.getEventsFromTicketmaster("Toronto","25","1");


        //Create a database API
        TicketMasterDatabaseAPI databaseAPI = new TicketMasterDatabaseAPI(getApplicationContext());
        //use the following to save the ottawa events to the database
        for(TicketMasterEvent ticketMasterEvent:eventFromTicketMasterFromOttawa)
            databaseAPI.saveNewEvent(ticketMasterEvent);
        //use the following to save the toronto events to the database
        for(TicketMasterEvent ticketMasterEvent:eventFromTicketMasterFromToronto)
            databaseAPI.saveNewEvent(ticketMasterEvent);

        eventFromTicketMasterFromOttawa=databaseAPI.getAllEventsForCity("Ottawa");
        eventFromTicketMasterFromToronto=databaseAPI.getAllEventsForCity("Toronto");
        databaseAPI.removeEventsFromCity("Ottawa");
        eventFromTicketMasterFromOttawa=databaseAPI.getAllEventsForCity("Ottawa");//this should be zero
        */

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String savedEmail = sharedPref.getString("savedHomeCity","");
        EditText homeCityEditText = (EditText)  findViewById(R.id.editTextHomeCity);
        homeCityEditText.setText(savedEmail);

        Button buttonStart = (Button) findViewById(R.id.button_start);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // your handler code here
                Intent ticketMasterMainPage = new Intent(MainActivity.this, TicketMasterMainActivity.class);

                EditText homeCityEditText = (EditText)  findViewById(R.id.editTextHomeCity);
                ticketMasterMainPage.putExtra("homeCity", homeCityEditText.getText().toString());

                startActivity(ticketMasterMainPage);
            }
        });
    }


    @Override
    protected void onPause()
    {
        super.onPause();

        EditText homeCityEditText = (EditText)  findViewById(R.id.editTextHomeCity);

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("savedHomeCity", homeCityEditText.getText().toString());
        editor.commit();
    }
}