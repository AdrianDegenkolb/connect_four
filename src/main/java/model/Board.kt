package model;

import java.util.Iterator;

public class Board implements Iterable<Field> {
    public final int HEIGHT;
    public final int WIDTH;
    private final Field[][] fields;

    public Board(int width, int height) {
        if (width <= 0 || height <= 0) throw new IllegalArgumentException("height and with must be natural numbers");
        HEIGHT = height;
        WIDTH = width;
        fields = new Field[WIDTH][HEIGHT];
        for (int j = 0; j < HEIGHT; j++) for (int i = 0; i < WIDTH; i++) fields[i][j] = new Field();
    }

    public boolean throwInColumn(int x, Token player) {
        for (int y = 0; y < HEIGHT; y++){
            if (fields[x][y].isEmpty()) {
                fields[x][y].setPlayer(player);
                return true;
            }
        }
        return false;
    }

    public void removeOfColumn(int x) {
        for (int y = HEIGHT - 1; y >= 0; y--){
            if (!fields[x][y].isEmpty()) {
                fields[x][y].setPlayer(Token.EMPTY);
                return;
            }
        }
    }

    public Token getWinner() {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Token res = checkFor4RowOnField(x, y);
                if (res != Token.EMPTY) return res;
            }
        }
        return Token.EMPTY;
    }

    private Token checkFor4RowOnField(int x, int y) {
        Token playerToCheck = fields[x][y].getPlayer();
        if (playerToCheck == Token.EMPTY) return Token.EMPTY;
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

        return Token.EMPTY;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder().append("|");
        for (int y = HEIGHT - 1; y >= 0; y--) {
            for (int x = 0; x < WIDTH; x++) {
                if (fields[x][y].getPlayer() == Token.PLAYER_1) str.append("X|");
                else if (fields[x][y].getPlayer() == Token.PLAYER_2) str.append("O|");
                else str.append(" |");
            }
            if (y != 0) str.append("\n|");
        }
        str.append("\n");
        for (int x = 0; x < WIDTH; x++) str.append("-").append(x + 1);
        return str.append("-").toString();
    }

	public boolean stillSpace() {
        int y = HEIGHT - 1;
        for (int x = 0; x < WIDTH; x++) if (fields[x][y].getPlayer() == Token.EMPTY) return true;
        return false;
    }

    public Field get(int x, int y) {
        return fields[x][y];
    }

    @Override
    public Iterator<Field> iterator() {
        return new BoardIterator();
    }

    class BoardIterator implements Iterator<Field> {
        private int pointer = 0;

        @Override
        public boolean hasNext() {
            return pointer < HEIGHT * WIDTH;
        }

        @Override
        public Field next() {
            Field res = fields[pointer % WIDTH][pointer / WIDTH];
            pointer++;
            return res;
        }
    }
}