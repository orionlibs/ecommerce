package de.hybris.platform.servicelayer.interceptor.impl;

import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.core.model.enumeration.EnumerationMetaTypeModel;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.Collections;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class EnumerationPrepareInterceptor implements PrepareInterceptor
{
    private TypeService typeService;
    private FlexibleSearchService flexibleSearchService;
    private static final Logger LOG = Logger.getLogger(EnumerationPrepareInterceptor.class);


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    public void onPrepare(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model instanceof EnumerationValueModel && ctx.isNew(model))
        {
            EnumerationValueModel enumValueModel = (EnumerationValueModel)model;
            EnumerationMetaTypeModel enumMetaTypeModel = this.typeService.getEnumerationTypeForCode(enumValueModel
                            .getItemtype());
            Integer sequenceNumber = Integer.valueOf(getNextSequenceNumber(enumMetaTypeModel.getCode()));
            if(LOG.isDebugEnabled())
            {
                LOG.debug("The next sequence number=" + sequenceNumber + " will be set for new enum value=" + enumValueModel
                                .getCode());
            }
            enumValueModel.setSequenceNumber(sequenceNumber);
            enumValueModel.setCodeLowerCase(enumValueModel.getCode().toLowerCase(LocaleHelper.getPersistenceLocale()));
        }
    }


    private int getNextSequenceNumber(String typeCode)
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT MAX({")
                        .append("sequenceNumber")
                        .append("}) FROM {")
                        .append(typeCode)
                        .append("}");
        FlexibleSearchQuery query = new FlexibleSearchQuery(stringBuilder.toString());
        query.setResultClassList(Collections.singletonList(Integer.class));
        SearchResult<Object> res = this.flexibleSearchService.search(query);
        Integer max = res.getResult().isEmpty() ? null : res.getResult().get(0);
        return (max != null) ? (max.intValue() + 1) : 0;
    }
}
