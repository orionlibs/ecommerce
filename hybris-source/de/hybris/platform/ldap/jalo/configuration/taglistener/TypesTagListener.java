package de.hybris.platform.ldap.jalo.configuration.taglistener;

import de.hybris.bootstrap.xml.ObjectProcessor;
import de.hybris.bootstrap.xml.ParseAbortException;
import de.hybris.bootstrap.xml.TagListener;
import de.hybris.platform.ldap.constants.LDAPConstants;
import de.hybris.platform.ldap.jalo.configuration.valueobject.TypeEntryValueObject;
import java.util.Arrays;
import java.util.Collection;
import org.apache.log4j.Logger;

public class TypesTagListener extends AbstractConfigurationTagListener
{
    public static final String tagName = LDAPConstants.CONFIG.XML.TAGS.TYPES;
    private static final Logger log = Logger.getLogger(TypesTagListener.class.getName());


    public TypesTagListener(AbstractConfigurationTagListener parent)
    {
        super(parent, tagName);
    }


    protected Collection createSubTagListeners()
    {
        return Arrays.asList(new TagListener[] {(TagListener)new TypeTagListener(this)});
    }


    protected Object processEndElement(ObjectProcessor processor) throws ParseAbortException
    {
        Collection<TypeEntryValueObject> entries = getSubTagValueCollection(LDAPConstants.CONFIG.XML.TAGS.TYPE);
        return entries;
    }
}
