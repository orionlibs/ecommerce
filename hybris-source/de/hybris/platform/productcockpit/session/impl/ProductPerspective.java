package de.hybris.platform.productcockpit.session.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cockpit.components.navigationarea.renderer.OpenBoxesSectionRenderer;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.NewItemService;
import de.hybris.platform.cockpit.services.ObjectCollectionService;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.EditorAreaListener;
import de.hybris.platform.cockpit.session.NavigationAreaListener;
import de.hybris.platform.cockpit.session.SearchBrowserModel;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitPerspective;
import de.hybris.platform.cockpit.session.impl.BrowserAreaListener;
import de.hybris.platform.cockpit.util.CockpitBrowserAreaAutoSearchConfigurationUtil;
import de.hybris.platform.cockpit.util.UITools;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Div;

public class ProductPerspective extends AbstractProductCockpitPerspective
{
    private static final Logger LOG = LoggerFactory.getLogger(ProductPerspective.class);
    private static final String SUPERCATEGORIES = ".supercategories";
    private static final String CATALOGVERSION = ".catalogVersion";
    OpenBoxesSectionRenderer openBoxesSectionRenderer = null;
    private final MyNavigationAreaListener navListener = new MyNavigationAreaListener(this, (BaseUICockpitPerspective)this);
    private final MyBrowserAreaListener browserAreaListener = new MyBrowserAreaListener(this);
    private final MyEditorAreaListener editorAreaListener = new MyEditorAreaListener(this);
    private Div openBrowsersContainer = null;
    private ObjectCollectionService objectCollectionService = null;
    private ProductBrowserWrapper activeBrowserClone;
    private NewItemService newItemService;


    public NavigationArea getProductNavigationArea()
    {
        return (NavigationArea)getNavigationArea();
    }


    public Div getOpenBrowsersContainer()
    {
        return this.openBrowsersContainer;
    }


    public void setOpenBrowsersContainer(Div div)
    {
        this.openBrowsersContainer = div;
        resetOpenBrowserContainer();
    }


    public void resetOpenBrowserContainer()
    {
        if(this.openBrowsersContainer != null)
        {
            UITools.detachChildren((Component)this.openBrowsersContainer);
            this.openBoxesSectionRenderer = new OpenBoxesSectionRenderer();
            this.openBoxesSectionRenderer.setNavigationArea(getNavigationArea());
            this.openBoxesSectionRenderer.render((Component)this.openBrowsersContainer);
        }
    }


    public void onHide()
    {
    }


    public void onShow()
    {
        getEditorArea().setManagingPerspective((UICockpitPerspective)this);
        createTemplateList("Product");
        BrowserModel browserModel = getBrowserArea().getFocusedBrowser();
        if(browserModel instanceof SearchBrowserModel)
        {
            SearchBrowserModel searchBrowser = (SearchBrowserModel)browserModel;
            if(searchBrowser.getResult() == null)
            {
                if(!CockpitBrowserAreaAutoSearchConfigurationUtil.isInitialSearchDisabled(Executions.getCurrent()) &&
                                !CockpitBrowserAreaAutoSearchConfigurationUtil.isAutomaticSearchDisabled(Executions.getCurrent()))
                {
                    searchBrowser.updateItems(0);
                }
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


    public void createNewItem(ObjectTemplate template, Map<String, Object> initVals, boolean loadDefaultValues)
    {
        Map<String, Object> initValues = new HashMap<>((initVals == null) ? Collections.EMPTY_MAP : initVals);
        if(template != null && (
                        UISessionUtils.getCurrentSession().getTypeService().getObjectType("Product").isAssignableFrom((ObjectType)template) ||
                                        UISessionUtils.getCurrentSession().getTypeService().getObjectType("Category").isAssignableFrom((ObjectType)template)))
        {
            TypedObject catalogVersion = null;
            String code = template.getBaseType().getCode();
            List<TypedObject> cats = new ArrayList<>();
            BrowserModel focusedBrowser = getBrowserArea().getFocusedBrowser();
            if(focusedBrowser instanceof DefaultProductSearchBrowserModel)
            {
                Set<CatalogVersionModel> selectedCatalogVersions = new HashSet<>(((DefaultProductSearchBrowserModel)focusedBrowser).getSelectedCatalogVersions());
                for(CategoryModel categoryModel : ((DefaultProductSearchBrowserModel)focusedBrowser).getSelectedCategories())
                {
                    cats.add(getTypeService().wrapItem(categoryModel));
                    if(categoryModel.getCatalogVersion() != null)
                    {
                        selectedCatalogVersions.add(categoryModel.getCatalogVersion());
                    }
                }
                if(selectedCatalogVersions.size() == 1)
                {
                    CatalogVersionModel next = selectedCatalogVersions.iterator().next();
                    catalogVersion = getTypeService().wrapItem(next);
                }
            }
            if(!initValues.containsKey(code + ".supercategories"))
            {
                initValues.put(code + ".supercategories", cats);
            }
            if(!initValues.containsKey(code + ".catalogVersion"))
            {
                initValues.put(code + ".catalogVersion", (catalogVersion == null) ? null : catalogVersion);
            }
        }
        super.createNewItem(template, initValues, loadDefaultValues);
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
            this.activeBrowserClone = new ProductBrowserWrapper(getBrowserArea().getFocusedBrowser(), activeItem);
        }
        activateItemInEditorBasic(activeItem);
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


    protected EditorAreaListener getEditorAreaListener()
    {
        return (EditorAreaListener)this.editorAreaListener;
    }


    protected BrowserAreaListener getBrowserAreaListener()
    {
        return (BrowserAreaListener)this.browserAreaListener;
    }


    public void leftArrowPressed()
    {
    }


    public void rightArrowPressed()
    {
    }


    protected NavigationAreaListener getNavigationAreaListener()
    {
        return (NavigationAreaListener)this.navListener;
    }


    @Required
    public void setObjectCollectionService(ObjectCollectionService objectCollectionService)
    {
        this.objectCollectionService = objectCollectionService;
    }


    public ObjectCollectionService getObjectCollectionService()
    {
        return this.objectCollectionService;
    }


    @Required
    public void setNewItemService(NewItemService newItemService)
    {
        this.newItemService = newItemService;
    }


    public NewItemService getNewItemService()
    {
        return this.newItemService;
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
}
