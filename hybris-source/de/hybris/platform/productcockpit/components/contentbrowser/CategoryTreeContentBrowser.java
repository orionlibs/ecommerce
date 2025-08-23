package de.hybris.platform.productcockpit.components.contentbrowser;

import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.cockpit.components.contentbrowser.AbstractBrowserComponent;
import de.hybris.platform.cockpit.components.contentbrowser.AbstractContentBrowser;
import de.hybris.platform.cockpit.components.contentbrowser.DefaultAdvancedContentBrowser;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.SystemService;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.productcockpit.components.macfinder.MacFinderTreeComponent;
import de.hybris.platform.productcockpit.model.macfinder.MacFinderTreeModelAbstract;
import de.hybris.platform.productcockpit.model.macfinder.node.MacFinderTreeNode;
import de.hybris.platform.productcockpit.session.impl.CategoryTreeBrowserModel;
import org.apache.commons.lang.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

public class CategoryTreeContentBrowser extends DefaultAdvancedContentBrowser
{
    private static final Logger LOG = LoggerFactory.getLogger(CategoryTreeContentBrowser.class);
    private final transient ListitemRenderer listItemRenderer = (ListitemRenderer)new MacFinderTreeNodeRenderer(this);
    public static final String MEDIA = "ct_media";
    public static final String PRODUCT = "ct_product";
    public static final String CATEGORY = "ct_category";


    protected boolean initialize()
    {
        setActiveWhenUnfocused(true);
        return super.initialize();
    }


    public MacFinderTreeModelAbstract getTreeModel()
    {
        return getModel().getTreeModel();
    }


    public ListitemRenderer getListItemRenderer()
    {
        return this.listItemRenderer;
    }


    protected AbstractBrowserComponent createMainAreaComponent()
    {
        MacFinderTreeComponent component = new MacFinderTreeComponent(getModel(), (AbstractContentBrowser)this);
        component.setListItemRenderer(getListItemRenderer());
        return (AbstractBrowserComponent)component;
    }


    protected AbstractBrowserComponent createCaptionComponent()
    {
        return (AbstractBrowserComponent)new MyCaptionComponent(this, (BrowserModel)getModel(), (AbstractContentBrowser)this);
    }


    protected AbstractBrowserComponent createToolbarComponent()
    {
        return null;
    }


    public CategoryTreeBrowserModel getModel()
    {
        return (CategoryTreeBrowserModel)super.getModel();
    }


    protected TypedObject getParentObject(MacFinderTreeNode node)
    {
        TypedObject ret = null;
        try
        {
            if(node instanceof de.hybris.platform.productcockpit.model.macfinder.node.CategoryNode || node instanceof de.hybris.platform.productcockpit.model.macfinder.node.ProductNode || node instanceof de.hybris.platform.productcockpit.model.macfinder.node.MediaNode)
            {
                ret = node.getContainingColumn().getSelectedNode().getOriginalItem();
            }
            else if(node instanceof de.hybris.platform.productcockpit.model.macfinder.node.LeafNode)
            {
                ret = node.getContainingColumn().getParentColumn().getParentColumn().getSelectedNode().getOriginalItem();
            }
        }
        catch(Exception e)
        {
            LOG.warn(e.getMessage(), e);
        }
        return ret;
    }


    public void updateActivation()
    {
        getModel().refreshActiveItems();
        super.updateActivation();
    }


    private EventListener getProductContextMenuListener(Listitem listItem, Hbox horizontalContainer, Label categoryLabelContainer)
    {
        return (EventListener)new Object(this, horizontalContainer, listItem, categoryLabelContainer);
    }


    private boolean isProductContextMenuVisible()
    {
        SystemService systemService = UISessionUtils.getCurrentSession().getSystemService();
        if(BooleanUtils.toBooleanDefaultIfNull(Boolean.valueOf(
                        UITools.getCockpitParameter("default.categoryProductRelation.showContextMenu", Executions.getCurrent())), true) && systemService
                        .checkPermissionOn(GeneratedCatalogConstants.Relations.CATEGORYPRODUCTRELATION, "remove"))
        {
            return true;
        }
        return false;
    }
}
