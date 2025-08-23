package de.hybris.platform.mediaconversion.os.config;

import de.hybris.platform.mediaconversion.os.NoSuchConfigurationKeyException;
import de.hybris.platform.mediaconversion.os.OsConfigurationService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultOsConfigurationService implements OsConfigurationService
{
    private static final Logger LOG = Logger.getLogger(DefaultOsConfigurationService.class);
    public static final FileFilter DIRECTORY_FILTER = (FileFilter)new Object();
    private final Map<File, File> dirCache = new ConcurrentHashMap<>();
    private final Map<String, String> keyCache = new ConcurrentHashMap<>();
    private ConfigurationService configurationService;
    private String osName = System.getProperty("os.name");
    private String osArch = System.getProperty("os.arch");


    public String retrieveOsSpecificProperty(String prefix, String defaultValue)
    {
        try
        {
            String key = retrieveOsConfigurationKey(prefix);
            return getConfigurationService().getConfiguration().getString(key, defaultValue);
        }
        catch(NoSuchConfigurationKeyException e)
        {
            LOG.debug("No os specific configuration key available for prefix '" + prefix + "'.");
            return defaultValue;
        }
    }


    public String retrieveOsConfigurationKey(String prefix) throws NoSuchConfigurationKeyException
    {
        String ret = this.keyCache.get(prefix);
        if(ret == null)
        {
            ret = doRetrieveOsConfigurationKey(prefix);
            this.keyCache.put(prefix, ret);
        }
        return ret;
    }


    protected String doRetrieveOsConfigurationKey(String prefix) throws NoSuchConfigurationKeyException
    {
        ConfigKeyNameExtractor configKeyNameExtractor = new ConfigKeyNameExtractor(prefix);
        String osMatch = match(getOsName(), configKeys(prefix), (NameExtractor<String>)configKeyNameExtractor);
        if(osMatch == null)
        {
            throw new NoSuchConfigurationKeyException("No configuration key present for prefix '" + prefix + "'.", prefix);
        }
        String osNamePrefix = prefix + "." + prefix;
        String ret = match(getOsArch(), configKeys(osNamePrefix), (NameExtractor<String>)new ConfigKeyNameExtractor(osNamePrefix));
        return (ret == null) ? osMatch : ret;
    }


    private String[] configKeys(String prefix)
    {
        Iterator<String> itr = getConfigurationService().getConfiguration().getKeys(prefix);
        List<String> ret = new ArrayList<>();
        while(itr.hasNext())
        {
            ret.add(itr.next());
        }
        return ret.<String>toArray(new String[ret.size()]);
    }


    public File retrieveOsDirectory(File rootDirectory)
    {
        File ret = this.dirCache.get(rootDirectory);
        if(ret == null)
        {
            ret = doRetrieveOsDirectory(rootDirectory);
            this.dirCache.put(rootDirectory, ret);
        }
        return ret;
    }


    protected File doRetrieveOsDirectory(File root)
    {
        if(!root.isDirectory() || !root.exists())
        {
            throw new IllegalArgumentException("Specified directory '" + root + "' is not a existing directory.");
        }
        File osMatch = match(getOsName(), root
                        .listFiles(DIRECTORY_FILTER), FileNameExtractor.INSTANCE);
        if(osMatch != null)
        {
            return match(getOsArch(), osMatch
                            .listFiles(DIRECTORY_FILTER), FileNameExtractor.INSTANCE);
        }
        throw new NoOsDirectoryException("Cannot retrieve os directory");
    }


    static <T> T match(String target, T[] entries, NameExtractor<T> extractor)
    {
        String lowerTarget = target.toLowerCase();
        T ret = null;
        int leastDistance = 0;
        for(T current : entries)
        {
            int distance = StringDistance.computeDistance(lowerTarget, extractor.extract(current));
            if(ret == null || leastDistance > distance)
            {
                ret = current;
                leastDistance = distance;
            }
        }
        LOG.debug("Picked '" + ret + "' for '" + target + "'.");
        return ret;
    }


    private void clearCache()
    {
        this.keyCache.clear();
        this.dirCache.clear();
    }


    public ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }


    @Required
    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }


    public String getOsName()
    {
        return this.osName;
    }


    public void setOsName(String osName)
    {
        if(osName.equals(getOsName()))
        {
            LOG.debug("Os name '" + getOsName() + "' stays as is.");
            return;
        }
        LOG.warn("Overriding os name '" + getOsName() + "' with '" + osName + "'.");
        this.osName = osName;
        clearCache();
    }


    public String getOsArch()
    {
        return this.osArch;
    }


    public void setOsArch(String osArch)
    {
        if(osArch.equals(getOsArch()))
        {
            LOG.debug("Os arch '" + getOsArch() + "' stays as is.");
            return;
        }
        LOG.warn("Overriding os arch '" + getOsArch() + "' with '" + osArch + "'.");
        this.osArch = osArch;
        clearCache();
    }
}
