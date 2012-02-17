package at.ainf.diagnosis.debugger;

import at.ainf.diagnosis.partitioning.ScoringFunction;
import at.ainf.theory.model.ITheory;

import at.ainf.theory.storage.Partition;
//import at.ainf.querygen.partitioning.ScoringFunction;
import at.ainf.theory.storage.AxiomSet;


import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: pfleiss
 * Date: 18.11.11
 * Time: 08:36
 * To change this template use File | Settings | File Templates.
 */
public interface QueryDebugger<Id> {

    public Set<? extends AxiomSet<Id>> getConflictSets();

    public Set<? extends AxiomSet<Id>> getHittingSets();

    public Set<? extends AxiomSet<Id>> getValidHittingSets();

    public ITheory<Id> getTheory();

    public void init();

    Partition<Id> getQuery(ScoringFunction<Id> f, boolean minimizeQuery, double acceptanceThreshold);

    void updateMaxHittingSets(int number);

    public boolean debug();

    void reset();

    public void addQueryDebuggerListener(QueryDebuggerListener<Id> listener);

    public void removeQueryDebuggerListener(QueryDebuggerListener<Id> listener);

    public boolean resume();

}