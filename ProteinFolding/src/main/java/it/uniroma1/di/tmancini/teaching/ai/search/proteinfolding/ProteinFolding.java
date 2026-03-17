package it.uniroma1.di.tmancini.teaching.ai.search.proteinfolding;

import it.uniroma1.di.tmancini.teaching.ai.search.*;
import java.util.concurrent.Callable;

public class ProteinFolding extends Problem implements Callable<Integer> {

    public ProteinFolding() {
        super("ProteinFolding");
    }

    @Override
    public Integer call() {
        return 0;
    }

}
