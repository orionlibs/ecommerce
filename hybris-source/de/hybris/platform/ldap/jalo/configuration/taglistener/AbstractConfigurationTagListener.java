package de.hybris.platform.ldap.jalo.configuration.taglistener;

import de.hybris.bootstrap.xml.DefaultTagListener;
import de.hybris.bootstrap.xml.ObjectProcessor;
import de.hybris.bootstrap.xml.ParseAbortException;
import de.hybris.platform.ldap.jalo.configuration.ConfigurationParser;

public abstract class AbstractConfigurationTagListener extends DefaultTagListener
{
    private final ConfigurationParser parser;
    private final String tagName;


    public AbstractConfigurationTagListener(ConfigurationParser parser, String tagName)
    {
        super(null);
        this.tagName = tagName;
        this.parser = parser;
    }


    public AbstractConfigurationTagListener(AbstractConfigurationTagListener parent, String tagName)
    {
        super(parent);
        this.tagName = tagName;
        this.parser = null;
    }


    protected Object processStartElement(ObjectProcessor processor) throws ParseAbortException
    {
        return null;
    }


    public ConfigurationParser getParser()
    {
        return this.parser;
    }


    public String getTagName()
    {
        return this.tagName;
    }
}
