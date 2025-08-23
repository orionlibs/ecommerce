package de.hybris.platform.ruleengineservices.rule.dao.impl;

import com.google.common.collect.ImmutableMap;
import de.hybris.platform.ruleengine.enums.RuleType;
import de.hybris.platform.ruleengineservices.model.RuleGroupModel;
import de.hybris.platform.ruleengineservices.rule.dao.RuleGroupDao;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.lang.StringUtils;

public class DefaultRuleGroupDao extends AbstractItemDao implements RuleGroupDao
{
    private static final String GET_ALL_RULE_GROUPS_EXCEPTING = "select {pk} from {RuleGroup} where {pk} not in ( ?groupIds )";
    private static final String GET_ALL_REFERRED_RULE_GROUPS = "select distinct {pk} from {RuleGroup as rg join SourceRule as sr on {sr.ruleGroup} = {rg.pk}}";
    protected static final String GET_RULE_GROUP_FOR_ENGINE_RULE_TYPE = "select distinct {pk} from {RuleGroup as rg join SourceRule as sr on {sr.ruleGroup} = {rg.pk} join RuleToEngineRuleTypeMapping as r2re on {r2re.ruleType} = {sr.itemtype}} where {r2re.engineRuleType} = ?engineRuleType";
    protected static final String GET_ALL_RULE_GROUPS_QUERY = "select {pk} from {RuleGroup}";
    protected static final String GET_RULE_GROUP_BY_CODE = " where {code} = ?code";


    public Optional<RuleGroupModel> findRuleGroupByCode(String code)
    {
        if(StringUtils.isEmpty(code))
        {
            return Optional.empty();
        }
        ImmutableMap immutableMap = ImmutableMap.of("code", code);
        FlexibleSearchQuery query = new FlexibleSearchQuery("select {pk} from {RuleGroup} where {code} = ?code", (Map)immutableMap);
        SearchResult<RuleGroupModel> search = getFlexibleSearchService().search(query);
        List<RuleGroupModel> rules = search.getResult();
        return rules.stream().findFirst();
    }


    public List<RuleGroupModel> findRuleGroupOfType(RuleType engineRuleType)
    {
        ImmutableMap immutableMap = ImmutableMap.of("engineRuleType", engineRuleType);
        FlexibleSearchQuery query = new FlexibleSearchQuery("select distinct {pk} from {RuleGroup as rg join SourceRule as sr on {sr.ruleGroup} = {rg.pk} join RuleToEngineRuleTypeMapping as r2re on {r2re.ruleType} = {sr.itemtype}} where {r2re.engineRuleType} = ?engineRuleType", (Map)immutableMap);
        SearchResult<RuleGroupModel> search = getFlexibleSearchService().search(query);
        return search.getResult();
    }


    protected List<RuleGroupModel> findAllReferredRuleGroups()
    {
        FlexibleSearchQuery query = new FlexibleSearchQuery("select distinct {pk} from {RuleGroup as rg join SourceRule as sr on {sr.ruleGroup} = {rg.pk}}");
        SearchResult<RuleGroupModel> search = getFlexibleSearchService().search(query);
        return search.getResult();
    }


    public List<RuleGroupModel> findAllNotReferredRuleGroups()
    {
        FlexibleSearchQuery query = new FlexibleSearchQuery("select {pk} from {RuleGroup} where {pk} not in ( ?groupIds )", Collections.singletonMap("groupIds", findAllReferredRuleGroups()));
        SearchResult<RuleGroupModel> search = getFlexibleSearchService().search(query);
        return search.getResult();
    }
}
