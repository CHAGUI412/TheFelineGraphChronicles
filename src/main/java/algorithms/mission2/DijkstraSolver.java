package algorithms.mission2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Dijkstra con cola de prioridad, tal como exige el enunciado.
 *
 * Complejidad: O((N + C) log N) con la cola de prioridad basada en
 * heap (N=nodos, C=conexiones). Es el algoritmo correcto aquí porque
 * los pesos son siempre no-negativos -- si hubiera pesos negativos,
 * un nodo ya "cerrado" podría dejar de ser el óptimo después de todo,
 * y el algoritmo daría una respuesta incorrecta.
 */
public final class DijkstraSolver {

    private record Candidate(int node, long distance) {
    }

    public DijkstraResult solve(WeightedGraph graph, int source, int destination) {
        if (source == destination) {
            return new DijkstraResult(true, 0L, List.of(source));
        }

        long[] dist = new long[graph.nodeCount()];
        int[] parent = new int[graph.nodeCount()];
        Arrays.fill(dist, Long.MAX_VALUE);
        Arrays.fill(parent, -1);
        dist[source] = 0L;

        PriorityQueue<Candidate> queue = new PriorityQueue<>((a, b) -> Long.compare(a.distance(), b.distance()));
        queue.add(new Candidate(source, 0L));
        boolean[] visited = new boolean[graph.nodeCount()];

        while (!queue.isEmpty()) {
            Candidate current = queue.poll();
            if (visited[current.node()]) continue;
            visited[current.node()] = true;

            if (current.node() == destination) {
                return new DijkstraResult(true, current.distance(), reconstructPath(parent, source, destination));
            }

            for (WeightedGraph.Edge edge : graph.neighbours(current.node())) {
                if (visited[edge.to()]) continue;
                long newDist = current.distance() + edge.weight();
                if (newDist < dist[edge.to()]) {
                    dist[edge.to()] = newDist;
                    parent[edge.to()] = current.node();
                    queue.add(new Candidate(edge.to(), newDist));
                }
            }
        }
        return DijkstraResult.unreachable();
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