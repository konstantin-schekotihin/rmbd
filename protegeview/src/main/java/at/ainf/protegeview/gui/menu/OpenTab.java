package at.ainf.protegeview.gui.menu;

import org.protege.editor.core.ui.action.ProtegeAction;
import org.protege.editor.core.ui.workspace.TabbedWorkspace;
import org.protege.editor.core.ui.workspace.WorkspaceTabPlugin;
import org.protege.editor.owl.model.OWLWorkspace;

import java.awt.event.ActionEvent;

/**
 * Created with IntelliJ IDEA.
 * User: pfleiss
 * Date: 10.09.12
 * Time: 11:55
 * To change this template use File | Settings | File Templates.
 */
public abstract class OpenTab extends ProtegeAction {

    public abstract String getViewId();

    public void actionPerformed(ActionEvent e) {

        org.protege.editor.core.ui.workspace.WorkspaceTab tab = null;
        TabbedWorkspace tabbedWorkspace = (TabbedWorkspace)getWorkspace();

        if (tabbedWorkspace.containsTab(getViewId())) {
            tab = (((OWLWorkspace)getWorkspace()).getWorkspaceTab(getViewId()));
        }
        else {
            for (WorkspaceTabPlugin plugin : tabbedWorkspace.getOrderedPlugins())
                if (plugin.getId().equals(getViewId())) {
                    tab = tabbedWorkspace.addTabForPlugin(plugin);
                    break;
                }

        }
        tabbedWorkspace.setSelectedTab(tab);
    }

    @Override
    public void initialise() throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void dispose() throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
    }

}
