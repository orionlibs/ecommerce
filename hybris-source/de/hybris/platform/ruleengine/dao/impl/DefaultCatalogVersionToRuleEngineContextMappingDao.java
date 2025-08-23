package de.hybris.platform.ruleengine.dao.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.ruleengine.dao.CatalogVersionToRuleEngineContextMappingDao;
import de.hybris.platform.ruleengine.enums.RuleType;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineContextModel;
import de.hybris.platform.ruleengine.model.CatalogVersionToRuleEngineContextMappingModel;
import de.hybris.platform.ruleengine.model.DroolsRuleEngineContextModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;

public class DefaultCatalogVersionToRuleEngineContextMappingDao extends AbstractItemDao implements CatalogVersionToRuleEngineContextMappingDao
{
    private static final String SELECT = "SELECT {pk} from {CatalogVersionToRuleEngineContextMapping} ";
    private static final String WHERE_CATALOG_VERSION_IN_TEMPLATE = "WHERE {catalogVersion} IN (?catalogVersions)";
    private static final String WHERE_CONTEXT_IN_TEMPLATE = "WHERE {context} IN (?contexts)";


    public Collection<CatalogVersionToRuleEngineContextMappingModel> findMappingsByCatalogVersion(Collection<CatalogVersionModel> catalogVersions, RuleType ruleType)
    {
        if(CollectionUtils.isEmpty(catalogVersions))
        {
            return Collections.emptyList();
        }
        String queryString = "SELECT {pk} from {CatalogVersionToRuleEngineContextMapping} WHERE {catalogVersion} IN (?catalogVersions)";
        FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT {pk} from {CatalogVersionToRuleEngineContextMapping} WHERE {catalogVersion} IN (?catalogVersions)");
        query.addQueryParameter("catalogVersions", catalogVersions);
        List<CatalogVersionToRuleEngineContextMappingModel> result = getFlexibleSearchService().search(query).getResult();
        if(Objects.nonNull(ruleType))
        {
            result = (List<CatalogVersionToRuleEngineContextMappingModel>)result.stream().filter(m -> filterMappingByRuleType(m.getContext(), ruleType)).collect(Collectors.toList());
        }
        return result;
    }


    public Collection<CatalogVersionToRuleEngineContextMappingModel> findByContext(Collection<AbstractRuleEngineContextModel> contexts)
    {
        if(CollectionUtils.isEmpty(contexts))
        {
            return Collections.emptyList();
        }
        String queryString = "SELECT {pk} from {CatalogVersionToRuleEngineContextMapping} WHERE {context} IN (?contexts)";
        FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT {pk} from {CatalogVersionToRuleEngineContextMapping} WHERE {context} IN (?contexts)");
        query.addQueryParameter("contexts", contexts);
        return getFlexibleSearchService()
                        .search(query).getResult();
    }


    protected boolean filterMappingByRuleType(AbstractRuleEngineContextModel abstractContext, RuleType ruleType)
    {
        if(abstractContext instanceof DroolsRuleEngineContextModel)
        {
            DroolsRuleEngineContextModel context = (DroolsRuleEngineContextModel)abstractContext;
            return (context.getKieSession().getKieBase() != null && context
                            .getKieSession().getKieBase().getKieModule() != null && ruleType
                            .equals(context.getKieSession().getKieBase().getKieModule().getRuleType()));
        }
        return false;
    }
}
