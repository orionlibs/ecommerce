package de.hybris.platform.warehousing.asn.strategy;

import de.hybris.platform.warehousing.model.AdvancedShippingNoticeEntryModel;
import java.util.Date;

public interface AsnReleaseDateStrategy
{
    Date getReleaseDateForStockLevel(AdvancedShippingNoticeEntryModel paramAdvancedShippingNoticeEntryModel);
}
