package de.hybris.platform.spring.ctx;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.web.context.support.XmlWebApplicationContext;

public class TenantIgnoreXmlWebApplicationContext extends XmlWebApplicationContext implements TenantNameAwareContext
{
    private static final Logger LOG = Logger.getLogger(TenantIgnoreXmlWebApplicationContext.class.getName());
    private final String ctxPath;
    private final String tenantId;


    public TenantIgnoreXmlWebApplicationContext(String tenantId, String ctxPath)
    {
        this.tenantId = tenantId;
        this.ctxPath = ctxPath;
    }


    public TenantIgnoreXmlWebApplicationContext()
    {
        this(null, null);
        LOG.warn("Please adjust your web application spring configuration by removing scope='tenant' occurences from spring configuration files");
    }


    protected void initBeanDefinitionReader(XmlBeanDefinitionReader beanDefinitionReader)
    {
        beanDefinitionReader.setDocumentReaderClass(WebScopeTenantIgnoreDocReader.class);
        super.initBeanDefinitionReader(beanDefinitionReader);
    }


    public String getDisplayName()
    {
        return super.getDisplayName() + " - " + super.getDisplayName();
    }


    public String getTenantName()
    {
        return this.tenantId;
    }
}
