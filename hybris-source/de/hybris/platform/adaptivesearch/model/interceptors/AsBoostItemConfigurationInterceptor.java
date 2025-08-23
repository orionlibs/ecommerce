package de.hybris.platform.adaptivesearch.model.interceptors;

import de.hybris.platform.adaptivesearch.model.AbstractAsBoostItemConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AbstractAsSearchConfigurationModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;

public class AsBoostItemConfigurationInterceptor extends AbstractAsInterceptor implements PrepareInterceptor<AbstractAsBoostItemConfigurationModel>, ValidateInterceptor<AbstractAsBoostItemConfigurationModel>, RemoveInterceptor<AbstractAsBoostItemConfigurationModel>
{
    public void onPrepare(AbstractAsBoostItemConfigurationModel boostItemConfiguration, InterceptorContext context) throws InterceptorException
    {
        updateUniqueIdx(boostItemConfiguration);
        markItemAsModified(context, (ItemModel)boostItemConfiguration, new String[] {"searchConfiguration", "searchProfile"});
    }


    protected void updateUniqueIdx(AbstractAsBoostItemConfigurationModel boostItemConfiguration)
    {
        String previousUniqueIdx = boostItemConfiguration.getUniqueIdx();
        String uniqueIdx = getAsItemModelHelper().generateBoostItemConfigurationUniqueIdx(boostItemConfiguration);
        if(!StringUtils.equals(previousUniqueIdx, uniqueIdx) && isUniqueIdxChangeAllowed(previousUniqueIdx, uniqueIdx))
        {
            boostItemConfiguration.setUniqueIdx(uniqueIdx);
        }
    }


    protected boolean isUniqueIdxChangeAllowed(String previousUniqueIdx, String uniqueIdx)
    {
        if(StringUtils.isBlank(previousUniqueIdx))
        {
            return true;
        }
        String previousItemIdentifier = StringUtils.substringAfterLast(previousUniqueIdx, "_");
        String itemIdentifier = StringUtils.substringAfterLast(uniqueIdx, "_");
        boolean isPreviousItemNullIdentifier = StringUtils.equals(previousItemIdentifier, "null");
        boolean isItemNullIdentifier = StringUtils.equals(itemIdentifier, "null");
        return (isPreviousItemNullIdentifier == isItemNullIdentifier);
    }


    public void onValidate(AbstractAsBoostItemConfigurationModel boostItemConfiguration, InterceptorContext context) throws InterceptorException
    {
        CatalogVersionModel catalogVersion = boostItemConfiguration.getCatalogVersion();
        AbstractAsSearchConfigurationModel searchConfiguration = getAsItemModelHelper().getSearchConfigurationForBoostItemConfiguration(boostItemConfiguration);
        if(searchConfiguration != null && !Objects.equals(catalogVersion, searchConfiguration.getCatalogVersion()))
        {
            throw new InterceptorException("Invalid catalog version: " + catalogVersion
                            .getCatalog() + ":" + catalogVersion.getVersion());
        }
    }


    public void onRemove(AbstractAsBoostItemConfigurationModel boostItemConfiguration, InterceptorContext context) throws InterceptorException
    {
        AbstractAsSearchConfigurationModel searchConfiguration = getAsItemModelHelper().getSearchConfigurationForBoostItemConfiguration(boostItemConfiguration);
        markItemAsModified(context, (ItemModel)searchConfiguration, new String[] {"searchProfile"});
    }
}
