package algorithms.mission1;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BfsSolverTest {

    @Test
    void findsShortestPathOnSampleGrid() {
        Board board = new Board(10, 10);
        int[][] bombs = {{0,2},{1,2},{2,2},{2,9},{3,1},{3,7},{5,3},{5,6},{5,9},{6,0},{6,1},{6,2},{6,7},{7,0},{7,3},{7,8},{8,7},{8,9},{9,2},{9,3},{9,4}};
        for (int[] b : bombs) board.setBomb(b[0], b[1]);

        BfsSolver solver = new BfsSolver();
        PathResult result = solver.solve(board, new Point(0, 0), new Point(9, 9));

        assertTrue(result.reachable());
        assertEquals(18, result.moves());
    }

    @Test
    void returnsZeroMovesWhenStartEqualsDestination() {
        Board board = new Board(3, 3);
        BfsSolver solver = new BfsSolver();

        PathResult result = solver.solve(board, new Point(1, 1), new Point(1, 1));

        assertTrue(result.reachable());
        assertEquals(0, result.moves());
    }

    @Test
    void returnsUnreachableWhenDestinationIsSurroundedByBombs() {
        Board board = new Board(3, 3);
        board.setBomb(1, 2);
        board.setBomb(2, 1);
        BfsSolver solver = new BfsSolver();

        PathResult result = solver.solve(board, new Point(0, 0), new Point(2, 2));

        assertFalse(result.reachable());
    }

    @Test
    void returnsUnreachableWhenStartCellIsABomb() {
        Board board = new Board(3, 3);
        board.setBomb(0, 0);
        BfsSolver solver = new BfsSolver();

        PathResult result = solver.solve(board, new Point(0, 0), new Point(2, 2));

        assertFalse(result.reachable());
    }

    @Test
    void findsOptimalPathOnEmptyThreeByThreeGrid() {
        Board board = new Board(3, 3);
        BfsSolver solver = new BfsSolver();

        PathResult result = solver.solve(board, new Point(0, 0), new Point(2, 2));

        assertTrue(result.reachable());
        assertEquals(4, result.moves());
    }
}