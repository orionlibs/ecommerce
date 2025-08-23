package de.hybris.platform.adaptivesearch.model.interceptors;

import de.hybris.platform.adaptivesearch.model.AsCategoryAwareSearchConfigurationModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import org.apache.commons.lang3.StringUtils;

public class AsCategoryAwareSearchConfigurationInterceptor extends AbstractAsInterceptor implements PrepareInterceptor<AsCategoryAwareSearchConfigurationModel>
{
    public void onPrepare(AsCategoryAwareSearchConfigurationModel searchConfiguration, InterceptorContext context)
    {
        updateUniqueIdx(searchConfiguration);
    }


    protected void updateUniqueIdx(AsCategoryAwareSearchConfigurationModel searchConfiguration)
    {
        String previousUniqueIdx = searchConfiguration.getUniqueIdx();
        String uniqueIdx = getAsItemModelHelper().generateCategoryAwareSearchConfigurationUniqueIdx(searchConfiguration);
        if(!StringUtils.equals(previousUniqueIdx, uniqueIdx) && isUniqueIdxChangeAllowed(previousUniqueIdx, uniqueIdx))
        {
            searchConfiguration.setUniqueIdx(uniqueIdx);
        }
    }


    protected boolean isUniqueIdxChangeAllowed(String previousUniqueIdx, String uniqueIdx)
    {
        if(StringUtils.isBlank(previousUniqueIdx))
        {
            return true;
        }
        String previousCategoryIdentifier = StringUtils.substringAfterLast(previousUniqueIdx, "_");
        String categoryIdentifier = StringUtils.substringAfterLast(uniqueIdx, "_");
        boolean isPreviousCategoryNullIdentifier = StringUtils.equals(previousCategoryIdentifier, "null");
        boolean isCategoryNullIdentifier = StringUtils.equals(categoryIdentifier, "null");
        return (isPreviousCategoryNullIdentifier == isCategoryNullIdentifier);
    }
}
