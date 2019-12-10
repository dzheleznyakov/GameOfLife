package zh.learn.gameoflife.backend;

import com.google.common.collect.ImmutableSet;
import org.junit.Test;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiPredicate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GridTest {
    @Test
    public void aloneCellDies() {
        Cell dyingCell = new Cell(0, 0);
        testCellWillBeDead(
                ImmutableSet.of(dyingCell),
                dyingCell);
    }

    @Test
    public void cellWithOneNeighbourDies() {
        Cell dyingCell = new Cell(0, 0);
        testCellWillBeDead(
                ImmutableSet.of(dyingCell, new Cell(0, 1)),
                dyingCell);
    }

    @Test
    public void cellWithTwoNeighboursSurvive() {
        Cell survivorCell = new Cell(0, 1);
        testCellWillBeAlive(
                ImmutableSet.of(new Cell(0, 0), survivorCell, new Cell(0, 2)),
                survivorCell);
    }

    @Test
    public void cellWithThreeNeighboursSurvive() {
        Cell survivorCell = new Cell(0, 0);
        testCellWillBeAlive(
                ImmutableSet.of(new Cell(0, 1), new Cell(1, 0), new Cell(0, -1), survivorCell),
                survivorCell);
    }

    @Test
    public void cellWithMoreThanTreeNeighboursDies() {
        Cell dyingCell = new Cell(0, 0);
        testCellWillBeDead(
                ImmutableSet.of(new Cell(0, 1), new Cell(1, 0), new Cell(0, -1), new Cell(-1, 0), dyingCell),
                dyingCell);
    }

    @Test
    public void deadCellWithNoNeighboursRemainsDead() {
        Cell deadCell = new Cell(100, 100);
        testCellWillBeDead(
                ImmutableSet.of(new Cell(0, 0)),
                deadCell);
    }

    @Test
    public void deadCellWithOneNeighbourRemainsDead() {
        Cell deadCell = new Cell(0, 1);
        testCellWillBeDead(
                ImmutableSet.of(new Cell(0, 0)),
                deadCell);
    }

    @Test
    public void deadCellWithTwoNeighboursRemainsDead() {
        Cell deadCell = new Cell(0, 0);
        testCellWillBeDead(
                ImmutableSet.of(new Cell(0, 1), new Cell(1, 0)),
                deadCell);
    }

    @Test
    public void deadCellWithThreeNeighboursComesAlive() {
        Cell deadCell = new Cell(0, 0);
        testCellWillBeAlive(
                ImmutableSet.of(new Cell(0, 1), new Cell(1, 0), new Cell(-1, 0)),
                deadCell);
    }

    @Test
    public void deadCellWithMoreThanThreeNeighboursRemainsDead() {
        Cell deadCell = new Cell(0, 0);
        testCellWillBeDead(
                ImmutableSet.of(new Cell(0, 1), new Cell(1, 0), new Cell(-1, 0), new Cell(0, -1)),
                deadCell);
    }

    private void testCellWillBeAlive(Set<Cell> initialState, Cell cell) {
        testCellInNextState(initialState, cell, Set::contains);
    }

    private void testCellWillBeDead(Set<Cell> initialState, Cell cell) {
        testCellInNextState(initialState, cell, (set, c) -> !set.contains(c));
    }

    private void testCellInNextState(Set<Cell> initialState, Cell cell, BiPredicate<Set<Cell>, Cell> shouldSurvive) {
        Grid grid = new Grid(initialState);

        Grid nextGrid = grid.getNextGrid();
        Set<Cell> nextState = nextGrid.getState();

        assertTrue(shouldSurvive.test(nextState, cell));
    }

    @Test
    public void testNextState1() {
        Set<Cell> initialState = parseState("-0-");
        testNextState(initialState,
                ImmutableSet.of());
    }

    @Test
    public void testNextState2() {
        Set<Cell> initialState = parseState("00-");
        testNextState(initialState,
                ImmutableSet.of());
    }

    @Test
    public void testNextState3() {
        Set<Cell> initialState = parseState(
                "---\n" +
                "000\n" +
                "---\n");
        Set<Cell> expectedState = parseState(
                "-0-\n" +
                "-0-\n" +
                "-0-\n");
        testNextState(initialState, expectedState);
    }

    @Test
    public void testNextState4() {
        Set<Cell> initialState = parseState(
                "----\n" +
                "-00-\n" +
                "-0--\n" +
                "----\n");
        Set<Cell> expectedState = parseState(
                "----\n" +
                "-00-\n" +
                "-00-\n" +
                "----\n");
        testNextState(initialState, expectedState);
    }

    @Test
    public void testNextState5() {
        Set<Cell> initialState = parseState(
                "----\n" +
                "-00-\n" +
                "-00-\n" +
                "----\n");
        Set<Cell> expectedState = parseState(
                "----\n" +
                "-00-\n" +
                "-00-\n" +
                "----\n");
        testNextState(initialState, expectedState);
    }

    private Set<Cell> parseState(String stateString) {
        Set<Cell> state = new HashSet<>();
        String[] rows = stateString.split("\n");
        for (int y = 0; y < rows.length; y++) {
            String row = rows[y];
            String[] elements = row.split("");
            for (int x = 0; x < elements.length; x++) {
                String element = elements[x];
                if (Objects.equals(element, "0"))
                    state.add(new Cell(x, -y));
            }
        }
        return state;
    }

    private void testNextState(Set<Cell> initialState, Set<Cell> expectedNextState) {
        Grid grid = new Grid(initialState);

        Grid nextGrid = grid.getNextGrid();
        Set<Cell> actualNextState = nextGrid.getState();

        assertEquals(expectedNextState, actualNextState);
    }
}
