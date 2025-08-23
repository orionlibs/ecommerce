package de.hybris.platform.ldap.jalo.configuration.taglistener;

import de.hybris.platform.ldap.constants.LDAPConstants;

public class BeforeTagListener extends ImpExTagListener
{
    public static final String TAGNAME = LDAPConstants.CONFIG.XML.TAGS.BEFORE;


    public BeforeTagListener(AbstractConfigurationTagListener parent)
    {
        super(parent, TAGNAME);
    }
}
