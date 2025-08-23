package de.hybris.platform.warehousing.asn.service;

import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.warehousing.model.AdvancedShippingNoticeModel;
import java.util.List;

public interface AsnService
{
    void processAsn(AdvancedShippingNoticeModel paramAdvancedShippingNoticeModel);


    AdvancedShippingNoticeModel confirmAsnReceipt(String paramString);


    AdvancedShippingNoticeModel getAsnForInternalId(String paramString);


    List<StockLevelModel> getStockLevelsForAsn(AdvancedShippingNoticeModel paramAdvancedShippingNoticeModel);


    AdvancedShippingNoticeModel cancelAsn(String paramString);
}
