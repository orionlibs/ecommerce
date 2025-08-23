package de.hybris.platform.ruleengine.versioning.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.ruleengine.dao.EngineRuleDao;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengine.model.DroolsKIEModuleModel;
import de.hybris.platform.ruleengine.model.DroolsRuleModel;
import de.hybris.platform.ruleengine.versioning.RuleModelRemoveHandler;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.PersistenceOperation;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Required;

public class RuleEngineRuleModelRemoveHandler implements RuleModelRemoveHandler
{
    private EngineRuleDao engineRuleDao;


    public void handleOnRemove(AbstractRuleEngineRuleModel rule, InterceptorContext context)
    {
        previousVersionExistsMakeItActive(rule, context);
    }


    protected void previousVersionExistsMakeItActive(AbstractRuleEngineRuleModel ruleEngineRule, InterceptorContext ctx)
    {
        Preconditions.checkArgument(Objects.nonNull(ruleEngineRule), "The rule engine rule should not be null here");
        Preconditions.checkArgument(ruleEngineRule instanceof DroolsRuleModel, "The rule must be an instance of DroolsRule");
        DroolsRuleModel droolsRule = (DroolsRuleModel)ruleEngineRule;
        DroolsKIEModuleModel module = droolsRule.getKieBase().getKieModule();
        long version = ruleEngineRule.getVersion().longValue() - 1L;
        if(version >= 0L)
        {
            AbstractRuleEngineRuleModel rule = getEngineRuleDao().getRuleByCodeAndMaxVersion(ruleEngineRule.getCode(), module.getName(), ruleEngineRule
                            .getVersion().longValue() - 1L);
            if(Objects.nonNull(rule))
            {
                rule.setCurrentVersion(Boolean.TRUE);
                ctx.registerElementFor(ruleEngineRule, PersistenceOperation.DELETE);
                ctx.registerElementFor(rule, PersistenceOperation.SAVE);
            }
        }
    }


    protected EngineRuleDao getEngineRuleDao()
    {
        return this.engineRuleDao;
    }


    @Required
    public void setEngineRuleDao(EngineRuleDao engineRuleDao)
    {
        this.engineRuleDao = engineRuleDao;
    }
}
