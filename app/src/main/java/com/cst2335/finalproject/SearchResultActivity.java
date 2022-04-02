package com.cst2335.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;


public class SearchResultActivity extends AppCompatActivity {

    private List<TicketMasterEvent> ticketMasterEvents;
    private String City = null;
    private String Radius = null;
    public static boolean isTicketMasterQuery;
    MyListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);


        Intent fromLastPage = getIntent();
        City = fromLastPage.getStringExtra("City");
        Radius = fromLastPage.getStringExtra("Radius");
        String pageNumber = fromLastPage.getStringExtra("pageNumber");
        String totalPageNumber = fromLastPage.getStringExtra("totalPageNumber");
       // if(fromLastPage.getStringExtra("TicketmasterQuery").equals("true"))
        if(pageNumber==null)
            pageNumber="1";

        if(isTicketMasterQuery)
        {
            ticketMasterEvents=TicketMasterAPI.getEventsFromTicketmaster(City,Radius,pageNumber);
            if(totalPageNumber==null)
                totalPageNumber=String.valueOf(TicketMasterAPI.totalNumberOfPages(City,Radius));


        }
        else
        {
            TicketMasterDatabaseAPI database = new TicketMasterDatabaseAPI(this);
            if(City==null || City.equals(""))
                ticketMasterEvents=database.getAllEvents();
            else
              ticketMasterEvents=database.getAllEventsForCity(City);
            if(totalPageNumber==null)
                totalPageNumber="1";

        }

        ((TextView) findViewById(R.id.textViewPageNum)).setText(pageNumber);
        ((TextView) findViewById(R.id.textViewTotalPage)).setText(totalPageNumber);



        // instantiate the custom list adapter
        adapter = new MyListAdapter(this, ticketMasterEvents);

// get the ListView and attach the adapter
        ListView itemsListView  = (ListView) findViewById(R.id.eventListView);

        //scroll to the bottom automatically when adding new rows
        itemsListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        itemsListView.setStackFromBottom(true);

        itemsListView.setAdapter(adapter);

        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                itemsListView.setSelection(adapter.getCount() - 1);
            }
        });

        //list view OnItemClick() listener
        itemsListView.setOnItemClickListener((list, view, position, id) ->
                {

                        // your handler code here
                        Intent eventDetail = new Intent(this, EventDetailActivity.class);
                    eventDetail.putExtra("eventObject", ticketMasterEvents.get(position));

                    startActivity(eventDetail);


                }
        );

    }
    @Override
    public void onResume(){
        super.onResume();
        if(!isTicketMasterQuery)
        {
            TicketMasterDatabaseAPI database = new TicketMasterDatabaseAPI(this);
            List<TicketMasterEvent> newList;
            if(City==null || City.equals(""))
                newList=database.getAllEvents();
            else
                newList=database.getAllEventsForCity(City);

            if(adapter!=null && newList.size()!=ticketMasterEvents.size())
            {
                ticketMasterEvents.clear();
                ticketMasterEvents.addAll(newList);
                adapter.notifyDataSetChanged();
            }

        }

    }
    public void nextPage(View view)
    {
       int currentPageNumber=Integer.valueOf(((TextView) findViewById(R.id.textViewPageNum)).getText().toString());
        int totalPageNumber=Integer.valueOf(((TextView) findViewById(R.id.textViewTotalPage)).getText().toString());
        if(currentPageNumber<totalPageNumber)
        {

            Intent nextPage = new Intent(this, SearchResultActivity.class);
            nextPage.putExtra("pageNumber",String.valueOf(currentPageNumber+1));
            nextPage.putExtra("totalPageNumber",String.valueOf(totalPageNumber));
            nextPage.putExtra("City",City);
            nextPage.putExtra("Radius",Radius);
            finish();
            startActivity(nextPage);

        }




    }
    public void previousPage(View view)
    {
        int currentPageNumber=Integer.valueOf(((TextView) findViewById(R.id.textViewPageNum)).getText().toString());
        int totalPageNumber=Integer.valueOf(((TextView) findViewById(R.id.textViewTotalPage)).getText().toString());

        if(currentPageNumber>1)
        {
            Intent nextPage = new Intent(this, SearchResultActivity.class);
            nextPage.putExtra("pageNumber",String.valueOf(currentPageNumber-1));
            nextPage.putExtra("totalPageNumber",String.valueOf(totalPageNumber));
            nextPage.putExtra("City",City);
            nextPage.putExtra("Radius",Radius);
            finish();
            startActivity(nextPage);
        }

    }

    public class MyListAdapter extends BaseAdapter {
        private Context context; //context
        private List<TicketMasterEvent> items; //data source of the list adapter

        //public constructor
        public MyListAdapter(Context context, List<TicketMasterEvent> items) {
            this.context = context;
            this.items = items;
        }

        @Override
        public int getCount() {
            return items.size(); //returns total of items in the list
        }

        @Override
        public Object getItem(int position) {
            return items.get(position); //returns list item at the specified position
        }

        @Override
        public long getItemId(int position) {
            TicketMasterEvent evt = (TicketMasterEvent) getItem(position);
            return position;
        }

        @Override
        public View getView(int position, View old, ViewGroup parent) {

            // get current item to be displayed
            TicketMasterEvent currentItem = (TicketMasterEvent) getItem(position);
            View newView;

            // inflate the layout for each list row
            //if(currentItem.getMessageType() == 0) //send
            {
                newView = LayoutInflater.from(context).inflate(R.layout.ticket_event_listview_item, parent, false);
            }
            //else //receive
            /*{
                newView = LayoutInflater.from(context).
                        inflate(R.layout.chat_room_row_receive_layout, parent, false);
            }*/

            // get the TextView for item name and item description
            TextView eventTitle = (TextView) newView.findViewById(R.id.textViewEventTitle);
            eventTitle.setText(currentItem.getName());


            return newView;
        }
    }
}