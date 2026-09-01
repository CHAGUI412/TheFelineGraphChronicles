package algorithms.mission3;

import java.util.ArrayList;
import java.util.List;

/**
 * Grafo dirigido con pesos (pueden ser negativos). Repetidas entre el
 * mismo par ordenado se guardan como aristas separadas.
 */
public final class DirectedWeightedGraph {

    public record Edge(int from, int to, long weight) {
    }

    private final int nodeCount;
    private final List<Edge> edges = new ArrayList<>();
    private final List<List<Edge>> outgoing;

    public DirectedWeightedGraph(int nodeCount) {
        this.nodeCount = nodeCount;
        outgoing = new ArrayList<>(nodeCount);
        for (int i = 0; i < nodeCount; i++) {
            outgoing.add(new ArrayList<>());
        }
    }

    public void addDirectedEdge(int from, int to, long weight) {
        Edge e = new Edge(from, to, weight);
        edges.add(e);
        outgoing.get(from).add(e);
    }

    public List<Edge> allEdges() {
        return edges;
    }

    public List<Edge> outgoing(int node) {
        return outgoing.get(node);
    }

    public int nodeCount() {
        return nodeCount;
    }
}