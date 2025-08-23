package de.hybris.platform.governor;

import com.google.common.annotations.Beta;
import de.hybris.platform.core.Registry;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Beta
public final class ResourceGovernorProvider
{
    private static final Logger LOG = LoggerFactory.getLogger(ResourceGovernorProvider.class);
    private static final ResourceGovernorProvider INSTANCE = new ResourceGovernorProvider();
    private static final NoOpGovernor NO_OP_GOVERNOR = new NoOpGovernor();
    private static final String GOVERNOR_BEAN_NAME = StringUtils.uncapitalize(ResourceGovernor.class.getSimpleName());


    @Beta
    public static ResourceGovernorProvider getInstance()
    {
        return INSTANCE;
    }


    @Beta
    public ResourceGovernor getResourceGovernor()
    {
        if(!Registry.isCurrentTenantStarted())
        {
            LOG.debug("Tenant is not running. The '{}' will be used.", NO_OP_GOVERNOR.getClass().getSimpleName());
            return (ResourceGovernor)NO_OP_GOVERNOR;
        }
        return (ResourceGovernor)Registry.getCoreApplicationContext().getBean(GOVERNOR_BEAN_NAME, ResourceGovernor.class);
    }
}
