package de.hybris.platform.customersupportbackoffice.editor;

import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.cockpitng.editors.CockpitEditorRenderer;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.util.notifications.NotificationService;
import com.hybris.cockpitng.widgets.configurableflow.ConfigurableFlowContextParameterNames;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.ticket.enums.CsTicketState;
import de.hybris.platform.ticket.model.CsTicketModel;
import de.hybris.platform.ticket.service.TicketBusinessService;
import de.hybris.platform.ticket.service.TicketException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;

public class TicketStateEditor extends AbstractComponentWidgetAdapterAware implements CockpitEditorRenderer<Object>
{
    private static final Logger LOG = Logger.getLogger(TicketStateEditor.class);
    @Resource
    protected TicketBusinessService ticketBusinessService;
    @Resource
    protected UserService userService;
    @Resource
    protected ModelService modelService;
    @Resource
    protected LabelService labelService;
    @Resource
    protected NotificationService notificationService;
    private static final Object CLOSE_STATE = new Object();
    private static final Object REOPEN_STATE = new Object();
    private static final Object OPEN_STATE = new Object();
    public static final String OUTPUT_SOCKET = "state";
    public static final String TICKET_CLOSE_WIZARD = "csTicketCloseWizard";
    public static final String TICKET_REOPEN_WIZARD = "csTicketReopenWizard";


    public void render(Component parent, EditorContext<Object> context, EditorListener<Object> listener)
    {
        CsTicketModel ticket = (CsTicketModel)context.getParameter("parentObject");
        WidgetInstanceManager wim = (WidgetInstanceManager)context.getParameter("wim");
        if(context.getInitialValue().equals(CsTicketState.NEW) && this.userService.getCurrentUser() instanceof EmployeeModel && ticket
                        .getAssignedAgent() == null)
        {
            try
            {
                this.ticketBusinessService.assignTicketToAgent(ticket, (EmployeeModel)this.userService.getCurrentUser());
                this.notificationService.notifyUser(this.notificationService.getWidgetNotificationSource(wim), "UpdateObject", NotificationEvent.Level.SUCCESS, new Object[] {Collections.singletonList(ticket)});
                this.ticketBusinessService.setTicketState(ticket, CsTicketState.OPEN);
                this.modelService.refresh(ticket);
            }
            catch(TicketException e)
            {
                LOG.warn(e.getMessage(), (Throwable)e);
            }
        }
        Combobox box = new Combobox();
        parent.appendChild((Component)box);
        ListModelList model = new ListModelList();
        model.add(ticket.getState());
        model.setSelection(Collections.singletonList(ticket.getState()));
        if(this.ticketBusinessService.isTicketClosed(ticket))
        {
            model.add(REOPEN_STATE);
        }
        else
        {
            model.addAll(this.ticketBusinessService.getTicketNextStates(ticket));
            model.add(CLOSE_STATE);
        }
        box.setModel((ListModel)model);
        box.setReadonly(true);
        box.setAutodrop(true);
        box.setItemRenderer(this::createStateItem);
        box.addEventListener("onSelect", event -> {
            Comboitem selectedItem = box.getSelectedItem();
            if(selectedItem != null)
            {
                if(CLOSE_STATE.equals(selectedItem.getValue()))
                {
                    sendOutput("state", createWizardContext("csTicketCloseWizard", ticket, wim));
                }
                else if(REOPEN_STATE.equals(selectedItem.getValue()))
                {
                    sendOutput("state", createWizardContext("csTicketReopenWizard", ticket, wim));
                }
                else
                {
                    this.ticketBusinessService.setTicketState(ticket, (CsTicketState)selectedItem.getValue());
                    this.notificationService.notifyUser(this.notificationService.getWidgetNotificationSource(wim), "UpdateObject", NotificationEvent.Level.SUCCESS, new Object[] {Collections.singletonList(ticket)});
                }
                wim.getWidgetslot().updateView();
            }
        });
    }


    private void createStateItem(Comboitem item, Object data, int index)
    {
        if(CLOSE_STATE.equals(data))
        {
            item.setLabel(Labels.getLabel("customersupport_backoffice_tickets_state_close"));
            item.setSclass("close");
            item.setValue(CLOSE_STATE);
        }
        else if(REOPEN_STATE.equals(data))
        {
            item.setLabel(Labels.getLabel("customersupport_backoffice_tickets_state_reopen"));
            item.setSclass("reopen");
            item.setValue(REOPEN_STATE);
        }
        else if(OPEN_STATE.equals(data))
        {
            item.setLabel(Labels.getLabel("customersupport_backoffice_tickets_state_open"));
            item.setSclass("reopen");
            item.setValue(OPEN_STATE);
        }
        else
        {
            item.setValue(data);
            String label = Labels.getLabel("customersupport_backoffice_tickets_state_" + data.toString().toLowerCase());
            if(StringUtils.isBlank(label))
            {
                label = String.valueOf(data);
            }
            item.setLabel(label);
        }
    }


    protected Map<String, Object> createWizardContext(String typeName, CsTicketModel ticket, WidgetInstanceManager wim)
    {
        Map<String, Object> wizardInput = new HashMap<>();
        wizardInput.put(ConfigurableFlowContextParameterNames.TYPE_CODE.getName(), typeName);
        wizardInput.put(ConfigurableFlowContextParameterNames.PARENT_OBJECT.getName(), ticket);
        wizardInput.put(ConfigurableFlowContextParameterNames.PARENT_OBJECT_TYPE.getName(), typeName);
        wizardInput.put("wim", wim);
        return wizardInput;
    }
}
