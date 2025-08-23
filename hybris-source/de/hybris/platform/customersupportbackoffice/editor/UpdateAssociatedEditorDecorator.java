package de.hybris.platform.customersupportbackoffice.editor;

import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.editors.impl.AbstractCockpitEditorRenderer;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import com.hybris.cockpitng.util.notifications.NotificationService;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.QuoteModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.ticket.model.CsTicketModel;
import de.hybris.platform.ticket.service.TicketBusinessService;
import java.util.Collections;
import java.util.List;
import javax.annotation.Resource;
import org.zkoss.zk.ui.Component;

public class UpdateAssociatedEditorDecorator extends AbstractCockpitEditorRenderer<String>
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
        Object object = editorContext.getParameter("parentObject");
        if(object instanceof CsTicketModel)
        {
            CsTicketModel ticket = (CsTicketModel)editorContext.getParameter("parentObject");
            ancestorEditor.addEventListener("onValueChanged", event -> {
                AbstractOrderModel association = (AbstractOrderModel)event.getData();
                ticket.setOrder(association);
                this.ticketBusinessService.updateTicket(ticket);
                this.modelService.refresh(ticket);
                List<Editor> editors = findEmbeddedEditors(parent);
                editors.forEach(());
                this.notificationService.notifyUser(this.notificationService.getWidgetNotificationSource(wim), "UpdateObject", NotificationEvent.Level.SUCCESS, new Object[] {Collections.singletonList(ticket)});
                wim.getWidgetslot().updateView();
            });
        }
        else if(object instanceof QuoteModel)
        {
            QuoteModel quote = (QuoteModel)object;
            List<Editor> editors = findEmbeddedEditors(parent);
            editors.forEach(editor -> {
                if("com.hybris.cockpitng.editor.asmdeeplinkreferenceeditor".equalsIgnoreCase(editor.getComponentID()) && editor.getEditorRenderer() instanceof AbstractComponentWidgetAdapterAware)
                {
                    ((AbstractComponentWidgetAdapterAware)editor.getEditorRenderer()).sendOutput("itemSelectedForDecorator", quote);
                }
            });
        }
    }
}
