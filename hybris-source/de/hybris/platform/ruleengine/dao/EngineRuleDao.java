package de.hybris.platform.ruleengine.dao;

import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengine.model.AbstractRulesModuleModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;
import java.util.Collection;
import java.util.List;

public interface EngineRuleDao extends Dao
{
    <T extends AbstractRuleEngineRuleModel> T getRuleByUuid(String paramString);


    <T extends AbstractRuleEngineRuleModel> Collection<T> getRulesByUuids(Collection<String> paramCollection);


    List<AbstractRuleEngineRuleModel> findRulesByCode(String paramString);


    AbstractRuleEngineRuleModel getRuleByCode(String paramString1, String paramString2);


    AbstractRuleEngineRuleModel getRuleByCodeAndMaxVersion(String paramString1, String paramString2, long paramLong);


    AbstractRuleEngineRuleModel getActiveRuleByCodeAndMaxVersion(String paramString1, String paramString2, long paramLong);


    List<AbstractRuleEngineRuleModel> getActiveRules(String paramString);


    List<AbstractRuleEngineRuleModel> getActiveRules(AbstractRulesModuleModel paramAbstractRulesModuleModel);


    <T extends AbstractRuleEngineRuleModel> List<T> getRulesForVersion(String paramString, long paramLong);


    <T extends AbstractRuleEngineRuleModel> List<T> getRulesBetweenVersions(String paramString, long paramLong1, long paramLong2);


    <T extends AbstractRuleEngineRuleModel> List<T> getActiveRulesForVersion(String paramString, long paramLong);


    Long getCurrentRulesSnapshotVersion(AbstractRulesModuleModel paramAbstractRulesModuleModel);


    Long getRuleVersion(String paramString1, String paramString2);
}
