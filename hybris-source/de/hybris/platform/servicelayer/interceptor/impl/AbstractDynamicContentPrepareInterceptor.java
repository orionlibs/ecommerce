package de.hybris.platform.servicelayer.interceptor.impl;

import de.hybris.platform.core.model.AbstractDynamicContentModel;
import de.hybris.platform.dynamiccontent.DynamicContentChecksumCalculator;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import org.springframework.beans.factory.annotation.Required;

public class AbstractDynamicContentPrepareInterceptor implements PrepareInterceptor<AbstractDynamicContentModel>
{
    private DynamicContentChecksumCalculator dynamicContentChecksumCalculator;


    @Required
    public void setDynamicContentChecksumCalculator(DynamicContentChecksumCalculator dynamicContentChecksumCalculator)
    {
        this.dynamicContentChecksumCalculator = dynamicContentChecksumCalculator;
    }


    public void onPrepare(AbstractDynamicContentModel model, InterceptorContext ctx) throws InterceptorException
    {
        HistoricalContentCreator historicalContentCreator = new HistoricalContentCreator(model, ctx);
        historicalContentCreator.executeIfContentChanged();
        model.setChecksum(calculateChecksum(model.getContent()));
    }


    private String calculateChecksum(String content)
    {
        return (content == null) ? null : this.dynamicContentChecksumCalculator.calculateChecksumOf(content);
    }
}
