package com.example.electricbillmaker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class BillDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_detail);
        setTitle("Bill Details");

        TextView tv = findViewById(R.id.tvDetails);
        ImageView back = findViewById(R.id.btnBack);
        back.setOnClickListener(v -> finish());

        Intent i = getIntent();
        String details = "Month: " + i.getStringExtra("month") +
                "\nUnits Used: " + i.getIntExtra("units", 0) + " kWh" +
                "\nTotal Charges: RM " + String.format("%.2f", i.getDoubleExtra("total", 0)) +
                "\nRebate: " + String.format("%.2f", i.getDoubleExtra("rebate", 0)) + "%" +
                "\nFinal Cost: RM " + String.format("%.2f", i.getDoubleExtra("final", 0));

        tv.setText(details);
    }
}
