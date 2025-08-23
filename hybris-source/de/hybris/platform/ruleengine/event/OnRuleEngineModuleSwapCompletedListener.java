package de.hybris.platform.ruleengine.event;

import de.hybris.platform.ruleengine.dao.RulesModuleDao;
import de.hybris.platform.ruleengine.drools.KieSessionHelper;
import de.hybris.platform.ruleengine.model.DroolsKIEModuleModel;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import org.springframework.beans.factory.annotation.Required;

public class OnRuleEngineModuleSwapCompletedListener<T> extends AbstractEventListener<RuleEngineModuleSwapCompletedEvent>
{
    private KieSessionHelper<T> kieSessionHelper;
    private RulesModuleDao rulesModuleDao;


    protected void onEvent(RuleEngineModuleSwapCompletedEvent event)
    {
        if(!event.isFailed())
        {
            DroolsKIEModuleModel module = (DroolsKIEModuleModel)getRulesModuleDao().findByName(event.getRulesModuleName());
            getKieSessionHelper().shutdownKieSessionPools(module.getMvnArtifactId(), event
                            .getRulesModuleVersion());
        }
    }


    protected KieSessionHelper<T> getKieSessionHelper()
    {
        return this.kieSessionHelper;
    }


    protected RulesModuleDao getRulesModuleDao()
    {
        return this.rulesModuleDao;
    }


    @Required
    public void setKieSessionHelper(KieSessionHelper<T> kieSessionHelper)
    {
        this.kieSessionHelper = kieSessionHelper;
    }


    @Required
    public void setRulesModuleDao(RulesModuleDao rulesModuleDao)
    {
        this.rulesModuleDao = rulesModuleDao;
    }
}
