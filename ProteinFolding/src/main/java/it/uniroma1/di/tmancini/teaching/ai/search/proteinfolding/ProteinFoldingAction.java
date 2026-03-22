package it.uniroma1.di.tmancini.teaching.ai.search.proteinfolding;

import it.uniroma1.di.tmancini.teaching.ai.search.Action;

public class ProteinFoldingAction extends Action {

    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    private final Direction direction;
    private final int cost;

    public ProteinFoldingAction(Direction direction, int cost) {
        this.direction = direction;
        this.cost = cost;
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public double getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return direction.toString();
    }
}
