package de.hybris.platform.licence.sap;

import com.sap.security.core.server.likey.LogAndTrace;
import com.sap.security.core.server.likey.Persistence;
import com.sap.security.core.server.likey.StdLogAndTrace;
import de.hybris.bootstrap.config.ConfigUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;
import org.apache.commons.io.IOUtils;

public class DefaultPersistence implements Persistence
{
    private static final String PROPS_FILE_NAME = "" + ConfigUtil.getPlatformConfig(DefaultPersistence.class).getSystemConfig()
                    .getConfigDir() + "/licence/installedSaplicenses.properties";
    private LogAndTrace logger;


    public boolean init()
    {
        try
        {
            File propsFile = getFileFromLocation(getPropsFileName());
            return (propsFile != null);
        }
        catch(IllegalStateException e)
        {
            getLogger().writeError(e.getMessage());
            return false;
        }
    }


    public boolean insertKey(String key, String value)
    {
        Objects.requireNonNull(key, "key is required");
        Objects.requireNonNull(value, "value is required");
        Properties properties = loadPropertiesFromLocation(getPropsFileName());
        if(!properties.containsKey(key))
        {
            properties.put(key, value);
            writePropertiesFile(properties);
            return true;
        }
        return false;
    }


    public boolean updateKey(String key, String value)
    {
        Objects.requireNonNull(key, "key is required");
        Objects.requireNonNull(value, "value is required");
        Properties properties = loadPropertiesFromLocation(getPropsFileName());
        if(properties.containsKey(key))
        {
            properties.put(key, value);
            writePropertiesFile(properties);
            return true;
        }
        return false;
    }


    public boolean deleteKey(String key)
    {
        Objects.requireNonNull(key, "key is required");
        Properties properties = loadPropertiesFromLocation(getPropsFileName());
        if(properties.containsKey(key))
        {
            properties.remove(key);
            writePropertiesFile(properties);
            return true;
        }
        return false;
    }


    private void writePropertiesFile(Properties properties)
    {
        FileOutputStream fos = null;
        try
        {
            File file = getPropertyFile();
            fos = new FileOutputStream(file);
            properties.store(fos, "Installed SAP licenses");
        }
        catch(IOException e)
        {
            throw new IllegalStateException(e.getMessage(), e);
        }
        finally
        {
            IOUtils.closeQuietly(fos);
        }
    }


    public String getKey(String key)
    {
        Objects.requireNonNull(key, "key is required");
        Properties properties = loadPropertiesFromLocation(getPropsFileName());
        return (String)properties.get(key);
    }


    public Properties getKeys()
    {
        try
        {
            return loadPropertiesFromLocation(getPropsFileName());
        }
        catch(IllegalStateException e)
        {
            getLogger().writeError(e.getMessage());
            return null;
        }
    }


    private Properties loadPropertiesFromLocation(String location)
    {
        Objects.requireNonNull(location, "location of properties file is required");
        File propertyFile = getFileFromLocation(location);
        Properties properties = new Properties();
        FileInputStream in = null;
        try
        {
            in = new FileInputStream(propertyFile);
            properties.load(in);
            return properties;
        }
        catch(IOException e)
        {
            throw new IllegalStateException("Cannot read property file: " + propertyFile.getAbsolutePath());
        }
        finally
        {
            IOUtils.closeQuietly(in);
        }
    }


    File getPropertyFile()
    {
        return getFileFromLocation(getPropsFileName());
    }


    private File getFileFromLocation(String location)
    {
        Objects.requireNonNull(location, "location of properties file is required");
        File file = new File(location);
        if(!file.exists())
        {
            try
            {
                file.createNewFile();
            }
            catch(IOException e)
            {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }
        return file;
    }


    private LogAndTrace getLogger()
    {
        if(this.logger == null)
        {
            this.logger = (LogAndTrace)new StdLogAndTrace();
        }
        return this.logger;
    }


    String getPropsFileName()
    {
        return PROPS_FILE_NAME;
    }
}
