package de.hybris.platform.ldap.jalo.configuration.taglistener;

import de.hybris.platform.ldap.constants.LDAPConstants;

public class AfterTagListener extends ImpExTagListener
{
    public static final String TAGNAME = LDAPConstants.CONFIG.XML.TAGS.AFTER;


    public AfterTagListener(AbstractConfigurationTagListener parent)
    {
        super(parent, TAGNAME);
    }
}
