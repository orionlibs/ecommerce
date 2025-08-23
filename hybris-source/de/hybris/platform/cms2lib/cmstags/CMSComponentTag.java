package de.hybris.platform.cms2lib.cmstags;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.cms2.model.contents.containers.AbstractCMSComponentContainerModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.preview.CMSPreviewTicketModel;
import de.hybris.platform.cms2.servicelayer.data.CMSDataFactory;
import de.hybris.platform.cms2.servicelayer.data.RestrictionData;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.cms2.servicelayer.services.CMSPreviewService;
import de.hybris.platform.cms2.servicelayer.services.CMSRestrictionService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class CMSComponentTag extends TagSupport
{
    private static final long serialVersionUID = 1334123496915519595L;
    private static final String NPTFW_ID = "No preview ticket found with cmsTicketId.";
    private static final Logger LOG = Logger.getLogger(CMSComponentTag.class.getName());
    protected static final String INIT_LE_JS = "initLiveEdit";
    protected static final String DEFAULT_CONTROLLER = "DefaultCMSComponentController";
    private String uid;
    private AbstractCMSComponentModel component;
    private boolean evaluateRestriction;


    public CMSComponentTag()
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
        this.uid = null;
    }


    protected ServletContext getServletContext()
    {
        return this.pageContext.getServletContext();
    }


    protected Collection<SimpleCMSComponentModel> getComponents(WebApplicationContext appContext, HttpServletRequest request) throws CMSItemNotFoundException
    {
        List<SimpleCMSComponentModel> ret = new ArrayList<>();
        AbstractCMSComponentModel componentModel = null;
        CMSComponentService service = (CMSComponentService)appContext.getBean("cmsComponentService");
        CMSRestrictionService cmsRestrictionService = (CMSRestrictionService)appContext.getBean("cmsRestrictionService");
        CMSDataFactory restrictionDataFactory = (CMSDataFactory)appContext.getBean("cmsDataFactory");
        RestrictionData restrictionData = populate(request, restrictionDataFactory);
        boolean previewEnabled = isPreviewEnabled();
        if(this.component != null)
        {
            componentModel = this.component;
        }
        else if(StringUtils.isNotEmpty(this.uid))
        {
            componentModel = service.getAbstractCMSComponent(this.uid);
        }
        else
        {
            throw new CMSItemNotFoundException("No component found.");
        }
        boolean allowed = isAllowed(componentModel, restrictionData, cmsRestrictionService, previewEnabled);
        if(allowed)
        {
            if(componentModel.isContainer())
            {
                AbstractCMSComponentContainerModel container = (AbstractCMSComponentContainerModel)componentModel;
                container.getCurrentCMSComponents().stream()
                                .filter(innerComponent -> isAllowed((AbstractCMSComponentModel)innerComponent, restrictionData, cmsRestrictionService, previewEnabled))
                                .forEach(innerComponent -> ret.add(innerComponent));
            }
            else
            {
                ret.add((SimpleCMSComponentModel)componentModel);
            }
        }
        return ret;
    }


    protected boolean isAllowed(AbstractCMSComponentModel component, RestrictionData restrictionData, CMSRestrictionService cmsRestrictionService, boolean previewEnabled)
    {
        boolean allowed = true;
        if(Boolean.FALSE.equals(component.getVisible()))
        {
            allowed = false;
        }
        else if(component.isRestricted() && !previewEnabled && this.evaluateRestriction)
        {
            allowed = cmsRestrictionService.evaluateCMSComponent(component, restrictionData);
        }
        return allowed;
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


    @Deprecated(since = "1811", forRemoval = true)
    protected boolean isLiveEdit()
    {
        boolean liveEdit = false;
        String ticketId = this.pageContext.getRequest().getParameter("cmsTicketId");
        if(StringUtils.isNotBlank(ticketId))
        {
            WebApplicationContext appContext = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
            CMSPreviewService service = (CMSPreviewService)appContext.getBean("cmsPreviewService");
            CMSPreviewTicketModel previewTicket = service.getPreviewTicket(ticketId);
            if(previewTicket == null)
            {
                LOG.warn("No preview ticket found with cmsTicketId.");
            }
            else
            {
                liveEdit = Boolean.TRUE.equals(previewTicket.getPreviewData().getLiveEdit());
            }
        }
        else if(LOG.isDebugEnabled())
        {
            LOG.debug("Could not determine live edit status. Reason: No preview ticket ID supplied.");
        }
        return liveEdit;
    }


    public int doStartTag() throws JspException
    {
        WebApplicationContext appContext = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        HttpServletRequest request = (HttpServletRequest)this.pageContext.getRequest();
        boolean liveEdit = isLiveEdit();
        if(liveEdit)
        {
            String init = (String)request.getAttribute("initLiveEdit");
            if(StringUtils.isEmpty(init))
            {
                initLiveEditJS(request);
                request.setAttribute("initLiveEdit", "true");
            }
        }
        try
        {
            ContentSlotModel contentSlot = (ContentSlotModel)request.getAttribute("contentSlot");
            String prefix = (contentSlot == null) ? "ceid_" : ("ceid_" + contentSlot.getUid() + "___");
            for(SimpleCMSComponentModel element : getComponents(appContext, request))
            {
                String id = prefix + prefix;
                if(liveEdit)
                {
                    this.pageContext.getOut().write("<!-- Start of liveEdit [" + id + "] -->");
                    this.pageContext.getOut().write("<div id=\"" + id + "\">");
                }
                this.pageContext.setAttribute("componentUid", element.getUid(), 2);
                String code = element.getTypeCode();
                String controllerName = code + "Controller";
                if(!appContext.containsBean(controllerName))
                {
                    LOG.debug("No controller defined for ContentElement [" + code + "]. Using default Controller");
                    controllerName = "DefaultCMSComponentController";
                }
                this.pageContext.include("/view/" + controllerName);
                if(liveEdit)
                {
                    this.pageContext.getOut().write("</div>");
                    this.pageContext.getOut().write("<!-- End of liveEdit [" + id + "] -->");
                }
            }
        }
        catch(Exception e)
        {
            LOG.warn("Error processing tag", e);
        }
        return 0;
    }


    protected void initLiveEditJS(HttpServletRequest request)
    {
        JspWriter out = this.pageContext.getOut();
        try
        {
            out.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"" + request
                            .getContextPath() + "/stylesheets/liveEdit.css\"/>");
            out.write("<script type=\"text/javascript\" src=\"" + request.getContextPath() + "/js/liveedit.js\"></script>");
        }
        catch(IOException e)
        {
            LOG.warn("Could not write initial liveEdit JavaScript", e);
        }
    }


    public int doEndTag() throws JspException
    {
        return 6;
    }


    public void setUid(String uid)
    {
        this.uid = uid;
    }


    public void setComponent(AbstractCMSComponentModel component)
    {
        this.component = component;
    }


    public void setEvaluateRestriction(boolean evaluateRestriction)
    {
        this.evaluateRestriction = evaluateRestriction;
    }


    protected RestrictionData populate(HttpServletRequest request, CMSDataFactory cmsDataFactory)
    {
        Object catalog = request.getAttribute("catalogId");
        Object category = request.getAttribute("currentCategoryCode");
        Object product = request.getAttribute("currentProductCode");
        String catalogId = Objects.nonNull(catalog) ? catalog.toString() : "";
        String categoryCode = Objects.nonNull(category) ? category.toString() : "";
        String productCode = Objects.nonNull(product) ? product.toString() : "";
        return cmsDataFactory.createRestrictionData(categoryCode, productCode, catalogId);
    }
}
