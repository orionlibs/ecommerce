package de.hybris.platform.webservicescommons.jaxb;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

public interface JaxbContextFactory
{
    JAXBContext createJaxbContext(Class... paramVarArgs) throws JAXBException;


    boolean supports(Class<?> paramClass);
}
