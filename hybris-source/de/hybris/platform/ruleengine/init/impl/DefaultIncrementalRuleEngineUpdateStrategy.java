package de.hybris.platform.ruleengine.init.impl;

import de.hybris.platform.ruleengine.init.IncrementalRuleEngineUpdateStrategy;
import de.hybris.platform.ruleengine.model.DroolsRuleModel;
import de.hybris.platform.ruleengine.util.EngineRulesRepository;
import java.util.Collection;
import org.kie.api.builder.ReleaseId;
import org.springframework.beans.factory.annotation.Required;

public class DefaultIncrementalRuleEngineUpdateStrategy implements IncrementalRuleEngineUpdateStrategy
{
    private EngineRulesRepository engineRulesRepository;
    private int totalNumOfRulesThreshold;
    private float fractionOfRulesThreshold;


    public boolean shouldUpdateIncrementally(ReleaseId releaseId, String moduleName, Collection<DroolsRuleModel> rulesToAdd, Collection<DroolsRuleModel> rulesToRemove)
    {
        long totalNumberOfDeployedRules = getEngineRulesRepository().countDeployedEngineRulesForModule(moduleName);
        int numberOfRulesToUpdate = rulesToAdd.size() + rulesToRemove.size();
        boolean updateIncrementally = (Math.sqrt(totalNumberOfDeployedRules * totalNumberOfDeployedRules + (numberOfRulesToUpdate * numberOfRulesToUpdate)) > this.totalNumOfRulesThreshold);
        if(updateIncrementally && numberOfRulesToUpdate > 0)
        {
            updateIncrementally = (totalNumberOfDeployedRules > 0L && numberOfRulesToUpdate / totalNumberOfDeployedRules < this.fractionOfRulesThreshold);
        }
        return updateIncrementally;
    }


    protected EngineRulesRepository getEngineRulesRepository()
    {
        return this.engineRulesRepository;
    }


    @Required
    public void setEngineRulesRepository(EngineRulesRepository engineRulesRepository)
    {
        this.engineRulesRepository = engineRulesRepository;
    }


    protected int getTotalNumOfRulesThreshold()
    {
        return this.totalNumOfRulesThreshold;
    }


    @Required
    public void setTotalNumOfRulesThreshold(int totalNumOfRulesThreshold)
    {
        this.totalNumOfRulesThreshold = totalNumOfRulesThreshold;
    }


    protected float getFractionOfRulesThreshold()
    {
        return this.fractionOfRulesThreshold;
    }


    @Required
    public void setFractionOfRulesThreshold(float fractionOfRulesThreshold)
    {
        this.fractionOfRulesThreshold = fractionOfRulesThreshold;
    }
}
