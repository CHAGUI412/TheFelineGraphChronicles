package algorithms.mission4;

import java.util.List;

/**
 * Resultado de Kruskal: ¿se pudo conectar todo?, ¿con qué costo total?,
 * ¿qué cables se usaron? (para poder resaltarlos en la GUI).
 */
public record MstResult(boolean connected, long totalCost, List<Edge> mstEdges) {

    public static MstResult impossible() {
        return new MstResult(false, 0L, List.of());
    }
}