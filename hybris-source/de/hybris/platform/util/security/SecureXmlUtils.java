package de.hybris.platform.util.security;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecureXmlUtils
{
    private static final Logger LOG = LoggerFactory.getLogger(SecureXmlUtils.class);
    private static final XMLInputFactory factory = XMLInputFactory.newFactory();

    static
    {
        factory.setProperty("javax.xml.stream.isSupportingExternalEntities", Boolean.valueOf(false));
        factory.setProperty("javax.xml.stream.supportDTD", Boolean.valueOf(false));
    }

    public static XMLInputFactory getSecureXmlFactory()
    {
        return factory;
    }


    public static void closeReaderQuietly(XMLStreamReader xmlStreamReader)
    {
        if(xmlStreamReader != null)
        {
            try
            {
                xmlStreamReader.close();
            }
            catch(XMLStreamException e)
            {
                LOG.error("Failed to close XmlStreamReader.", e);
            }
        }
    }
}
