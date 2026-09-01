package algorithms.mission2;

import io.mission2.Mission2Parser;
import org.junit.jupiter.api.Test;
import samples.SampleInputs;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DijkstraSolverTest {

    @Test
    void matchesTheThreeSampleCases() {
        Mission2Parser parser = new Mission2Parser();
        List<Mission2Parser.TestCase> cases = parser.parse(SampleInputs.MISSION_2);
        DijkstraSolver solver = new DijkstraSolver();

        DijkstraResult r1 = solver.solve(cases.get(0).graph(), cases.get(0).source(), cases.get(0).destination());
        assertTrue(r1.reachable());
        assertEquals(100, r1.cost());

        DijkstraResult r2 = solver.solve(cases.get(1).graph(), cases.get(1).source(), cases.get(1).destination());
        assertTrue(r2.reachable());
        assertEquals(150, r2.cost());

        DijkstraResult r3 = solver.solve(cases.get(2).graph(), cases.get(2).source(), cases.get(2).destination());
        assertFalse(r3.reachable());
    }

    @Test
    void returnsZeroCostWhenSourceEqualsDestination() {
        WeightedGraph graph = new WeightedGraph(3);
        DijkstraSolver solver = new DijkstraSolver();

        DijkstraResult result = solver.solve(graph, 0, 0);

        assertTrue(result.reachable());
        assertEquals(0, result.cost());
    }

    @Test
    void keepsTheCheaperOfTwoDuplicateEdges() {
        WeightedGraph graph = new WeightedGraph(2);
        graph.addBidirectionalEdge(0, 1, 100);
        graph.addBidirectionalEdge(0, 1, 50);
        DijkstraSolver solver = new DijkstraSolver();

        DijkstraResult result = solver.solve(graph, 0, 1);

        assertEquals(50, result.cost());
    }

    @Test
    void handlesSelfLoopWithoutCrashing() {
        WeightedGraph graph = new WeightedGraph(3);
        graph.addBidirectionalEdge(0, 1, 10);
        graph.addBidirectionalEdge(1, 2, 5);
        graph.addBidirectionalEdge(1, 1, 999); // self-loop
        DijkstraSolver solver = new DijkstraSolver();

        DijkstraResult result = solver.solve(graph, 0, 2);

        assertTrue(result.reachable());
        assertEquals(15, result.cost());
    }

    @Test
    void returnsUnreachableForIsolatedNode() {
        WeightedGraph graph = new WeightedGraph(3);
        graph.addBidirectionalEdge(0, 1, 5);
        DijkstraSolver solver = new DijkstraSolver();

        DijkstraResult result = solver.solve(graph, 0, 2);

        assertFalse(result.reachable());
    }
}