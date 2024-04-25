package com.example.roomfund;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.MyViewHolder> {

    Context context;
    private List<listDateHistory> mListDateHistories;

    public DateAdapter(Context context, ArrayList<listDateHistory> listDateHistories) {
        this.context = context;
        this.mListDateHistories = listDateHistories;
    }

    public DateAdapter() {
    }

    public void clearApplications() {
        int size = this.mListDateHistories.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                mListDateHistories.remove(0);
            }

            this.notifyItemRangeRemoved(0, size);
        }
    }

    public void setData(List<listDateHistory> list) {
        this.mListDateHistories = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        listDateHistory listDateHistory=mListDateHistories.get(position);

        holder.date.setText(listDateHistory.getNd());


    }

    @Override
    public int getItemCount() {
        if (mListDateHistories!=null){
            return mListDateHistories.size();
        }
        return 0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView date;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            date=itemView.findViewById(R.id.tv_time);
        }
    }
}
