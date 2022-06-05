package com.example.covidtracker;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.covidtracker.api.CountryData;

import java.text.NumberFormat;
import java.util.List;
import java.util.Map;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.ViewHolder> {

    private Context context;
    private List<CountryData> list;

    public CountryAdapter(Context context, List<CountryData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.country_rv_item, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CountryData data = list.get(position);

        holder.countrycases.setText(NumberFormat.getInstance().format(Integer.parseInt(data.getCases())));
        holder.countryname.setText(data.getCountry());
        holder.sno.setText(String.valueOf(position + 1));

        Map<String, String> img = data.getCountryInfo();
        Glide.with(context).load(img.get("flag")).into(holder.countryflag);

        //onclicking the item of recycleview open the MainActivity also passinf countryname
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,MainActivity.class);
                intent.putExtra("country",data.getCountry());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView sno, countryname, countrycases;
        private ImageView countryflag;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sno = itemView.findViewById(R.id.sno);
            countryname = itemView.findViewById(R.id.countryName);
            countrycases = itemView.findViewById(R.id.cases);
            countryflag = itemView.findViewById(R.id.countryimage);
        }
    }

    // this function is used while searching the country
    // this method will change the list with modified list which contains that searched country name
    // and notify that data in list is changed
    public void filterDataSet(List<CountryData> filterlist) {
        list = filterlist;
        notifyDataSetChanged();
    }

}
