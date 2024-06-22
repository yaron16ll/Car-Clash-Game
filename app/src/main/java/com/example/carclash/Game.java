package com.example.carclash;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.carclash.Logic.GameManager;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.Timer;
import java.util.TimerTask;

public class Game extends AppCompatActivity {


    private AppCompatImageView[] lives;
    private AppCompatImageView[] deads;

    private ExtendedFloatingActionButton leftBtn;
    private ExtendedFloatingActionButton rightBtn;
    private AppCompatImageView[][] rocks;
    private AppCompatImageView[] car;
    private GameManager gameManager;
    private static final long DELAY = 1000L;
    private long startTime;
    private boolean timerOn = false;
    private Timer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        findViews();
        gameManager = new GameManager(lives.length);
        initViews();
        startGame();
    }

    private void findViews() {
        //array of hearts
        lives = new AppCompatImageView[]{
                findViewById(R.id.live1),
                findViewById(R.id.live2),
                findViewById(R.id.live3)
        };
        //array of deads
        deads = new AppCompatImageView[]{
                findViewById(R.id.dead1),
                findViewById(R.id.dead2),
                findViewById(R.id.dead3)
        };


        //arrows buttons
        leftBtn = findViewById(R.id.left_btn);
        rightBtn = findViewById(R.id.right_btn);

        //matrix of barriers
        rocks = new AppCompatImageView[][]{
                {
                        findViewById(R.id.rock1),
                        findViewById(R.id.rock2),
                        findViewById(R.id.rock3)
                },

                {
                        findViewById(R.id.rock13),
                        findViewById(R.id.rock14),
                        findViewById(R.id.rock15)
                },
                {
                        findViewById(R.id.rock19),
                        findViewById(R.id.rock20),
                        findViewById(R.id.rock21)
                }
        };

        //array of cols cars
        car = new AppCompatImageView[]{
                findViewById(R.id.car1),
                findViewById(R.id.car2),
                findViewById(R.id.car3)
        };

    }

    private void initViews() {
        leftBtn.setOnClickListener(v -> moveClicked("left"));
        rightBtn.setOnClickListener(v -> moveClicked("right"));
    }

    private void moveClicked(String direction) {
        gameManager.moveCar(direction);
        refreshUI();
    }

    private void refreshUI() {
        if (gameManager.isGameOver()) {
            stopTimer();

        } else {
            showCar();
            dropRocks();
            updateLives();
        }
    }

    ;

    private void updateLives() {
        boolean isCrushed = gameManager.checkCrush();

        if (isCrushed) {
            lives[gameManager.getAmountOfCrushes() - 1].setVisibility(View.INVISIBLE);
            deads[gameManager.getAmountOfCrushes() - 1].setVisibility(View.VISIBLE);

            vibrate();
            Toast.makeText(this, "CRUSH", Toast.LENGTH_SHORT).show();
        }
    }

    private void dropRocks() {
        for (int i = 0; i < gameManager.getRocksRows(); i++) {
            for (int j = 0; j < gameManager.getRocksCols(); j++) {

                String currentCol = gameManager.getRocks()[i][j];
                if (currentCol.equals(gameManager.getNONE())) {
                    rocks[i][j].setVisibility(View.INVISIBLE);

                } else {
                    rocks[i][j].setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void showCar() {
        int currentCarPos = gameManager.getCarPosition();
        Log.d("pos", "" + currentCarPos);
        for (int i = 0; i < gameManager.getRocksCols(); i++) {
            if (i == currentCarPos) {
                car[i].setVisibility(View.VISIBLE);
            } else {
                car[i].setVisibility(View.INVISIBLE);
            }
        }
    }

    private void setRocksTime() {
        gameManager.setRocksPeriod();
        refreshUI();
    }

    private void startGame() {
        if (!timerOn) {
             startTime = System.currentTimeMillis();
            timerOn = true;
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(() -> setRocksTime());
                }
            }, 0L, DELAY);
        }
    }

    private void stopTimer() {
        timerOn = false;
        timer.cancel();
    }


    private void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
    }

}