package io.mission3;

import algorithms.mission3.MaxChurunResult;

/**
 * Da formato a un caso de la Mision 3, respetando el orden de
 * precedencia exacto del enunciado: primero "Limon blocked the way"
 * (inalcanzable), luego "Infinite churun!" (ciclo positivo), y solo
 * si ninguna de esas dos aplica, el numero.
 */
public final class Mission3OutputFormatter {

    public String format(int caseNumber, MaxChurunResult result) {
        return switch (result.status()) {
            case UNREACHABLE -> "Case #" + caseNumber + ": Limon blocked the way";
            case UNBOUNDED -> "Case #" + caseNumber + ": Infinite churun!";
            case FINITE -> "Case #" + caseNumber + ": " + result.value();
        };
    }
}