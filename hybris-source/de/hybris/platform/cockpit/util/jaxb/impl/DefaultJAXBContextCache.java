package de.hybris.platform.cockpit.util.jaxb.impl;

import de.hybris.platform.cockpit.util.jaxb.JAXBContextCache;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.xml.sax.SAXException;

public class DefaultJAXBContextCache implements JAXBContextCache
{
    private final ConcurrentMap<Class, JAXBContext> JAXB_CONTEXT_CACHE = (ConcurrentMap)new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Schema> SCHEMA_CACHE = new ConcurrentHashMap<>();


    public JAXBContext resolveContext(Class jaxbClass) throws JAXBException
    {
        JAXBContext jaxbContext = this.JAXB_CONTEXT_CACHE.get(jaxbClass);
        if(jaxbContext == null)
        {
            synchronized(jaxbClass.getPackage().getName().intern())
            {
                jaxbContext = this.JAXB_CONTEXT_CACHE.get(jaxbClass);
                if(jaxbContext == null)
                {
                    jaxbContext = JAXBContext.newInstance(jaxbClass.getPackage().getName());
                    this.JAXB_CONTEXT_CACHE.putIfAbsent(jaxbClass, jaxbContext);
                }
            }
        }
        return jaxbContext;
    }


    public Schema resolveSchema(URL url) throws SAXException, IOException
    {
        String urlExternalForm = url.toExternalForm();
        Schema schema = this.SCHEMA_CACHE.get(urlExternalForm);
        if(schema == null)
        {
            synchronized(urlExternalForm.intern())
            {
                schema = this.SCHEMA_CACHE.get(urlExternalForm);
                if(schema == null)
                {
                    SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema", "com.sun.org.apache.xerces.internal.jaxp.validation.XMLSchemaFactory", null);
                    schemaFactory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
                    schema = schemaFactory.newSchema(url);
                    this.SCHEMA_CACHE.putIfAbsent(urlExternalForm, schema);
                }
            }
        }
        return schema;
    }
}
