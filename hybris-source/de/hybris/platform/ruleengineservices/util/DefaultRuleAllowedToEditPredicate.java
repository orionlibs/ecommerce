package de.hybris.platform.ruleengineservices.util;

import de.hybris.platform.ruleengineservices.enums.RuleStatus;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import de.hybris.platform.ruleengineservices.rule.dao.RuleDao;
import java.util.Optional;
import java.util.function.Predicate;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRuleAllowedToEditPredicate implements Predicate<AbstractRuleModel>
{
    private RuleDao ruleDao;


    public boolean test(AbstractRuleModel ruleInstance)
    {
        if(ruleInstance.getStatus() == RuleStatus.UNPUBLISHED)
        {
            return true;
        }
        Optional<AbstractRuleModel> latestUnpublishedRule = getRuleDao().findRuleByCodeAndStatus(ruleInstance.getCode(), RuleStatus.UNPUBLISHED);
        return !latestUnpublishedRule.isPresent();
    }


    protected RuleDao getRuleDao()
    {
        return this.ruleDao;
    }


    @Required
    public void setRuleDao(RuleDao ruleDao)
    {
        this.ruleDao = ruleDao;
    }
}
