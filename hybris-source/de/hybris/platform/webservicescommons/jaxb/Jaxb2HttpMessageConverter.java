package de.hybris.platform.webservicescommons.jaxb;

import com.ctc.wstx.stax.WstxInputFactory;
import com.ctc.wstx.stax.WstxOutputFactory;
import de.hybris.platform.util.Config;
import de.hybris.platform.webservicescommons.jaxb.exceptions.UnmarshalException;
import de.hybris.platform.webservicescommons.jaxb.util.WebserviceValidationEventHandler;
import de.hybris.platform.webservicescommons.jaxb.wrapper.JaxbWrapperSupport;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.xml.AbstractXmlHttpMessageConverter;
import org.springframework.util.ClassUtils;

public class Jaxb2HttpMessageConverter extends AbstractXmlHttpMessageConverter<Object>
{
    private JaxbContextFactory jaxbContextFactory;
    private JaxbWrapperSupport jaxbWrapperSupport;
    private Map<String, ?> marshallerProperties;
    private Map<String, ?> unmarshallerProperties;
    private final ConcurrentMap<Class, JAXBContext> jaxbContexts = (ConcurrentMap)new ConcurrentHashMap<>();
    private JAXBContext jaxbNullContext;
    private final XMLInputFactory xmlInputFactory;
    private final XMLOutputFactory xmlOutputFactory;


    public Jaxb2HttpMessageConverter() throws FactoryConfigurationError
    {
        this.xmlInputFactory = createXMLInputFactory();
        this.xmlOutputFactory = createXMLOutputFactory();
    }


    protected final XMLInputFactory createXMLInputFactory() throws FactoryConfigurationError
    {
        WstxInputFactory wstxInputFactory = new WstxInputFactory();
        if(!Config.getBoolean("webservicescommons.xml.external.entities.enable", false))
        {
            wstxInputFactory.setProperty("javax.xml.stream.isSupportingExternalEntities", Boolean.FALSE);
        }
        wstxInputFactory.setProperty("javax.xml.stream.supportDTD", Boolean.valueOf(Config.getBoolean("webservicescommons.xml.dtd.enable", false)));
        return (XMLInputFactory)wstxInputFactory;
    }


    protected final XMLOutputFactory createXMLOutputFactory() throws FactoryConfigurationError
    {
        return (XMLOutputFactory)new WstxOutputFactory();
    }


    protected Object readFromSource(Class<?> clazz, HttpHeaders headers, Source source)
    {
        Class<?> wrappedClazz = this.jaxbWrapperSupport.getWrapperClass(clazz);
        Unmarshaller unmarshaller = createUnmarshaller(wrappedClazz, clazz);
        JAXBElement je = unmarshal(unmarshaller, wrappedClazz, headers, source);
        return this.jaxbWrapperSupport.unwrap(je.getValue());
    }


    protected JAXBElement unmarshal(Unmarshaller unmarshaller, Class<?> clazz, HttpHeaders headers, Source source)
    {
        try
        {
            if(MediaType.APPLICATION_XML.isCompatibleWith(headers.getContentType()))
            {
                return unmarshaller.unmarshal(createXMLStreamReader(source), clazz);
            }
            return unmarshaller.unmarshal(source, clazz);
        }
        catch(JAXBException | XMLStreamException | org.eclipse.persistence.exceptions.EclipseLinkException e)
        {
            throw new UnmarshalException("Can't unmarshall input to class: " + clazz, e);
        }
    }


    public Unmarshaller createUnmarshaller(Class<?> clazz)
    {
        return createUnmarshaller(clazz, clazz);
    }


    public Unmarshaller createUnmarshaller(Class<?> wrappedClazz, Class<?> clazz)
    {
        Unmarshaller unmarshaller;
        JAXBContext jaxbContext = getJaxbContext(wrappedClazz, clazz);
        try
        {
            unmarshaller = jaxbContext.createUnmarshaller();
            unmarshaller.setEventHandler((ValidationEventHandler)new WebserviceValidationEventHandler());
            if(this.unmarshallerProperties != null)
            {
                for(Map.Entry<String, ?> entry : this.unmarshallerProperties.entrySet())
                {
                    unmarshaller.setProperty(entry.getKey(), entry.getValue());
                }
            }
        }
        catch(JAXBException e)
        {
            throw new UnmarshalException("Can't create unmarshaller for class: " + clazz, e);
        }
        return unmarshaller;
    }


    protected void writeToResult(Object o, HttpHeaders headers, Result result) throws IOException
    {
        Object input = this.jaxbWrapperSupport.wrap(o);
        Class<?> clazz = ClassUtils.getUserClass(input);
        Marshaller marshaller = createMarshaller(clazz, o.getClass());
        marshal(headers, result, input, clazz, marshaller);
    }


    protected void marshal(HttpHeaders headers, Result result, Object input, Class clazz, Marshaller marshaller)
    {
        try
        {
            if(MediaType.APPLICATION_XML.isCompatibleWith(headers.getContentType()))
            {
                marshaller.marshal(input, createXMLStreamWriter(result));
            }
            else
            {
                marshaller.marshal(input, result);
            }
        }
        catch(JAXBException | XMLStreamException e)
        {
            throw new HttpMessageNotWritableException("Can't marshall class: " + clazz, e);
        }
    }


    public Marshaller createMarshaller(Class<?> clazz)
    {
        return createMarshaller(clazz, clazz);
    }


    public Marshaller createMarshaller(Class<?> wrappedClazz, Class<?> clazz)
    {
        Marshaller marshaller;
        JAXBContext jaxbContext = getJaxbContext(wrappedClazz, clazz);
        try
        {
            marshaller = jaxbContext.createMarshaller();
            marshaller.setEventHandler((ValidationEventHandler)new WebserviceValidationEventHandler());
            if(this.marshallerProperties != null)
            {
                for(Map.Entry<String, ?> entry : this.marshallerProperties.entrySet())
                {
                    marshaller.setProperty(entry.getKey(), entry.getValue());
                }
            }
        }
        catch(JAXBException e)
        {
            throw new HttpMessageNotWritableException("Can't create marshaller for class: " + clazz, e);
        }
        return marshaller;
    }


    public final JAXBContext getJaxbContext(Class<?> wrappedClazz, Class<?> clazz)
    {
        if(wrappedClazz == null)
        {
            if(this.jaxbNullContext == null)
            {
                try
                {
                    this.jaxbNullContext = this.jaxbContextFactory.createJaxbContext(new Class[0]);
                }
                catch(JAXBException e)
                {
                    throw new HttpMessageConversionException("Could not instantiate null JAXBContext", e);
                }
            }
            return this.jaxbNullContext;
        }
        JAXBContext jaxbContext = this.jaxbContexts.get(wrappedClazz);
        if(jaxbContext == null)
        {
            try
            {
                jaxbContext = this.jaxbContextFactory.createJaxbContext(new Class[] {wrappedClazz, clazz});
                this.jaxbContexts.putIfAbsent(wrappedClazz, jaxbContext);
            }
            catch(JAXBException e)
            {
                throw new HttpMessageConversionException("Could not instantiate JAXBContext for class [" + clazz + "]: ", e);
            }
        }
        return jaxbContext;
    }


    protected boolean supports(Class<?> clazz)
    {
        if(this.jaxbContextFactory != null)
        {
            return (this.jaxbContextFactory.supports(clazz) || this.jaxbWrapperSupport.supports(clazz));
        }
        return false;
    }


    protected XMLStreamReader createXMLStreamReader(Source source) throws XMLStreamException
    {
        return this.xmlInputFactory.createXMLStreamReader(source);
    }


    protected XMLStreamWriter createXMLStreamWriter(Result result) throws XMLStreamException
    {
        return this.xmlOutputFactory.createXMLStreamWriter(result);
    }


    public JaxbContextFactory getJaxbContextFactory()
    {
        return this.jaxbContextFactory;
    }


    @Required
    public void setJaxbContextFactory(JaxbContextFactory jaxbContextFactory)
    {
        this.jaxbContextFactory = jaxbContextFactory;
    }


    public Map<String, ?> getMarshallerProperties()
    {
        return this.marshallerProperties;
    }


    public void setMarshallerProperties(Map<String, ?> marshallerProperties)
    {
        this.marshallerProperties = marshallerProperties;
    }


    public Map<String, ?> getUnmarshallerProperties()
    {
        return this.unmarshallerProperties;
    }


    public void setUnmarshallerProperties(Map<String, ?> unmarshallerProperties)
    {
        this.unmarshallerProperties = unmarshallerProperties;
    }


    public void setJaxbWrapperSupport(JaxbWrapperSupport jaxbWrapperSupport)
    {
        this.jaxbWrapperSupport = jaxbWrapperSupport;
    }


    public JaxbWrapperSupport getJaxbWrapperSupport()
    {
        return this.jaxbWrapperSupport;
    }
}
