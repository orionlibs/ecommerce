package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.components.notifier.Notification;
import de.hybris.platform.cockpit.constants.GeneratedCockpitConstants;
import de.hybris.platform.cockpit.model.CockpitObjectAbstractCollectionModel;
import de.hybris.platform.cockpit.model.CockpitSavedQueryModel;
import de.hybris.platform.cockpit.model.collection.ObjectCollection;
import de.hybris.platform.cockpit.model.collection.impl.ObjectCollectionImpl;
import de.hybris.platform.cockpit.model.query.impl.UISavedQuery;
import de.hybris.platform.cockpit.model.search.Query;
import de.hybris.platform.cockpit.model.search.SearchType;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.NavigationAreaListener;
import de.hybris.platform.cockpit.session.SearchBrowserModel;
import de.hybris.platform.cockpit.session.UIBrowserArea;
import de.hybris.platform.cockpit.session.UINavigationArea;
import de.hybris.platform.cockpit.session.UISessionUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;

public class DefaultNavigationAreaListener implements NavigationAreaListener
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultNavigationAreaListener.class);
    private final BaseUICockpitPerspective perspective;


    public DefaultNavigationAreaListener(BaseUICockpitPerspective perspective)
    {
        if(perspective == null)
        {
            throw new IllegalArgumentException("Perspective can not be null.");
        }
        this.perspective = perspective;
    }


    public void browserTaskDoubleClicked()
    {
        LOG.info("Browser double clicked event ignored.");
    }


    public void browserTaskSelected()
    {
        if(getPerspective().getBrowserArea() != null && getPerspective().getNavigationArea() != null)
        {
            BrowserModel browserModel = getNavigationArea().getSelectedBrowserTask();
            getPerspective().getBrowserArea().show(browserModel);
        }
    }


    public void savedQuerySelected()
    {
        UISavedQuery uiSavedQuery = null;
        if(getNavigationArea() != null && (uiSavedQuery = getNavigationArea().getSelectedSavedQuery()) != null)
        {
            CockpitSavedQueryModel savedQuery = uiSavedQuery.getSavedQuery();
            if(savedQuery != null && getBrowserArea() != null)
            {
                SearchBrowserModel searchBrowser = getSearchBrowserModel();
                Query query = getNavigationArea().getSavedQueryService().getQuery(savedQuery);
                if(query != null && CollectionUtils.isNotEmpty(query.getSelectedTypes()) && query
                                .getSelectedTypes().iterator().next() != null)
                {
                    searchBrowser.setRootType(UISessionUtils.getCurrentSession().getTypeService()
                                    .getObjectTemplate(((SearchType)query.getSelectedTypes().iterator().next()).getCode()));
                }
                if(savedQuery.getDefaultViewMode() != null)
                {
                    searchBrowser.setViewMode(savedQuery.getDefaultViewMode());
                }
                getBrowserArea().replaceBrowser(getBrowserArea().getFocusedBrowser(), (BrowserModel)searchBrowser);
                searchBrowser.updateItems(query);
            }
        }
    }


    protected SearchBrowserModel getSearchBrowserModel()
    {
        return (SearchBrowserModel)new DefaultSearchBrowserModel();
    }


    public void collectionSelected()
    {
        CollectionBrowserModel collectionBrowserModel = new CollectionBrowserModel();
        if(getNavigationArea().getSelectedCollection().getObjectCollection() instanceof ObjectCollectionImpl)
        {
            CockpitObjectAbstractCollectionModel collectionModel = (CockpitObjectAbstractCollectionModel)UISessionUtils.getCurrentSession().getModelService().get(getNavigationArea().getSelectedCollection().getObjectCollection().getPK());
            ((ObjectCollectionImpl)getNavigationArea().getSelectedCollection().getObjectCollection()).setLabel(collectionModel
                            .getLabel());
        }
        collectionBrowserModel.setCollection(getNavigationArea().getSelectedCollection().getObjectCollection());
        getBrowserArea().replaceBrowser(getBrowserArea().getFocusedBrowser(), (BrowserModel)collectionBrowserModel);
        collectionBrowserModel.updateItems();
    }


    public void dynamicQuerySelected()
    {
        DynamicQueryBrowserModel dynamicQueryBrowser = new DynamicQueryBrowserModel();
        dynamicQueryBrowser.setDynamicQuery(getNavigationArea().getSelectedDynamicQuery().getQuery());
        dynamicQueryBrowser.updateItems();
        getBrowserArea().replaceBrowser(getBrowserArea().getFocusedBrowser(), (BrowserModel)dynamicQueryBrowser);
    }


    public void collectionChanged(ObjectCollection collection)
    {
        CollectionBrowserModel collectionBrowserModel = getCorrespondingCollectionBrowser(collection);
        if(collectionBrowserModel != null)
        {
            collectionBrowserModel.setCollection(collection);
            collectionBrowserModel.updateItems();
            if(getPerspective().getNotifier() != null)
            {
                Notification notification = new Notification(Labels.getLabel("collection.collections"), Labels.getLabel("collection.changed"));
                getPerspective().getNotifier().setNotification(notification);
            }
        }
        else
        {
            LOG.debug("No corresponding browser found for collection '" + collection.getLabel() + "'");
        }
        if(collection.getType().equals("CockpitObjectSpecialCollection") && collection
                        .getQualifier().equals(GeneratedCockpitConstants.Enumerations.CockpitSpecialCollectionType.BLACKLIST))
        {
            getBrowserArea().getFocusedBrowser().updateItems();
        }
        getNavigationArea().update();
    }


    private CollectionBrowserModel getCorrespondingCollectionBrowser(ObjectCollection objectCollection)
    {
        CollectionBrowserModel collectionBrowserModel = null;
        for(BrowserModel b : getBrowserArea().getBrowsers())
        {
            if(b instanceof CollectionBrowserModel && !(b instanceof DynamicQueryBrowserModel))
            {
                CollectionBrowserModel tmp = (CollectionBrowserModel)b;
                if(tmp.getCollection().equals(objectCollection))
                {
                    collectionBrowserModel = tmp;
                    break;
                }
            }
        }
        return collectionBrowserModel;
    }


    public void collectionAdded(ObjectCollection collection)
    {
        getNavigationArea().update();
        if(getPerspective().getNotifier() != null)
        {
            Notification notification = new Notification(Labels.getLabel("collection.collections"), Labels.getLabel("collection.created"));
            getPerspective().getNotifier().setNotification(notification);
        }
    }


    public void savedQueryChanged(CockpitSavedQueryModel query)
    {
        getNavigationArea().update();
        if(getPerspective().getNotifier() != null)
        {
            Notification notification = new Notification("Saved Queries", "A saved query was changed.");
            getPerspective().getNotifier().setNotification(notification);
        }
    }


    public void collectionDoubleClicked()
    {
        CollectionBrowserModel collectionBrowserModel = new CollectionBrowserModel();
        collectionBrowserModel.setCollection(getNavigationArea().getSelectedCollection().getObjectCollection());
        collectionBrowserModel.updateItems();
        getBrowserArea().show((BrowserModel)collectionBrowserModel);
    }


    public void dynamicQueryDoubleClicked()
    {
        DynamicQueryBrowserModel dynamicQueryBrowser = new DynamicQueryBrowserModel();
        dynamicQueryBrowser.setDynamicQuery(getNavigationArea().getSelectedDynamicQuery().getQuery());
        dynamicQueryBrowser.updateItems();
        getBrowserArea().show((BrowserModel)dynamicQueryBrowser);
    }


    public void savedQueryDoubleClicked()
    {
        SearchBrowserModel searchBrowser = getSearchBrowserModel();
        Query query = getNavigationArea().getSavedQueryService().getQuery(
                        getNavigationArea().getSelectedSavedQuery().getSavedQuery());
        searchBrowser.setLastQuery(query);
        getNavigationArea().update();
        getBrowserArea().show((BrowserModel)searchBrowser);
        searchBrowser.updateItems();
    }


    protected BaseUICockpitPerspective getPerspective()
    {
        return this.perspective;
    }


    private UINavigationArea getNavigationArea()
    {
        return getPerspective().getNavigationArea();
    }


    private UIBrowserArea getBrowserArea()
    {
        return getPerspective().getBrowserArea();
    }
}
