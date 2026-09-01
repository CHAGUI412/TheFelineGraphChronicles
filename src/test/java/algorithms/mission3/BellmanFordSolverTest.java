package algorithms.mission3;

import io.mission3.Mission3Parser;
import org.junit.jupiter.api.Test;
import samples.SampleInputs;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BellmanFordSolverTest {

    @Test
    void matchesTheThreeSampleCasesAndAgreesWithFloydWarshall() {
        Mission3Parser parser = new Mission3Parser();
        List<Mission3Parser.TestCase> cases = parser.parse(SampleInputs.MISSION_3);
        FloydWarshallSolver floydWarshall = new FloydWarshallSolver();
        BellmanFordSolver bellmanFord = new BellmanFordSolver();

        for (Mission3Parser.TestCase testCase : cases) {
            MaxChurunResult fwResult = floydWarshall.solveAllPairs(testCase.graph())[testCase.source()][testCase.destination()];
            MaxChurunResult bfResult = bellmanFord.solveFromSource(testCase.graph(), testCase.source(), testCase.destination());

            assertTrue(fwResult.status() == bfResult.status());
            if (fwResult.status() == MaxChurunResult.Status.FINITE) {
                assertEquals(fwResult.value(), bfResult.value());
            }
        }
    }

    @Test
    void returnsZeroWhenSourceEqualsDestination() {
        DirectedWeightedGraph graph = new DirectedWeightedGraph(3);
        BellmanFordSolver solver = new BellmanFordSolver();

        MaxChurunResult result = solver.solveFromSource(graph, 1, 1);

        assertTrue(result.status() == MaxChurunResult.Status.FINITE);
        assertEquals(0, result.value());
    }

    @Test
    void keepsTheMaximumOfDuplicateEdges() {
        DirectedWeightedGraph graph = new DirectedWeightedGraph(2);
        graph.addDirectedEdge(0, 1, 5);
        graph.addDirectedEdge(0, 1, 12);
        BellmanFordSolver solver = new BellmanFordSolver();

        MaxChurunResult result = solver.solveFromSource(graph, 0, 1);

        assertEquals(12, result.value());
    }

    @Test
    void returnsUnreachableWhenNoPathExists() {
        DirectedWeightedGraph graph = new DirectedWeightedGraph(2);
        BellmanFordSolver solver = new BellmanFordSolver();

        MaxChurunResult result = solver.solveFromSource(graph, 0, 1);

        assertTrue(result.status() == MaxChurunResult.Status.UNREACHABLE);
    }

    @Test
    void doesNotMarkUnboundedWhenPositiveCycleCannotReachDestination() {
        DirectedWeightedGraph graph = new DirectedWeightedGraph(4);
        graph.addDirectedEdge(0, 1, 5);
        graph.addDirectedEdge(1, 2, 10);
        graph.addDirectedEdge(2, 1, -3);
        graph.addDirectedEdge(0, 3, 20);

        BellmanFordSolver solver = new BellmanFordSolver();
        MaxChurunResult result = solver.solveFromSource(graph, 0, 3);

        assertTrue(result.status() == MaxChurunResult.Status.FINITE);
        assertEquals(20, result.value());
    }
}