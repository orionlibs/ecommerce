package de.hybris.platform.adaptivesearchbackoffice.editors.sorts;

import de.hybris.platform.adaptivesearch.data.AbstractAsSortConfiguration;
import de.hybris.platform.adaptivesearch.data.AsConfigurationHolder;
import de.hybris.platform.adaptivesearch.data.AsSearchProfileResult;
import de.hybris.platform.adaptivesearch.data.AsSearchResultData;
import de.hybris.platform.adaptivesearch.data.AsSort;
import de.hybris.platform.adaptivesearch.data.AsSortData;
import de.hybris.platform.adaptivesearch.model.AbstractAsConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel;
import de.hybris.platform.adaptivesearch.model.AbstractAsSortConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AsSortModel;
import de.hybris.platform.adaptivesearch.util.MergeMap;
import de.hybris.platform.adaptivesearchbackoffice.data.AbstractSortConfigurationEditorData;
import de.hybris.platform.adaptivesearchbackoffice.data.SearchContextData;
import de.hybris.platform.adaptivesearchbackoffice.data.SearchResultData;
import de.hybris.platform.adaptivesearchbackoffice.data.SortEditorData;
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
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zul.ListModelList;

public class AsSortsDataHandler extends AbstractDataHandler<SortEditorData, AsSortModel>
{
    private I18NService i18NService;


    public String getTypeCode()
    {
        return "AsSort";
    }


    protected SortEditorData createEditorData()
    {
        SortEditorData editorData = new SortEditorData();
        editorData.setValid(true);
        return editorData;
    }


    protected void loadDataFromSearchResult(Map<Object, SortEditorData> mapping, SearchResultData searchResult, Map<String, Object> parameters)
    {
        if(searchResult == null || searchResult.getAsSearchResult() == null)
        {
            return;
        }
        AsSearchResultData asSearchResult = searchResult.getAsSearchResult();
        AsSearchProfileResult searchProfileResult = asSearchResult.getSearchProfileResult();
        if(searchProfileResult != null && MapUtils.isNotEmpty(searchProfileResult.getSorts()))
        {
            AbstractAsSearchProfileModel searchProfile = (AbstractAsSearchProfileModel)parameters.get("searchProfile");
            for(AsConfigurationHolder<AsSort, AbstractAsSortConfiguration> sortHolder : (Iterable<AsConfigurationHolder<AsSort, AbstractAsSortConfiguration>>)((MergeMap)searchProfileResult
                            .getSorts()).orderedValues())
            {
                AsSort sort = (AsSort)sortHolder.getConfiguration();
                SortEditorData editorData = (SortEditorData)getOrCreateEditorData(mapping, sort.getCode());
                convertFromSearchProfileResult(sortHolder, editorData, searchProfile);
            }
        }
        if(CollectionUtils.isNotEmpty(asSearchResult.getAvailableSorts()))
        {
            for(AsSortData sort : asSearchResult.getAvailableSorts())
            {
                SortEditorData editorData = mapping.get(sort.getCode());
                if(editorData != null)
                {
                    convertFromSearchResult(sort, editorData);
                }
            }
        }
    }


    protected void loadDataFromInitialValue(Map<Object, SortEditorData> mapping, Collection<AsSortModel> initialValue, Map<String, Object> parameters)
    {
        if(CollectionUtils.isNotEmpty(initialValue))
        {
            for(AsSortModel sort : initialValue)
            {
                SortEditorData editorData = (SortEditorData)getOrCreateEditorData(mapping, sort.getCode());
                convertFromModel(sort, editorData);
            }
        }
    }


    protected void convertFromSearchProfileResult(AsConfigurationHolder<AsSort, AbstractAsSortConfiguration> source, SortEditorData target, AbstractAsSearchProfileModel searchProfile)
    {
        AsSort sort = (AsSort)source.getConfiguration();
        target.setUid(sort.getUid());
        target.setCode(sort.getCode());
        target.setName(sort.getName());
        target.setPriority(sort.getPriority());
        target.setSortConfiguration((AbstractAsSortConfiguration)sort);
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


    protected void convertFromSearchResult(AsSortData source, SortEditorData target)
    {
        target.setInSearchResult(true);
    }


    protected void convertFromModel(AsSortModel source, SortEditorData target)
    {
        if(StringUtils.isBlank(source.getUid()))
        {
            source.setUid(getAsUidGenerator().generateUid());
        }
        target.setUid(source.getUid());
        target.setCode(source.getCode());
        target.setValid(getAsConfigurationService().isValid((AbstractAsConfigurationModel)source));
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


    protected void postLoadData(Collection<AsSortModel> initialValue, SearchResultData searchResult, Map<String, Object> parameters, ListModelList<SortEditorData> data)
    {
        if(data != null)
        {
            if(searchResult != null)
            {
                localizeSorts(searchResult.getSearchContext(), data.getInnerList());
            }
            updateRankAttributes(data.getInnerList());
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


    protected void updateRankAttributes(List<? extends AbstractSortConfigurationEditorData> sorts)
    {
        AbstractSortConfigurationEditorData previousSort = null;
        for(AbstractSortConfigurationEditorData sort : sorts)
        {
            if(previousSort != null && previousSort.getModel() != null && sort.getModel() != null &&
                            ObjectUtils.equals(previousSort.getPriority(), sort.getPriority()))
            {
                previousSort.setRankDownAllowed(true);
                sort.setRankUpAllowed(true);
            }
            previousSort = sort;
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
