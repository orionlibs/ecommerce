package de.hybris.platform.persistence.audit.internal.conditional;

import de.hybris.platform.util.security.SecureXmlUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLStreamReader;

public class ConditionalAuditConfigReader
{
    public ConditionalAuditConfig fromClasspathXml(String classpath)
    {
        try
        {
            InputStream resourceAsStream = ConditionalAuditConfigReader.class.getClassLoader().getResourceAsStream(classpath);
            try
            {
                ConditionalAuditConfigReader configReader = new ConditionalAuditConfigReader();
                ConditionalAuditConfig conditionalAuditConfig = configReader.fromXmlStream(resourceAsStream);
                if(resourceAsStream != null)
                {
                    resourceAsStream.close();
                }
                return conditionalAuditConfig;
            }
            catch(Throwable throwable)
            {
                if(resourceAsStream != null)
                {
                    try
                    {
                        resourceAsStream.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                }
                throw throwable;
            }
        }
        catch(IOException e)
        {
            throw new ConditionalAuditException(e);
        }
    }


    public ConditionalAuditConfig fromXmlStream(InputStream inputStream)
    {
        Objects.requireNonNull(inputStream, "inputStream is required");
        ConditionalAuditConfig readConfig = readConfigFromInputStream(inputStream);
        return readConfig;
    }


    private ConditionalAuditConfig readConfigFromInputStream(InputStream inputStream)
    {
        XMLStreamReader xmlStreamReader = null;
        try
        {
            xmlStreamReader = SecureXmlUtils.getSecureXmlFactory().createXMLStreamReader(inputStream);
            JAXBContext jaxbContext = JAXBContext.newInstance(new Class[] {ConditionalAuditConfig.class});
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            unmarshaller.setListener((Unmarshaller.Listener)new XMLConditionalAuditListener());
            return (ConditionalAuditConfig)unmarshaller.unmarshal(xmlStreamReader);
        }
        catch(JAXBException | javax.xml.stream.XMLStreamException e)
        {
            throw new IllegalStateException(e.getMessage(), e);
        }
        finally
        {
            SecureXmlUtils.closeReaderQuietly(xmlStreamReader);
        }
    }
}
