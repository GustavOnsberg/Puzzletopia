package main;

import java.awt.*;
import java.util.ArrayList;

public class PuzzleInfo {
    public int getN() {
        return n;
    }

    public int getM() {
        return m;
    }

    public ArrayList<Point> getPointCross() {
        return pointCross;
    }

    public ArrayList<Point> getPointHorizontal() {
        return pointHorizontal;
    }

    public ArrayList<Point> getPointVertical() {
        return pointVertical;
    }

    int n;
    int m;
    ArrayList<Point> pointCross;
    ArrayList<Point> pointHorizontal;
    ArrayList<Point> pointVertical;

    public PuzzleInfo(int n, int m){
        this.n = n;
        this.m = m;

        pointCross = new ArrayList<Point>();
        pointHorizontal = new ArrayList<Point>();
        pointVertical = new ArrayList<Point>();
    }
}
