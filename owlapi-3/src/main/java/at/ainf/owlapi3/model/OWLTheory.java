package at.ainf.owlapi3.model;

import at.ainf.diagnosis.model.*;
import at.ainf.owlapi3.debugging.OWLNegateAxiom;
import at.ainf.diagnosis.storage.AxiomSet;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
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
public class OWLTheory extends AbstractSearchableObject<OWLLogicalAxiom> {

    private static Logger logger = LoggerFactory.getLogger(OWLTheory.class.getName());

    private OWLOntologyManager owlOntologyManager;

    private OWLOntology ontology;

    private OWLOntology original;

    private boolean includeTrivialEntailments = true;
    private final boolean BUFFERED_SOLVER = true;
    private boolean REDUCE_TO_UNSAT = false;

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

    public boolean isIncludeTrivialEntailments() {
        return includeTrivialEntailments;
    }

    public void setIncludeTrivialEntailments(boolean includeTrivialEntailments) {
        this.includeTrivialEntailments = includeTrivialEntailments;
    }

    public void doBayesUpdate(Set<? extends AxiomSet<OWLLogicalAxiom>> hittingSets) {
        for (AxiomSet<OWLLogicalAxiom> hs : hittingSets) {
            Set<OWLLogicalAxiom> positive = new LinkedHashSet<OWLLogicalAxiom>();
            
            for (int i = 0; i < getKnowledgeBase().getTestsSize(); i++) {
                Set<OWLLogicalAxiom> testcase = getKnowledgeBase().getTest(i);
                
                if(i-1>-1) {
                    Set<OWLLogicalAxiom> olderTC = getKnowledgeBase().getTest(i-1);
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

    /* public void doBayesUpdate(Set<? extends AxiomSet<OWLLogicalAxiom>> hittingSets) {
        for (AxiomSet<OWLLogicalAxiom> hs : hittingSets) {
            for (int i = 0; i < getTestsSize(); i++) {
                Set<OWLLogicalAxiom> testcase = getTest(i);
                List<Set<OWLLogicalAxiom>> olderTestcases = getTests(0, i);
                Set<OWLLogicalAxiom> positive = getPositiveTests(olderTestcases);

                double value = (hs.getMeasure() / 2) > 0 ? (hs.getMeasure() / 2) : Double.MIN_VALUE;

                if (getTypeOfTest(testcase)) {
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
    } */

    private Set<OWLLogicalAxiom> getPositiveTests(List<Set<OWLLogicalAxiom>> list) {
        Set<OWLLogicalAxiom> set = new HashSet<OWLLogicalAxiom>();

        for (Set<OWLLogicalAxiom> testcase : list) {
            if (getKnowledgeBase().getTypeOfTest(testcase))
                set.addAll(testcase);
        }

        return set;

    }


    protected static final OWLClass TOP_CLASS = OWLManager.getOWLDataFactory().getOWLThing();
    protected static final OWLClass BOTTOM_CLASS = OWLManager.getOWLDataFactory().getOWLNothing();

    protected boolean isReduceToUnsat() {
        return REDUCE_TO_UNSAT;
    }


    private OWLReasonerFactory originalReasonerFactory;

    private OWLOntology origOntology;

    private Set<OWLLogicalAxiom> origBackgroundAxioms;


    public void reset() {

        try {
            init(originalReasonerFactory, origOntology, origBackgroundAxioms);
        } catch (InconsistentTheoryException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (SolverException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private void init(OWLReasonerFactory reasonerFactory, OWLOntology ontology, Set<OWLLogicalAxiom> backgroundAxioms)
            throws InconsistentTheoryException, SolverException {
        OWLOntologyManager man = ontology.getOWLOntologyManager();
        setOwlOntologyManager(man);
        setReasoner(new ReasonerOWL(man, reasonerFactory));

        try {
            OWLOntology dontology = owlOntologyManager.createOntology();
            OWLLiteral lit = owlOntologyManager.getOWLDataFactory().getOWLLiteral("Test Ontology");
            IRI iri = OWLRDFVocabulary.RDFS_COMMENT.getIRI();
            OWLAnnotation anno = owlOntologyManager.getOWLDataFactory().getOWLAnnotation(owlOntologyManager.getOWLDataFactory().getOWLAnnotationProperty(iri), lit);
            owlOntologyManager.applyChange(new AddOntologyAnnotation(dontology, anno));
            this.ontology = dontology;
            if (BUFFERED_SOLVER)
                setSolver(reasonerFactory.createReasoner(this.ontology));
            else
                setSolver(reasonerFactory.createNonBufferingReasoner(this.ontology));
        } catch (OWLOntologyCreationException e) {
            throw new OWLRuntimeException(e);
        }

        Set<OWLLogicalAxiom> logicalAxioms = ontology.getLogicalAxioms();
        getKnowledgeBase().addFormular(logicalAxioms);
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

        /*if (reduce) {
            REDUCE_TO_UNSAT = true;
            updateAxioms(getOntology(), logicalAxioms, backgroundAxioms);
            getSolver().flush();
            if (getSolver().verifyRequirements()) {
                Set<OWLClass> entities = getSolver().getUnsatisfiableClasses().getEntities();
                updateAxioms(getOntology(), Collections.<OWLLogicalAxiom>emptySet());
                entities.remove(BOTTOM_CLASS);
                if (!entities.isEmpty()) {
                    String iri = "http://ainf.at/testiri#";
                    for (OWLClass cl : entities) {
                        OWLDataFactory fac = man.getOWLDataFactory();
                        OWLIndividual test_individual = fac.getOWLNamedIndividual(IRI.create(iri + "d_" + cl.getIRI().getFragment()));

                        addBackgroundFormula(fac.getOWLClassAssertionAxiom(cl, test_individual));
                    }
                }
            } else
                updateAxioms(getOntology(), Collections.<OWLLogicalAxiom>emptySet());
        }*/

    }

    public void activateReduceToUns() {
        updateAxioms(getOntology(), getOriginalOntology().getLogicalAxioms(), getKnowledgeBase().getBackgroundFormulas());
        getSolver().flush();
        if (getSolver().isConsistent()) {
            Set<OWLClass> entities = getSolver().getUnsatisfiableClasses().getEntities();
            updateAxioms(getOntology(), Collections.<OWLLogicalAxiom>emptySet());
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
        } else
            updateAxioms(getOntology(), Collections.<OWLLogicalAxiom>emptySet());
    }

    public OWLTheory(OWLReasonerFactory reasonerFactory, OWLOntology ontology, Set<OWLLogicalAxiom> backgroundAxioms)
            throws InconsistentTheoryException, SolverException {
        this.originalReasonerFactory = reasonerFactory;
        this.origOntology = ontology;
        this.origBackgroundAxioms = backgroundAxioms;

        init(reasonerFactory, ontology, backgroundAxioms);

    }


    public OWLOntology getOriginalOntology() {
        return this.original;
    }

    public OWLOntology getOntology() {
        return this.ontology;
    }

    public OWLOntologyManager getOwlOntologyManager() {
        return owlOntologyManager;
    }

    protected void setOwlOntologyManager(OWLOntologyManager owlOntologyManager) {
        this.owlOntologyManager = owlOntologyManager;
    }

    private OWLNegateAxiom vis = null;

    public OWLLogicalAxiom negate(OWLLogicalAxiom ax) {
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


    public boolean testDiagnosis(Collection<OWLLogicalAxiom> diag) throws SolverException {
        // clean up formula stack
        getReasoner().clearFormularCache();
        List<OWLLogicalAxiom> kb = new LinkedList<OWLLogicalAxiom>(getKnowledgeBase().getFaultyFormulas());
        // apply diagnosis
        kb.removeAll(diag);
        getReasoner().addFormularsToCache(kb);
        /*
        add(getBackgroundFormulas());
        for (Set<OWLLogicalAxiom> test : getEntailedTests()) {
            add(test);
        }

        for (Set<OWLLogicalAxiom> test : getPositiveTests()) {
            add(test);
        }
        */
        if (!verifyConsistency()) {
            getReasoner().clearFormularCache();
            return false;
        }

        for (Set<OWLLogicalAxiom> test : getKnowledgeBase().getNegativeTests()) {
            if (!isEntailed(test)) {
                getReasoner().clearFormularCache();
                return false;
            }
        }

        for (Set<OWLLogicalAxiom> test : getKnowledgeBase().getNonentailedTests()) {
            if (isEntailed(test)) {
                getReasoner().clearFormularCache();
                return false;
            }
        }

        getReasoner().clearFormularCache();
        return true;
    }

    public boolean areTestsConsistent() throws SolverException {
        // clear stack
        getReasoner().clearFormularCache();
        for (Set<OWLLogicalAxiom> test : getKnowledgeBase().getPositiveTests()) {
            getReasoner().addFormularsToCache(test);
        }
        for (Set<OWLLogicalAxiom> test : getKnowledgeBase().getEntailedTests()) {
            getReasoner().addFormularsToCache(test);
        }
        if (!verifyConsistency()) {
            getReasoner().clearFormularCache();
            return false;
        }

        // verify negative tests
        for (Set<OWLLogicalAxiom> test : getKnowledgeBase().getNegativeTests()) {
            if (isEntailed(test)) {
                getReasoner().clearFormularCache();
                return false;
            }
        }

        // verify negative tests
        for (Set<OWLLogicalAxiom> test : getKnowledgeBase().getNonentailedTests()) {
            if (isEntailed(test)) {
                getReasoner().clearFormularCache();
                return false;
            }
        }

        getReasoner().clearFormularCache();
        return true;
    }

    public int getConsistencyCount() {
        // TODO remove logging
        int res = consistencyCount;
        this.consistencyCount = 0;
        return res;
    }

    private void incrementConsistencyChecks()
    {
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
        updateAxioms(getOntology(), getReasoner().getFormularCache(), getKnowledgeBase().getBackgroundFormulas());
        boolean consistent = doConsistencyTest(getSolver());
        //removeAxioms(getBackgroundFormulas(), getOntology());
        //removeAxioms(getFormularCache(), getOntology());
        stop();
        if (logger.isTraceEnabled())
            logger.trace(getOntology().getOntologyID() + " is consistent: " + consistent);
        return consistent;
    }

    protected boolean doConsistencyTest(OWLReasoner reasoner) {
        boolean consistent, coherent = true;
        //if (useCache)
        //    verifyCache(ontology.getLogicalAxioms());
        start("Reasoner sync ");
        if (BUFFERED_SOLVER) reasoner.flush();
        stop();
        start("Consistency test");
        incrementConsistencyChecks();
        long timeCons = System.currentTimeMillis();
        consistent = reasoner.isConsistent();
        addConsistencyTime(System.currentTimeMillis() - timeCons);
        stop();
        start("Coherency test");
        if (!isReduceToUnsat() && consistent) {
            coherent = checkCoherency(reasoner);
        }
        stop();
        consistent = consistent && coherent;
        if (consistent) {
            if (checkTestsConsistency()) return false;
        }
        
        //if (useCache && consistent)
        //    updateCache(ontology.getLogicalAxioms());
        return consistent;
    }

    private void updateCache(Set<OWLLogicalAxiom> ax) {
        int count = 0;
        Collections.sort(getCache());
        for (Iterator<CacheEntry> it = getCache().iterator();it.hasNext();)
        {
            CacheEntry saved = it.next();
            if (count >= threshold) {
                it.remove();
                continue;
            }
            if (saved.size() < ax.size() && ax.containsAll(saved.set))
                it.remove();
            else
                count++;
        }
        getCache().add(new CacheEntry(ax));
    }

    private boolean verifyCache(Set<OWLLogicalAxiom> ax) {
        for (CacheEntry saved : getCache())
        {            
            if (saved.size() >= ax.size() && saved.containsAll(ax))
                return true;               
        }
        return false;
    }

    private boolean checkCoherency(OWLReasoner reasoner) {
        reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);
        return reasoner.getBottomClassNode().isSingleton();
    }

    private boolean checkTestsConsistency() {
        OWLReasoner solver = getSolver();
        for (Set<OWLLogicalAxiom> test : getKnowledgeBase().getNegativeTests()) {
            if (!solver.isEntailed(test)) {
                return true;
            }
        }

        for (Set<OWLLogicalAxiom> test : getKnowledgeBase().getNonentailedTests()) {
            if (test != null && solver.isEntailed(test)) {
                return true;
            }
        }
        return false;
    }

    public OWLReasoner getSolver() {
        return (OWLReasoner) super.getSolver();

    }


    public boolean isEntailed(Set<OWLLogicalAxiom> test) {
        start("Consistency + entailment");
        //updateAxioms(getOntology(), getFormularCache());
        OWLReasoner solver = getSolver();
        if (BUFFERED_SOLVER) solver.flush();
        if (!solver.isConsistent())
            return false;
        //solver.precomputeInferences(InferenceType.CLASS_HIERARCHY);
        boolean res = solver.isEntailed(test);
        //removeAxioms(getFormularCache(), getOntology());
        //if (BUFFERED_SOLVER) solver.flush();
        stop();
        return res;
    }

    public void updateAxioms(OWLOntology ontology, Set<OWLLogicalAxiom>... axioms) {
        Set<OWLLogicalAxiom> add = new HashSet<OWLLogicalAxiom>();
        Set<OWLLogicalAxiom> remove = new HashSet<OWLLogicalAxiom>();

        for (Set<OWLLogicalAxiom> axiomset : axioms) {
            for (OWLLogicalAxiom axiom : axiomset) {
                if (axiom != null && !ontology.containsAxiom(axiom))
                    add.add(axiom);
            }
        }

        for (OWLLogicalAxiom axiom : ontology.getLogicalAxioms()) {
            boolean rem = true;
            for (Set<OWLLogicalAxiom> axiomset : axioms)
                if (axiomset.contains(axiom))
                    rem = false;
            if (rem)
                remove.add(axiom);
        }
        if (!add.isEmpty())
            getOwlOntologyManager().addAxioms(ontology, add);
        if (!remove.isEmpty())
            getOwlOntologyManager().removeAxioms(ontology, remove);
    }


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

    protected OWLNegateAxiom getNegationVisitor() {
        return vis;
    }

    public boolean diagnosisEntailsWithoutEntailedTC(AxiomSet<OWLLogicalAxiom> hs, Set<OWLLogicalAxiom> ent) {
        // cleanup stack
        Collection<OWLLogicalAxiom> stack = getReasoner().getFormularCache();
        getReasoner().clearFormularCache();

        updateAxioms(ontology, getKnowledgeBase().getBackgroundFormulas(), setminus(getKnowledgeBase().getFaultyFormulas(), hs));

        boolean res = isEntailed(new LinkedHashSet<OWLLogicalAxiom>(ent));

        // restore the state of the theory prior to the test
        getReasoner().clearFormularCache();
        //updateAxioms(getOntology(), logicalAxioms);
        getReasoner().addFormularsToCache(stack);
        return res;
    }

    // A
    public boolean diagnosisConsistent(AxiomSet<OWLLogicalAxiom> hs, Set<OWLLogicalAxiom> ent) {
        // cleanup stack
        Collection<OWLLogicalAxiom> stack = getReasoner().getFormularCache();
        getReasoner().clearFormularCache();
        // cleanup ontology
        //Set<OWLLogicalAxiom> logicalAxioms = getOntology().getLogicalAxioms();
        //removeAxioms(logicalAxioms, getOntology());

        // add entailed test cases to simulate extension EX
        for (Set<OWLLogicalAxiom> test : getKnowledgeBase().getEntailedTests()) {
            getReasoner().addFormularsToCache(test);
        }
        // add axioms to the ontology
        getReasoner().addFormularsToCache(setminus(getKnowledgeBase().getFaultyFormulas(), hs));
        getReasoner().addFormularsToCache(getKnowledgeBase().getBackgroundFormulas());
        getReasoner().addFormularsToCache(ent);
        //addAxioms(getOriginalOntology().getLogicalAxioms(), getOntology());
        //removeAxioms(hs, getOntology());
        //addAxioms(ent, getOntology());
        //addAxioms(getBackgroundFormulas(), getOntology());

        boolean res = verifyConsistency();

        // restore the state of the theory prior to the test
        getReasoner().clearFormularCache();
        //removeAxioms(ent, getOntology());
        //removeAxioms(getBackgroundFormulas(), getOntology());
        //removeAxioms(getOriginalOntology().getLogicalAxioms(), getOntology());
        //addAxioms(logicalAxioms, getOntology());
        //updateAxioms(getOntology(), logicalAxioms);
        getReasoner().addFormularsToCache(stack);
        return res;
    }

    //B
    public boolean diagnosisEntails(AxiomSet<OWLLogicalAxiom> hs, Set<OWLLogicalAxiom> ent) {
        // cleanup stack
        Collection<OWLLogicalAxiom> stack = getReasoner().getFormularCache();
        getReasoner().clearFormularCache();

        updateAxioms(ontology, flatten(getKnowledgeBase().getPositiveTests()), flatten(getKnowledgeBase().getEntailedTests()),
                getKnowledgeBase().getBackgroundFormulas(), setminus(getKnowledgeBase().getFaultyFormulas(), hs));

        boolean res = isEntailed(new LinkedHashSet<OWLLogicalAxiom>(ent));

        // restore the state of the theory prior to the test
        getReasoner().clearFormularCache();
        //updateAxioms(getOntology(), logicalAxioms);
        getReasoner().addFormularsToCache(stack);
        return res;
    }

    private Set<OWLLogicalAxiom> flatten(Collection<Set<OWLLogicalAxiom>> collection) {
        Set<OWLLogicalAxiom> ax = new LinkedHashSet<OWLLogicalAxiom>();
        for (Set<OWLLogicalAxiom> ps : collection)
            ax.addAll(ps);
        return ax;
    }

    public boolean diagnosisEntails(AxiomSet<OWLLogicalAxiom> hs, Set<OWLLogicalAxiom> ent, Set<OWLLogicalAxiom> axioms) {
        // cleanup stack
        Collection<OWLLogicalAxiom> stack = getReasoner().getFormularCache();
        getReasoner().clearFormularCache();
        // cleanup ontology
        //Set<OWLLogicalAxiom> logicalAxioms = getOntology().getLogicalAxioms();
        //removeAxioms(logicalAxioms, getOntology());

        // add entailed test cases to simulate extension EX
        for (Set<OWLLogicalAxiom> test : getKnowledgeBase().getEntailedTests()) {
            getReasoner().addFormularsToCache(test);
        }
        // add axioms to the ontology
        getReasoner().addFormularsToCache(setminus(getOriginalOntology().getLogicalAxioms(), hs));
        getReasoner().addFormularsToCache(getKnowledgeBase().getBackgroundFormulas());
        getReasoner().addFormularsToCache(axioms);
        //removeAxioms(hs, getOntology());
        //addAxioms(, getOntology());

        boolean res = isEntailed(new LinkedHashSet<OWLLogicalAxiom>(ent));

        // restore the state of the theory prior to the test
        getReasoner().clearFormularCache();
        //updateAxioms(getOntology(), logicalAxioms);
        getReasoner().addFormularsToCache(stack);
        return res;
    }

    protected Set<OWLLogicalAxiom> setminus(Set<OWLLogicalAxiom> logicalAxioms, Set<OWLLogicalAxiom> hs) {
        Set<OWLLogicalAxiom> res = new LinkedHashSet<OWLLogicalAxiom>(logicalAxioms);
        res.removeAll(hs);
        return res;
    }

    public boolean diagnosisConsistentWithoutEntailedTc(AxiomSet<OWLLogicalAxiom> hs, Set<OWLLogicalAxiom> ent) {
        // cleanup stack
        Collection<OWLLogicalAxiom> stack = getReasoner().getFormularCache();
        getReasoner().clearFormularCache();

        // add axioms to the ontology
        getReasoner().addFormularsToCache(setminus(getKnowledgeBase().getFaultyFormulas(), hs));
        getReasoner().addFormularsToCache(getKnowledgeBase().getBackgroundFormulas());
        getReasoner().addFormularsToCache(ent);
        //addAxioms(getOriginalOntology().getLogicalAxioms(), getOntology());
        //removeAxioms(hs, getOntology());
        //addAxioms(ent, getOntology());
        //addAxioms(getBackgroundFormulas(), getOntology());

        boolean res = verifyConsistency();

        // restore the state of the theory prior to the test
        getReasoner().clearFormularCache();
        //removeAxioms(ent, getOntology());
        //removeAxioms(getBackgroundFormulas(), getOntology());
        //removeAxioms(getOriginalOntology().getLogicalAxioms(), getOntology());
        //addAxioms(logicalAxioms, getOntology());
        //updateAxioms(getOntology(), logicalAxioms);
        getReasoner().addFormularsToCache(stack);
        return res;
    }



    public boolean supportEntailments() {
        return true;
    }

    public boolean diagnosisConsistent(AxiomSet<OWLLogicalAxiom> hs, Set<OWLLogicalAxiom> ent, Set<OWLLogicalAxiom> axioms) {
        // cleanup stack
        Collection<OWLLogicalAxiom> stack = getReasoner().getFormularCache();
        getReasoner().clearFormularCache();
        // cleanup ontology
        //Set<OWLLogicalAxiom> logicalAxioms = getOntology().getLogicalAxioms();
        //removeAxioms(logicalAxioms, getOntology());

        // add entailed test cases to simulate extension EX
        for (Set<OWLLogicalAxiom> test : getKnowledgeBase().getEntailedTests()) {
            getReasoner().addFormularsToCache(test);
        }
        // add axioms to the ontology
        getReasoner().addFormularsToCache(setminus(getKnowledgeBase().getFaultyFormulas(), hs));
        getReasoner().addFormularsToCache(getKnowledgeBase().getBackgroundFormulas());
        getReasoner().addFormularsToCache(axioms);
        getReasoner().addFormularsToCache(ent);
        //addAxioms(getOriginalOntology().getLogicalAxioms(), getOntology());
        //removeAxioms(hs, getOntology());
        //addAxioms(ent, getOntology());
        //addAxioms(getBackgroundFormulas(), getOntology());

        boolean res = verifyConsistency();

        // restore the state of the theory prior to the test
        getReasoner().clearFormularCache();
        //removeAxioms(ent, getOntology());
        //removeAxioms(getBackgroundFormulas(), getOntology());
        //removeAxioms(getOriginalOntology().getLogicalAxioms(), getOntology());
        //addAxioms(logicalAxioms, getOntology());
        //updateAxioms(getOntology(), logicalAxioms);
        getReasoner().addFormularsToCache(stack);
        return res;
    }

    public final Set<OWLLogicalAxiom> getEntailments(Set<OWLLogicalAxiom> hittingSet) throws SolverException {

        OWLReasoner reasoner = getSolver();

        Set<OWLLogicalAxiom> axioms = setminus(getKnowledgeBase().getFaultyFormulas(), hittingSet);
        Collection<OWLLogicalAxiom> stack = getReasoner().getFormularCache();
        getReasoner().clearFormularCache();

        getReasoner().addFormularsToCache(axioms);
        getReasoner().addFormularsToCache(getKnowledgeBase().getBackgroundFormulas());

        if (!verifyConsistency()) {
            getReasoner().clearFormularCache();
            getReasoner().addFormularsToCache(stack);
            return null;
        }

        Set<OWLLogicalAxiom> entailments = extractEntailments(reasoner, getOwlOntologyManager());

        getReasoner().clearFormularCache();
        getReasoner().addFormularsToCache(stack);
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

    protected Set<OWLLogicalAxiom> extractEntailments(OWLReasoner reasoner, OWLOntologyManager manager) {
        List<InferredAxiomGenerator<? extends OWLLogicalAxiom>> axiomGenerators =
                new LinkedList<InferredAxiomGenerator<? extends OWLLogicalAxiom>>();

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

        /*
        axiomGenerators.add(new InferredSubClassAxiomGenerator());
        axiomGenerators.add(new InferredEquivalentClassAxiomGenerator());
        axiomGenerators.add(new InferredClassAssertionAxiomGenerator());
        axiomGenerators.add(new InferredPropertyAssertionGenerator());
        /*

           // axiomGenerators.add(new InferredDisjointClassesAxiomGenerator());




            axiomGenerators.add(new InferredSubObjectPropertyAxiomGenerator());
              axiomGenerators.add(new InferredSubDataPropertyAxiomGenerator());

              axiomGenerators.add(new InferredEquivalentDataPropertiesAxiomGenerator());
              axiomGenerators.add(new InferredEquivalentObjectPropertyAxiomGenerator());
              axiomGenerators.add(new InferredInverseObjectPropertiesAxiomGenerator());
              axiomGenerators.add(new InferredObjectPropertyCharacteristicAxiomGenerator());
              axiomGenerators.add(new InferredSubDataPropertyAxiomGenerator());
              axiomGenerators.add(new InferredSubObjectPropertyAxiomGenerator());
              axiomGenerators.add(new InferredDataPropertyCharacteristicAxiomGenerator());
        */

        InferenceType[] infType = new InferenceType[]{InferenceType.CLASS_HIERARCHY, InferenceType.CLASS_ASSERTIONS,
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
        return entailments;
    }

    /*private void addDisjoint(OWLReasoner reasoner, OWLOntologyManager manager, Set<OWLLogicalAxiom> entailments) {
        OWLDataFactory dataFactory = manager.getOWLDataFactory();
        for (OWLClass cls : getOntology().getClassesInSignature()) {
            for (OWLClass dis : ((Reasoner) reasoner).getDisjointClasses(cls).getFlattened()) {
                OWLLogicalAxiom ax = dataFactory.getOWLDisjointClassesAxiom(cls, dis);
                if (includeTrivialEntailments || (!getOntology().containsAxiom(ax) && !ax.getClassesInSignature().contains(TOP_CLASS)))
                    entailments.add(ax);
            }
        }*/
    //}

    private void addSubclass(OWLReasoner reasoner, OWLOntologyManager manager, Set<OWLLogicalAxiom> entailments) {
        OWLDataFactory dataFactory = manager.getOWLDataFactory();
        for (OWLClass cls : getOntology().getClassesInSignature()) {
            for (OWLClass dis : reasoner.getSubClasses(cls, true).getFlattened()) {
                OWLLogicalAxiom ax = dataFactory.getOWLSubClassOfAxiom(dis, cls);
                if (includeTrivialEntailments || (!getOntology().containsAxiom(ax) && !ax.getClassesInSignature().contains(TOP_CLASS)))
                    entailments.add(ax);
            }
            ;
        }
    }

    //

}
