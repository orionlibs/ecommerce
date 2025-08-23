package de.hybris.platform.solrserver.strategies.impl;

import de.hybris.bootstrap.config.ConfigUtil;
import de.hybris.bootstrap.config.PlatformConfig;
import de.hybris.bootstrap.config.SystemConfig;
import de.hybris.platform.solrserver.strategies.SolrServerConfigurationProvider;
import de.hybris.platform.solrserver.util.VersionUtils;
import de.hybris.platform.util.Utilities;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class DefaultSolrServerConfigurationProvider implements SolrServerConfigurationProvider
{
    public Map<String, String> getConfiguration()
    {
        Map<String, String> configuration = new HashMap<>();
        configuration.putAll(Utilities.getConfig().getAllParameters());
        PlatformConfig platformConfig = Utilities.getPlatformConfig();
        SystemConfig systemConfig = ConfigUtil.getSystemConfig(platformConfig.getPlatformHome().getAbsolutePath());
        configuration.put("HYBRIS_CONFIG_PATH", systemConfig.getConfigDir().getAbsolutePath());
        configuration.put("HYBRIS_DATA_PATH", systemConfig.getDataDir().getAbsolutePath());
        configuration.put("HYBRIS_LOG_PATH", systemConfig.getLogDir().getAbsolutePath());
        String extDir = Utilities.getExtensionInfo("solrserver").getExtensionDirectory().getAbsolutePath();
        String solrServerVersion = configuration.get("solrserver.solr.server.version");
        String versionPath = VersionUtils.getVersionPath(solrServerVersion);
        Path solrServerPath = Paths.get(extDir, new String[] {"resources", "solr", versionPath, "server"});
        configuration.put("SOLR_SERVER_PATH", solrServerPath.toString());
        return configuration;
    }
}
