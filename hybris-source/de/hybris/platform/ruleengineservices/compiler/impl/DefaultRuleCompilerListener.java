package de.hybris.platform.ruleengineservices.compiler.impl;

import de.hybris.platform.ruleengine.dao.EngineRuleDao;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerContext;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerListener;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRuleCompilerListener implements RuleCompilerListener
{
    private EngineRuleDao engineRuleDao;


    public void beforeCompile(RuleCompilerContext context)
    {
    }


    public void afterCompile(RuleCompilerContext context)
    {
        AbstractRuleEngineRuleModel engineRule = getEngineRuleDao().getRuleByCode(context.getRule().getCode(), context
                        .getModuleName());
        context.setRuleVersion(engineRule.getVersion().longValue());
    }


    public void afterCompileError(RuleCompilerContext context)
    {
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
