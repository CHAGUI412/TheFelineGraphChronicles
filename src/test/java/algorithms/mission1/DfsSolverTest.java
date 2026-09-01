package algorithms.mission1;

import io.mission1.Mission1Parser;
import org.junit.jupiter.api.Test;
import samples.SampleInputs;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DfsSolverTest {

    @Test
    void findsAValidButNonOptimalPathOnSampleGrid() {
        Mission1Parser parser = new Mission1Parser();
        List<Mission1Parser.TestCase> cases = parser.parse(SampleInputs.MISSION_1);
        Mission1Parser.TestCase testCase = cases.get(0);

        DfsSolver solver = new DfsSolver();
        PathResult result = solver.solve(testCase.board(), testCase.start(), testCase.destination());

        assertTrue(result.reachable());
        assertEquals(32, result.moves());
    }
}