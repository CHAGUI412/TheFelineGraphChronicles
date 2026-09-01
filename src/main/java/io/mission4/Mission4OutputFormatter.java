package io.mission4;

import algorithms.mission4.MstResult;

/**
 * Da formato a un caso de la Misión 4: "Case #k: <costo total>"
 * o "Case #k: Limon cut too many cables" si no se pudo conectar todo.
 */
public final class Mission4OutputFormatter {

    public String format(int caseNumber, MstResult result) {
        if (!result.connected()) {
            return "Case #" + caseNumber + ": Limon cut too many cables";
        }
        return "Case #" + caseNumber + ": " + result.totalCost();
    }
}