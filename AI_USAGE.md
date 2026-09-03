# AI_USAGE.md

## Herramientas usadas y para qué partes

Se usó Claude (Anthropic) durante todo el desarrollo con el fin de resolver dudas,
tener mas organización en el proyecto y apoyarnos para hacer la interfaz gráfica y los tests.

## 2-3 prompts decisivos

1. Pedir la implementación de DFS con orden determinista
   (arriba/abajo/izquierda/derecha) en vez de solo BFS — necesario
   porque el enunciado exige ese orden fijo específicamente para que
   el resultado de DFS sea comparable/calificable.
2. Pedir que se verificara cada algoritmo contra los valores exactos
   del enunciado (18/32 en Misión 1, 100/150 en Misión 2, etc.) antes
   de darlo por bueno — esto fue clave para detectar el bug de DFS
   descrito abajo, que de otra forma hubiera pasado desapercibido.
3. Pedir explícitamente el caso borde de la Sección 5 del enunciado
   ("un ciclo positivo que no puede llegar al destino no debe marcarse
   como infinito") como test automatizado, no solo como idea — el
   enunciado advierte que este caso rompe implementaciones descuidadas.

## Al menos dos casos donde el resultado generado estuvo mal o fue subóptimo

1. **Bug real en la primera versión de `DfsSolver`:** la primera
   implementación marcaba una casilla como "visitada" en el momento de
   meterla a la pila, no en el momento de sacarla. Al verificar contra
   el valor esperado del PDF (32 movimientos), dio 20 en cambio.
   El problema: un DFS recursivo de verdad no "reserva" los vecinos de
   una casilla apenas los descubre — se compromete de lleno con el
   primer vecino válido y solo retrocede si ese camino se agota. Marcar
   "visitado" demasiado pronto le robaba casillas al camino correcto.
   Se corrigió moviendo el `visited[...] = true` al momento de sacar de
   la pila (no de meter), replicando así el comportamiento real de la
   recursión.
2. **Layout inicial poco usable en `Mission1Panel`:** la primera versión
   ponía el área de salida en la región `NORTH` de un `BorderLayout`,
   lo que la dejaba con una altura mínima casi invisible. Se corrigió
   usando un `JSplitPane` vertical ajustable entre la salida de texto y
   el canvas de dibujo.
