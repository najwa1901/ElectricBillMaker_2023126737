package com.example.electricbillmaker;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setTitle("About");

        TextView tv = findViewById(R.id.tvAbout);
        ImageView back = findViewById(R.id.btnBack);
        back.setOnClickListener(v -> finish());

        tv.setText(
                "STUDENT INFORMATION\n\n" +
                        "NAME: NAJWA BINTI ABDUL LATIB\n" +
                        "STUDENT ID : 2023126737\n" +
                        "CCOURSE: CDCS240 - INFORMATION TECHNOLOGY\n\n" +
                        "© 2025 Najwa binti Abdul Latib. All rights reserved.\n\n" +
                        "Application Website:\n" +
                        "https://github.com/yourusername/YourAppRepo"
        );
        tv.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
