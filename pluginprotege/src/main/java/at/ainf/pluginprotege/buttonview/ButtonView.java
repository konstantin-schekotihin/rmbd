package at.ainf.pluginprotege.buttonview;

import at.ainf.pluginprotege.WorkspaceTab;
import org.protege.editor.owl.ui.view.AbstractOWLViewComponent;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: pfleiss
 * Date: 21.09.11
 * Time: 12:48
 * To change this template use File | Settings | File Templates.
 */
public class ButtonView extends AbstractOWLViewComponent {



    protected void initialiseOWLView() throws Exception {

        setLayout(new BorderLayout());

        add(new ButtonToolbar((WorkspaceTab) getOWLWorkspace().getWorkspaceTab("at.ainf.pluginprotege.WorkspaceTab"),null),BorderLayout.NORTH);


    }

    protected void disposeOWLView() {

    }


}
