package com.hybris.backoffice.searchservices.locale;

import com.hybris.backoffice.search.services.BackofficeFacetSearchConfigService;
import com.hybris.backoffice.searchservices.model.BackofficeIndexedTypeToSearchservicesIndexConfigModel;
import com.hybris.backoffice.widgets.quicktogglelocale.controller.IndexedLanguagesResolver;
import de.hybris.platform.searchservices.model.SnIndexConfigurationModel;
import de.hybris.platform.util.Config;
import java.util.Collection;
import org.apache.commons.collections.CollectionUtils;

public class SearchservicesIndexedLanguagesResolver implements IndexedLanguagesResolver
{
    private BackofficeFacetSearchConfigService backofficeFacetSearchConfigService;


    public boolean isIndexed(String isoCode)
    {
        String currentSearchStrategy = Config.getString("backoffice.fulltext.search.strategy", "");
        if(!currentSearchStrategy.equals("searchservices"))
        {
            return true;
        }
        Collection mappedFacetSearchConfigs = this.backofficeFacetSearchConfigService.getAllMappedFacetSearchConfigs();
        if(CollectionUtils.isEmpty(mappedFacetSearchConfigs))
        {
            return true;
        }
        return mappedFacetSearchConfigs.stream().allMatch(mappedFacetSearchConfig -> {
            BackofficeIndexedTypeToSearchservicesIndexConfigModel configModel = (BackofficeIndexedTypeToSearchservicesIndexConfigModel)mappedFacetSearchConfig;
            SnIndexConfigurationModel snIndexConfigurationModel = configModel.getSnIndexConfiguration();
            return isLanguageAvailableFor(isoCode, snIndexConfigurationModel);
        });
    }


    private boolean isLanguageAvailableFor(String isoCode, SnIndexConfigurationModel snIndexConfigurationModel)
    {
        return snIndexConfigurationModel.getLanguages().stream().anyMatch(languageModel -> languageModel.getIsocode().equals(isoCode));
    }


    public BackofficeFacetSearchConfigService getBackofficeFacetSearchConfigService()
    {
        return this.backofficeFacetSearchConfigService;
    }


    public void setBackofficeFacetSearchConfigService(BackofficeFacetSearchConfigService backofficeFacetSearchConfigService)
    {
        this.backofficeFacetSearchConfigService = backofficeFacetSearchConfigService;
    }
}
