package de.hybris.platform.ldap.jalo.configuration.taglistener;

import de.hybris.bootstrap.xml.ObjectProcessor;
import de.hybris.bootstrap.xml.ParseAbortException;
import de.hybris.bootstrap.xml.TagListener;
import de.hybris.platform.ldap.constants.LDAPConstants;
import java.util.Arrays;
import java.util.Collection;
import org.apache.log4j.Logger;

public class ObjectClassesTagListener extends AbstractConfigurationTagListener
{
    public static final String tagName = LDAPConstants.CONFIG.XML.TAGS.OBJECT_CLASSES;
    private static final Logger log = Logger.getLogger(ObjectClassesTagListener.class.getName());


    public ObjectClassesTagListener(AbstractConfigurationTagListener parent)
    {
        super(parent, tagName);
    }


    protected Collection createSubTagListeners()
    {
        return Arrays.asList(new TagListener[] {(TagListener)new ObjectClassTagListener(this)});
    }


    protected Object processEndElement(ObjectProcessor processor) throws ParseAbortException
    {
        return getSubTagValueCollection(LDAPConstants.CONFIG.XML.TAGS.OBJECT_CLASS);
    }
}
