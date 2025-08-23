package de.hybris.bootstrap.config;

import de.hybris.bootstrap.util.LocaleHelper;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ConfigUtil
{
    public static final String ENV_PROPERTIES_PREFIX = "env.properties.prefix";
    private static final String FILE_PROJECT_PROPERTIES = "project.properties";
    private static final String FILE_PRODUCTION_PROPERTIES = "production.properties";
    private static final String FILE_ADVANCED_PROPERTIES = "advanced.properties";
    private static final String FILE_LOCAL_PROPERTIES;
    private static final String FILE_ENV_PROPERTIES = "env.properties";
    private static final String FILE_BUILD_NUMBER = "build.number";
    private static final String INSTALLED_TENANTS = "installed.tenants";
    private static final String OPTIONAL_CONFIG_DIR_PROP = "hybris.optional.config.dir";
    static final String CONFIG_FILE_ENCODING_PROP = "hybris.config.file.encoding";
    private static final String RUNTIME_PROPERTIES_ENV = "HYBRIS_RUNTIME_PROPERTIES";
    private static final String OPTIONAL_CONFIG_DIR_ENV = "HYBRIS_OPT_CONFIG_DIR";
    private static final String DEVELOPMENT_MODE_PROPERTY = "development.mode";
    private static final String CONFIG_DIR_REL_TO_PLATFORM_HOME = "/../../config";
    private static final Map<String, String> ENV_OVERWRITABLE_PROPERTIES = new HashMap<>();

    static
    {
        ENV_OVERWRITABLE_PROPERTIES.put("HTTP_CONNECTOR_SECURE", "tomcat.http.connector.secure");
        ENV_OVERWRITABLE_PROPERTIES.put("HTTP_PORT", "tomcat.http.port");
        ENV_OVERWRITABLE_PROPERTIES.put("HTTPS_PORT", "tomcat.https.port");
    }

    static EnvProvider envProvider = new EnvProvider();
    private static final AtomicReference<String> HYBRIS_OPTIONAL_CONFIG_DIR = new AtomicReference<>("");

    static
    {
        String useconfig = System.getProperty("useconfig");
        if(useconfig == null || useconfig.isEmpty())
        {
            FILE_LOCAL_PROPERTIES = "local.properties";
        }
        else
        {
            FILE_LOCAL_PROPERTIES = "local" + System.getProperty("useconfig") + ".properties";
            System.out.println("Using local properties file " + FILE_LOCAL_PROPERTIES);
        }
    }

    private static File getPlatformHome(Class clazz)
    {
        try
        {
            File bootstrapFile, platformHome;
            URL url = clazz.getResource("/core-items.xml");
            if(url == null)
            {
                throw new BootstrapConfigException("Can not determine path to core-items.xml");
            }
            String rawBootstrapFile = URLDecoder.decode(url.getFile(), "UTF-8");
            boolean glassfish = Boolean.parseBoolean(System.getProperty("glassfish", "false"));
            if(glassfish)
            {
                String other = rawBootstrapFile.substring(0, rawBootstrapFile.lastIndexOf('!')).substring("file:".length());
                bootstrapFile = new File(other);
            }
            else
            {
                bootstrapFile = new File(rawBootstrapFile);
            }
            if(!bootstrapFile.exists())
            {
                throw new BootstrapConfigException("Can not find path to core-items.xml (" + bootstrapFile
                                .getAbsolutePath() + ")");
            }
            if(glassfish)
            {
                File deploymentDir = bootstrapFile.getParentFile().getParentFile();
                if(deploymentDir == null)
                {
                    throw new BootstrapConfigException("Can not determine path to platformhome");
                }
                platformHome = new File(bootstrapFile.getParentFile().getParentFile(), "bin/platform");
            }
            else
            {
                platformHome = bootstrapFile.getParentFile().getParentFile().getParentFile().getParentFile();
                if(platformHome == null)
                {
                    throw new BootstrapConfigException("Can not determine path to platformhome");
                }
            }
            if(!platformHome.exists())
            {
                throw new BootstrapConfigException("Can not determine path to platformhome (" + platformHome
                                .getAbsolutePath() + ")");
            }
            return platformHome;
        }
        catch(Exception e)
        {
            throw new BootstrapConfigException("Can not determine platformhome", e);
        }
    }


    public static SystemConfig getSystemConfig(String platformHome)
    {
        try
        {
            File platformHomeFile = new File(platformHome);
            if(!platformHomeFile.exists())
            {
                throw new BootstrapConfigException("Can not determine platform home");
            }
            File envFile = getEnvPropertiesFile(platformHomeFile);
            Properties envProps = new Properties();
            envProps.setProperty("platformhome", platformHomeFile.getCanonicalPath());
            if(envFile.exists())
            {
                loadProperties(envProps, envFile);
                if(!envProps.containsKey("HYBRIS_BOOTSTRAP_BIN_DIR"))
                {
                    envProps.put("HYBRIS_BOOTSTRAP_BIN_DIR", "${platformhome}/bootstrap/bin");
                }
            }
            else
            {
                setDefaultProperties(envProps);
            }
            return SystemConfig.getInstanceByProps(envProps);
        }
        catch(Exception e)
        {
            throw new BootstrapConfigException("Can not load env.properties via file system", e);
        }
    }


    private static File getEnvPropertiesFile(File platformHomeFile)
    {
        File envFile;
        String optionalConfigFolder = envProvider.getOptionalConfigDirPath();
        if(optionalConfigFolder != null && optionalConfigFolder.trim().length() > 0)
        {
            envFile = new File(optionalConfigFolder, "env.properties");
            if(envFile.exists() && envFile.isFile())
            {
                return envFile;
            }
        }
        else
        {
            envFile = new File(platformHomeFile, "env.properties");
        }
        return envFile;
    }


    private static void setDefaultProperties(Properties envProps)
    {
        envProps.put("HYBRIS_BIN_DIR", "${platformhome}/../../bin");
        envProps.put("HYBRIS_CONFIG_DIR", "${platformhome}/../../config");
        envProps.put("HYBRIS_DATA_DIR", "${platformhome}/../../data");
        envProps.put("HYBRIS_LOG_DIR", "${platformhome}/../../log");
        envProps.put("HYBRIS_TEMP_DIR", "${platformhome}/../../temp");
        envProps.put("HYBRIS_BOOTSTRAP_BIN_DIR", "${platformhome}/bootstrap/bin");
    }


    public static Collection<String> loadInstalledTenantIDsFromConfig(PlatformConfig platformConfig)
    {
        Properties props = new Properties();
        loadRuntimeProperties(props, platformConfig);
        String tenantIDs = props.getProperty("installed.tenants");
        if(tenantIDs == null || tenantIDs.length() == 0)
        {
            return Collections.emptyList();
        }
        Collection<String> ret = new ArrayList<>();
        for(String token : tenantIDs.split("[,; ]+"))
        {
            String id = token.trim();
            if(id.length() > 0)
            {
                ret.add(id);
            }
        }
        return Collections.unmodifiableCollection(ret);
    }


    private static boolean isProductionEnvironment(PlatformConfig platformConfig)
    {
        File projectProperties = new File(platformConfig.getPlatformHome(), "project.properties");
        File configDir = platformConfig.getSystemConfig().getConfigDir();
        File localPropertiesFile = new File(configDir, FILE_LOCAL_PROPERTIES);
        List<File> configFiles = new ArrayList<>();
        configFiles.add(projectProperties);
        configFiles.add(localPropertiesFile);
        boolean isDevelopment = true;
        for(File file : configFiles)
        {
            if(file.exists())
            {
                Properties localProperties = new Properties();
                loadProperties(localProperties, file);
                if(localProperties.containsKey("development.mode"))
                {
                    isDevelopment = "true".equalsIgnoreCase(localProperties.getProperty("development.mode"));
                }
            }
        }
        Properties runtimeProperties = new Properties();
        loadHybrisRuntimeProperties(runtimeProperties);
        if(runtimeProperties.containsKey("development.mode"))
        {
            isDevelopment = "true".equalsIgnoreCase(runtimeProperties.getProperty("development.mode"));
        }
        Properties optionalConfigDirProperties = new Properties();
        loadHybrisOptionalConfigDir(optionalConfigDirProperties, platformConfig);
        if(optionalConfigDirProperties.containsKey("development.mode"))
        {
            isDevelopment = "true".equalsIgnoreCase(optionalConfigDirProperties.getProperty("development.mode"));
        }
        Properties envProperties = new Properties();
        loadHybrisPropertiesFromEnvironment(envProperties);
        if(envProperties.containsKey("development.mode"))
        {
            isDevelopment = "true".equalsIgnoreCase(envProperties.getProperty("development.mode"));
        }
        return !isDevelopment;
    }


    public static void loadRuntimeProperties(Properties props, PlatformConfig platformConfig)
    {
        try
        {
            File numberFile = new File(platformConfig.getSystemConfig().getDataDir(), "build.number");
            if(numberFile.exists())
            {
                loadProperties(props, numberFile);
            }
            boolean productionEnv = isProductionEnvironment(platformConfig);
            File propFile = new File(new File(platformConfig.getPlatformHome(), "resources"), "advanced.properties");
            loadProperties(props, propFile);
            propFile = new File(platformConfig.getPlatformHome(), "project.properties");
            loadProperties(props, propFile);
            for(ExtensionInfo info : platformConfig.getExtensionInfosInBuildOrder())
            {
                propFile = new File(info.getExtensionDirectory(), "project.properties");
                if(propFile.exists())
                {
                    loadProperties(props, propFile);
                }
                File productionProps = new File(info.getExtensionDirectory(), "production.properties");
                if(productionEnv && productionProps.exists())
                {
                    loadProperties(props, productionProps);
                }
            }
            propFile = new File(platformConfig.getSystemConfig().getConfigDir(), FILE_LOCAL_PROPERTIES);
            if(propFile.exists())
            {
                loadProperties(props, propFile);
            }
            loadHybrisRuntimeProperties(props);
            loadHybrisOptionalConfigDir(props, platformConfig);
            loadHybrisPropertiesFromEnvironment(props);
            updateOverwrittenExtensionProperties(props, platformConfig);
            updateOverwrittenServerProperties(props);
            expandProperties(props, platformConfig);
            props.setProperty("development.mode", !productionEnv ? "true" : "false");
        }
        catch(Exception e)
        {
            throw new BootstrapConfigException("Can not load properties", e);
        }
    }


    static void updateOverwrittenServerProperties(Properties props)
    {
        for(String envPropertyName : ENV_OVERWRITABLE_PROPERTIES.keySet())
        {
            String envPropertyValue = getPropertyOrEnv(envPropertyName);
            if(envPropertyValue != null && !"".equals(envPropertyValue.trim()))
            {
                props.put(envToHybrisProperty(envPropertyName), envPropertyValue);
            }
        }
    }


    private static String envToHybrisProperty(String envProperty)
    {
        return ENV_OVERWRITABLE_PROPERTIES.get(envProperty);
    }


    public static String getPropertyOrEnv(String name)
    {
        String envValue = envProvider.getenv(name);
        if(envValue != null)
        {
            return envValue;
        }
        return envProvider.getProperty(name);
    }


    static void updateOverwrittenExtensionProperties(Properties props, PlatformConfig platformConfig)
    {
        String extensionnames = "";
        String extensionenvs = "";
        String extensionwebmods = "";
        for(ExtensionInfo info : platformConfig.getExtensionInfosInBuildOrder())
        {
            extensionnames = extensionnames + extensionnames + ";";
            if(info.getCoreModule() != null)
            {
                String ejbmanager = info.getCoreModule().getManager();
                String jalomanager = ejbmanager.replace(".session.", ".jalo.");
                extensionenvs = extensionenvs + extensionenvs + "," + info.getName() + ";";
            }
            if(info.getWebModule() != null)
            {
                String webroot = props.getProperty(info.getName() + ".webroot", info.getWebModule().getWebRoot());
                if(webroot == null)
                {
                    webroot = "";
                }
                info.getWebModule().setWebRoot(webroot);
                extensionwebmods = extensionwebmods + extensionwebmods + "," + info.getName() + ";";
            }
        }
        if(extensionnames.length() > 0)
        {
            extensionnames = extensionnames.substring(0, extensionnames.length() - 1);
        }
        props.setProperty("extension.names", extensionnames);
        props.setProperty("extension.envs", extensionenvs);
        props.setProperty("extension.webmods", extensionwebmods);
    }


    private static void loadHybrisRuntimeProperties(Properties props)
    {
        String runtimePropertiesPath = envProvider.getRuntimePropertiesPath();
        if(runtimePropertiesPath != null)
        {
            File runtimePropFile = new File(runtimePropertiesPath);
            if(runtimePropFile.exists())
            {
                System.out.print("HYBRIS_RUNTIME_PROPERTIES environment variable is set.");
                System.out.println(" Loading additional properties from " + runtimePropertiesPath);
                loadProperties(props, runtimePropFile);
            }
        }
    }


    private static void loadHybrisOptionalConfigDir(Properties props, PlatformConfig platformConfig)
    {
        boolean propertyIsSet, propertyHasChanged;
        String configDirPath = envProvider.getOptionalConfigDirPath();
        if(configDirPath == null || configDirPath.isEmpty())
        {
            configDirPath = props.getProperty("hybris.optional.config.dir");
            propertyIsSet = false;
        }
        else
        {
            propertyIsSet = true;
        }
        configDirPath = platformConfig.getSystemConfig().replaceProperties(configDirPath);
        if(configDirPath != null)
        {
            propertyHasChanged = !Objects.equals(HYBRIS_OPTIONAL_CONFIG_DIR.getAndSet(configDirPath), configDirPath);
        }
        else
        {
            propertyHasChanged = false;
        }
        if(propertyIsSet && propertyHasChanged)
        {
            System.out.print("HYBRIS_OPT_CONFIG_DIR environment variable is set.");
        }
        if(configDirPath != null)
        {
            if(propertyHasChanged)
            {
                System.out.println(" Loading optional hybris properties from " + configDirPath + " directory");
            }
            Properties properties = DirectoryConfigLoader.loadFromDir(configDirPath, FILE_LOCAL_PROPERTIES);
            props.putAll(properties);
        }
    }


    private static void loadHybrisPropertiesFromEnvironment(Properties props)
    {
        String prefix = props.getProperty("env.properties.prefix", "y_");
        Map<String, String> fromEnv = filterAndUnescapeEnvSettings(envProvider.getEnv(), prefix);
        props.putAll(fromEnv);
    }


    private static Map<String, String> filterAndUnescapeEnvSettings(Map<String, String> all, String prefix)
    {
        Map<String, String> ret = new LinkedHashMap<>();
        for(Map.Entry<String, String> e : all.entrySet())
        {
            String validKey = checkAndTransformKey(e.getKey(), prefix);
            if(validKey != null)
            {
                ret.put(validKey, e.getValue());
            }
        }
        return ret;
    }


    private static String checkAndTransformKey(String fullKey, String prefix)
    {
        if(fullKey.toLowerCase(LocaleHelper.getPersistenceLocale()).startsWith(prefix))
        {
            return unescapeKey(fullKey.substring(prefix.length()));
        }
        return null;
    }


    private static String unescapeKey(String key)
    {
        StringBuilder buffer = new StringBuilder(key.length());
        int i = 0;
        for(int s = key.length(); i < s; i++)
        {
            char c = key.charAt(i);
            if('_' == c)
            {
                if(i + 1 < s && '_' == key.charAt(i + 1))
                {
                    buffer.append(c);
                    i++;
                }
                else
                {
                    buffer.append('.');
                }
            }
            else
            {
                buffer.append(c);
            }
        }
        return buffer.toString();
    }


    private static void expandProperties(Properties props, PlatformConfig platformConfig)
    {
        for(Map.Entry<Object, Object> entry : props.entrySet())
        {
            String value = (String)entry.getValue();
            if(value.contains("${"))
            {
                expandProperty(value, platformConfig, props, (String)entry.getKey());
            }
        }
    }


    private static void expandProperty(String value, PlatformConfig platformConfig, Properties props, String key)
    {
        String replacement = "";
        int lastIndex = 0;
        Pattern pattern = Pattern.compile("\\$\\{[^\\}]*\\}");
        Matcher matcher = pattern.matcher(value);
        String newKey = null;
        while(matcher.find())
        {
            replacement = replacement + replacement;
            newKey = matcher.group().substring(2, matcher.group().length() - 1);
            String newValue = platformConfig.getSystemConfig().getDir(newKey);
            if(newValue == null)
            {
                newValue = props.getProperty(newKey);
                if(newValue != null && newValue.contains("${"))
                {
                    expandProperty(newValue, platformConfig, props, newKey);
                    newValue = props.getProperty(newKey);
                }
            }
            if(newValue == null)
            {
                replacement = replacement + replacement;
            }
            else
            {
                replacement = replacement + replacement;
            }
            lastIndex = matcher.end();
        }
        if(lastIndex < value.length())
        {
            replacement = replacement + replacement;
        }
        if(key != null)
        {
            props.put(key, replacement);
        }
    }


    public static PlatformConfig getPlatformConfig(Class clazz)
    {
        try
        {
            return PlatformConfig.getInstance(getSystemConfig(getPlatformHome(clazz).getCanonicalPath()));
        }
        catch(IOException e)
        {
            throw new BootstrapConfigException("Error while loading platform config", e);
        }
    }


    public static String getConfigDirPath(Class clazz)
    {
        return "" + getPlatformHome(clazz) + "/../../config";
    }


    public static void loadLog4JProperties(Properties props, Class clazz)
    {
        PlatformConfig platformConfig = getPlatformConfig(clazz);
        loadRuntimeProperties(props, platformConfig);
        for(Iterator<Object> iter = props.keySet().iterator(); iter.hasNext(); )
        {
            String key = (String)iter.next();
            if(!key.startsWith("log4j"))
            {
                iter.remove();
            }
        }
    }


    public static void loadVelocityProperties(Properties props, Class clazz)
    {
        PlatformConfig platformConfig = getPlatformConfig(clazz);
        Properties p = new Properties();
        loadRuntimeProperties(p, platformConfig);
        p.entrySet().stream().filter(e -> ((String)e.getKey()).startsWith("velocity."))
                        .forEach(e -> props.put(((String)e.getKey()).substring("velocity.".length()), e.getValue()));
        overrideDefaultVelocityPropertiesInCaseOfUseOldPatternProperties(props);
    }


    static void overrideDefaultVelocityPropertiesInCaseOfUseOldPatternProperties(Properties properties)
    {
        for(Map.Entry<Object, Object> property : properties.entrySet())
        {
            String velocityPropertyKey = (String)property.getKey();
            Optional<String> key = LegacyVelocityKeysMapper.getNewPatternVelocityKey(velocityPropertyKey);
            Objects.requireNonNull(properties);
            key.ifPresent(properties::remove);
        }
    }


    private static void loadProperties(Properties props, File resource)
    {
        try
        {
            if(!resource.exists())
            {
                throw new BootstrapConfigException("Can not load properties from " + resource.getAbsolutePath());
            }
            FileInputStream inputStream = new FileInputStream(resource);
            try
            {
                Charset configurationFileEncoding = getConfigurationFileEncoding().orElse(Charset.forName("ISO-8859-1"));
                InputStreamReader reader = new InputStreamReader(inputStream, configurationFileEncoding);
                try
                {
                    props.load(reader);
                    reader.close();
                }
                catch(Throwable throwable)
                {
                    try
                    {
                        reader.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                    throw throwable;
                }
                inputStream.close();
            }
            catch(Throwable throwable)
            {
                try
                {
                    inputStream.close();
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


    static Optional<Charset> getConfigurationFileEncoding()
    {
        String configFileEncoding = System.getProperty("hybris.config.file.encoding");
        if(configFileEncoding == null || configFileEncoding.trim().length() == 0)
        {
            return Optional.empty();
        }
        configFileEncoding = configFileEncoding.trim();
        try
        {
            if(Charset.isSupported(configFileEncoding))
            {
                return Optional.of(Charset.forName(configFileEncoding));
            }
            System.err.println(
                            String.format("Encoding %s is not supported for property files, using default ISO 8859-1", new Object[] {configFileEncoding}));
            return Optional.empty();
        }
        catch(IllegalCharsetNameException e)
        {
            System.err.println(
                            String.format("Illegal charset name %s provided for property file encoding, using default ISO 8859-1", new Object[] {configFileEncoding}));
            return Optional.empty();
        }
    }


    public static String unescapeProperty(String propertyValue)
    {
        int idx = propertyValue.indexOf('#');
        if(idx > 0 && propertyValue.charAt(idx - 1) == '\\')
        {
            idx = -1;
        }
        if(idx > 0)
        {
            propertyValue = propertyValue.substring(0, idx);
        }
        propertyValue = propertyValue.trim();
        propertyValue = propertyValue.replace("\\#", "#");
        propertyValue = propertyValue.replace("{tab}", "\t");
        return propertyValue;
    }
}
