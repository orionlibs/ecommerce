package de.hybris.platform.ldap.jalo.configuration.taglistener;

import de.hybris.bootstrap.xml.ObjectProcessor;
import de.hybris.bootstrap.xml.ParseAbortException;
import de.hybris.bootstrap.xml.TagListener;
import de.hybris.platform.ldap.constants.LDAPConstants;
import de.hybris.platform.ldap.jalo.configuration.valueobject.MappingValueEntry;
import java.util.Arrays;
import java.util.Collection;

public class MappingValueTagListener extends AbstractConfigurationTagListener
{
    public static final String tagName = LDAPConstants.CONFIG.XML.TAGS.VALUE;


    public MappingValueTagListener(AbstractConfigurationTagListener parent)
    {
        super(parent, tagName);
    }


    protected Collection createSubTagListeners()
    {
        return Arrays.asList(new TagListener[] {(TagListener)new LdapTagListener(this), (TagListener)new HybrisTagListener(this)});
    }


    protected Object processEndElement(ObjectProcessor processor) throws ParseAbortException
    {
        String ldap = (String)getSubTagValue(LDAPConstants.CONFIG.XML.TAGS.LDAP);
        String hybris = (String)getSubTagValue(LDAPConstants.CONFIG.XML.TAGS.HYBRIS);
        return new MappingValueEntry(ldap, hybris);
    }
}
