package org.example;

public class Ring {
    private int groesse;
    private int position = 0;

    public Ring(int groesse) {
        this.groesse = groesse;
    }

    public int getGroesse() {
        return groesse;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setGroesse(int groesse) {
        this.groesse = groesse;
    }

    public String getLabel() {
        return String.valueOf((char)('A' + groesse - 1));
    }
}
