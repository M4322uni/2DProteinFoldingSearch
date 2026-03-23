package it.uniroma1.di.tmancini.teaching.ai.search.proteinfolding;
import it.uniroma1.di.tmancini.teaching.ai.search.*;

import java.util.*;
import java.io.*;

public class IterativeDeepeningDFSExplorer extends SearchStateExplorer {

    private boolean isRunning;
    private long durationMsec;
    private final long startTime;
    private static Map<Integer, String> prefixes;

    public IterativeDeepeningDFSExplorer(Problem p, String outFileName) {
        super(p, null, outFileName);
        super.setFrontier( new LIFOFrontier() );
        isRunning = false;
        durationMsec = -1;
        startTime = (new java.util.Date()).getTime();
        prefixes = new HashMap<>();
    }

    public IterativeDeepeningDFSExplorer(Problem p) {
        this(p, null);
    }

    private long getDurationMsec() {
        if (isRunning || durationMsec < 0) {
            durationMsec = (new java.util.Date()).getTime() - startTime;
        }
        return durationMsec;
    }

    @Override
    public void outputStats() {
        System.out.println("Statistics:\n - nbIterations = " + nbIter);
        System.out.println(" - maxFrontierSize = " + maxFrontierSize);
        System.out.println(" - duration (sec) = " + getDurationMsec()/1000.0);
    }

    @Override
    public List<Action> run(State initialState) {
        int maxDepth = 1;
        List<Action> ret;
        while ((ret = runOne(initialState, maxDepth)) == null) maxDepth++;
        return ret;
    }

    private List<Action> runOne(State initialState, int maxDepth) {
        isRunning = true;
        frontier.clear();
        frontier.enqueue(  new SearchNode(initialState) );

        nbIter = 0;
        maxFrontierSize = 0;

        boolean done = false;
        SearchNode result = null;
        while(!done) {
            if (frontier.isEmpty()) {
                done = true;
                break;
            }

            nbIter++;
            if (maxFrontierSize < frontier.size()) {
                maxFrontierSize = frontier.size();
            }

            if (verbosity.ordinal() >= VERBOSITY.statsonly.ordinal() && nbIter % 100 == 0) {
                outputStats();
            }


            SearchNode currNode = frontier.dequeue();
            State currState = currNode.getState();
            int currDepth = currNode.getDepth();

            outputString(VERBOSITY.low, currDepth, " X");
            outputString(VERBOSITY.high, currDepth, "Current node:");
            outputNode(VERBOSITY.high, currDepth, currNode);

            if (currState.isGoal()) {
                result = currNode;
                done = true;
            } else {
                if (currDepth < maxDepth) {
                    for(Action a : currState.executableActions()) {
                        SearchNode childNode = new SearchNode(currNode, a);
                        outputString(VERBOSITY.high, currDepth, "Enqueueing child node:");
                        outputNode(VERBOSITY.high, currDepth, childNode);
                        boolean enqueued = frontier.enqueue(childNode);
                        if (enqueued) {
                            outputString(VERBOSITY.high, currDepth, "--> enqueued");
                        } else {
                            outputString(VERBOSITY.high, currDepth, "--> already in frontier with lower cost --> no action");
                        }
                    }
                } else {
                    outputString(VERBOSITY.low, currDepth, "max depth reached, cutting tree");
                }
            }
        }

        isRunning = false;
        if (result == null) return null;

        LinkedList<Action> actions = new LinkedList<>();
        while (result != null ) {
            Action a = result.getAction();
            if (a != null) {
                actions.addFirst( result.getAction() );
            }
            result = result.getParent();
        }


        return actions;
    }

    private static String outputPrefix(int depth) {
        String s = prefixes.get(depth);
        if (s != null) return s;

        s = "      ".repeat(Math.max(0, depth));
        prefixes.put(depth, s);
        return s;
    }

    private void outputNode(VERBOSITY minVerbosity, int depth, SearchNode o) {
        if (verbosity.ordinal() < minVerbosity.ordinal()) return;
        System.out.println(o.toStringWithPrefix(outputPrefix(depth)));
    }


    private void outputString(VERBOSITY minVerbosity, int depth, String msg) {
        if (verbosity.ordinal() < minVerbosity.ordinal()) return;
        System.out.print(outputPrefix(depth));
        System.out.println(msg);
    }

} //:~