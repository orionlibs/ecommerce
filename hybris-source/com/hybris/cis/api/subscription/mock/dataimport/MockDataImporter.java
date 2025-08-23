package com.hybris.cis.api.subscription.mock.dataimport;

import java.io.IOException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.springframework.core.io.ClassPathResource;

public final class MockDataImporter
{
    public static <T> T importMockData(Class<T> clazz, String path) throws JAXBException, IOException
    {
        JAXBContext jc = JAXBContext.newInstance(new Class[] {clazz});
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        return (T)unmarshaller.unmarshal((new ClassPathResource(path)).getInputStream());
    }
}
