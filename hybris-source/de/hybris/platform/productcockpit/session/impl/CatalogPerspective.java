package de.hybris.platform.productcockpit.session.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.EditorAreaListener;
import de.hybris.platform.cockpit.session.NavigationAreaListener;
import de.hybris.platform.cockpit.session.SearchBrowserModel;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitPerspective;
import de.hybris.platform.cockpit.session.impl.BrowserAreaListener;
import de.hybris.platform.productcockpit.components.macfinder.MacFinderTreeComponent;
import de.hybris.platform.productcockpit.model.macfinder.node.MacFinderTreeNode;
import de.hybris.platform.productcockpit.session.CatalogNavigationAreaListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CatalogPerspective extends AbstractProductCockpitPerspective
{
    private static final Logger LOG = LoggerFactory.getLogger(CatalogPerspective.class);
    private static final String SUPERCATEGORIES = ".supercategories";
    private static final String CATALOGVERSION = ".catalogVersion";
    private final CatalogNavigationAreaListener navListener = (CatalogNavigationAreaListener)new CatalogNavigationAreaListenerImpl(this, (BaseUICockpitPerspective)this);
    private final BrowserAreaListener browserAreaListener = (BrowserAreaListener)new CatalogBrowserAreaListenerImpl(this);
    private final MyEditorAreaListener editorAreaListener = new MyEditorAreaListener(this);
    private CatalogBrowserWrapper activeBrowserClone;


    public void createNewItem(ObjectTemplate template, Map<String, Object> initVals, boolean loadDefaultValues)
    {
        Map<String, Object> initValues = new HashMap<>((initVals == null) ? Collections.EMPTY_MAP : initVals);
        if(template != null && (
                        UISessionUtils.getCurrentSession().getTypeService().getObjectType("Product").isAssignableFrom((ObjectType)template) ||
                                        UISessionUtils.getCurrentSession().getTypeService().getObjectType("Category").isAssignableFrom((ObjectType)template)))
        {
            CatalogVersionModel catalogVersionModel = ((CatalogNavigationArea)getNavigationArea()).getSelectedCatalogVersion();
            TypedObject catalogVersion = (catalogVersionModel == null) ? null : getTypeService().wrapItem(catalogVersionModel.getPk());
            MacFinderTreeNode lastNode = null;
            if(getBrowserArea().getFocusedBrowser() instanceof CategoryTreeBrowserModel)
            {
                CategoryTreeBrowserModel catalogBrowserArea = (CategoryTreeBrowserModel)getBrowserArea().getFocusedBrowser();
                lastNode = ((MacFinderTreeComponent)catalogBrowserArea.getTreeModel().getBindedComponent()).getLastSelectedCategory();
            }
            String code = template.getBaseType().getCode();
            if(lastNode != null &&
                            !UISessionUtils.getCurrentSession().getTypeService().getObjectType("Category").isAssignableFrom((ObjectType)template))
            {
                List<Object> cats = new ArrayList();
                cats.add(lastNode.getOriginalItem());
                String categoryKey = getCategoryKey(initValues);
                if(StringUtils.isNotBlank(categoryKey))
                {
                    if(initValues.containsKey(categoryKey))
                    {
                        int index = 0;
                        for(TypedObject initCat : initValues.get(categoryKey))
                        {
                            if(!cats.contains(initCat))
                            {
                                cats.add(index++, initCat);
                            }
                        }
                    }
                    initValues.put(categoryKey, cats);
                }
                else
                {
                    initValues.put(code + ".supercategories", cats);
                }
            }
            if(catalogVersion != null)
            {
                if(!initValues.containsKey(code + ".catalogVersion"))
                {
                    initValues.put(code + ".catalogVersion", catalogVersion);
                }
            }
        }
        super.createNewItem(template, initValues, loadDefaultValues);
    }


    private String getCategoryKey(Map<String, Object> map)
    {
        String key = null;
        for(String k : map.keySet())
        {
            if(k != null && k.contains(".supercategories"))
            {
                key = k;
                break;
            }
        }
        return key;
    }


    public void onShow()
    {
        getEditorArea().setManagingPerspective((UICockpitPerspective)this);
        createTemplateList("Product");
        BrowserModel browser = getBrowserArea().getFocusedBrowser();
        if(browser != null && browser instanceof SearchBrowserModel)
        {
            SearchBrowserModel searchBrowser = (SearchBrowserModel)browser;
            if(searchBrowser.getResult() == null)
            {
                searchBrowser.updateItems();
            }
        }
        try
        {
            if(getActiveItem() != null)
            {
                activateItemInEditor(getActiveItem());
            }
        }
        catch(Exception e)
        {
            LOG.warn("Error occurred when trying to load active item (Reason: '" + e.getMessage() + "').", e);
        }
    }


    public void onHide()
    {
    }


    protected EditorAreaListener getEditorAreaListener()
    {
        return (EditorAreaListener)this.editorAreaListener;
    }


    protected BrowserAreaListener getBrowserAreaListener()
    {
        return this.browserAreaListener;
    }


    protected NavigationAreaListener getNavigationAreaListener()
    {
        return (NavigationAreaListener)this.navListener;
    }


    protected void activateItemInEditorArea(TypedObject activeItem)
    {
        activateItemInEditorAndCloneBrowser(activeItem);
    }


    public void activateItemInEditorBasic(TypedObject activeItem)
    {
        super.activateItemInEditorArea(activeItem);
    }


    public void activateItemInEditorAndCloneBrowser(TypedObject activeItem)
    {
        if(activeItem != null)
        {
            this.activeBrowserClone = new CatalogBrowserWrapper(getBrowserArea().getFocusedBrowser(), activeItem);
        }
        activateItemInEditorBasic(activeItem);
    }


    public void handleItemRemoved(TypedObject object)
    {
        super.handleItemRemoved(object);
        if(this.activeBrowserClone != null)
        {
            BrowserModel browser = this.activeBrowserClone.getBrowser();
            if(browser != null)
            {
                browser.updateItems();
            }
        }
    }


    public boolean isEditorAreaBrowsingEnabled()
    {
        if(this.activeBrowserClone != null && this.activeBrowserClone.getBrowser() != null && this.activeBrowserClone
                        .getBrowser().getItems() != null && this.activeBrowserClone.getBrowser().getItems().size() > 1)
        {
            return true;
        }
        return false;
    }
}
