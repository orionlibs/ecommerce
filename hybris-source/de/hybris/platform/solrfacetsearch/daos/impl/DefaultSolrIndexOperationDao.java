package de.hybris.platform.solrfacetsearch.daos.impl;

import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.solrfacetsearch.daos.SolrIndexOperationDao;
import de.hybris.platform.solrfacetsearch.enums.IndexerOperationStatus;
import de.hybris.platform.solrfacetsearch.enums.IndexerOperationValues;
import de.hybris.platform.solrfacetsearch.model.SolrIndexModel;
import de.hybris.platform.solrfacetsearch.model.SolrIndexOperationModel;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DefaultSolrIndexOperationDao extends DefaultGenericDao<SolrIndexOperationModel> implements SolrIndexOperationDao
{
    protected static final String OPERATIONS_PARAM = "operations";
    protected static final String LAST_SUCCESSFUL_OPERATION_QUERY = "SELECT {pk} FROM {SolrIndexOperation} WHERE {index} = ?index AND {operation} IN (?operations) AND {status} = ?status AND {external} = ?external ORDER BY {startTime} DESC";


    public DefaultSolrIndexOperationDao()
    {
        super("SolrIndexOperation");
    }


    public SolrIndexOperationModel findIndexOperationById(long id)
    {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("id", Long.valueOf(id));
        Collection<SolrIndexOperationModel> operations = find(queryParams);
        ServicesUtil.validateIfSingleResult(operations, "operation not found: " + queryParams.toString(), "more than one operation was found: " + queryParams
                        .toString());
        return operations.iterator().next();
    }


    public Optional<SolrIndexOperationModel> findLastSuccesfulIndexOperation(SolrIndexModel index)
    {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("index", index);
        queryParams.put("operations", Arrays.asList(new IndexerOperationValues[] {IndexerOperationValues.FULL, IndexerOperationValues.UPDATE}));
        queryParams.put("status", IndexerOperationStatus.SUCCESS);
        queryParams.put("external", Boolean.valueOf(false));
        FlexibleSearchQuery searchQuery = new FlexibleSearchQuery("SELECT {pk} FROM {SolrIndexOperation} WHERE {index} = ?index AND {operation} IN (?operations) AND {status} = ?status AND {external} = ?external ORDER BY {startTime} DESC", queryParams);
        searchQuery.setCount(1);
        searchQuery.setNeedTotal(false);
        List<SolrIndexOperationModel> operations = getFlexibleSearchService().search(searchQuery).getResult();
        if(operations.isEmpty())
        {
            return Optional.empty();
        }
        return Optional.of(operations.get(0));
    }
}
