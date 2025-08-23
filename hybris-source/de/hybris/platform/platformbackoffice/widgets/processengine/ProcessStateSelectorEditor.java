package de.hybris.platform.platformbackoffice.widgets.processengine;

import com.hybris.backoffice.widgets.notificationarea.NotificationService;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.editors.impl.AbstractCockpitEditorRenderer;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.BackofficeSpringUtil;
import de.hybris.platform.processengine.definition.NoSuchProcessDefinitionException;
import de.hybris.platform.processengine.definition.ProcessDefinition;
import de.hybris.platform.processengine.definition.ProcessDefinitionFactory;
import de.hybris.platform.processengine.definition.ProcessDefinitionId;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Div;

public class ProcessStateSelectorEditor extends AbstractCockpitEditorRenderer<String>
{
    private static final Logger LOG = LoggerFactory.getLogger(ProcessStateSelectorEditor.class);
    private ProcessDefinitionFactory processDefinitionFactory;
    private NotificationService notificationService;


    public void render(Component parent, EditorContext<String> context, EditorListener<String> listener)
    {
        Validate.notNull("All parameters are mandatory", new Object[] {parent, context, listener});
        WidgetInstanceManager widgetInstanceManager = ((Editor)parent).getWidgetInstanceManager();
        Map<String, Object> startSyncMap = (Map<String, Object>)widgetInstanceManager.getModel().getValue("repairProcessForm", Map.class);
        BusinessProcessModel businessProcess = (BusinessProcessModel)startSyncMap.get("businessProcess");
        Div editorDiv = new Div();
        editorDiv.setParent(parent);
        Combobox processStateCombobox = new Combobox();
        processStateCombobox.setParent((Component)editorDiv);
        try
        {
            fillComboBox(processStateCombobox, businessProcess);
            processStateCombobox.addEventListener("onChange", event -> listener.onValueChanged(processStateCombobox.getSelectedItem().getValue()));
        }
        catch(NoSuchProcessDefinitionException e)
        {
            LOG.warn("Could not load process", (Throwable)e);
            getNotificationService().notifyUser(widgetInstanceManager, "General", NotificationEvent.Level.FAILURE, new Object[] {e});
            processStateCombobox.setDisabled(true);
        }
    }


    private void fillComboBox(Combobox syncJobsComboBox, BusinessProcessModel businessProcess)
    {
        for(String state : getProcessAvailableStates(businessProcess))
        {
            Comboitem comboitem = new Comboitem(state);
            comboitem.setValue(state);
            syncJobsComboBox.appendChild((Component)comboitem);
        }
    }


    protected List<String> getProcessAvailableStates(BusinessProcessModel process)
    {
        ProcessDefinition processDefinition = getProcessDefinitionFactory().getProcessDefinition(ProcessDefinitionId.of(process));
        List<String> ids = new ArrayList<>(processDefinition.getNodeIds());
        Collections.sort(ids);
        return ids;
    }


    protected ProcessDefinitionFactory getProcessDefinitionFactory()
    {
        if(this.processDefinitionFactory == null)
        {
            this.processDefinitionFactory = (ProcessDefinitionFactory)BackofficeSpringUtil.getBean("processDefinitionFactory");
        }
        return this.processDefinitionFactory;
    }


    protected NotificationService getNotificationService()
    {
        if(this.notificationService == null)
        {
            this.notificationService = (NotificationService)BackofficeSpringUtil.getBean("notificationService");
        }
        return this.notificationService;
    }
}
