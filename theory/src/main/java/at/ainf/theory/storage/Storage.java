package at.ainf.theory.storage;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: kostya
 * Date: 20.04.11
 * Time: 15:57
 * To change this template use File | Settings | File Templates.
 */
public interface Storage<T extends AxiomSet<Id>, Id> {

    boolean addConflict(T conflict);

    boolean removeConflictSet(T cs);


    boolean addHittingSet(T hittingSet);

    boolean removeHittingSet(T hittingSet);


    void invalidateHittingSet(T hittingSet);

    void resetStorage();


    Set<T> getConflicts();

    Set<T> getDiagnoses();

    Set<T> getHittingSets();








}
