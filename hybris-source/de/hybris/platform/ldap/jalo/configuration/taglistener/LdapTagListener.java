package de.hybris.platform.ldap.jalo.configuration.taglistener;

import de.hybris.bootstrap.xml.ObjectProcessor;
import de.hybris.bootstrap.xml.ParseAbortException;
import de.hybris.platform.ldap.constants.LDAPConstants;

public class LdapTagListener extends AbstractConfigurationTagListener
{
    public static final String tagName = LDAPConstants.CONFIG.XML.TAGS.LDAP;


    public LdapTagListener(AbstractConfigurationTagListener parent)
    {
        super(parent, tagName);
    }


    protected Object processEndElement(ObjectProcessor processor) throws ParseAbortException
    {
        return getCharacters();
    }
}
