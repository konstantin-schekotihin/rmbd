package at.ainf.owlapi3.model;

import at.ainf.diagnosis.model.*;
import at.ainf.diagnosis.storage.FormulaSet;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.util.*;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;

import static _dev.TimeLog.start;
import static _dev.TimeLog.stop;

//import org.semanticweb.HermiT.Reasoner;

/**
 * Created by IntelliJ IDEA.
 * User: kostya
 * Date: 13.01.2010
 * Time: 14:21:13
 * To change this template use File | Settings | File Templates.
 */
public class OWLTheory extends BaseSearchableObject<OWLLogicalAxiom> {

    private static Logger logger = LoggerFactory.getLogger(OWLTheory.class.getName());

    private OWLOntologyManager owlOntologyManager;

    //private OWLOntology ontology;

    private OWLOntology original;

    private boolean includeTrivialEntailments = true;
    private final boolean BUFFERED_SOLVER = true;
    private boolean REDUCE_TO_UNSAT = false;

    @Override
    protected BaseSearchableObject<OWLLogicalAxiom> getNewInstance(IKnowledgeBase<OWLLogicalAxiom> knowledgeBase, AbstractReasoner<OWLLogicalAxiom> reasoner) throws SolverException, InconsistentTheoryException {
        return new OWLTheory(this.originalReasonerFactories, this.origOntology, knowledgeBase.getBackgroundFormulas());
    }

    public OWLTheory copyChangedTheory(Set<OWLAxiom> knowledgeBaseAxioms) throws SolverException, InconsistentTheoryException, OWLOntologyCreationException {
        OWLOntology ontology = getOwlOntologyManager().createOntology(knowledgeBaseAxioms);
        return new OWLTheory(this.originalReasonerFactories, ontology, getKnowledgeBase().getBackgroundFormulas());
    }

    public void setIncludeTrivialEntailments(boolean includeTrivialEntailments) {
        this.includeTrivialEntailments = includeTrivialEntailments;
    }

    public void doBayesUpdate(Set<? extends FormulaSet<OWLLogicalAxiom>> hittingSets) {
        for (FormulaSet<OWLLogicalAxiom> hs : hittingSets) {
            Set<OWLLogicalAxiom> positive = new LinkedHashSet<OWLLogicalAxiom>();

            for (int i = 0; i < getKnowledgeBase().getTestsSize(); i++) {
                Set<OWLLogicalAxiom> testcase = getKnowledgeBase().getTest(i);

                if (i - 1 > -1) {
                    Set<OWLLogicalAxiom> olderTC = getKnowledgeBase().getTest(i - 1);
                    if (getKnowledgeBase().getTypeOfTest(olderTC))
                        positive.addAll(olderTC);
                }
                BigDecimal value = hs.getMeasure().divide(BigDecimal.valueOf(2));

                if (getKnowledgeBase().getTypeOfTest(testcase)) {
                    if (!diagnosisEntails(hs, testcase, positive)) {
                        hs.setMeasure(value);
                    }
                } else {
                    if (diagnosisConsistent(hs, testcase, positive)) {
                        hs.setMeasure(value);
                    }
                }
            }
        }
    }


    protected static final OWLClass TOP_CLASS = OWLManager.getOWLDataFactory().getOWLThing();
    protected static final OWLClass BOTTOM_CLASS = OWLManager.getOWLDataFactory().getOWLNothing();

    protected boolean isReduceToUnsat() {
        return REDUCE_TO_UNSAT;
    }


    private List<OWLReasonerFactory> originalReasonerFactories;

    private OWLOntology origOntology;

    private Set<OWLLogicalAxiom> origBackgroundAxioms;


    public void reset() {

        try {
            init(originalReasonerFactories, origOntology, origBackgroundAxioms);
        } catch (InconsistentTheoryException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (SolverException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    // Set<OWLLogicalAxiom> formularCache = new LinkedHashSet<OWLLogicalAxiom>();

    private void init(List<OWLReasonerFactory> reasonerFactory, OWLOntology ontology, Set<OWLLogicalAxiom> backgroundAxioms)
            throws InconsistentTheoryException, SolverException {
        OWLOntologyManager man = ontology.getOWLOntologyManager();
        setOwlOntologyManager(man);
        if (reasonerFactory.size() == 1)
            setReasoner(new ReasonerOWL(man, reasonerFactory.get(0)));
        else
            setReasoner(new MultipleReasonersOWL(man, reasonerFactory));

        //try {
        //OWLOntology dontology = owlOntologyManager.createOntology();
        OWLLiteral lit = owlOntologyManager.getOWLDataFactory().getOWLLiteral("Test Ontology");
        IRI iri = OWLRDFVocabulary.RDFS_COMMENT.getIRI();
        OWLAnnotation anno = owlOntologyManager.getOWLDataFactory().getOWLAnnotation(owlOntologyManager.getOWLDataFactory().getOWLAnnotationProperty(iri), lit);
        //owlOntologyManager.applyChange(new AddOntologyAnnotation(dontology, anno));

            /*if (BUFFERED_SOLVER)
                setSolver(reasonerFactory.createReasoner(getOntology()));
            else
                setSolver(reasonerFactory.createNonBufferingReasoner(getOntology()));*/
        /*} catch (OWLOntologyCreationException e) {
            throw new OWLRuntimeException(e);
        }*/

        Set<OWLLogicalAxiom> logicalAxioms = ontology.getLogicalAxioms();
        getKnowledgeBase().addFormulas(logicalAxioms);
        //getKnowledgeBase().addFaultyFormulas(setminus(logicalAxioms, backgroundAxioms));
        this.original = ontology;

        // add all axioms from imported ontologies, as well as background axioms into a new test ontology
        //OWLOntology dontology = owlOntologyManager.createOntology(IRI.create("http://ontology.ainf.at/debugging" + System.nanoTime()));
        Set<OWLOntology> importsClosure = man.getImportsClosure(ontology);
        for (OWLOntology ont : importsClosure) {
            if (!ont.equals(ontology))
                for (OWLLogicalAxiom ax : ont.getLogicalAxioms()) {
                    getKnowledgeBase().addBackgroundFormulas(Collections.<OWLLogicalAxiom>singleton(ax));
                }
        }

        getKnowledgeBase().addBackgroundFormulas(backgroundAxioms);

    }

    public void activateReduceToUns() {
        LinkedHashSet<OWLLogicalAxiom> backupCachedFormulars = new LinkedHashSet<OWLLogicalAxiom>(getReasoner().getFormulasCache());
        getReasoner().clearFormulasCache();
        getReasoner().addFormulasToCache(getOriginalOntology().getLogicalAxioms());
        if (getReasoner().isConsistent()) {
            Set<OWLClass> entities = getReasoner().getUnsatisfiableEntities();
            entities.remove(BOTTOM_CLASS);
            if (!entities.isEmpty()) {
                String iri = "http://ainf.at/testiri#";
                // TODO module d extraction machen
                for (OWLClass cl : entities) {
                    OWLDataFactory fac = getOriginalOntology().getOWLOntologyManager().getOWLDataFactory();
                    OWLIndividual test_individual = fac.getOWLNamedIndividual(IRI.create(iri + "d_" + cl.getIRI().getFragment()));
                    getKnowledgeBase().addBackgroundFormulas(Collections.<OWLLogicalAxiom>singleton(fac.getOWLClassAssertionAxiom(cl, test_individual)));
                }
            }
        }

        getReasoner().clearFormulasCache();
        getReasoner().addFormulasToCache(backupCachedFormulars);

    }

    public OWLTheory(OWLReasonerFactory reasonerFactory, OWLOntology ontology, Set<OWLLogicalAxiom> backgroundAxioms)
            throws InconsistentTheoryException, SolverException {
        this(Collections.singletonList(reasonerFactory), ontology, backgroundAxioms);
    }

    public OWLTheory(List<OWLReasonerFactory> reasonerFactories, OWLOntology ontology, Set<OWLLogicalAxiom> backgroundAxioms)
            throws InconsistentTheoryException, SolverException {
        this.originalReasonerFactories = reasonerFactories;
        this.origOntology = ontology;
        this.origBackgroundAxioms = backgroundAxioms;
        init(reasonerFactories, ontology, backgroundAxioms);
    }


    public OWLOntology getOriginalOntology() {
        return this.original;
    }

    /*public OWLOntology getOntology() {
        return this.ontology;
    }*/

    public OWLOntologyManager getOwlOntologyManager() {
        return owlOntologyManager;
    }

    protected void setOwlOntologyManager(OWLOntologyManager owlOntologyManager) {
        this.owlOntologyManager = owlOntologyManager;
    }

    //private OWLNegateAxiom vis = null;

    /*public OWLLogicalAxiom negate(OWLLogicalAxiom ax) {
        if (getNegationVisitor() == null)
            vis = new OWLNegateAxiom(getOwlOntologyManager().getOWLDataFactory());
        OWLLogicalAxiom negated = (OWLLogicalAxiom) ax.accept(getNegationVisitor());
        return negated;
    }

    public void registerTestCases() throws SolverException, InconsistentTheoryException {
        Set<OWLLogicalAxiom> tests = new HashSet<OWLLogicalAxiom>();
        for (Set<? extends OWLLogicalAxiom> testCase : getKnowledgeBase().getPositiveTests())
            tests.addAll(testCase);
        for (Set<? extends OWLLogicalAxiom> testCase : getKnowledgeBase().getEntailedTests())
            tests.addAll(testCase);

        getKnowledgeBase().addBackgroundFormulas(tests);
    }

    public void unregisterTestCases() throws SolverException {
        Set<OWLLogicalAxiom> tests = new HashSet<OWLLogicalAxiom>();
        for (Set<? extends OWLLogicalAxiom> testCase : getKnowledgeBase().getPositiveTests())
            tests.addAll(testCase);
        for (Set<? extends OWLLogicalAxiom> testCase : getKnowledgeBase().getEntailedTests())
            tests.addAll(testCase);
        getKnowledgeBase().removeBackgroundFormulas(tests);
    }
    */

    public ReasonerOWL getReasoner() {
        return (ReasonerOWL) super.getReasoner();
    }

    public boolean testDiagnosis(Collection<OWLLogicalAxiom> diag) throws SolverException {
        // clean up formula stack
        getReasoner().clearFormulasCache();
        List<OWLLogicalAxiom> kb = new LinkedList<OWLLogicalAxiom>(getKnowledgeBase().getFaultyFormulas());
        // apply diagnosis
        kb.removeAll(diag);
        getReasoner().addFormulasToCache(kb);

        if (!verifyConsistency()) {
            getReasoner().clearFormulasCache();
            return false;
        }

        for (Set<OWLLogicalAxiom> test : getKnowledgeBase().getNegativeTests()) {
            if (!isEntailed(test)) {
                getReasoner().clearFormulasCache();
                return false;
            }
        }

        for (Set<OWLLogicalAxiom> test : getKnowledgeBase().getNonentailedTests()) {
            if (isEntailed(test)) {
                getReasoner().clearFormulasCache();
                return false;
            }
        }

        getReasoner().clearFormulasCache();
        return true;
    }

    public boolean areTestsConsistent() throws SolverException {
        // clear stack
        getReasoner().clearFormulasCache();
        for (Set<OWLLogicalAxiom> test : getKnowledgeBase().getPositiveTests()) {
            getReasoner().addFormulasToCache(test);
        }
        for (Set<OWLLogicalAxiom> test : getKnowledgeBase().getEntailedTests()) {
            getReasoner().addFormulasToCache(test);
        }
        if (!verifyConsistency()) {
            getReasoner().clearFormulasCache();
            return false;
        }

        // verify negative tests
        for (Set<OWLLogicalAxiom> test : getKnowledgeBase().getNegativeTests()) {
            if (isEntailed(test)) {
                getReasoner().clearFormulasCache();
                return false;
            }
        }

        // verify negative tests
        for (Set<OWLLogicalAxiom> test : getKnowledgeBase().getNonentailedTests()) {
            if (isEntailed(test)) {
                getReasoner().clearFormulasCache();
                return false;
            }
        }

        getReasoner().clearFormulasCache();
        return true;
    }

    public int getConsistencyCount() {
        // TODO remove logging
        int res = consistencyCount;
        this.consistencyCount = 0;
        return res;
    }

    private void incrementConsistencyChecks() {
        // TODO remove logging
        this.consistencyCount++;
    }

    private int consistencyCount = 0;

    private long consistencyTime = 0;

    private void addConsistencyTime(long time) {
        consistencyTime += time;
    }

    public long getConsistencyTime() {
        long res = consistencyTime;
        consistencyTime = 0;
        return res;
    }

    public boolean verifyConsistency() {
        start("Overall consistency check including management");
        //setFormularCach(getReasoner().getFormulasCache(), getKnowledgeBase().getBackgroundFormulas());
        LinkedHashSet<OWLLogicalAxiom> formularsToAdd = new LinkedHashSet<OWLLogicalAxiom>(getKnowledgeBase().getBackgroundFormulas());
        formularsToAdd.removeAll(getReasoner().getFormulasCache());
        getReasoner().addFormulasToCache(formularsToAdd);
        boolean consistent = doConsistencyTest();
        getReasoner().removeFormulasFromCache(formularsToAdd);
        //removeAxioms(getBackgroundFormulas(), getOntology());
        //removeAxioms(getFormulasCache(), getOntology());
        stop();
        if (logger.isTraceEnabled())
            logger.trace(getOriginalOntology().getOntologyID() + " is consistent: " + consistent);
        return consistent;
    }

    public boolean verifyConsistencyWithReasonerBackground() {
        start("Overall consistency check including management");
        //setFormularCach(getReasoner().getFormulasCache(), getKnowledgeBase().getBackgroundFormulas());
        LinkedHashSet<OWLLogicalAxiom> formularsToAdd = new LinkedHashSet<OWLLogicalAxiom>(getReasoner().getBackgroundAxioms());
        formularsToAdd.removeAll(getReasoner().getFormulasCache());
        getReasoner().addFormulasToCache(formularsToAdd);
        boolean consistent = doConsistencyTest();
        getReasoner().removeFormulasFromCache(formularsToAdd);
        //removeAxioms(getBackgroundFormulas(), getOntology());
        //removeAxioms(getFormulasCache(), getOntology());
        stop();
        if (logger.isTraceEnabled())
            logger.trace(getOriginalOntology().getOntologyID() + " is consistent: " + consistent);
        return consistent;
    }

    public boolean verifySatisfiability(OWLClass unsatClass) {
        start("Sat  check including management");

        LinkedHashSet<OWLLogicalAxiom> formularsToAdd = new LinkedHashSet<OWLLogicalAxiom>(getKnowledgeBase().getBackgroundFormulas());
        formularsToAdd.removeAll(getReasoner().getFormulasCache());
        getReasoner().addFormulasToCache(formularsToAdd);
        boolean consistent = getReasoner().isSatisfiable(unsatClass);
        getReasoner().removeFormulasFromCache(formularsToAdd);
        stop();
        if (logger.isTraceEnabled())
            logger.trace(getOriginalOntology().getOntologyID() + " is consistent: " + consistent);
        return consistent;
    }

    protected boolean doConsistencyTest() {

        //OWLReasoner reasoner = getSolver();
        //Speed4JMeasurement.start("consistencytest");
        boolean consistent, coherent = true;
        //if (useCache)
        //    verifyCache(ontology.getLogicalAxioms());
        start("Reasoner sync ");
        //if (BUFFERED_SOLVER) reasoner.flush();
        stop();
        start("Consistency test");
        incrementConsistencyChecks();
        long timeCons = System.currentTimeMillis();
        consistent = getReasoner().isConsistent();
        addConsistencyTime(System.currentTimeMillis() - timeCons);
        stop();
        start("Coherency test");
        if (!isReduceToUnsat() && consistent) {
            coherent = getReasoner().isCoherent();
        }
        stop();
        consistent = consistent && coherent;
        if (consistent) {
            if (checkTestsConsistency()) return false;
        }
        //Speed4JMeasurement.stop();

        //if (useCache && consistent)
        //    updateCache(ontology.getLogicalAxioms());
        return consistent;
    }

    /*private boolean checkCoherency(OWLReasoner reasoner) {
        reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);
        return reasoner.getBottomClassNode().isSingleton();
    }*/

    private boolean checkTestsConsistency() {
        //OWLReasoner solver = getSolver();
        for (Set<OWLLogicalAxiom> test : getKnowledgeBase().getNegativeTests()) {
            if (!getReasoner().isEntailed(test)) {
                return true;
            }
        }

        for (Set<OWLLogicalAxiom> test : getKnowledgeBase().getNonentailedTests()) {
            if (test != null && getReasoner().isEntailed(test)) {
                return true;
            }
        }
        return false;
    }

    public boolean isEntailed(Set<OWLLogicalAxiom> test, Set<OWLLogicalAxiom> cache) {
        start("Consistency + entailment");
        //setFormularCach(getOntology(), getFormulasCache());
        //OWLReasoner solver = getSolver();
        //LinkedHashSet<OWLLogicalAxiom> backupCachedFormulas = new LinkedHashSet<OWLLogicalAxiom>(getReasoner().getReasonerFormulas());
        getReasoner().clearFormulasCache();
        getReasoner().addFormulasToCache(cache);
        //if (BUFFERED_SOLVER) solver.flush();
        if (!getReasoner().isConsistent())
            return false;
        //solver.precomputeInferences(InferenceType.CLASS_HIERARCHY);
        boolean res = getReasoner().isEntailed(test);
        getReasoner().clearFormulasCache();
        //getReasoner().addFormulasToCache(backupCachedFormulas);
        //removeAxioms(getFormulasCache(), getOntology());
        //if (BUFFERED_SOLVER) solver.flush();
        stop();
        return res;
    }

    public boolean isEntailed(Set<OWLLogicalAxiom> test) {
        start("Consistency + entailment");
        //setFormularCach(getOntology(), getFormulasCache());
        //OWLReasoner solver = getSolver();
        //  LinkedHashSet<OWLLogicalAxiom> backupCachedFormulars = new LinkedHashSet<OWLLogicalAxiom>(getReasoner().getReasonerFormulas());
        //  getReasoner().clearFormulasCache();
        //getReasoner().addFormulasToCache(formularCache);
        //if (BUFFERED_SOLVER) solver.flush();
        if (!getReasoner().isConsistent())
            return false;
        //solver.precomputeInferences(InferenceType.CLASS_HIERARCHY);
        boolean res = getReasoner().isEntailed(test);
        //  getReasoner().clearFormulasCache();
        //  getReasoner().addFormulasToCache(backupCachedFormulars);
        //removeAxioms(getFormulasCache(), getOntology());
        //if (BUFFERED_SOLVER) solver.flush();
        stop();
        return res;
    }

    /*
    public void addAxioms(Set<OWLLogicalAxiom> axioms, OWLOntology ontology) {
        getOwlOntologyManager().addAxioms(ontology, axioms);
        //for (OWLLogicalAxiom ax : axioms) {
        //    getOwlOntologyManager().addAxiom(ontology, ax);
        //}
    }

    public void removeAxioms(Set<OWLLogicalAxiom> axioms, OWLOntology ontology) {
        getOwlOntologyManager().removeAxioms(ontology, axioms);
        //for (OWLLogicalAxiom ax : axioms) {
        //    getOwlOntologyManager().removeAxioms(ontology, axioms);
        //}

    }

    */
    /*
    protected OWLNegateAxiom getNegationVisitor() {
        return vis;
    }
    */
    // A
    public boolean diagnosisConsistent(FormulaSet<OWLLogicalAxiom> hs, Set<OWLLogicalAxiom> ent) {
        // cleanup stack
        Collection<OWLLogicalAxiom> stack = getReasoner().getFormulasCache();
        getReasoner().clearFormulasCache();
        // cleanup ontology
        //Set<OWLLogicalAxiom> logicalAxioms = getOntology().getLogicalAxioms();
        //removeAxioms(logicalAxioms, getOntology());

        // add entailed test cases to simulate extension EX
        for (Set<OWLLogicalAxiom> test : getKnowledgeBase().getEntailedTests()) {
            getReasoner().addFormulasToCache(test);
        }
        // add axioms to the ontology
        getReasoner().addFormulasToCache(setminus(getKnowledgeBase().getFaultyFormulas(), hs));
        getReasoner().addFormulasToCache(getKnowledgeBase().getBackgroundFormulas());
        getReasoner().addFormulasToCache(ent);
        //addAxioms(getOriginalOntology().getLogicalAxioms(), getOntology());
        //removeAxioms(hs, getOntology());
        //addAxioms(ent, getOntology());
        //addAxioms(getBackgroundFormulas(), getOntology());

        boolean res = verifyConsistency();

        // restore the state of the theory prior to the test
        getReasoner().clearFormulasCache();
        //removeAxioms(ent, getOntology());
        //removeAxioms(getBackgroundFormulas(), getOntology());
        //removeAxioms(getOriginalOntology().getLogicalAxioms(), getOntology());
        //addAxioms(logicalAxioms, getOntology());
        //setFormularCach(getOntology(), logicalAxioms);
        getReasoner().addFormulasToCache(stack);
        return res;
    }

    public boolean diagnosisEntailsWithoutEntailedTC(FormulaSet<OWLLogicalAxiom> hs, Set<OWLLogicalAxiom> ent) {
        // cleanup stack
        Collection<OWLLogicalAxiom> stack = getReasoner().getFormulasCache();
        getReasoner().clearFormulasCache();

        Set<OWLLogicalAxiom> cache = new LinkedHashSet<OWLLogicalAxiom>();
        cache.addAll(getKnowledgeBase().getBackgroundFormulas());
        cache.addAll(setminus(getKnowledgeBase().getFaultyFormulas(), hs));

        boolean res = isEntailed(new LinkedHashSet<OWLLogicalAxiom>(ent), cache);

        // restore the state of the theory prior to the test
        getReasoner().clearFormulasCache();
        //setFormularCach(getOntology(), logicalAxioms);
        getReasoner().addFormulasToCache(stack);
        return res;
    }

    //B
    public boolean diagnosisEntails(FormulaSet<OWLLogicalAxiom> hs, Set<OWLLogicalAxiom> ent) {
        // cleanup stack
        Collection<OWLLogicalAxiom> stack = getReasoner().getFormulasCache();
        getReasoner().clearFormulasCache();

        Set<OWLLogicalAxiom> cache = new LinkedHashSet<OWLLogicalAxiom>();
        cache.addAll(flatten(getKnowledgeBase().getPositiveTests()));
        cache.addAll(flatten(getKnowledgeBase().getEntailedTests()));
        cache.addAll(getKnowledgeBase().getBackgroundFormulas());
        cache.addAll(setminus(getKnowledgeBase().getFaultyFormulas(), hs));

        boolean res = isEntailed(new LinkedHashSet<OWLLogicalAxiom>(ent), cache);

        // restore the state of the theory prior to the test
        getReasoner().clearFormulasCache();
        //setFormularCach(getOntology(), logicalAxioms);
        getReasoner().addFormulasToCache(stack);
        return res;
    }

    private Set<OWLLogicalAxiom> flatten(Collection<Set<OWLLogicalAxiom>> collection) {
        Set<OWLLogicalAxiom> ax = new LinkedHashSet<OWLLogicalAxiom>();
        for (Set<OWLLogicalAxiom> ps : collection)
            ax.addAll(ps);
        return ax;
    }



    protected Set<OWLLogicalAxiom> setminus(Set<OWLLogicalAxiom> logicalAxioms, Set<OWLLogicalAxiom> hs) {
        Set<OWLLogicalAxiom> res = new LinkedHashSet<OWLLogicalAxiom>(logicalAxioms);
        res.removeAll(hs);
        return res;
    }

    public boolean diagnosisConsistentWithoutEntailedTc(FormulaSet<OWLLogicalAxiom> hs, Set<OWLLogicalAxiom> ent) {
        // cleanup stack
        Collection<OWLLogicalAxiom> stack = getReasoner().getFormulasCache();
        getReasoner().clearFormulasCache();

        // add axioms to the ontology
        getReasoner().addFormulasToCache(setminus(getKnowledgeBase().getFaultyFormulas(), hs));
        getReasoner().addFormulasToCache(getKnowledgeBase().getBackgroundFormulas());
        getReasoner().addFormulasToCache(ent);
        //addAxioms(getOriginalOntology().getLogicalAxioms(), getOntology());
        //removeAxioms(hs, getOntology());
        //addAxioms(ent, getOntology());
        //addAxioms(getBackgroundFormulas(), getOntology());

        boolean res = verifyConsistency();

        // restore the state of the theory prior to the test
        getReasoner().clearFormulasCache();
        //removeAxioms(ent, getOntology());
        //removeAxioms(getBackgroundFormulas(), getOntology());
        //removeAxioms(getOriginalOntology().getLogicalAxioms(), getOntology());
        //addAxioms(logicalAxioms, getOntology());
        //setFormularCach(getOntology(), logicalAxioms);
        getReasoner().addFormulasToCache(stack);
        return res;
    }


    public boolean supportEntailments() {
        return true;
    }

    public boolean diagnosisConsistent(FormulaSet<OWLLogicalAxiom> hs, Set<OWLLogicalAxiom> ent, Set<OWLLogicalAxiom> axioms) {
        // cleanup stack
        Collection<OWLLogicalAxiom> stack = getReasoner().getFormulasCache();
        getReasoner().clearFormulasCache();
        // cleanup ontology
        //Set<OWLLogicalAxiom> logicalAxioms = getOntology().getLogicalAxioms();
        //removeAxioms(logicalAxioms, getOntology());

        // add entailed test cases to simulate extension EX
        for (Set<OWLLogicalAxiom> test : getKnowledgeBase().getEntailedTests()) {
            getReasoner().addFormulasToCache(test);
        }
        // add axioms to the ontology
        getReasoner().addFormulasToCache(setminus(getKnowledgeBase().getFaultyFormulas(), hs));
        getReasoner().addFormulasToCache(getKnowledgeBase().getBackgroundFormulas());
        getReasoner().addFormulasToCache(axioms);
        getReasoner().addFormulasToCache(ent);
        //addAxioms(getOriginalOntology().getLogicalAxioms(), getOntology());
        //removeAxioms(hs, getOntology());
        //addAxioms(ent, getOntology());
        //addAxioms(getBackgroundFormulas(), getOntology());

        boolean res = verifyConsistency();

        // restore the state of the theory prior to the test
        getReasoner().clearFormulasCache();
        //removeAxioms(ent, getOntology());
        //removeAxioms(getBackgroundFormulas(), getOntology());
        //removeAxioms(getOriginalOntology().getLogicalAxioms(), getOntology());
        //addAxioms(logicalAxioms, getOntology());
        //setFormularCach(getOntology(), logicalAxioms);
        getReasoner().addFormulasToCache(stack);
        return res;
    }

    public final Set<OWLLogicalAxiom> getEntailments(Set<OWLLogicalAxiom> hittingSet) throws SolverException {


        Set<OWLLogicalAxiom> axioms = setminus(getKnowledgeBase().getFaultyFormulas(), hittingSet);
        Collection<OWLLogicalAxiom> stack = getReasoner().getFormulasCache();
        getReasoner().clearFormulasCache();

        getReasoner().addFormulasToCache(axioms);
        getReasoner().addFormulasToCache(getKnowledgeBase().getBackgroundFormulas());

        if (!verifyConsistency()) {
            getReasoner().clearFormulasCache();
            getReasoner().addFormulasToCache(stack);
            return null;
        }

        Set<OWLLogicalAxiom> entailments = extractEntailments(getOwlOntologyManager());

        getReasoner().clearFormulasCache();
        getReasoner().addFormulasToCache(stack);
        return entailments;
    }

    private boolean includeSubClassOfAxioms = false;

    private boolean includeClassAssertionAxioms = false;

    private boolean includeEquivalentClassAxioms = false;

    private boolean includePropertyAssertAxioms = false;

    private boolean includeDisjointClassAxioms = false;

    private boolean includeOntologyAxioms = true;

    private boolean includeReferencingThingAxioms = true;

    public boolean isIncludeSubClassOfAxioms() {
        return includeSubClassOfAxioms;
    }

    public void setIncludeSubClassOfAxioms(boolean includeSubClassOfAxioms) {
        this.includeSubClassOfAxioms = includeSubClassOfAxioms;
    }

    public boolean isIncludeClassAssertionAxioms() {
        return includeClassAssertionAxioms;
    }

    public void setIncludeClassAssertionAxioms(boolean includeClassAssertionAxioms) {
        this.includeClassAssertionAxioms = includeClassAssertionAxioms;
    }

    public boolean isIncludeEquivalentClassAxioms() {
        return includeEquivalentClassAxioms;
    }

    public void setIncludeEquivalentClassAxioms(boolean includeEquivalentClassAxioms) {
        this.includeEquivalentClassAxioms = includeEquivalentClassAxioms;
    }

    public boolean isIncludePropertyAssertAxioms() {
        return includePropertyAssertAxioms;
    }

    public void setIncludePropertyAssertAxioms(boolean includePropertyAssertAxioms) {
        this.includePropertyAssertAxioms = includePropertyAssertAxioms;
    }

    public boolean isIncludeDisjointClassAxioms() {
        return includeDisjointClassAxioms;
    }

    public void setIncludeDisjointClassAxioms(boolean includeDisjointClassAxioms) {
        this.includeDisjointClassAxioms = includeDisjointClassAxioms;
    }

    public boolean isIncludeOntologyAxioms() {
        return includeOntologyAxioms;
    }

    public void setIncludeOntologyAxioms(boolean includeOntologyAxioms) {
        this.includeOntologyAxioms = includeOntologyAxioms;
    }

    public boolean isIncludeReferencingThingAxioms() {
        return includeReferencingThingAxioms;
    }

    public void setIncludeReferencingThingAxioms(boolean includeReferencingThingAxioms) {
        this.includeReferencingThingAxioms = includeReferencingThingAxioms;
    }

    protected Set<OWLLogicalAxiom> extractEntailments(OWLOntologyManager manager) {
        List<InferredAxiomGenerator<? extends OWLLogicalAxiom>> axiomGenerators =
                new LinkedList<InferredAxiomGenerator<? extends OWLLogicalAxiom>>();

        //OWLReasoner reasoner = getSolver();

        if (isIncludeSubClassOfAxioms())
            axiomGenerators.add(new InferredSubClassAxiomGenerator());
        if (isIncludeClassAssertionAxioms())
            axiomGenerators.add(new InferredClassAssertionAxiomGenerator());
        if (isIncludeEquivalentClassAxioms())
            axiomGenerators.add(new InferredEquivalentClassAxiomGenerator());
        if (isIncludeDisjointClassAxioms())
            axiomGenerators.add(new InferredDisjointClassesAxiomGenerator());
        if (isIncludePropertyAssertAxioms())
            axiomGenerators.add(new InferredPropertyAssertionGenerator());

        getReasoner().setAxiomGenerators(axiomGenerators);
        getReasoner().setIncludeOntologyAxioms(isIncludeOntologyAxioms());
        getReasoner().setIncludeAxiomsReferencingThing(isIncludeReferencingThingAxioms());

        return getReasoner().getEntailments();

        /*InferenceType[] infType = new InferenceType[]{InferenceType.CLASS_HIERARCHY, InferenceType.CLASS_ASSERTIONS,
                InferenceType.DISJOINT_CLASSES, InferenceType.DIFFERENT_INDIVIDUALS, InferenceType.SAME_INDIVIDUAL};
        if (!axiomGenerators.isEmpty())
            reasoner.precomputeInferences(infType);

        Set<OWLLogicalAxiom> entailments = new LinkedHashSet<OWLLogicalAxiom>();
        //addDisjoint(reasoner, manager, entailments);
        //addSubclass(reasoner, manager, entailments);
        for (InferredAxiomGenerator<? extends OWLLogicalAxiom> axiomGenerator : axiomGenerators) {
            for (OWLLogicalAxiom ax : axiomGenerator.createAxioms(manager, reasoner)) {
                if (!getOntology().containsAxiom(ax) || isIncludeOntologyAxioms())
                    if (!ax.getClassesInSignature().contains(TOP_CLASS) || isIncludeReferencingThingAxioms()) {
                        entailments.add(ax);
                    }

                //if (includeTrivialEntailments || (!getOntology().containsAxiom(ax) && !ax.getClassesInSignature().contains(TOP_CLASS)))
                //    entailments.add(ax);
            }
        }

        if (isIncludeOntologyAxioms())
            entailments.addAll(getOntology().getLogicalAxioms());
        return entailments;*/

    }

    //

    /*
        private int threshold = 20;

    public boolean caching() {
        return useCache;
    }

    public void useCache(boolean useCache, int threshold) {
        this.useCache = useCache;
        this.threshold = threshold;
    }

    private boolean useCache = true;

    public List<CacheEntry> getCache() {
        return cache;
    }

    private class CacheEntry implements Comparable<CacheEntry>{
        Set<OWLLogicalAxiom> set;
        int calls = 0;

        public CacheEntry(Set<OWLLogicalAxiom> set){
            this.set = set;
        }
        public int size(){
            return set.size();
        }

        public boolean containsAll(Set<OWLLogicalAxiom> set){
            boolean res = this.set.containsAll(set);
            if (res) this.calls++;
            return res;
        }

        public int compareTo(CacheEntry o) {
            if (this.calls != o.calls)
                return -1*Integer.valueOf(this.calls).compareTo(o.calls);
            return -1*Integer.valueOf(size()).compareTo(o.size());
        }

        @Override
        public String toString() {
            return "Cache entry: " + set.size()
                    + " ax used " + calls;
        }
    }

    private List<CacheEntry> cache = new ArrayList<CacheEntry>(threshold);
     */

}
