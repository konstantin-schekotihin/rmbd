package at.ainf.owlapi3.performance;

import at.ainf.diagnosis.model.InconsistentTheoryException;
import at.ainf.diagnosis.model.SolverException;
import at.ainf.diagnosis.quickxplain.QuickXplain;
import at.ainf.diagnosis.storage.FormulaSet;
import at.ainf.diagnosis.storage.FormulaSetImpl;
import at.ainf.diagnosis.tree.HsTreeSearch;
import at.ainf.diagnosis.tree.SimpleCostsEstimator;
import at.ainf.diagnosis.tree.TreeSearch;
import at.ainf.diagnosis.tree.exceptions.NoConflictException;
import at.ainf.diagnosis.tree.searchstrategy.UniformCostSearchStrategy;
import at.ainf.owlapi3.base.CalculateDiagnoses;
import at.ainf.owlapi3.base.OAEI11ConferenceSession;
import at.ainf.owlapi3.base.SimulatedSession;
import at.ainf.owlapi3.base.tools.TableList;
import at.ainf.owlapi3.costestimation.OWLAxiomCostsEstimator;
import at.ainf.owlapi3.costestimation.OWLAxiomKeywordCostsEstimator;
import at.ainf.owlapi3.model.OWLIncoherencyExtractor;
import at.ainf.owlapi3.model.OWLTheory;
import org.junit.Test;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.model.OWLLogicalAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.util.*;

import static at.ainf.owlapi3.util.SetUtils.createIntersection;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by IntelliJ IDEA.
 * User: mjoszt
 * Date: 07.12.2014
 * Time: 10:15
 */

public class ConflictTreeTest extends OntologyTests {

    private static Logger logger = LoggerFactory.getLogger(ConflictTreeTest.class.getName());
    private OWLTheory ctTheory;
    private OWLTheory origTheory;

    /**
     * Set of diagnoses found during search
     */
    private Set<OWLLogicalAxiom> foundDiagnoses;

//    static Set<Searcher<OWLLogicalAxiom>> searchers = new LinkedHashSet<Searcher<OWLLogicalAxiom>>();


    /**
     * Sets up theory and search
     * @return search
     * @throws SolverException
     * @throws InconsistentTheoryException
     * @throws NoConflictException
     * @param strOntologyFile
     */
    private TreeSearch<FormulaSet<OWLLogicalAxiom>, OWLLogicalAxiom> testSetup(String strOntologyFile) throws SolverException, InconsistentTheoryException, NoConflictException {
        //create theory
        OWLOntology ontology = getOntologySimple(strOntologyFile);
        ctTheory = getSimpleTheory(ontology, false);
        //set query parameters
//        ctTheory.setIncludeSubClassOfAxioms(true);
//        ctTheory.setIncludeClassAssertionAxioms(true);
//        ctTheory.setIncludeEquivalentClassAxioms(true);
//        ctTheory.setIncludePropertyAssertAxioms(true);
//        ctTheory.setIncludeDisjointClassAxioms(true);


        origTheory = (OWLTheory) ctTheory.copy();

        //setup search
        TreeSearch<FormulaSet<OWLLogicalAxiom>,OWLLogicalAxiom> search = new HsTreeSearch<FormulaSet<OWLLogicalAxiom>, OWLLogicalAxiom>();
        search.setSearchStrategy(new UniformCostSearchStrategy<OWLLogicalAxiom>());
        search.setSearcher(new QuickXplain<OWLLogicalAxiom>());
        search.setSearchable(ctTheory);
        search.setCostsEstimator(new OWLAxiomKeywordCostsEstimator(ctTheory));

        //set for all found diagnoses during search
        foundDiagnoses = new LinkedHashSet<OWLLogicalAxiom>();

        return search;
    }

    @Test
    /**
     * Testing the ontologies which are throwing the NoSuchElementException
     */
    public void testThrowingException() throws SolverException, InconsistentTheoryException, OWLOntologyCreationException {
        logger.info("doUnsolveableTest_with_2015_ontologies");

        String matchingsDir = "oaei11conference/matchings/";
        String ontologyDir = "oaei11conference/ontology";

        File[] f = getMappingFiles(matchingsDir, "incoherent", "error_throwing.txt");

        Set<File> files = new LinkedHashSet<File>();
        Map<File, String> map = new HashMap<File, String>();
        for (File file : f) {
            files.add(file);
            map.put(file, "incoherent");
        }
        //each of the following configurations is throwing an exception
        //runOaeiConferenceTests(matchingsDir, ontologyDir, files, map, 1);   // aroma-cmt-confof.rdf
        //runOaeiConferenceTests(matchingsDir, ontologyDir, files, map, 2); // aroma-cmt-ekaw.rdf
        runOaeiConferenceTests(matchingsDir, ontologyDir, files, map, 3); // aroma-conference-confof.rdf
    }

    @Test
    /**
     * Tests ConflictTreeSimulatedSession with unsolvable ontologies, target diagnoses are calculated
     */
    public void doUnsolvableOAEIConferenceTest() throws SolverException, InconsistentTheoryException, OWLOntologyCreationException {
        logger.info("doUnsolveableTest_with_2015_ontologies");

        String matchingsDir = "oaei11conference/matchings/";
        String ontologyDir = "oaei11conference/ontology";

        File[] f = getMappingFiles(matchingsDir, "incoherent", "incoherent_2015.txt");
        File[] f2 = getMappingFiles(matchingsDir, "inconsistent", "inconsistent_2015.txt");

        Set<File> files = new LinkedHashSet<File>();
        Map<File, String> map = new HashMap<File, String>();
        for (File file : f) {
            files.add(file);
            map.put(file, "incoherent");
        }
        for (File file : f2) {
            files.add(file);
            map.put(file, "inconsistent");
        }

        runOaeiConferenceTests(matchingsDir, ontologyDir, files, map);
        /*
        result: 2015-02-26-T-10-58-50
        junit.framework.AssertionFailedError
            at at.ainf.owlapi3.performance.ConflictTreeTest.computeHSShortLog(ConflictTreeTest.java:562)
            at at.ainf.owlapi3.performance.ConflictTreeSession.search(ConflictTreeSession.java:109)
            at at.ainf.owlapi3.performance.ConflictTreeSession.search(ConflictTreeSession.java:69)
            at at.ainf.owlapi3.performance.ConflictTreeTest.runOaeiConferenceTests(ConflictTreeTest.java:315)
            at at.ainf.owlapi3.performance.ConflictTreeTest.doUnsolvableOAEIConferenceTest(ConflictTreeTest.java:174)

            and

        result: 2015-02-26-T-12-13-55
        at.ainf.diagnosis.model.InconsistentTheoryException: Background theory or test cases are inconsistent! Finding conflicts is impossible!
            at at.ainf.diagnosis.quickxplain.BaseQuickXplain.verifyKnowledgeBase(BaseQuickXplain.java:173)
            at at.ainf.diagnosis.quickxplain.MultiQuickXplain.search(MultiQuickXplain.java:107)
            at at.ainf.diagnosis.quickxplain.BaseQuickXplain.search(BaseQuickXplain.java:116)
            at at.ainf.owlapi3.performance.ConflictTreeSession.computeNConflictsAtTime(ConflictTreeSession.java:193)
            at at.ainf.owlapi3.performance.ConflictTreeSession.search(ConflictTreeSession.java:83)
            at at.ainf.owlapi3.performance.ConflictTreeSession.search(ConflictTreeSession.java:69)
            at at.ainf.owlapi3.performance.ConflictTreeTest.runOaeiConferenceTests(ConflictTreeTest.java:353)
            at at.ainf.owlapi3.performance.ConflictTreeTest.doUnsolvableOAEIConferenceTest(ConflictTreeTest.java:219)

        */
    }


    @Test
    /**
     * Tests ConflictTreeSimulatedSession with solvable ontologies from OAEI11 Conference, target diagnoses are calculated
     */
    public void doOAEIConferenceTest() throws SolverException, InconsistentTheoryException, OWLOntologyCreationException {
        logger.info("doOAEIConferenceTest");

        String matchingsDir = "oaei11conference/matchings/";
        String ontologyDir = "oaei11conference/ontology";

        File[] f = getMappingFiles(matchingsDir, "incoherent", "includedIncoher.txt");
        File[] f2 = getMappingFiles(matchingsDir, "inconsistent", "included.txt");

        Set<File> files = new LinkedHashSet<File>();
        Map<File, String> map = new HashMap<File, String>();
        for (File file : f) {
            files.add(file);
            map.put(file, "incoherent");
        }
        for (File file : f2) {
            files.add(file);
            map.put(file, "inconsistent");
        }

        runOaeiConferenceTests(matchingsDir, ontologyDir, files, map);
        /*
        result: 2015-02-26-T-09-29-38
        java.lang.OutOfMemoryError: GC overhead limit exceeded
            at java.util.TreeMap.keyIterator(TreeMap.java:1106)
            at java.util.TreeMap$KeySet.iterator(TreeMap.java:1119)
            at java.util.TreeSet.iterator(TreeSet.java:181)
            at java.util.Collections$UnmodifiableCollection$1.<init>(Collections.java:1039)
            at java.util.Collections$UnmodifiableCollection.iterator(Collections.java:1038)
            at at.ainf.diagnosis.storage.FormulaSetImpl.iterator(FormulaSetImpl.java:123)
            at at.ainf.diagnosis.tree.AbstractTreeSearch.intersectsWith(AbstractTreeSearch.java:712)
            at at.ainf.diagnosis.tree.AbstractTreeSearch.canReuseConflict(AbstractTreeSearch.java:697)
            at at.ainf.diagnosis.tree.AbstractTreeSearch.processNode(AbstractTreeSearch.java:363)
            at at.ainf.diagnosis.tree.AbstractTreeSearch.processOpenNodes(AbstractTreeSearch.java:311)
            at at.ainf.diagnosis.tree.AbstractTreeSearch.searchDiagnoses(AbstractTreeSearch.java:268)
            at at.ainf.diagnosis.tree.AbstractTreeSearch.start(AbstractTreeSearch.java:202)
            at at.ainf.owlapi3.base.OAEI11ConferenceSession.getRandomDiagSet(OAEI11ConferenceSession.java:151)
            at at.ainf.owlapi3.performance.OAEI11ConferenceTests.runOaeiConferenceTests(OAEI11ConferenceTests.java:151)
            at at.ainf.owlapi3.performance.OAEI11ConferenceTests.doTestsOAEIConference(OAEI11ConferenceTests.java:131)
        */

    }

    private void runOaeiConferenceTests(String matchingsDir, String ontologyDir, Set<File> files, Map<File, String> map) throws SolverException, InconsistentTheoryException, OWLOntologyCreationException {
        runOaeiConferenceTests(matchingsDir, ontologyDir, files, map, 0);
    }

    /**
     * Runs ConflictTreeSimulatedSession with calculated diagnoses
     * @param matchingsDir
     * @param ontologyDir
     * @param files
     * @param map
     * @throws SolverException
     * @throws InconsistentTheoryException
     * @throws OWLOntologyCreationException
     */
    private void runOaeiConferenceTests(String matchingsDir, String ontologyDir, Set<File> files, Map<File, String> map, int numberOfConflicts) throws SolverException, InconsistentTheoryException, OWLOntologyCreationException {
        OAEI11ConferenceSession conferenceSession = new OAEI11ConferenceSession();

        QSSType[] qssTypes = new QSSType[]{QSSType.MINSCORE};
        //QSSType[] qssTypes = new QSSType[]{QSSType.MINSCORE, QSSType.SPLITINHALF, QSSType.DYNAMICRISK};

        TargetSource targetSource = TargetSource.FROM_30_DIAGS;
        for (File file : files) {
            logger.info("processing " + file.getName());
            String out = "STAT, " + file;

            // file parameter
            String fileName = file.getName();
            StringTokenizer t = new StringTokenizer(fileName, "-");
            String matcher = t.nextToken();
            String o1 = t.nextToken();
            String o2 = t.nextToken();
            o2 = o2.substring(0, o2.length() - 4);
            String n = file.getName().substring(0, file.getName().length() - 4);

            Set<FormulaSet<OWLLogicalAxiom>> targetDgSet = getRandomDiagSet(matchingsDir, ontologyDir, map, file, o1, o2, n, conferenceSession);
            logger.info("number of found diagnoses (max. 30): " + targetDgSet.size());
            /*
            TODO: for each ontology always the same "randomly" chosen diagnosis is calculated, dependent
                from the size of the targetDgSet -> change to a real random selection
             */
            Random random = new Random(12311);
            int randomNr = conferenceSession.chooseRandomNum(targetDgSet, random);
            Set<OWLLogicalAxiom> targetDg = conferenceSession.chooseRandomDiag(targetDgSet,file,randomNr);

            OWLOntology inconsOntology = loadOntology(matchingsDir, ontologyDir, map, conferenceSession, file, o1, o2, n);
//                OWLTheory faultyTheory = getExtendTheory(inconsOntology, false);
            OWLTheory faultyTheory = loadTheory(inconsOntology, ontologyDir, o1, o2);
            Set<OWLLogicalAxiom> rem = validateMinimality(targetDg, faultyTheory);
            if(!rem.isEmpty()) {
                targetDg.removeAll(rem);
                assertTrue(validateMinimality(targetDg, faultyTheory).isEmpty());
            }

            Map<QSSType, DurationStat> ctTimes = new HashMap<QSSType, DurationStat>();
            Map<QSSType, List<Double>> ctQueries = new HashMap<QSSType, List<Double>>();

            for (QSSType qssType : qssTypes) {
                String message = "act," + file.getName() + "," + map.get(file) + "," + targetSource
                        + "," + qssType + "," + randomNr;
                long preprocessModulExtract = System.currentTimeMillis();
                OWLOntology ontology = loadOntology(matchingsDir, ontologyDir, map, conferenceSession, file, o1, o2, n);
                ctTheory = loadTheory(ontology, ontologyDir, o1, o2);
                preprocessModulExtract = System.currentTimeMillis() - preprocessModulExtract;
                message += "," + preprocessModulExtract;

                //Define Treesearch here
                TreeSearch<FormulaSet<OWLLogicalAxiom>,OWLLogicalAxiom> search = getUniformCostSearch(ctTheory, false);

                Map<OWLLogicalAxiom, BigDecimal> map1 = conferenceSession.readRdfMapping(matchingsDir + map.get(file), n + ".rdf");
                OWLAxiomCostsEstimator es = new OWLAxiomCostsEstimator(ctTheory, map1);
                search.setCostsEstimator(es);
                search.reset();

                //set for all found diagnoses during search
                foundDiagnoses = new LinkedHashSet<OWLLogicalAxiom>();

                ctTimes.put(qssType, new DurationStat());
                ctQueries.put(qssType, new LinkedList<Double>());

                //calculation part
                ConflictTreeSession conflictTreeSearch = new ConflictTreeSession(this, ctTheory, search);
                if (numberOfConflicts > 0) {
                    conflictTreeSearch.setMaximumNumberOfConflicts(numberOfConflicts);
                }
                conflictTreeSearch.setOutputString(out);
                conflictTreeSearch.setMessageString(message);
                FormulaSet<OWLLogicalAxiom> targetD = new FormulaSetImpl<OWLLogicalAxiom>(new BigDecimal(1), targetDg, null);
                long completeTime = conflictTreeSearch.search(targetD, ctQueries, qssType, true);
                ctTimes.get(qssType).add(completeTime);
                out += conflictTreeSearch.getOutputString();

                foundDiagnoses = conflictTreeSearch.getDiagnosis();
                logger.info("targetD: " + targetD.size() + ", " + CalculateDiagnoses.renderAxioms(targetD));
                logger.info("foundD: " + foundDiagnoses.size() + ", " + CalculateDiagnoses.renderAxioms(foundDiagnoses));



                faultyTheory.getReasoner().addFormulasToCache(faultyTheory.getKnowledgeBase().getFaultyFormulas());
                assertFalse(faultyTheory.verifyConsistency());
                Set<OWLLogicalAxiom> removedFound = validateMinimality(foundDiagnoses, faultyTheory);
                // assertTrue(removed.isEmpty());
                Set<OWLLogicalAxiom> removedTarget  = validateMinimality(targetD, faultyTheory);
                assertTrue(removedTarget.isEmpty());

                assertTrue(targetD.containsAll(foundDiagnoses));
                //TODO uncomment later
                //assertTrue(foundDiagnoses.containsAll(targetD));

                resetTheoryTests(ctTheory);
                search.reset();
            }
            logger.info(out);
        }
    }

    private Set<OWLLogicalAxiom> validateMinimality(Set<OWLLogicalAxiom> diagnosis, OWLTheory faultyTheory) throws SolverException {
        Set<OWLLogicalAxiom> rem = new HashSet<OWLLogicalAxiom>();
        faultyTheory.reset();
        assertTrue(faultyTheory.testDiagnosis(diagnosis));
        for (OWLLogicalAxiom o : diagnosis) {
            Set<OWLLogicalAxiom> cp = new HashSet<OWLLogicalAxiom>(diagnosis);
            cp.remove(o);
            faultyTheory.reset();
            if (faultyTheory.testDiagnosis(cp))
                rem.add(o);
        }
        return rem;
    }

    public Set<FormulaSet<OWLLogicalAxiom>> getRandomDiagSet(String matchingsDir, String ontologyDir, Map<File, String> map, File file, String o1, String o2, String n, OAEI11ConferenceSession conferenceSession) throws SolverException, InconsistentTheoryException {
        OWLOntology preOntology = loadOntology(matchingsDir, ontologyDir, map, conferenceSession, file, o1, o2, n);
        OWLTheory preTheory = loadTheory(preOntology, ontologyDir, o1, o2);
        TreeSearch<FormulaSet<OWLLogicalAxiom>,OWLLogicalAxiom> search = getUniformCostSearch(preTheory, false);

        Map<OWLLogicalAxiom, BigDecimal> map1 = conferenceSession.readRdfMapping(matchingsDir + map.get(file), n + ".rdf");
        OWLAxiomCostsEstimator es = new OWLAxiomCostsEstimator(preTheory, map1);
        search.setCostsEstimator(es);
        preTheory.getReasoner().addFormulasToCache(preTheory.getKnowledgeBase().getFaultyFormulas());
        assertFalse(preTheory.verifyConsistency());
        search.reset();

        try {
            search.setMaxDiagnosesNumber(30);
            search.start();
        } catch (NoConflictException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        Set<FormulaSet<OWLLogicalAxiom>> diagnoses = new TreeSet<FormulaSet<OWLLogicalAxiom>>(search.getDiagnoses());

        search.reset();

        return diagnoses;
    }



    private OWLTheory loadTheory(OWLOntology ontology, String ontologyDir, String o1, String o2) {
        OWLTheory theory = getExtendTheory(ontology, false);

        LinkedHashSet<OWLLogicalAxiom> bx = new LinkedHashSet<OWLLogicalAxiom>();
        OWLOntology ontology1 = getOntologySimple(ontologyDir, o1 + ".owl");
        OWLOntology ontology2 = getOntologySimple(ontologyDir, o2 + ".owl");
        Set<OWLLogicalAxiom> onto1Axioms = createIntersection(ontology.getLogicalAxioms(), ontology1.getLogicalAxioms());
        Set<OWLLogicalAxiom> onto2Axioms = createIntersection(ontology.getLogicalAxioms(), ontology2.getLogicalAxioms());
        int modOnto1Size = onto1Axioms.size();
        int modOnto2Size = onto2Axioms.size();
        int modMappingSize = ontology.getLogicalAxioms().size() - modOnto1Size - modOnto2Size;
        bx.addAll(onto1Axioms);
        bx.addAll(onto2Axioms);
        theory.getKnowledgeBase().addBackgroundFormulas(bx);

        logger.info("ontology axiom sizes: " + modOnto1Size + "," + modOnto2Size + "," + modMappingSize);
        return theory;
    }

    private OWLOntology loadOntology(String matchingsDir, String ontologyDir, Map<File, String> map, OAEI11ConferenceSession conferenceSession, File file, String o1, String o2, String n) {
        OWLOntology merged = conferenceSession.getOntology(ontologyDir,
                o1, o2, matchingsDir + map.get(file), n + ".rdf");

        return new OWLIncoherencyExtractor(
                new Reasoner.ReasonerFactory()).getIncoherentPartAsOntology(merged);
    }

    //@Test
    /**
     * Tests ConflictTreeSimulatedSession with unsolvable ontologies, target diagnoses are from file
     */
    /*
    public void doUnsolvableOAEIConferenceFromFileTest() throws SolverException, InconsistentTheoryException, OWLOntologyCreationException {
        logger.info("doUnsolveableTest_with_2015_ontologies_from_file");

        String matchingsDir = "oaei11conference/matchings/";
        String ontologyDir = "oaei11conference/ontology";

        File[] f = getMappingFiles(matchingsDir, "incoherent", "incoherent_2015.txt");
        File[] f2 = getMappingFiles(matchingsDir, "inconsistent", "inconsistent_2015.txt");

        Set<File> files = new LinkedHashSet<File>();
        Map<File, String> map = new HashMap<File, String>();
        for (File file : f) {
            files.add(file);
            map.put(file, "incoherent");
        }
        for (File file : f2) {
            files.add(file);
            map.put(file, "inconsistent");
        }

        runOaeiConferenceTestsFromFile(matchingsDir, ontologyDir, files, map);
    }
    */

    /**
     * Runs ConflictTreeSimulatedSession with diagnoses from file
     * @param matchingsDir
     * @param ontologyDir
     * @param files
     * @param map
     * @throws SolverException
     * @throws InconsistentTheoryException
     * @throws OWLOntologyCreationException
     */
    /*
    private void runOaeiConferenceTestsFromFile(String matchingsDir, String ontologyDir, Set<File> files, Map<File, String> map) throws SolverException, InconsistentTheoryException, OWLOntologyCreationException {
        OAEI11ConferenceSession conferenceSession = new OAEI11ConferenceSession();

        QSSType[] qssTypes = new QSSType[]{QSSType.MINSCORE, QSSType.SPLITINHALF, QSSType.DYNAMICRISK};

        TargetSource targetSource  = TargetSource.FROM_FILE;
        for (File file : files) {
            logger.info("processing " + file.getName());
            String out = "STAT, " + file;

            Map<QSSType, DurationStat> ctTimes = new HashMap<QSSType, DurationStat>();
            Map<QSSType, List<Double>> ctQueries = new HashMap<QSSType, List<Double>>();

            for (QSSType qssType : qssTypes) {
                String fileName = file.getName();
                StringTokenizer t = new StringTokenizer(fileName, "-");
                String matcher = t.nextToken();

                String o1 = t.nextToken();
                String o2 = t.nextToken();
                o2 = o2.substring(0, o2.length() - 4);

                String n = file.getName().substring(0, file.getName().length() - 4);
                OWLOntology ontology = conferenceSession.getOntology(ontologyDir,
                        o1, o2, matchingsDir + map.get(file), n + ".rdf");

                Set<OWLLogicalAxiom> targetDg;
                ctTheory = getExtendTheory(ontology, false);
                origTheory = (OWLTheory) ctTheory.copy();
                TreeSearch<FormulaSet<OWLLogicalAxiom>,OWLLogicalAxiom> search = getUniformCostSearch(ctTheory, false);

                LinkedHashSet<OWLLogicalAxiom> bx = new LinkedHashSet<OWLLogicalAxiom>();
                OWLOntology ontology1 = getOntologySimple(ontologyDir, o1 + ".owl");
                OWLOntology ontology2 = getOntologySimple(ontologyDir, o2 + ".owl");
                Set<OWLLogicalAxiom> onto1Axioms = createIntersection(ontology.getLogicalAxioms(), ontology1.getLogicalAxioms());
                Set<OWLLogicalAxiom> onto2Axioms = createIntersection(ontology.getLogicalAxioms(), ontology2.getLogicalAxioms());
                int modOnto1Size = onto1Axioms.size();
                int modOnto2Size = onto2Axioms.size();
                int modMappingSize = ontology.getLogicalAxioms().size() - modOnto1Size - modOnto2Size;
                bx.addAll(onto1Axioms);
                bx.addAll(onto2Axioms);
                ctTheory.getKnowledgeBase().addBackgroundFormulas(bx);

                Map<OWLLogicalAxiom, BigDecimal> map1 = conferenceSession.readRdfMapping(matchingsDir + map.get(file), n + ".rdf");
                OWLAxiomCostsEstimator es = new OWLAxiomCostsEstimator(ctTheory, map1);
                search.setCostsEstimator(es);
                //Set<FormulaSet<OWLLogicalAxiom>> allD = new LinkedHashSet<FormulaSet<OWLLogicalAxiom>>(search.getDiagnoses());
                search.reset();
                targetDg = null;

                //TODO read targetDg from File | which file for our ontologies?

                //set for all found diagnoses during search
                foundDiagnoses = new LinkedHashSet<OWLLogicalAxiom>();

                ctTimes.put(qssType, new DurationStat());
                ctQueries.put(qssType, new LinkedList<Double>());

                String message = "act," + file.getName() + "," + map.get(file) + "," + targetSource
                        + "," + qssType + ", , ," + modOnto1Size + "," + modOnto2Size + "," + modMappingSize;

                //calculation part
                ConflictTreeSession conflictTreeSearch = new ConflictTreeSession(this, ctTheory, search);
                conflictTreeSearch.setOutputString(out);
                conflictTreeSearch.setMessageString(message);
                FormulaSet<OWLLogicalAxiom> targetD = new FormulaSetImpl<OWLLogicalAxiom>(new BigDecimal(1), targetDg, null);
                long completeTime = conflictTreeSearch.search(targetD, ctQueries, qssType, true);
                ctTimes.get(qssType).add(completeTime);
                out += conflictTreeSearch.getOutputString();

                foundDiagnoses = conflictTreeSearch.getDiagnosis();

                resetTheoryTests(ctTheory);
                search.reset();
            }
            logger.info(out);
        }
    }
    */


    @Test
    public void testCompareSearchMethods() throws SolverException, InconsistentTheoryException, NoConflictException, OWLOntologyCreationException {
        logger.info("NormalSimulatedSession compared to ConflictTreeSimulatedSession");

        TreeSearch<FormulaSet<OWLLogicalAxiom>, OWLLogicalAxiom> search = testSetup("ontologies/ecai2010.owl");
        Set<? extends FormulaSet<OWLLogicalAxiom>> diagnoses = getDiagnoses(search);

        //setup second search
        TreeSearch<FormulaSet<OWLLogicalAxiom>,OWLLogicalAxiom> ctSearch = new HsTreeSearch<FormulaSet<OWLLogicalAxiom>, OWLLogicalAxiom>();
        ctSearch.setSearchStrategy(new UniformCostSearchStrategy<OWLLogicalAxiom>());
        ctSearch.setSearcher(new QuickXplain<OWLLogicalAxiom>());
        ctSearch.setSearchable(ctTheory);
        ctSearch.setCostsEstimator(new OWLAxiomKeywordCostsEstimator(ctTheory));

        Map<QSSType, DurationStat> nTimes = new HashMap<QSSType, DurationStat>();
        Map<QSSType, List<Double>> nQueries = new HashMap<QSSType, List<Double>>();
        Map<QSSType, DurationStat> ctTimes = new HashMap<QSSType, DurationStat>();
        Map<QSSType, List<Double>> ctQueries = new HashMap<QSSType, List<Double>>();

        for (QSSType type : QSSType.values()) { //run for each scoring function
            logger.info("QSSType: " + type);

            //run normal simulated session
            logger.info("NormalSimulatedSession");
            nTimes.put(type, new DurationStat());
            nQueries.put(type, new LinkedList<Double>());
            for (FormulaSet<OWLLogicalAxiom> targetDiagnosis : diagnoses) { //run for each possible target diagnosis
                long completeTime = System.currentTimeMillis();

                computeHS(search, ctTheory, targetDiagnosis, nQueries.get(type), type);
                ctTheory.getKnowledgeBase().removeFormulas(targetDiagnosis);
                completeTime = System.currentTimeMillis() - completeTime;
                nTimes.get(type).add(completeTime);

                foundDiagnoses.addAll(targetDiagnosis);
                ctTheory.getReasoner().addFormulasToCache(ctTheory.getKnowledgeBase().getFaultyFormulas());
                assertTrue(ctTheory.verifyConsistency());
                ctTheory.reset();

                resetTheoryTests(ctTheory);
                search.reset();

            }
            logger.info("found Diagnoses: " + foundDiagnoses.size());
            logger.info("found Diagnosis: " + CalculateDiagnoses.renderAxioms(foundDiagnoses));
            logger.info("found all target diagnoses: " + (foundDiagnoses.size() > 0 && foundDiagnoses.containsAll(diagnoses)));
            // end (run normal simulated session)

            foundDiagnoses.clear();

            //run conflict tree simulated session
            logger.info("ConflictTreeSimulatedSession");
            ctTimes.put(type, new DurationStat());
            ctQueries.put(type, new LinkedList<Double>());
            for (FormulaSet<OWLLogicalAxiom> targetDiagnosis : diagnoses) { //run for each possible target diagnosis
                logger.info("targetD: " + CalculateDiagnoses.renderAxioms(targetDiagnosis));

                ConflictTreeSession conflictTreeSearch = new ConflictTreeSession(this, ctTheory, ctSearch);
                long completeTime = conflictTreeSearch.search(targetDiagnosis, ctQueries, type);
                ctTimes.get(type).add(completeTime);

                foundDiagnoses.addAll(conflictTreeSearch.getDiagnosis());
                ctTheory.getReasoner().addFormulasToCache(ctTheory.getKnowledgeBase().getFaultyFormulas());
                assertTrue(ctTheory.verifyConsistency());
                ctTheory.reset();

                resetTheoryTests(ctTheory);
                ctSearch.reset();
            }
            logger.info("found Diagnoses: " + foundDiagnoses.size());
            logger.info("found Diagnosis: " + CalculateDiagnoses.renderAxioms(foundDiagnoses));
            logger.info("found all target diagnoses: " + (foundDiagnoses.size() > 0 && foundDiagnoses.containsAll(diagnoses)));
            // end (run conflict tree simulated session)

            //print time statistics
            logStatistics(nQueries, nTimes, type, "normal");
            logStatistics(ctQueries, ctTimes, type, "treeSearch");

            foundDiagnoses.clear();
        }
    }


    @Test
    public void testNormalSimulatedSession() throws SolverException, InconsistentTheoryException, NoConflictException, OWLOntologyCreationException {
        logger.info("NormalSimulatedSession");

        TreeSearch<FormulaSet<OWLLogicalAxiom>, OWLLogicalAxiom> search = testSetup("ontologies/ecai2010.owl");
        Set<? extends FormulaSet<OWLLogicalAxiom>> diagnoses = getDiagnoses(search);

        Map<QSSType, DurationStat> nTimes = new HashMap<QSSType, DurationStat>();
        Map<QSSType, List<Double>> nQueries = new HashMap<QSSType, List<Double>>();

        for (QSSType type : QSSType.values()) { //run for each scoring function
            logger.info("QSSType: " + type);

            nTimes.put(type, new DurationStat());
            nQueries.put(type, new LinkedList<Double>());
            for (FormulaSet<OWLLogicalAxiom> targetDiagnosis : diagnoses) { //run for each possible target diagnosis
                logger.info("targetD: " + CalculateDiagnoses.renderAxioms(targetDiagnosis));
                long completeTime = System.currentTimeMillis();

                computeHS(search, ctTheory, targetDiagnosis, nQueries.get(type), type);
                ctTheory.getKnowledgeBase().removeFormulas(targetDiagnosis);
                completeTime = System.currentTimeMillis() - completeTime;
                nTimes.get(type).add(completeTime);

                foundDiagnoses.addAll(targetDiagnosis);
                ctTheory.getReasoner().addFormulasToCache(ctTheory.getKnowledgeBase().getFaultyFormulas());
                assertTrue(ctTheory.verifyConsistency());
                ctTheory.reset();

                resetTheoryTests(ctTheory);
                search.reset();
            }
            logStatistics(nQueries, nTimes, type, "normal");
            logger.info("found Diagnoses: " + foundDiagnoses.size());
            logger.info("found Diagnosis: " + CalculateDiagnoses.renderAxioms(foundDiagnoses));
            logger.info("found all target diagnoses: " + (foundDiagnoses.size() > 0 && foundDiagnoses.containsAll(diagnoses)));
        }
    }


    @Test
    public void testConflictTreeSimulatedSession() throws SolverException, InconsistentTheoryException, OWLOntologyCreationException, NoConflictException {
        logger.info("ConflictTreeSimulatedSession");

        TreeSearch<FormulaSet<OWLLogicalAxiom>, OWLLogicalAxiom> search = testSetup("ontologies/ecai2010.owl");
        Set<? extends FormulaSet<OWLLogicalAxiom>> diagnoses = getDiagnoses(search);

        Map<QSSType, DurationStat> ctTimes = new HashMap<QSSType, DurationStat>();
        Map<QSSType, List<Double>> ctQueries = new HashMap<QSSType, List<Double>>();

        for (QSSType type : QSSType.values()) { //run for each scoring function
            logger.info("QSSType: " + type);

            ctTimes.put(type, new DurationStat());
            ctQueries.put(type, new LinkedList<Double>());
            for (FormulaSet<OWLLogicalAxiom> targetDiagnosis : diagnoses) {
                logger.info("targetD: " + CalculateDiagnoses.renderAxioms(targetDiagnosis));

                ConflictTreeSession conflictTreeSearch = new ConflictTreeSession(this, ctTheory, search);
                long completeTime = conflictTreeSearch.search(targetDiagnosis, ctQueries, type);
                ctTimes.get(type).add(completeTime);

                foundDiagnoses.addAll(conflictTreeSearch.getDiagnosis());
                ctTheory.getReasoner().addFormulasToCache(ctTheory.getKnowledgeBase().getFaultyFormulas());
                assertTrue(ctTheory.verifyConsistency());
                ctTheory.reset();

                resetTheoryTests(ctTheory);
                search.reset();
            }
            logStatistics(ctQueries, ctTimes, type, "treeSearch");
            logger.info("found Diagnoses: " + foundDiagnoses.size());
            logger.info("found Diagnosis: " + CalculateDiagnoses.renderAxioms(foundDiagnoses));
            logger.info("found all target diagnoses: " + (foundDiagnoses.size() > 0 && foundDiagnoses.containsAll(diagnoses)));
        }
    }


    private Set<? extends FormulaSet<OWLLogicalAxiom>> getDiagnoses(TreeSearch<FormulaSet<OWLLogicalAxiom>, OWLLogicalAxiom> search) throws SolverException, InconsistentTheoryException, NoConflictException {
        //cost estimator
        SimpleCostsEstimator<OWLLogicalAxiom> es = new SimpleCostsEstimator<OWLLogicalAxiom>();
        search.setCostsEstimator(es);

        //searching diagnoses
        search.start();
        Set<? extends FormulaSet<OWLLogicalAxiom>> diagnoses = search.getDiagnoses();

        //resets history
        ctTheory.getKnowledgeBase().clearTestCases();
        ctTheory.getKnowledgeBase().clearTestsList();

        search.reset();

        return diagnoses;
    }

    /**
     * Logs the time and the number of needed queries for a specific QSS type
     * @param queries
     * @param times
     * @param type
     * @param statisticName
     */
    private void logStatistics(Map<QSSType, List<Double>> queries, Map<QSSType, DurationStat> times, QSSType type, String statisticName){
        List<Double> queriesOfType = queries.get(type);
        double res = 0;
        for (Double qs : queriesOfType) {
            res += qs;
        }
        logger.info(statisticName + " needed time " + type + ": " + getStringTime(times.get(type).getOverall()) +
                        ", max " + getStringTime(times.get(type).getMax()) +
                        ", min " + getStringTime(times.get(type).getMin()) +
                        ", avg2 " + getStringTime(times.get(type).getMean()) +
                        ", Queries max " + Collections.max(queries.get(type)) +
                        ", min " + Collections.min(queries.get(type)) +
                        ", avg2 " + res / queriesOfType.size()
        );

    }

    private void resetTheoryTests(OWLTheory theory) {
        theory.getKnowledgeBase().addFormulas(origTheory.getKnowledgeBase().getKnowledgeBase());
        theory.getKnowledgeBase().clearTestCases();
        theory.getKnowledgeBase().clearTestsList();
    }

    protected String computeHSShortLog(TreeSearch<FormulaSet<OWLLogicalAxiom>,OWLLogicalAxiom> search,
                                       OWLTheory theory, FormulaSet<OWLLogicalAxiom> diagnoses,
                                       List<Double> queries, QSSType type, String message) {
        return computeHSShortLog(search, theory, diagnoses, queries, type, message, null);
    }
    protected String computeHSShortLog(TreeSearch<FormulaSet<OWLLogicalAxiom>,OWLLogicalAxiom> search,
                                       OWLTheory theory, FormulaSet<OWLLogicalAxiom> diagnoses,
                                       List<Double> queries, QSSType type, String message, OWLTheory queryAnswerTheory) {
        SimulatedSession session = new SimulatedSession();
        session.setShowElRates(false);
        TableList entry = new TableList();
        session.setEntry(entry);
        session.setMessage(message);
        session.setTargetD(diagnoses);
        session.setScoringFunct(type);
        session.setTheory(theory);
        session.setQueryAnswerTheory(queryAnswerTheory);
        session.setSearch(search);
        String out = session.simulateQuerySession();
        FormulaSet<OWLLogicalAxiom> diag = getMostProbable(search.getDiagnoses());
        logger.info("found Diag: " + CalculateDiagnoses.renderAxioms(diag));
        boolean foundCorrectD = diagnoses.containsAll(diag); // with ConflictTreeTest only a subset of the diagnosis could be returned
        if (this.getClass() != ConflictTreeTest.class) {
            foundCorrectD = diag.equals(diagnoses);
            theory.getKnowledgeBase().clearTestCases(); // don't clear if called from ConflictTreeTest
        }
        search.reset();
        //Assert.assertTrue(foundCorrectD);
        queries.add(entry.getMeanQuery());
        return out;
    }



    //copied methods from OAE11ConferenceTests

    protected File[] getMappingFiles(String matchingsDir, String dir, String exclusionFile) {
        URL exclusionFileUrl = ClassLoader.getSystemResource(matchingsDir + exclusionFile);
        File folder = new File(ClassLoader.getSystemResource(matchingsDir + dir).getFile());
        if (exclusionFileUrl == null)
            return folder.listFiles();
        File incl = new File(exclusionFileUrl.getFile());
        MyFilenameFilter filter = new MyFilenameFilter(incl);
        return folder.listFiles(filter);
    }

    private class MyFilenameFilter implements FilenameFilter {
        private Set<String> acceptedNames;

        public MyFilenameFilter(File includedNames) {
            acceptedNames = new LinkedHashSet<String>();
            try{
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(includedNames)));
                String strLine;
                while ((strLine = br.readLine()) != null)   {
                    if (!strLine.startsWith("#") && strLine.endsWith(".rdf"))
                        acceptedNames.add(strLine);
                }
                br.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        public boolean accept(File dir, String name) {
            return acceptedNames.contains(name);
        }
    }


}