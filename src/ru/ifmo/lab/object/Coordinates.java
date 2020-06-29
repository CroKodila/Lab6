package ru.ifmo.lab.object;

import java.io.Serializable;

public class Coordinates implements Serializable {
    private Long x; //Значение поля должно быть больше -746, Поле не может быть null
    private Double y;// Поле не может быть null

    public Coordinates(long x, double y){
        this.x = x;
        this.y = y;
    }

    public Long getX() {
        return x;
    }

    public void setX(Long x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append("\t").append("coordinates(x): ").append(this.x).append("\n");
        sb.append("\t").append("coordinates(y): ").append(this.y);
        return sb.toString();
    }

    public Coordinates(){};
}

