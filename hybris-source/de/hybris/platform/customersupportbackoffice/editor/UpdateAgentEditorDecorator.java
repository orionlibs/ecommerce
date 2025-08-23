package de.hybris.platform.customersupportbackoffice.editor;

import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.editors.impl.AbstractCockpitEditorRenderer;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.notifications.NotificationService;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.ticket.model.CsTicketModel;
import de.hybris.platform.ticket.service.TicketBusinessService;
import java.util.Collections;
import javax.annotation.Resource;
import org.zkoss.zk.ui.Component;

public class UpdateAgentEditorDecorator extends AbstractCockpitEditorRenderer<String>
{
    @Resource
    protected TicketBusinessService ticketBusinessService;
    @Resource
    protected ModelService modelService;
    @Resource
    protected NotificationService notificationService;


    public void render(Component parent, EditorContext<String> editorContext, EditorListener<String> editorListener)
    {
        Editor ancestorEditor = findAncestorEditor(parent);
        WidgetInstanceManager wim = (WidgetInstanceManager)editorContext.getParameter("wim");
        CsTicketModel ticket = (CsTicketModel)editorContext.getParameter("parentObject");
        ancestorEditor.addEventListener("onValueChanged", event -> {
            EmployeeModel agent = (EmployeeModel)event.getData();
            this.modelService.refresh(ticket);
            this.ticketBusinessService.assignTicketToAgent(ticket, agent);
            this.modelService.refresh(ticket);
            this.notificationService.notifyUser(this.notificationService.getWidgetNotificationSource(wim), "UpdateObject", NotificationEvent.Level.SUCCESS, new Object[] {Collections.singletonList(ticket)});
            wim.getWidgetslot().updateView();
        });
    }
}
