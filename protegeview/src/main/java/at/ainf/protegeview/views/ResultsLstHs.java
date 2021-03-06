package at.ainf.protegeview.views;

import at.ainf.diagnosis.storage.FormulaSet;
import at.ainf.protegeview.debugmanager.DebugManager;
import org.protege.editor.core.ui.list.MListButton;
import org.protege.editor.owl.OWLEditorKit;
import org.semanticweb.owlapi.model.OWLLogicalAxiom;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: pfleiss
 * Date: 29.09.11
 * Time: 09:01
 * To change this template use File | Settings | File Templates.
 */
public class ResultsLstHs extends ResultsList {

    public ResultsLstHs(OWLEditorKit owlEditorKit) {
        super(owlEditorKit);    // To change body of overridden methods use File | Settings | File Templates.
    }

    public void showConfidenceDialog(final ResultsListSection sec) {
        final JSlider slider = new JSlider(0,100,sec.getUserTargetConfidence());
        String confStr = "Confidence Level: " + sec.getUserTargetConfidence();
        final JLabel confidenceLevelPanel = new JLabel(confStr);
        Object complexMsg[] = {"How confident are you this is the target diagnosis?", slider,confidenceLevelPanel};
        slider.setMajorTickSpacing(10);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                confidenceLevelPanel.setText("Confidence Level: " + slider.getValue());
            }
        });
        JOptionPane optionPane = new JOptionPane();
        optionPane.setMessage(complexMsg);
        optionPane.setMessageType(JOptionPane.QUESTION_MESSAGE);
        JDialog dialog = optionPane.createDialog(null, "Confidence Level");
        dialog.setVisible(true);
        sec.setUserTargetConfidence (slider.getValue());

        sec.setUserMarkedThisTarget (true);

    }


    @Override
    protected List<MListButton> getButtons(Object value) {
        if (value instanceof ResultsListSectionItem) {
            return super.getButtons(value);
        }
        else {
            List<MListButton> buttons = new ArrayList<MListButton>();
            final ResultsListSection sec = (ResultsListSection) value;

            final AcceptDiagnosisButton acceptDiagnosisButton = new AcceptDiagnosisButton(null);
            acceptDiagnosisButton.setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                /*DefaultListModel m = ((DefaultListModel)getModel());
                for (int i = 0; i < m.size(); i++) {
                    if (m.get(i) instanceof ResultsListSection) {
                        ((ResultsListSection) m.get(i)).setUserMarkedThisTarget(false);
                    }
                }*/
                boolean marked = sec.isUserMarkedThisTarget();
                if (marked)
                    sec.setUserMarkedThisTarget(false);
                else
                    showConfidenceDialog(sec);

              }
            });


            buttons.add(acceptDiagnosisButton);

            buttons.add(new ShowHSTreeButton(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                   ResultsListSection section = (ResultsListSection)((ShowHSTreeButton)e.getSource()).getRowObject();
                    FormulaSet<OWLLogicalAxiom> node = (FormulaSet<OWLLogicalAxiom>) section
                            .getAxiomSet();
                    DebugManager.getInstance().setTreeNode(node);
                    DebugManager.getInstance().notifyTreeNodeChanged();
                }
            }));

            buttons.add(new EntailmentsButton(new ActionListener() {
              public void actionPerformed(ActionEvent e) {
                sec.setShowEntailments(!sec.isShowEntailments());

                  Set<ResultsListSection> set = new HashSet<ResultsListSection>();

                DefaultListModel m = ((DefaultListModel) getModel());
                for (int i = 0; i < m.size(); i++) {
                    if (m.get(i) instanceof ResultsListSection &&
                            ((ResultsListSection)m.get(i)).isShowEntailments()) {
                        set.add (((ResultsListSection) m.get(i)));
                    }
                }

                  DebugManager.getInstance().setEntSets(set);
                  DebugManager.getInstance().notifyEntSetSelected();

              }
            }));

            return buttons;
        }
    }
}
