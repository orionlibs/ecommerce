package de.hybris.platform.ldap.jalo.configuration.taglistener;

import de.hybris.bootstrap.xml.ObjectProcessor;
import de.hybris.bootstrap.xml.ParseAbortException;
import de.hybris.bootstrap.xml.TagListener;
import de.hybris.platform.ldap.constants.LDAPConstants;
import de.hybris.platform.ldap.jalo.configuration.valueobject.AttributeValueObject;
import de.hybris.platform.ldap.jalo.configuration.valueobject.HeaderEntry;
import java.util.Arrays;
import java.util.Collection;

public class AttributeTagListener extends AbstractConfigurationTagListener
{
    public static final String TAGNAME = LDAPConstants.CONFIG.XML.TAGS.ATTRIBUTE;


    public AttributeTagListener(AbstractConfigurationTagListener parent)
    {
        super(parent, TAGNAME);
    }


    protected Collection createSubTagListeners()
    {
        return Arrays.asList(new TagListener[] {(TagListener)new LdapTagListener(this), (TagListener)new HybrisTagListener(this), (TagListener)new ImpExTagListener(this)});
    }


    protected Object processEndElement(ObjectProcessor processor) throws ParseAbortException
    {
        String ldap = (String)getSubTagValue(LDAPConstants.CONFIG.XML.TAGS.LDAP);
        String hybris = (String)getSubTagValue(LDAPConstants.CONFIG.XML.TAGS.HYBRIS);
        String impex = (String)getSubTagValue(LDAPConstants.CONFIG.XML.TAGS.IMPEX);
        HeaderEntry headerEntry = new HeaderEntry(hybris, impex);
        return new AttributeValueObject(ldap, headerEntry);
    }
}
