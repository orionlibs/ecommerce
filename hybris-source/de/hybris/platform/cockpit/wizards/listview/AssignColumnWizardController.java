package de.hybris.platform.cockpit.wizards.listview;

import de.hybris.platform.cockpit.components.duallistbox.impl.DefaultSimpleDualListboxEditor;
import de.hybris.platform.cockpit.model.listview.ColumnDescriptor;
import de.hybris.platform.cockpit.model.listview.UIListView;
import de.hybris.platform.cockpit.model.listview.impl.ListView;
import de.hybris.platform.cockpit.wizards.Wizard;
import de.hybris.platform.cockpit.wizards.WizardPage;
import de.hybris.platform.cockpit.wizards.impl.DefaultPageController;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.Clients;

public class AssignColumnWizardController extends DefaultPageController
{
    private static final Logger LOG = LoggerFactory.getLogger(AssignColumnWizardController.class);


    public void done(Wizard wizard, WizardPage page)
    {
        if(!(page instanceof AssignColumnPage))
        {
            LOG.warn(AssignColumnWizardController.class.getName() + " should be used with " + AssignColumnWizardController.class.getName());
            return;
        }
        AssignColumnPage assignWorkflowPage = (AssignColumnPage)page;
        DefaultSimpleDualListboxEditor editor = assignWorkflowPage.getEditor();
        List<ColumnDescriptor> assignedValuesList = editor.getAssignedValuesList();
        UIListView listView = (UIListView)wizard.getWizardContext().getAttribute("listView");
        List<ColumnDescriptor> savedVisibleColumns = new ArrayList<>(listView.getModel().getColumnComponentModel().getVisibleColumns());
        Integer colIndex = (Integer)wizard.getWizardContext().getAttribute("colIndex");
        if(!assignedValuesList.isEmpty())
        {
            for(ColumnDescriptor desc : assignedValuesList)
            {
                if(!savedVisibleColumns.contains(desc))
                {
                    if(((ListView)listView).isEditing())
                    {
                        ((ListView)listView).stopEditing();
                    }
                    try
                    {
                        ((ListView)listView).fireShowColumn(desc, colIndex);
                        ((ListView)listView).focusFocusComponent();
                    }
                    finally
                    {
                        Clients.showBusy(null, false);
                    }
                }
            }
            List<ColumnDescriptor> columnsToHide = new ArrayList<>();
            columnsToHide.addAll(savedVisibleColumns);
            columnsToHide.removeAll(assignedValuesList);
            if(!columnsToHide.isEmpty())
            {
                for(ColumnDescriptor columnToHide : columnsToHide)
                {
                    if(((ListView)listView).isEditing())
                    {
                        ((ListView)listView).stopEditing();
                    }
                    try
                    {
                        EventListener evenListener = (EventListener)wizard.getWizardContext().getAttribute("hideListener");
                        if(evenListener != null)
                        {
                            try
                            {
                                evenListener.onEvent((Event)new HideColumnEvent("hideCOlumnEvent", (Component)listView, columnToHide));
                            }
                            catch(Exception e)
                            {
                                LOG.error("Cannot send HideColumnEvent", e.getCause());
                            }
                        }
                        ((ListView)listView).focusFocusComponent();
                    }
                    finally
                    {
                        Clients.showBusy(null, false);
                    }
                }
            }
        }
    }
}
