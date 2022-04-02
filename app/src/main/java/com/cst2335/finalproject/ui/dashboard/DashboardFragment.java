package com.cst2335.finalproject.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.cst2335.finalproject.R;
import com.cst2335.finalproject.SearchResultActivity;
import com.cst2335.finalproject.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        //TextView tv = (TextView) findViewById(R.id.text_search);
        Button searchButton = (Button) root.findViewById(R.id.button_searchDB);
        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // your handler code here
                Intent searchResultActivity = new Intent(getActivity(), SearchResultActivity.class);

                EditText cityName = (EditText)  root.findViewById(R.id.editTextCityDB);
                searchResultActivity.putExtra("City", cityName.getText().toString());

                //searchResultActivity.putExtra("TicketmasterQuery", "false");
                SearchResultActivity.isTicketMasterQuery=false;
                startActivity(searchResultActivity);

            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}