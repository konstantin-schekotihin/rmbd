package at.ainf.protegeview.gui.buttons;

import at.ainf.protegeview.gui.axiomsetviews.DiagnosesView;

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

    public StartButton(final DiagnosesView toolboxView) {
        super("Refresh Diagnoses","start to calculate diagnoses","Search.png",KeyEvent.VK_D,
                new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        toolboxView.getEditorKitHook().getActiveOntologyDiagnosisSearcher().doCalculateDiagnosis(new SearchErrorHandler());
                    }
                }
        );

    }
}
