package com.cst2335.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class EventDetailActivity extends AppCompatActivity {

    TicketMasterEvent selectedEvent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        if(SearchResultActivity.isTicketMasterQuery)
            ((Button) findViewById(R.id.EventSaveButton)).setText("Save");
        else
            ((Button) findViewById(R.id.EventSaveButton)).setText("Delete");
        selectedEvent = (TicketMasterEvent)getIntent().getSerializableExtra("eventObject");
        ((TextView) findViewById(R.id.textViewEventDate)).setText(selectedEvent.getDate()+" / "+selectedEvent.getTime());

        if(selectedEvent.getMaxPrice().equals("N/A")) {
            ((TextView) findViewById(R.id.textViewEventPrice)).setText("N/A");
        }
        else
            ((TextView) findViewById(R.id.textViewEventPrice)).setText("$"+selectedEvent.getMinPrice()+":$"+selectedEvent.getMaxPrice());

        ((TextView) findViewById(R.id.textViewEventName)).setText(selectedEvent.getName());
        ((TextView) findViewById(R.id.textViewEventURL)).setText(selectedEvent.getEventUrl());
        TicketMasterAPI.LoadImageFromWebOperations((ImageView) findViewById(R.id.imageViewEventPic),selectedEvent.getImageUrl());
    }

    public void saveEvent(View view)
    {
        TicketMasterDatabaseAPI database=new TicketMasterDatabaseAPI(this);
        if(SearchResultActivity.isTicketMasterQuery)
            database.saveNewEvent(selectedEvent);
        else
            database.removeEvent(selectedEvent.getID());

         finish();


        }
}