package de.hybris.platform.ruleengineservices.rule.dao.impl;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.ruleengine.enums.RuleType;
import de.hybris.platform.ruleengineservices.enums.RuleStatus;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import de.hybris.platform.ruleengineservices.rule.dao.RuleDao;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRuleDao extends AbstractItemDao implements RuleDao
{
    protected static final String GET_ALL_RULES_QUERY = "select {pk} from {AbstractRule}";
    protected static final String GET_ALL_ACTIVE_RULES_QUERY = " where ({startDate} <= ?startDate OR {startDate} IS NULL) AND ({endDate} >= ?endDate OR {endDate} IS NULL)";
    protected static final String GET_RULE_BY_CODE = " where {code} = ?code";
    protected static final String GET_ENGINE_RULE_TYPE_FOR_RULE_TYPE = "select {engineRuleType} from {RuleToEngineRuleTypeMapping} WHERE {ruleType} = ?ruleType ";
    protected static final String GET_MAX_VERSION_FOR_CODE = "select MAX( {version} ) from {AbstractRule} where {code} = ?code";
    protected static final String GET_RULE_BY_CODE_STATUS = " where {code} = ?code and {status} = ?status";
    protected static final String GET_RULE_BY_CODE_STATUSES = " where {code} = ?code and {status} IN (?statuses)";
    protected static final String GET_RULE_BY_STATUSES = " where {status} IN (?statuses)";
    protected static final String GET_RULE_BY_CODE_VERSION = " where {code} = ?code and {version} = ?version";
    protected static final String GET_RULE_BY_STATUS_AND_VERSION = " where {status} in (?status) and  {version} = ?version";
    private I18NService i18NService;
    private TypeService typeService;


    public <T extends AbstractRuleModel> List<T> findAllRules()
    {
        FlexibleSearchQuery query = new FlexibleSearchQuery("select {pk} from {AbstractRule}");
        SearchResult<T> search = getFlexibleSearchService().search(query);
        return filterByLastVersion(search.getResult());
    }


    public <T extends AbstractRuleModel> List<T> findAllRulesByType(Class<T> type)
    {
        ComposedTypeModel composedType = this.typeService.getComposedTypeForClass(type);
        String typecode = composedType.getCode();
        StringBuilder sb = createSelectStatement(typecode);
        FlexibleSearchQuery query = new FlexibleSearchQuery(sb.toString());
        SearchResult<T> search = getFlexibleSearchService().search(query);
        return filterByLastVersion(search.getResult());
    }


    public <T extends AbstractRuleModel> List<T> findAllActiveRules()
    {
        Calendar now = Calendar.getInstance(getI18NService().getCurrentTimeZone(), getI18NService().getCurrentLocale());
        now.set(14, 0);
        now.set(13, 0);
        ImmutableMap immutableMap = ImmutableMap.of("startDate", now.getTime(), "endDate", now.getTime());
        FlexibleSearchQuery query = new FlexibleSearchQuery("select {pk} from {AbstractRule} where ({startDate} <= ?startDate OR {startDate} IS NULL) AND ({endDate} >= ?endDate OR {endDate} IS NULL)", (Map)immutableMap);
        SearchResult<T> search = getFlexibleSearchService().search(query);
        return filterByLastVersion(search.getResult());
    }


    public <T extends AbstractRuleModel> List<T> findAllActiveRulesByType(Class<T> type)
    {
        Calendar now = Calendar.getInstance(getI18NService().getCurrentTimeZone(), getI18NService().getCurrentLocale());
        now.set(14, 0);
        now.set(13, 0);
        ComposedTypeModel composedType = this.typeService.getComposedTypeForClass(type);
        String typecode = composedType.getCode();
        ImmutableMap immutableMap = ImmutableMap.of("startDate", now.getTime(), "endDate", now.getTime());
        StringBuilder sb = createSelectStatement(typecode);
        sb.append(" where ({startDate} <= ?startDate OR {startDate} IS NULL) AND ({endDate} >= ?endDate OR {endDate} IS NULL)");
        FlexibleSearchQuery query = new FlexibleSearchQuery(sb.toString(), (Map)immutableMap);
        SearchResult<T> search = getFlexibleSearchService().search(query);
        return filterByLastVersion(search.getResult());
    }


    public <T extends AbstractRuleModel> T findRuleByCode(String code)
    {
        List<T> versionRules = findAllRuleVersionsByCode(code);
        List<T> lastVersionRules = filterByLastVersion(versionRules);
        return CollectionUtils.isNotEmpty(lastVersionRules) ? lastVersionRules.get(0) : null;
    }


    public <T extends AbstractRuleModel> List<T> findAllRuleVersionsByCode(String code)
    {
        ImmutableMap immutableMap = ImmutableMap.of("code", code);
        FlexibleSearchQuery query = new FlexibleSearchQuery("select {pk} from {AbstractRule} where {code} = ?code", (Map)immutableMap);
        SearchResult<T> search = getFlexibleSearchService().search(query);
        return search.getResult();
    }


    public <T extends AbstractRuleModel> T findRuleByCodeAndType(String code, Class<T> type)
    {
        ComposedTypeModel composedType = this.typeService.getComposedTypeForClass(type);
        String typecode = composedType.getCode();
        ImmutableMap immutableMap = ImmutableMap.of("code", code);
        StringBuilder sb = createSelectStatement(typecode);
        sb.append(" where {code} = ?code");
        FlexibleSearchQuery query = new FlexibleSearchQuery(sb.toString(), (Map)immutableMap);
        SearchResult<T> search = getFlexibleSearchService().search(query);
        List<T> lastVersionRules = filterByLastVersion(search.getResult());
        return CollectionUtils.isNotEmpty(lastVersionRules) ? lastVersionRules.get(0) : null;
    }


    public RuleType findEngineRuleTypeByRuleType(Class<?> type)
    {
        ComposedTypeModel typeModel = this.typeService.getComposedTypeForClass(type);
        FlexibleSearchQuery query = new FlexibleSearchQuery("select {engineRuleType} from {RuleToEngineRuleTypeMapping} WHERE {ruleType} = ?ruleType ");
        query.addQueryParameter("ruleType", typeModel);
        query.setResultClassList(Collections.singletonList(RuleType.class));
        try
        {
            return (RuleType)getFlexibleSearchService().searchUnique(query);
        }
        catch(ModelNotFoundException e)
        {
            return null;
        }
    }


    public Long getRuleVersion(String code)
    {
        Long nextRuleVersion;
        ServicesUtil.validateParameterNotNullStandardMessage("code", code);
        ImmutableMap immutableMap = ImmutableMap.of("code", code);
        FlexibleSearchQuery query = new FlexibleSearchQuery("select MAX( {version} ) from {AbstractRule} where {code} = ?code", (Map)immutableMap);
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


    public Optional<AbstractRuleModel> findRuleByCodeAndStatus(String code, RuleStatus ruleStatus)
    {
        List<AbstractRuleModel> rules = findAllRuleVersionsByCodeAndStatus(code, ruleStatus);
        List<AbstractRuleModel> lastVersionRules = filterByLastVersion(rules);
        return lastVersionRules.stream().findFirst();
    }


    public <T extends AbstractRuleModel> List<T> findAllRuleVersionsByCodeAndStatus(String code, RuleStatus ruleStatus)
    {
        ImmutableMap immutableMap = ImmutableMap.of("code", code, "status", ruleStatus);
        FlexibleSearchQuery query = new FlexibleSearchQuery("select {pk} from {AbstractRule} where {code} = ?code and {status} = ?status", (Map)immutableMap);
        SearchResult<T> search = getFlexibleSearchService().search(query);
        return search.getResult();
    }


    public <T extends AbstractRuleModel> List<T> findAllRuleVersionsByCodeAndStatuses(String code, RuleStatus... ruleStatuses)
    {
        if(Objects.nonNull(ruleStatuses) && ruleStatuses.length > 0)
        {
            ImmutableMap immutableMap = ImmutableMap.of("code", code, "statuses", Lists.newArrayList((Object[])ruleStatuses));
            FlexibleSearchQuery query = new FlexibleSearchQuery("select {pk} from {AbstractRule} where {code} = ?code and {status} IN (?statuses)", (Map)immutableMap);
            SearchResult<T> search = getFlexibleSearchService().search(query);
            return search.getResult();
        }
        return findAllRuleVersionsByCode(code);
    }


    public <T extends AbstractRuleModel> List<T> findAllRulesWithStatuses(RuleStatus... ruleStatuses)
    {
        ImmutableMap immutableMap = ImmutableMap.of("statuses", Lists.newArrayList((Object[])ruleStatuses));
        FlexibleSearchQuery query = new FlexibleSearchQuery("select {pk} from {AbstractRule} where {status} IN (?statuses)", (Map)immutableMap);
        SearchResult<T> search = getFlexibleSearchService().search(query);
        return search.getResult();
    }


    public <T extends AbstractRuleModel> List<T> findByVersionAndStatuses(Long version, RuleStatus... ruleStatuses)
    {
        ImmutableMap immutableMap = ImmutableMap.of("version", version, "status", Lists.newArrayList((Object[])ruleStatuses));
        FlexibleSearchQuery query = new FlexibleSearchQuery("select {pk} from {AbstractRule} where {status} in (?status) and  {version} = ?version", (Map)immutableMap);
        SearchResult<T> search = getFlexibleSearchService().search(query);
        return search.getResult();
    }


    public Optional<AbstractRuleModel> findRuleByCodeAndVersion(String code, Long version)
    {
        ImmutableMap immutableMap = ImmutableMap.of("code", code, "version", version);
        FlexibleSearchQuery query = new FlexibleSearchQuery("select {pk} from {AbstractRule} where {code} = ?code and {version} = ?version", (Map)immutableMap);
        SearchResult<AbstractRuleModel> search = getFlexibleSearchService().search(query);
        List<AbstractRuleModel> rules = search.getResult();
        return rules.stream().findFirst();
    }


    protected StringBuilder createSelectStatement(String typecode)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("select {").append("pk").append("} from {").append(typecode).append('}');
        return sb;
    }


    protected <T extends AbstractRuleModel> List<T> filterByLastVersion(List<T> rulesWithVersion)
    {
        List<T> filteredRules = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(rulesWithVersion))
        {
            Map<String, List<T>> twinRulesMap = (Map<String, List<T>>)rulesWithVersion.stream().collect(Collectors.groupingBy(AbstractRuleModel::getCode));
            twinRulesMap.values().stream().map(Collection::stream)
                            .forEach(l -> {
                                Objects.requireNonNull(filteredRules);
                                l.max(Comparator.comparing(AbstractRuleModel::getVersion)).ifPresent(filteredRules::add);
                            });
        }
        return filteredRules;
    }


    protected I18NService getI18NService()
    {
        return this.i18NService;
    }


    @Required
    public void setI18NService(I18NService i18NService)
    {
        this.i18NService = i18NService;
    }


    protected TypeService getTypeService()
    {
        return this.typeService;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }
}
