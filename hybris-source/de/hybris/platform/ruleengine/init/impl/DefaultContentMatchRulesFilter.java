package de.hybris.platform.ruleengine.init.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import de.hybris.platform.ruleengine.dao.EngineRuleDao;
import de.hybris.platform.ruleengine.init.ContentMatchRulesFilter;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengine.model.AbstractRulesModuleModel;
import de.hybris.platform.ruleengine.model.DroolsKIEModuleModel;
import de.hybris.platform.ruleengine.model.DroolsRuleModel;
import de.hybris.platform.ruleengine.util.EngineRulePreconditions;
import de.hybris.platform.ruleengine.util.EngineRulesRepository;
import de.hybris.platform.ruleengine.util.RuleMappings;
import de.hybris.platform.ruleengine.versioning.ModuleVersionResolver;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Required;

public class DefaultContentMatchRulesFilter implements ContentMatchRulesFilter
{
    private EngineRuleDao engineRuleDao;
    private ModuleVersionResolver<DroolsKIEModuleModel> moduleVersionResolver;
    private EngineRulesRepository engineRulesRepository;


    public Pair<Collection<DroolsRuleModel>, Collection<DroolsRuleModel>> apply(Collection<String> ruleUuids)
    {
        return apply(ruleUuids, null);
    }


    public Pair<Collection<DroolsRuleModel>, Collection<DroolsRuleModel>> apply(Collection<String> ruleUuids, Long newModuleVersion)
    {
        Preconditions.checkArgument(CollectionUtils.isNotEmpty(ruleUuids), "The provided rule UUIDs collections shouldn't be NULL or empty");
        Collection<DroolsRuleModel> rulesByUuids = getEngineRuleDao().getRulesByUuids(ruleUuids);
        Optional<DroolsKIEModuleModel> kieModule = verifyTheRulesModuleIsSame(getRulesWithKieBase(rulesByUuids));
        if(kieModule.isPresent())
        {
            DroolsKIEModuleModel module = kieModule.get();
            Optional<Long> moduleDeployedVersion = getModuleVersionResolver().getDeployedModuleVersion((AbstractRulesModuleModel)module);
            if(moduleDeployedVersion.isPresent())
            {
                Collection<DroolsRuleModel> deployedRuleset = getEngineRulesRepository().getDeployedEngineRulesForModule(module.getName());
                Collection<DroolsRuleModel> rulesetToDeploy = getRulesetWithMaxVersion(rulesByUuids, newModuleVersion);
                Set<DroolsRuleModel> rulesetToAdd = Sets.newHashSet(rulesetToDeploy);
                rulesetToAdd.removeAll(deployedRuleset);
                Set<DroolsRuleModel> rulesetToRemove = Sets.newHashSet(deployedRuleset);
                rulesetToRemove.removeAll(rulesetToDeploy);
                return (Pair<Collection<DroolsRuleModel>, Collection<DroolsRuleModel>>)ImmutablePair.of(rulesetToAdd, rulesetToRemove);
            }
        }
        return (Pair<Collection<DroolsRuleModel>, Collection<DroolsRuleModel>>)ImmutablePair.of(rulesByUuids, Lists.newArrayList());
    }


    protected List<DroolsRuleModel> getRulesWithKieBase(Collection<DroolsRuleModel> rules)
    {
        return (List<DroolsRuleModel>)rules.stream().filter(r -> Objects.nonNull(r.getKieBase())).collect(Collectors.toList());
    }


    protected Collection<DroolsRuleModel> getRulesetWithMaxVersion(Collection<DroolsRuleModel> rulesByUuids, Long version)
    {
        Long maxVersion = version;
        if(Objects.isNull(version))
        {
            maxVersion = Long.valueOf(Long.MAX_VALUE);
        }
        Map<String, DroolsRuleModel> rulesByCodeMap = Maps.newHashMap();
        List<DroolsRuleModel> activeDroolRules = (List<DroolsRuleModel>)rulesByUuids.stream().filter(AbstractRuleEngineRuleModel::getActive).collect(Collectors.toList());
        for(DroolsRuleModel ruleByUuid : activeDroolRules)
        {
            String code = ruleByUuid.getCode();
            if(rulesByCodeMap.containsKey(code))
            {
                DroolsRuleModel ruleForCode = rulesByCodeMap.get(code);
                Long ruleVersion = ruleByUuid.getVersion();
                if(ruleVersion.longValue() > ruleForCode.getVersion().longValue() && ruleVersion.longValue() <= maxVersion.longValue())
                {
                    rulesByCodeMap.replace(code, ruleByUuid);
                }
                continue;
            }
            if(ruleByUuid.getVersion().longValue() <= maxVersion.longValue())
            {
                rulesByCodeMap.put(code, ruleByUuid);
            }
        }
        return rulesByCodeMap.values();
    }


    protected Optional<DroolsKIEModuleModel> verifyTheRulesModuleIsSame(Collection<DroolsRuleModel> droolRules)
    {
        Optional<DroolsKIEModuleModel> rulesModule = Optional.empty();
        if(CollectionUtils.isNotEmpty(droolRules))
        {
            DroolsRuleModel firstDroolsRule = droolRules.iterator().next();
            EngineRulePreconditions.checkRuleHasKieModule((AbstractRuleEngineRuleModel)firstDroolsRule);
            DroolsKIEModuleModel kieModule = firstDroolsRule.getKieBase().getKieModule();
            String kieModuleName = kieModule.getName();
            if(Objects.isNull(kieModuleName))
            {
                throw new IllegalStateException("The KIE module cannot have the empty name");
            }
            if(droolRules.stream().anyMatch(r -> !RuleMappings.module(r).getName().equals(kieModuleName)))
            {
                throw new IllegalStateException("All the rules in the collection should have the same DroolsKIEModuleModel [" + kieModuleName + "]");
            }
            return Optional.of(kieModule);
        }
        return rulesModule;
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


    protected ModuleVersionResolver<DroolsKIEModuleModel> getModuleVersionResolver()
    {
        return this.moduleVersionResolver;
    }


    @Required
    public void setModuleVersionResolver(ModuleVersionResolver<DroolsKIEModuleModel> moduleVersionResolver)
    {
        this.moduleVersionResolver = moduleVersionResolver;
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
}
