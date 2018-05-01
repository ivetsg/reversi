package com.example.root.reversi;

import java.io.Serializable;

/**
 * Created by orioljorge on 23/4/18.
 */

public class Cela implements Serializable {

    private static final char WHITE = 'w';
    private static final char BLACK = 'b';
    private static final char EMPTY = '·';
    private static final char HINT = 'h';

    private char state;

    private Cela(char state) {
        this.state = state;
    }

    public static Cela empty() {
        return new Cela(EMPTY);
    }

    public static Cela white() {
        return new Cela(WHITE);
    }

    public static Cela black() {
        return new Cela(BLACK);
    }

    public static Cela hint() {
        return new Cela(HINT);
    }

    public boolean isEmpty() {
        return this.state == EMPTY;
    }

    public boolean isWhite() {
        return this.state == WHITE;
    }

    public boolean isBlack() {
        return this.state == BLACK;
    }

    public boolean isHint() {
        return this.state == HINT;
    }

    public void setHint() {
        this.state = HINT;
    }

    public void setWhite() {
        this.state = WHITE;
    }

    public void setBlack() {
        this.state = BLACK;
    }

    public void reverse() {
        switch (this.state) {
            case WHITE:
                this.state = BLACK;
                break;
            case BLACK:
                this.state = WHITE;
                break;
            default:
                this.state = EMPTY;
                break;
        }
    }

    public String toString() {
        return String.valueOf(this.state);
    }

    public static Cela cellFromChar(char c) {
        switch (c) {
            case WHITE:
                return white();
            case BLACK:
                return black();
            case HINT:
                return hint();
            default:
                return empty();
        }
    }
}