package de.hybris.platform.cockpit.util.jaxb;

import java.io.IOException;
import java.net.URL;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.validation.Schema;
import org.xml.sax.SAXException;

public interface JAXBContextCache
{
    JAXBContext resolveContext(Class paramClass) throws JAXBException;


    Schema resolveSchema(URL paramURL) throws SAXException, IOException;
}
