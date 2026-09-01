package algorithms.mission3;

import java.util.List;

/**
 * Resultado de una consulta de churun máximo, siguiendo la precedencia
 * exacta del enunciado: UNREACHABLE > UNBOUNDED > FINITE.
 * "path" trae el camino real (caso FINITE) o el ciclo responsable
 * (caso UNBOUNDED) -- para poder dibujarlo en la GUI.
 */
public record MaxChurunResult(Status status, long value, List<Integer> path) {

    public enum Status { UNREACHABLE, UNBOUNDED, FINITE }

    public static MaxChurunResult unreachable() {
        return new MaxChurunResult(Status.UNREACHABLE, 0L, List.of());
    }

    public static MaxChurunResult unbounded(List<Integer> cycle) {
        return new MaxChurunResult(Status.UNBOUNDED, 0L, cycle);
    }

    public static MaxChurunResult finite(long value, List<Integer> path) {
        return new MaxChurunResult(Status.FINITE, value, path);
    }
}