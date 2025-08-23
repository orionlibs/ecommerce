package de.hybris.platform.adaptivesearchbackoffice.editors.promotedsorts;

import de.hybris.platform.adaptivesearch.data.AbstractAsSortConfiguration;
import de.hybris.platform.adaptivesearch.data.AsConfigurationHolder;
import de.hybris.platform.adaptivesearch.data.AsPromotedSort;
import de.hybris.platform.adaptivesearch.data.AsSearchProfileResult;
import de.hybris.platform.adaptivesearch.data.AsSearchResultData;
import de.hybris.platform.adaptivesearch.data.AsSortData;
import de.hybris.platform.adaptivesearch.model.AbstractAsConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel;
import de.hybris.platform.adaptivesearch.model.AbstractAsSortConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AsPromotedSortModel;
import de.hybris.platform.adaptivesearch.util.MergeMap;
import de.hybris.platform.adaptivesearchbackoffice.data.AbstractSortConfigurationEditorData;
import de.hybris.platform.adaptivesearchbackoffice.data.PromotedSortEditorData;
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

public class AsPromotedSortsDataHandler extends AbstractDataHandler<PromotedSortEditorData, AsPromotedSortModel>
{
    private I18NService i18NService;


    public String getTypeCode()
    {
        return "AsPromotedSort";
    }


    protected PromotedSortEditorData createEditorData()
    {
        PromotedSortEditorData editorData = new PromotedSortEditorData();
        editorData.setValid(true);
        return editorData;
    }


    protected void loadDataFromSearchResult(Map<Object, PromotedSortEditorData> mapping, SearchResultData searchResult, Map<String, Object> parameters)
    {
        if(searchResult == null || searchResult.getAsSearchResult() == null)
        {
            return;
        }
        AsSearchResultData asSearchResult = searchResult.getAsSearchResult();
        AsSearchProfileResult searchProfileResult = asSearchResult.getSearchProfileResult();
        if(searchProfileResult != null && MapUtils.isNotEmpty(searchProfileResult.getPromotedSorts()))
        {
            AbstractAsSearchProfileModel searchProfile = (AbstractAsSearchProfileModel)parameters.get("searchProfile");
            for(AsConfigurationHolder<AsPromotedSort, AbstractAsSortConfiguration> promotedSortHolder : (Iterable<AsConfigurationHolder<AsPromotedSort, AbstractAsSortConfiguration>>)((MergeMap)searchProfileResult
                            .getPromotedSorts()).orderedValues())
            {
                AsPromotedSort promotedSort = (AsPromotedSort)promotedSortHolder.getConfiguration();
                PromotedSortEditorData editorData = (PromotedSortEditorData)getOrCreateEditorData(mapping, promotedSort.getCode());
                convertFromSearchProfileResult(promotedSortHolder, editorData, searchProfile);
            }
        }
        if(CollectionUtils.isNotEmpty(asSearchResult.getAvailableSorts()))
        {
            for(AsSortData sort : asSearchResult.getAvailableSorts())
            {
                PromotedSortEditorData editorData = mapping.get(sort.getCode());
                if(editorData != null)
                {
                    convertFromSearchResult(sort, editorData);
                }
            }
        }
    }


    protected void loadDataFromInitialValue(Map<Object, PromotedSortEditorData> mapping, Collection<AsPromotedSortModel> initialValue, Map<String, Object> parameters)
    {
        if(CollectionUtils.isNotEmpty(initialValue))
        {
            for(AsPromotedSortModel promotedSort : initialValue)
            {
                PromotedSortEditorData editorData = (PromotedSortEditorData)getOrCreateEditorData(mapping, promotedSort.getCode());
                convertFromModel(promotedSort, editorData);
            }
        }
    }


    protected void convertFromSearchProfileResult(AsConfigurationHolder<AsPromotedSort, AbstractAsSortConfiguration> source, PromotedSortEditorData target, AbstractAsSearchProfileModel searchProfile)
    {
        AsPromotedSort promotedSort = (AsPromotedSort)source.getConfiguration();
        target.setUid(promotedSort.getUid());
        target.setCode(promotedSort.getCode());
        target.setName(promotedSort.getName());
        target.setPriority(promotedSort.getPriority());
        target.setSortConfiguration((AbstractAsSortConfiguration)promotedSort);
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


    protected void convertFromSearchResult(AsSortData source, PromotedSortEditorData target)
    {
        target.setInSearchResult(true);
    }


    protected void convertFromModel(AsPromotedSortModel source, PromotedSortEditorData target)
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


    protected void postLoadData(Collection<AsPromotedSortModel> initialValue, SearchResultData searchResult, Map<String, Object> parameters, ListModelList<PromotedSortEditorData> data)
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
