package de.hybris.platform.ordercancel.impl.denialstrategies;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.ordercancel.OrderCancelDenialReason;
import de.hybris.platform.ordercancel.OrderCancelDenialStrategy;
import de.hybris.platform.ordercancel.model.OrderCancelConfigModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Required;

public class ConsignmentPaymentCapturedDenialStrategy extends AbstractCancelDenialStrategy implements OrderCancelDenialStrategy
{
    private Collection<ConsignmentStatus> notCancellableConsignmentStatuses;
    private ConfigurationService configurationService;


    public OrderCancelDenialReason getCancelDenialReason(OrderCancelConfigModel configuration, OrderModel order, PrincipalModel requestor, boolean partialCancel, boolean partialEntryCancel)
    {
        OrderCancelDenialReason result = null;
        if(getConfigurationService().getConfiguration().getBoolean("warehousing.capturepaymentonconsignment", false))
        {
            boolean hasUnallocatedItems = (order.getEntries().stream().mapToLong(entry -> ((OrderEntryModel)entry).getQuantityUnallocated().longValue()).sum() > 0L);
            if(!hasUnallocatedItems && order.getConsignments().stream()
                            .allMatch(consignmentModel -> getNotCancellableConsignmentStatuses().contains(consignmentModel.getStatus())))
            {
                result = getReason();
            }
        }
        return result;
    }


    protected Collection<ConsignmentStatus> getNotCancellableConsignmentStatuses()
    {
        return this.notCancellableConsignmentStatuses;
    }


    @Required
    public void setNotCancellableConsignmentStatuses(Collection<ConsignmentStatus> notCancellableConsignmentStatuses)
    {
        this.notCancellableConsignmentStatuses = notCancellableConsignmentStatuses;
    }


    protected ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }


    @Required
    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }
}
