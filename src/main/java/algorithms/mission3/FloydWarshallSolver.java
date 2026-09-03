package algorithms.mission3;

import java.util.Arrays;

/**
 * Este algoritmo responde: "¿cuál es la mejor ruta entre CADA PAR de
 * nodos del mapa?" -- todas las combinaciones a la vez, no solo una.
 *
 * La idea en una frase: por cada nodo del mapa, nos preguntamos "¿me
 * conviene hacer escala aquí para mejorar alguna ruta?" -- y repetimos
 * esa pregunta para todos los nodos, uno por uno.
 *
 * Complejidad: O(N^3) en tiempo (el triple for), O(N^2) en espacio
 * (la matriz). Es la elección correcta cuando se necesita la respuesta
 * para TODOS los pares a la vez (como exige la matriz de la GUI), a
 * diferencia de Bellman-Ford, que solo resuelve desde un origen fijo.
 */
public final class FloydWarshallSolver {

    public static final long NO_ROUTE = Long.MIN_VALUE / 4;

    public long[][] computeMatrix(DirectedWeightedGraph graph) {
        int n = graph.nodeCount();
        long[][] d = new long[n][n];
        for (long[] row : d) {
            Arrays.fill(row, NO_ROUTE);
        }
        for (int i = 0; i < n; i++) {
            d[i][i] = 0;
        }
        for (DirectedWeightedGraph.Edge e : graph.allEdges()) {
            if (e.weight() > d[e.from()][e.to()]) {
                d[e.from()][e.to()] = e.weight();
            }
        }

        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                if (d[i][k] == NO_ROUTE) continue;
                for (int j = 0; j < n; j++) {
                    if (d[k][j] == NO_ROUTE) continue;
                    long viaK = d[i][k] + d[k][j];
                    if (viaK > d[i][j]) {
                        d[i][j] = viaK;
                    }
                }
            }
        }
        return d;
    }

    /**
     * @return la matriz N x N completa de resultados, para mostrarla en la GUI.
     *         (Aquí no reconstruimos caminos individuales -- la matriz solo
     *         necesita el número/estado de cada celda; el camino resaltado
     *         del par S-D lo calcula BellmanFordSolver por separado.)
     */
    public MaxChurunResult[][] solveAllPairs(DirectedWeightedGraph graph) {
        int n = graph.nodeCount();
        long[][] d = computeMatrix(graph);

        boolean[][] unbounded = new boolean[n][n];
        for (int k = 0; k < n; k++) {
            if (d[k][k] > 0) {
                for (int i = 0; i < n; i++) {
                    if (d[i][k] == NO_ROUTE) continue;
                    for (int j = 0; j < n; j++) {
                        if (d[k][j] == NO_ROUTE) continue;
                        unbounded[i][j] = true;
                    }
                }
            }
        }

        MaxChurunResult[][] result = new MaxChurunResult[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (d[i][j] == NO_ROUTE) {
                    result[i][j] = MaxChurunResult.unreachable();
                } else if (unbounded[i][j]) {
                    result[i][j] = MaxChurunResult.unbounded(java.util.List.of());
                } else {
                    result[i][j] = MaxChurunResult.finite(d[i][j], java.util.List.of());
                }
            }
        }
        return result;
    }
}