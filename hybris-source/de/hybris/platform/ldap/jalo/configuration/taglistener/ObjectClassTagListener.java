package de.hybris.platform.ldap.jalo.configuration.taglistener;

import de.hybris.bootstrap.xml.ObjectProcessor;
import de.hybris.bootstrap.xml.ParseAbortException;
import de.hybris.platform.ldap.constants.LDAPConstants;
import org.apache.log4j.Logger;

public class ObjectClassTagListener extends AbstractConfigurationTagListener
{
    public static final String tagName = LDAPConstants.CONFIG.XML.TAGS.OBJECT_CLASS;
    private static final Logger log = Logger.getLogger(ObjectClassTagListener.class.getName());


    public ObjectClassTagListener(AbstractConfigurationTagListener parent)
    {
        super(parent, tagName);
    }


    protected Object processEndElement(ObjectProcessor processor) throws ParseAbortException
    {
        return getCharacters();
    }
}
