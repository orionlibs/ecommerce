package de.hybris.platform.adaptivesearch.model.interceptors;

import de.hybris.platform.adaptivesearch.model.AbstractAsSearchConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel;
import de.hybris.platform.adaptivesearch.model.AbstractAsSortConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AsSortExpressionModel;
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

public class AsSortExpressionInterceptor extends AbstractAsInterceptor implements PrepareInterceptor<AsSortExpressionModel>, ValidateInterceptor<AsSortExpressionModel>, RemoveInterceptor<AsSortExpressionModel>
{
    public void onPrepare(AsSortExpressionModel sortExpression, InterceptorContext context) throws InterceptorException
    {
        updateUniqueIdx(sortExpression);
        markItemAsModified(context, (ItemModel)sortExpression, new String[] {"sortConfiguration", "searchConfiguration", "searchProfile"});
    }


    protected void updateUniqueIdx(AsSortExpressionModel sortExpression)
    {
        String previousUniqueIdx = sortExpression.getUniqueIdx();
        String uniqueIdx = getAsItemModelHelper().generateSortExpressionUniqueIdx(sortExpression);
        if(!StringUtils.equals(previousUniqueIdx, uniqueIdx))
        {
            sortExpression.setUniqueIdx(uniqueIdx);
        }
    }


    public void onValidate(AsSortExpressionModel sortExpression, InterceptorContext context) throws InterceptorException
    {
        CatalogVersionModel catalogVersion = sortExpression.getCatalogVersion();
        AbstractAsSortConfigurationModel sortConfiguration = resolveAndValidateSortConfiguration((ItemModel)sortExpression);
        if(!Objects.equals(catalogVersion, sortConfiguration.getCatalogVersion()))
        {
            throw new InterceptorException("Invalid catalog version: " + catalogVersion
                            .getCatalog() + ":" + catalogVersion.getVersion());
        }
        AbstractAsSearchConfigurationModel searchConfiguration = getAsItemModelHelper().getSearchConfigurationForSortConfiguration(sortConfiguration);
        AbstractAsSearchProfileModel searchProfile = getAsItemModelHelper().getSearchProfileForSearchConfiguration(searchConfiguration);
        if(searchProfile == null)
        {
            throw new InterceptorException("Invalid search profile");
        }
        String indexType = searchProfile.getIndexType();
        String expression = sortExpression.getExpression();
        AsSearchProvider searchProvider = resolveSearchProvider();
        if(!searchProvider.isValidSortExpression(indexType, expression))
        {
            throw new InterceptorException("Expression cannot be used for sorts: " + expression);
        }
    }


    public void onRemove(AsSortExpressionModel sortExpression, InterceptorContext context) throws InterceptorException
    {
        AbstractAsSortConfigurationModel sortConfiguration = resolveSortConfiguration((ItemModel)sortExpression);
        markItemAsModified(context, (ItemModel)sortConfiguration, new String[] {"searchConfiguration", "searchProfile"});
    }


    protected AbstractAsSortConfigurationModel resolveSortConfiguration(ItemModel model)
    {
        return (AbstractAsSortConfigurationModel)getModelService().getAttributeValue(model, "sortConfiguration");
    }


    protected AbstractAsSortConfigurationModel resolveAndValidateSortConfiguration(ItemModel model) throws InterceptorException
    {
        Object sortConfiguration = getModelService().getAttributeValue(model, "sortConfiguration");
        if(!(sortConfiguration instanceof AbstractAsSortConfigurationModel))
        {
            throw new InterceptorException("Invalid sort");
        }
        return (AbstractAsSortConfigurationModel)sortConfiguration;
    }
}
