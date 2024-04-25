package com.example.roomfund.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roomfund.DateAdapter;
import com.example.roomfund.R;
import com.example.roomfund.listDateHistory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class history extends Fragment {
    private View mView;
    RecyclerView recyclerView;
    DatabaseReference database;
    DateAdapter myAdapter;
    ArrayList<listDateHistory> list;
    private Button btnUpHistory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.fragment_history, container, false);

        recyclerView = mView.findViewById(R.id.history_list);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        list = new ArrayList<>();
        myAdapter = new DateAdapter(getContext(),list);
        recyclerView.setAdapter(myAdapter);
        getDate();
        initListener();
        return mView;
    }

    private void initListener() {
        btnUpHistory=mView.findViewById(R.id.btn_update_history);
        btnUpHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAdapter.clearApplications();
                getDate();
            }
        });

    }

    private void getDate() {
        database= FirebaseDatabase.getInstance().getReference();
        DatabaseReference myRef=database.child("history").child("so");
        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                }
                else {
                    int n=Integer.parseInt(String.valueOf(task.getResult().getValue()));
                    for (int i=n; i>=1; i--) {

                        database= FirebaseDatabase.getInstance().getReference();
                        DatabaseReference myRef=database.child("history").child("lan"+i).child("nd");

                        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (!task.isSuccessful()) {
                                }
                                else {
                                    String s1=String.valueOf(task.getResult().getValue());
                                    listDateHistory s=new listDateHistory(s1);
                                    list.add(s);
                                    myAdapter.notifyDataSetChanged();
                                }
                            }
                        });
                    }
                }
            }
        });
    }



}
