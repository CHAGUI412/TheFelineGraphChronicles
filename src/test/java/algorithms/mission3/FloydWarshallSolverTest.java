package algorithms.mission3;

import io.mission3.Mission3Parser;
import org.junit.jupiter.api.Test;
import samples.SampleInputs;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FloydWarshallSolverTest {

    @Test
    void matchesTheThreeSampleCases() {
        Mission3Parser parser = new Mission3Parser();
        List<Mission3Parser.TestCase> cases = parser.parse(SampleInputs.MISSION_3);
        FloydWarshallSolver solver = new FloydWarshallSolver();

        var r1 = solver.solveAllPairs(cases.get(0).graph())[cases.get(0).source()][cases.get(0).destination()];
        assertTrue(r1.status() == MaxChurunResult.Status.FINITE);
        assertEquals(110, r1.value());

        var r2 = solver.solveAllPairs(cases.get(1).graph())[cases.get(1).source()][cases.get(1).destination()];
        assertTrue(r2.status() == MaxChurunResult.Status.UNBOUNDED);

        var r3 = solver.solveAllPairs(cases.get(2).graph())[cases.get(2).source()][cases.get(2).destination()];
        assertTrue(r3.status() == MaxChurunResult.Status.FINITE);
        assertEquals(-65, r3.value());
    }

    @Test
    void doesNotMarkUnboundedWhenPositiveCycleCannotReachDestination() {
        // Ciclo positivo entre 1 y 2, pero SIN salida hacia 3.
        DirectedWeightedGraph graph = new DirectedWeightedGraph(4);
        graph.addDirectedEdge(0, 1, 5);
        graph.addDirectedEdge(1, 2, 10);
        graph.addDirectedEdge(2, 1, -3); // ciclo, ganancia neta +7
        graph.addDirectedEdge(0, 3, 20); // camino directo, no pasa por el ciclo

        FloydWarshallSolver solver = new FloydWarshallSolver();
        MaxChurunResult result = solver.solveAllPairs(graph)[0][3];

        assertTrue(result.status() == MaxChurunResult.Status.FINITE);
        assertEquals(20, result.value());
    }
}