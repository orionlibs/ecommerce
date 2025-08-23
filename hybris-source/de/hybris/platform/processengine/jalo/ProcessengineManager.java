package de.hybris.platform.processengine.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.cronjob.jalo.GeneratedCronJobManager;

@Deprecated(since = "ages", forRemoval = false)
public class ProcessengineManager extends GeneratedCronJobManager
{
    public static ProcessengineManager getInstance()
    {
        return (ProcessengineManager)Registry.getCurrentTenant().getJaloConnection().getExtensionManager()
                        .getExtension("processing");
    }
}
