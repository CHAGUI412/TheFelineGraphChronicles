package io.mission2;

import algorithms.mission2.DijkstraResult;

/**
 * Da formato a un caso de la Misión 2: "Case #k: <costo>"
 * o "Case #k: Nina is very sad" si no se pudo llegar.
 */
public final class Mission2OutputFormatter {

    public String format(int caseNumber, DijkstraResult result) {
        if (!result.reachable()) {
            return "Case #" + caseNumber + ": Nina is very sad";
        }
        return "Case #" + caseNumber + ": " + result.cost();
    }
}