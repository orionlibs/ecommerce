package de.hybris.platform.ruleengine.drools;

import java.util.Optional;
import org.kie.api.builder.KieModule;

public interface KieModuleService
{
    void saveKieModule(String paramString1, String paramString2, KieModule paramKieModule);


    Optional<KieModule> loadKieModule(String paramString1, String paramString2);
}
