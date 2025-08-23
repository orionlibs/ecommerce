package de.hybris.platform.ruleengine.init;

import de.hybris.platform.ruleengine.model.DroolsRuleModel;
import java.util.Collection;
import org.kie.api.builder.ReleaseId;

public interface IncrementalRuleEngineUpdateStrategy
{
    boolean shouldUpdateIncrementally(ReleaseId paramReleaseId, String paramString, Collection<DroolsRuleModel> paramCollection1, Collection<DroolsRuleModel> paramCollection2);
}
