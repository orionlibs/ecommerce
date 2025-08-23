package de.hybris.platform.cmscockpit.components.listview.impl;

import de.hybris.platform.cms2.model.CMSPageTypeModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cmscockpit.util.SitePreviewTools;
import de.hybris.platform.cockpit.components.listview.AbstractListViewAction;
import de.hybris.platform.cockpit.components.listview.ListViewAction;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.MediaUpdateService;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.util.Config;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Image;
import org.zkoss.zul.Menupopup;

public class SitePreviewThumbnailAction extends AbstractListViewAction
{
    private static final String IMAGE_URI = "/cmscockpit/images/snapshot.gif";
    private static final Logger LOG = Logger.getLogger(SitePreviewThumbnailAction.class);
    private CMSAdminSiteService adminSiteService = null;
    private MediaUpdateService mediaUpdateService = null;
    private TypeService typeService = null;
    private boolean previewUrlAdjustingEnabled = false;


    protected void doCreateContext(ListViewAction.Context context)
    {
    }


    public Menupopup getContextPopup(ListViewAction.Context context)
    {
        return null;
    }


    protected CMSAdminSiteService getCmsAdminSiteService()
    {
        if(this.adminSiteService == null)
        {
            this.adminSiteService = (CMSAdminSiteService)SpringUtil.getBean("cmsAdminSiteService");
        }
        return this.adminSiteService;
    }


    public EventListener getEventListener(ListViewAction.Context context)
    {
        return (EventListener)new Object(this, context);
    }


    public String getImageURI(ListViewAction.Context context)
    {
        return (Config.getParameter("cmscockpit.default.cutyCaptExecutable") != null && isAvailable(context)) ? "/cmscockpit/images/snapshot.gif" : null;
    }


    public Menupopup getPopup(ListViewAction.Context context)
    {
        return null;
    }


    public String getTooltip(ListViewAction.Context context)
    {
        return Labels.getLabel("cmscockpit.generate.preview");
    }


    protected boolean isAvailable(ListViewAction.Context context)
    {
        boolean ret = false;
        TypedObject wrappedPageModel = context.getItem();
        if(wrappedPageModel == null)
        {
            return ret;
        }
        if(wrappedPageModel.getObject() instanceof de.hybris.platform.cms2.model.pages.AbstractPageModel)
        {
            ComposedTypeModel composedType = getTypeService().getComposedTypeForClass(wrappedPageModel.getObject().getClass());
            ret = !((CMSPageTypeModel)composedType).isPreviewDisabled();
        }
        return ret;
    }


    public MediaUpdateService getMediaUpdateService()
    {
        if(this.mediaUpdateService == null)
        {
            this.mediaUpdateService = (MediaUpdateService)SpringUtil.getBean("mediaUpdateService");
        }
        return this.mediaUpdateService;
    }


    public TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = (TypeService)SpringUtil.getBean("typeService");
        }
        return this.typeService;
    }


    protected Image generateImage(String url, String cutyCaptExecutable, String workingDir, int width, long timeout)
    {
        return SitePreviewTools.generatePreviewImage(adjustPreviewUrl(url), cutyCaptExecutable, workingDir, width, timeout);
    }


    protected String adjustPreviewUrl(String url)
    {
        String ret = url;
        if(this.previewUrlAdjustingEnabled || isAdjustingEnabledFromProperty())
        {
            ret = StringUtils.deleteWhitespace(url);
        }
        return ret;
    }


    protected boolean isAdjustingEnabledFromProperty()
    {
        try
        {
            return Boolean.parseBoolean(UITools.getCockpitParameter("default.sitePreviewThumbnailAction.enableUrlAdjusting",
                            Executions.getCurrent()));
        }
        catch(Exception e)
        {
            LOG.warn("Could not retrieve property 'sitePreviewThumbnailAction.enableUrlAdjusting', returning false.");
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Error was", e);
            }
            return false;
        }
    }


    public void setPreviewUrlAdjustingEnabled(boolean previewUrlAdjustingEnabled)
    {
        this.previewUrlAdjustingEnabled = previewUrlAdjustingEnabled;
    }
}
