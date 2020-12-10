package com.example.androidphotos61;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.*;

import java.util.ArrayList;

import adapters.CustomSearchResultsAdapter;

public class SearchResults extends AppCompatActivity {
    public CustomSearchResultsAdapter searchAdapter;
    public ArrayList<Integer> photos;
    GridView gridView;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        Toolbar toolbar = findViewById(R.id.toolbar);

        Bundle bundle = getIntent().getExtras();
        photos = (ArrayList<Integer>)bundle.getIntegerArrayList("photos");
        String filter = bundle.getString("filter");


        searchAdapter = new CustomSearchResultsAdapter(this, photos);
        gridView = (GridView) findViewById(R.id.searchResultsGridView);
        gridView.setAdapter(searchAdapter);


        if(filter.equals("location")){
            toolbar.setTitle("Search Results By Location");
        }else{
            toolbar.setTitle("Search Results By Person");
        }
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
