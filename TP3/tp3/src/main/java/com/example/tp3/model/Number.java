package com.example.tp3.model;

public class Number {
    private int number;
    private int square;

    public Number(int number,int square) {
        this.number = number;
        this.square = square;

    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getSquare() {
        return square;
    }

    public void setSquare(int square) {
        this.square = square;
    }
}
