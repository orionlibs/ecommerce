package de.hybris.platform.ruleengine.dao.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import de.hybris.platform.ruleengine.dao.RulesModuleDao;
import de.hybris.platform.ruleengine.enums.RuleType;
import de.hybris.platform.ruleengine.model.AbstractRulesModuleModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.List;
import java.util.Map;

public class DefaultRulesModuleDao extends AbstractItemDao implements RulesModuleDao
{
    private static final String FIND_ACTIVE_MODULE_BY_NAME = "select {pk} from {AbstractRulesModule} where {name} = ?name and {active} = ?active";
    private static final String FIND_ALL_ACTIVE_MODULES = "select {pk} from {AbstractRulesModule} where {active} = ?active";
    private static final String FIND_ALL_ACTIVE_MODULES_BY_RULE_TYPE = "select {pk} from {AbstractRulesModule} where {active} = ?active and {ruleType} = ?ruleType";
    private static final String FIND_MODULE_BY_NAME_AND_VERSION = "select {pk} from {AbstractRulesModule} where {name} = ?name and {version} = ?version";


    public <T extends AbstractRulesModuleModel> T findByName(String name)
    {
        Map<String, Object> queryParams = Maps.newHashMap();
        queryParams.put("name", name);
        queryParams.put("active", Boolean.TRUE);
        FlexibleSearchQuery query = new FlexibleSearchQuery("select {pk} from {AbstractRulesModule} where {name} = ?name and {active} = ?active", queryParams);
        return (T)getFlexibleSearchService().searchUnique(query);
    }


    public List<AbstractRulesModuleModel> findAll()
    {
        Map<String, Object> queryParams = Maps.newHashMap();
        queryParams.put("active", Boolean.TRUE);
        FlexibleSearchQuery query = new FlexibleSearchQuery("select {pk} from {AbstractRulesModule} where {active} = ?active", queryParams);
        SearchResult<AbstractRulesModuleModel> search = getFlexibleSearchService().search(query);
        return search.getResult();
    }


    public List<AbstractRulesModuleModel> findActiveRulesModulesByRuleType(RuleType ruleType)
    {
        ServicesUtil.validateParameterNotNull(ruleType, "Rule type must be not null");
        Map<String, Object> queryParams = Maps.newHashMap();
        queryParams.put("active", Boolean.TRUE);
        queryParams.put("ruleType", ruleType);
        FlexibleSearchQuery query = new FlexibleSearchQuery("select {pk} from {AbstractRulesModule} where {active} = ?active and {ruleType} = ?ruleType", queryParams);
        SearchResult<AbstractRulesModuleModel> search = getFlexibleSearchService().search(query);
        return search.getResult();
    }


    public <T extends AbstractRulesModuleModel> T findByNameAndVersion(String name, long version)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("name", name);
        Preconditions.checkArgument((version >= 0L), "provided [version] must be positive: %s", version);
        Map<String, Object> queryParams = Maps.newHashMap();
        queryParams.put("name", name);
        queryParams.put("version", Long.valueOf(version));
        FlexibleSearchQuery query = new FlexibleSearchQuery("select {pk} from {AbstractRulesModule} where {name} = ?name and {version} = ?version", queryParams);
        T ruleModule = null;
        try
        {
            AbstractRulesModuleModel abstractRulesModuleModel = (AbstractRulesModuleModel)getFlexibleSearchService().searchUnique(query);
        }
        catch(ModelNotFoundException e)
        {
            ruleModule = null;
        }
        return ruleModule;
    }
}
