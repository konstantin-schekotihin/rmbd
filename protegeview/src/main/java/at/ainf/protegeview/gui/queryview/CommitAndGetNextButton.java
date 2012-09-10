package at.ainf.protegeview.gui.queryview;

import at.ainf.protegeview.gui.toolboxview.AbstractGuiButton;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Created with IntelliJ IDEA.
 * User: pfleiss
 * Date: 07.09.12
 * Time: 17:15
 * To change this template use File | Settings | File Templates.
 */
public class CommitAndGetNextButton extends AbstractGuiButton {

    public CommitAndGetNextButton(final QueryView queryView) {
        super("Commit and Get Next","Commit and Get New Query", "Next2.png", KeyEvent.VK_X ,
                new AbstractAction(){
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        queryView.getEditorKitHook().getActiveOntologyDiagnosisSearcher().doCommitAndGetNewQuery(new QueryErrorHandler());
                    }
                }
        );

    }
}
