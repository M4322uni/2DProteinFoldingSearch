package it.uniroma1.di.tmancini.teaching.ai.search.proteinfolding;

import it.uniroma1.di.tmancini.teaching.ai.search.*;
import picocli.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;

public class ProteinFolding extends Problem implements Callable<Integer> {

    @CommandLine.Option(names={"--sequence"}, required = true,
            description = "The chain of ammino-acids, as a string of h and p characters.")
    private String sequence;

    @CommandLine.Option(names={"--algo", "--algorithm"}, required = true,
            description = "The algorithm to be used, to be chosen between DFS, BFS, MIN_COST,"
            + " ITERATIVE_DEEPENING, BEST_FIRST, A_STAR.")
    private String algo;

    @CommandLine.Option(names={"-x"}, defaultValue = "0",
            description = "The starting x-coordinate of the chain, as an integer in the range" +
                "[−(n − 1), (n − 1)], where n is the length of the chain.")
    private int x;

    @CommandLine.Option(names={"-y"}, defaultValue = "0",
            description = "The starting y-coordinate of the chain, as an integer in the range" +
                    "[−(n − 1), (n − 1)], where n is the length of the chain.")
    private int y;

    @CommandLine.Option(names={"-v", "--verbosity"}, defaultValue = "0",
            description = "The verbosity level of the output, as a positive integer number. " +
                    "0 corresponds to 'standard', while higher numbers correspond to higher verbosity.")
    private int vlevel;

    public ProteinFolding() {
        super("ProteinFolding");
    }

    private void checkInput() {
        Set<String> domain = Set.of("DFS", "BFS", "MIN_COST",
                "ITERATIVE_DEEPENING", "BEST_FIRST", "A_STAR");
        if (!domain.contains(algo.toUpperCase())) {
            throw new IllegalArgumentException(
                    "[ERROR] Unknown algorithm.");
        }
        if (this.x <= 0) {
            throw new IllegalArgumentException(
                    "[ERROR] X-coordinate out of range.");
        }
        if (this.y < 0) {
            throw new IllegalArgumentException(
                    "[ERROR] Y-coordinate out of range.");
        }
        if (this.vlevel < 0) {
            throw new IllegalArgumentException(
                    "[ERROR] Negative verbosity levels are not allowed."
                    + " Please a non-negative value for option '--verbosity'");
        }
    }

    @Override
    public Integer call() {
        try {
            checkInput();



            return 0;
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
            return 1;
        }
    }
}
