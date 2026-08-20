package algorithms.mission1;

import java.util.List;

/**
 * La respuesta de BFS o DFS: ¿se llegó?, ¿en cuántos pasos?, ¿por qué camino?
 */
public record PathResult(boolean reachable, int moves, List<Point> path) {

    public static PathResult unreachable() {
        return new PathResult(false, -1, List.of());
    }
}