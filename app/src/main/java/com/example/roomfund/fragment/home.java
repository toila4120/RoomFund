package com.example.roomfund.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.roomfund.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Optional;

public class home extends Fragment {
    private TextView tvMoney;
    private EditText edtNote,edtMoney;
    private Button update;
    private View mView;
    private FirebaseDatabase database;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.fragment_home, container, false);
        initUi();
        getMoney();
        initlistener();
        return mView;
    }

    private void getMoney() {
        database= FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Money");
        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                }
                else {
                    int money1;
                    money1=Integer.parseInt(String.valueOf(task.getResult().getValue()));
                    String money3=String.valueOf(money1);
                    tvMoney.setText(money3);
                    database= FirebaseDatabase.getInstance();
                    database.getReference().child("Money").setValue(money1);
                }
            }
        });
    }
    //
    private void initlistener() {
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtMoney.getText().toString().equals("")||edtNote.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "Yêu cầu nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    setMoney();
                    getData();
                    Toast.makeText(getActivity(), "Update success", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getData() {
        database= FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Money");
        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                }
                else {
                    int money1,money2,money;
                    money1=Integer.parseInt(String.valueOf(task.getResult().getValue()));
                    money2=Integer.parseInt(edtMoney.getText().toString().trim());
                    money=money1+money2;
                    String money3=String.valueOf(money);
                    tvMoney.setText(money3);
                    database= FirebaseDatabase.getInstance();
                    database.getReference().child("Money").setValue(money);
                }
            }
        });
    }



    private void setMoney() {
        Calendar calendar=Calendar.getInstance();
        String ngay=(calendar.get(Calendar.DATE)+"/"
                + (calendar.get(Calendar.MONTH)+1)+"/"+ calendar.get(Calendar.YEAR)).toString();
        dem(ngay);
    }

    private void dem(String ngay){
        database= FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("history").child("so");
        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                } else {
                    String i1=(String.valueOf(task.getResult().getValue()));
                    if (i1.equals("null")){
                        i1="0";
                    }
                    int i=Integer.parseInt(i1);
                    i++;
                    Calendar calendar=Calendar.getInstance();
                    FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                    database.getReference().child("history").child("so").setValue(i);
                    database.getReference().child("history").child("lan"+i).child("nd")
                            .setValue(ngay+" "+calendar.get(Calendar.HOUR)+":"+calendar.get(Calendar.MINUTE)+":"+calendar.get(Calendar.SECOND)
                                    +" "+user.getDisplayName()+" đã mua "
                                    +edtNote.getText().toString().trim()
                                    +" với giá: "+edtMoney.getText().toString().trim()+" VNĐ");

                }
            }
        });
    }

    private void initUi() {
        edtMoney=mView.findViewById(R.id.edt_money);
        tvMoney=mView.findViewById(R.id.tv_money);
        edtNote=mView.findViewById(R.id.edt_note);
        update=mView.findViewById(R.id.btn_update_money);
    }
}
