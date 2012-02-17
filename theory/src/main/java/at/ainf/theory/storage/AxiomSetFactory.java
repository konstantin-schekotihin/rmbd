package at.ainf.theory.storage;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: pfleiss
 * Date: 19.01.12
 * Time: 15:51
 * To change this template use File | Settings | File Templates.
 */
public class AxiomSetFactory {
    
    private static int hsCnt = 0;
    
    private static int csCnt = 0;


    public static <Id> AxiomSet<Id> createHittingSet(double measure, Set<Id> hittingSet, Set<Id> entailments) {
        String name = AxiomSet.TypeOfSet.HITTING_SET.toString() + hsCnt++;
        return new HittingSet<Id>(name, measure, hittingSet, entailments);
    }

    public static <Id> AxiomSet<Id> createConflictSet(double measure, Set<Id> hittingSet, Set<Id> entailments) {
        String name = AxiomSet.TypeOfSet.CONFLICT_SET.toString() + csCnt++ ;
        return new ConflictSet<Id>(name, measure, hittingSet, entailments);
    }

}