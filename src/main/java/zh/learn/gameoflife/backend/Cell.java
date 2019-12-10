package zh.learn.gameoflife.backend;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;

import java.util.Set;

public class Cell {
    private final int x;
    private final int y;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Set<Cell> getNeighbours() {
        return ImmutableSet.of(
                new Cell(x - 1, y - 1), new Cell(x, y - 1), new Cell(x + 1, y - 1),
                new Cell(x - 1, y), new Cell(x + 1, y),
                new Cell(x - 1, y + 1), new Cell(x, y + 1), new Cell(x + 1, y + 1));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return x == cell.x &&
                y == cell.y;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(x, y);
    }

    @Override
    public String toString() {
        return "Cell(" + x + ", " + y + ")";
    }
}
