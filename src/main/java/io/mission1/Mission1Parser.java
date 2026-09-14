package io.mission1;

import algorithms.mission1.Board;
import algorithms.mission1.Point;
import io.TokenScanner;

import java.util.ArrayList;
import java.util.List;

/**
 * Convierte el texto de entrada de la Misión 1 en una lista de casos
 * de prueba (tablero + inicio + destino), listos para los solvers.
 */
public final class Mission1Parser {

    public record TestCase(Board board, Point start, Point destination) {
    }

    public List<TestCase> parse(String rawInput) {
        TokenScanner scanner = new TokenScanner(rawInput);
        List<TestCase> cases = new ArrayList<>();

        while (scanner.hasNext()) {
            int rows = scanner.nextInt();
            int cols = scanner.nextInt();
            if (rows == 0 && cols == 0) {
                break; // caso final, no se procesa
            }
            // NUEVO: sin esto, un R o C negativo tira un error de Java
            // ilegible ("-5" a secas) en vez de un mensaje claro.
            if (rows < 0 || cols < 0) {
                throw new IllegalArgumentException(
                        "El tablero debe tener dimensiones positivas (se recibió R=" + rows + ", C=" + cols + ")");
            }

            Board board = new Board(rows, cols);
            int bombRows = scanner.nextInt();
            for (int i = 0; i < bombRows; i++) {
                int rowIndex = scanner.nextInt();
                int bombCount = scanner.nextInt();
                for (int j = 0; j < bombCount; j++) {
                    int col = scanner.nextInt();
                    board.setBomb(rowIndex, col);
                }
            }

            int startRow = scanner.nextInt();
            int startCol = scanner.nextInt();
            int destRow = scanner.nextInt();
            int destCol = scanner.nextInt();

            cases.add(new TestCase(board, new Point(startRow, startCol), new Point(destRow, destCol)));
        }
        return cases;
    }
}