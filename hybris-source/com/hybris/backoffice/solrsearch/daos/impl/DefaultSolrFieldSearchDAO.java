package com.hybris.backoffice.solrsearch.daos.impl;

import com.google.common.collect.Lists;
import com.hybris.backoffice.solrsearch.daos.SolrFieldSearchDAO;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

@Deprecated(since = "2105", forRemoval = true)
public class DefaultSolrFieldSearchDAO implements SolrFieldSearchDAO
{
    private static final String FIND_CONTAINING_PKS = "SELECT {t:pk} FROM {%s AS t} WHERE {t:pk} in (?pks)";
    private FlexibleSearchService flexibleSearchService;


    public List<ItemModel> findAll(String typeCode, List<Long> itemsPks)
    {
        FlexibleSearchQuery query = new FlexibleSearchQuery(String.format("SELECT {t:pk} FROM {%s AS t} WHERE {t:pk} in (?pks)", new Object[] {typeCode}));
        query.addQueryParameter("pks", itemsPks);
        List<ItemModel> items = this.flexibleSearchService.search(query).getResult();
        return CollectionUtils.isNotEmpty(items) ? orderItemsByPkList(items, itemsPks) : Lists.newArrayList();
    }


    protected List<ItemModel> orderItemsByPkList(List<ItemModel> items, List<Long> itemsPks)
    {
        Comparator<ItemModel> pkComparator = (left, right) -> Long.compare(itemsPks.indexOf(left.getPk().getLong()), itemsPks.indexOf(right.getPk().getLong()));
        return (List<ItemModel>)items.stream().sorted(pkComparator).collect(Collectors.toList());
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }
}
