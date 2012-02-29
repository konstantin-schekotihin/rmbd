package at.ainf.diagnosis.tree;


import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: kostya
 * Date: 13.06.11
 * Time: 17:27
 * To change this template use File | Settings | File Templates.
 */
public interface CostsEstimator<Id> {

    double getAxiomSetCosts(Set<Id> labelSet);

    double getAxiomCosts(Id label);


}