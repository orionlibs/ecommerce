package de.hybris.platform.ruleengine.init;

import de.hybris.platform.ruleengine.MessageLevel;
import de.hybris.platform.ruleengine.RuleEngineActionResult;
import de.hybris.platform.ruleengine.cache.KIEModuleCacheBuilder;
import de.hybris.platform.ruleengine.enums.DroolsEqualityBehavior;
import de.hybris.platform.ruleengine.enums.DroolsEventProcessingMode;
import de.hybris.platform.ruleengine.enums.DroolsSessionType;
import de.hybris.platform.ruleengine.impl.KieContainerListener;
import de.hybris.platform.ruleengine.model.DroolsKIEBaseModel;
import de.hybris.platform.ruleengine.model.DroolsKIEModuleModel;
import de.hybris.platform.ruleengine.model.DroolsKIESessionModel;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import org.apache.commons.lang3.tuple.Pair;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.Message;
import org.kie.api.builder.ReleaseId;
import org.kie.api.builder.model.KieBaseModel;
import org.kie.api.builder.model.KieModuleModel;
import org.kie.api.builder.model.KieSessionModel;
import org.kie.api.conf.EqualityBehaviorOption;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.runtime.KieContainer;

public interface RuleEngineKieModuleSwapper
{
    List<Object> switchKieModule(DroolsKIEModuleModel paramDroolsKIEModuleModel, KieContainerListener paramKieContainerListener, LinkedList<Supplier<Object>> paramLinkedList, boolean paramBoolean, RuleEngineActionResult paramRuleEngineActionResult);


    void switchKieModuleAsync(String paramString, KieContainerListener paramKieContainerListener, List<Object> paramList, Supplier<Object> paramSupplier, List<Supplier<Object>> paramList1, boolean paramBoolean, RuleEngineActionResult paramRuleEngineActionResult);


    KieContainer initializeNewKieContainer(DroolsKIEModuleModel paramDroolsKIEModuleModel, KieModule paramKieModule, RuleEngineActionResult paramRuleEngineActionResult);


    Pair<KieModule, KIEModuleCacheBuilder> createKieModule(DroolsKIEModuleModel paramDroolsKIEModuleModel, RuleEngineActionResult paramRuleEngineActionResult);


    void addKieBase(KieModuleModel paramKieModuleModel, KieFileSystem paramKieFileSystem, DroolsKIEBaseModel paramDroolsKIEBaseModel, KIEModuleCacheBuilder paramKIEModuleCacheBuilder);


    void addKieBase(KieModuleModel paramKieModuleModel, DroolsKIEBaseModel paramDroolsKIEBaseModel);


    String activateKieModule(DroolsKIEModuleModel paramDroolsKIEModuleModel);


    boolean removeKieModuleIfPresent(ReleaseId paramReleaseId, RuleEngineActionResult paramRuleEngineActionResult);


    boolean removeOldKieModuleIfPresent(RuleEngineActionResult paramRuleEngineActionResult);


    void addKieSession(KieBaseModel paramKieBaseModel, DroolsKIESessionModel paramDroolsKIESessionModel);


    void addRules(KieFileSystem paramKieFileSystem, DroolsKIEBaseModel paramDroolsKIEBaseModel, KIEModuleCacheBuilder paramKIEModuleCacheBuilder);


    void writeKModuleXML(KieModuleModel paramKieModuleModel, KieFileSystem paramKieFileSystem);


    void writePomXML(DroolsKIEModuleModel paramDroolsKIEModuleModel, KieFileSystem paramKieFileSystem);


    ReleaseId getReleaseId(DroolsKIEModuleModel paramDroolsKIEModuleModel);


    Optional<ReleaseId> getDeployedReleaseId(DroolsKIEModuleModel paramDroolsKIEModuleModel, String paramString);


    void setUpKieServices();


    void waitForSwappingToFinish();


    void addRulesToCache(DroolsKIEBaseModel paramDroolsKIEBaseModel, KIEModuleCacheBuilder paramKIEModuleCacheBuilder);


    static KieSessionModel.KieSessionType getSessionType(DroolsSessionType sessionType)
    {
        switch(null.$SwitchMap$de$hybris$platform$ruleengine$enums$DroolsSessionType[sessionType.ordinal()])
        {
            case 1:
                return KieSessionModel.KieSessionType.STATEFUL;
            case 2:
                return KieSessionModel.KieSessionType.STATELESS;
        }
        return null;
    }


    static EqualityBehaviorOption getEqualityBehaviorOption(DroolsEqualityBehavior behavior)
    {
        switch(null.$SwitchMap$de$hybris$platform$ruleengine$enums$DroolsEqualityBehavior[behavior.ordinal()])
        {
            case 1:
                return EqualityBehaviorOption.EQUALITY;
            case 2:
                return EqualityBehaviorOption.IDENTITY;
        }
        return null;
    }


    static EventProcessingOption getEventProcessingOption(DroolsEventProcessingMode eventProcessingMode)
    {
        switch(null.$SwitchMap$de$hybris$platform$ruleengine$enums$DroolsEventProcessingMode[eventProcessingMode.ordinal()])
        {
            case 1:
                return EventProcessingOption.STREAM;
            case 2:
                return EventProcessingOption.CLOUD;
        }
        return null;
    }


    static MessageLevel convertLevel(Message.Level level)
    {
        if(level == null)
        {
            return null;
        }
        switch(null.$SwitchMap$org$kie$api$builder$Message$Level[level.ordinal()])
        {
            case 1:
                return MessageLevel.ERROR;
            case 2:
                return MessageLevel.WARNING;
            case 3:
                return MessageLevel.INFO;
        }
        return null;
    }
}
