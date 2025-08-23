package de.hybris.platform.ruleengineservices.rule.dao.impl;

import com.google.common.collect.Lists;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.ruleengineservices.model.RuleConditionDefinitionModel;
import de.hybris.platform.ruleengineservices.rule.dao.RuleConditionDefinitionDao;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRuleConditionDefinitionDao extends AbstractItemDao implements RuleConditionDefinitionDao
{
    protected static final String GET_ALL_CONDITION_DEFINITIONS = "select {pk} from {RuleConditionDefinition}";
    protected static final String GET_CONDITION_DEFINITIONS_BY_RULE_TYPE = "select {pk} from {RuleConditionDefinition as rcd JOIN RuleConditionDefinitionRuleTypeMapping as rcdm ON {rcd.PK} = {rcdm.definition}} WHERE {rcdm.ruleType} in (?ruleTypes)";
    private TypeService typeService;


    public List<RuleConditionDefinitionModel> findAllRuleConditionDefinitions()
    {
        FlexibleSearchQuery query = new FlexibleSearchQuery("select {pk} from {RuleConditionDefinition}");
        SearchResult<RuleConditionDefinitionModel> search = getFlexibleSearchService().search(query);
        return search.getResult();
    }


    public List<RuleConditionDefinitionModel> findRuleConditionDefinitionsByRuleType(Class<?> ruleType)
    {
        ComposedTypeModel ruleTypeModel = this.typeService.getComposedTypeForClass(ruleType);
        List<ComposedTypeModel> ruleTypes = Lists.newArrayList();
        ruleTypes.add(ruleTypeModel);
        Collection<ComposedTypeModel> superTypes = ruleTypeModel.getAllSuperTypes();
        ruleTypes.addAll(superTypes);
        FlexibleSearchQuery query = new FlexibleSearchQuery("select {pk} from {RuleConditionDefinition as rcd JOIN RuleConditionDefinitionRuleTypeMapping as rcdm ON {rcd.PK} = {rcdm.definition}} WHERE {rcdm.ruleType} in (?ruleTypes)");
        query.addQueryParameter("ruleTypes", ruleTypes);
        SearchResult<RuleConditionDefinitionModel> search = getFlexibleSearchService().search(query);
        return search.getResult();
    }


    public TypeService getTypeService()
    {
        return this.typeService;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }
}
