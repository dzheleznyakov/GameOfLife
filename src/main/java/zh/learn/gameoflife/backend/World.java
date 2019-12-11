package zh.learn.gameoflife.backend;

import com.google.common.collect.ImmutableSet;

import java.util.Collection;
import java.util.Set;

public class World {
    private final Set<Cell> state;

    public World(Collection<Cell> aliveCells) {
        this.state = ImmutableSet.copyOf(aliveCells);
    }

    public World getNextWorld() {
        Set<Cell> nextState = state.stream()
                .map(Cell::getNeighbours)
                .flatMap(Set::stream)
                .filter(cell -> state.contains(cell) ? shouldSurvive(cell) : shouldComeAlive(cell))
                .collect(ImmutableSet.toImmutableSet());

        return new World(nextState);
    }

    private boolean shouldSurvive(Cell cell) {
        long aliveNeighboursCount = cell.getNeighbours()
                .stream()
                .filter(state::contains)
                .count();
        return aliveNeighboursCount == 2 || aliveNeighboursCount == 3;
    }

    private boolean shouldComeAlive(Cell cell) {
        long aliveNeighboursCount = cell.getNeighbours()
                .stream()
                .filter(state::contains)
                .count();
        return aliveNeighboursCount == 3;
    }

    public Set<Cell> getState() {
        return state;
    }
}
