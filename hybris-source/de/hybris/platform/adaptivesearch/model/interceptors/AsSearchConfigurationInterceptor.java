package de.hybris.platform.adaptivesearch.model.interceptors;

import de.hybris.platform.adaptivesearch.model.AbstractAsSearchConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import java.util.Objects;

public class AsSearchConfigurationInterceptor extends AbstractAsInterceptor implements PrepareInterceptor<AbstractAsSearchConfigurationModel>, ValidateInterceptor<AbstractAsSearchConfigurationModel>, RemoveInterceptor<AbstractAsSearchConfigurationModel>
{
    public void onPrepare(AbstractAsSearchConfigurationModel searchConfiguration, InterceptorContext context) throws InterceptorException
    {
        markItemAsModified(context, (ItemModel)searchConfiguration, new String[] {"searchProfile"});
    }


    public void onValidate(AbstractAsSearchConfigurationModel searchConfiguration, InterceptorContext context) throws InterceptorException
    {
        CatalogVersionModel catalogVersion = searchConfiguration.getCatalogVersion();
        AbstractAsSearchProfileModel searchProfile = getAsItemModelHelper().getSearchProfileForSearchConfiguration(searchConfiguration);
        if(searchProfile != null && !Objects.equals(catalogVersion, searchProfile.getCatalogVersion()))
        {
            throw new InterceptorException("Invalid catalog version: " + catalogVersion
                            .getCatalog() + ":" + catalogVersion.getVersion());
        }
    }


    public void onRemove(AbstractAsSearchConfigurationModel searchConfiguration, InterceptorContext context) throws InterceptorException
    {
        AbstractAsSearchProfileModel searchProfile = getAsItemModelHelper().getSearchProfileForSearchConfiguration(searchConfiguration);
        markItemAsModified(context, (ItemModel)searchProfile, new String[0]);
    }
}
