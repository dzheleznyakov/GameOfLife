package zh.learn.gameoflife.backend;

import com.google.common.collect.ImmutableSet;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;

public class CellTest {
    @Test
    public void testGetNeighbours() {
        Cell cell = new Cell(1, 1);

        Set<Cell> expectedNeighbours = ImmutableSet.of(
                new Cell(0, 0), new Cell(1, 0), new Cell(2, 0),
                new Cell(0, 1), new Cell(2, 1),
                new Cell(0, 2), new Cell(1, 2), new Cell(2, 2));
        Set<Cell> actualNeighbours = cell.getNeighbours();

        assertEquals(expectedNeighbours, actualNeighbours);
    }
}
