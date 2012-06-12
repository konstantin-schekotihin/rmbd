package at.ainf.diagnosis.tree.searchstrategy;

import at.ainf.diagnosis.tree.CostNode;
import at.ainf.diagnosis.tree.CostsEstimator;
import at.ainf.diagnosis.tree.Node;
import at.ainf.diagnosis.tree.TreeSearch;
import at.ainf.theory.storage.AxiomSet;

import java.util.Collection;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: pfleiss
 * Date: 11.06.12
 * Time: 15:41
 * To change this template use File | Settings | File Templates.
 */
public class UniformCostSearchStrategy<Id> implements SearchStrategy<Id> {

    private PriorityQueue<Node<Id>> opensNodes = new PriorityQueue<Node<Id>>();



    public Node<Id> createRootNode(Set<Id> conflict, CostsEstimator<Id> costsEstimator, Collection<Id> act) {
        CostNode<Id> node = new CostNode<Id>(conflict);
        node.setCostsEstimator(costsEstimator);
        node.setNodePathCosts(node.getRootNodeCosts(act));
        return node;
    }


    public void expand(Node<Id> node) {
        addNodes(node.expandNode());
    }



    public double getConflictMeasure(Set<Id> conflict, CostsEstimator<Id> costsEstimator) {
        return costsEstimator.getAxiomSetCosts(conflict);
    }

    public double getDiagnosisMeasure(Node<Id> node) {
        return ((CostNode<Id>) node).getNodePathCosts();
    }

    public void finalizeSearch(TreeSearch<AxiomSet<Id>, Id> search) {
        search.getTheory().doBayesUpdate(search.getStorage().getDiagnoses());
        search.getStorage().normalizeValidHittingSets();
    }

    public Node<Id> getNode() {
        // gets the first open node of the List
        return popOpenNodes();
    }

    public void addNodes(List<Node<Id>> nodeList) {

        for (Node<Id> node : nodeList) {
            pushOpenNode(node);
        }
        //Collections.sort(getOpenNodes(), new NodeComparator());
    }

    public Collection<Node<Id>> getOpenNodes() {
        return this.opensNodes;
    }

    public Node<Id> popOpenNodes() {
        return this.opensNodes.poll();
    }

    public void pushOpenNode(Node<Id> idNode) {
        this.opensNodes.add(idNode);
    }

}