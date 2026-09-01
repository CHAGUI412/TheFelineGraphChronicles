package io.mission2;

import algorithms.mission2.WeightedGraph;
import io.TokenScanner;

import java.util.ArrayList;
import java.util.List;

/**
 * Convierte el texto de entrada de la Misión 2 en una lista de casos
 * de prueba (grafo + inicio + destino).
 */
public final class Mission2Parser {

    public record TestCase(WeightedGraph graph, int source, int destination) {
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

            WeightedGraph graph = new WeightedGraph(nodeCount);
            for (int i = 0; i < edgeCount; i++) {
                int a = scanner.nextInt();
                int b = scanner.nextInt();
                long weight = scanner.nextLong();
                graph.addBidirectionalEdge(a, b, weight);
            }
            cases.add(new TestCase(graph, source, destination));
        }
        return cases;
    }
}