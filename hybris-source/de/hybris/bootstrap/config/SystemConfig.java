package de.hybris.bootstrap.config;

import de.hybris.bootstrap.loader.rule.internal.IgnoreClassLoaderRuleParam;
import de.hybris.bootstrap.loader.rule.internal.RuleParamsConfigurator;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

public final class SystemConfig
{
    public static final String PLATFORM_HOME = "platformhome";
    public static final String PROPERTY_BIN_DIR = "HYBRIS_BIN_DIR";
    public static final String PROPERTY_CONFIG_DIR = "HYBRIS_CONFIG_DIR";
    public static final String PROPERTY_BOOTSTRAP_BIN_DIR = "HYBRIS_BOOTSTRAP_BIN_DIR";
    public static final String PROPERTY_DATA_DIR = "HYBRIS_DATA_DIR";
    public static final String PROPERTY_LOG_DIR = "HYBRIS_LOG_DIR";
    public static final String PROPERTY_TEMP_DIR = "HYBRIS_TEMP_DIR";
    private static volatile SystemConfig singleton;
    private File platformHome;
    private final Map<String, File> props;
    private List<IgnoreClassLoaderRuleParam> ignoreRuleList;


    public static SystemConfig getInstanceByProps(Hashtable properties)
    {
        if(singleton == null)
        {
            synchronized(SystemConfig.class)
            {
                singleton = loadSystemConfig(properties);
            }
        }
        return singleton;
    }


    private static SystemConfig loadSystemConfig(Hashtable properties)
    {
        SystemConfig config = new SystemConfig();
        String platformHome = (String)System.getProperties().get("platformhome");
        if(platformHome == null)
        {
            platformHome = (String)properties.get("platformhome");
        }
        config.platformHome = getFile(platformHome, "platformhome", false);
        for(Map.Entry<String, File> entry : config.props.entrySet())
        {
            String dir = (String)System.getProperties().get(entry.getKey());
            if(dir == null)
            {
                dir = (String)properties.get(entry.getKey());
            }
            try
            {
                File dirFile = getFile(replaceMacros(dir, platformHome), entry.getKey(), true);
                entry.setValue(dirFile);
            }
            catch(Exception e)
            {
                throw new IllegalArgumentException("Exception while setting value for property with key '" + (String)entry.getKey() + "'", e);
            }
        }
        config.ignoreRuleList = RuleParamsConfigurator.loadIgnoreRuleClassLoaderProperties(properties);
        return config;
    }


    private static String replaceMacros(String source, String replacement)
    {
        if(replacement == null)
        {
            return source;
        }
        return source.replace("${platformhome}", replacement);
    }


    private SystemConfig()
    {
        this.platformHome = null;
        this.props = new HashMap<>();
        this.props.put("HYBRIS_BIN_DIR", null);
        this.props.put("HYBRIS_CONFIG_DIR", null);
        this.props.put("HYBRIS_DATA_DIR", null);
        this.props.put("HYBRIS_LOG_DIR", null);
        this.props.put("HYBRIS_TEMP_DIR", null);
        this.props.put("HYBRIS_BOOTSTRAP_BIN_DIR", null);
    }


    private static File getFile(String path, String name, boolean failOnError)
    {
        File file;
        if(path == null)
        {
            if(failOnError)
            {
                throw new IllegalArgumentException("Property " + name + " is not set");
            }
            return null;
        }
        String pathWithoutQuotes = path;
        if(pathWithoutQuotes.length() > 2 && pathWithoutQuotes.charAt(0) == '"' && pathWithoutQuotes
                        .charAt(pathWithoutQuotes.length() - 1) == '"')
        {
            pathWithoutQuotes = pathWithoutQuotes.substring(1, pathWithoutQuotes.length() - 1);
        }
        try
        {
            file = (new File(pathWithoutQuotes)).getCanonicalFile();
        }
        catch(IOException e)
        {
            throw new IllegalArgumentException("Error while resolving path " + pathWithoutQuotes + " for " + name, e);
        }
        if(!file.exists())
        {
            if(file.mkdirs())
            {
                System.out.println("Created " + name + " folder at " + file.getAbsolutePath());
            }
            else
            {
                throw new IllegalArgumentException(name + " path " + name + " is not existent and can not be created");
            }
        }
        if(!file.isDirectory())
        {
            throw new IllegalArgumentException(name + " path " + name + " is not a directory");
        }
        return file;
    }


    public File getPlatformHome()
    {
        return this.platformHome;
    }


    public File getBootstrapBinDir()
    {
        return this.props.get("HYBRIS_BOOTSTRAP_BIN_DIR");
    }


    public File getConfigDir()
    {
        return this.props.get("HYBRIS_CONFIG_DIR");
    }


    public File getBinDir()
    {
        return this.props.get("HYBRIS_BIN_DIR");
    }


    public File getDataDir()
    {
        return this.props.get("HYBRIS_DATA_DIR");
    }


    public File getLogDir()
    {
        return this.props.get("HYBRIS_LOG_DIR");
    }


    public File getTempDir()
    {
        return this.props.get("HYBRIS_TEMP_DIR");
    }


    public String replaceProperties(String path)
    {
        if(path == null)
        {
            return null;
        }
        String result = path;
        result = result.replace("${platformhome}", Matcher.quoteReplacement(this.platformHome.getAbsolutePath()));
        for(Map.Entry<String, File> entry : this.props.entrySet())
        {
            result = result.replaceAll("\\$\\{" + (String)entry.getKey() + "\\}",
                            Matcher.quoteReplacement(((File)entry.getValue()).getAbsolutePath()));
        }
        return result;
    }


    public String getDir(String property)
    {
        if(property == null)
        {
            return null;
        }
        try
        {
            if(property.equals("platformhome"))
            {
                return getPlatformHome().getCanonicalPath();
            }
            File result = this.props.get(property);
            if(result == null)
            {
                return null;
            }
            return result.getCanonicalPath();
        }
        catch(IOException e)
        {
            System.err.println("IOException during resolving getDir() for property \"" + property + "\"");
            e.printStackTrace();
            return null;
        }
    }


    public List<IgnoreClassLoaderRuleParam> getIgnoreClassLoaderRuleParamList()
    {
        return this.ignoreRuleList;
    }
}
