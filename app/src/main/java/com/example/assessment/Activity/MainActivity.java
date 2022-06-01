package com.example.assessment.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.assessment.Adapter.LandingListAdapter;
import com.example.assessment.Model.LandingListData;
import com.example.assessment.R;

public class MainActivity extends AppCompatActivity {

    LandingListAdapter adapter;
    RecyclerView recyclerView;
    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        LandingListData[] landingListData = new LandingListData[] {
                new LandingListData("Search Articles", android.R.drawable.ic_menu_search),
                new LandingListData("Popular", android.R.drawable.ic_menu_agenda),
//                new LandingListData("Most Shared", android.R.drawable.ic_menu_share),
//                new LandingListData("Most Emailed", android.R.drawable.ic_dialog_email),
        };

        recyclerView = findViewById(R.id.recyclerView);
        adapter = new LandingListAdapter(landingListData, (view, position) -> {

            if(position == 0){
                Intent intent = new Intent(this, SearchActivity.class);
                String message = "Search Articles";
                intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(intent);

            }else if(position == 1){

                Intent intent = new Intent(this, PopularActivity.class);
                String message = "Popular";
                intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(intent);

            }

        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


    }

}