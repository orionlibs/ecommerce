package de.hybris.platform.servicelayer.search.paginated.dao.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.core.servicelayer.data.SortData;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.paginated.PaginatedFlexibleSearchParameter;
import de.hybris.platform.servicelayer.search.paginated.PaginatedFlexibleSearchService;
import de.hybris.platform.servicelayer.search.paginated.dao.PaginatedGenericDao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultPaginatedGenericDao<M extends ItemModel> implements PaginatedGenericDao<M>
{
    private final String typeCode;
    private static final String QUERY_ALIAS = "c";
    private PaginatedFlexibleSearchService paginatedFlexibleSearchService;


    public DefaultPaginatedGenericDao(String typeCode)
    {
        this.typeCode = typeCode;
    }


    public SearchPageData<M> find(SearchPageData<M> searchPageData)
    {
        return find(new HashMap<>(), searchPageData);
    }


    public SearchPageData<M> find(Map<String, ?> params, SearchPageData searchPageData)
    {
        PaginatedFlexibleSearchParameter parameter = new PaginatedFlexibleSearchParameter();
        parameter.setFlexibleSearchQuery(createFlexibleSearchQuery(params));
        parameter.setSearchPageData(searchPageData);
        parameter.setSortCodeToQueryAlias(getSortCodeToQueryAlias(searchPageData));
        return getPaginatedFlexibleSearchService().search(parameter);
    }


    protected FlexibleSearchQuery createFlexibleSearchQuery(Map<String, ?> params)
    {
        StringBuilder builder = createQueryString();
        appendWhereClausesToBuilder(builder, params);
        FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString());
        if(params != null && !params.isEmpty())
        {
            query.addQueryParameters(params);
        }
        return query;
    }


    protected StringBuilder createQueryString()
    {
        StringBuilder builder = new StringBuilder(25);
        builder.append("SELECT {").append("c").append(":").append("pk").append("} ");
        builder.append("FROM {").append(this.typeCode).append(" AS ").append("c").append("} ");
        return builder;
    }


    protected void appendWhereClausesToBuilder(StringBuilder builder, Map<String, ?> params)
    {
        if(params != null && !params.isEmpty())
        {
            builder.append("WHERE ");
            boolean firstParam = true;
            for(String paramName : params.keySet())
            {
                if(!firstParam)
                {
                    builder.append("AND ");
                }
                if(params.get(paramName) instanceof java.util.Collection)
                {
                    builder.append("{").append("c").append(":").append(paramName).append("} IN (?").append(paramName)
                                    .append(")").append(' ');
                }
                else
                {
                    builder.append("{").append("c").append(":").append(paramName).append("} = ?").append(paramName)
                                    .append(' ');
                }
                firstParam = false;
            }
        }
    }


    protected Map<String, String> getSortCodeToQueryAlias(SearchPageData searchPageData)
    {
        Map<String, String> sortCodeToQueryAlias = new HashMap<>();
        List<SortData> sorts = searchPageData.getSorts();
        if(!CollectionUtils.isEmpty(sorts))
        {
            for(SortData sortData : sorts)
            {
                sortCodeToQueryAlias.put(sortData.getCode().toLowerCase(), "c");
            }
        }
        return sortCodeToQueryAlias;
    }


    protected PaginatedFlexibleSearchService getPaginatedFlexibleSearchService()
    {
        return this.paginatedFlexibleSearchService;
    }


    @Required
    public void setPaginatedFlexibleSearchService(PaginatedFlexibleSearchService paginatedFlexibleSearchService)
    {
        this.paginatedFlexibleSearchService = paginatedFlexibleSearchService;
    }
}
