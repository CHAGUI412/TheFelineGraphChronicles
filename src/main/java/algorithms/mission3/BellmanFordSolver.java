package algorithms.mission3;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

/**
 * Este algoritmo responde una pregunta más chica que Floyd-Warshall:
 * "¿cuál es la mejor ruta desde UN origen fijo hacia todos los demás
 * nodos?" -- por eso su resultado para el destino D debe coincidir
 * siempre con lo que dice Floyd-Warshall (si no coinciden, algo está mal).
 *
 * Complejidad: O(N*M) en tiempo (N=nodos, M=conexiones), O(N) en
 * espacio. Se usa aquí, además, porque su técnica de "una ronda extra
 * de relajación" es la forma estándar de detectar ciclos de ganancia
 * positiva -- cosa que Floyd-Warshall detecta distinto (con d[k][k]).
 */
public final class BellmanFordSolver {

    private static final long NO_ROUTE = Long.MIN_VALUE / 4;

    public MaxChurunResult solveFromSource(DirectedWeightedGraph graph, int source, int destination) {
        int n = graph.nodeCount();
        long[] dist = new long[n];
        int[] parent = new int[n];
        Arrays.fill(dist, NO_ROUTE);
        Arrays.fill(parent, -1);
        dist[source] = 0;

        for (int iter = 0; iter < n - 1; iter++) {
            for (DirectedWeightedGraph.Edge e : graph.allEdges()) {
                if (dist[e.from()] == NO_ROUTE) continue;
                long candidate = dist[e.from()] + e.weight();
                if (candidate > dist[e.to()]) {
                    dist[e.to()] = candidate;
                    parent[e.to()] = e.from();
                }
            }
        }

        boolean[] flagged = new boolean[n];
        int cycleWitness = -1;
        for (DirectedWeightedGraph.Edge e : graph.allEdges()) {
            if (dist[e.from()] == NO_ROUTE) continue;
            if (dist[e.from()] + e.weight() > dist[e.to()]) {
                flagged[e.to()] = true;
                parent[e.to()] = e.from();
                cycleWitness = e.to();
            }
        }

        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            if (flagged[i]) stack.push(i);
        }
        while (!stack.isEmpty()) {
            int node = stack.pop();
            for (DirectedWeightedGraph.Edge e : graph.outgoing(node)) {
                if (!flagged[e.to()]) {
                    flagged[e.to()] = true;
                    stack.push(e.to());
                }
            }
        }

        if (dist[destination] == NO_ROUTE) {
            return MaxChurunResult.unreachable();
        }
        if (flagged[destination]) {
            return MaxChurunResult.unbounded(extractCycle(parent, cycleWitness, n));
        }
        return MaxChurunResult.finite(dist[destination], reconstructPath(parent, source, destination));
    }

    /**
     * A partir de un nodo "contagiado", retrocede n veces por los padres
     * para garantizar caer DENTRO del ciclo (no solo en un nodo que lo
     * alimenta), y desde ahí traza el ciclo completo hasta repetir.
     */
    private List<Integer> extractCycle(int[] parent, int witness, int n) {
        int node = witness;
        for (int i = 0; i < n; i++) {
            node = parent[node];
        }
        List<Integer> cycle = new ArrayList<>();
        int current = node;
        do {
            cycle.add(current);
            current = parent[current];
        } while (current != node && cycle.size() <= n);
        cycle.add(node);
        Collections.reverse(cycle);
        return cycle;
    }

    private List<Integer> reconstructPath(int[] parent, int source, int destination) {
        List<Integer> path = new ArrayList<>();
        int current = destination;
        while (current != source) {
            path.add(current);
            current = parent[current];
        }
        path.add(source);
        Collections.reverse(path);
        return path;
    }
}