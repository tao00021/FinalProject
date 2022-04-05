package com.cst2335.finalproject.ui.home;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.cst2335.finalproject.R;
import com.cst2335.finalproject.TicketMasterMainActivity;
import com.cst2335.finalproject.databinding.FragmentHomeBinding;

import java.util.List;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    public List<com.cst2335.finalproject.TicketMasterEvent> ticketMasterEvents = new ArrayList<com.cst2335.finalproject.TicketMasterEvent>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        /*final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

        String City = TicketMasterMainActivity.homeCity;
        String Radius = "50";

        //ticketMasterEvents = com.cst2335.finalproject.TicketMasterAPI.getEventsFromTicketmaster(City, Radius,"1");

        // instantiate the custom list adapter
        MyListAdapter adapter = new MyListAdapter(root.getContext(), ticketMasterEvents);

// get the ListView and attach the adapter
        ListView itemsListView  = (ListView) root.findViewById(R.id.home_list_view);

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


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    public class MyListAdapter extends BaseAdapter {
        private Context context; //context
        private List<com.cst2335.finalproject.TicketMasterEvent> items; //data source of the list adapter

        //public constructor
        public MyListAdapter(Context context, List<com.cst2335.finalproject.TicketMasterEvent> items) {
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
            com.cst2335.finalproject.TicketMasterEvent evt = (com.cst2335.finalproject.TicketMasterEvent) getItem(position);
            return position;
        }

        @Override
        public View getView(int position, View old, ViewGroup parent) {

            // get current item to be displayed
            com.cst2335.finalproject.TicketMasterEvent currentItem = (com.cst2335.finalproject.TicketMasterEvent) getItem(position);
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