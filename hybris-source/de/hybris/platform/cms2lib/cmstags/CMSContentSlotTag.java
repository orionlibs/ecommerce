package de.hybris.platform.cms2lib.cmstags;

import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.preview.CMSPreviewTicketModel;
import de.hybris.platform.cms2.servicelayer.services.CMSContentSlotService;
import de.hybris.platform.cms2.servicelayer.services.CMSPreviewService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class CMSContentSlotTag extends BodyTagSupport
{
    private static final long serialVersionUID = -2741851653451895432L;
    private static final String NPTFW_ID = "No preview ticket found with cmsTicketId.";
    private static final Logger LOG = Logger.getLogger(CMSContentSlotTag.class.getName());
    private ContentSlotModel contentSlot;
    private List<SimpleCMSComponentModel> components;
    private SimpleCMSComponentModel item;
    private String var;
    private int index;
    private int size;
    private int scope = 2;
    private String uid;
    private String position;


    public CMSContentSlotTag()
    {
        init();
    }


    public void release()
    {
        super.release();
        init();
    }


    protected void init()
    {
        this.index = 0;
        this.size = 0;
        this.var = null;
        this.components = null;
        this.item = null;
        this.scope = 2;
        this.uid = null;
        this.position = null;
    }


    protected void prepare()
    {
        this.components = new ArrayList<>();
        WebApplicationContext appContext = WebApplicationContextUtils.getRequiredWebApplicationContext(this.pageContext.getServletContext());
        CMSContentSlotService contentSlotService = (CMSContentSlotService)appContext.getBean("cmsContentSlotService");
        if(!StringUtils.isEmpty(this.uid))
        {
            try
            {
                this.contentSlot = contentSlotService.getContentSlotForId(this.uid);
            }
            catch(UnknownIdentifierException e)
            {
                LOG.warn("Error processing tag: " + e.getMessage());
            }
        }
        if(this.contentSlot != null)
        {
            if(!StringUtils.isEmpty(this.position))
            {
                this.contentSlot.setCurrentPosition(this.position);
            }
            this.components.addAll(contentSlotService.getSimpleCMSComponents(this.contentSlot, isPreviewEnabled(), (HttpServletRequest)this.pageContext
                            .getRequest()));
        }
        this.size = this.components.size();
    }


    protected boolean isPreviewEnabled()
    {
        boolean previewEnabled = false;
        String ticketId = this.pageContext.getRequest().getParameter("cmsTicketId");
        if(StringUtils.isNotBlank(ticketId))
        {
            WebApplicationContext appContext = WebApplicationContextUtils.getRequiredWebApplicationContext(this.pageContext.getServletContext());
            CMSPreviewService service = (CMSPreviewService)appContext.getBean("cmsPreviewService");
            CMSPreviewTicketModel previewTicket = service.getPreviewTicket(ticketId);
            if(previewTicket == null)
            {
                LOG.warn("No preview ticket found with cmsTicketId.");
            }
            else
            {
                previewEnabled = Boolean.FALSE.equals(previewTicket.getPreviewData().getEditMode());
            }
        }
        else if(LOG.isDebugEnabled())
        {
            LOG.debug("Could not determine preview edit status. Reason: No preview ticket ID supplied.");
        }
        return previewEnabled;
    }


    protected boolean hasNext()
    {
        return (this.index < this.size);
    }


    protected SimpleCMSComponentModel next()
    {
        return this.components.get(this.index);
    }


    protected void exposeVariables()
    {
        this.pageContext.setAttribute(this.var, this.item, this.scope);
        this.pageContext.setAttribute("contentSlot", this.contentSlot, this.scope);
        this.pageContext.setAttribute("elementPos", Integer.valueOf(this.index), this.scope);
        this.pageContext.setAttribute("isFirstElement", Boolean.valueOf((this.index == 0)), this.scope);
        this.pageContext.setAttribute("isLastElement", Boolean.valueOf((this.index == this.size - 1)), this.scope);
        this.pageContext.setAttribute("numberOfElements", Integer.valueOf(this.size), this.scope);
    }


    protected void unexposeVariables()
    {
        this.pageContext.removeAttribute(this.var, this.scope);
        this.pageContext.removeAttribute("contentSlot", this.scope);
        this.pageContext.removeAttribute("elementPos", this.scope);
        this.pageContext.removeAttribute("isFirstElement", this.scope);
        this.pageContext.removeAttribute("isLastElement", this.scope);
        this.pageContext.removeAttribute("numberOfElements", this.scope);
    }


    public int doStartTag() throws JspException
    {
        this.index = 0;
        prepare();
        if(hasNext())
        {
            this.item = next();
            exposeVariables();
            this.index++;
            return 1;
        }
        return 0;
    }


    public int doAfterBody() throws JspException
    {
        if(hasNext())
        {
            this.item = next();
            exposeVariables();
            this.index++;
            return 2;
        }
        unexposeVariables();
        return 0;
    }


    public void setContentSlot(ContentSlotModel contentSlot)
    {
        this.contentSlot = contentSlot;
    }


    public void setUid(String uid)
    {
        this.uid = uid;
    }


    public void setPosition(String position)
    {
        this.position = position;
    }


    public void setVar(String var)
    {
        this.var = var;
    }


    public void setBodyContent(BodyContent bodyContent)
    {
        this.bodyContent = bodyContent;
    }


    public void setScope(String scope)
    {
        if(StringUtils.isEmpty(scope) || StringUtils.equalsIgnoreCase(scope, "request"))
        {
            this.scope = 2;
        }
        else if(StringUtils.equalsIgnoreCase(scope, "page"))
        {
            this.scope = 1;
        }
    }
}
