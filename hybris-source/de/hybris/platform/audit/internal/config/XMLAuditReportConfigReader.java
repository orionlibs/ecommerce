package de.hybris.platform.audit.internal.config;

import de.hybris.platform.audit.internal.config.validation.AuditReportConfigValidator;
import de.hybris.platform.core.model.audit.AuditReportConfigModel;
import de.hybris.platform.util.security.SecureXmlUtils;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Objects;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLStreamReader;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.xml.sax.SAXException;

public class XMLAuditReportConfigReader
{
    private static final Logger LOG = LoggerFactory.getLogger(XMLAuditReportConfigReader.class);
    private AuditReportConfigValidator auditReportConfigValidator;
    private volatile Schema schema;


    public AuditReportConfig fromXml(AuditReportConfigModel config)
    {
        Objects.requireNonNull(config, "config is required");
        AuditReportConfig readedConfig = fromXml(new ByteArrayInputStream(config.getContent().getBytes()));
        this.auditReportConfigValidator.markInvalidConfigurationFragments(readedConfig);
        return readedConfig;
    }


    public AuditReportConfig fromXml(InputStream inputStream)
    {
        Objects.requireNonNull(inputStream, "inputStream is required");
        AuditReportConfig readedConfig = readConfigFromInputStream(inputStream);
        this.auditReportConfigValidator.markInvalidConfigurationFragments(readedConfig);
        return readedConfig;
    }


    public AuditReportConfig fromXmlWithoutValidation(InputStream inputStream)
    {
        Objects.requireNonNull(inputStream, "inputStream is required");
        return readConfigFromInputStream(inputStream);
    }


    private AuditReportConfig readConfigFromInputStream(InputStream inputStream)
    {
        XMLStreamReader xmlStreamReader = null;
        try
        {
            xmlStreamReader = SecureXmlUtils.getSecureXmlFactory().createXMLStreamReader(inputStream);
            JAXBContext jaxbContext = JAXBContext.newInstance(new Class[] {AuditReportConfig.class});
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            unmarshaller.setSchema(getSchema());
            unmarshaller.setListener((Unmarshaller.Listener)new XMLAuditReportListener());
            return (AuditReportConfig)unmarshaller.unmarshal(xmlStreamReader);
        }
        catch(SAXException e)
        {
            LOG.error("Failed to load schema: " + e.getMessage(), e);
            throw new IllegalStateException("Internal error.", e);
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


    private Schema getSchema() throws SAXException
    {
        if(this.schema == null)
        {
            synchronized(this)
            {
                if(this.schema == null)
                {
                    this.schema = loadSchema();
                }
            }
        }
        return this.schema;
    }


    private Schema loadSchema() throws SAXException
    {
        SchemaFactory fac = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema", "com.sun.org.apache.xerces.internal.jaxp.validation.XMLSchemaFactory", null);
        fac.setProperty("http://javax.xml.XMLConstants/property/accessExternalSchema", "");
        fac.setProperty("http://javax.xml.XMLConstants/property/accessExternalDTD", "");
        return fac.newSchema(getClass().getResource("/audit/audit.xsd"));
    }


    @Required
    public void setAuditReportConfigValidator(AuditReportConfigValidator auditReportConfigValidator)
    {
        this.auditReportConfigValidator = auditReportConfigValidator;
    }
}
