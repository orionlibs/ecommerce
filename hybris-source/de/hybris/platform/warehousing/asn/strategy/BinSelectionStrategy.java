package de.hybris.platform.warehousing.asn.strategy;

import de.hybris.platform.warehousing.model.AdvancedShippingNoticeEntryModel;
import java.util.Map;

public interface BinSelectionStrategy
{
    Map<String, Integer> getBinsForAsnEntry(AdvancedShippingNoticeEntryModel paramAdvancedShippingNoticeEntryModel);
}
