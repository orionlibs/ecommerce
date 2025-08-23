package de.hybris.platform.personalizationservices.segment.impl;

import de.hybris.platform.personalizationservices.model.CxSegmentModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public class CxSegmentRemoveInterceptor implements RemoveInterceptor<CxSegmentModel>
{
    public void onRemove(CxSegmentModel cxSegmentModel, InterceptorContext interceptorContext) throws InterceptorException
    {
        if(cxSegmentModel == null || StringUtils.isEmpty(cxSegmentModel.getCode()))
        {
            return;
        }
        checkTriggers(cxSegmentModel);
    }


    protected void checkTriggers(CxSegmentModel cxSegmentModel) throws InterceptorException
    {
        if(CollectionUtils.isNotEmpty(cxSegmentModel.getTriggers()) ||
                        CollectionUtils.isNotEmpty(cxSegmentModel.getExpressionTriggers()))
        {
            throw new InterceptorException("Segments that are related to triggers are not allowed to be removed!");
        }
    }
}
