package de.hybris.platform.customersupportbackoffice.editor;

import com.hybris.cockpitng.editor.defaultenum.DefaultEnumEditor;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.notifications.NotificationService;
import de.hybris.platform.ticket.model.CsTicketModel;
import de.hybris.platform.ticket.service.TicketBusinessService;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;

public class TicketCategoryEditor extends DefaultEnumEditor
{
    @Resource
    protected TicketBusinessService ticketBusinessService;
    @Resource
    protected NotificationService notificationService;
    private static final Logger LOG = LoggerFactory.getLogger(TicketCategoryEditor.class);


    public void render(Component parent, EditorContext<Object> context, EditorListener<Object> listener)
    {
        context.setOptional(false);
        CsTicketModel ticket = (CsTicketModel)context.getParameter("parentObject");
        WidgetInstanceManager wim = (WidgetInstanceManager)context.getParameter("wim");
        super.render(parent, context, (EditorListener)new Object(this, ticket, wim, listener));
    }
}
