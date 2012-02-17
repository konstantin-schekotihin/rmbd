package at.ainf.owlcontroller;

import at.ainf.diagnosis.Searcher;
import at.ainf.diagnosis.debugger.SimpleQueryDebugger;
import at.ainf.diagnosis.quickxplain.NewQuickXplain;
import at.ainf.diagnosis.tree.exceptions.NoConflictException;
import at.ainf.owlapi3.debugging.OWLNegateAxiom;
import at.ainf.owlapi3.model.OWLDiagnosisSearchableObject;
import at.ainf.owlapi3.model.OWLTheory;
import at.ainf.theory.model.InconsistentTheoryException;
import at.ainf.theory.model.SolverException;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

import java.net.URISyntaxException;
import java.util.*;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Created by IntelliJ IDEA.
 * User: kostya
 * Date: 18.01.2010
 * Time: 08:58:55
 * To change this template use File | Settings | File Templates.
 */
public class TreeTest {

    private static Logger logger = Logger.getLogger(TreeTest.class.getName());
    //private OWLDebugger debugger = new SimpleDebugger();
    private SimpleQueryDebugger<OWLLogicalAxiom> debug = new SimpleQueryDebugger<OWLLogicalAxiom>(null);
    private OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
    private static final String TEST_IRI = "http://www.semanticweb.org/ontologies/2010/0/ecai.owl#";

    @BeforeClass
    public static void setUp() {
        String conf = ClassLoader.getSystemResource("owlcontroller-log4j.properties").getFile();
        PropertyConfigurator.configure(conf);
    }

    @Test
    public void testDiagnosisSearcher() throws InconsistentTheoryException, OWLOntologyCreationException, SolverException, NoConflictException {
        OWLTheory th = Utils.loadTheory(manager, "ontologies/ecai.simple.owl");
        Searcher<OWLLogicalAxiom> searcher = new NewQuickXplain<OWLLogicalAxiom>();
        Set<OWLLogicalAxiom> diagnosis = searcher.search(new OWLDiagnosisSearchableObject(th), th.getActiveFormulas(), null);

        String logd = "Hitting set: {" + Utils.logCollection(diagnosis);
        logger.info(logd);
    }

    @Test
    public void testSimpleTestCases() throws InconsistentTheoryException, SolverException, URISyntaxException, OWLException {

        OWLTheory th = Utils.loadTheory(manager, "ontologies/ecai.simple.owl");
        debug.set_Theory(th);
        debug.reset();
        OWLOntologyManager manager = th.getOntology().getOWLOntologyManager();

        // test only entailed test case

        OWLDataFactory owlDataFactory = manager.getOWLDataFactory();

        OWLClassAssertionAxiom axiom = owlDataFactory.getOWLClassAssertionAxiom(owlDataFactory.getOWLClass(IRI.create(TEST_IRI + "C")),
                owlDataFactory.getOWLNamedIndividual(IRI.create(TEST_IRI + "w")));
        th.addEntailedTest(axiom);

        assertTrue(debug.debug());

        String logd = Utils.logCollection(logger, "Hitting sets", debug.getValidHittingSets());
        String logc = Utils.logCollection(logger, "Conflicts", debug.getConflictSets());
        assertEquals("Hitting sets { {C}, {D} }", logd);
        assertEquals("Conflicts { {C,D} }", logc);

        // test only nonentailed test case

        debug.reset();

        th.removeEntailedTest(axiom);
        th.addNonEntailedTest(axiom);
        assertTrue(debug.debug());

        logd = Utils.logCollection(logger, "Hitting sets", debug.getValidHittingSets());
        logc = Utils.logCollection(logger, "Conflicts", debug.getConflictSets());
        assertEquals("Hitting sets { {A}, {B} }", logd);
        assertEquals("Conflicts { {A,B} }", logc);

        // test both test casea
        debug.reset();
        th.removeNonEntailedTest(axiom);

        th.addEntailedTest(axiom);

        OWLClassAssertionAxiom naxiom = owlDataFactory.getOWLClassAssertionAxiom(owlDataFactory.getOWLClass(IRI.create(TEST_IRI + "D")),
                owlDataFactory.getOWLNamedIndividual(IRI.create(TEST_IRI + "v")));
        th.addNonEntailedTest(naxiom);

        assertTrue(debug.debug());

        logd = Utils.logCollection(logger, "Hitting sets", debug.getValidHittingSets());
        logc = Utils.logCollection(logger, "Conflicts", debug.getConflictSets());
        assertEquals("Hitting sets { {C}, {A,D}, {B,D} }", logd);
        assertEquals("Conflicts { {A,B,C}, {C,D} }", logc);

        // test without test cases

        debug.reset();
        th.removeEntailedTest(axiom);
        th.removeNonEntailedTest(naxiom);
        assertTrue(debug.debug());

        logd = Utils.logCollection(logger, "Hitting sets", debug.getValidHittingSets());
        logc = Utils.logCollection(logger, "Conflicts", debug.getConflictSets());
        assertEquals("Hitting sets { {A}, {B}, {C}, {D} }", logd);
        assertEquals("Conflicts { {A,B,C,D} }", logc);

    }

    @Test
    public void testTestCases() throws OWLException, SolverException, URISyntaxException, InconsistentTheoryException {

        testOntology("ontologies/ecai.owl", false);

        String logd = Utils.logCollection(logger, "Hitting sets", debug.getValidHittingSets());
        String logc = Utils.logCollection(logger, "Conflicts", debug.getConflictSets());
        assertEquals("Hitting sets { {A1}, {M1}, {M3,M2}, {A2,M2} }", logd);
        assertEquals("Conflicts { {M3,A1,A2,M1}, {A1,M1,M2} }", logc);


        // testConsistency that there are no changes in the theory

        debug.reset();
        assertTrue(debug.debug());

        assertEquals(logd, Utils.logCollection(logger, "Hitting sets", debug.getValidHittingSets()));
        assertEquals(logc, Utils.logCollection(logger, "Conflicts", debug.getConflictSets()));


        OWLDataFactory owlDataFactory = ((OWLTheory)debug.getTheory()).getOntology().getOWLOntologyManager().getOWLDataFactory();
        List<OWLLogicalAxiom> negTest = new LinkedList<OWLLogicalAxiom>();
        OWLSubClassOfAxiom ax = owlDataFactory.getOWLSubClassOfAxiom(owlDataFactory.getOWLClass(IRI.create(TEST_IRI + "B")),
                owlDataFactory.getOWLClass(IRI.create(TEST_IRI + "M3")));
        OWLLogicalAxiom negate = ((OWLTheory)debug.getTheory()).negate(ax);
        negTest.add(negate);


        debug.reset();
        assertTrue(debug.debug());

        Utils.logCollection(logger, "Hitting sets", debug.getValidHittingSets());
        Utils.logCollection(logger, "Conflicts", debug.getConflictSets());


    }

    /*
    @Test
    public void testNegation() throws SolverException, URISyntaxException, OWLException, InconsistentTheoryException {


        OWLDataFactory owlDataFactory = manager.getOWLDataFactory();
        List<OWLLogicalAxiom> list = new LinkedList<OWLLogicalAxiom>();

        OWLLogicalAxiom ax = owlDataFactory.getOWLSubClassOfAxiom(owlDataFactory.getOWLClass(IRI.create(TEST_IRI + "B")),
                owlDataFactory.getOWLClass(IRI.create(TEST_IRI + "M3")));
        list.add(ax);
        ax = owlDataFactory.getOWLClassAssertionAxiom(owlDataFactory.getOWLClass(IRI.create(TEST_IRI + "C")),
                owlDataFactory.getOWLNamedIndividual(IRI.create(TEST_IRI + "w")));
        list.add(ax);

        OWLReasonerFactory reasonerFactory = new Reasoner.ReasonerFactory();
        OWLOntology ontology = manager.createOntology();

        for (OWLLogicalAxiom axiom : list) {

            manager.addAxiom(ontology, axiom);
            OWLLogicalAxiom nax = negateFormulas(axiom);
            manager.addAxiom(ontology, nax);
            OWLTheory th = new OWLTheory(reasonerFactory, ontology);
            Assert.assertFalse(th.verifyRequirements());
        }

    }


    @Test
    public void readTest() throws InconsistentTheoryException, OWLOntologyCreationException, SolverException, FileNotFoundException {
        FileInputStream st = new FileInputStream("C:\\Users\\kostya\\ontologies\\Ontology1300710969086\\Ontology1300710969086.owl");
        OWLOntology o = manager.loadOntologyFromOntologyDocument(st);
        o.getLogicalAxioms();
    }
    */

    public OWLLogicalAxiom negate(OWLLogicalAxiom ax) {
        OWLNegateAxiom vis = new OWLNegateAxiom(manager.getOWLDataFactory());
        OWLLogicalAxiom negated = (OWLLogicalAxiom) ax.accept(vis);
        return negated;
    }

    private void testEntailment(OWLTheory th, OWLReasoner solver, OWLLogicalAxiom ax, boolean res) {
        OWLLogicalAxiom neg = th.negate(ax);
        th.getOwlOntologyManager().addAxiom(th.getOntology(), neg);
        solver.flush();
        assertEquals(solver.isConsistent(), res);
        //if (res)
        //assertEquals(solver.getUnsatisfiableClasses().getSize(), 1);
        th.getOwlOntologyManager().removeAxiom(th.getOntology(), neg);
    }

    @Test
    public void testDebug1() throws OWLException, SolverException, URISyntaxException, InconsistentTheoryException {
        testOntology("ontologies/ecai.1.owl", false);
        String logd = Utils.logCollection(logger, "Hitting sets", debug.getValidHittingSets());
        String logc = Utils.logCollection(logger, "Conflicts", debug.getConflictSets());
        assertEquals("Hitting sets { {A1}, {M1}, {M2} }", logd);
        assertEquals("Conflicts { {A1,M1,M2} }", logc);
    }

    @Test
    public void testDebug2() throws OWLException, SolverException, URISyntaxException, InconsistentTheoryException {
        testOntology("ontologies/ecai.2.owl", false);

        String logd = Utils.logCollection(logger, "Hitting sets", debug.getValidHittingSets());
        String logc = Utils.logCollection(logger, "Conflicts", debug.getConflictSets());
        assertEquals("Hitting sets { {M3}, {A1}, {A2}, {M1} }", logd);
        assertEquals("Conflicts { {M3,A1,A2,M1} }", logc);
    }

    @Test
    public void testCommonEntailments() throws OWLException, SolverException, URISyntaxException, InconsistentTheoryException {
        OWLTheory owlTheory = testOntology("ontologies/ecai.owl", false);

        Iterator<? extends Set<OWLLogicalAxiom>> hsi = debug.getValidHittingSets().iterator();
        Set<OWLLogicalAxiom> entailments = new TreeSet<OWLLogicalAxiom>();
        while (hsi.hasNext()) {
            Collection<OWLLogicalAxiom> inferredAxioms = owlTheory.getEntailments(hsi.next());
            if (entailments.isEmpty())
                entailments.addAll(inferredAxioms);
            else
                entailments.retainAll(inferredAxioms);
        }
        String log = Utils.logCollection(entailments);
        logger.info(log);

    }

    @Test
    public void testDebugFull() throws OWLException, SolverException, URISyntaxException, InconsistentTheoryException {
        testOntology("ontologies/ecai.owl", false);

        String logd = Utils.logCollection(logger, "Hitting sets", debug.getValidHittingSets());
        String logc = Utils.logCollection(logger, "Conflicts", debug.getConflictSets());
        assertEquals("Hitting sets { {A1}, {M1}, {M3,M2}, {A2,M2} }", logd);
        assertEquals("Conflicts { {M3,A1,A2,M1}, {A1,M1,M2} }", logc);
    }

    @Test
    public void testDebug3() throws OWLException, SolverException, URISyntaxException, InconsistentTheoryException {
        testOntology("ontologies/ecai.3.owl", false);

        String logd = Utils.logCollection(logger, "Hitting sets", debug.getValidHittingSets());
        String logc = Utils.logCollection(logger, "Conflicts", debug.getConflictSets());
        //assertEquals("Hitting sets { {M2,A2}, {M2,M3}, {M1}, {A1} }", logd);
        //assertEquals("Conflicts { {M2,M1,A1}, {M3,A2,M1,A1} }", logc);
    }

    //@Test
    public void testDebugCorrect() throws OWLException, SolverException, URISyntaxException, InconsistentTheoryException {
        testOntology("ontologies/ecai.corrected.owl", true);

        String logd = Utils.logCollection(logger, "Hitting sets", debug.getValidHittingSets());
        String logc = Utils.logCollection(logger, "Conflicts", debug.getConflictSets());
        assertEquals("Hitting sets { }", logd);
        assertEquals("Conflicts { }", logc);
    }

    /* @Test
    public void testEntailment() throws OWLException, SolverException, URISyntaxException {
        testOntology("ontologies/ecai.corrected.owl", true);

        Collection<OWLLogicalAxiom> inferredAxioms = debugger.getTheory().getEntailments();
        OWLLogicalAxiom ax = inferredAxioms.iterator().next();
        OWLOntology owlOntology = debugger.getOWLOntology();
        OWLReasoner reasoner = debugger.getTheory().getSolver();

        assertTrue(reasoner.isEntailed(ax));
        OWLLogicalAxiom neg = debugger.getTheory().negateFormulas(ax);
        owlOntology.getOWLOntologyManager().addAxiom(owlOntology, neg);
        reasoner.flush();
        boolean consistent = reasoner.verifyRequirements();
        assertFalse(consistent);
    }               */

    @Test
    public void testDebugIncoherent() throws OWLException, SolverException, URISyntaxException, InconsistentTheoryException {
        testOntology("ontologies/ecai.incoherent.owl", false);

        String logd = Utils.logCollection(logger, "Hitting sets", debug.getValidHittingSets());
        String logc = Utils.logCollection(logger, "Conflicts", debug.getConflictSets());
        assertEquals("Hitting sets { {A1}, {M2}, {D[ C A1 ]} }", logd);
        assertEquals("Conflicts { {A1,M2,D[ C A1 ]} }", logc);
    }


    private OWLTheory testOntology(String path, boolean sat) throws OWLException, SolverException, URISyntaxException, InconsistentTheoryException {
        return testOntology(path, sat, null, null);
    }

    private OWLTheory testOntology(String path, boolean sat, Collection<OWLLogicalAxiom> positiveCases,
                                   Collection<OWLLogicalAxiom> negativeCases) throws OWLException, SolverException,
            URISyntaxException, InconsistentTheoryException {
        //debugger.reset();
        OWLTheory th = Utils.loadTheory(manager, path);

        if (positiveCases != null)
            for (OWLLogicalAxiom test : positiveCases)
                th.addPositiveTest(test);
        if (negativeCases != null)
            for (OWLLogicalAxiom test : negativeCases)
                th.addNegativeTest(test);

        debug.set_Theory(th);
        debug.reset();
        debug.debug();
        //debugger.setTheory(th);
        //assertEquals(debugger.debug(), !sat);
        return th;
    }


    private Collection<OWLLogicalAxiom> getInferredAxioms(OWLTheory th, Set<OWLLogicalAxiom> hs) {
        th.removeAxioms(hs, th.getOntology());
        OWLReasoner solver = th.getSolver();
        //testConsistency(solver, true);

        OWLOntology ontology = th.getOntology();
        Set<OWLClass> classes = new TreeSet<OWLClass>();
        for (OWLOntology ont : solver.getRootOntology().getImportsClosure()) {
            classes.addAll(ont.getClassesInSignature(true));
        }

        Collection<OWLLogicalAxiom> axs = new TreeSet<OWLLogicalAxiom>();

        for (OWLClass cl : classes) {
            getSubClasses(axs, cl);
        }

        th.addAxioms(hs, th.getOntology());
        return axs;
    }

    private void getSubClasses(Collection<OWLLogicalAxiom> axs, OWLClass cl) {
        if (cl.isTopEntity() || cl.isBottomEntity())
            return;
        OWLReasoner solver = ((OWLTheory)debug.getTheory()).getSolver();
        OWLOntology ontology = ((OWLTheory)debug.getTheory()).getOntology();
        OWLOntologyManager manager = ontology.getOWLOntologyManager();
        OWLDataFactory dataFactory = manager.getOWLDataFactory();

        for (OWLClass sub : solver.getSubClasses(cl, true).getFlattened()) {
            if (!sub.isTopEntity() && !sub.isBottomEntity()) {
                axs.add(dataFactory.getOWLSubClassOfAxiom(sub, cl));
                getSubClasses(axs, sub);
            }
        }
    }

    private Collection<Collection<OWLLogicalAxiom>> getDiagnosesEntailments(OWLTheory th,
                                                                            Collection<Set<OWLLogicalAxiom>> hittingSets)
            throws OWLException, SolverException {
        List<Collection<OWLLogicalAxiom>> entailments = new LinkedList<Collection<OWLLogicalAxiom>>();

        if (hittingSets == null || hittingSets.isEmpty()) {
            entailments.add(th.getEntailments(new TreeSet<OWLLogicalAxiom>()));
            return entailments;
        }
        for (Set<OWLLogicalAxiom> hs : hittingSets) {
            Collection<OWLLogicalAxiom> ax = th.getEntailments(hs);
            entailments.add(ax);
        }
        return entailments;
    }

    private Collection<Collection<OWLLogicalAxiom>> createTestCases(Collection<OWLLogicalAxiom> ent, OWLTheory theory) {
        Collection<Collection<OWLLogicalAxiom>> tests = new LinkedList<Collection<OWLLogicalAxiom>>();
        for (OWLLogicalAxiom ax : ent) {
            Collection<OWLLogicalAxiom> test = new LinkedList<OWLLogicalAxiom>();
            test.add(theory.negate(ax));
            tests.add(test);
        }
        return tests;
    }

}