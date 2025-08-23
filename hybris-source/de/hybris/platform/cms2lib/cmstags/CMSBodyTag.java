package de.hybris.platform.cms2lib.cmstags;

import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.preview.CMSPreviewTicketModel;
import de.hybris.platform.cms2.model.preview.PreviewDataModel;
import de.hybris.platform.cms2.servicelayer.services.CMSPreviewService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class CMSBodyTag extends BodyTagSupport
{
    private static final Logger LOG = Logger.getLogger(CMSBodyTag.class);


    protected ServletContext getServletContext()
    {
        return this.pageContext.getServletContext();
    }


    protected boolean isLiveEdit()
    {
        return (isPreviewDataModelValid() && Boolean.TRUE.equals(getPreviewData(this.pageContext.getRequest()).getLiveEdit()));
    }


    protected boolean isPreviewDataModelValid()
    {
        return (getPreviewData(this.pageContext.getRequest()) != null);
    }


    protected String getPreviewTicketId(ServletRequest httpRequest)
    {
        String id = httpRequest.getParameter("cmsTicketId");
        WebApplicationContext appContext = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        SessionService sessionService = (SessionService)appContext.getBean("sessionService");
        if(StringUtils.isBlank(id))
        {
            id = (String)sessionService.getAttribute("cmsTicketId");
        }
        return id;
    }


    protected PreviewDataModel getPreviewData(ServletRequest httpRequest)
    {
        return getPreviewData(getPreviewTicketId(httpRequest), httpRequest);
    }


    protected PreviewDataModel getPreviewData(String ticketId, ServletRequest httpRequest)
    {
        PreviewDataModel ret = null;
        WebApplicationContext appContext = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        CMSPreviewService cmsPreviewService = (CMSPreviewService)appContext.getBean("cmsPreviewService");
        CMSPreviewTicketModel previewTicket = cmsPreviewService.getPreviewTicket(ticketId);
        if(previewTicket != null)
        {
            ret = previewTicket.getPreviewData();
        }
        return ret;
    }


    public int doStartTag() throws JspException
    {
        String extraScript = "<script type=\"text/javascript\">\n var currentUserId;\n var currentJaloSessionId;\n function getCurrentPageLocation(url, pagePk, userUid, jaloSessionId){\n   if (url != \"\") {\n parent.postMessage({eventName:notifyIframeAboutUrlChange, data: [url, pagePk, userUid, jaloSessionId]},'*');\n \n   }\n}\n</script>\n";
        AbstractPageModel currentPage = (AbstractPageModel)this.pageContext.getRequest().getAttribute("currentPage");
        String currentPagePk = null;
        if(currentPage != null)
        {
            currentPagePk = currentPage.getPk().toString();
        }
        StringBuilder bodyTagBuilder = new StringBuilder();
        bodyTagBuilder.append("body");
        bodyTagBuilder.append(isPreviewDataModelValid() ? (" onload=\"getCurrentPageLocation(window.location.href, '" +
                        currentPagePk + "' , currentUserId , currentJaloSessionId)\"") : "");
        bodyTagBuilder.append(isLiveEdit() ? " onclick=\"return getCMSElement(event)\"" : "");
        try
        {
            this.pageContext.getOut().print("<" + bodyTagBuilder.toString() + ">\n");
            this.pageContext.getOut().print(isPreviewDataModelValid()
                            ? "<script type=\"text/javascript\">\n var currentUserId;\n var currentJaloSessionId;\n function getCurrentPageLocation(url, pagePk, userUid, jaloSessionId){\n   if (url != \"\") {\n parent.postMessage({eventName:notifyIframeAboutUrlChange, data: [url, pagePk, userUid, jaloSessionId]},'*');\n \n   }\n}\n</script>\n"
                            : "");
        }
        catch(IOException e)
        {
            LOG.warn("Error processing tag", e);
        }
        return 1;
    }


    public int doAfterBody() throws JspException
    {
        try
        {
            StringBuilder bodyTagBuilder = new StringBuilder();
            bodyTagBuilder.append(isPreviewDataModelValid() ? ("<script>currentUserId='" + getCurrentUserUid() + "';currentJaloSessionId='" +
                            getCurrentJaloSessionId() + "';</script>") : "");
            bodyTagBuilder.append("</body>");
            this.pageContext.getOut().print(bodyTagBuilder.toString());
        }
        catch(IOException e)
        {
            LOG.warn("Error processing tag", e);
        }
        return 0;
    }


    protected String getCurrentUserUid()
    {
        String ret = "";
        WebApplicationContext appContext = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        UserService userService = (UserService)appContext.getBean("userService");
        if(userService != null)
        {
            ret = userService.getCurrentUser().getUid();
        }
        return ret;
    }


    protected String getCurrentJaloSessionId()
    {
        String ret = "";
        WebApplicationContext appContext = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        SessionService sessionService = (SessionService)appContext.getBean("sessionService");
        if(sessionService != null)
        {
            ret = sessionService.getCurrentSession().getSessionId();
        }
        return ret;
    }
}
