package de.hybris.platform.adaptivesearch.model.interceptors;

import de.hybris.platform.adaptivesearch.model.AbstractAsFacetConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AsFacetRangeModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import java.util.Objects;
import org.apache.commons.lang.StringUtils;

public class AsFacetRangeInterceptor extends AbstractAsInterceptor implements PrepareInterceptor<AsFacetRangeModel>, ValidateInterceptor<AsFacetRangeModel>, RemoveInterceptor<AsFacetRangeModel>
{
    public void onPrepare(AsFacetRangeModel facetRangeModel, InterceptorContext context) throws InterceptorException
    {
        updateUniqueIdx(facetRangeModel);
        markItemAsModified(context, (ItemModel)facetRangeModel, new String[] {"facetConfiguration", "searchConfiguration", "searchProfile"});
    }


    protected void updateUniqueIdx(AsFacetRangeModel facetRange)
    {
        String previousUniqueIdx = facetRange.getUniqueIdx();
        String uniqueIdx = getAsItemModelHelper().generateFacetRangeUniqueIdx(facetRange);
        if(!StringUtils.equals(previousUniqueIdx, uniqueIdx))
        {
            facetRange.setUniqueIdx(uniqueIdx);
        }
    }


    public void onValidate(AsFacetRangeModel facetRangeModel, InterceptorContext context) throws InterceptorException
    {
        CatalogVersionModel catalogVersion = facetRangeModel.getCatalogVersion();
        AbstractAsFacetConfigurationModel facetConfiguration = resolveAndValidateFacetConfiguration((ItemModel)facetRangeModel);
        if(facetConfiguration != null && !Objects.equals(catalogVersion, facetConfiguration.getCatalogVersion()))
        {
            throw new InterceptorException("Invalid catalog version: " + catalogVersion
                            .getCatalog() + ":" + catalogVersion.getVersion());
        }
    }


    public void onRemove(AsFacetRangeModel facetRangeModel, InterceptorContext context) throws InterceptorException
    {
        AbstractAsFacetConfigurationModel facetConfiguration = resolveFacetConfiguration((ItemModel)facetRangeModel);
        markItemAsModified(context, (ItemModel)facetConfiguration, new String[] {"searchConfiguration", "searchProfile"});
    }


    protected AbstractAsFacetConfigurationModel resolveFacetConfiguration(ItemModel model)
    {
        return (AbstractAsFacetConfigurationModel)getModelService().getAttributeValue(model, "facetConfiguration");
    }


    protected AbstractAsFacetConfigurationModel resolveAndValidateFacetConfiguration(ItemModel model) throws InterceptorException
    {
        Object facetConfiguration = getModelService().getAttributeValue(model, "facetConfiguration");
        if(!(facetConfiguration instanceof AbstractAsFacetConfigurationModel))
        {
            throw new InterceptorException("Invalid facet");
        }
        return (AbstractAsFacetConfigurationModel)facetConfiguration;
    }
}
