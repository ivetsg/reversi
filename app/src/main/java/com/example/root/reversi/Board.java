package com.example.root.reversi;

import java.io.Serializable;

/**
 * Created by root on 24/04/18.
 */

public class Board implements Serializable {

    public Cela[][] cells;
    private final int order;

    public int black;
    public int white;
    public int casellesRestants;

    private Board(int order, Display display) {
        this.order = order;
        this.cells = new Cela[order][order];
        this.black = 0;
        this.white = 0;
        this.casellesRestants = order*2;
        initBoard();
    }

    public Board(int order) {
        this(order, null);
    }

    private void initBoard(){
        for (int i = 0; i < order; i++) {
            for (int j = 0; j < order; j++) {
                this.cells[i][j] = Cela.empty();
            }
        }

        this.cells[order/2 - 1][order/2 - 1] = Cela.white();
        this.cells[order/2 - 1][order/2] = Cela.black();
        this.cells[order/2][order/2 - 1] = Cela.black();
        this.cells[order/2][order/2] = Cela.white();
        this.cells[order/2-1][order/2-2] = Cela.hint();
        this.cells[order/2-2][order/2-1] = Cela.hint();
        this.cells[order/2+1][order/2] = Cela.hint();
        this.cells[order/2][order/2+1] = Cela.hint();


        this.sumar(2, "Black");
        this.sumar(2, "White");
        calculCasellesRestants();
    }

    public int size() {
        return order;
    }

    public boolean contains(Position position) {
        return position.getRow() < this.size() && position.getColumn() < this.size() && position.getRow() >= 0 && position.getColumn() >= 0;
    }

    public boolean isEmpty(Position position) {
        return this.contains(position) && (this.cells[position.getRow()][position.getColumn()].isEmpty() || this.contains(position) && this.cells[position.getRow()][position.getColumn()].isHint());
    }

    public boolean isWhite(Position position) {
        return this.contains(position) && this.cells[position.getRow()][position.getColumn()].isWhite();
    }

    public boolean isBlack(Position position) {
        return this.contains(position) && this.cells[position.getRow()][position.getColumn()].isBlack();

    }

    public void setWhite(Position position) {
        if (this.isValidEmpy(position)) {
            this.cells[position.getRow()][position.getColumn()].setWhite();
            this.sumar(1, "White");
        }
    }

    public void setBlack(Position position) {
        if (this.isValidEmpy(position)) {
            this.cells[position.getRow()][position.getColumn()].setBlack();
            this.sumar(1, "Black");
        }
    }

    public void reverse(Position position) {
        if (this.isValidFull(position)) {
            changeColors(position);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(64);
        for (Cela[] cell : this.cells) {
            for (Cela aCell : cell) {
                sb.append(aCell.toString());
            }
            sb.append("\n");
        }
        return sb.toString();
    }


    //Metodes Auxiliars
    private boolean isValidEmpy(Position position) {
        return this.contains(position) && this.isEmpty(position);
    }

    private boolean isValidFull(Position position) {
        return this.contains(position) && !this.isEmpty(position);
    }

    private void sumar(int i, String color) {
        if (color.equals("Black")) {
            this.black += i;
        } else {
            this.white += i;
        }
    }

    private void restar(int i, String color) {
        if (color.equals("Black")) {
            this.black -= i;
        } else {
            this.white -= i;
        }
    }

    private void changeColors(Position position) {
        if (this.cells[position.getRow()][position.getColumn()].isBlack()) {
            this.cells[position.getRow()][position.getColumn()].setWhite();
            this.restar(1, "Black");
            this.sumar(1, "White");
        } else {
            this.cells[position.getRow()][position.getColumn()].setBlack();
            this.sumar(1, "Black");
            this.restar(1, "White");
        }
        calculCasellesRestants();
    }

    private void calculCasellesRestants(){
        this.casellesRestants = (order*order) - this.black - this.white;
    }


}
