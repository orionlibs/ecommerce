package de.hybris.platform.cmscockpit.session.impl;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.navigation.CMSNavigationNodeModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.CMSNavigationService;
import de.hybris.platform.cockpit.components.contentbrowser.AbstractContentBrowser;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.label.LabelService;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.UIBrowserArea;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.AbstractAdvancedBrowserModel;
import java.util.Collection;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;

public class NavigationNodeBrowserModel extends AbstractAdvancedBrowserModel
{
    private static final String CMS_NAVIGATION_SERVICE = "cmsNavigationService";
    private LabelService labelService;
    private TypeService typeService;
    private CMSNavigationService cmsNavigationService;


    public NavigationNodeBrowserModel()
    {
        UIBrowserArea browserArea = UISessionUtils.getCurrentSession().getCurrentPerspective().getBrowserArea();
        if(browserArea instanceof NavigationNodeBrowserArea)
        {
            CMSSiteModel cmsSite = ((NavigationNodeBrowserArea)browserArea).getActiveSite();
            CatalogVersionModel catVer = ((NavigationNodeBrowserArea)browserArea).getActiveCatalogVersion();
            if(cmsSite == null)
            {
                setLabel(Labels.getLabel("cmscockpit.no.website"));
            }
            else
            {
                String siteLabel = getLabelService().getObjectTextLabelForTypedObject(getTypeService().wrapItem(cmsSite));
                if(catVer == null)
                {
                    setLabel(siteLabel);
                }
                else
                {
                    CatalogModel catalog = catVer.getCatalog();
                    String catalogLabel = getLabelService().getObjectTextLabelForTypedObject(getTypeService().wrapItem(catalog));
                    String catVerLabel = getLabelService().getObjectTextLabelForTypedObject(getTypeService().wrapItem(catVer));
                    String arrows = " >> ";
                    setLabel(siteLabel + " >> " + siteLabel + " >> " + catalogLabel);
                }
            }
        }
    }


    public void removeItems(Collection<Integer> indexes)
    {
    }


    public void blacklistItems(Collection<Integer> indexes)
    {
    }


    public AbstractContentBrowser createViewComponent()
    {
        return (AbstractContentBrowser)new NavigationNodeContentBrowser();
    }


    public void updateItems()
    {
        fireItemsChanged();
    }


    public TypedObject getItem(int index)
    {
        return null;
    }


    public List<TypedObject> getItems()
    {
        return null;
    }


    public Object clone() throws CloneNotSupportedException
    {
        return null;
    }


    protected LabelService getLabelService()
    {
        if(this.labelService == null)
        {
            this.labelService = UISessionUtils.getCurrentSession().getLabelService();
        }
        return this.labelService;
    }


    public void removeSelectedNavigationNode(NavigationNodeContentBrowser content)
    {
        content.removeSelectedNavigationNode();
    }


    public void fireAddNewNavigationNode(NavigationNodeContentBrowser content)
    {
        content.fireAddRootNavigatioNode();
    }


    public int getTreeRootChildCount()
    {
        int ret = 0;
        UIBrowserArea browserArea = UISessionUtils.getCurrentSession().getCurrentPerspective().getBrowserArea();
        if(browserArea instanceof NavigationNodeBrowserArea)
        {
            NavigationNodeBrowserArea navigationBrowserArea = (NavigationNodeBrowserArea)browserArea;
            CatalogVersionModel catalogVersionModel = navigationBrowserArea.getActiveCatalogVersion();
            if(catalogVersionModel != null)
            {
                List<CMSNavigationNodeModel> rootNavigationNodes = getCmsNavigationNodeService().getRootNavigationNodes(navigationBrowserArea.getActiveCatalogVersion());
                ret = CollectionUtils.isEmpty(rootNavigationNodes) ? 0 : CollectionUtils.size(rootNavigationNodes);
            }
        }
        return ret;
    }


    protected TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = UISessionUtils.getCurrentSession().getTypeService();
        }
        return this.typeService;
    }


    protected CMSNavigationService getCmsNavigationNodeService()
    {
        if(this.cmsNavigationService == null)
        {
            this.cmsNavigationService = (CMSNavigationService)SpringUtil.getBean("cmsNavigationService");
        }
        return this.cmsNavigationService;
    }
}
