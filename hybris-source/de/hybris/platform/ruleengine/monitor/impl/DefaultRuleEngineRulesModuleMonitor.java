package de.hybris.platform.ruleengine.monitor.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.ruleengine.init.RuleEngineBootstrap;
import de.hybris.platform.ruleengine.model.DroolsKIEModuleModel;
import de.hybris.platform.ruleengine.monitor.RuleEngineRulesModuleMonitor;
import java.util.Objects;
import javax.annotation.PostConstruct;
import org.kie.api.KieServices;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRuleEngineRulesModuleMonitor implements RuleEngineRulesModuleMonitor<DroolsKIEModuleModel>
{
    private KieServices kieServices;
    private RuleEngineBootstrap<KieServices, KieContainer, DroolsKIEModuleModel> ruleEngineBootstrap;


    public boolean isRulesModuleDeployed(DroolsKIEModuleModel rulesModule)
    {
        Preconditions.checkArgument(Objects.nonNull(rulesModule), "Provided rules module cannot be NULL");
        String deployedMvnVersion = rulesModule.getDeployedMvnVersion();
        if(Objects.nonNull(deployedMvnVersion))
        {
            ReleaseId releaseId = getKieServices().newReleaseId(rulesModule.getMvnGroupId(), rulesModule.getMvnArtifactId(), deployedMvnVersion);
            return Objects.nonNull(getKieServices().getRepository().getKieModule(releaseId));
        }
        return false;
    }


    @PostConstruct
    private void setUp()
    {
        if(Objects.isNull(this.kieServices))
        {
            this.kieServices = (KieServices)getRuleEngineBootstrap().getEngineServices();
        }
    }


    protected KieServices getKieServices()
    {
        return this.kieServices;
    }


    public void setKieServices(KieServices kieServices)
    {
        this.kieServices = kieServices;
    }


    protected RuleEngineBootstrap<KieServices, KieContainer, DroolsKIEModuleModel> getRuleEngineBootstrap()
    {
        return this.ruleEngineBootstrap;
    }


    @Required
    public void setRuleEngineBootstrap(RuleEngineBootstrap<KieServices, KieContainer, DroolsKIEModuleModel> ruleEngineBootstrap)
    {
        this.ruleEngineBootstrap = ruleEngineBootstrap;
    }
}
