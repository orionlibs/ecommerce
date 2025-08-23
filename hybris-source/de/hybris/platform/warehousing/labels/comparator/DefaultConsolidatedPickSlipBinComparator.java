package de.hybris.platform.warehousing.labels.comparator;

import de.hybris.platform.warehousing.data.pickslip.ConsolidatedPickSlipFormEntry;
import java.util.Comparator;

public class DefaultConsolidatedPickSlipBinComparator implements Comparator<ConsolidatedPickSlipFormEntry>
{
    public int compare(ConsolidatedPickSlipFormEntry entry1, ConsolidatedPickSlipFormEntry entry2)
    {
        return entry1.getBin().compareTo(entry2.getBin());
    }
}
