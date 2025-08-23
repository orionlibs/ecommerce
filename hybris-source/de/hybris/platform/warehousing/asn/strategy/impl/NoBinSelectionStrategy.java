package de.hybris.platform.warehousing.asn.strategy.impl;

import de.hybris.platform.warehousing.asn.strategy.BinSelectionStrategy;
import de.hybris.platform.warehousing.model.AdvancedShippingNoticeEntryModel;
import java.util.HashMap;
import java.util.Map;

public class NoBinSelectionStrategy implements BinSelectionStrategy
{
    public Map<String, Integer> getBinsForAsnEntry(AdvancedShippingNoticeEntryModel asnEntry)
    {
        Map<String, Integer> bins = new HashMap<>();
        bins.put(null, Integer.valueOf(asnEntry.getQuantity()));
        return bins;
    }
}
