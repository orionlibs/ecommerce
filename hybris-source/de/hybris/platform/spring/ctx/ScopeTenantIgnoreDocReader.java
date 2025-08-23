package de.hybris.platform.spring.ctx;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.beans.factory.xml.DefaultBeanDefinitionDocumentReader;
import org.springframework.beans.factory.xml.XmlReaderContext;
import org.w3c.dom.Element;

public class ScopeTenantIgnoreDocReader extends DefaultBeanDefinitionDocumentReader
{
    private static final Logger LOG = Logger.getLogger(ScopeTenantIgnoreDocReader.class.getName());
    private static final String ignoredScopeName = "tenant";
    private final boolean lazyInitOverride = readLazyInitOverrideConfiguration();


    protected boolean readLazyInitOverrideConfiguration()
    {
        return false;
    }


    protected BeanDefinitionParserDelegate createDelegate(XmlReaderContext readerContext, Element root, BeanDefinitionParserDelegate parentDelegate)
    {
        Object object = new Object(this, readerContext);
        object.initDefaults(root, parentDelegate);
        return (BeanDefinitionParserDelegate)object;
    }
}
