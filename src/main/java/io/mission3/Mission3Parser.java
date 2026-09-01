package io.mission3;

import algorithms.mission3.DirectedWeightedGraph;
import io.TokenScanner;

import java.util.ArrayList;
import java.util.List;

/**
 * Convierte el texto de entrada de la Misión 3 en una lista de casos
 * de prueba (grafo dirigido + origen + destino).
 */
public final class Mission3Parser {

    public record TestCase(DirectedWeightedGraph graph, int source, int destination) {
    }

    public List<TestCase> parse(String rawInput) {
        TokenScanner scanner = new TokenScanner(rawInput);
        List<TestCase> cases = new ArrayList<>();

        int testCaseCount = scanner.nextInt();
        for (int t = 0; t < testCaseCount; t++) {
            int nodeCount = scanner.nextInt();
            int edgeCount = scanner.nextInt();
            int source = scanner.nextInt();
            int destination = scanner.nextInt();

            DirectedWeightedGraph graph = new DirectedWeightedGraph(nodeCount);
            for (int i = 0; i < edgeCount; i++) {
                int from = scanner.nextInt();
                int to = scanner.nextInt();
                long weight = scanner.nextLong();
                graph.addDirectedEdge(from, to, weight);
            }
            cases.add(new TestCase(graph, source, destination));
        }
        return cases;
    }
}