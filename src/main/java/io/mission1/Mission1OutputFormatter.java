package io.mission1;

import algorithms.mission1.PathResult;

/**
 * Da formato a un caso de la Misión 1: "Case #k: BFS <b> DFS <d>"
 * o "Case #k: Nina is unreachable" si no se pudo llegar.
 */
public final class Mission1OutputFormatter {

    public String format(int caseNumber, PathResult bfsResult, PathResult dfsResult) {
        if (!bfsResult.reachable() || !dfsResult.reachable()) {
            return "Case #" + caseNumber + ": Nina is unreachable";
        }
        return "Case #" + caseNumber + ": BFS " + bfsResult.moves() + " DFS " + dfsResult.moves();
    }
}
