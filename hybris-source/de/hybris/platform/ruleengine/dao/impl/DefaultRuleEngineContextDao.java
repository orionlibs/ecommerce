package de.hybris.platform.ruleengine.dao.impl;

import com.google.common.collect.ImmutableMap;
import de.hybris.platform.ruleengine.dao.RuleEngineContextDao;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineContextModel;
import de.hybris.platform.ruleengine.model.AbstractRulesModuleModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import java.util.List;
import java.util.Map;

public class DefaultRuleEngineContextDao extends AbstractItemDao implements RuleEngineContextDao
{
    private static final String GET_CONTEXT_BY_NAME = "select {pk} from {AbstractRuleEngineContext} where {name} = ?name";
    private static final String GET_CONTEXT_BY_RULES_MODULE = "select {pk} from {DroolsRuleEngineContext as ctx JOIN DroolsKIESession as s ON {ctx.kieSession} = {s.pk} JOIN DroolsKIEBase as kb ON {s.kieBase} = {kb.pk}} where {kb.kieModule} = ?module";


    public AbstractRuleEngineContextModel findRuleEngineContextByName(String name)
    {
        ImmutableMap immutableMap = ImmutableMap.of("name", name);
        FlexibleSearchQuery query = new FlexibleSearchQuery("select {pk} from {AbstractRuleEngineContext} where {name} = ?name", (Map)immutableMap);
        return (AbstractRuleEngineContextModel)getFlexibleSearchService().searchUnique(query);
    }


    public <T extends AbstractRuleEngineContextModel> List<T> findRuleEngineContextByRulesModule(AbstractRulesModuleModel rulesModule)
    {
        ImmutableMap immutableMap = ImmutableMap.of("module", rulesModule);
        FlexibleSearchQuery query = new FlexibleSearchQuery("select {pk} from {DroolsRuleEngineContext as ctx JOIN DroolsKIESession as s ON {ctx.kieSession} = {s.pk} JOIN DroolsKIEBase as kb ON {s.kieBase} = {kb.pk}} where {kb.kieModule} = ?module", (Map)immutableMap);
        return getFlexibleSearchService().search(query).getResult();
    }
}
