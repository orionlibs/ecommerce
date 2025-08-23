package de.hybris.platform.customersupportbackoffice.widgets.sessioncontext.util;

import com.hybris.cockpitng.core.model.WidgetModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.customersupportbackoffice.widgets.sessioncontext.model.SessionContextModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.ticket.model.CsTicketModel;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SessionContextUtil
{
    public static SessionContextModel createOrReturnSessionContext(WidgetModel model)
    {
        SessionContextModel sessionContext = getCurrentSessionContext(model);
        if(null == sessionContext)
        {
            sessionContext = new SessionContextModel();
            model.setValue("sessionContext", sessionContext);
        }
        return sessionContext;
    }


    public static SessionContextModel getCurrentSessionContext(WidgetModel model)
    {
        SessionContextModel sessionContext = null;
        if(null != model.getValue("sessionContext", SessionContextModel.class))
        {
            sessionContext = (SessionContextModel)model.getValue("sessionContext", SessionContextModel.class);
        }
        return sessionContext;
    }


    public static void clearSessionContext(WidgetModel model)
    {
        model.remove("sessionContext");
    }


    public static void populateCustomer(WidgetModel model, UserModel customerModel)
    {
        updateSessionContext(model, customerModel, null, null);
    }


    public static void populateOrder(WidgetModel model, OrderModel orderModel)
    {
        updateSessionContext(model, orderModel.getUser(), null, (AbstractOrderModel)orderModel);
    }


    public static void populateTicket(WidgetModel model, CsTicketModel ticketModel)
    {
        AbstractOrderModel orderModel = null;
        if(null != ticketModel.getOrder() && ticketModel.getOrder() instanceof OrderModel)
        {
            orderModel = ticketModel.getOrder();
        }
        updateSessionContext(model, ticketModel.getCustomer(), ticketModel, orderModel);
    }


    public static void updateSessionContext(WidgetModel model, UserModel customerModel, CsTicketModel ticketModel, AbstractOrderModel orderModel)
    {
        SessionContextModel sessionContext = createOrReturnSessionContext(model);
        sessionContext.setCurrentTicket(ticketModel);
        sessionContext.setCurrentCustomer(customerModel);
        sessionContext.setCurrentOrder(orderModel);
        if(null != orderModel && orderModel instanceof OrderModel && null != ((OrderModel)orderModel).getReturnRequests())
        {
            List<ReturnRequestModel> returns = ((OrderModel)orderModel).getReturnRequests();
            Map<String, ReturnRequestModel> returnsMap = (Map<String, ReturnRequestModel>)returns.stream().collect(
                            Collectors.toMap(ReturnRequestModel::getCode, Function.identity()));
            sessionContext.setCurrentOrderReturns(returnsMap);
        }
        else
        {
            sessionContext.setCurrentOrderReturns(null);
        }
    }
}
