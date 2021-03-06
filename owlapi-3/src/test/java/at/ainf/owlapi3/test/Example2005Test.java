package at.ainf.owlapi3.test;

import at.ainf.diagnosis.debugger.SimpleQueryDebugger;
import at.ainf.diagnosis.model.InconsistentTheoryException;
import at.ainf.diagnosis.model.SolverException;
import at.ainf.diagnosis.storage.FormulaSet;
import at.ainf.diagnosis.tree.exceptions.NoConflictException;
import at.ainf.owlapi3.model.OWLTheory;
import at.ainf.owlapi3.parser.MyOWLRendererParser;
import org.junit.Ignore;
import org.junit.Test;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.expression.ParserException;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;

import java.io.File;
import java.util.*;

import static org.junit.Assert.assertTrue;

/**
 * Created by IntelliJ IDEA.
 * User: pfleiss
 * Date: 18.04.11
 * Time: 10:15
 * To change this template use File | Settings | File Templates.
 */
public class Example2005Test {

    static OWLTheory theory;

    static OWLOntology ontology;

    static OWLReasonerFactory reasonerFactory;

    static Set<OWLLogicalAxiom> bax = new LinkedHashSet<OWLLogicalAxiom>();

    public static File file = new File(ClassLoader.getSystemResource("ontologies/iswc2005.owl").getFile());

    MyOWLRendererParser parser;

    @Ignore
    @Test
    public void testCSandHS() throws OWLOntologyCreationException, InconsistentTheoryException, SolverException, NoConflictException, ParserException {
        //SimpleStorage<OWLLogicalAxiom> storage = new SimpleStorage<OWLLogicalAxiom>();
        //UniformCostSearch<OWLLogicalAxiom> start = new UniformCostSearch<OWLLogicalAxiom>(storage);

        //start.setSearcher(new QuickXplain<OWLLogicalAxiom>());


        createOntology();
        //theory = new OWLTheory(reasonerFactory, ontology, bax);
        //start.setNodeCostsEstimator(new OWLAxiomNodeCostsEstimator(theory,map));

        //start.setSearchable(theory);

        OWLTheory t = new OWLTheory(new Reasoner.ReasonerFactory(),ontology,bax);
        SimpleQueryDebugger<OWLLogicalAxiom> debugger = new SimpleQueryDebugger<OWLLogicalAxiom>(t);
        debugger.updateMaxHittingSets(-1);

        debugger.start();

        Set<? extends FormulaSet<OWLLogicalAxiom>> hittingset = debugger.getDiagnoses();
        Set<? extends FormulaSet<OWLLogicalAxiom>> conflictset = debugger.getConflicts();

        assertTrue(hittingset.size() == 3);
        assertTrue(hittingset.iterator().next().size() == 1);
        Iterator<? extends FormulaSet<OWLLogicalAxiom>> hittingSetItr = hittingset.iterator();
        assertTrue(MyOWLRendererParser.render(hittingSetItr.next().iterator().next()).equals("A3 SubClassOf A4 and A5"));
        assertTrue(MyOWLRendererParser.render(hittingSetItr.next().iterator().next()).equals("A4 SubClassOf C and (s only F)"));
        assertTrue(MyOWLRendererParser.render(hittingSetItr.next().iterator().next()).equals("A5 SubClassOf s some (not (F))"));

        assertTrue(conflictset.size() == 1);
        Collection<OWLLogicalAxiom> hset = conflictset.iterator().next();
        for (OWLLogicalAxiom axiom : hset) {
            assertTrue(MyOWLRendererParser.render(axiom).equals("A3 SubClassOf A4 and A5") ||
                    MyOWLRendererParser.render(axiom).equals("A4 SubClassOf C and (s only F)") ||
                    MyOWLRendererParser.render(axiom).equals("A5 SubClassOf s some (not (F))"));
        }

    }

    @Ignore
    @Test
    public void testposT() throws OWLOntologyCreationException, InconsistentTheoryException, SolverException, NoConflictException {
        //SimpleStorage<OWLLogicalAxiom> storage = new SimpleStorage<OWLLogicalAxiom>();
        //BreadthFirstSearch<OWLLogicalAxiom> start = new BreadthFirstSearch<OWLLogicalAxiom>(storage);

        //start.setSearcher(new QuickXplain<OWLLogicalAxiom>());
        createOntology();
        //theory = new OWLTheory(reasonerFactory, ontology, bax);

        //start.setSearchable(theory);
        OWLTheory t = new OWLTheory(new Reasoner.ReasonerFactory(),ontology,bax);
        SimpleQueryDebugger<OWLLogicalAxiom> debugger = new SimpleQueryDebugger<OWLLogicalAxiom>(t);
        debugger.setMaxDiagnosesNumber(0);

        t.getKnowledgeBase().addPositiveTest(Collections.singleton(parser.parse("w Type not C")));

        debugger.start();

        Set<? extends FormulaSet<OWLLogicalAxiom>> hittingset = debugger.getDiagnoses();
        Set<? extends FormulaSet<OWLLogicalAxiom>> conflictset = debugger.getConflicts();

        assertTrue(hittingset.size() == 3);
        Iterator<? extends FormulaSet<OWLLogicalAxiom>> hsItr = hittingset.iterator();
        Set<OWLLogicalAxiom> hs = hsItr.next();
        assertTrue(hs.size() == 2);
        for (OWLLogicalAxiom a : hs) {
            assertTrue(MyOWLRendererParser.render(a).equals("A6 SubClassOf A4 and D") ||
                    MyOWLRendererParser.render(a).equals("A5 SubClassOf s some (not (F))"));
        }

        hs = hsItr.next();
        assertTrue(hs.size() == 2);
        for (OWLLogicalAxiom a : hs) {
            assertTrue(MyOWLRendererParser.render(a).equals("A3 SubClassOf A4 and A5") ||
                    MyOWLRendererParser.render(a).equals("A6 SubClassOf A4 and D"));
        }
        hs = hsItr.next();
        assertTrue(hs.size() == 1);
        assertTrue(MyOWLRendererParser.render(hs.iterator().next()).equals("A4 SubClassOf C and (s only F)"));


        assertTrue(conflictset.size() == 2);


    }

    public void createOntology() throws OWLOntologyCreationException, InconsistentTheoryException, SolverException {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

        ontology = manager.loadOntologyFromOntologyDocument(file);

        bax = new LinkedHashSet<OWLLogicalAxiom>();

        for (OWLIndividual ind : ontology.getIndividualsInSignature()) {
            bax.addAll(ontology.getClassAssertionAxioms(ind));
            bax.addAll(ontology.getObjectPropertyAssertionAxioms(ind));
        }

        parser = new MyOWLRendererParser(ontology);
        reasonerFactory = new Reasoner.ReasonerFactory();

    }

}
