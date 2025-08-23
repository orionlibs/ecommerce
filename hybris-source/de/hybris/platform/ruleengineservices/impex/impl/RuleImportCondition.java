package de.hybris.platform.ruleengineservices.impex.impl;

import de.hybris.platform.core.Registry;
import de.hybris.platform.impex.jalo.header.AbstractColumnDescriptor;
import de.hybris.platform.impex.jalo.imp.ValueLine;
import de.hybris.platform.ruleengineservices.enums.RuleStatus;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import de.hybris.platform.ruleengineservices.rule.dao.RuleDao;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

public class RuleImportCondition implements Predicate<ValueLine>
{
    private static final String RULE_DAO = "ruleDao";
    private static final RuleStatus[] LOOKUP_RULE_STATUSES = (RuleStatus[])ArrayUtils.removeElement((Object[])RuleStatus.values(), RuleStatus.UNPUBLISHED);


    public boolean test(ValueLine valueLine)
    {
        ValueLine.ValueEntry valueEntry = getCodeValueEntry(valueLine);
        List<AbstractRuleModel> rules = getRuleDao().findAllRuleVersionsByCodeAndStatuses(valueEntry.getCellValue(), getLookupRuleStatuses());
        return CollectionUtils.isEmpty(rules);
    }


    protected ValueLine.ValueEntry getCodeValueEntry(ValueLine valueLine)
    {
        Collection<AbstractColumnDescriptor> columns = valueLine.getHeader().getColumnsByQualifier("code");
        ServicesUtil.validateIfSingleResult(columns, "No column with given code[code] was found", "More than one column with given code [code] was found");
        return valueLine.getValueEntry(((AbstractColumnDescriptor)columns.iterator().next()).getValuePosition());
    }


    protected RuleDao getRuleDao()
    {
        return (RuleDao)Registry.getApplicationContext().getBean("ruleDao", RuleDao.class);
    }


    protected RuleStatus[] getLookupRuleStatuses()
    {
        return LOOKUP_RULE_STATUSES;
    }
}
