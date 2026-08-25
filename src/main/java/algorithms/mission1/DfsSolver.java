package algorithms.mission1;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

/**
 * Busca UN camino válido (no necesariamente el más corto). DFS = Depth-First Search.
 */
public final class DfsSolver {

    private static final int[] DR = {-1, 1, 0, 0};
    private static final int[] DC = {0, 0, -1, 1};

    // Guarda una casilla pendiente.
    private record Frame(Point point, Point cameFrom) {}

    public PathResult solve(Board board, Point start, Point destination) {
        if (board.isBomb(start.row(), start.col()) || board.isBomb(destination.row(), destination.col())) {
            return PathResult.unreachable();
        }
        if (start.equals(destination)) {
            return new PathResult(true, 0, List.of(start));
        }

        boolean[][] visited = new boolean[board.rows()][board.cols()];
        Point[][] parent = new Point[board.rows()][board.cols()];

        Deque<Frame> stack = new ArrayDeque<>();
        stack.push(new Frame(start, null));

        while (!stack.isEmpty()) {
            Frame frame = stack.pop();
            Point current = frame.point();

            // Si ya fue visitada por otro camino mientras esperaba en la pila, ignorar.
            if (visited[current.row()][current.col()]) continue;

            visited[current.row()][current.col()] = true;
            parent[current.row()][current.col()] = frame.cameFrom();

            if (current.equals(destination)) {
                List<Point> path = reconstructPath(parent, start, destination);
                return new PathResult(true, path.size() - 1, path);
            }

            // Orden invertido (derecha, izquierda, abajo, arriba) para que al
            // sacar de la pila, salga en el orden correcto: arriba, abajo, izquierda, derecha.
            for (int dir = 3; dir >= 0; dir--) {
                int newRow = current.row() + DR[dir];
                int newCol = current.col() + DC[dir];

                if (!board.inBounds(newRow, newCol)) continue;
                if (board.isBomb(newRow, newCol)) continue;
                if (visited[newRow][newCol]) continue;

                stack.push(new Frame(new Point(newRow, newCol), current));
            }
        }
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
        Collections.reverse(path);
        return path;
    }
}