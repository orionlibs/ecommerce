package de.hybris.platform.core.initialization;

import de.hybris.platform.core.model.initialization.SystemSetupAuditModel;

public interface SystemSetupAuditDAO
{
    boolean isPatchApplied(String paramString);


    SystemSetupAuditModel storeSystemPatchAction(SystemSetupCollectorResult paramSystemSetupCollectorResult);
}
