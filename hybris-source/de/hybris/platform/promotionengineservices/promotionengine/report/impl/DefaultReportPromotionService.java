package de.hybris.platform.promotionengineservices.promotionengine.report.impl;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.promotionengineservices.promotionengine.report.ReportPromotionService;
import de.hybris.platform.promotionengineservices.promotionengine.report.data.PromotionEngineResults;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Required;

public class DefaultReportPromotionService implements ReportPromotionService
{
    private Converter<AbstractOrderModel, PromotionEngineResults> promotionEngineResultsConverter;


    public PromotionEngineResults report(AbstractOrderModel order)
    {
        if(Objects.nonNull(order))
        {
            return (PromotionEngineResults)getPromotionEngineResultsConverter().convert(order);
        }
        return new PromotionEngineResults();
    }


    protected Converter<AbstractOrderModel, PromotionEngineResults> getPromotionEngineResultsConverter()
    {
        return this.promotionEngineResultsConverter;
    }


    @Required
    public void setPromotionEngineResultsConverter(Converter<AbstractOrderModel, PromotionEngineResults> promotionEngineResultsConverter)
    {
        this.promotionEngineResultsConverter = promotionEngineResultsConverter;
    }
}
