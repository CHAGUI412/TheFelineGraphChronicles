package algorithms.mission4;

/**
 * Estructura Union-Find (conjuntos disjuntos), con compresión de camino
 * y unión por tamaño, tal como exige el enunciado.
 * find(): "¿a qué grupo pertenece x?" -- casi O(1) en la práctica.
 * union(): "conecta los grupos de a y b" -- devuelve false si ya estaban juntos.
 */
public final class UnionFind {

    private final int[] parent;
    private final int[] size;

    public UnionFind(int n) {
        parent = new int[n];
        size = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }

    public int find(int x) {
        while (parent[x] != x) {
            parent[x] = parent[parent[x]]; // compresión de camino
            x = parent[x];
        }
        return x;
    }

    /**
     * @return true si a y b estaban en grupos distintos (y ahora quedan
     *         unidos); false si ya pertenecían al mismo grupo.
     */
    public boolean union(int a, int b) {
        int rootA = find(a);
        int rootB = find(b);
        if (rootA == rootB) {
            return false;
        }
        if (size[rootA] < size[rootB]) {
            int temp = rootA;
            rootA = rootB;
            rootB = temp;
        }
        parent[rootB] = rootA;
        size[rootA] += size[rootB];
        return true;
    }
}