package de.hybris.platform.ldap.jalo.configuration.taglistener;

import de.hybris.bootstrap.xml.AbstractValueObject;
import de.hybris.bootstrap.xml.ObjectProcessor;
import de.hybris.bootstrap.xml.ParseAbortException;
import de.hybris.bootstrap.xml.TagListener;
import de.hybris.platform.ldap.constants.LDAPConstants;
import de.hybris.platform.ldap.jalo.configuration.ConfigurationParser;
import de.hybris.platform.ldap.jalo.configuration.valueobject.ConfigValueObject;
import de.hybris.platform.ldap.jalo.configuration.valueobject.MappingsValueObject;
import de.hybris.platform.ldap.jalo.configuration.valueobject.TypeEntryValueObject;
import java.util.Arrays;
import java.util.Collection;
import org.apache.log4j.Logger;

public class ConfigTagListener extends AbstractConfigurationTagListener
{
    public static final String tagName = LDAPConstants.CONFIG.XML.TAGS.CONFIG;
    private static final Logger log = Logger.getLogger(ConfigTagListener.class.getName());


    public ConfigTagListener(ConfigurationParser parser)
    {
        super(parser, tagName);
    }


    public ConfigTagListener(AbstractConfigurationTagListener parent)
    {
        super(parent, tagName);
    }


    protected Collection createSubTagListeners()
    {
        return Arrays.asList(new TagListener[] {(TagListener)new TypesTagListener(this), (TagListener)new MappingsTagListener(this)});
    }


    protected Object processEndElement(ObjectProcessor processor) throws ParseAbortException
    {
        Collection<TypeEntryValueObject> typeEntries = (Collection<TypeEntryValueObject>)getSubTagValue(LDAPConstants.CONFIG.XML.TAGS.TYPES);
        Collection<MappingsValueObject> mappingEntries = (Collection<MappingsValueObject>)getSubTagValue(LDAPConstants.CONFIG.XML.TAGS.MAPPINGS);
        ConfigValueObject config = new ConfigValueObject(typeEntries, mappingEntries);
        processor.process((TagListener)this, (AbstractValueObject)config);
        return null;
    }
}
