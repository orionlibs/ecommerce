package de.hybris.platform.returns;

import de.hybris.platform.basecommerce.enums.CancelReason;
import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.returns.model.ReturnEntryModel;

public class ReturnActionEntry
{
    private final ReturnEntryModel returnEntry;
    private final long actionQuantity;
    private String notes;
    private HybrisEnumValue actionReason;


    public ReturnActionEntry(ReturnEntryModel returnEntry)
    {
        this.returnEntry = returnEntry;
        this.actionQuantity = returnEntry.getExpectedQuantity().longValue();
    }


    public ReturnActionEntry(ReturnEntryModel returnEntry, long actionQuantity)
    {
        this(returnEntry, actionQuantity, null);
    }


    public ReturnActionEntry(ReturnEntryModel returnEntry, long actionQuantity, String notes)
    {
        this(returnEntry, actionQuantity, notes, (HybrisEnumValue)CancelReason.NA);
    }


    public ReturnActionEntry(ReturnEntryModel returnEntry, String notes, HybrisEnumValue actionReason)
    {
        this(returnEntry, returnEntry.getExpectedQuantity().longValue(), notes, actionReason);
    }


    public ReturnActionEntry(ReturnEntryModel returnEntry, long actionQuantity, String notes, HybrisEnumValue actionReason)
    {
        this.returnEntry = returnEntry;
        if(actionQuantity < 0L)
        {
            throw new IllegalArgumentException("ReturnActionEntry's actionQuantity value must be greater than zero");
        }
        if(actionQuantity > returnEntry.getExpectedQuantity().longValue())
        {
            throw new IllegalArgumentException("ReturnActionEntry's actionQuantity value cannot be greater than actual ReturnEntry expected quantity");
        }
        this.actionQuantity = actionQuantity;
        this.notes = notes;
        this.actionReason = actionReason;
    }


    public ReturnEntryModel getReturnEntry()
    {
        return this.returnEntry;
    }


    public long getActionQuantity()
    {
        return this.actionQuantity;
    }


    public String getNotes()
    {
        return this.notes;
    }


    public void setNotes(String notes)
    {
        this.notes = notes;
    }


    public HybrisEnumValue getActionReason()
    {
        return this.actionReason;
    }


    public void setActionReason(HybrisEnumValue actionReason)
    {
        this.actionReason = actionReason;
    }
}
