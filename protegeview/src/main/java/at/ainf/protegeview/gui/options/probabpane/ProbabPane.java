package at.ainf.protegeview.gui.options.probabpane;

import at.ainf.owlapi3.costestimation.OWLAxiomKeywordCostsEstimator;
import at.ainf.owlapi3.model.OWLTheory;
import at.ainf.protegeview.model.EditorKitHook;
import at.ainf.protegeview.model.configuration.SearchCreator;
import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntax;
import org.protege.editor.owl.model.OWLWorkspace;
import org.semanticweb.owlapi.model.OWLLogicalAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import uk.ac.manchester.cs.owl.owlapi.mansyntaxrenderer.ManchesterOWLSyntaxOWLObjectRendererImpl;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: pfleiss
 * Date: 19.09.12
 * Time: 12:11
 * To change this template use File | Settings | File Templates.
 */
public class ProbabPane extends JPanel {


    private Set<KeywordSliderPanel> sliders;

    private Map<ManchesterOWLSyntax, BigDecimal> probabMap = new LinkedHashMap<ManchesterOWLSyntax, BigDecimal>();

    protected void copyProbabMap(Map<ManchesterOWLSyntax, BigDecimal> from, Map<ManchesterOWLSyntax, BigDecimal> to) {
        for (ManchesterOWLSyntax keyword : from.keySet())
            to.put(keyword,from.get(keyword));
    }

    protected Set<ManchesterOWLSyntax> getUsedKeywords(OWLOntology ontology) {
        ManchesterOWLSyntaxOWLObjectRendererImpl renderer = new ManchesterOWLSyntaxOWLObjectRendererImpl();
        Set<OWLLogicalAxiom> axioms = ontology.getLogicalAxioms();

        String axiomStr = "";
        for (OWLLogicalAxiom axiom : axioms)
            axiomStr += renderer.render(axiom) + "\t";

        Set<ManchesterOWLSyntax> used = new LinkedHashSet<ManchesterOWLSyntax>();
        for (ManchesterOWLSyntax keyword : OWLAxiomKeywordCostsEstimator.keywords) {
            if (axiomStr.contains(keyword.toString()))
                used.add(keyword);
        }
        return used;
    }

    public Map<ManchesterOWLSyntax,BigDecimal> getProbabMap() {
        for (KeywordSliderPanel sliderPanel : sliders) {
            probabMap.put(sliderPanel.getKeyword(),sliderPanel.getProbab());
        }
        return probabMap;
    }

    protected Set<KeywordSliderPanel> createSliders(Set<ManchesterOWLSyntax> keywords, OWLWorkspace owlWorkspace) {
        Set<KeywordSliderPanel> sliders = new LinkedHashSet<KeywordSliderPanel>();

        for (ManchesterOWLSyntax keyword : keywords)
            sliders.add(new KeywordSliderPanel(keyword,probabMap.get(keyword),owlWorkspace));

        return sliders;
    }

    public ProbabPane(EditorKitHook editorKitHook) {
        SearchCreator creator = editorKitHook.getActiveOntologyDiagnosisSearcher().getSearchCreator();
        OWLOntology ontology =  ((OWLTheory) creator.getSearch().getSearchable()).getOriginalOntology();

        OWLAxiomKeywordCostsEstimator est = (OWLAxiomKeywordCostsEstimator) creator.getSearch().getCostsEstimator();

        copyProbabMap(est.getKeywordProbabilities(),probabMap);
        Set<ManchesterOWLSyntax> usedKeywords = getUsedKeywords(ontology);
        sliders = createSliders(usedKeywords,editorKitHook.getOWLEditorKit().getOWLWorkspace());

        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);

        for (KeywordSliderPanel slider : sliders) {
            add(slider);
            add(Box.createVerticalStrut(10));

        }

    }

}
