# The Feline Graph Chronicles

Languages and Compilers — Universidad EIA. Sistema en Java + Swing que
resuelve las cuatro misiones del enunciado: BFS/DFS, Dijkstra,
Floyd-Warshall + Bellman-Ford, y Kruskal.

## Integrantes del grupo

- TODO: nombre 1
- TODO: nombre 2 (si aplica)
- TODO: nombre 3 (si aplica)

## Cómo compilar y ejecutar

Requiere JDK 17+ y Maven.

```bash
mvn clean package
java -jar target/feline-graph-chronicles.jar
```

Para correr los tests:

```bash
mvn test
```

## Estructura del proyecto

```
src/main/java/
├── algorithms/    -> lógica pura de los 6 algoritmos (sin GUI, sin I/O)
│    ├── mission1/  Point, Board, PathResult, BfsSolver, DfsSolver
│    ├── mission2/  WeightedGraph, DijkstraResult, DijkstraSolver
│    ├── mission3/  DirectedWeightedGraph, MaxChurunResult,
│    │              FloydWarshallSolver, BellmanFordSolver
│    └── mission4/  Edge, UnionFind, MstResult, KruskalSolver
├── io/            -> parseo de input y formateo de output, por misión
├── samples/       -> inputs de ejemplo (una sola fuente de verdad,
│                     compartida entre la GUI y los tests)
├── gui/           -> Swing: un panel + un canvas de dibujo por misión
└── Main.java

src/test/java/     -> tests con JUnit 5, valores esperados sacados del
                       enunciado, uno por algoritmo (27 tests en total)
```

## Decisiones tomadas

- **Estructura de paquetes simplificada:** decidimos no usar el prefijo
  `com.eia.felinegraph` (convención estándar de Java para publicar
  librerías) porque este proyecto nunca se publica — los paquetes van
  directo bajo `src/main/java/` (`algorithms`, `io`, `gui`, `samples`).
- **DFS iterativo (no recursivo):** usamos una pila explícita
  (`ArrayDeque`) en vez de recursión, porque los tableros de la Misión 1
  pueden llegar a 10^6 celdas y desbordarían el stack de Java.
- **Estructuras de datos:** `PriorityQueue` para Dijkstra (exigido por
  el enunciado), `Union-Find` con compresión de camino + unión por
  tamaño para Kruskal.
- **Librería de dibujo:** ninguna — todo el dibujo (tableros, grafos,
  matriz) usa `Graphics2D` puro de Swing, sin librerías externas.
- **Un solo `FelineTheme`** con la paleta de colores, compartido entre
  los 4 canvas de dibujo, para que toda la app se vea consistente.