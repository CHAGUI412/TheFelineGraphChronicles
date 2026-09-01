package algorithms.mission4;

import io.mission4.Mission4Parser;
import org.junit.jupiter.api.Test;
import samples.SampleInputs;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KruskalSolverTest {

    @Test
    void matchesTheSampleCase() {
        Mission4Parser parser = new Mission4Parser();
        List<Mission4Parser.TestCase> cases = parser.parse(SampleInputs.MISSION_4);
        Mission4Parser.TestCase testCase = cases.get(0);

        KruskalSolver solver = new KruskalSolver();
        MstResult result = solver.solve(testCase.nodeCount(), testCase.candidateEdges());

        assertTrue(result.connected());
        assertEquals(55, result.totalCost());
    }

    @Test
    void returnsImpossibleWhenGraphIsDisconnected() {
        List<Edge> edges = List.of(new Edge(0, 1, 5), new Edge(2, 3, 5));
        KruskalSolver solver = new KruskalSolver();

        MstResult result = solver.solve(4, edges);

        assertFalse(result.connected());
    }

    @Test
    void ignoresSelfLoopsWithoutCrashing() {
        List<Edge> edges = List.of(
                new Edge(0, 0, 999), // self-loop
                new Edge(0, 1, 5),
                new Edge(1, 2, 5)
        );
        KruskalSolver solver = new KruskalSolver();

        MstResult result = solver.solve(3, edges);

        assertTrue(result.connected());
        assertEquals(10, result.totalCost());
    }

    @Test
    void keepsTheCheaperOfDuplicateEdges() {
        List<Edge> edges = List.of(new Edge(0, 1, 100), new Edge(0, 1, 30));
        KruskalSolver solver = new KruskalSolver();

        MstResult result = solver.solve(2, edges);

        assertEquals(30, result.totalCost());
    }

    @Test
    void returnsZeroCostForSingleNode() {
        KruskalSolver solver = new KruskalSolver();

        MstResult result = solver.solve(1, List.of());

        assertTrue(result.connected());
        assertEquals(0, result.totalCost());
    }
}