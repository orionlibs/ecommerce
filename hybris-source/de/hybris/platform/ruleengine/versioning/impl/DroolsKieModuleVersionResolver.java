package de.hybris.platform.ruleengine.versioning.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.ruleengine.model.DroolsKIEModuleModel;
import de.hybris.platform.ruleengine.versioning.ModuleVersionResolver;
import java.util.Optional;

public class DroolsKieModuleVersionResolver implements ModuleVersionResolver<DroolsKIEModuleModel>
{
    public Optional<Long> getDeployedModuleVersion(DroolsKIEModuleModel rulesModule)
    {
        Preconditions.checkNotNull(rulesModule, "The instance of DroolsKIEModuleModel must not be null here");
        return Optional.<String>ofNullable(rulesModule.getDeployedMvnVersion()).map(v -> extractModuleVersion(rulesModule.getName(), v));
    }


    public Long extractModuleVersion(String moduleName, String deployedMvnVersion)
    {
        Long deployedModuleVersion = null;
        try
        {
            int idx = deployedMvnVersion.lastIndexOf('.');
            if(idx != -1 && deployedMvnVersion.length() > idx + 1)
            {
                deployedModuleVersion = Long.valueOf(Long.parseLong(deployedMvnVersion.substring(idx + 1).trim()));
            }
        }
        catch(RuntimeException e)
        {
            throw new IllegalArgumentException("Error during the deployed version of module [" + moduleName + "] occurred: ", e);
        }
        return deployedModuleVersion;
    }
}
