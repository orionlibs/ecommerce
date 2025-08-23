package de.hybris.e2e.transport.cts.impl;

import de.hybris.e2e.transport.cts.ConfigurationHolder;
import de.hybris.e2e.transport.utils.ConsolePrinter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CtsConfigurationHolder implements ConfigurationHolder
{
    private static final Logger LOG = Logger.getLogger(CtsConfigurationHolder.class.getName());
    private static final int DEFAULTPACKAGESIZE = 40;
    private String applicationType;
    private String user;
    private String password;
    private String sid;
    private String url;
    private String wsName;
    private String wsBindingName;
    private int packageSize;
    final Properties prop = new Properties();


    public CtsConfigurationHolder(String path)
    {
        String propFileName = path + path + "project.properties";
        try
        {
            InputStream inputStream = new FileInputStream(propFileName);
            try
            {
                this.prop.load(inputStream);
                readValues(this.prop);
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
        catch(FileNotFoundException fileNotFoundException)
        {
            ConsolePrinter.println(fileNotFoundException.getMessage());
            LOG.log(Level.SEVERE, fileNotFoundException.getMessage(), fileNotFoundException);
        }
        catch(IOException ioException)
        {
            ConsolePrinter.println(ioException.getMessage());
            LOG.log(Level.SEVERE, ioException.getMessage(), ioException);
        }
    }


    private void readValues(Properties prop)
    {
        this.applicationType = prop.getProperty("cts.application.type");
        this.sid = prop.getProperty("cts.application.sid");
        this.user = prop.getProperty("cts.application.user");
        this.password = prop.getProperty("cts.application.password");
        this.url = prop.getProperty("cts.application.ws_url");
        this.wsName = prop.getProperty("cts.application.ws_name");
        this.wsBindingName = prop.getProperty("cts.application.ws_binding");
        try
        {
            this.packageSize = Integer.parseInt(prop.getProperty("cts.application.package_size", "1"));
        }
        catch(Exception nfe)
        {
            LOG.log(Level.INFO, "Since the parameter \"cts.application.package_size\" has not been provided it will set by default to 40", nfe);
            this.packageSize = 40;
        }
        finally
        {
            if(this.packageSize < 1)
            {
                this.packageSize = 40;
            }
        }
    }


    public String getApplicationType()
    {
        return this.applicationType;
    }


    public String getSid()
    {
        return this.sid;
    }


    public String getUser()
    {
        return this.user;
    }


    public String getPassword()
    {
        return this.password;
    }


    public String getUrl()
    {
        return this.url;
    }


    public int getPackageSize()
    {
        return this.packageSize;
    }


    public String getWsName()
    {
        return this.wsName;
    }


    public String getWsBindingName()
    {
        return this.wsBindingName;
    }
}
