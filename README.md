# The Feline Graph Chronicles

Languages and Compilers — Universidad EIA. Sistema en Java + Swing que
resuelve las cuatro misiones del enunciado: BFS/DFS, Dijkstra,
Floyd-Warshall + Bellman-Ford, y Kruskal, con una interfaz gráfica
temática (gatos héroes vs. el villano Limón).

## Integrantes del grupo

- Valeria Gomez Cataño
- Isaac David Chagüi Galeano
- Juan Jose Restrepo Davila

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
├── gui/
│    ├── MainFrame          -> ventana principal, arma la barra de
│    │                         misiones y el panel de cada una
│    ├── TitleBanner        -> franja superior con el título del app
│    ├── mission1/  Mission1Panel, BoardCanvas
│    ├── mission2/  Mission2Panel, GraphCanvas
│    ├── mission3/  Mission3Panel, GraphCanvas3, MatrixPanel
│    ├── mission4/  Mission4Panel, MstCanvas
│    └── theme/             -> todo el sistema visual, compartido
│         ├── FelineTheme        colores, fuentes, estilos de componentes
│         ├── CatSprites         carga y dibuja los avatares circulares
│         ├── PawIcon            ícono de huella (pestañas, decoración)
│         ├── RoundedButton      botón con esquinas redondeadas
│         ├── RoundedLineBorder  borde redondeado reutilizable
│         ├── CardPanel          panel con fondo redondeado ("tarjeta")
│         ├── MissionTabButton   botón tipo píldora para elegir misión
│         └── MissionTabBar      barra que agrupa los MissionTabButton
└── Main.java

src/main/resources/
└── cats/          -> imágenes de los personajes (pola.png, minerva.png,
                       nero.png, limon.png), cargadas por CatSprites
                       desde el classpath

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
- **Dibujo del grafo/tablero:** ninguna librería externa — todo el
  dibujo de celdas, nodos, aristas y la matriz usa `Graphics2D` puro de
  Swing. Las imágenes de los personajes (Pola, Minerva, Nina, Limón) sí
  son archivos reales, cargados con `ImageIO` desde
  `src/main/resources/cats/` y dibujados como avatar circular (las
  imágenes originales no tienen fondo transparente).
- **Sistema de tema visual (`gui/theme/`):** un paquete completo de 8
  clases (no una sola), compartido por las 4 misiones — colores y
  fuentes centralizados en `FelineTheme`, componentes propios
  (`RoundedButton`, `CardPanel`, `MissionTabButton`) dibujados a mano
  con `Graphics2D` en vez de depender del estilo por defecto de Swing.
- **Sin Look & Feel forzado:** se probó Nimbus, pero daba mal contraste
  en pestañas y desplegables sin control directo sobre cada
  componente — se optó por estilar cada componente explícitamente
  (`styleButton`, `styleComboBox`, etc.), que funciona igual sin
  importar el Look & Feel del sistema operativo.
- **Selector de misión propio (`MissionTabBar`):** en vez de las
  pestañas nativas de `JTabbedPane` (que no dan feedback visual al
  interactuar), se construyó una barra de botones tipo "píldora" con
  resalte al pasar el mouse y halo dorado al seleccionar, combinada con
  `CardLayout` para mostrar el panel de cada misión.
- **Animaciones con `javax.swing.Timer`:** cada misión anima un
  personaje recorriendo el camino/ciclo/árbol resultante, con duración
  total fija (~2.5–3 segundos) sin importar cuántos pasos tenga el
  camino — la velocidad se ajusta automáticamente para que la
  demostración sea siempre predecible en tiempo.

