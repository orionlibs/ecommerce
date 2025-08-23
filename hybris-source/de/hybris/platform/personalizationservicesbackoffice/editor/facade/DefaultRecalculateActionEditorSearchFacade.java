package de.hybris.platform.personalizationservicesbackoffice.editor.facade;

import com.google.common.collect.Lists;
import com.hybris.cockpitng.editor.commonreferenceeditor.ReferenceEditorSearchFacade;
import com.hybris.cockpitng.search.data.SearchQueryData;
import com.hybris.cockpitng.search.data.pageable.Pageable;
import com.hybris.cockpitng.search.data.pageable.PageableList;
import de.hybris.platform.personalizationservices.RecalculateAction;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DefaultRecalculateActionEditorSearchFacade implements ReferenceEditorSearchFacade<String>
{
    public Pageable<String> search(SearchQueryData searchQueryData)
    {
        List<String> recalculateActionNames = getFilteredEnumValues(searchQueryData.getSearchQueryText());
        return (Pageable<String>)new PageableList(recalculateActionNames, searchQueryData.getPageSize());
    }


    protected List<String> getFilteredEnumValues(String textQuery)
    {
        List<String> collect = (List<String>)Stream.<RecalculateAction>of(RecalculateAction.values()).map(Enum::name).collect(Collectors.toList());
        return filterEnumValues(collect, textQuery);
    }


    protected List<String> filterEnumValues(List<String> values, String textQuery)
    {
        List<String> result = Lists.newArrayList(values);
        return (List<String>)result.stream().filter(v -> {
            String txtQuery = (textQuery == null) ? "" : textQuery;
            return v.toLowerCase(Locale.ROOT).contains(txtQuery.toLowerCase(Locale.ROOT));
        }).collect(Collectors.toList());
    }
}
