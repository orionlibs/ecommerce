package de.hybris.platform.ldap.jalo.configuration.taglistener;

import de.hybris.bootstrap.xml.ObjectProcessor;
import de.hybris.bootstrap.xml.ParseAbortException;
import de.hybris.bootstrap.xml.TagListener;
import de.hybris.platform.ldap.constants.LDAPConstants;
import de.hybris.platform.ldap.jalo.configuration.valueobject.AttributeValueObject;
import de.hybris.platform.ldap.jalo.configuration.valueobject.AttributesValueObject;
import java.util.Arrays;
import java.util.Collection;

public class AttributesTagListener extends AbstractConfigurationTagListener
{
    public AttributesTagListener(AbstractConfigurationTagListener parent)
    {
        super(parent, LDAPConstants.CONFIG.XML.TAGS.ATTRIBUTES);
    }


    protected Collection<TagListener> createSubTagListeners()
    {
        return Arrays.asList(new TagListener[] {(TagListener)new AttributeTagListener(this)});
    }


    public String getTagName()
    {
        return LDAPConstants.CONFIG.XML.TAGS.ATTRIBUTES;
    }


    protected Object processEndElement(ObjectProcessor processor) throws ParseAbortException
    {
        Collection<AttributeValueObject> attributes = getSubTagValueCollection(LDAPConstants.CONFIG.XML.TAGS.ATTRIBUTE);
        AttributesValueObject ret = new AttributesValueObject(attributes);
        return ret;
    }
}
