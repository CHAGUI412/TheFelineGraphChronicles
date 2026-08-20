package algorithms.mission1;

/**
 * El mapa: cuántas filas/columnas tiene, y dónde están las bombas.
 */
public final class Board {

    private final int rows;
    private final int cols;
    private final boolean[][] bomb;

    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.bomb = new boolean[rows][cols];
    }

    public void setBomb(int row, int col) {
        bomb[row][col] = true;
    }

    public boolean isBomb(int row, int col) {
        return bomb[row][col];
    }

    public int rows() {
        return rows;
    }

    public int cols() {
        return cols;
    }

    public boolean inBounds(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }
}