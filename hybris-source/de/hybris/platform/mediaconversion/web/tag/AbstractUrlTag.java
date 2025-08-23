package de.hybris.platform.mediaconversion.web.tag;

import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.mediaconversion.web.facades.OnDemandConversionFacade;
import javax.servlet.ServletContext;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public abstract class AbstractUrlTag extends SimpleTagSupport
{
    private static final Logger LOG = Logger.getLogger(AbstractUrlTag.class);
    private MediaContainerModel container;
    private String format;


    protected OnDemandConversionFacade retrieveImageFacade()
    {
        if(getJspContext() instanceof PageContext)
        {
            return retrieveImageFacade(((PageContext)getJspContext()).getServletContext());
        }
        throw new IllegalStateException("Failed to access servlet context.");
    }


    protected OnDemandConversionFacade retrieveImageFacade(ServletContext servletCtx)
    {
        WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(servletCtx);
        if(ctx != null)
        {
            return (OnDemandConversionFacade)ctx.getBean("onDemandConversionFacade", OnDemandConversionFacade.class);
        }
        LOG.warn("Couldn't find bean + onDemandConversionFacade. ApplicationContext already/still down?");
        throw new NoSuchBeanDefinitionException("Couldn't find bean onDemandConversionFacade");
    }


    protected String retrieveURL()
    {
        String ret = retrieveImageFacade().retrieveURL(getContainer(), getFormat());
        LOG.debug("Image url is '" + ret + "'.");
        return ret;
    }


    public MediaContainerModel getContainer()
    {
        return this.container;
    }


    public void setContainer(MediaContainerModel container)
    {
        this.container = container;
    }


    public String getFormat()
    {
        return this.format;
    }


    public void setFormat(String format)
    {
        this.format = format;
    }
}
