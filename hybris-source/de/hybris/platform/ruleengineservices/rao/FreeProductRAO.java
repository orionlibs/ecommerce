package de.hybris.platform.ruleengineservices.rao;

public class FreeProductRAO extends AbstractRuleActionRAO
{
    private OrderEntryRAO addedOrderEntry;
    private int quantityAdded;


    public void setAddedOrderEntry(OrderEntryRAO addedOrderEntry)
    {
        this.addedOrderEntry = addedOrderEntry;
    }


    public OrderEntryRAO getAddedOrderEntry()
    {
        return this.addedOrderEntry;
    }


    public void setQuantityAdded(int quantityAdded)
    {
        this.quantityAdded = quantityAdded;
    }


    public int getQuantityAdded()
    {
        return this.quantityAdded;
    }
}
