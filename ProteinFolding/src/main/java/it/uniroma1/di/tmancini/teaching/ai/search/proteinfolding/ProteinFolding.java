package it.uniroma1.di.tmancini.teaching.ai.search.proteinfolding;

import it.uniroma1.di.tmancini.teaching.ai.search.*;
import jdk.jshell.spi.ExecutionControl;
import picocli.*;

import java.util.*;
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
        algo = algo.toUpperCase();
        int n = sequence.length();
        if (!domain.contains(algo)) {
            throw new IllegalArgumentException(
                    "[ERROR] Unknown algorithm.");
        }
        if (-n+1 > this.x || this.x > n-1) {
            throw new IllegalArgumentException(
                    "[ERROR] X-coordinate out of range.");
        }
        if (-n+1 > this.y || this.y > n-1 ) {
            throw new IllegalArgumentException(
                    "[ERROR] Y-coordinate out of range.");
        }
        if (this.vlevel < 0) {
            throw new IllegalArgumentException(
                    "[ERROR] Negative verbosity levels are not allowed."
                    + " Please a non-negative value for option '--verbosity'");
        }
        sequence = sequence.toUpperCase();
        if (sequence.chars().anyMatch(x -> x != 'H' && x != 'P')) {
            throw new IllegalArgumentException(
                    "[ERROR] Invalid ammino-acids chain");
        }
    }

    @Override
    public Integer call() {
        try {
            checkInput();

            SearchStateExplorer explorer = switch (algo) {
                case "DFS" -> new DFSExplorer(this);
                case "BFS" -> new BFSExplorer(this);
                case "MIN_COST" -> new MinCostExplorer(this);
                case "ITERATIVE_DEEPENING" -> throw new IllegalArgumentException("Not yet implemented");
                case "BEST_FIRST" -> throw new IllegalArgumentException("Not yet implemented");
                case "A_STAR" -> throw new IllegalArgumentException("Not yet implemented");
                default -> throw new RuntimeException("Unexpected branch");
            };

            explorer.setVerbosity(SearchStateExplorer.VERBOSITY.values()[vlevel]);

            ProteinFoldingState initialState = new ProteinFoldingState(this, sequence, x, y);
            long startStamp, endStamp;
            List<Action> result;

            System.out.println("Starting search");
            startStamp = System.currentTimeMillis();
            result = explorer.run( initialState );
            endStamp = System.currentTimeMillis();
            System.out.println("Search ended");
            explorer.outputStats();
            System.out.println("Actions: " + Arrays.toString(result.toArray()));
            return 0;
        }
        catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new ProteinFolding()).execute(args);
        System.exit(exitCode);
    }
}
