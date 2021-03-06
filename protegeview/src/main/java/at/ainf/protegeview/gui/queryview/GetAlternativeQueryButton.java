package at.ainf.protegeview.gui.queryview;

import at.ainf.protegeview.gui.buttons.AbstractGuiButton;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Created with IntelliJ IDEA.
 * User: pfleiss
 * Date: 07.09.12
 * Time: 17:14
 * To change this template use File | Settings | File Templates.
 */
public class GetAlternativeQueryButton extends AbstractGuiButton {

    public GetAlternativeQueryButton(final QueryView queryView) {
        super("Get Alternative Query","If you don't want to answer the actual query, there is no correct answer or you don't know you can get a new quey", "next.png", KeyEvent.VK_A,
                new AbstractAction(){
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        queryView.getEditorKitHook().getActiveOntologyDiagnosisSearcher().doGetAlternativeQuery();
                    }
                }
        );

    }

}
