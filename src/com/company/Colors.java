package com.company;

//does starting state or a matter?

import java.util.ArrayList;

public class Colors {
    private int state = 0, a = 255, r, g, b, changeRate;
    public ArrayList<Integer[]> dimensionColors = new ArrayList<Integer[]>();

    public Colors (int startR, int startG, int startB, double ratio) {
        this.r = startR;
        this.g = startG;
        this.b = startB;
        //changeRate = (int)Math.ceil(1/ratio);
        changeRate = 25;
        Integer[] startingColors = {r, g, b};
        dimensionColors.add(startingColors);
    }

    public void setRGB() {
        if(state == 0){
            g += changeRate;
            if(g >= 255) {
                state = 1;
                g = 255;
            }
        }
        if(state == 1){
            r -= changeRate;
            if(r <= 0) {
                state = 2;
                r = 0;
            }
        }
        if(state == 2){
            b += changeRate;
            if(b >= 255) {
                state = 3;
                b = 255;
            }
        }
        if(state == 3){
            g -= changeRate;
            if(g <= 0) {
                state = 4;
                g = 0;
            }
        }
        if(state == 4){
            r += changeRate;
            if(r >= 255) {
                state = 5;
                r = 255;
            }
        }
        if(state == 5) {
            b -= changeRate;
            if (b <= 0) {
                state = 0;
                b = 0;
            }
        }
        Integer[] currentColors = {r, g, b};
        dimensionColors.add(currentColors);
        //int hex = (a << 24) + (r << 16) + (g << 8) + (b);
    }

    public void undoRGB() {
        if (b == 0)
        if(state == 0){
            b += changeRate;
            state = 5;
        }
        if(state == 1){
            g -= changeRate;
            state = 0;
        }
        if(state == 2){
            r += changeRate;
            state = 1;
        }
        if(state == 3){
            b -= changeRate;
            state = 2;
        }
        if(state == 4){
            g += changeRate;
            state = 3;
        }
        if(state == 5) {
            r -= changeRate;
            state = 4;
        }
    }

    public int getR() {
        return r;
    }

    public int getRSafe() {
        return r;
    }

    public int getG() {
        return g;
    }

    public int getB() {
        return b;
    }

    public int getChangeRate() {
        return changeRate;
    }

    public int getDimNum() {
        return dimensionColors.size();
    }

    public void setR(int r) {
        this.r = r;
    }

    public void setG(int g) {
        this.g = g;
    }

    public void setB(int b) {
        this.b = b;
    }
}
