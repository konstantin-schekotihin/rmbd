package at.ainf.protegeview.gui.axiomsetviews.axiomslist;

import at.ainf.diagnosis.storage.FormulaSet;
import at.ainf.protegeview.gui.AbstractAxiomList;
import org.protege.editor.owl.OWLEditorKit;
import org.semanticweb.owlapi.model.OWLLogicalAxiom;
import org.semanticweb.owlapi.model.OWLOntology;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: pfleiss
 * Date: 04.09.12
 * Time: 17:38
 * To change this template use File | Settings | File Templates.
 */
public class SimpleAxiomList extends AbstractAxiomList {

    public SimpleAxiomList(OWLEditorKit editorKit, Color headerColor) {
        super(editorKit);
        setCellRenderer(new AxiomListItemRenderer(editorKit,headerColor));
    }

    public void updateList(Set<FormulaSet<OWLLogicalAxiom>> setOfFormulaSets, OWLOntology ontology, String headerPref, boolean isIncludeMeasure) {
        List<Object> items = new ArrayList<Object>();
        for (FormulaSet<OWLLogicalAxiom> formulaSet : setOfFormulaSets) {
            items.add(new AxiomListHeader(formulaSet,headerPref,isIncludeMeasure));
            for (OWLLogicalAxiom axiom : formulaSet) {
                items.add(new AxiomListItem(axiom,ontology));
            }
            items.add(" ");
        }
        if (items.size()>0)
            items.remove(items.size()-1);

        /*DefaultListModel model = (DefaultListModel) getModel();
        model.clear();
        for (Object item : items)
            model.addElement(item);*/



        setListData(items.toArray());
        //setFixedCellHeight(24);

                //listModel.addElement(MyOWLRendererParser.render(axiom));

    }




}
