package de.hybris.platform.cms2.servicelayer.interceptor.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

public class ContentSlotForPageValidateInterceptor implements ValidateInterceptor
{
    private FlexibleSearchService flexibleSearchService;


    public void onValidate(Object model, InterceptorContext ctx) throws InterceptorException
    {
        ContentSlotForPageModel csmodel = (ContentSlotForPageModel)model;
        String position = csmodel.getPosition();
        AbstractPageModel page = csmodel.getPage();
        ContentSlotModel contentSlot = csmodel.getContentSlot();
        if(page.getPk() == null || contentSlot.getPk() == null)
        {
            return;
        }
        StringBuilder query = new StringBuilder("SELECT COUNT({csfp.PK}) FROM {  ContentSlotForPage as csfp JOIN  AbstractPage as ap ON {csfp.page}={ap.PK} JOIN  ContentSlot as cs ON {csfp.contentSlot}={cs.PK}} WHERE {csfp.position}=?position AND {ap.PK}=?page AND {cs.PK}=?slot");
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("position", position);
        queryParams.put("page", page);
        queryParams.put("slot", contentSlot);
        if(csmodel.getPk() != null)
        {
            query.append(" AND {csfp.PK} <> ?updatePK");
            queryParams.put("updatePK", csmodel);
        }
        FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query.toString(), queryParams);
        searchQuery.setCatalogVersions(new CatalogVersionModel[] {csmodel.getCatalogVersion()});
        searchQuery.setResultClassList(Collections.singletonList(Integer.class));
        List<Integer> result = this.flexibleSearchService.search(searchQuery).getResult();
        if(!result.isEmpty() && ((Integer)result.get(0)).intValue() > 0)
        {
            throw new InterceptorException("position, page and content slot must be unique");
        }
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    public FlexibleSearchService getFlexibleSearchService()
    {
        return this.flexibleSearchService;
    }
}
