package de.hybris.platform.customersupportbackoffice.renderers;

import com.hybris.cockpitng.core.config.impl.jaxb.listview.ListColumn;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.common.WidgetComponentRenderer;
import de.hybris.platform.ticket.enums.CsTicketState;
import de.hybris.platform.ticket.model.CsTicketModel;
import org.apache.commons.lang.StringUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;

public class TicketStatusCellRenderer implements WidgetComponentRenderer<Listcell, ListColumn, CsTicketModel>
{
    private static final String TICKET_STATE_OPEN = "customersupport_backoffice_tickets_state_open";
    private static final String TICKET_STATE_PREFIX = "customersupport_backoffice_tickets_state_";


    public void render(Listcell parent, ListColumn configuration, CsTicketModel ticket, DataType dataType, WidgetInstanceManager widgetInstanceManager)
    {
        Label stateLabel = null;
        if(ticket.getState().equals(CsTicketState.OPEN))
        {
            stateLabel = new Label(Labels.getLabel("customersupport_backoffice_tickets_state_open"));
        }
        else
        {
            String state = Labels.getLabel("customersupport_backoffice_tickets_state_" + ticket.getState().getCode().toLowerCase());
            if(StringUtils.isEmpty(state))
            {
                state = ticket.getState().getCode();
            }
            stateLabel = new Label(state);
        }
        stateLabel.setVisible(true);
        stateLabel.setParent((Component)parent);
    }
}
