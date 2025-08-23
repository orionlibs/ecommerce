package de.hybris.platform.omsbackoffice.dto;

import de.hybris.platform.basecommerce.enums.CancelReason;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.zkoss.zul.ListModelArray;

public class OrderEntryToCancelDto implements Comparable<OrderEntryToCancelDto>
{
    private AbstractOrderEntryModel orderEntry;
    private Long quantityToCancel;
    private Long quantityAvailableToCancel;
    private String cancelOrderEntryComment;
    private CancelReason selectedReason;
    private String deliveryModeName;
    private ListModelArray<String> cancelReasonsModel = new ListModelArray(new ArrayList());


    public OrderEntryToCancelDto(AbstractOrderEntryModel orderEntry, List<String> reasons, Long quantityAvailableToCancel, String deliveryModeName)
    {
        this.orderEntry = orderEntry;
        this.quantityToCancel = Long.valueOf(0L);
        this.quantityAvailableToCancel = quantityAvailableToCancel;
        this.cancelReasonsModel = new ListModelArray(reasons);
        this.deliveryModeName = deliveryModeName;
    }


    public int compareTo(OrderEntryToCancelDto orderEntryToCancelDto)
    {
        return Long.compare(getOrderEntry().getProduct().getPk().getLong().longValue(), orderEntryToCancelDto.getOrderEntry().getProduct().getPk().getLong().longValue());
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }
        OrderEntryToCancelDto that = (OrderEntryToCancelDto)o;
        if(!Objects.equals(this.orderEntry, that.orderEntry))
        {
            return false;
        }
        if(!Objects.equals(this.quantityToCancel, that.quantityToCancel))
        {
            return false;
        }
        if(!Objects.equals(this.quantityAvailableToCancel, that.quantityAvailableToCancel))
        {
            return false;
        }
        if(!Objects.equals(this.cancelOrderEntryComment, that.cancelOrderEntryComment))
        {
            return false;
        }
        if(!Objects.equals(this.selectedReason, that.selectedReason))
        {
            return false;
        }
        if(!Objects.equals(this.deliveryModeName, that.deliveryModeName))
        {
            return false;
        }
        return Objects.equals(this.cancelReasonsModel, that.cancelReasonsModel);
    }


    public int hashCode()
    {
        int result = (this.orderEntry != null) ? this.orderEntry.hashCode() : 0;
        result = 31 * result + ((this.quantityToCancel != null) ? this.quantityToCancel.hashCode() : 0);
        result = 31 * result + ((this.quantityAvailableToCancel != null) ? this.quantityAvailableToCancel.hashCode() : 0);
        result = 31 * result + ((this.cancelOrderEntryComment != null) ? this.cancelOrderEntryComment.hashCode() : 0);
        result = 31 * result + ((this.selectedReason != null) ? this.selectedReason.hashCode() : 0);
        result = 31 * result + ((this.deliveryModeName != null) ? this.deliveryModeName.hashCode() : 0);
        result = 31 * result + ((this.cancelReasonsModel != null) ? this.cancelReasonsModel.hashCode() : 0);
        return result;
    }


    public void setCancelReasonsModel(ListModelArray<String> cancelReasonsModel)
    {
        this.cancelReasonsModel = cancelReasonsModel;
    }


    public AbstractOrderEntryModel getOrderEntry()
    {
        return this.orderEntry;
    }


    public void setOrderEntry(AbstractOrderEntryModel orderEntry)
    {
        this.orderEntry = orderEntry;
    }


    public CancelReason getSelectedReason()
    {
        return this.selectedReason;
    }


    public void setSelectedReason(CancelReason selectedReason)
    {
        this.selectedReason = selectedReason;
    }


    public ListModelArray<String> getCancelReasonsModel()
    {
        return this.cancelReasonsModel;
    }


    public String getCancelOrderEntryComment()
    {
        return this.cancelOrderEntryComment;
    }


    public void setCancelOrderEntryComment(String cancelOrderEntryComment)
    {
        this.cancelOrderEntryComment = cancelOrderEntryComment;
    }


    public void setQuantityToCancel(Long quantityToCancel)
    {
        this.quantityToCancel = quantityToCancel;
    }


    public Long getQuantityToCancel()
    {
        return this.quantityToCancel;
    }


    public Long getQuantityAvailableToCancel()
    {
        return this.quantityAvailableToCancel;
    }


    public void setQuantityAvailableToCancel(Long quantityAvailableToCancel)
    {
        this.quantityAvailableToCancel = quantityAvailableToCancel;
    }


    public String getDeliveryModeName()
    {
        return this.deliveryModeName;
    }


    public void setDeliveryModeName(String deliveryModeName)
    {
        this.deliveryModeName = deliveryModeName;
    }
}
