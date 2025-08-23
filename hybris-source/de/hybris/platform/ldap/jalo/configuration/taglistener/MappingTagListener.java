package de.hybris.platform.ldap.jalo.configuration.taglistener;

import de.hybris.bootstrap.xml.ObjectProcessor;
import de.hybris.bootstrap.xml.ParseAbortException;
import de.hybris.bootstrap.xml.TagListener;
import de.hybris.platform.ldap.constants.LDAPConstants;
import de.hybris.platform.ldap.jalo.configuration.valueobject.MappingValueEntry;
import de.hybris.platform.ldap.jalo.configuration.valueobject.MappingsValueObject;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

public class MappingTagListener extends AbstractConfigurationTagListener
{
    public static final String tagName = LDAPConstants.CONFIG.XML.TAGS.MAPPING;
    private static final Logger log = Logger.getLogger(MappingTagListener.class.getName());


    public MappingTagListener(AbstractConfigurationTagListener parent)
    {
        super(parent, tagName);
    }


    protected Collection createSubTagListeners()
    {
        return Arrays.asList(new TagListener[] {(TagListener)new MappingValuesTagListener(this), (TagListener)new MappingAttributesTagListener(this)});
    }


    protected Object processEndElement(ObjectProcessor processor) throws ParseAbortException
    {
        log.info("" + this + ":processEndElement...");
        Collection<MappingValueEntry> values = (Collection<MappingValueEntry>)getSubTagValue(LDAPConstants.CONFIG.XML.TAGS.VALUES);
        Collection<String> attributes = (Collection<String>)getSubTagValue(LDAPConstants.CONFIG.XML.TAGS.ATTRIBUTES);
        Map<String, String> valuesMap = new HashMap<>();
        for(MappingValueEntry entry : values)
        {
            if(log.isDebugEnabled())
            {
                log.debug("mapping: " + entry.getLDAP() + " -> " + entry.getHybris());
            }
            valuesMap.put(entry.getLDAP(), entry.getHybris());
        }
        return new MappingsValueObject(attributes, valuesMap);
    }
}
