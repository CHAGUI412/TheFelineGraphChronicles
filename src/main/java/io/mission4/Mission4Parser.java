package io.mission4;

import algorithms.mission4.Edge;
import io.TokenScanner;

import java.util.ArrayList;
import java.util.List;

/**
 * Convierte el texto de entrada de la Misión 4 en una lista de casos
 * de prueba (número de intersecciones + cables candidatos).
 * OJO: el input usa intersecciones 1-indexadas (1..N); acá les
 * restamos 1 para trabajar 0-indexado, como el resto del proyecto.
 */
public final class Mission4Parser {

    public record TestCase(int nodeCount, List<Edge> candidateEdges) {
    }

    public List<TestCase> parse(String rawInput) {
        TokenScanner scanner = new TokenScanner(rawInput);
        List<TestCase> cases = new ArrayList<>();

        int testCaseCount = scanner.nextInt();
        for (int t = 0; t < testCaseCount; t++) {
            int nodeCount = scanner.nextInt();
            int edgeCount = scanner.nextInt();

            List<Edge> edges = new ArrayList<>();
            for (int i = 0; i < edgeCount; i++) {
                int a = scanner.nextInt() - 1; // el input es 1-indexado
                int b = scanner.nextInt() - 1;
                long cost = scanner.nextLong();
                edges.add(new Edge(a, b, cost));
            }
            cases.add(new TestCase(nodeCount, edges));
        }
        return cases;
    }
}