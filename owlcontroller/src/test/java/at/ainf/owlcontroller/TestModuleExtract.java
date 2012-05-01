package at.ainf.owlcontroller;

import at.ainf.diagnosis.quickxplain.FastDiagnosis;
import at.ainf.diagnosis.quickxplain.NewQuickXplain;
import at.ainf.diagnosis.tree.BreadthFirstSearch;
import at.ainf.diagnosis.tree.TreeSearch;
import at.ainf.diagnosis.tree.exceptions.NoConflictException;
import at.ainf.owlapi3.model.DualTreeOWLTheory;
import at.ainf.owlapi3.model.OWLIncoherencyExtractor;
import at.ainf.owlapi3.model.OWLTheory;
import at.ainf.theory.model.InconsistentTheoryException;
import at.ainf.theory.model.SolverException;
import at.ainf.theory.storage.AxiomSet;
import at.ainf.theory.storage.DualStorage;
import at.ainf.theory.storage.SimpleStorage;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import uk.ac.manchester.cs.owlapi.modularity.ModuleType;
import uk.ac.manchester.cs.owlapi.modularity.SyntacticLocalityModuleExtractor;

import java.io.File;
import java.io.InputStream;
import java.util.*;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: pfleiss
 * Date: 23.04.12
 * Time: 16:10
 * To change this template use File | Settings | File Templates.
 */
public class TestModuleExtract {
    private static Logger logger = Logger.getLogger(TestModuleExtract.class.getName());
    private OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

    @BeforeClass
    public static void setUp() {
        String conf = ClassLoader.getSystemResource("owlcontroller-log4j.properties").getFile();
        PropertyConfigurator.configure(conf);
    }

    @Ignore
    @Test
    public void  testExtractor() throws OWLOntologyCreationException {

        String testDir = ClassLoader.getSystemResource("alignment/ontologies").getPath();
        LinkedList<File> files = new LinkedList<File>();
        collectAllFiles(new File(testDir), files);
        testDir = ClassLoader.getSystemResource("big/").getPath();
        collectAllFiles(new File(testDir), files);
        testDir = ClassLoader.getSystemResource("queryontologies/").getPath();
        collectAllFiles(new File(testDir), files);
        for (File file : files) {
            if (file.getName().endsWith(".owl") && !file.getName().endsWith("cton.owl")) {
                OWLOntology ontology = OWLManager.createOWLOntologyManager().loadOntologyFromOntologyDocument(file);
                long timeOne = System.currentTimeMillis();
                OWLIncoherencyExtractor ex = new OWLIncoherencyExtractor(new Reasoner.ReasonerFactory(),ontology);
                OWLOntology extracted = ex.getIncoherentPartAsOntology();
                timeOne = System.currentTimeMillis() - timeOne;
                long timeMu = System.currentTimeMillis();
                OWLIncoherencyExtractor ex2 = new OWLIncoherencyExtractor(new Reasoner.ReasonerFactory(),ontology);
                Set<OWLOntology> m = ex2.getIncoherentPartAsOntologies();
                double meanLog = 0.0;
                double meanAll = 0.0;
                double meanSig = 0.0;
                for (OWLOntology o : m) {
                    meanLog += o.getLogicalAxiomCount();
                    meanAll += o.getAxiomCount();
                    meanSig += o.getSignature(true).size();
                }
                meanLog = meanLog / m.size();
                meanAll = meanAll / m.size();
                meanSig = meanSig / m.size();
                timeMu = System.currentTimeMillis() - timeMu;
                logger.info("ontology:," + file.getName() + ","
                        + timeOne + "," + timeMu + ","
                        + meanLog + "," + meanAll +"," + meanSig
                        + "," + m.size() + ","
                        + ontology.getLogicalAxiomCount() + "," + ontology.getAxiomCount() + ","
                        + ontology.getSignature(true).size() + ","
                        + extracted.getLogicalAxiomCount() + "," + extracted.getAxiomCount() + ","
                        + extracted.getSignature(true).size());
            }
        }

    }

    private void collectAllFiles(File testDir, List<File> files) {
        if (testDir.isFile()) {
            files.add(testDir);
            return;
        }
        for (File file : testDir.listFiles()) {
            collectAllFiles(file, files);
        }
    }

    @Ignore
    @Test
    public void testMouse() throws InconsistentTheoryException, OWLOntologyCreationException, SolverException, NoConflictException {

        String ont = "queryontologies/human.owl";

        manager = OWLManager.createOWLOntologyManager();
        TreeSearch<? extends AxiomSet<OWLLogicalAxiom>, OWLLogicalAxiom> searchNormal = new BreadthFirstSearch<OWLLogicalAxiom>(new SimpleStorage<OWLLogicalAxiom>());
        searchNormal.setSearcher(new NewQuickXplain<OWLLogicalAxiom>());
        OWLOntology ontology = loadOntology (  ont);
        OWLTheory theoryNormal = createTheory(manager, ontology, false);
        searchNormal.setTheory(theoryNormal);
        searchNormal.run();

        Set<? extends AxiomSet<OWLLogicalAxiom>> resultNormal = searchNormal.getStorage().getDiagnoses();


    }

    @Ignore
    @Test
    public void testResultsEqual() throws InconsistentTheoryException, OWLOntologyCreationException, SolverException, NoConflictException {

        String ontos[] = {"queryontologies/uni_3.owl"};

        //String ontos[] = {"queryontologies/Economy-SDA.owl", "queryontologies/Transportation-SDA.owl"};
        for (String ont : ontos) {
        int runs = 1;
        long p[] = new long[runs];
        long stop1A[] = new long[runs];
        long stop2a[] = new long[runs];
        for (int i = 0; i < runs; i++) {


                manager = OWLManager.createOWLOntologyManager();
                TreeSearch<? extends AxiomSet<OWLLogicalAxiom>, OWLLogicalAxiom> searchNormal = new BreadthFirstSearch<OWLLogicalAxiom>(new SimpleStorage<OWLLogicalAxiom>());
                searchNormal.setSearcher(new NewQuickXplain<OWLLogicalAxiom>());
                OWLOntology ontology = loadOntology (  ont);
                long pre = System.currentTimeMillis();
        ontology = new OWLIncoherencyExtractor(new Reasoner.ReasonerFactory(),ontology).getIncoherentPartAsOntology();
            p[i] = System.currentTimeMillis() - pre;
                OWLTheory theoryNormal = createTheory(manager, ontology, false);
                searchNormal.setTheory(theoryNormal);
                long stop1 = System.currentTimeMillis();
                searchNormal.run();
                stop1A[i] = System.currentTimeMillis() - stop1;
                Set<? extends AxiomSet<OWLLogicalAxiom>> resultNormal = searchNormal.getStorage().getDiagnoses();

                manager = OWLManager.createOWLOntologyManager();
                TreeSearch<? extends AxiomSet<OWLLogicalAxiom>, OWLLogicalAxiom> searchDual = new BreadthFirstSearch<OWLLogicalAxiom>(new SimpleStorage<OWLLogicalAxiom>());
                searchDual.setSearcher(new NewQuickXplain<OWLLogicalAxiom>());
                OWLOntology ontology2 = loadOntology(  ont);
                OWLTheory theoryDual = createTheory(manager, ontology2, false);
                searchDual.setTheory(theoryDual);
                long stop2 = System.currentTimeMillis();
                searchDual.run();
                stop2a[i] = System.currentTimeMillis() - stop2 ;
                Set<? extends AxiomSet<OWLLogicalAxiom>> resultDual = searchDual.getStorage().getDiagnoses();

                logger.info(ont + ",hs," + searchNormal.getStorage().getDiagnoses().size()
                    + ",cs," + searchNormal.getStorage().getConflicts().size());
                logger.info(ont + ",hs," + searchDual.getStorage().getDiagnoses().size()
                            + ",cs," + searchDual.getStorage().getConflicts().size());
            assertTrue(resultNormal.equals(resultDual));
        }

        double meanPre = calcMean(p);
        double meanStop1 = calcMean(stop1A);
        double meanStop2 = calcMean(stop2a);

        logger.info("time needed " + ont + " : " + meanPre + " " + meanStop1 + " " + meanStop2);
        }
    }

    double calcMean(long[] a) {
        double result = 0;
        for (long e : a)
            result += e;

        return result / a.length;
    }

    public OWLOntology loadOntology(String path) throws OWLOntologyCreationException {
        InputStream st = ClassLoader.getSystemResourceAsStream(path);
        return manager.loadOntologyFromOntologyDocument(st);
    }

    public OWLTheory createTheory(OWLOntologyManager manager, OWLOntology ontology, boolean dual) throws SolverException, InconsistentTheoryException, OWLOntologyCreationException {
        Set<OWLLogicalAxiom> bax = new HashSet<OWLLogicalAxiom>();
        for (OWLIndividual ind : ontology.getIndividualsInSignature()) {
            bax.addAll(ontology.getClassAssertionAxioms(ind));
            bax.addAll(ontology.getObjectPropertyAssertionAxioms(ind));
        }

        OWLReasonerFactory reasonerFactory = new Reasoner.ReasonerFactory();
        OWLTheory theory = null;
        if(dual)
            theory = new DualTreeOWLTheory(reasonerFactory, ontology, bax);
        else
            theory = new OWLTheory(reasonerFactory, ontology, bax);
        //assert (theory.verifyRequirements());

        return theory;
    }

    public OWLTheory createTheory(OWLOntologyManager manager, String path, boolean dual) throws SolverException, InconsistentTheoryException, OWLOntologyCreationException {
        InputStream st = ClassLoader.getSystemResourceAsStream(path);
        OWLOntology ontology = manager.loadOntologyFromOntologyDocument(st);
        Set<OWLLogicalAxiom> bax = new HashSet<OWLLogicalAxiom>();
        for (OWLIndividual ind : ontology.getIndividualsInSignature()) {
            bax.addAll(ontology.getClassAssertionAxioms(ind));
            bax.addAll(ontology.getObjectPropertyAssertionAxioms(ind));
        }

        OWLReasonerFactory reasonerFactory = new Reasoner.ReasonerFactory();
        OWLTheory theory = null;
        if(dual)
            theory = new DualTreeOWLTheory(reasonerFactory, ontology, bax);
        else
            theory = new OWLTheory(reasonerFactory, ontology, bax);
        //assert (theory.verifyRequirements());

        return theory;
    }

}
