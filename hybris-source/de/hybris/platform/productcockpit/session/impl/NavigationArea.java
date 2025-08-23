package de.hybris.platform.productcockpit.session.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanelModel;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.impl.ItemChangedEvent;
import de.hybris.platform.cockpit.model.CockpitSavedQueryModel;
import de.hybris.platform.cockpit.model.advancedsearch.AdvancedSearchModel;
import de.hybris.platform.cockpit.model.collection.ObjectCollection;
import de.hybris.platform.cockpit.model.query.impl.UICollectionQuery;
import de.hybris.platform.cockpit.model.query.impl.UIQuery;
import de.hybris.platform.cockpit.model.query.impl.UISavedQuery;
import de.hybris.platform.cockpit.model.search.Query;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.NavigationAreaListener;
import de.hybris.platform.cockpit.session.SearchBrowserModel;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UISession;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitNavigationArea;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.productcockpit.components.navigationarea.NavigationAreaModel;
import de.hybris.platform.productcockpit.err.ProductCockpitBusinessException;
import de.hybris.platform.productcockpit.services.catalog.CatalogService;
import de.hybris.platform.productcockpit.session.ProductNavigationAreaListener;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.tx.TransactionBody;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Messagebox;

public class NavigationArea extends BaseUICockpitNavigationArea
{
    private static final Logger log = LoggerFactory.getLogger(NavigationArea.class);
    private CatalogService productCockpitCatalogService;
    private int categoryLevel = 1;


    public void initialize(Map<String, Object> params)
    {
        super.initialize(params);
    }


    public void update()
    {
        super.update();
    }


    public UICollectionQuery getCollectionQuery(ObjectCollection coll)
    {
        return ((NavigationAreaModel)getSectionModel()).getCollectionQuery(coll);
    }


    public Collection<CatalogVersionModel> getSelectedCatalogVersions()
    {
        Collection<CatalogVersionModel> ret;
        BrowserModel b = getPerspective().getBrowserArea().getFocusedBrowser();
        if(b instanceof DefaultProductSearchBrowserModel)
        {
            DefaultProductSearchBrowserModel searchBrowser = (DefaultProductSearchBrowserModel)b;
            ret = searchBrowser.getSelectedCatalogVersions();
        }
        else
        {
            ret = Collections.EMPTY_SET;
            if(log.isDebugEnabled())
            {
                log.debug("No focused query browser available.");
            }
        }
        return ret;
    }


    public Collection<CategoryModel> getSelectedCategories()
    {
        Collection<CategoryModel> ret;
        BrowserModel b = getPerspective().getBrowserArea().getFocusedBrowser();
        if(b instanceof DefaultProductSearchBrowserModel)
        {
            DefaultProductSearchBrowserModel searchBrowser = (DefaultProductSearchBrowserModel)b;
            ret = searchBrowser.getSelectedCategories();
        }
        else
        {
            ret = Collections.EMPTY_SET;
            if(log.isDebugEnabled())
            {
                log.debug("No focused query browser available.");
            }
        }
        return ret;
    }


    public SectionPanelModel getSectionModel()
    {
        if(super.getSectionModel() == null)
        {
            NavigationAreaModel m = new NavigationAreaModel(this);
            m.initialize();
            setSectionModel((SectionPanelModel)m);
        }
        return super.getSectionModel();
    }


    public void setSelectedCatalogItems(Collection<CatalogVersionModel> versions, Collection<CategoryModel> categories, boolean doubleClicked)
    {
        UISessionUtils.getCurrentSession().setSelectedCatalogVersions(new ArrayList<>(versions));
        BrowserModel b = getPerspective().getBrowserArea().getFocusedBrowser();
        if(b instanceof DefaultProductSearchBrowserModel)
        {
            DefaultProductSearchBrowserModel prodSearchBrowser = (DefaultProductSearchBrowserModel)b;
            prodSearchBrowser.setSelectedCatalogVersions((versions.isEmpty() && categories.isEmpty()) ?
                            getProductCockpitCatalogService().getAllCatalogVersions(getSession().getUser()) : versions);
            prodSearchBrowser.setSelectedCategories(categories);
            fireCatalogItemSelectionChanged(doubleClicked);
        }
        else
        {
            DefaultProductSearchBrowserModel prodSearchBrowser = new DefaultProductSearchBrowserModel();
            prodSearchBrowser.setSelectedCatalogVersions((versions.isEmpty() && categories.isEmpty()) ?
                            getProductCockpitCatalogService().getAllCatalogVersions(getSession().getUser()) : versions);
            prodSearchBrowser.setSelectedCategories(categories);
            prodSearchBrowser.setShowCreateButton(true);
            getPerspective().getBrowserArea().replaceBrowser(getPerspective().getBrowserArea().getFocusedBrowser(), (BrowserModel)prodSearchBrowser);
            fireCatalogItemSelectionChanged(doubleClicked);
        }
    }


    public void saveQuery(SearchBrowserModel searchBrowser)
    {
        Query query = searchBrowser.getLastQuery();
        if(query == null)
        {
            log.error("Cannot save query, Query is null.");
        }
        CockpitSavedQueryModel savedQuery = getSavedQueryService().createSavedQuery(searchBrowser.getLabel(), query,
                        getSession().getUser());
        UISavedQuery uiSavedQuery = new UISavedQuery(savedQuery);
        UISessionUtils.getCurrentSession().getCurrentPerspective().getNavigationArea().setSelectedQuery((UIQuery)uiSavedQuery);
    }


    public CatalogService getProductCockpitCatalogService()
    {
        return this.productCockpitCatalogService;
    }


    @Required
    public void setProductCockpitCatalogService(CatalogService productCockpitCatalogService)
    {
        this.productCockpitCatalogService = productCockpitCatalogService;
    }


    public UISession getSession()
    {
        return UISessionUtils.getCurrentSession();
    }


    public void handlePasteOperation(String rawContent, UICollectionQuery collection)
    {
        String[] codes = rawContent.replaceAll("\\s", ";").replaceAll(";+", ";").split(";");
        List<String> codeList = new ArrayList<>(Arrays.asList(codes));
        try
        {
            ObjectCollection _objectCollection = pasteProductToCollection(
                            (collection != null) ? collection.getObjectCollection() : null, codeList, getSession().getUser());
            if(collection == null)
            {
                fireCollectionAdded(_objectCollection);
            }
            else
            {
                fireCollectionChanged(_objectCollection);
            }
        }
        catch(ProductCockpitBusinessException e)
        {
            displayMessage(Labels.getLabel("navigationarea.allcodesnotfound"));
            log.debug(e.getMessage(), (Throwable)e);
        }
    }


    public ObjectCollection pasteProductToCollection(ObjectCollection collection, List<String> codes, UserModel user) throws ProductCockpitBusinessException
    {
        Object object = new Object(this, codes, collection, user);
        try
        {
            return (ObjectCollection)Transaction.current().execute((TransactionBody)object);
        }
        catch(Exception ex)
        {
            throw (ProductCockpitBusinessException)Transaction.toException(ex, ProductCockpitBusinessException.class);
        }
    }


    private List<PK> findMatchedProduct(List<String> codes)
    {
        List<PK> results = new ArrayList<>();
        SessionContext ctx = null;
        try
        {
            if(UITools.searchRestrictionsDisabledInCockpit())
            {
                ctx = JaloSession.getCurrentSession().createLocalSessionContext();
                ctx.setAttribute("disableRestrictions", Boolean.TRUE);
            }
            for(String code : codes)
            {
                Product product = (Product)ProductManager.getInstance().getFirstItemByAttribute(Product.class, "code", code);
                if(product != null)
                {
                    results.add(product.getPK());
                }
            }
        }
        finally
        {
            if(ctx != null)
            {
                JaloSession.getCurrentSession().removeLocalSessionContext();
            }
        }
        return results;
    }


    private void displayMessage(String message)
    {
        try
        {
            Messagebox.show(message, "Information", 1, "z-msgbox z-msgbox-information");
        }
        catch(InterruptedException interruptException)
        {
            log.error(interruptException.getMessage(), interruptException);
        }
    }


    protected void fireCatalogItemSelectionChanged(boolean doubleClicked)
    {
        for(NavigationAreaListener listener : getListeners())
        {
            ((ProductNavigationAreaListener)listener).catalogItemSelectionChanged(doubleClicked);
        }
    }


    public int getCategoryLevel()
    {
        return this.categoryLevel;
    }


    public void setCategoryLevel(int categoryLevel)
    {
        this.categoryLevel = categoryLevel;
    }


    public UICockpitPerspective getManagingPerspective()
    {
        return getPerspective();
    }


    public boolean isFocused()
    {
        return equals(getManagingPerspective().getFocusedArea());
    }


    public void resetContext()
    {
        super.resetContext();
        setAllProductVisible();
    }


    protected void setAllProductVisible()
    {
        BrowserModel b = getPerspective().getBrowserArea().getFocusedBrowser();
        if(b instanceof DefaultProductSearchBrowserModel)
        {
            DefaultProductSearchBrowserModel prodSearchBrowser = (DefaultProductSearchBrowserModel)b;
            if(!prodSearchBrowser.isCollapsed())
            {
                prodSearchBrowser.collapse();
            }
            AdvancedSearchModel advancedSearchModel = prodSearchBrowser.getAdvancedSearchModel();
            advancedSearchModel.resetToInitialSearchParameters();
        }
        setSelectedCatalogItems(Collections.EMPTY_LIST, Collections.EMPTY_LIST, true);
    }


    public void onCockpitEvent(CockpitEvent event)
    {
        super.onCockpitEvent(event);
        if(event instanceof ItemChangedEvent && ItemChangedEvent.ChangeType.CREATED.equals(((ItemChangedEvent)event).getChangeType()) && ((ItemChangedEvent)event)
                        .getItem() != null && ((ItemChangedEvent)event)
                        .getItem().getObject() instanceof CategoryModel)
        {
            getSectionModel().update();
        }
    }
}
