package de.hybris.platform.adaptivesearch.model.interceptors;

import de.hybris.platform.adaptivesearch.model.AbstractAsSearchConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AbstractAsSortConfigurationModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;

public class AsSortConfigurationInterceptor extends AbstractAsInterceptor implements PrepareInterceptor<AbstractAsSortConfigurationModel>, ValidateInterceptor<AbstractAsSortConfigurationModel>, RemoveInterceptor<AbstractAsSortConfigurationModel>
{
    public void onPrepare(AbstractAsSortConfigurationModel sortConfiguration, InterceptorContext context) throws InterceptorException
    {
        updateUniqueIdx(sortConfiguration);
        markItemAsModified(context, (ItemModel)sortConfiguration, new String[] {"searchConfiguration", "searchProfile"});
    }


    protected void updateUniqueIdx(AbstractAsSortConfigurationModel sortConfiguration)
    {
        String previousUniqueIdx = sortConfiguration.getUniqueIdx();
        String uniqueIdx = getAsItemModelHelper().generateSortConfigurationUniqueIdx(sortConfiguration);
        if(!StringUtils.equals(previousUniqueIdx, uniqueIdx))
        {
            sortConfiguration.setUniqueIdx(uniqueIdx);
        }
    }


    public void onValidate(AbstractAsSortConfigurationModel sortConfiguration, InterceptorContext context) throws InterceptorException
    {
        CatalogVersionModel catalogVersion = sortConfiguration.getCatalogVersion();
        AbstractAsSearchConfigurationModel searchConfiguration = getAsItemModelHelper().getSearchConfigurationForSortConfiguration(sortConfiguration);
        if(searchConfiguration != null && !Objects.equals(catalogVersion, searchConfiguration.getCatalogVersion()))
        {
            throw new InterceptorException("Invalid catalog version: " + catalogVersion
                            .getCatalog() + ":" + catalogVersion.getVersion());
        }
    }


    public void onRemove(AbstractAsSortConfigurationModel sortConfiguration, InterceptorContext context) throws InterceptorException
    {
        AbstractAsSearchConfigurationModel searchConfiguration = getAsItemModelHelper().getSearchConfigurationForSortConfiguration(sortConfiguration);
        markItemAsModified(context, (ItemModel)searchConfiguration, new String[] {"searchProfile"});
    }
}
