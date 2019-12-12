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
        ImmutableSet.Builder<Cell> builder = ImmutableSet.builder();
        for (int i = -1; i <= 1 ; ++i) {
            for (int j = -1; j <= 1; ++j) {
                if (i != 0 || j != 0) {
                    builder.add(new Cell(x + i, y + j));
                }
            }
        }
        return builder.build();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
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
