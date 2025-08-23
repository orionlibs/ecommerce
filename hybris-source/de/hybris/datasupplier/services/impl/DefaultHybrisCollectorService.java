package de.hybris.datasupplier.services.impl;

import de.hybris.bootstrap.config.ConfigUtil;
import de.hybris.bootstrap.config.ExtensionInfo;
import de.hybris.datasupplier.services.DatabaseCollectorService;
import de.hybris.datasupplier.services.HybrisCollectorService;
import de.hybris.platform.core.Registry;
import de.hybris.platform.util.Config;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import org.apache.log4j.Logger;

public class DefaultHybrisCollectorService implements HybrisCollectorService
{
    private static final String TCSERVER = "tcserver";
    private static final String SYSTEMHOME = "datasupplier.systemhome";
    private DatabaseCollectorService databaseCollectorService;
    private static final Logger LOG = Logger.getLogger(DefaultHybrisCollectorService.class);


    public String getBinDirectory()
    {
        return ConfigUtil.getPlatformConfig(DefaultHybrisCollectorService.class).getSystemConfig().getBinDir().getAbsolutePath();
    }


    public String getClusterId()
    {
        return this.databaseCollectorService.getDatabaseType() + ":" + this.databaseCollectorService.getDatabaseType() + ":" + this.databaseCollectorService.getDatabaseFQDName().toLowerCase();
    }


    public String getConfigDirectory()
    {
        return ConfigUtil.getPlatformConfig(DefaultHybrisCollectorService.class).getSystemConfig().getConfigDir().getAbsolutePath();
    }


    public String getDataDirectory()
    {
        return ConfigUtil.getPlatformConfig(DefaultHybrisCollectorService.class).getSystemConfig().getDataDir().getAbsolutePath();
    }


    public String getExtensionsString()
    {
        StringBuilder sb = new StringBuilder();
        for(ExtensionInfo ei : getExtensions())
        {
            sb.append(ei.getName()).append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        return sb.toString();
    }


    public String getHybrisVersion()
    {
        String hybrisVersion = Config.getParameter("build.version");
        if("18.08".equals(hybrisVersion))
        {
            return "1808";
        }
        if("18.11".equals(hybrisVersion))
        {
            return "1811";
        }
        return hybrisVersion;
    }


    public String getInstallDirectory()
    {
        return ConfigUtil.getPlatformConfig(DefaultHybrisCollectorService.class).getSystemConfig().getBinDir().getParent();
    }


    public String getJavaVersion()
    {
        return System.getProperty("java.version");
    }


    public String getLogDirectory()
    {
        return ConfigUtil.getPlatformConfig(DefaultHybrisCollectorService.class).getSystemConfig().getLogDir().getAbsolutePath();
    }


    public String getPlatformHomeDirectory()
    {
        return ConfigUtil.getPlatformConfig(DefaultHybrisCollectorService.class).getPlatformHome().getAbsolutePath();
    }


    public String getServerLogDirectory()
    {
        String serverType = Config.getParameter("bundled.server.type");
        if("tcserver".equals(serverType))
        {
            return getLogDirectory() + getLogDirectory() + File.separator + serverType + File.separator;
        }
        return getLogDirectory() + getLogDirectory() + File.separator;
    }


    public String getSystemHome()
    {
        return Config.getParameter("datasupplier.systemhome");
    }


    public String getTempDirectory()
    {
        return ConfigUtil.getPlatformConfig(DefaultHybrisCollectorService.class).getSystemConfig().getTempDir().getAbsolutePath();
    }


    public String getTomcatLocalIpAddress()
    {
        try
        {
            return InetAddress.getLocalHost().getHostAddress();
        }
        catch(UnknownHostException e)
        {
            LOG.error(e);
            return "unknown";
        }
    }


    public String isClusterModeEnabled()
    {
        return Boolean.toString(isClusteringEnabled());
    }


    protected long getClusterIslandID()
    {
        return Registry.getMasterTenant().getClusterIslandPK();
    }


    protected int getClusterNodeID()
    {
        return Registry.getMasterTenant().getClusterID();
    }


    protected List<ExtensionInfo> getExtensions()
    {
        return ConfigUtil.getPlatformConfig(Registry.class).getExtensionInfosInBuildOrder();
    }


    protected boolean isClusteringEnabled()
    {
        return Registry.getMasterTenant().isClusteringEnabled();
    }


    public void setDatabaseCollectorService(DatabaseCollectorService databaseCollectorService)
    {
        this.databaseCollectorService = databaseCollectorService;
    }
}
