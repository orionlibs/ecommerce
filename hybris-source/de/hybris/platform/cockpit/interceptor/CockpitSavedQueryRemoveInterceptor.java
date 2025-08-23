package de.hybris.platform.cockpit.interceptor;

import de.hybris.platform.cockpit.model.CockpitSavedQueryModel;
import de.hybris.platform.cockpit.model.CockpitUIComponentConfigurationModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import org.springframework.beans.factory.annotation.Required;

public class CockpitSavedQueryRemoveInterceptor implements RemoveInterceptor
{
    private ModelService modelService;
    private FlexibleSearchService flexibleSearchService;


    public void onRemove(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model instanceof CockpitSavedQueryModel)
        {
            CockpitSavedQueryModel query = (CockpitSavedQueryModel)model;
            if(query.getCode() != null)
            {
                removeRelatedListViewConfiguration(query.getCode());
            }
        }
    }


    private void removeRelatedListViewConfiguration(String code)
    {
        String configCode = "listViewContentBrowser_" + code;
        String query = "SELECT {c.pk} FROM {CockpitUIComponentConfiguration AS c } WHERE {c.code}='" + configCode + "'";
        SearchResult<CockpitUIComponentConfigurationModel> relatedConfiguration = this.flexibleSearchService.search(query);
        this.modelService.removeAll(relatedConfiguration.getResult());
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }
}
