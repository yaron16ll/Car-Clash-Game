package com.example.carclash.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carclash.R;
import com.example.carclash.models.SharedPreferencesManager;
import com.google.android.material.button.MaterialButton;

public class Menu extends AppCompatActivity {
    private MaterialButton fastModeBtn, slowModeBtn, sensorBtn, recodBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity);

        SharedPreferencesManager.init(this); // Initialize SharedPreferencesManager
        findViews();
        initViews();
    }

    private void findViews() {
        fastModeBtn = findViewById(R.id.button1);
        slowModeBtn = findViewById(R.id.button2);
        sensorBtn = findViewById(R.id.button3);
        recodBtn = findViewById(R.id.button4);
    }

    private void initViews() {
        fastModeBtn.setOnClickListener(v -> startGameActivity(670L));
        slowModeBtn.setOnClickListener(v -> startGameActivity(1000L));
        sensorBtn.setOnClickListener(v -> btn3Clicked());
        recodBtn.setOnClickListener(v -> startRecordActivity());
    }

    private void startGameActivity(long delay) {
        Intent intent = new Intent(this, Game.class);
        intent.putExtra("key", delay);
        startActivity(intent);
    }

    private void startRecordActivity() {
        Intent intent = new Intent(this, Record.class);
        startActivity(intent);
    }

    private void btn3Clicked() {
        Intent intent = new Intent(this, Game.class);
        intent.putExtra("key", 1000L);
        intent.putExtra("isSensor", true);

        startActivity(intent);
    }
}
