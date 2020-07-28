package model;

import java.awt.Graphics;

import model.procedure.Display;

import java.awt.Color;

public class Board {
    public static final int HEIGHT = 6;
    public static final int WIDTH = 7;
    private Field[][] fields = new Field[WIDTH][HEIGHT];

    public Board() {
        for (int j = 0; j < HEIGHT; j++) for (int i = 0; i < WIDTH; i++) fields[i][j] = new Field();
    }

    public boolean throwInColumn(int x, Identifier player) {
        for (int y = 0; y < HEIGHT; y++){
            if (fields[x][y].isEmpty()) {
                fields[x][y].setPlayer(player);
                return true;
            }
        }
        return false;
    }

    public boolean removeOfColumn(int x) {
        for (int y = HEIGHT - 1; y >= 0; y--){
            if (!fields[x][y].isEmpty()) {
                fields[x][y].setPlayer(Identifier.EMPTY);
                return true;
            }
        }
        return false;
    }

    public Identifier getWinner() {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Identifier res = checkFor4RowOnField(x, y);
                if (res != Identifier.EMPTY) return res;
            }
        }
        return Identifier.EMPTY;
    }

    private Identifier checkFor4RowOnField(int x, int y) {
        Identifier playerToCheck = fields[x][y].getPlayer();
        if (playerToCheck == Identifier.EMPTY) return Identifier.EMPTY;
        int amountInRow = 1;
        for (int dx = 1; true; dx++) {
            if (x + dx >= WIDTH) break;
            if (fields[x + dx][y].getPlayer() != playerToCheck) break;
            amountInRow++;
        }
        for (int dx = 1; true; dx++) {
            if (x - dx < 0) break;
            if (fields[x - dx][y].getPlayer() != playerToCheck) break;
            amountInRow++;
        }
        if (amountInRow >= 4) return playerToCheck;

        amountInRow = 1;
        for (int dy = 1; true; dy++) {
            if (y + dy >= HEIGHT) break;
            if (fields[x][y + dy].getPlayer() != playerToCheck) break;
            amountInRow++;
        }
        for (int dy = 1; true; dy++) {
            if (y - dy < 0) break;
            if (fields[x][y - dy].getPlayer() != playerToCheck) break;
            amountInRow++;
        }
        if (amountInRow >= 4) return playerToCheck;

        amountInRow = 1;
        for (int d = 1; true; d++) {
            if (x + d >= WIDTH || y + d >= HEIGHT) break;
            if (fields[x + d][y + d].getPlayer() != playerToCheck) break;
            amountInRow++;
        }
        for (int d = 1; true; d++) {
            if (x - d < 0 || y - d < 0) break;
            if (fields[x - d][y - d].getPlayer() != playerToCheck) break;
            amountInRow++;
        }
        if (amountInRow >= 4) return playerToCheck;

        amountInRow = 1;
        for (int d = 1; true; d++) {
            if (x + d >= WIDTH || y - d < 0) break;
            if (fields[x + d][y - d].getPlayer() != playerToCheck) break;
            amountInRow++;
        }
        for (int d = 1; true; d++) {
            if (x - d < 0 || y + d >= HEIGHT) break;
            if (fields[x - d][y + d].getPlayer() != playerToCheck) break;
            amountInRow++;
        }
        if (amountInRow >= 4) return playerToCheck;

        return Identifier.EMPTY;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder().append("|");
        for (int y = HEIGHT - 1; y >= 0; y--) {
            for (int x = 0; x < WIDTH; x++) {
                if (fields[x][y].getPlayer() == Identifier.PLAYER_1) str.append("X|");
                else if (fields[x][y].getPlayer() == Identifier.PLAYER_2) str.append("O|");
                else str.append(" |");
            }
            if (y != 0) str.append("\n|");
        }
        str.append("\n");
        for (int x = 0; x < WIDTH; x++) str.append("--");
        return str.append("-").toString();
    }

	public boolean stillSpace() {
        int y = HEIGHT - 1;
        for (int x = 0; x < WIDTH; x++) if (fields[x][y].getPlayer() == Identifier.EMPTY) return true;
        return false;
    }

    public Field[][] getFields() {
        return fields.clone();
    }

    public void render(Graphics g) {
        g.setColor(Color.white);
        for (int x = 0; x < WIDTH + 1; x++) {
            g.drawLine(x * Display.GRIDSIZE, 0, x * Display.GRIDSIZE, (HEIGHT + 1) * Display.GRIDSIZE);
        }
        for (int y = 0; y < HEIGHT + 1; y++) {
            g.drawLine(0, y * Display.GRIDSIZE, (WIDTH + 1) * Display.GRIDSIZE, y * Display.GRIDSIZE);
        }
        int margin = 10;
        int radius = Display.GRIDSIZE/2 - margin;
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                if (fields[x][y].getPlayer() == Identifier.PLAYER_1) {
                    g.setColor(new Color(40, 160, 40));
                    g.fillOval(x * Display.GRIDSIZE + margin, -(y + 1) * Display.GRIDSIZE + margin + HEIGHT * Display.GRIDSIZE, 2 * radius, 2 * radius);
                } else if (fields[x][y].getPlayer() == Identifier.PLAYER_2) {
                    g.setColor(new Color(160, 40, 40));
                    g.fillOval(x * Display.GRIDSIZE + margin, -(y + 1) * Display.GRIDSIZE + margin + HEIGHT * Display.GRIDSIZE, 2 * radius, 2 * radius);
                }
            }
        }
    }
}