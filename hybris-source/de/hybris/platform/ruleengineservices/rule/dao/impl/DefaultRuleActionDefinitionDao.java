package de.hybris.platform.ruleengineservices.rule.dao.impl;

import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.ruleengineservices.model.RuleActionDefinitionModel;
import de.hybris.platform.ruleengineservices.rule.dao.RuleActionDefinitionDao;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRuleActionDefinitionDao extends AbstractItemDao implements RuleActionDefinitionDao
{
    protected static final String GET_ALL_ACTION_DEFINITIONS = "select {pk} from {RuleActionDefinition}";
    protected static final String GET_ACTION_DEFINITIONS_BY_RULE_TYPE = "select {pk} from {RuleActionDefinition as rad JOIN RuleActionDefinitionRuleTypeMapping as radm ON {rad.PK} = {radm.definition}} WHERE {radm.ruleType} in (?ruleTypes)";
    private TypeService typeService;


    public List<RuleActionDefinitionModel> findAllRuleActionDefinitions()
    {
        FlexibleSearchQuery query = new FlexibleSearchQuery("select {pk} from {RuleActionDefinition}");
        SearchResult<RuleActionDefinitionModel> search = getFlexibleSearchService().search(query);
        return search.getResult();
    }


    public List<RuleActionDefinitionModel> findRuleActionDefinitionsByRuleType(Class<?> ruleType)
    {
        ComposedTypeModel ruleTypeModel = this.typeService.getComposedTypeForClass(ruleType);
        List<ComposedTypeModel> ruleTypes = new ArrayList<>();
        ruleTypes.add(ruleTypeModel);
        Collection<ComposedTypeModel> superTypes = ruleTypeModel.getAllSuperTypes();
        ruleTypes.addAll(superTypes);
        FlexibleSearchQuery query = new FlexibleSearchQuery("select {pk} from {RuleActionDefinition as rad JOIN RuleActionDefinitionRuleTypeMapping as radm ON {rad.PK} = {radm.definition}} WHERE {radm.ruleType} in (?ruleTypes)");
        query.addQueryParameter("ruleTypes", ruleTypes);
        SearchResult<RuleActionDefinitionModel> search = getFlexibleSearchService().search(query);
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
