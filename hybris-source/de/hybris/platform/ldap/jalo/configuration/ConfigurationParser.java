package de.hybris.platform.ldap.jalo.configuration;

import de.hybris.bootstrap.xml.AbstractValueObject;
import de.hybris.bootstrap.xml.ObjectProcessor;
import de.hybris.bootstrap.xml.ParseAbortException;
import de.hybris.bootstrap.xml.Parser;
import de.hybris.bootstrap.xml.TagListener;
import de.hybris.platform.ldap.jalo.configuration.taglistener.ConfigTagListener;
import de.hybris.platform.ldap.jalo.configuration.valueobject.ConfigValueObject;
import java.io.IOException;
import java.io.InputStream;

public class ConfigurationParser extends Parser implements ObjectProcessor
{
    private final ConfigTagListener root = new ConfigTagListener(this);
    private ConfigValueObject config;


    public ConfigurationParser()
    {
        super(null, null);
    }


    public void parse(InputStream inputStream) throws ParseAbortException
    {
        try
        {
            parse(inputStream, "utf-8", (TagListener)this.root);
        }
        finally
        {
            try
            {
                inputStream.close();
            }
            catch(IOException iOException)
            {
            }
        }
    }


    protected ObjectProcessor getObjectProcessor()
    {
        return this;
    }


    public ConfigValueObject getConfig()
    {
        return this.config;
    }


    public void process(TagListener listener, AbstractValueObject obj) throws ParseAbortException
    {
        this.config = (ConfigValueObject)obj;
    }
}
