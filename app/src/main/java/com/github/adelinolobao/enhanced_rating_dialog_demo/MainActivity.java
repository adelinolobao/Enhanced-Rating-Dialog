package com.github.adelinolobao.enhanced_rating_dialog_demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.adelinolobao.enhanced_rating_dialog.RatingDialog;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RatingDialog.show(this);
    }
}
