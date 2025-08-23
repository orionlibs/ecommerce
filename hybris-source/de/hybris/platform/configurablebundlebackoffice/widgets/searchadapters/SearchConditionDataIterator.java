package de.hybris.platform.configurablebundlebackoffice.widgets.searchadapters;

import com.google.common.collect.Lists;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import com.hybris.backoffice.widgets.advancedsearch.impl.SearchConditionData;
import com.hybris.backoffice.widgets.advancedsearch.impl.SearchConditionDataList;
import java.util.Iterator;
import java.util.List;

public class SearchConditionDataIterator
{
    protected static Iterator<SearchConditionData> createSearchConditionDataIterator(List<SearchConditionData> data)
    {
        List<SearchConditionData> conditions = Lists.newArrayList();
        for(SearchConditionData item : data)
        {
            if(SearchConditionDataList.class.isInstance(item))
            {
                SearchConditionDataList actual = (SearchConditionDataList)item;
                conditions.addAll(Lists.newArrayList(createSearchConditionDataIterator(actual.getConditions())));
                continue;
            }
            conditions.add(item);
        }
        return conditions.iterator();
    }


    public static Iterator<SearchConditionData> createSearchConditionDataIterator(AdvancedSearchData searchData)
    {
        List<SearchConditionData> condition = searchData.getConditions("_orphanedSearchConditions");
        return createSearchConditionDataIterator(condition);
    }
}
