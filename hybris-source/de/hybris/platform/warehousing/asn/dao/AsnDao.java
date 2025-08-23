package de.hybris.platform.warehousing.asn.dao;

import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.warehousing.model.AdvancedShippingNoticeModel;
import java.util.List;

public interface AsnDao
{
    List<StockLevelModel> getStockLevelsForAsn(AdvancedShippingNoticeModel paramAdvancedShippingNoticeModel);


    AdvancedShippingNoticeModel getAsnForInternalId(String paramString);
}
