package de.hybris.platform.ruleengine.versioning;

import java.util.Optional;

public interface ModuleVersionResolver<T extends de.hybris.platform.ruleengine.model.AbstractRulesModuleModel>
{
    Optional<Long> getDeployedModuleVersion(T paramT);


    Long extractModuleVersion(String paramString1, String paramString2);
}
