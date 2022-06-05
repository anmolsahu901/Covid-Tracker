package com.example.covidtracker.api;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    static final String BASE_URL = "https://corona.lmao.ninja/v2/";

    @GET("countries")
    Call<ArrayList<CountryData>> getCountryData();

}
