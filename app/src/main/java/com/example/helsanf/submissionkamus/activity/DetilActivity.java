package com.example.helsanf.submissionkamus.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.helsanf.submissionkamus.R;

public class DetilActivity extends AppCompatActivity {
    public static String KATA = "kata";
    public static String ARTI = "arti";

    TextView txtKata , txtDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detil);
        txtKata = findViewById(R.id.txtkatadetail);
        txtDetail = findViewById(R.id.txtdescdetail);

        txtKata.setText(getIntent().getStringExtra(KATA));
        txtDetail.setText(getIntent().getStringExtra(ARTI));

    }
}
