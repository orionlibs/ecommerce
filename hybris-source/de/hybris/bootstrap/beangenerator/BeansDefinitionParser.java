package de.hybris.bootstrap.beangenerator;

import de.hybris.bootstrap.beangenerator.definitions.xml.AbstractPojos;
import de.hybris.bootstrap.beangenerator.definitions.xml.ObjectFactory;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class BeansDefinitionParser
{
    protected static final Logger LOG = Logger.getLogger(BeansDefinitionParser.class);
    private static final JAXBContext jaxbContext = initJAXBContext();
    private final Schema beansSchema;
    private final XMLInputFactory xmlInputFactory;


    public BeansDefinitionParser(String schemaPath)
    {
        File schemaFile = new File(schemaPath);
        if(!schemaFile.exists())
        {
            throw new IllegalArgumentException("Given schema file [" + schemaFile + "] does not exist");
        }
        SchemaFactory fac = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
        try
        {
            this.beansSchema = fac.newSchema(new URL("file:" + schemaPath));
        }
        catch(SAXException | java.net.MalformedURLException e)
        {
            throw new BeanGenerationException(e.getMessage(), e);
        }
        this.xmlInputFactory = XMLInputFactory.newFactory();
        this.xmlInputFactory.setProperty("javax.xml.stream.isSupportingExternalEntities", Boolean.valueOf(false));
        this.xmlInputFactory.setProperty("javax.xml.stream.supportDTD", Boolean.valueOf(false));
    }


    public AbstractPojos parseBeansDefinition(String filename)
    {
        if(filename == null)
        {
            throw new IllegalArgumentException("File name cannot be null");
        }
        try
        {
            FileReader reader = new FileReader(filename);
            try
            {
                AbstractPojos abstractPojos = doParseBeansDefinition(new InputSource(reader));
                reader.close();
                return abstractPojos;
            }
            catch(Throwable throwable)
            {
                try
                {
                    reader.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
                throw throwable;
            }
        }
        catch(IOException e)
        {
            throw new BeanGenerationException(e.getMessage(), e);
        }
    }


    protected AbstractPojos doParseBeansDefinition(InputSource inputSource)
    {
        try
        {
            XMLStreamReader xmlStreamReader = null;
            try
            {
                xmlStreamReader = this.xmlInputFactory.createXMLStreamReader(inputSource.getCharacterStream());
                Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                unmarshaller.setSchema(this.beansSchema);
                JAXBElement<AbstractPojos> rootElement = unmarshaller.unmarshal(xmlStreamReader, AbstractPojos.class);
                return (AbstractPojos)rootElement.getValue();
            }
            finally
            {
                if(xmlStreamReader != null)
                {
                    xmlStreamReader.close();
                }
            }
        }
        catch(UnmarshalException ue)
        {
            if(ue.getLinkedException() instanceof SAXParseException)
            {
                throw new BeanGenerationException(buildExceptionMessage((SAXParseException)ue.getLinkedException()), ue
                                .getLinkedException());
            }
            throw new BeanGenerationException(ue.getMessage(), ue);
        }
        catch(JAXBException | javax.xml.stream.XMLStreamException e)
        {
            throw new BeanGenerationException(e.getMessage(), e);
        }
    }


    protected String buildExceptionMessage(SAXParseException exc)
    {
        StringBuilder message = new StringBuilder(100);
        if(StringUtils.isNotBlank(exc.getMessage()))
        {
            String messageTxt = exc.getMessage().contains(":") ? exc.getMessage().split(":")[1] : exc.getMessage();
            message.append(messageTxt);
        }
        message.append(" Syntax error occurred at line :").append(exc.getLineNumber()).append(", column :")
                        .append(exc.getColumnNumber());
        return message.toString();
    }


    private static JAXBContext initJAXBContext()
    {
        try
        {
            return JAXBContext.newInstance(ObjectFactory.class.getPackage().getName(), ObjectFactory.class.getClassLoader());
        }
        catch(JAXBException e)
        {
            throw new BeanGenerationException(e.getMessage(), e);
        }
    }
}
