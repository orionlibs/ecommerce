package de.hybris.platform.ldap.jalo.configuration.taglistener;

import de.hybris.bootstrap.xml.ObjectProcessor;
import de.hybris.bootstrap.xml.ParseAbortException;
import de.hybris.bootstrap.xml.TagListener;
import de.hybris.platform.ldap.constants.LDAPConstants;
import java.util.Arrays;
import java.util.Collection;

public class MappingAttributesTagListener extends AbstractConfigurationTagListener
{
    public static final String tagName = LDAPConstants.CONFIG.XML.TAGS.ATTRIBUTES;


    public MappingAttributesTagListener(AbstractConfigurationTagListener parent)
    {
        super(parent, tagName);
    }


    protected Collection createSubTagListeners()
    {
        return Arrays.asList(new TagListener[] {(TagListener)new MappingAttributeTagListener(this)});
    }


    public String getTagName()
    {
        return tagName;
    }


    protected Object processEndElement(ObjectProcessor processor) throws ParseAbortException
    {
        Collection<String> attributes = getSubTagValueCollection(LDAPConstants.CONFIG.XML.TAGS.ATTRIBUTE);
        return attributes;
    }
}
