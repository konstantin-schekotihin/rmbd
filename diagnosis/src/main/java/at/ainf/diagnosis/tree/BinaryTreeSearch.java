package at.ainf.diagnosis.tree;

import at.ainf.diagnosis.model.InconsistentTheoryException;
import at.ainf.diagnosis.model.SolverException;
import at.ainf.diagnosis.storage.FormulaSet;
import at.ainf.diagnosis.tree.exceptions.NoConflictException;
import at.ainf.diagnosis.tree.splitstrategy.MostFrequentSplitStrategy;
import at.ainf.diagnosis.tree.splitstrategy.SplitStrategy;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: pfleiss
 * Date: 13.11.12
 * Time: 17:01
 * To change this template use File | Settings | File Templates.
 */
public class BinaryTreeSearch<T extends FormulaSet<Id>,Id> extends AbstractTreeSearch<T,Id> implements TreeSearch<T,Id> {


    private SplitStrategy<Id> splitStrategy = new MostFrequentSplitStrategy<Id>();


    @Override
    protected void pruneConflictSets(Node<Id> node, T conflictSet) throws SolverException, InconsistentTheoryException {

    }

    @Override
    protected void proveValidnessConflict(T conflictSet) throws SolverException {

    }

    public boolean proveValidnessDiagnosis(Set<Id> diagnosis) throws SolverException {

        if (getSearchable().getKnowledgeBase().hasTests())
            return getSearchable().testDiagnosis(diagnosis);

        return true;

    }

    /*
    public boolean pruneHittingSet(Node<Id> node) {
        if (node.isRoot()) return false;
        Collection<Id> pathLabels = node.getPathLabels();
        for (T diagnosis : getHittingSets()) {
            if (pathLabels.containsAll(diagnosis))
                return true;
                else if(diagnosis.containsAll(pathLabels))   {
                    this.hittingSets.remove(diagnosis);
                return false;
            }

        }
        return false;
    }  */

    public void createRoot() throws NoConflictException,
            SolverException, InconsistentTheoryException {
        // if there is already a root
        if (getRoot() != null) return;
        Set<Set<Id>> conflict = calculateConflict(null,null);

        //convert to linked HashSet
        LinkedHashSet conflictH = new LinkedHashSet(conflict);

        BHSTreeNode<Id> node = new BHSTreeNode<Id>(conflictH);
        node.setSplitStrategy(this.splitStrategy);
        node.setCostsEstimator(getCostsEstimator());

        setRoot(node);
    }

    public Set<Set<Id>> calculateNode(Node<Id> node,Set<Id> path) throws NoConflictException,SolverException,InconsistentTheoryException{
        if(node.getAxiomSets()==null || node.getAxiomSets().isEmpty())
            return calculateConflict(node,path);

        else return null;
    }

    public void updateTree(List<T> invalidHittingSets) throws SolverException, InconsistentTheoryException, NoConflictException {

        //construct Set of Elements that must be deleted
        Set<Id> delete = new LinkedHashSet<Id>();


        updateNode(((BHSTreeNode)getRoot()),delete);
    }

    public void updateNode(BHSTreeNode<Id> node,Set<Id> delete)  throws SolverException, InconsistentTheoryException, NoConflictException{

        for (Set<Id> ax : node.getNewConflicts()) {
            Set<Id> axioms = getSearcher().search(getSearchable(), ax).iterator().next();

            //identify invalid elements
            for(Id id:ax){
                if(!axioms.contains(id))
                    delete.add(id);
            }

        }
        Set<Node<Id>> removeNode=node.updateNode(delete);

        for(Node<Id> n:removeNode){
          removeChildren(n);
        }


        if(node.getChildren().isEmpty())
            getSearchStrategy().pushOpenNode(node);
        else
        for(Node<Id> child:node.getChildren()){
            updateNode((BHSTreeNode<Id>)child,delete);
        }

    }

    private void removeChildren(Node<Id> idNode) {
        if (idNode!=null && !getSearchStrategy().getOpenNodes().remove(idNode)) {
            for (Node<Id> node : idNode.getChildren()) {
                removeChildren(node);
            }
        }
    }


    @Override
    public Set<Set<Id>> calculateConflict(Node<Id> node,Set<Id> path) throws SolverException, NoConflictException, InconsistentTheoryException {



        Set<Set<Id>> quickConflict = super.calculateConflict(node,path);

       if (node != null) {
        Set<Node<Id>> deleteNodes= new LinkedHashSet<Node<Id>>();
        Set<Node<Id>> pushNodes= new LinkedHashSet<Node<Id>>();

           for(Set<Id> conflictS: quickConflict) {

                LinkedHashSet<Id> conflict = new LinkedHashSet<Id>(conflictS);

               getSearchStrategy().getOpenNodes().add(node);

               // for(HSTreeNode<Id> leaf: (Set<HSTreeNode>)((HSTreeNode)getRoot()).getLeaves()) {
                for(Node<Id> leaf: getSearchStrategy().getOpenNodes()) {

                    //Für MultiparentGraph: Statt intersects-Bedingung: !nonHittingPaths.isEmpty()
                    if(!leaf.isClosed() && !intersectsWith(conflict,leaf.getPathLabels().iterator().next().getPositivePath())) {
                   //nur für MultiParentBHs
                    // if(!leaf.isClosed()){

                    ((BHSTreeNode<Id>)leaf).addNewConflict(conflict);
                     List<Node<Id>> newNodes=  ((BHSTreeNode<Id>)leaf).expandNode(conflict);

                         if(!newNodes.isEmpty()){
                       deleteNodes.add(leaf);

                         for(Node<Id> newNode:newNodes)
                            pushNodes.add(newNode);
                     }
                    }


                    //Alte Sachen von SingleParentBHS
                       // ((BHSTreeNode<Id>)leaf).addNewConflict(conflict);
                        //SEHR UNSCHÖN später ausbessern
                       // if(((HSTreeNode<Id>)leaf).getConflicts()!=null)
                         //   ((HSTreeNode<Id>)leaf).getConflicts().add(((BHSTreeNode)leaf).updateConflict(conflict));
                        //else leaf.setAxiomSet((LinkedHashSet<Id>)((BHSTreeNode)leaf).updateConflict(conflict));
                    }
               /*
               Kann beides optimiert werden indem Arrays in jedem
               Durchlauf neu initialisiert werden um nicht
               jedesmal das ganze Array zu durchlaufen
                */
               for(Node<Id> pushNode :pushNodes){
                   getSearchStrategy().pushOpenNode(pushNode);
               }


               for(Node<Id> delete:deleteNodes){
                   getSearchStrategy().getOpenNodes().remove(delete);
               }
              // getSearchStrategy().getOpenNodes().remove(node);

                }



       }

        return quickConflict;
    }


    public void setSplitStrategy(SplitStrategy<Id> ss){
        this.splitStrategy=ss ;

    }

    public SplitStrategy<Id> getSplitStrategy(){
        return this.splitStrategy ;

    }

    /*  public Set<Set<Id>> computeDiagnoses() throws NoConflictException,SolverException,InconsistentTheoryException{

     createRoot();



     return computeDiagnoses(getRoot());
 }

 public Set<Set<Id>> computeDiagnoses(SimpleNode<Id> node){

     Set<Set<Id>> diagnoses =new LinkedHashSet<Set<Id>>();
     Set<Set<Id>> conflicts=node.getAxiomSets();

     if(conflicts.size()==0){
      diagnoses.add(node.getPathLabels());
     }
    else{
         //openNodes.remove(conflicts)
         if( !unitRule(conflicts) ){
             if(!lastConflictRule(conflicts)){
                 subSetRule(conflicts);
                 splitRule(conflicts);
             }



         }
     }
     return diagnoses;
 }

 private void subSetRule(Set<Set<Id>> conflicts){
     for(Set<Id >c1:conflicts){
         for(Set<Id>c2:conflicts){
             if(c1!=c2 && c1.containsAll(c2) )  conflicts.remove(c2);
         }
     }
 }


private Set<Set<Id>> ignore(Id e, Set<Set<Id>>conflicts){

    Set<Set<Id>> newConflicts=conflicts;

     for(Set<Id >c:newConflicts){
         c.remove(e);
     }
     return newConflicts;
 }

private Set<Set<Id>>  addToHS(Id e, Set<Set<Id>>conflicts){
    Set<Set<Id>> newConflicts=conflicts;
     for(Set<Id> c : newConflicts){
         if (c.contains(e))
          newConflicts.remove(c);
     }
     return newConflicts;
 }


 public boolean unitRule(Set<Set<Id>>conflicts){

     boolean foundElement=false;

     for (Set<Id> conflict:conflicts){
         if (conflict.size()==1 && !foundElement) {
             Id e=conflict.iterator().next();
             SimpleNode node =new SimpleNode(addToHS(e,conflicts));
             //tree.addNode(newConflicts);
             //Edge edge= new Edge();
             //edge.from=conflicts;
             //edge.to=newConflicts;
             //edge.value=e;
             //tree.addEdge(e);
             //openNodes.add(newConflicts);
             foundElement=true;

         }
     }
     return foundElement;
 }

 public void splitRule(Set<Set<Id>> conflicts){
     Id e=chooseSplitElement(conflicts);
    SimpleNode node1=new SimpleNode(addToHS(e,conflicts));
    SimpleNode node2=new SimpleNode(ignore(e,conflicts));
     //tree.addNode(node1)
     //tree.addNode(node2)
     //tree.addEdge(conflicts,node1,e)
     //tree.addEdge(conflicts,node2,null)
     //openNodes.add(node1)
     //openNodes.add(node2)
 }

 public boolean lastConflictRule(Set<Set<Id>> conflicts){

     boolean foundElement=false;

     if(conflicts.size()==1){

         foundElement=true;
         Set<Id> conflict=conflicts.iterator().next();
         Id e=conflict.iterator().next();
         SimpleNode node1=new SimpleNode(addToHS(e,conflicts));
        // tree.addNode(newNode1)
         //tree.addEdge(conflicts,newNode1,e)

         if(!(conflict.size()==1)){
             SimpleNode node2=new SimpleNode(ignore(e,conflicts));
           //  tree.addNode(newNode2)
            // tree.addEdge(conflicts,newNode2,null)
            // openNodes.add(newNode2)
         }
     }
     return foundElement;
 }

 protected Set<Set<Id>> calculateNode(SimpleNode<Id> node) throws SolverException, InconsistentTheoryException, NoConflictException{
     return calculateConflict(node);
 }


private Id chooseSplitElement(Set<Set<Id>> conflicts){
    return null;
}
*/
}
