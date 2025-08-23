package de.hybris.platform.cmscockpit.components.welcomepage;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminPageService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;

public class SiteBox extends Div
{
    private static final long serialVersionUID = -6192361890421421723L;
    private static final Logger LOG = Logger.getLogger(SiteBox.class);


    public SiteBox(CMSSiteModel site)
    {
        setStyle("float: left; position: relative;");
        Hbox hbox = new Hbox();
        appendChild((Component)hbox);
        for(ContentCatalogModel contentCatalogModel : site.getContentCatalogs())
        {
            for(CatalogVersionModel version : contentCatalogModel.getCatalogVersions())
            {
                hbox.appendChild((Component)createSiteBox(version, site));
            }
        }
    }


    protected Div createSiteBox(CatalogVersionModel version, CMSSiteModel site)
    {
        Div container = new Div();
        container.setSclass("itemBoxParent");
        Div innerContainer = new Div();
        innerContainer.setSclass("itemBox");
        innerContainer.setHeight("100%");
        container.appendChild((Component)innerContainer);
        Div imgCnt = new Div();
        imgCnt.setStyle("width: 110px; height: 100px; overflow: hidden; border: 1px solid #999; margin: 5px;");
        innerContainer.appendChild((Component)imgCnt);
        Div titleDiv = new Div();
        titleDiv.setStyle("position: absolute; top 122px; left: 5px; overflow: hidden; right: 5px;");
        titleDiv.setSclass("welcome_site_title");
        titleDiv.appendChild((Component)new Label(site.getName()));
        innerContainer.appendChild((Component)titleDiv);
        String versionLabel = UISessionUtils.getCurrentSession().getLabelService().getObjectTextLabel(UISessionUtils.getCurrentSession().getTypeService().wrapItem(version));
        Div labelDiv = new Div();
        labelDiv.setStyle("position: absolute; top: 130px; left: 5px; overflow: hidden; right: 5px;");
        labelDiv.setSclass("welcome_page_name");
        labelDiv.appendChild((Component)new Label(versionLabel));
        innerContainer.appendChild((Component)labelDiv);
        ContentPageModel contentPageModel = getCmsAdminPageService().getHomepage(site);
        if(contentPageModel == null)
        {
            if(site.getStartPageLabel() != null)
            {
                try
                {
                    ContentPageModel pageByLabel = getCmsPageService().getDefaultPageForLabel(site.getStartPageLabel(), version);
                    contentPageModel = pageByLabel;
                }
                catch(CMSItemNotFoundException e)
                {
                    LOG.error(e.getMessage());
                }
            }
        }
        Image img = null;
        if(contentPageModel != null && contentPageModel.getPreviewImage() != null)
        {
            String imgURL = contentPageModel.getPreviewImage().getDownloadURL();
            if(imgURL != null)
            {
                img = new Image(UITools.getAdjustedUrl(imgURL));
            }
        }
        if(img == null)
        {
            img = new Image("/cmscockpit/images/page_nopreview.gif");
        }
        img.setWidth("110px");
        imgCnt.appendChild((Component)img);
        UITools.addBusyListener((Component)container, "onClick", (EventListener)new Object(this, site, version), null, null);
        return container;
    }


    protected CMSPageService getCmsPageService()
    {
        return (CMSPageService)SpringUtil.getBean("cmsPageService");
    }


    protected CMSAdminPageService getCmsAdminPageService()
    {
        return (CMSAdminPageService)SpringUtil.getBean("cmsAdminPageService");
    }
}
