package algorithms.mission2;

import java.util.ArrayList;
import java.util.List;

/**
 * Grafo no dirigido con pesos, para Dijkstra. Los nodos son 0..n-1.
 * Guarda el grafo como una "lista de vecinos" — para cada nodo, guarda
 * con quién está conectado y con qué costo.
 */
public final class WeightedGraph {

    public record Edge(int to, long weight) {
    }

    private final List<List<Edge>> adjacency;

    public WeightedGraph(int nodeCount) {
        adjacency = new ArrayList<>(nodeCount);
        for (int i = 0; i < nodeCount; i++) {
            adjacency.add(new ArrayList<>());
        }
    }

    public void addBidirectionalEdge(int a, int b, long weight) {
        adjacency.get(a).add(new Edge(b, weight));
        adjacency.get(b).add(new Edge(a, weight));
    }

    public List<Edge> neighbours(int node) {
        return adjacency.get(node);
    }

    public int nodeCount() {
        return adjacency.size();
    }
}