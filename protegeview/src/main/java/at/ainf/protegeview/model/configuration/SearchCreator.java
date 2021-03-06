package at.ainf.protegeview.model.configuration;

import at.ainf.diagnosis.model.InconsistentTheoryException;
import at.ainf.diagnosis.model.SolverException;
import at.ainf.diagnosis.quickxplain.DirectDiagnosis;
import at.ainf.diagnosis.quickxplain.QuickXplain;
import at.ainf.diagnosis.storage.FormulaSet;
import at.ainf.diagnosis.tree.HsTreeSearch;
import at.ainf.diagnosis.tree.InvHsTreeSearch;
import at.ainf.diagnosis.tree.SimpleCostsEstimator;
import at.ainf.diagnosis.tree.TreeSearch;
import at.ainf.diagnosis.tree.searchstrategy.BreadthFirstSearchStrategy;
import at.ainf.diagnosis.tree.searchstrategy.UniformCostSearchStrategy;
import at.ainf.owlapi3.costestimation.OWLAxiomKeywordCostsEstimator;
import at.ainf.owlapi3.model.DualTreeOWLTheory;
import at.ainf.owlapi3.model.OWLIncoherencyExtractor;
import at.ainf.owlapi3.model.OWLTheory;
import org.protege.editor.owl.model.inference.OWLReasonerManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: pfleiss
 * Date: 04.09.12
 * Time: 15:18
 * To change this template use File | Settings | File Templates.
 */
public class SearchCreator {

    private TreeSearch<FormulaSet<OWLLogicalAxiom>, OWLLogicalAxiom> search;

    private OWLOntology ontology;

    private SearchConfiguration config;

    private OWLReasonerManager reasonerMan;

    public SearchCreator(OWLOntology ontology, OWLReasonerManager reasonerMan) {
        this.ontology = ontology;
        this.reasonerMan = reasonerMan;
        readConfiguration();
    }

    public TreeSearch<FormulaSet<OWLLogicalAxiom>, OWLLogicalAxiom> getSearch() {
        if (search == null)
            createSearch();

        return search;
    }

    public void fullReset() {
        readConfiguration();
        search = null;
    }

    protected void copyTestcases(OWLTheory copyFrom, OWLTheory copyTo) {
            for (Set<OWLLogicalAxiom> testcase : copyFrom.getKnowledgeBase().getPositiveTests())
                copyTo.getKnowledgeBase().addPositiveTest(testcase);
            for (Set<OWLLogicalAxiom> testcase : copyFrom.getKnowledgeBase().getNegativeTests())
                copyTo.getKnowledgeBase().addNegativeTest(testcase);
            for (Set<OWLLogicalAxiom> testcase : copyFrom.getKnowledgeBase().getEntailedTests())
                copyTo.getKnowledgeBase().addEntailedTest(testcase);
            for (Set<OWLLogicalAxiom> testcase : copyFrom.getKnowledgeBase().getNonentailedTests())
                copyTo.getKnowledgeBase().addNonEntailedTest(testcase);

    }

    public void reset() {

        readConfiguration();
        OWLTheory theoryOld = (OWLTheory) getSearch().getSearchable();

        search = null;
        OWLTheory theory = (OWLTheory) getSearch().getSearchable();
        copyTestcases(theoryOld,theory);
    }

    public SearchConfiguration getConfig() {
        return config;
    }

    private OWLTheory createTheory(boolean dual) {
        OWLReasonerFactory reasonerFactory = reasonerMan.getCurrentReasonerFactory().getReasonerFactory();
        OWLIncoherencyExtractor extractor = new OWLIncoherencyExtractor(reasonerFactory);
        OWLOntology ont = extractor.getIncoherentPartAsOntology(ontology);

        Set<OWLLogicalAxiom> bax = new HashSet<OWLLogicalAxiom>();
        if (config.aBoxInBG) {
            for (OWLIndividual ind : ont.getIndividualsInSignature()) {
                bax.addAll(ont.getClassAssertionAxioms(ind));
                bax.addAll(ont.getObjectPropertyAssertionAxioms(ind));
            }
        }
        if (config.tBoxInBG) {
            for (OWLAxiom axiom : ont.getTBoxAxioms(false)) {
                bax.add((OWLLogicalAxiom) axiom);
            }
        }

        OWLTheory theory = null;
        try {
            if(dual)
                theory = new DualTreeOWLTheory(reasonerFactory, ont, bax);
            else
                theory = new OWLTheory(reasonerFactory, ont, bax);

            if(config.reduceIncoherency)
                theory.activateReduceToUns();
            theory.setIncludeSubClassOfAxioms(config.inclEntSubClass);
            theory.setIncludeClassAssertionAxioms(config.inclEntSubClass);
            theory.setIncludeEquivalentClassAxioms(config.inclEntSubClass);
            theory.setIncludeDisjointClassAxioms(config.inclEntSubClass);
            theory.setIncludePropertyAssertAxioms(config.inclEntSubClass);
            theory.setIncludeReferencingThingAxioms(config.inclEntSubClass);
            theory.setIncludeOntologyAxioms(config.inclEntSubClass);

        }
        catch (InconsistentTheoryException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        catch (SolverException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return theory;
    }

    private void createSearch() {
        OWLTheory theory = null;
        if (config.treeType == SearchConfiguration.TreeType.REITER ) {
            search = new HsTreeSearch<FormulaSet<OWLLogicalAxiom>, OWLLogicalAxiom>();
            search.setSearcher(new QuickXplain<OWLLogicalAxiom>());
            theory = createTheory(false);
        }
        else if (config.treeType == SearchConfiguration.TreeType.DUAL ) {
            search = new InvHsTreeSearch<FormulaSet<OWLLogicalAxiom>, OWLLogicalAxiom>();
            search.setSearcher(new DirectDiagnosis<OWLLogicalAxiom>());
            theory = createTheory(true);
        }

        if (config.searchType == SearchConfiguration.SearchType.BREATHFIRST) {
            search.setSearchStrategy(new BreadthFirstSearchStrategy<OWLLogicalAxiom>());
            search.setCostsEstimator(new SimpleCostsEstimator<OWLLogicalAxiom>());
        }
        else if (config.searchType == SearchConfiguration.SearchType.UNIFORM_COST) {
            search.setSearchStrategy(new UniformCostSearchStrategy<OWLLogicalAxiom>());
            search.setCostsEstimator(new OWLAxiomKeywordCostsEstimator(theory));
        }
        search.setSearchable(theory);

    }

    private void readConfiguration() {
        config = ConfigFileManager.readConfiguration();

    }

    public void updateConfig(SearchConfiguration newConfiguration) {
        ConfigFileManager.writeConfiguration(newConfiguration);
        reset();
    }

}
