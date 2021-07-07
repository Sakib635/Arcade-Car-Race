package com.SakibAnik.arcadecarrace;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class developers extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developers);


    }
    public  void back(View view){
        Intent intent = new Intent(getApplicationContext(), StartAction.class);
        startActivity(intent);
    }
}