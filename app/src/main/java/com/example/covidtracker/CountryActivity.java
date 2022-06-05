package com.example.covidtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covidtracker.api.ApiUtilities;
import com.example.covidtracker.api.CountryData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CountryActivity extends AppCompatActivity {

    private RecyclerView RVcountries;
    private List<CountryData> list;
    CountryAdapter adapter;
    EditText ETsearch;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);


        ETsearch = findViewById(R.id.searchET);
        RVcountries = findViewById(R.id.countriesRV);
        list = new ArrayList<>();
        adapter = new CountryAdapter(this,list);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.show();

        RVcountries.setLayoutManager(new LinearLayoutManager(this));
//        RVcountries.setHasFixedSize(true);
        RVcountries.setAdapter(adapter);

        //api working
        ApiUtilities.getApiInterface().getCountryData().enqueue(new Callback<ArrayList<CountryData>>() {
            @Override
            public void onResponse(Call<ArrayList<CountryData>> call, Response<ArrayList<CountryData>> response) {
                list.addAll(response.body());
                adapter.notifyDataSetChanged();

                dialog.dismiss();


            }

            @Override
            public void onFailure(Call<ArrayList<CountryData>> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(CountryActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        //working of searchEt to change recycleview items when searching
        ETsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //when country name is typed call this method
                filter(editable.toString());
            }
        });


    }

    // this method is simply creating new Arraylist of type CountryData that contains that searched
    //text  and then passing that arraylist to method of adapter class that will update the dataset/list
    private void filter(String text) {
        List <CountryData> filterlist = new ArrayList<>();
        for (CountryData items :list){
            if(items.getCountry().toLowerCase().contains(text.toLowerCase())){
                filterlist.add(items);
            }
        }

        adapter.filterDataSet(filterlist);

    }
}