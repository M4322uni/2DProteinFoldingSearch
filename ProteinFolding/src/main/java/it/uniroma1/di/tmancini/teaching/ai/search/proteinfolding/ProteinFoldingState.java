package it.uniroma1.di.tmancini.teaching.ai.search.proteinfolding;

import it.uniroma1.di.tmancini.teaching.ai.search.*;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import it.uniroma1.di.tmancini.teaching.ai.search.proteinfolding.ProteinFoldingAction.Direction;
import static java.lang.Math.*;

public class ProteinFoldingState extends State {

    private final int x, y, stage;
    private final String sequence;
    private final char[][] configuration;

    public ProteinFoldingState(Problem p, String sequence, int x, int y) {
        super(p);
        this.x = x;
        this.y = y;
        stage = 0;
        this.sequence = sequence;
        int sequenceLength = sequence.length();
        configuration = new char[2*sequenceLength-1][2*sequenceLength-1];
        for (int i = 0; i < 2*sequenceLength-1; i++)
            for (int j = 0; j < 2*sequenceLength-1; j++)
                    configuration[i][j] = '#';
        setConfiguration(0, 0, sequence.charAt(0));
    }

    public ProteinFoldingState(Problem p, ProteinFoldingState old, int x, int y) {
        super(p);
        this.x = x;
        this.y = y;
        stage = old.stage+1;
        this.sequence = old.sequence;
        configuration = old.configuration.clone();
        setConfiguration(y, x, sequence.charAt(stage));
    }

    char getConfiguration(int y, int x) {
        int length = sequence.length();
        return configuration[y+length-1][x+length-1];
    }

    void setConfiguration(int y, int x, char val) {
        int length = sequence.length();
        configuration[y+length-1][x+length-1] = val;
    }

    private int countCost(Direction move) {
        int cost = 1, length = sequence.length(), nX, nY;
        switch (move) {
            case UP:
                nX = x; nY = y-1; break;
            case DOWN:
                nX = x; nY = y+1; break;
            case LEFT:
                nX = x-1; nY = y; break;
            case RIGHT:
                nX = x+1; nY = y; break;
            default:
                System.err.println("Warning: unexpected direction passed to \"countCost\" method");
                return -1;
        }
        if (-length+1 <= nY && nY <= length-1
                && -length+1 <= nX && nX <= length-1
                && getConfiguration(nY, nX) == '#') {
            for (int i = max(nY-1, -length+1); i <= min(nY+1, length-1); i++) {
                for (int j = max(nX-1, -length+1); j <= min(nX+1, length-1); j++) {
                    if ((i == nY && j == nX) || (i == y && j == x)) continue;
                    if (sequence.charAt(stage+1) != getConfiguration(i, j)) cost++;
                }
            }
            return cost;
        }
        return -1;
    }

    @Override
    public Collection<? extends Action> executableActions() {
        LinkedList<ProteinFoldingAction> ret = new LinkedList<>();
        int length = sequence.length(), cost;
        for (Direction val : Direction.values()) {
            cost = countCost(val);
            if (cost != -1)
                ret.add(new ProteinFoldingAction(val, cost));
        }
        return ret;
    }

    //TODO
    @Override
    public State resultingState(Action a) {
        if (!(a instanceof ProteinFoldingAction)) {
            System.err.println("Warning: unexpected action type");
            return null;
        }
        ProteinFoldingAction cast = (ProteinFoldingAction) a;
        return switch(cast.getDirection()) {
            case UP -> new ProteinFoldingState(getProblem(), this, x, y-1);
            case DOWN ->new ProteinFoldingState(getProblem(), this, x, y+1);
            case LEFT -> new ProteinFoldingState(getProblem(), this, x-1, y);
            case RIGHT -> new ProteinFoldingState(getProblem(), this, x+1, y);
        };
    }

    @Override
    public boolean isGoal() {
        return stage == sequence.length()-1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProteinFoldingState comp = (ProteinFoldingState) o;
        return x == comp.x
                && y == comp.y
                && stage == comp.stage
                && sequence.equals(comp.sequence)
                && Arrays.deepEquals(configuration, comp.configuration); //!!!
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = hash * 31 + ((Integer) x).hashCode();
        hash = hash * 31 + ((Integer) y).hashCode();
        hash = hash * 31 + ((Integer) stage).hashCode();
        hash = hash * 31 + (sequence == null ? 0 : sequence.hashCode());
        hash = hash * 31 + (configuration == null ? 0 : Arrays.deepHashCode(configuration));
        return hash;
    }

    //TODO
    @Override
    public String toString() {
        StringBuilder construct = new StringBuilder();
        for (int i = 0; i < configuration.length; i++) {
            construct.append("|");
            for (int j = 0; j < configuration.length; j++) {
                switch(getConfiguration(i, j)) {
                    case '#':
                        construct.append("   |");
                        break;
                    case 'H':
                        construct.append(" H |");
                        break;
                    case 'P':
                        construct.append(" P |");
                        break;
                    default:
                        construct.append("Warning: unknown molecule");
                        break;
                }
            }
            construct.append("\n");
        }
        return construct.toString();
    }

    //TODO
    @Override
    public String toStringWithPrefix(String prefix) {
        throw new UnsupportedOperationException("Needs to be implemented by non-abstract subclasses");
    }
}
