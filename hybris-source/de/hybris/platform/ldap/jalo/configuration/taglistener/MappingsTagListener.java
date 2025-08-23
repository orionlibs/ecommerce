package de.hybris.platform.ldap.jalo.configuration.taglistener;

import de.hybris.bootstrap.xml.ObjectProcessor;
import de.hybris.bootstrap.xml.ParseAbortException;
import de.hybris.bootstrap.xml.TagListener;
import de.hybris.platform.ldap.constants.LDAPConstants;
import de.hybris.platform.ldap.jalo.configuration.valueobject.MappingsValueObject;
import java.util.Arrays;
import java.util.Collection;
import org.apache.log4j.Logger;

public class MappingsTagListener extends AbstractConfigurationTagListener
{
    public static final String tagName = LDAPConstants.CONFIG.XML.TAGS.MAPPINGS;
    private static final Logger log = Logger.getLogger(MappingsTagListener.class.getName());


    public MappingsTagListener(AbstractConfigurationTagListener parent)
    {
        super(parent, tagName);
    }


    protected Collection createSubTagListeners()
    {
        return Arrays.asList(new TagListener[] {(TagListener)new MappingTagListener(this)});
    }


    protected Object processEndElement(ObjectProcessor processor) throws ParseAbortException
    {
        Collection<MappingsValueObject> mappingEntries = getSubTagValueCollection(LDAPConstants.CONFIG.XML.TAGS.MAPPING);
        return mappingEntries;
    }
}
