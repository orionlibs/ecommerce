package de.hybris.platform.adaptivesearchbackoffice.editors.excludedsorts;

import de.hybris.platform.adaptivesearch.data.AbstractAsSortConfiguration;
import de.hybris.platform.adaptivesearch.data.AsConfigurationHolder;
import de.hybris.platform.adaptivesearch.data.AsExcludedSort;
import de.hybris.platform.adaptivesearch.data.AsSearchProfileResult;
import de.hybris.platform.adaptivesearch.data.AsSearchResultData;
import de.hybris.platform.adaptivesearch.model.AbstractAsConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel;
import de.hybris.platform.adaptivesearch.model.AbstractAsSortConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AsExcludedSortModel;
import de.hybris.platform.adaptivesearch.util.MergeMap;
import de.hybris.platform.adaptivesearchbackoffice.data.AbstractSortConfigurationEditorData;
import de.hybris.platform.adaptivesearchbackoffice.data.ExcludedSortEditorData;
import de.hybris.platform.adaptivesearchbackoffice.data.SearchContextData;
import de.hybris.platform.adaptivesearchbackoffice.data.SearchResultData;
import de.hybris.platform.adaptivesearchbackoffice.editors.configurablemultireference.AbstractDataHandler;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.i18n.I18NService;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zul.ListModelList;

public class AsExcludedSortsDataHandler extends AbstractDataHandler<ExcludedSortEditorData, AsExcludedSortModel>
{
    private I18NService i18NService;


    public String getTypeCode()
    {
        return "AsExcludedSort";
    }


    protected ExcludedSortEditorData createEditorData()
    {
        ExcludedSortEditorData editorData = new ExcludedSortEditorData();
        editorData.setValid(true);
        return editorData;
    }


    protected void loadDataFromSearchResult(Map<Object, ExcludedSortEditorData> mapping, SearchResultData searchResult, Map<String, Object> parameters)
    {
        if(searchResult == null || searchResult.getAsSearchResult() == null)
        {
            return;
        }
        AsSearchResultData asSearchResult = searchResult.getAsSearchResult();
        AsSearchProfileResult searchProfileResult = asSearchResult.getSearchProfileResult();
        if(searchProfileResult != null && MapUtils.isNotEmpty(searchProfileResult.getExcludedSorts()))
        {
            AbstractAsSearchProfileModel searchProfile = (AbstractAsSearchProfileModel)parameters.get("searchProfile");
            for(AsConfigurationHolder<AsExcludedSort, AbstractAsSortConfiguration> excludedSortHolder : (Iterable<AsConfigurationHolder<AsExcludedSort, AbstractAsSortConfiguration>>)((MergeMap)searchProfileResult
                            .getExcludedSorts()).orderedValues())
            {
                AsExcludedSort excludedSort = (AsExcludedSort)excludedSortHolder.getConfiguration();
                ExcludedSortEditorData editorData = (ExcludedSortEditorData)getOrCreateEditorData(mapping, excludedSort.getCode());
                convertFromSearchProfileResult(excludedSortHolder, editorData, searchProfile);
            }
        }
    }


    protected void loadDataFromInitialValue(Map<Object, ExcludedSortEditorData> mapping, Collection<AsExcludedSortModel> initialValue, Map<String, Object> parameters)
    {
        if(CollectionUtils.isNotEmpty(initialValue))
        {
            for(AsExcludedSortModel excludedSort : initialValue)
            {
                ExcludedSortEditorData editorData = (ExcludedSortEditorData)getOrCreateEditorData(mapping, excludedSort.getCode());
                convertFromModel(excludedSort, editorData);
            }
        }
    }


    protected void convertFromSearchProfileResult(AsConfigurationHolder<AsExcludedSort, AbstractAsSortConfiguration> source, ExcludedSortEditorData target, AbstractAsSearchProfileModel searchProfile)
    {
        AsExcludedSort excludedSort = (AsExcludedSort)source.getConfiguration();
        target.setUid(excludedSort.getUid());
        target.setCode(excludedSort.getCode());
        target.setName(excludedSort.getName());
        target.setPriority(excludedSort.getPriority());
        target.setSortConfiguration((AbstractAsSortConfiguration)excludedSort);
        target.setFromSearchProfile(isConfigurationFromSearchProfile((AbstractAsSortConfiguration)source.getConfiguration(), searchProfile));
        AbstractAsSortConfiguration replacedConfiguration = CollectionUtils.isNotEmpty(source.getReplacedConfigurations()) ? source.getReplacedConfigurations().get(0) : null;
        target.setOverride((replacedConfiguration != null));
        target.setOverrideFromSearchProfile(isConfigurationFromSearchProfile(replacedConfiguration, searchProfile));
    }


    protected boolean isConfigurationFromSearchProfile(AbstractAsSortConfiguration configuration, AbstractAsSearchProfileModel searchProfile)
    {
        if(configuration == null || searchProfile == null)
        {
            return false;
        }
        return StringUtils.equals(searchProfile.getCode(), configuration.getSearchProfileCode());
    }


    protected void convertFromModel(AsExcludedSortModel source, ExcludedSortEditorData target)
    {
        if(StringUtils.isBlank(source.getUid()))
        {
            source.setUid(getAsUidGenerator().generateUid());
        }
        target.setUid(source.getUid());
        target.setValid(getAsConfigurationService().isValid((AbstractAsConfigurationModel)source));
        target.setCode(source.getCode());
        target.setName(buildNameLocalizationMap((AbstractAsSortConfigurationModel)source));
        target.setPriority(source.getPriority());
        target.setModel((ItemModel)source);
        target.setFromSearchProfile(true);
        target.setFromSearchConfiguration(true);
    }


    protected Map<String, String> buildNameLocalizationMap(AbstractAsSortConfigurationModel source)
    {
        Set<Locale> supportedLocales = this.i18NService.getSupportedLocales();
        return (Map<String, String>)supportedLocales.stream().filter(locale -> StringUtils.isNotBlank(source.getName(locale)))
                        .collect(Collectors.toMap(Locale::toString, locale -> source.getName(locale)));
    }


    protected void postLoadData(Collection<AsExcludedSortModel> initialValue, SearchResultData searchResult, Map<String, Object> parameters, ListModelList<ExcludedSortEditorData> data)
    {
        if(data != null && searchResult != null)
        {
            localizeSorts(searchResult.getSearchContext(), data.getInnerList());
        }
    }


    protected void localizeSorts(SearchContextData searchContext, List<? extends AbstractSortConfigurationEditorData> sorts)
    {
        String language = (searchContext != null) ? searchContext.getLanguage() : null;
        for(AbstractSortConfigurationEditorData sort : sorts)
        {
            String label = (String)sort.getName().get(language);
            if(StringUtils.isNotBlank(label))
            {
                sort.setLabel(label);
                continue;
            }
            sort.setLabel("[" + sort.getCode() + "]");
        }
    }


    public I18NService getI18NService()
    {
        return this.i18NService;
    }


    @Required
    public void setI18NService(I18NService i18NService)
    {
        this.i18NService = i18NService;
    }
}
