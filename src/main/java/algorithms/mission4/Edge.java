package algorithms.mission4;

/**
 * Un cable candidato entre dos intersecciones. Trabajamos 0-indexado
 * internamente; el parser convierte desde el input, que es 1-indexado.
 */
public record Edge(int a, int b, long cost) {
}