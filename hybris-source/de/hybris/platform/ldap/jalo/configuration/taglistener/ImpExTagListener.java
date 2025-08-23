package de.hybris.platform.ldap.jalo.configuration.taglistener;

import de.hybris.bootstrap.xml.ObjectProcessor;
import de.hybris.bootstrap.xml.ParseAbortException;
import de.hybris.platform.ldap.constants.LDAPConstants;

public class ImpExTagListener extends AbstractConfigurationTagListener
{
    public static final String TAGNAME = LDAPConstants.CONFIG.XML.TAGS.IMPEX;


    public ImpExTagListener(AbstractConfigurationTagListener parent)
    {
        super(parent, TAGNAME);
    }


    public ImpExTagListener(AbstractConfigurationTagListener parent, String tagName)
    {
        super(parent, tagName);
    }


    protected Object processEndElement(ObjectProcessor processor) throws ParseAbortException
    {
        return getCharacters();
    }
}
