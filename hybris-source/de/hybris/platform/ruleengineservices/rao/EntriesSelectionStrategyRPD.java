package de.hybris.platform.ruleengineservices.rao;

import de.hybris.platform.ruleengineservices.enums.OrderEntrySelectionStrategy;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class EntriesSelectionStrategyRPD implements Serializable
{
    private List<OrderEntryRAO> orderEntries;
    private OrderEntrySelectionStrategy selectionStrategy;
    private int quantity;
    private boolean targetOfAction;


    public void setOrderEntries(List<OrderEntryRAO> orderEntries)
    {
        this.orderEntries = orderEntries;
    }


    public List<OrderEntryRAO> getOrderEntries()
    {
        return this.orderEntries;
    }


    public void setSelectionStrategy(OrderEntrySelectionStrategy selectionStrategy)
    {
        this.selectionStrategy = selectionStrategy;
    }


    public OrderEntrySelectionStrategy getSelectionStrategy()
    {
        return this.selectionStrategy;
    }


    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }


    public int getQuantity()
    {
        return this.quantity;
    }


    public void setTargetOfAction(boolean targetOfAction)
    {
        this.targetOfAction = targetOfAction;
    }


    public boolean isTargetOfAction()
    {
        return this.targetOfAction;
    }


    public boolean equals(Object o)
    {
        if(o == null)
        {
            return false;
        }
        if(o == this)
        {
            return true;
        }
        if(getClass() != o.getClass())
        {
            return false;
        }
        EntriesSelectionStrategyRPD other = (EntriesSelectionStrategyRPD)o;
        return (Objects.equals(getOrderEntries(), other.getOrderEntries()) &&
                        Objects.equals(getSelectionStrategy(), other.getSelectionStrategy()) &&
                        Objects.equals(Integer.valueOf(getQuantity()), Integer.valueOf(other.getQuantity())) &&
                        Objects.equals(Boolean.valueOf(isTargetOfAction()), Boolean.valueOf(other.isTargetOfAction())));
    }


    public int hashCode()
    {
        int result = 1;
        Object<OrderEntryRAO> attribute = (Object<OrderEntryRAO>)this.orderEntries;
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        OrderEntrySelectionStrategy orderEntrySelectionStrategy = this.selectionStrategy;
        result = 31 * result + ((orderEntrySelectionStrategy == null) ? 0 : orderEntrySelectionStrategy.hashCode());
        Integer integer = Integer.valueOf(this.quantity);
        result = 31 * result + ((integer == null) ? 0 : integer.hashCode());
        Boolean bool = Boolean.valueOf(this.targetOfAction);
        result = 31 * result + ((bool == null) ? 0 : bool.hashCode());
        return result;
    }
}
