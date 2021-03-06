package at.ainf.owlapi3.module.iterative.diagsearcher;

import org.semanticweb.owlapi.model.OWLLogicalAxiom;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: pfleiss
 * Date: 12.03.13
 * Time: 13:39
 * To change this template use File | Settings | File Templates.
 */
public interface ModuleDiagSearcher {

    public Set<OWLLogicalAxiom> calculateDiag(Set<OWLLogicalAxiom> axioms, Set<OWLLogicalAxiom> backg);


    public TreeCreator getTreeCreator();

    public void setTreeCreator(TreeCreator treeCreator);

    public void setReasonerFactory(OWLReasonerFactory reasonerFactory);

    public OWLReasonerFactory getReasonerFactory();

}
