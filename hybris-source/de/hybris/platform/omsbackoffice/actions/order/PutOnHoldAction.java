package de.hybris.platform.omsbackoffice.actions.order;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;

public class PutOnHoldAction extends AbstractComponentWidgetAdapterAware implements CockpitAction<OrderModel, OrderModel>
{
    protected static final String SOCKET_OUT_CONTEXT = "putOnHoldContext";
    @Resource
    private List<OrderStatus> onHoldableOrderStatusList;
    @Resource
    private List<ConsignmentStatus> notCancellableConsignmentStatus;


    public ActionResult<OrderModel> perform(ActionContext<OrderModel> actionContext)
    {
        OrderModel order = (OrderModel)actionContext.getData();
        sendOutput("putOnHoldContext", order);
        ActionResult<OrderModel> actionResult = new ActionResult("success");
        actionResult.getStatusFlags().add(ActionResult.StatusFlag.OBJECT_PERSISTED);
        return actionResult;
    }


    public boolean canPerform(ActionContext<OrderModel> actionContext)
    {
        OrderModel order = (OrderModel)actionContext.getData();
        return (isValidOrderStatusAndConsignments(order) && order.getConsignments().stream()
                        .anyMatch(consignment -> !getNotCancellableConsignmentStatus().contains(consignment.getStatus())) && order
                        .getEntries().stream().anyMatch(orderEntry -> (getQuantityPending(orderEntry).longValue() > 0L)));
    }


    protected boolean isValidOrderStatusAndConsignments(OrderModel order)
    {
        return (order != null && getOnHoldableOrderStatusList().contains(order.getStatus()) && order.getConsignments() != null);
    }


    protected Long getQuantityPending(AbstractOrderEntryModel orderEntryModel)
    {
        return Long.valueOf(orderEntryModel.getQuantity().longValue() - getQuantityShipped(orderEntryModel).longValue());
    }


    protected Long getQuantityShipped(AbstractOrderEntryModel orderEntryModel)
    {
        long shippedquantity = 0L;
        if(CollectionUtils.isNotEmpty(orderEntryModel.getConsignmentEntries()))
        {
            Collection<ConsignmentStatus> confirmedConsignmentStatus = Arrays.asList(new ConsignmentStatus[] {ConsignmentStatus.SHIPPED, ConsignmentStatus.PICKUP_COMPLETE});
            shippedquantity = orderEntryModel.getConsignmentEntries().stream().filter(consignmentEntry -> confirmedConsignmentStatus.contains(consignmentEntry.getConsignment().getStatus())).mapToLong(consEntry -> consEntry.getQuantity().longValue()).sum();
        }
        return Long.valueOf(shippedquantity);
    }


    public boolean needsConfirmation(ActionContext<OrderModel> actionContext)
    {
        return false;
    }


    public String getConfirmationMessage(ActionContext<OrderModel> actionContext)
    {
        return null;
    }


    protected List<OrderStatus> getOnHoldableOrderStatusList()
    {
        return CollectionUtils.isEmpty(this.onHoldableOrderStatusList) ? Collections.EMPTY_LIST : this.onHoldableOrderStatusList;
    }


    protected List<ConsignmentStatus> getNotCancellableConsignmentStatus()
    {
        return CollectionUtils.isEmpty(this.notCancellableConsignmentStatus) ? Collections.EMPTY_LIST : this.notCancellableConsignmentStatus;
    }
}
