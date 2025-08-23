package de.hybris.platform.ruleengine.init;

import de.hybris.platform.ruleengine.cache.KIEModuleCacheBuilder;
import java.util.List;
import org.kie.api.builder.ReleaseId;
import org.kie.api.builder.model.KieModuleModel;

public interface RulePublishingSpliterator
{
    RulePublishingFuture publishRulesAsync(KieModuleModel paramKieModuleModel, ReleaseId paramReleaseId, List<String> paramList, KIEModuleCacheBuilder paramKIEModuleCacheBuilder);
}
