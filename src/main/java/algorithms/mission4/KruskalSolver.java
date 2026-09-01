package algorithms.mission4;

import java.util.ArrayList;
import java.util.List;

/**
 * Kruskal: ordena los cables de más barato a más caro, y va agregando
 * cada uno solo si conecta dos zonas todavía no conectadas entre sí
 * (usando UnionFind para saberlo rápido). El tiempo lo domina el
 * ordenamiento: O(C log C).
 */
public final class KruskalSolver {

    public MstResult solve(int nodeCount, List<Edge> candidateEdges) {
        List<Edge> sorted = new ArrayList<>(candidateEdges);
        sorted.sort((e1, e2) -> Long.compare(e1.cost(), e2.cost()));

        UnionFind unionFind = new UnionFind(nodeCount);
        List<Edge> mstEdges = new ArrayList<>();
        long totalCost = 0;

        for (Edge edge : sorted) {
            if (edge.a() == edge.b()) {
                continue; // self-loop: nunca conecta dos zonas distintas
            }
            if (unionFind.union(edge.a(), edge.b())) {
                mstEdges.add(edge);
                totalCost += edge.cost();
            }
        }

        if (mstEdges.size() != nodeCount - 1) {
            return MstResult.impossible(); // no se logró conectar todo
        }
        return new MstResult(true, totalCost, mstEdges);
    }
}