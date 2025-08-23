package de.hybris.platform.promotionengineservices.promotionengine.report;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.promotionengineservices.promotionengine.report.data.PromotionEngineResults;

public interface ReportPromotionService
{
    PromotionEngineResults report(AbstractOrderModel paramAbstractOrderModel);
}
