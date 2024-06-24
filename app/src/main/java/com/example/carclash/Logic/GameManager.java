package com.example.carclash.Logic;


import android.view.View;
import android.widget.Toast;

public class GameManager {
    private final String NONE = "none";
    private final String ROCK = "rock ";
    private int num = 0;
    private String[][] rocks = new String[][]{
            {NONE, NONE, NONE},
            {NONE, NONE, NONE},
            {NONE, NONE, NONE}
    };
    private int lives;
    private int amountOfCrushes = 0;

    private int carPosition = 1; // 0 = left, 1 = mid, 2 = right
    private final int rocksRows = 3;
    private final int rocksCols = 3;


    //Constructors
    public GameManager() {
        this(3);
    }

    public GameManager(int lives) {
        this.lives = lives;
    }


    //Methods
    public boolean isGameOver() {
        return this.lives == 0;
    }


    public void moveCar(String direction) {
        if (direction.equals("left")) {
            if (this.carPosition == 0) {
                return;
            } else {
                setCarPosition(this.carPosition - 1);
            }
        } else {
            if (this.carPosition == 2) {
                return;
            } else {
                setCarPosition(this.carPosition + 1);
            }
        }
    }


    public void setRocksPeriod() {
        for (int i = this.rocksRows - 1; i > -1; i--) {
            for (int j = this.rocksCols - 1; j > -1; j--) {
                // clean first row
                if (i == 0) {
                    this.rocks[i][j] = this.NONE;
                }
                //move row up to down
                else {
                    this.rocks[i][j] = this.rocks[i - 1][j];
                }
            }
        }
        //fill top row add new Rock

            num = (int) (Math.random() * 3); // Generates a random number between 0 (inclusive) and matrixCols (exclusive)
                this.rocks[0][num] = this.ROCK;

    }


    public boolean checkCrush() {
        if (this.rocks[this.rocksRows - 1][this.carPosition].equals(this.ROCK)) {
            setLives(this.lives - 1);
            setAmountOfCrushes(this.amountOfCrushes + 1);
            return true;
        }
        return false;
    }



    //Setters/Getters
    public int getAmountOfCrushes() {
        return amountOfCrushes;
    }

    public void setAmountOfCrushes(int amountOfCrushes) {
        this.amountOfCrushes = amountOfCrushes;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getRocksRows() {
        return rocksRows;
    }

    public int getRocksCols() {
        return rocksCols;
    }

    public String getNONE() {
        return NONE;
    }


    public String getRock() {
        return ROCK;
    }


    public int getCarPosition() {
        return carPosition;
    }

    public int getLives() {
        return lives;
    }

    public void setCarPosition(int carPosition) {
        this.carPosition = carPosition;
    }

    public String[][] getRocks() {
        return rocks;
    }
}