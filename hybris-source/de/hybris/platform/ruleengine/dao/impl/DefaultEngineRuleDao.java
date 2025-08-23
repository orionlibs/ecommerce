package de.hybris.platform.ruleengine.dao.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearchException;
import de.hybris.platform.ruleengine.dao.EngineRuleDao;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengine.model.AbstractRulesModuleModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultEngineRuleDao extends AbstractItemDao implements EngineRuleDao
{
    protected static final String MODULE_NAME = "moduleName";
    protected static final String RULES_MODULE = "ruleModule";
    protected static final String FROM_ALL_WITH_MODULE_NAME = "from {DroolsRule AS r JOIN DroolsKIEBase AS b ON {r.kieBase} = {b.pk} JOIN DroolsKIEModule AS m ON {b.kieModule} = {m.pk}} where {m.name} = ?moduleName";
    protected static final String GET_ALL_RULES_QUERY = "select {pk} from {DroolsRule AS r JOIN DroolsKIEBase AS b ON {r.kieBase} = {b.pk} JOIN DroolsKIEModule AS m ON {b.kieModule} = {m.pk}} where {m.name} = ?moduleName AND {active} = ?active and {currentVersion} = ?currentVersion";
    protected static final String GET_ALL_RULES_FOR_MODULE_QUERY = "select {pk} from {DroolsRule AS r JOIN DroolsKIEBase AS b ON {r.kieBase} = {b.pk} JOIN DroolsKIEModule AS m ON {b.kieModule} = {m.pk}} where {m.name} = ?moduleName AND {active} = ?active and {currentVersion} = ?currentVersion";
    protected static final String GET_RULE_BY_UUID = "select {pk} from {DroolsRule} where  {uuid} = ?uuid";
    protected static final String GET_RULES_BY_MULTIPLE_UUID = "select {pk} from {DroolsRule} where {uuid} IN (?uuids)";
    protected static final String GET_RULES_BY_CODE = "select {pk} from {DroolsRule} where {code} = ?code";
    protected static final String GET_RULE_BY_CODE_AND_MODULE = "select {pk} from {DroolsRule AS r JOIN DroolsKIEBase AS b ON {r.kieBase} = {b.pk} JOIN DroolsKIEModule AS m ON {b.kieModule} = {m.pk}} where {m.name} = ?moduleName AND {code} = ?code";
    protected static final String GET_MAX_VERSION_FOR_CODE = "select MAX({version}) from {DroolsRule AS r JOIN DroolsKIEBase AS b ON {r.kieBase} = {b.pk} JOIN DroolsKIEModule AS m ON {b.kieModule} = {m.pk}} where {m.name} = ?moduleName AND {code} = ?code";
    protected static final String GET_MAX_VERSION_WITH_MODULE = "select MAX( {r.version} ) from {DroolsRule AS r JOIN DroolsKIEBase AS b ON {r.kieBase} = {b.pk} JOIN DroolsKIEModule AS m ON {b.kieModule} = {m.pk}} where {m.name} = ?moduleName";
    protected static final String GET_ALL_RULES_FOR_VERSION = "select {r.pk} from {DroolsRule AS r JOIN DroolsKIEBase AS b ON {r.kieBase} = {b.pk} JOIN DroolsKIEModule AS m ON {b.kieModule} = {m.pk}} where {m.name} = ?moduleName AND {version} <= ?version";
    protected static final String GET_RULES_BETWEEN_VERSIONS = "select {r.pk} from {DroolsRule AS r JOIN DroolsKIEBase AS b ON {r.kieBase} = {b.pk} JOIN DroolsKIEModule AS m ON {b.kieModule} = {m.pk}} where {m.name} = ?moduleName AND {version} <= ?deployedVersion AND {version} > ?startVersion";
    protected static final String GET_ALL_AVAILABLE_KIE_MODULES = "select {pk} from {AbstractRulesModule}";
    private static final Logger LOG = LoggerFactory.getLogger(DefaultEngineRuleDao.class);
    private TimeService timeService;


    public AbstractRuleEngineRuleModel getRuleByUuid(String uuid)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("uuid", uuid);
        Map<String, Object> queryParams = Maps.newHashMap();
        queryParams.put("uuid", uuid);
        FlexibleSearchQuery query = new FlexibleSearchQuery("select {pk} from {DroolsRule} where  {uuid} = ?uuid", queryParams);
        return getWithMaximumVersion(query, -1L);
    }


    public <T extends AbstractRuleEngineRuleModel> Collection<T> getRulesByUuids(Collection<String> ruleUuids)
    {
        Preconditions.checkArgument(CollectionUtils.isNotEmpty(ruleUuids), "A collection of UUIDs shouldn't be null or empty");
        ImmutableMap immutableMap = ImmutableMap.of("uuids", ruleUuids);
        FlexibleSearchQuery query = new FlexibleSearchQuery("select {pk} from {DroolsRule} where {uuid} IN (?uuids)", (Map)immutableMap);
        SearchResult<T> search = getFlexibleSearchService().search(query);
        return search.getResult();
    }


    public List<AbstractRuleEngineRuleModel> findRulesByCode(String code)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("code", code);
        Map<String, Object> queryParams = Maps.newHashMap();
        queryParams.put("code", code);
        FlexibleSearchQuery query = new FlexibleSearchQuery("select {pk} from {DroolsRule} where {code} = ?code", queryParams);
        SearchResult<AbstractRuleEngineRuleModel> search = getFlexibleSearchService().search(query);
        return search.getResult();
    }


    public AbstractRuleEngineRuleModel getRuleByCode(String code, String moduleName)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("code", code);
        ServicesUtil.validateParameterNotNullStandardMessage("moduleName", moduleName);
        Map<String, Object> queryParams = Maps.newHashMap();
        queryParams.put("code", code);
        queryParams.put("moduleName", moduleName);
        FlexibleSearchQuery query = new FlexibleSearchQuery("select {pk} from {DroolsRule AS r JOIN DroolsKIEBase AS b ON {r.kieBase} = {b.pk} JOIN DroolsKIEModule AS m ON {b.kieModule} = {m.pk}} where {m.name} = ?moduleName AND {code} = ?code", queryParams);
        return getWithMaximumVersion(query, -1L);
    }


    public AbstractRuleEngineRuleModel getRuleByCodeAndMaxVersion(String code, String moduleName, long version)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("code", code);
        ServicesUtil.validateParameterNotNullStandardMessage("moduleName", moduleName);
        Map<String, Object> queryParams = Maps.newHashMap();
        queryParams.put("code", code);
        queryParams.put("moduleName", moduleName);
        FlexibleSearchQuery query = new FlexibleSearchQuery("select {pk} from {DroolsRule AS r JOIN DroolsKIEBase AS b ON {r.kieBase} = {b.pk} JOIN DroolsKIEModule AS m ON {b.kieModule} = {m.pk}} where {m.name} = ?moduleName AND {code} = ?code", queryParams);
        return getWithMaximumVersion(query, version);
    }


    public AbstractRuleEngineRuleModel getActiveRuleByCodeAndMaxVersion(String code, String moduleName, long version)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("code", code);
        ServicesUtil.validateParameterNotNullStandardMessage("moduleName", moduleName);
        Map<String, Object> queryParams = Maps.newHashMap();
        queryParams.put("code", code);
        queryParams.put("moduleName", moduleName);
        FlexibleSearchQuery query = new FlexibleSearchQuery("select {pk} from {DroolsRule AS r JOIN DroolsKIEBase AS b ON {r.kieBase} = {b.pk} JOIN DroolsKIEModule AS m ON {b.kieModule} = {m.pk}} where {m.name} = ?moduleName AND {code} = ?code", queryParams);
        AbstractRuleEngineRuleModel ruleForModuleVersion = getWithMaximumVersion(query, version);
        return (Objects.nonNull(ruleForModuleVersion) && ruleForModuleVersion.getActive().booleanValue()) ? ruleForModuleVersion : null;
    }


    public List<AbstractRuleEngineRuleModel> getActiveRules(String moduleName)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("moduleName", moduleName);
        Map<String, Object> queryParams = Maps.newHashMap();
        queryParams.put("active", Boolean.TRUE);
        queryParams.put("currentVersion", Boolean.TRUE);
        queryParams.put("tmsp", getRoundedTimestamp());
        queryParams.put("moduleName", moduleName);
        FlexibleSearchQuery query = new FlexibleSearchQuery("select {pk} from {DroolsRule AS r JOIN DroolsKIEBase AS b ON {r.kieBase} = {b.pk} JOIN DroolsKIEModule AS m ON {b.kieModule} = {m.pk}} where {m.name} = ?moduleName AND {active} = ?active and {currentVersion} = ?currentVersion",
                        queryParams);
        SearchResult<AbstractRuleEngineRuleModel> search = getFlexibleSearchService().search(query);
        return search.getResult();
    }


    public List<AbstractRuleEngineRuleModel> getActiveRules(AbstractRulesModuleModel ruleModule)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("ruleModule", ruleModule);
        String moduleName = ruleModule.getName();
        ServicesUtil.validateParameterNotNullStandardMessage("moduleName", moduleName);
        Map<String, Object> queryParams = Maps.newHashMap();
        queryParams.put("active", Boolean.TRUE);
        queryParams.put("currentVersion", Boolean.TRUE);
        queryParams.put("moduleName", moduleName);
        queryParams.put("tmsp", getRoundedTimestamp());
        FlexibleSearchQuery query = new FlexibleSearchQuery("select {pk} from {DroolsRule AS r JOIN DroolsKIEBase AS b ON {r.kieBase} = {b.pk} JOIN DroolsKIEModule AS m ON {b.kieModule} = {m.pk}} where {m.name} = ?moduleName AND {active} = ?active and {currentVersion} = ?currentVersion",
                        queryParams);
        SearchResult<AbstractRuleEngineRuleModel> search = getFlexibleSearchService().search(query);
        return search.getResult();
    }


    public Long getCurrentRulesSnapshotVersion(AbstractRulesModuleModel ruleModule)
    {
        Long nextRuleVersion;
        ServicesUtil.validateParameterNotNullStandardMessage("ruleModule", ruleModule);
        String moduleName = ruleModule.getName();
        ServicesUtil.validateParameterNotNullStandardMessage("moduleName", moduleName);
        Map<String, Object> queryParams = Maps.newHashMap();
        queryParams.put("moduleName", moduleName);
        FlexibleSearchQuery query = new FlexibleSearchQuery("select MAX( {r.version} ) from {DroolsRule AS r JOIN DroolsKIEBase AS b ON {r.kieBase} = {b.pk} JOIN DroolsKIEModule AS m ON {b.kieModule} = {m.pk}} where {m.name} = ?moduleName", queryParams);
        query.setResultClassList(Collections.singletonList(Long.class));
        try
        {
            nextRuleVersion = (Long)getFlexibleSearchService().searchUnique(query);
        }
        catch(ModelNotFoundException e)
        {
            nextRuleVersion = Long.valueOf(0L);
        }
        return nextRuleVersion;
    }


    public Long getRuleVersion(String code, String moduleName)
    {
        Long nextRuleVersion;
        ServicesUtil.validateParameterNotNullStandardMessage("code", code);
        ServicesUtil.validateParameterNotNullStandardMessage("moduleName", moduleName);
        Map<String, Object> queryParams = Maps.newHashMap();
        queryParams.put("code", code);
        queryParams.put("moduleName", moduleName);
        FlexibleSearchQuery query = new FlexibleSearchQuery("select MAX({version}) from {DroolsRule AS r JOIN DroolsKIEBase AS b ON {r.kieBase} = {b.pk} JOIN DroolsKIEModule AS m ON {b.kieModule} = {m.pk}} where {m.name} = ?moduleName AND {code} = ?code", queryParams);
        query.setResultClassList(Collections.singletonList(Long.class));
        try
        {
            nextRuleVersion = (Long)getFlexibleSearchService().searchUnique(query);
        }
        catch(ModelNotFoundException e)
        {
            nextRuleVersion = null;
        }
        return nextRuleVersion;
    }


    public <T extends AbstractRuleEngineRuleModel> List<T> getRulesForVersion(String moduleName, long version)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("moduleName", moduleName);
        ImmutableMap immutableMap = ImmutableMap.of("version", Long.valueOf(version), "moduleName", moduleName, "tmsp",
                        getRoundedTimestamp());
        return getRulesForVersion("select {r.pk} from {DroolsRule AS r JOIN DroolsKIEBase AS b ON {r.kieBase} = {b.pk} JOIN DroolsKIEModule AS m ON {b.kieModule} = {m.pk}} where {m.name} = ?moduleName AND {version} <= ?version", (Map<String, Object>)immutableMap, version);
    }


    public <T extends AbstractRuleEngineRuleModel> List<T> getRulesBetweenVersions(String moduleName, long startVersion, long deployedVersion)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("moduleName", moduleName);
        Map<String, Object> queryParams = Map.of("moduleName", moduleName, "startVersion", Long.valueOf(startVersion), "deployedVersion",
                        Long.valueOf(deployedVersion), "tmsp", getRoundedTimestamp());
        return getRulesForVersion("select {r.pk} from {DroolsRule AS r JOIN DroolsKIEBase AS b ON {r.kieBase} = {b.pk} JOIN DroolsKIEModule AS m ON {b.kieModule} = {m.pk}} where {m.name} = ?moduleName AND {version} <= ?deployedVersion AND {version} > ?startVersion", queryParams, deployedVersion);
    }


    public <T extends AbstractRuleEngineRuleModel> List<T> getActiveRulesForVersion(String moduleName, long version)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("moduleName", moduleName);
        ImmutableMap immutableMap = ImmutableMap.of("version", Long.valueOf(version), "moduleName", moduleName, "tmsp",
                        getRoundedTimestamp());
        List<T> rulesForModuleVersion = getRulesForVersion("select {r.pk} from {DroolsRule AS r JOIN DroolsKIEBase AS b ON {r.kieBase} = {b.pk} JOIN DroolsKIEModule AS m ON {b.kieModule} = {m.pk}} where {m.name} = ?moduleName AND {version} <= ?version", (Map<String, Object>)immutableMap, version);
        return (List<T>)rulesForModuleVersion.stream().filter(AbstractRuleEngineRuleModel::getActive).collect(Collectors.toList());
    }


    protected <T extends AbstractRuleEngineRuleModel> List<T> getRulesForVersion(String query, Map<String, Object> queryParams, long version)
    {
        FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(query, queryParams);
        try
        {
            SearchResult<T> search = getFlexibleSearchService().search(flexibleSearchQuery);
            List<T> rulesForVersion = search.getResult();
            if(CollectionUtils.isNotEmpty(rulesForVersion))
            {
                List<T> rules = new ArrayList<>();
                Map<String, List<T>> twinRulesMap = (Map<String, List<T>>)rulesForVersion.stream().collect(Collectors.groupingBy(AbstractRuleEngineRuleModel::getCode));
                twinRulesMap.values().stream().map(Collection::stream)
                                .forEach(l -> {
                                    Objects.requireNonNull(rules);
                                    l.max(Comparator.comparing(AbstractRuleEngineRuleModel::getVersion)).ifPresent(rules::add);
                                });
                return rules;
            }
        }
        catch(FlexibleSearchException e)
        {
            LOG.warn("Exception {} occurred as no rules were found for the version [{}]", e, Long.valueOf(version));
        }
        return Collections.emptyList();
    }


    protected AbstractRuleEngineRuleModel getWithMaximumVersion(FlexibleSearchQuery query, long version)
    {
        SearchResult<AbstractRuleEngineRuleModel> search = getFlexibleSearchService().search(query);
        List<AbstractRuleEngineRuleModel> ruleModels = search.getResult();
        Stream<AbstractRuleEngineRuleModel> ruleStream = ruleModels.stream();
        if(version >= 0L)
        {
            ruleStream = ruleStream.filter(r -> (r.getVersion().longValue() <= version));
        }
        return ruleStream.sorted(Comparator.<AbstractRuleEngineRuleModel, Comparable>comparing(AbstractRuleEngineRuleModel::getVersion).reversed()).findFirst().orElse(null);
    }


    protected Date getRoundedTimestamp()
    {
        Date currentTime = getTimeService().getCurrentTime();
        Calendar c = Calendar.getInstance();
        c.setTime(currentTime);
        c.set(13, 0);
        return currentTime;
    }


    protected AbstractRulesModuleModel getRulesModuleIfOneAvailable()
    {
        FlexibleSearchQuery query = new FlexibleSearchQuery("select {pk} from {AbstractRulesModule}");
        SearchResult<AbstractRulesModuleModel> search = getFlexibleSearchService().search(query);
        List<AbstractRulesModuleModel> searchResult = search.getResult();
        if(CollectionUtils.isNotEmpty(searchResult) && searchResult.size() == 1)
        {
            return searchResult.get(0);
        }
        throw new IllegalStateException("This method could be called in the case of only one AbstractRulesModuleModel available in the system, otherwise the explicit reference to a module should be provided");
    }


    protected TimeService getTimeService()
    {
        return this.timeService;
    }


    @Required
    public void setTimeService(TimeService timeService)
    {
        this.timeService = timeService;
    }
}
