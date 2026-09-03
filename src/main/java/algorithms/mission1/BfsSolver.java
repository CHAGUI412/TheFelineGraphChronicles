package algorithms.mission1;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

/**
 * Busca el camino MÁS CORTO en el mapa (BFS = Breadth-First Search).
 *
 * Complejidad: O(R*C) en tiempo y espacio (R=filas, C=columnas) --
 * cada casilla se visita como máximo una vez. Es el algoritmo correcto
 * para esta misión porque el mapa no tiene pesos (cada paso cuesta
 * siempre 1), y BFS garantiza el camino más corto justo en ese caso.
 */
public final class BfsSolver {

    // El orden de movimiento: arriba, abajo, izquierda, derecha.
    private static final int[] DR = {-1, 1, 0, 0};
    private static final int[] DC = {0, 0, -1, 1};

    public PathResult solve(Board board, Point start, Point destination) {
        // Caso borde: si el inicio o el destino tienen bomba, es imposible.
        if (board.isBomb(start.row(), start.col()) || board.isBomb(destination.row(), destination.col())) {
            return PathResult.unreachable();
        }
        // Caso borde: ya estamos en el destino, 0 movimientos.
        if (start.equals(destination)) {
            return new PathResult(true, 0, List.of(start));
        }

        boolean[][] visited = new boolean[board.rows()][board.cols()];
        Point[][] parent = new Point[board.rows()][board.cols()];

        Deque<Point> queue = new ArrayDeque<>();
        queue.add(start);
        visited[start.row()][start.col()] = true;

        while (!queue.isEmpty()) {
            Point current = queue.poll(); // saca el siguiente pendiente

            if (current.equals(destination)) {
                // Reconstruimos el camino retrocediendo por "padres".
                List<Point> path = reconstructPath(parent, start, destination);
                return new PathResult(true, path.size() - 1, path);
            }

            // Miramos las 4 casillas vecinas.
            for (int dir = 0; dir < 4; dir++) {
                int newRow = current.row() + DR[dir];
                int newCol = current.col() + DC[dir];

                if (!board.inBounds(newRow, newCol)) continue;   // fuera del mapa
                if (board.isBomb(newRow, newCol)) continue;       // tiene bomba
                if (visited[newRow][newCol]) continue;            // ya la exploramos

                visited[newRow][newCol] = true;
                parent[newRow][newCol] = current;  // "llegué aquí desde 'current'"
                queue.add(new Point(newRow, newCol));
            }
        }

        // La cola se vació y nunca llegamos al destino.
        return PathResult.unreachable();
    }

    private List<Point> reconstructPath(Point[][] parent, Point start, Point destination) {
        List<Point> path = new ArrayList<>();
        Point current = destination;
        while (!current.equals(start)) {
            path.add(current);
            current = parent[current.row()][current.col()];
        }
        path.add(start);
        Collections.reverse(path); // estaba al revés (de Nina hacia Pola)
        return path;
    }
}