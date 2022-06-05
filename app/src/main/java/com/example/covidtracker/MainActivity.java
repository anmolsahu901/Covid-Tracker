package com.example.covidtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covidtracker.api.ApiUtilities;
import com.example.covidtracker.api.CountryData;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView totalconfirm,todayconfirm,date,totalrecovered,todayrecovered,totalactive,totaldeath,todaydeath,totaltests,countryname;
    PieChart pieChart;
    private  ArrayList <CountryData> list;
    String countrytext = "India";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        countryname = findViewById(R.id.COUNTRYNAMETV);
        totalconfirm = findViewById(R.id.totalconfirm);
        todayconfirm = findViewById(R.id.todayconfirm);
        totalrecovered = findViewById(R.id.totalrecovered);
        todayrecovered = findViewById(R.id.todayrecovered);
        totaldeath = findViewById(R.id.totaldeath);
        todaydeath = findViewById(R.id.todaydeath);
        totalactive = findViewById(R.id.totalactive);
        totaltests = findViewById(R.id.totaltests);
        date = findViewById(R.id.date);
        pieChart = findViewById(R.id.piechart);
        list = new ArrayList<>();

        Intent intent = getIntent();
        if(intent.getStringExtra("country")!= null){
            countrytext = intent.getStringExtra("country");
        }

        //updating countryname and also in onResponse() using countryname to update data of that particular country
        countryname.setText(countrytext);

        //COUNTRY NAME ON CLICK COUNTRYACTIVITY
        countryname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,CountryActivity.class));
            }
        });

/// API WORKINGS...........
        ApiUtilities.getApiInterface().getCountryData()
                .enqueue(new Callback<ArrayList<CountryData>>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(Call<ArrayList<CountryData>> call, Response<ArrayList<CountryData>> response) {
                        list.addAll(response.body());

                        for (int i=0;i<list.size();i++)
                        {
                            if(list.get(i).getCountry().equals(countrytext)){
                                int confirm = Integer.parseInt(list.get(i).getCases());
                                int todayConfirm = Integer.parseInt(list.get(i).getTodayCases());
                                int active = Integer.parseInt(list.get(i).getActive());
                                int recovered = Integer.parseInt(list.get(i).getRecovered());

                                int todayRecovered = Integer.parseInt(list.get(i).getTodayRecovered());
                                int death = Integer.parseInt(list.get(i).getDeaths());
                                int todayDeath = Integer.parseInt(list.get(i).getTodayDeaths());
                                int totalTests = Integer.parseInt(list.get(i).getTests());

                                setDateText(list.get(i).getUpdated());


                                totalconfirm.setText(NumberFormat.getInstance().format(confirm));
                                totalactive.setText(NumberFormat.getInstance().format(active));
                                totalrecovered.setText(NumberFormat.getInstance().format(recovered));
                                totaldeath.setText(NumberFormat.getInstance().format(death));
                                todayconfirm.setText("+ ("+NumberFormat.getInstance().format(todayConfirm)+")");
                                todayrecovered.setText("+ ("+NumberFormat.getInstance().format(todayRecovered)+")");
                                todaydeath.setText("+ ("+NumberFormat.getInstance().format(todayDeath)+")");
                                totaltests.setText(NumberFormat.getInstance().format(totalTests));

//   mPieChart.addPieSlice(new PieModel("Freetime", 15, Color.parseColor("#FE6DA8")));
                                pieChart.addPieSlice(new PieModel("Confirm",confirm,getResources().getColor(R.color.yellow)));
                                pieChart.addPieSlice(new PieModel("Active",active,getResources().getColor(R.color.blue_pie)));
                                pieChart.addPieSlice(new PieModel("Recovered",recovered,getResources().getColor(R.color.green_pie)));
                                pieChart.addPieSlice(new PieModel("Death",death,getResources().getColor(R.color.red_pie)));

                                pieChart.startAnimation();

                            }
                        }
                    }



                    @Override
                    public void onFailure(Call<ArrayList<CountryData>> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Error : "+t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });



    }
    //imp: set date  from milliseconds in in formatted way
    private void setDateText(String updated) {
        DateFormat format = new SimpleDateFormat("MMM dd, yyyy");

        Long milliseconds = Long.parseLong(updated);

        Calendar calender = Calendar.getInstance();
        calender.setTimeInMillis(milliseconds);

        date.setText("Updated at "+format.format(calender.getTime()));
    }
}