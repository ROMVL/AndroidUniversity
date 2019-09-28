package ua.nure.romanikvladislav.colordisplay;

import java.io.Serializable;

public class Rgb implements Serializable {

    private int red;
    private int green;
    private int blue;

    public Rgb() {
        red = 0;
        green = 0;
        blue = 0;
    }

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int getBlue() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    @Override
    public String toString() {
        return "Rgb{" +
                "red=" + red +
                ", green=" + green +
                ", blue=" + blue +
                '}';
    }
}
