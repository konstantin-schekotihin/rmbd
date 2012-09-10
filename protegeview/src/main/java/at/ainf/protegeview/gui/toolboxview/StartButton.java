package at.ainf.protegeview.gui.toolboxview;

import at.ainf.protegeview.model.ErrorHandler;
import at.ainf.protegeview.model.OntologyDiagnosisSearcher;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Created with IntelliJ IDEA.
 * User: pfleiss
 * Date: 04.09.12
 * Time: 09:01
 * To change this template use File | Settings | File Templates.
 */
public class StartButton extends AbstractGuiButton {

    public StartButton(final ToolboxView toolboxView) {
        super("Start","start to calculate diagnoses","Search.png",KeyEvent.VK_S,
                new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        toolboxView.getEditorKitHook().getActiveOntologyDiagnosisSearcher().doCalculateDiagnosis(new SearchErrorHandler());
                    }
                }
        );

    }
}
