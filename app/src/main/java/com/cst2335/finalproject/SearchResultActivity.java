package com.cst2335.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;


public class SearchResultActivity extends AppCompatActivity {

    public List<TicketMasterEvent> ticketMasterEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        Intent fromLastPage = getIntent();
        String City = fromLastPage.getStringExtra("City");
        String Radius = fromLastPage.getStringExtra("Radius");

        ticketMasterEvents = TicketMasterAPI.getEventsFromTicketmaster(City, Radius,"1");

        // instantiate the custom list adapter
        MyListAdapter adapter = new MyListAdapter(this, ticketMasterEvents);

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
/*                    if(processOnItemLongClickListener && isOnPhone)
                        return;

                    Message msg = messages.get(position);

                    Bundle bd = new Bundle();
                    bd.putLong("dbID", msg.dbID);
                    bd.putString("msgText", msg.msgText);
                    bd.putInt("msgType", msg.msgType);
                    bd.putString("msgDate", msg.msgDate);

                    if(!isOnPhone)
                    {
                        aFragment = new DetailsFragment();
                        aFragment.setArguments(bd);
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.myFrameLayout, aFragment);
                        ft.commit();
                    }
                    else
                    {
                        // your handler code here
                        Intent emptyPage = new Intent(this, EmptyActivity.class);

                        emptyPage.putExtra("Message", bd);

                        startActivity(emptyPage);
                    }*/

                }
        );

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

            TextView eventDateTime = (TextView) newView.findViewById(R.id.textViewEventDateTime);
            eventDateTime.setText(currentItem.getDate() + " " + currentItem.getTime());

            TextView eventPrice = (TextView) newView.findViewById(R.id.textViewEventPrice);
            eventDateTime.setText("$" + currentItem.getMinPrice() + " - $" + currentItem.getMaxPrice());

            // returns the view for the current row
            return newView;
        }
    }
}