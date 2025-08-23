package de.hybris.platform.ldap.jalo.configuration.taglistener;

import de.hybris.bootstrap.xml.ObjectProcessor;
import de.hybris.bootstrap.xml.ParseAbortException;
import de.hybris.bootstrap.xml.TagListener;
import de.hybris.platform.ldap.constants.LDAPConstants;
import de.hybris.platform.ldap.jalo.configuration.valueobject.AttributesValueObject;
import de.hybris.platform.ldap.jalo.configuration.valueobject.TypeEntryValueObject;
import java.util.Arrays;
import java.util.Collection;
import org.apache.log4j.Logger;

public class TypeTagListener extends AbstractConfigurationTagListener
{
    public static final String tagName = LDAPConstants.CONFIG.XML.TAGS.TYPE;
    private static final Logger log = Logger.getLogger(TypeTagListener.class.getName());


    public TypeTagListener(AbstractConfigurationTagListener parent)
    {
        super(parent, tagName);
    }


    protected Collection createSubTagListeners()
    {
        return Arrays.asList(new TagListener[] {(TagListener)new TypeCodeTagListener(this), (TagListener)new ObjectClassesTagListener(this), (TagListener)new BeforeTagListener(this), (TagListener)new AfterTagListener(this), (TagListener)new AttributesTagListener(this),
                        (TagListener)new DefaultImpExHeaderEntryTagListener(this)});
    }


    protected Object processEndElement(ObjectProcessor processor) throws ParseAbortException
    {
        String type = (String)getSubTagValue(LDAPConstants.CONFIG.XML.TAGS.TYPECODE);
        String before = (String)getSubTagValue(LDAPConstants.CONFIG.XML.TAGS.BEFORE);
        String after = (String)getSubTagValue(LDAPConstants.CONFIG.XML.TAGS.AFTER);
        String defaultheaderentry = (String)getSubTagValue(LDAPConstants.CONFIG.XML.TAGS.DEFAULTIMPEXHEADERENTRY);
        Collection<String> objectClasses = (Collection<String>)getSubTagValue(LDAPConstants.CONFIG.XML.TAGS.OBJECT_CLASSES);
        AttributesValueObject attributes = (AttributesValueObject)getSubTagValue(LDAPConstants.CONFIG.XML.TAGS.ATTRIBUTES);
        attributes.setTypeCode(type);
        TypeEntryValueObject entry = new TypeEntryValueObject(type);
        entry.setDefaultHeaderEntry(defaultheaderentry);
        entry.setObjectclasses(objectClasses);
        entry.setAttributes(attributes);
        if(after != null)
        {
            entry.setAfter(after);
        }
        if(before != null)
        {
            entry.setBefore(before);
        }
        return entry;
    }
}
