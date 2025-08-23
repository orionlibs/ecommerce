package de.hybris.platform.adaptivesearch.model.interceptors;

import de.hybris.platform.adaptivesearch.enums.AsFacetType;
import de.hybris.platform.adaptivesearch.model.AbstractAsFacetConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AbstractAsSearchConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProvider;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;

public class AsFacetConfigurationInterceptor extends AbstractAsInterceptor implements PrepareInterceptor<AbstractAsFacetConfigurationModel>, ValidateInterceptor<AbstractAsFacetConfigurationModel>, RemoveInterceptor<AbstractAsFacetConfigurationModel>
{
    public void onPrepare(AbstractAsFacetConfigurationModel facetConfiguration, InterceptorContext context) throws InterceptorException
    {
        updateUniqueIdx(facetConfiguration);
        markItemAsModified(context, (ItemModel)facetConfiguration, new String[] {"searchConfiguration", "searchProfile"});
    }


    protected void updateUniqueIdx(AbstractAsFacetConfigurationModel facetConfiguration)
    {
        String previousUniqueIdx = facetConfiguration.getUniqueIdx();
        String uniqueIdx = getAsItemModelHelper().generateFacetConfigurationUniqueIdx(facetConfiguration);
        if(!StringUtils.equals(previousUniqueIdx, uniqueIdx))
        {
            facetConfiguration.setUniqueIdx(uniqueIdx);
        }
    }


    public void onValidate(AbstractAsFacetConfigurationModel facetConfiguration, InterceptorContext context) throws InterceptorException
    {
        CatalogVersionModel catalogVersion = facetConfiguration.getCatalogVersion();
        AbstractAsSearchConfigurationModel searchConfiguration = getAsItemModelHelper().getSearchConfigurationForFacetConfiguration(facetConfiguration);
        if(searchConfiguration != null && !Objects.equals(catalogVersion, searchConfiguration.getCatalogVersion()))
        {
            throw new InterceptorException("Invalid catalog version: " + catalogVersion
                            .getCatalog() + ":" + catalogVersion.getVersion());
        }
        AbstractAsSearchProfileModel searchProfile = getAsItemModelHelper().getSearchProfileForSearchConfiguration(searchConfiguration);
        if(searchProfile == null)
        {
            throw new InterceptorException("Invalid search profile");
        }
        String indexType = searchProfile.getIndexType();
        String indexProperty = facetConfiguration.getIndexProperty();
        AsSearchProvider searchProvider = resolveSearchProvider();
        if(!searchProvider.isValidFacetExpression(indexType, indexProperty))
        {
            throw new InterceptorException("Expression cannot be used for facets: " + indexProperty);
        }
        AsFacetType facetType = facetConfiguration.getFacetType();
        if(!searchProvider.isValidFacetType(indexType, facetType))
        {
            throw new InterceptorException("Facet type cannot be used for facets: " + facetType);
        }
        String sort = facetConfiguration.getSort();
        if(sort != null && !searchProvider.isValidFacetSort(indexType, sort))
        {
            throw new InterceptorException("Sort cannot be used for facets: " + sort);
        }
    }


    public void onRemove(AbstractAsFacetConfigurationModel facetConfiguration, InterceptorContext context) throws InterceptorException
    {
        AbstractAsSearchConfigurationModel searchConfiguration = getAsItemModelHelper().getSearchConfigurationForFacetConfiguration(facetConfiguration);
        markItemAsModified(context, (ItemModel)searchConfiguration, new String[] {"searchProfile"});
    }
}
