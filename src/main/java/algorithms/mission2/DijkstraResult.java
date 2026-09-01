package algorithms.mission2;

import java.util.List;

/**
 * Resultado de Dijkstra: ¿se llegó?, ¿con qué costo?, ¿por qué camino?
 * UNREACHABLE es el centinela de "sin ruta" -- nunca hacer aritmética sobre él.
 */
public record DijkstraResult(boolean reachable, long cost, List<Integer> path) {

    public static final long UNREACHABLE = Long.MAX_VALUE;

    public static DijkstraResult unreachable() {
        return new DijkstraResult(false, UNREACHABLE, List.of());
    }
}