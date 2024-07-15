package com.example.carclash.activities;

import static android.Manifest.permission_group.SENSORS;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.carclash.R;
import com.example.carclash.interfaces.MoveCallback;
import com.example.carclash.logic.GameManager;
import com.example.carclash.models.Record;
import com.example.carclash.models.SharedPreferencesManager;
import com.example.carclash.utilities.LocationService;
import com.example.carclash.utilities.MoveDetector;
import com.example.carclash.utilities.SoundPlayer;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Game extends AppCompatActivity {

    private MoveDetector moveDetector;

    private static final long DEFAULT_DELAY = 1000L;
    private static final int VIBRATION_DURATION = 500;
    private LocationService locationService;

    private static final boolean IS_SENSOR = false;

    private double latitude;
    private double longitude;

    private boolean isSensor;
    private int carDistance = 0;
    private AppCompatImageView[] lives;
    private AppCompatImageView[] deads;
    private MaterialTextView distance;
    private ExtendedFloatingActionButton leftBtn;
    private ExtendedFloatingActionButton rightBtn;
    private AppCompatImageView[][] rocks;
    private AppCompatImageView[][] healths;
    private AppCompatImageView[] car;
    private GameManager gameManager;
    private long delay;
    private long startTime;
    private boolean timerOn = false;
    private Timer timer;
    private SoundPlayer soundPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity);
        isSensor = getFromMenu2();

        findViews();
        initViews(isSensor);
        start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isSensor){
            moveDetector.stop();
        }
        stopTimer();
        if (soundPlayer != null) {
            soundPlayer.stop();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isSensor){
            moveDetector.start();
        }
        startTimer();
    }

    private void findViews() {
        lives = new AppCompatImageView[]{
                findViewById(R.id.live1),
                findViewById(R.id.live2),
                findViewById(R.id.live3)
        };
        deads = new AppCompatImageView[]{
                findViewById(R.id.dead1),
                findViewById(R.id.dead2),
                findViewById(R.id.dead3)
        };
        leftBtn = findViewById(R.id.left_btn);
        rightBtn = findViewById(R.id.right_btn);

        rocks = new AppCompatImageView[][]{
                {findViewById(R.id.rock1), findViewById(R.id.rock2), findViewById(R.id.rock3), findViewById(R.id.rock4), findViewById(R.id.rock5)},
                {findViewById(R.id.rock6), findViewById(R.id.rock7), findViewById(R.id.rock8), findViewById(R.id.rock9), findViewById(R.id.rock10)},
                {findViewById(R.id.rock11), findViewById(R.id.rock12), findViewById(R.id.rock13), findViewById(R.id.rock14), findViewById(R.id.rock15)},
                {findViewById(R.id.rock16), findViewById(R.id.rock17), findViewById(R.id.rock18), findViewById(R.id.rock19), findViewById(R.id.rock20)}
        };

        healths = new AppCompatImageView[][]{
                {findViewById(R.id.health1), findViewById(R.id.health2), findViewById(R.id.health3), findViewById(R.id.health4), findViewById(R.id.health5)},
                {findViewById(R.id.health6), findViewById(R.id.health7), findViewById(R.id.health8), findViewById(R.id.health9), findViewById(R.id.health10)},
                {findViewById(R.id.health11), findViewById(R.id.health12), findViewById(R.id.health13), findViewById(R.id.health14), findViewById(R.id.health15)},
                {findViewById(R.id.health16), findViewById(R.id.health17), findViewById(R.id.health18), findViewById(R.id.health19), findViewById(R.id.health20)}
        };

        car = new AppCompatImageView[]{
                findViewById(R.id.car1),
                findViewById(R.id.car2),
                findViewById(R.id.car3),
                findViewById(R.id.car4),
                findViewById(R.id.car5)
        };
        distance = findViewById(R.id.distanceText);
    }

    private void initViews(boolean isSensor) {
        if (isSensor) {
            leftBtn.setVisibility(View.INVISIBLE);
            rightBtn.setVisibility(View.INVISIBLE);
            initMoveDetector();

        } else {
            leftBtn.setOnClickListener(v -> moveClicked("left"));
            rightBtn.setOnClickListener(v -> moveClicked("right"));
        }
    }

    private void moveClicked(String direction) {
        gameManager.moveCar(direction);
        refreshUI();
    }

    private void refreshUI() {
        if (gameManager.isGameOver()) {
            if (isSensor){
                moveDetector.stop();
            }
            stopTimer();
            showDialog();
        } else {
            showCar();
            dropRocks();
            dropHealths();
            updateLives();
        }
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        EditText editText = dialogView.findViewById(R.id.dialog_text);
        Button button = dialogView.findViewById(R.id.dialog_button);

        isNameWritten(button, editText);

        button.setOnClickListener(v -> {
            String name = editText.getText().toString();
            saveResult(name);
            startActivity(new Intent(this, Menu.class));
        });
    }


    private void initMoveDetector() {
        if (!isSensor){
            return;
        }
        else{
            moveDetector = new MoveDetector(this,
                    new MoveCallback() {
                        @Override
                        public void moveLeft() {
                            // what to do if x move left
                            moveClicked("left");
                        }

                        @Override
                        public void moveRight() {
                            // what to do if x move right
                            moveClicked("right");
                        }
                    }
            );
        }
    }

    public void isNameWritten(Button button, EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                button.setEnabled(s.toString().trim().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing
            }
        });
    }

    private void saveResult(String name) {
        Gson gson = new Gson();
        ArrayList<Record> records;

        Record record = new Record().setName(name).setScore(this.carDistance).setLatitude(locationService.getLatitude()).setLongitude(locationService.getLongitude());
        String stringifiedRecords = SharedPreferencesManager.getInstance().getString("RecordList", null);

        if (stringifiedRecords == null) {
            records = new ArrayList<>();
        } else {
            Type recordListType = new TypeToken<ArrayList<Record>>() {
            }.getType();
            records = gson.fromJson(stringifiedRecords, recordListType);
        }

        records.add(record);
        SharedPreferencesManager.getInstance().putString("RecordList", gson.toJson(records));

    }

    private void updateLives() {
        boolean isCrushed = gameManager.checkCrush();
        boolean isHealed = gameManager.checkHealth();

        if (isHealed && isCrushed) {
            return;
        }

        if (isCrushed) {
            int crushes = gameManager.getAmountOfCrushes() - 1;
            if (crushes >= 0 && crushes < lives.length) {
                lives[crushes].setVisibility(View.INVISIBLE);
                deads[crushes].setVisibility(View.VISIBLE);
            }
            soundPlayer.play(R.raw.carcrash, false, 1.0f, 1.0f);
            vibrate();
            Toast.makeText(this, "CRUSH", Toast.LENGTH_SHORT).show();
        }

        if (isHealed) {
            int crushes = gameManager.getAmountOfCrushes();
            if (crushes < lives.length) {
                lives[crushes].setVisibility(View.VISIBLE);
                deads[crushes].setVisibility(View.INVISIBLE);
            }
            soundPlayer.play(R.raw.heart, false, 1.0f, 1.0f);
        }
    }

    private void dropRocks() {
        for (int i = 0; i < gameManager.getRocksRows(); i++) {
            for (int j = 0; j < gameManager.getRocksCols(); j++) {
                String currentCol = gameManager.getRocks()[i][j];
                rocks[i][j].setVisibility(currentCol.equals(gameManager.getNONE()) ? View.INVISIBLE : View.VISIBLE);
            }
        }
    }

    private void dropHealths() {
        for (int i = 0; i < gameManager.getRocksRows(); i++) {
            for (int j = 0; j < gameManager.getRocksCols(); j++) {
                String currentCol = gameManager.getHealths()[i][j];
                healths[i][j].setVisibility(currentCol.equals(gameManager.getNONE()) ? View.INVISIBLE : View.VISIBLE);
            }
        }
    }

    private void showCar() {
        int currentCarPos = gameManager.getCarPosition();
        for (int i = 0; i < gameManager.getRocksCols(); i++) {
            car[i].setVisibility(i == currentCarPos ? View.VISIBLE : View.INVISIBLE);
        }
    }

    private void setRocksTime() {
        carDistance++;
        distance.setText(String.format("%d", carDistance));

        gameManager.setRocksPeriod();
        gameManager.setHealthsPeriod();
        refreshUI();
    }

    private void start() {
        locationService = new LocationService(this);
        gameManager = new GameManager(lives.length);
        soundPlayer = new SoundPlayer(this);
        delay = getFromMenu();
     }

    private void startTimer() {
        if (!timerOn) {
            startTime = System.currentTimeMillis();
            timerOn = true;
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(() -> setRocksTime());
                }
            }, 0L, delay);
        }
    }

    private long getFromMenu() {
        Bundle extras = getIntent().getExtras();
        return extras != null ? extras.getLong("key", DEFAULT_DELAY) : DEFAULT_DELAY;


    }

    private boolean getFromMenu2() {
        Bundle extras = getIntent().getExtras();
        return extras != null ? extras.getBoolean("isSensor", IS_SENSOR) : IS_SENSOR;


    }


    private void stopTimer() {
        if (timer != null) {
            timerOn = false;
            timer.cancel();
        }
    }

    private void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (v != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(VIBRATION_DURATION, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                v.vibrate(VIBRATION_DURATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationService.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
