package de.hybris.platform.jalo.order;

import com.google.common.base.Preconditions;
import java.util.List;

public class OrderCloneHelper
{
    public static void postProcess(AbstractOrder original, AbstractOrder copy)
    {
        copyTotalTaxValues(original, copy);
        copyCalculatedFlag(original, copy);
    }


    public static void copyTotalTaxValues(AbstractOrder original, AbstractOrder copy)
    {
        copy.setTotalTaxValues(original.getTotalTaxValues());
    }


    public static void copyCalculatedFlag(AbstractOrder original, AbstractOrder copy)
    {
        copy.setCalculated(original.isCalculated());
        List<AbstractOrderEntry> originalEntries = original.getAllEntries();
        List<AbstractOrderEntry> copyEntries = copy.getAllEntries();
        if(originalEntries.size() != copyEntries.size())
        {
            throw new IllegalStateException("different entry numbers in original and copied order ( " + originalEntries.size() + "<>" + copyEntries
                            .size() + ")");
        }
        for(int i = 0; i < originalEntries.size(); i++)
        {
            AbstractOrderEntry originalEntry = originalEntries.get(i);
            AbstractOrderEntry copyEntry = copyEntries.get(i);
            Preconditions.checkArgument(originalEntry.getEntryNumber().equals(copyEntry.getEntryNumber()));
            copyEntry.setCalculated(originalEntry.isCalculated());
        }
    }
}
