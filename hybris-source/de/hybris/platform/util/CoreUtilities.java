package de.hybris.platform.util;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import de.hybris.bootstrap.config.AbstractExtensionModule;
import de.hybris.bootstrap.config.ConfigUtil;
import de.hybris.bootstrap.config.ExtensionInfo;
import de.hybris.bootstrap.config.PlatformConfig;
import de.hybris.bootstrap.config.TenantInfo;
import de.hybris.bootstrap.config.WebExtensionModule;
import de.hybris.platform.util.config.ConfigIntf;
import de.hybris.platform.util.config.HybrisConfig;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;

public class CoreUtilities
{
    private static final String CONFIG_KEY_EXTENSIONS = "extension.envs";
    private static final String CONFIG_KEY_EXTENSIONWEBMODS = "extension.webmods";
    private static final Logger LOG = Logger.getLogger(CoreUtilities.class.getName());
    private static volatile boolean alreadyWarnedBecauseOfMissingExtension = false;
    private static volatile List<String> installedExtensionNames = null;
    private static volatile Map<String, Class> installedExtensionClassMappings = null;
    private static volatile Map<String, String> installedWebModules = null;
    private static volatile Map<String, Properties> tenantProperties = null;
    private static volatile Properties coreProperties = null;
    private final PlatformConfig bootstrapConfig;
    private final ConfigIntf platformConfig;


    public CoreUtilities(PlatformConfig config, boolean standaloneMode, int clusterNode)
    {
        this.bootstrapConfig = config;
        this.platformConfig = (ConfigIntf)new HybrisConfig(loadPlatformPropertiesOnce(this.bootstrapConfig), standaloneMode, clusterNode);
    }


    public ConfigIntf getPlatformConfig()
    {
        return this.platformConfig;
    }


    public Map<String, String> getAllConfigProps() throws IllegalStateException
    {
        return this.platformConfig.getAllParameters();
    }


    public List<String> getAllConfiguredExtensionNames() throws IllegalStateException
    {
        assureInstalledExtensionClassMapping();
        return installedExtensionNames;
    }


    PlatformConfig getBootstrapConfig()
    {
        return this.bootstrapConfig;
    }


    public String getConfigProperty(String key)
    {
        return this.platformConfig.getParameter(key);
    }


    public String getConfigProperty(String key, String defaultValue)
    {
        String result = getConfigProperty(key);
        if(result == null)
        {
            result = defaultValue;
        }
        return defaultValue;
    }


    boolean isCorePropertiesNotLoaded()
    {
        return (coreProperties == null);
    }


    Properties loadPlatformPropertiesOnce(PlatformConfig config)
    {
        if(isCorePropertiesNotLoaded())
        {
            Properties props = loadRuntimeProperties(config);
            for(Map.Entry<Object, Object> entry : System.getProperties().entrySet())
            {
                props.put(entry.getKey(), ((String)entry.getValue()).trim());
            }
            for(Iterator<?> it = props.keySet().iterator(); it.hasNext(); )
            {
                String key = (String)it.next();
                String value = props.getProperty(key);
                String unescapedValue = ConfigUtil.unescapeProperty(value);
                props.setProperty(key, unescapedValue);
            }
            setDefaultWebroots(props, config);
            coreProperties = props;
        }
        return coreProperties;
    }


    Properties loadRuntimeProperties(PlatformConfig config)
    {
        Properties props = new Properties();
        ConfigUtil.loadRuntimeProperties(props, config);
        tryToOverwritePropertiesFrom("/client.properties", props, CoreUtilities.class);
        tryToOverwritePropertiesFrom("/clientlocal.properties", props, CoreUtilities.class);
        return props;
    }


    private void setDefaultWebroots(Properties props, PlatformConfig config)
    {
        for(Iterator<ExtensionInfo> it = config.getExtensionInfosInBuildOrder().iterator(); it.hasNext(); )
        {
            ExtensionInfo info = it.next();
            for(Iterator<AbstractExtensionModule> it2 = info.getModules().iterator(); it2.hasNext(); )
            {
                Object o = it2.next();
                if(o instanceof WebExtensionModule)
                {
                    WebExtensionModule webmod = (WebExtensionModule)o;
                    if(props.getProperty(info.getName() + ".webroot") == null)
                    {
                        props.setProperty(info.getName() + ".webroot", webmod.getWebRoot());
                    }
                }
            }
        }
    }


    private void tryToOverwritePropertiesFrom(String path, Properties prop, Class clazz)
    {
        try
        {
            InputStream is = clazz.getResourceAsStream(path);
            prop.load(is);
            is.close();
        }
        catch(Exception exception)
        {
        }
    }


    private void assureInstalledExtensionClassMapping()
    {
        if(installedExtensionClassMappings == null)
        {
            installedExtensionClassMappings = getInstalledExtensionClassMappingNoCache();
            installedExtensionNames = Lists.newArrayList(installedExtensionClassMappings.keySet());
        }
    }


    Map<String, Class> getInstalledExtensionClassMapping()
    {
        assureInstalledExtensionClassMapping();
        return installedExtensionClassMappings;
    }


    Map<String, Class> getInstalledExtensionClassMappingNoCache()
    {
        String nameStr = this.platformConfig.getParameter("extension.envs");
        if(nameStr != null)
        {
            ImmutableMap.Builder<String, Class<?>> classMappingBuilder = ImmutableMap.builder();
            for(StringTokenizer st = new StringTokenizer(nameStr, " ,;\t\n\f\r"); st.hasMoreTokens(); )
            {
                String name = st.nextToken().trim();
                String clName = st.nextToken();
                try
                {
                    classMappingBuilder.put(name, Class.forName(clName));
                }
                catch(ClassNotFoundException e)
                {
                    warnCannotLoadExtension(name, e);
                }
                catch(NoClassDefFoundError e)
                {
                    warnCannotLoadExtension(name, e);
                }
            }
            return (Map<String, Class>)classMappingBuilder.build();
        }
        return Collections.EMPTY_MAP;
    }


    Map<String, String> getInstalledWebModules()
    {
        if(installedWebModules == null)
        {
            String nameStr = this.platformConfig.getParameter("extension.webmods");
            if(nameStr != null)
            {
                Map<String, String> webmods_temp = new HashMap<>();
                String[] extensions = nameStr.split(";");
                for(int index = 0; index < extensions.length; index++)
                {
                    String[] singleext = extensions[index].split(",");
                    if(singleext.length == 1)
                    {
                        webmods_temp.put(singleext[0], "");
                    }
                    else if(singleext.length == 2)
                    {
                        if(!singleext[1].trim().equals("<disabled>"))
                        {
                            webmods_temp.put(singleext[0], singleext[1].trim());
                        }
                    }
                }
                installedWebModules = (Map<String, String>)ImmutableMap.copyOf(webmods_temp);
            }
            else
            {
                installedWebModules = Collections.EMPTY_MAP;
            }
        }
        return installedWebModules;
    }


    private void warnCannotLoadExtension(String n, Throwable e)
    {
        if(!alreadyWarnedBecauseOfMissingExtension)
        {
            LOG.warn("cannot load extension " + n + " (and others?) because: " + e);
            LOG.warn("If you receive this error while starting from eclipse, you need to add the project that ");
            LOG.warn("contains the " + n + " extension to the java build path of your project from where you have");
            LOG.warn("started this class. (project->right click->properties->build path->tab Projects.");
            alreadyWarnedBecauseOfMissingExtension = true;
        }
    }


    Map<String, Properties> getTenantInfos()
    {
        if(tenantProperties == null)
        {
            Map<String, TenantInfo> tenantInfos = getBootstrapConfig().getTenantInfos();
            ImmutableMap.Builder<String, Properties> tenantPropsBuilder = new ImmutableMap.Builder();
            for(TenantInfo info : tenantInfos.values())
            {
                tenantPropsBuilder.put(info.getTenantId(), info.getTenantProperties());
            }
            tenantProperties = (Map<String, Properties>)tenantPropsBuilder.build();
        }
        return tenantProperties;
    }
}
