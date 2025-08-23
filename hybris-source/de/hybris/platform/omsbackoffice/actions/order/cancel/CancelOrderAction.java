package de.hybris.platform.omsbackoffice.actions.order.cancel;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.ordercancel.OrderCancelService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;

public class CancelOrderAction extends AbstractComponentWidgetAdapterAware implements CockpitAction<OrderModel, OrderModel>
{
    protected static final String SOCKET_OUT_CONTEXT = "cancelOrderContext";
    protected static final String CAPTURE_PAYMENT_ON_CONSIGNMENT = "warehousing.capturepaymentonconsignment";
    @Resource
    private UserService userService;
    @Resource
    private OrderCancelService orderCancelService;
    @Resource
    private List<OrderStatus> notCancellableOrderStatus;
    @Resource
    private List<ConsignmentStatus> notCancellableConsignmentStatus;
    @Resource
    private ConfigurationService configurationService;


    public boolean canPerform(ActionContext<OrderModel> actionContext)
    {
        OrderModel order = (OrderModel)actionContext.getData();
        return (order != null && !CollectionUtils.isEmpty(order.getEntries()) && getOrderCancelService()
                        .isCancelPossible(order, (PrincipalModel)getUserService().getCurrentUser(), true, true).isAllowed() &&
                        !getNotCancellableOrderStatus().contains(order.getStatus()));
    }


    public String getConfirmationMessage(ActionContext<OrderModel> actionContext)
    {
        return null;
    }


    public boolean needsConfirmation(ActionContext<OrderModel> actionContext)
    {
        return false;
    }


    public ActionResult<OrderModel> perform(ActionContext<OrderModel> actionContext)
    {
        sendOutput("cancelOrderContext", actionContext.getData());
        return new ActionResult("success");
    }


    protected OrderCancelService getOrderCancelService()
    {
        return this.orderCancelService;
    }


    protected UserService getUserService()
    {
        return this.userService;
    }


    protected List<OrderStatus> getNotCancellableOrderStatus()
    {
        return this.notCancellableOrderStatus;
    }


    protected List<ConsignmentStatus> getNotCancellableConsignmentStatus()
    {
        return this.notCancellableConsignmentStatus;
    }


    protected ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }
}
