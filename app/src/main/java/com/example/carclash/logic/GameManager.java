package com.example.carclash.logic;

import android.media.MediaPlayer;

public class GameManager {
    private static final String NONE = "none";
    private static final String ROCK = "rock";
    private static final String HEALTH = "health";
    private static final int MAX_LIVES = 3;
    private static final int ROCKS_ROWS = 4;
    private static final int ROCKS_COLS = 5;

    private int num = 0;
    private String[][] rocks = new String[ROCKS_ROWS][ROCKS_COLS];
    private String[][] healths = new String[ROCKS_ROWS][ROCKS_COLS];

    private int lives;
    private int amountOfCrashes = 0;
    private int carPosition = 2; // 0 = left, 1 = midLeft, 2 = mid, 3 = midRight, 4 = right
    private MediaPlayer sound;

    public GameManager() {
        this(MAX_LIVES);
    }

    public GameManager(int lives) {
        this.lives = lives;
        initializeArrays();
    }

    private void initializeArrays() {
        for (int i = 0; i < ROCKS_ROWS; i++) {
            for (int j = 0; j < ROCKS_COLS; j++) {
                rocks[i][j] = NONE;
                healths[i][j] = NONE;
            }
        }
    }

    public boolean isGameOver() {
        return this.lives == 0;
    }

    public void moveCar(String direction) {
        if ("left".equals(direction) && carPosition > 0) {
            carPosition--;
        } else if ("right".equals(direction) && carPosition < ROCKS_COLS - 1) {
            carPosition++;
        }
    }

    public void setRocksPeriod() {
        moveArrayItems(rocks);
        addItemToTopRow(rocks, ROCK);
    }

    public void setHealthsPeriod() {
        moveArrayItems(healths);
        addItemToTopRow(healths, HEALTH);
    }

    private void moveArrayItems(String[][] array) {
        for (int i = ROCKS_ROWS - 1; i > 0; i--) {
            System.arraycopy(array[i - 1], 0, array[i], 0, ROCKS_COLS);
        }
        for (int j = 0; j < ROCKS_COLS; j++) {
            array[0][j] = NONE;
        }
    }

    private void addItemToTopRow(String[][] array, String item) {
        num = (int) (Math.random() * ROCKS_COLS);
        array[0][num] = item;
    }

    public boolean checkCrush() {
        if (ROCK.equals(rocks[ROCKS_ROWS - 1][carPosition])) {
            lives--;
            amountOfCrashes++;
            return true;
        }
        return false;
    }

    public boolean checkHealth() {
        if (HEALTH.equals(healths[ROCKS_ROWS - 1][carPosition]) && lives < MAX_LIVES) {
            lives++;
            amountOfCrashes--;
            return true;
        }
        return false;
    }

    // Getters and Setters
    public int getAmountOfCrushes() {
        return amountOfCrashes;
    }

    public void setAmountOfCrushes(int amountOfCrashes) {
        this.amountOfCrashes = amountOfCrashes;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getCarPosition() {
        return carPosition;
    }

    public void setCarPosition(int carPosition) {
        this.carPosition = carPosition;
    }

    public String[][] getRocks() {
        return rocks;
    }

    public String[][] getHealths() {
        return healths;
    }

    public int getRocksRows() {
        return ROCKS_ROWS;
    }

    public int getRocksCols() {
        return ROCKS_COLS;
    }

    public String getNONE() {
        return NONE;
    }

    public String getRock() {
        return ROCK;
    }
}
