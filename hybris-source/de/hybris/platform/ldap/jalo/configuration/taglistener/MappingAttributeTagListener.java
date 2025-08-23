package de.hybris.platform.ldap.jalo.configuration.taglistener;

import de.hybris.bootstrap.xml.ObjectProcessor;
import de.hybris.bootstrap.xml.ParseAbortException;
import de.hybris.platform.ldap.constants.LDAPConstants;

public class MappingAttributeTagListener extends AbstractConfigurationTagListener
{
    public static final String tagName = LDAPConstants.CONFIG.XML.TAGS.ATTRIBUTE;


    public MappingAttributeTagListener(AbstractConfigurationTagListener parent)
    {
        super(parent, tagName);
    }


    protected Object processEndElement(ObjectProcessor processor) throws ParseAbortException
    {
        return getCharacters();
    }
}
