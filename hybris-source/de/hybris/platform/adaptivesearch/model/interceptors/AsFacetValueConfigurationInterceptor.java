package de.hybris.platform.adaptivesearch.model.interceptors;

import de.hybris.platform.adaptivesearch.model.AbstractAsFacetConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AbstractAsFacetValueConfigurationModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import java.util.Objects;
import org.apache.commons.lang.StringUtils;

public class AsFacetValueConfigurationInterceptor extends AbstractAsInterceptor implements PrepareInterceptor<AbstractAsFacetValueConfigurationModel>, ValidateInterceptor<AbstractAsFacetValueConfigurationModel>, RemoveInterceptor<AbstractAsFacetValueConfigurationModel>
{
    public void onPrepare(AbstractAsFacetValueConfigurationModel facetValueConfiguration, InterceptorContext context) throws InterceptorException
    {
        updateUniqueIdx(facetValueConfiguration);
        markItemAsModified(context, (ItemModel)facetValueConfiguration, new String[] {"facetConfiguration", "searchConfiguration", "searchProfile"});
    }


    protected void updateUniqueIdx(AbstractAsFacetValueConfigurationModel facetValueConfiguration)
    {
        String previousUniqueIdx = facetValueConfiguration.getUniqueIdx();
        String uniqueIdx = getAsItemModelHelper().generateFacetValueConfigurationUniqueIdx(facetValueConfiguration);
        if(!StringUtils.equals(previousUniqueIdx, uniqueIdx))
        {
            facetValueConfiguration.setUniqueIdx(uniqueIdx);
        }
    }


    public void onValidate(AbstractAsFacetValueConfigurationModel facetValueConfiguration, InterceptorContext context) throws InterceptorException
    {
        CatalogVersionModel catalogVersion = facetValueConfiguration.getCatalogVersion();
        AbstractAsFacetConfigurationModel facetConfiguration = resolveAndValidateFacetConfiguration((ItemModel)facetValueConfiguration);
        if(facetConfiguration != null && !Objects.equals(catalogVersion, facetConfiguration.getCatalogVersion()))
        {
            throw new InterceptorException("Invalid catalog version: " + catalogVersion
                            .getCatalog() + ":" + catalogVersion.getVersion());
        }
        String value = facetValueConfiguration.getValue();
        if(StringUtils.isEmpty(value))
        {
            throw new InterceptorException("value does not exist: " + value);
        }
    }


    public void onRemove(AbstractAsFacetValueConfigurationModel facetValueConfiguration, InterceptorContext context) throws InterceptorException
    {
        AbstractAsFacetConfigurationModel facetConfiguration = resolveFacetConfiguration((ItemModel)facetValueConfiguration);
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
