package de.hybris.bootstrap.config;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class TenantsInfoLoader
{
    public static final String FORBIDDEN_EXTENSIONS = "forbidden.extensions";
    public static final String ALLOWED_EXTENSIONS = "allowed.extensions";
    private final PlatformConfig platformConfig;


    public TenantsInfoLoader(PlatformConfig platformConfig)
    {
        this.platformConfig = platformConfig;
    }


    private Map<String, String> decorateParams(Properties props)
    {
        SystemConfig sc = this.platformConfig.getSystemConfig();
        Map<String, String> dataIn = props;
        Map<String, String> dataOut = new LinkedHashMap<>();
        for(Map.Entry<String, String> e : dataIn.entrySet())
        {
            dataOut.put(e.getKey(), sc.replaceProperties(e.getValue()));
        }
        if(isBlank(dataOut.get("allowed.extensions")))
        {
            dataOut.put("allowed.extensions", getPlatformExtensionNamesInBuildOrder(parseForbiddenExtensionsConfig(dataOut)));
        }
        return dataOut;
    }


    private Collection<String> parseForbiddenExtensionsConfig(Map<String, String> dataOut)
    {
        Collection<String> forbidden = Collections.emptyList();
        if(!isBlank(dataOut.get("forbidden.extensions")))
        {
            forbidden = new ArrayList<>();
            for(String token : ((String)dataOut.get("forbidden.extensions")).split(";"))
            {
                if(!isBlank(token))
                {
                    forbidden.add(token.trim());
                }
            }
        }
        return forbidden;
    }


    private String getPlatformExtensionNamesInBuildOrder(Collection<String> forbidden)
    {
        Set<String> allowed = new LinkedHashSet<>();
        for(ExtensionInfo info : getPlatformConfig().getExtensionInfosInBuildOrder())
        {
            allowed.add(info.getName());
        }
        allowed.removeAll(forbidden);
        StringBuilder result = new StringBuilder(200);
        for(String entry : allowed)
        {
            result.append(entry).append(";");
        }
        return result.substring(0, result.length() - 1);
    }


    private boolean isBlank(String string)
    {
        int strLen;
        if(string == null || (strLen = string.length()) == 0)
        {
            return true;
        }
        for(int i = 0; i < strLen; i++)
        {
            if(!Character.isWhitespace(string.charAt(i)))
            {
                return false;
            }
        }
        return true;
    }


    protected Collection<String> getInstalledTenantIDs()
    {
        return ConfigUtil.loadInstalledTenantIDsFromConfig(getPlatformConfig());
    }


    protected PlatformConfig getPlatformConfig()
    {
        return this.platformConfig;
    }


    public Map<String, TenantInfo> getSlaveTenants()
    {
        Collection<String> tenantIDs = getInstalledTenantIDs();
        return tenantIDs.isEmpty() ? Collections.<String, TenantInfo>emptyMap() : loadSlaveTenantsProperties(tenantIDs);
    }


    private Map<String, TenantInfo> loadSlaveTenantsProperties(Collection<String> tenantIDs)
    {
        Map<String, TenantInfo> tenantInfos = new HashMap<>(tenantIDs.size() * 2);
        for(String tenantID : tenantIDs)
        {
            Map<String, String> tenantProps = decorateParams(loadSlaveTenantProperties(tenantID));
            tenantInfos.put(tenantID, new TenantInfo(tenantID, tenantProps));
        }
        return Collections.unmodifiableMap(tenantInfos);
    }


    private Properties loadSlaveTenantProperties(String tenantID)
    {
        String tenantPropertiesFile = "tenant_" + tenantID + ".properties";
        Properties props = new Properties();
        File platformProps = new File(getPlatformHome(), tenantPropertiesFile);
        File configProps = new File(getConfigDir(), "local_" + tenantPropertiesFile);
        boolean platformPropsExist = loadPropertiesIfFileExists(props, platformProps);
        boolean configPropsExist = loadPropertiesIfFileExists(props, configProps);
        StringBuilder sb = new StringBuilder();
        boolean extensionPropsExist = false;
        for(ExtensionInfo info : this.platformConfig.getExtensionInfosInBuildOrder())
        {
            File propFile = new File(info.getExtensionDirectory(), tenantPropertiesFile);
            extensionPropsExist = (loadPropertiesIfFileExists(props, propFile) || extensionPropsExist);
            sb.append(propFile).append(", ");
        }
        if(!platformPropsExist && !configPropsExist && !extensionPropsExist)
        {
            throw new BootstrapConfigException("Cannot find any tenant config file! Tried: " + platformProps + ", " + configProps + ", " +
                            getPathsString(sb) + ".");
        }
        return props;
    }


    private String getPathsString(StringBuilder sb)
    {
        int idx = sb.lastIndexOf(",");
        return (idx < 0) ? sb.toString() : sb.substring(0, idx);
    }


    protected File getPlatformHome()
    {
        return getPlatformConfig().getPlatformHome();
    }


    protected File getConfigDir()
    {
        return getPlatformConfig().getSystemConfig().getConfigDir();
    }


    private boolean loadPropertiesIfFileExists(Properties props, File resource)
    {
        try
        {
            if(!resource.exists())
            {
                return false;
            }
            InputStream inpuStream = new BufferedInputStream(new FileInputStream(resource));
            try
            {
                props.load(inpuStream);
                boolean bool = true;
                inpuStream.close();
                return bool;
            }
            catch(Throwable throwable)
            {
                try
                {
                    inpuStream.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
                throw throwable;
            }
        }
        catch(IOException e)
        {
            throw new BootstrapConfigException("Can not load properties from " + resource.getAbsolutePath(), e);
        }
    }
}
