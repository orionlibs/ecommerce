package de.hybris.platform.hac.facade;

import de.hybris.bootstrap.config.ConfigUtil;
import de.hybris.bootstrap.config.ExtensionInfo;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;

public class HacExtensionsFacade
{
    public Collection<ExtensionInfo> getTenantSpecificExtensions()
    {
        Tenant tenant = Objects.<Tenant>requireNonNull(Registry.getCurrentTenantNoFallback(), "Tenant must be available");
        if("master".equals(tenant.getTenantID()))
        {
            return new LinkedHashSet<>(getAllExtensions());
        }
        HashSet<String> allowedNames = new HashSet<>(tenant.getTenantSpecificExtensionNames());
        return (Collection<ExtensionInfo>)getAllExtensions().stream().filter(e -> isTenantSpecificExtension(e, allowedNames))
                        .collect(Collectors.toCollection(LinkedHashSet::new));
    }


    private List<ExtensionInfo> getAllExtensions()
    {
        return ConfigUtil.getPlatformConfig(Registry.class).getExtensionInfosInBuildOrder();
    }


    private boolean isTenantSpecificExtension(ExtensionInfo ext, HashSet<String> allowedNames)
    {
        return (ext.getCoreModule() == null || allowedNames.contains(ext.getName()));
    }


    public String getWebrootForExtension(ExtensionInfo info)
    {
        if(info.isWebExtension())
        {
            String webroot = info.getWebModule().getWebRoot();
            if(StringUtils.isBlank(webroot))
            {
                webroot = "/";
            }
            else
            {
                webroot = Utilities.getWebroot(info.getName(), Registry.getCurrentTenantNoFallback().getTenantID());
            }
            return webroot;
        }
        return null;
    }
}
