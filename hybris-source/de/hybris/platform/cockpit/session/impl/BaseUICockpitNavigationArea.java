package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.components.navigationarea.AbstractNavigationAreaModel;
import de.hybris.platform.cockpit.components.navigationarea.renderer.AbstractNavigationAreaSectionRenderer;
import de.hybris.platform.cockpit.components.notifier.Notification;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanelModel;
import de.hybris.platform.cockpit.components.sectionpanel.SimpleRenderer;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.model.CockpitSavedQueryModel;
import de.hybris.platform.cockpit.model.collection.ObjectCollection;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.query.impl.UICollectionQuery;
import de.hybris.platform.cockpit.model.query.impl.UIDynamicQuery;
import de.hybris.platform.cockpit.model.query.impl.UIQuery;
import de.hybris.platform.cockpit.model.query.impl.UISavedQuery;
import de.hybris.platform.cockpit.services.ObjectCollectionService;
import de.hybris.platform.cockpit.services.config.ListViewConfiguration;
import de.hybris.platform.cockpit.services.config.UIConfigurationService;
import de.hybris.platform.cockpit.services.config.UIRole;
import de.hybris.platform.cockpit.services.config.impl.UIComponentCache;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.query.DynamicQueryService;
import de.hybris.platform.cockpit.services.query.SavedQueryService;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.NavigationAreaListener;
import de.hybris.platform.cockpit.session.UINavigationArea;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;

public class BaseUICockpitNavigationArea extends AbstractUINavigationArea
{
    private static final Logger LOG = LoggerFactory.getLogger(BaseUICockpitNavigationArea.class);
    private SimpleRenderer contentSlotRenderer = null;
    private SimpleRenderer infoSlotRenderer = null;
    private HtmlBasedComponent contentSlotContainer = null;
    private HtmlBasedComponent infoSlotContainer = null;
    private String contentSlotLabel;
    private String contentSlotI3Label;
    private String infoSlotLabel;
    private String infoSlotI3Label;
    private List<String> infoSlotAttachmentTypes = Collections.EMPTY_LIST;
    private BrowserModel selectedBrowserTask;
    private ObjectCollectionService objectCollectionService;
    private SavedQueryService savedQueryService;
    private DynamicQueryService dynamicQueryService;
    private TypeService typeService;
    private UIComponentCache uiComponentCache;
    private UIConfigurationService uiConfigurationService;
    private ModelService modelService;
    private UISavedQuery selectedSavedQuery;
    private UICollectionQuery selectedCollection;
    private UIDynamicQuery selectedDynamicQuery;
    private boolean pushInfoBox = true;
    private String pushInfoBoxEventClassName = null;
    private final HashMap<String, Integer> selectedIndexes = new HashMap<>();


    public BrowserModel getSelectedBrowserTask()
    {
        return this.selectedBrowserTask;
    }


    public void setSelectedBrowserTask(BrowserModel browserModel)
    {
        this.selectedBrowserTask = browserModel;
        fireBrowserTaskSelected();
    }


    protected void fireBrowserTaskSelected()
    {
        for(NavigationAreaListener listener : getListeners())
        {
            listener.browserTaskSelected();
        }
    }


    public void initialize(Map<String, Object> params)
    {
        if(getSectionModel() != null)
        {
            getSectionModel().initialize();
        }
        updateContentSlotContainer();
        updateInfoSlotContainer();
    }


    public void update()
    {
        if(getSectionModel() != null)
        {
            getSectionModel().update();
        }
        updateContentSlotContainer();
        if(!this.pushInfoBox)
        {
            updateInfoSlotContainer();
        }
    }


    public void setSectionModel(SectionPanelModel sectionModel)
    {
        super.setSectionModel(sectionModel);
        if(sectionModel instanceof AbstractNavigationAreaModel && ((AbstractNavigationAreaModel)sectionModel)
                        .getNavigationArea() != this)
        {
            ((AbstractNavigationAreaModel)sectionModel).setNavigationArea(this);
        }
    }


    public void updateContentSlotContainer()
    {
        if(getContentSlotContainer() != null)
        {
            try
            {
                UITools.detachChildren((Component)getContentSlotContainer());
                if(getContentSlotRenderer() != null)
                {
                    getContentSlotRenderer().render((Component)getContentSlotContainer());
                    UITools.invalidateNextLayoutregion((Component)this.contentSlotContainer);
                    UITools.resizeBorderLayout((Component)this.contentSlotContainer, false);
                }
            }
            catch(Exception e)
            {
                LOG.warn("Could not update content slot (Reason: " + e.getMessage() + ").", e);
            }
        }
    }


    public void updateInfoSlotContainer()
    {
        if(getInfoSlotContainer() != null)
        {
            if(UITools.isFromOtherDesktop((Component)getInfoSlotContainer()))
            {
                LOG.info("Ignoring updating of info slot. Reason: Info slot container has not been correctly initialized since the desktop was created.");
            }
            else
            {
                try
                {
                    UITools.detachChildren((Component)getInfoSlotContainer());
                    if(getInfoSlotRenderer() != null)
                    {
                        getInfoSlotRenderer().render((Component)getInfoSlotContainer());
                    }
                }
                catch(Exception e)
                {
                    LOG.warn("Could not update info slot (Reason: " + e.getMessage() + ").", e);
                }
            }
        }
    }


    public void setContentSlotContainer(HtmlBasedComponent contentSlotContainer)
    {
        if(this.contentSlotContainer != contentSlotContainer)
        {
            this.contentSlotContainer = contentSlotContainer;
            updateContentSlotContainer();
        }
    }


    public HtmlBasedComponent getContentSlotContainer()
    {
        return this.contentSlotContainer;
    }


    public void setInfoSlotContainer(HtmlBasedComponent infoSlotContainer)
    {
        if(this.infoSlotContainer != infoSlotContainer)
        {
            this.infoSlotContainer = infoSlotContainer;
            updateInfoSlotContainer();
        }
    }


    public HtmlBasedComponent getInfoSlotContainer()
    {
        return this.infoSlotContainer;
    }


    public void setContentSlotRenderer(SimpleRenderer contentSlotRenderer)
    {
        this.contentSlotRenderer = contentSlotRenderer;
        if(this.contentSlotRenderer != null && this.contentSlotRenderer instanceof AbstractNavigationAreaSectionRenderer)
        {
            ((AbstractNavigationAreaSectionRenderer)this.contentSlotRenderer).setNavigationArea((UINavigationArea)this);
        }
    }


    public SimpleRenderer getContentSlotRenderer()
    {
        return this.contentSlotRenderer;
    }


    public SimpleRenderer getInfoSlotRenderer()
    {
        return this.infoSlotRenderer;
    }


    public void setInfoSlotRenderer(SimpleRenderer infoSlotRenderer)
    {
        this.infoSlotRenderer = infoSlotRenderer;
        if(this.infoSlotRenderer != null && this.infoSlotRenderer instanceof AbstractNavigationAreaSectionRenderer)
        {
            ((AbstractNavigationAreaSectionRenderer)this.infoSlotRenderer).setNavigationArea((UINavigationArea)this);
        }
    }


    public String getContentSlotLabel()
    {
        return (this.contentSlotLabel == null) ? Labels.getLabel(this.contentSlotI3Label) : this.contentSlotLabel;
    }


    public void setContentSlotLabel(String contentSlotLabel)
    {
        this.contentSlotLabel = contentSlotLabel;
    }


    public void setContentSlotI3Label(String contentSlotLabel)
    {
        this.contentSlotI3Label = contentSlotLabel;
    }


    public String getInfoSlotLabel()
    {
        return (this.infoSlotLabel == null) ? Labels.getLabel(this.infoSlotI3Label) : this.infoSlotLabel;
    }


    public void setInfoSlotLabel(String infoSlotLabel)
    {
        this.infoSlotLabel = infoSlotLabel;
    }


    public void setInfoSlotI3Label(String infoSlotLabel)
    {
        this.infoSlotI3Label = infoSlotLabel;
    }


    public List<String> getInfoSlotAttachmentTypes()
    {
        return this.infoSlotAttachmentTypes;
    }


    public void setInfoSlotAttachmentTypes(List<String> infoSlotAttachmentTypes)
    {
        this.infoSlotAttachmentTypes = infoSlotAttachmentTypes;
    }


    public void setSelectedQuery(UIQuery query)
    {
        setSelectedQuery(query, false);
    }


    public void setSelectedQuery(UIQuery query, boolean doubleClicked)
    {
        if(query instanceof UISavedQuery)
        {
            setSelectedSavedQuery((UISavedQuery)query, doubleClicked);
        }
        else if(query instanceof UICollectionQuery)
        {
            setSelectedCollection((UICollectionQuery)query, doubleClicked);
        }
        else if(query instanceof UIDynamicQuery)
        {
            setSelectedDynamicQuery((UIDynamicQuery)query, doubleClicked);
        }
    }


    public void setSelectedSavedQuery(UISavedQuery query, boolean doubleClicked)
    {
        clearQuerySelection();
        this.selectedSavedQuery = query;
        if(doubleClicked)
        {
            fireSavedQueryDoubleClicked();
        }
        else
        {
            fireSavedQuerySelected();
        }
    }


    public void setSelectedCollection(UICollectionQuery selectedCollection, boolean doubleClicked)
    {
        clearQuerySelection();
        this.selectedCollection = selectedCollection;
        if(doubleClicked)
        {
            fireCollectionDoubleClicked();
        }
        else
        {
            fireCollectionSelected();
        }
    }


    public void setSelectedDynamicQuery(UIDynamicQuery selectedDynamicQuery, boolean doubleClicked)
    {
        clearQuerySelection();
        this.selectedDynamicQuery = selectedDynamicQuery;
        if(doubleClicked)
        {
            fireDynamicQueryDoubleClicked();
        }
        else
        {
            fireDynamicQuerySelected();
        }
    }


    public UISavedQuery getSelectedSavedQuery()
    {
        return this.selectedSavedQuery;
    }


    public UIDynamicQuery getSelectedDynamicQuery()
    {
        return this.selectedDynamicQuery;
    }


    public UICollectionQuery getSelectedCollection()
    {
        return this.selectedCollection;
    }


    protected void clearQuerySelection()
    {
        clearCachedSavedQueryConfiguration();
        this.selectedSavedQuery = null;
        this.selectedDynamicQuery = null;
        this.selectedCollection = null;
    }


    private void clearCachedSavedQueryConfiguration()
    {
        UIRole sessionRole = this.uiConfigurationService.getSessionRole();
        ObjectTemplate template = null;
        if(this.selectedSavedQuery != null && this.selectedSavedQuery.getSavedQuery() != null &&
                        !this.modelService.isRemoved(this.selectedSavedQuery.getSavedQuery()))
        {
            template = this.typeService.getObjectTemplate(this.selectedSavedQuery.getSavedQuery().getSelectedTypeCode());
            UIComponentCache.CacheKey cacheKey = new UIComponentCache.CacheKey((sessionRole != null) ? sessionRole.getName() : null, template, "listViewContentBrowser_" + this.selectedSavedQuery.getSavedQuery().getCode(), ListViewConfiguration.class);
            this.uiComponentCache.addComponentConfiguration(cacheKey, null);
        }
    }


    protected void fireSavedQueryDoubleClicked()
    {
        for(NavigationAreaListener listener : getListeners())
        {
            listener.savedQueryDoubleClicked();
        }
    }


    protected void fireDynamicQueryDoubleClicked()
    {
        for(NavigationAreaListener listener : getListeners())
        {
            listener.dynamicQueryDoubleClicked();
        }
    }


    protected void fireCollectionDoubleClicked()
    {
        for(NavigationAreaListener listener : getListeners())
        {
            listener.collectionDoubleClicked();
        }
    }


    protected void fireSavedQuerySelected()
    {
        for(NavigationAreaListener listener : getListeners())
        {
            listener.savedQuerySelected();
        }
    }


    protected void fireDynamicQuerySelected()
    {
        for(NavigationAreaListener listener : getListeners())
        {
            listener.dynamicQuerySelected();
        }
    }


    protected void fireCollectionSelected()
    {
        for(NavigationAreaListener listener : getListeners())
        {
            listener.collectionSelected();
        }
    }


    protected void fireCollectionChanged(ObjectCollection collection)
    {
        for(NavigationAreaListener listener : getListeners())
        {
            listener.collectionChanged(collection);
        }
    }


    protected void fireSavedQueryChanged(CockpitSavedQueryModel query)
    {
        for(NavigationAreaListener listener : getListeners())
        {
            listener.savedQueryChanged(query);
        }
    }


    protected void fireCollectionAdded(ObjectCollection collection)
    {
        for(NavigationAreaListener listener : getListeners())
        {
            listener.collectionAdded(collection);
        }
    }


    public int addToCollection(TypedObject item, UICollectionQuery collection, boolean multiDrag)
    {
        int number = 0;
        if(item != null && item.getObject() instanceof ItemModel)
        {
            List<TypedObject> selItems = getPerspective().getBrowserArea().getFocusedBrowser().getSelectedItems();
            List<TypedObject> itemsToAdd = new ArrayList<>();
            if(multiDrag && selItems.contains(item))
            {
                itemsToAdd.addAll(selItems);
            }
            else
            {
                itemsToAdd.add(item);
            }
            UICollectionQuery collQuery = collection;
            boolean newAdded = false;
            if(collQuery == null)
            {
                collQuery = ((AbstractNavigationAreaModel)getSectionModel()).addNewCollection();
                newAdded = true;
            }
            Notification notification = null;
            try
            {
                number = getObjectCollectionService().addToCollection(collQuery.getObjectCollection(), itemsToAdd);
                if(number == 0)
                {
                    notification = new Notification(Labels.getLabel("collection.collections"), Labels.getLabel("collection.cannot.add.items", (Object[])new String[] {collQuery
                                    .getLabel()}));
                }
            }
            catch(IllegalStateException ise)
            {
                String[] attrs = {(itemsToAdd.size() != 1) ? "s" : "", collQuery.getLabel(), ise.getMessage()};
                notification = new Notification(Labels.getLabel("general.error"), Labels.getLabel("collection.cannot.add.item", (Object[])attrs));
            }
            finally
            {
                if(notification != null && getPerspective().getNotifier() != null)
                {
                    getPerspective().getNotifier().setNotification(notification);
                }
            }
            if(newAdded)
            {
                fireCollectionAdded(collQuery.getObjectCollection());
            }
            else
            {
                fireCollectionChanged(collQuery.getObjectCollection());
            }
        }
        return number;
    }


    public TypeService getTypeService()
    {
        return this.typeService;
    }


    @Required
    public void setCockpitTypeService(TypeService typeService)
    {
        this.typeService = typeService;
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
    public void setSavedQueryService(SavedQueryService savedQueryService)
    {
        this.savedQueryService = savedQueryService;
    }


    @Required
    public void setUiComponentCache(UIComponentCache uiComponentCache)
    {
        this.uiComponentCache = uiComponentCache;
    }


    @Required
    public void setUiConfigurationService(UIConfigurationService uiConfigurationService)
    {
        this.uiConfigurationService = uiConfigurationService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public SavedQueryService getSavedQueryService()
    {
        return this.savedQueryService;
    }


    @Required
    public void setDynamicQueryService(DynamicQueryService dynamicQueryService)
    {
        this.dynamicQueryService = dynamicQueryService;
    }


    public DynamicQueryService getDynamicQueryService()
    {
        return this.dynamicQueryService;
    }


    public void removeQuery(UIQuery query)
    {
        if(query.equals(getSelectedCollection()) || (query instanceof UISavedQuery && ((UISavedQuery)query)
                        .getSavedQuery().equals(
                                        (getSelectedSavedQuery() == null) ? null : getSelectedSavedQuery().getSavedQuery())))
        {
            clearQuerySelection();
        }
        if(query instanceof UICollectionQuery)
        {
            List<BrowserModel> browsers = getPerspective().getBrowserArea().getBrowsers();
            boolean collectionInUse = false;
            for(BrowserModel browserModel : browsers)
            {
                if(browserModel instanceof CollectionBrowserModel)
                {
                    ObjectCollection b_collection = ((CollectionBrowserModel)browserModel).getCollection();
                    if(b_collection != null && b_collection.equals(((UICollectionQuery)query).getObjectCollection()))
                    {
                        collectionInUse = true;
                        break;
                    }
                }
            }
            if(collectionInUse)
            {
                getPerspective().getNotifier()
                                .setNotification(new Notification(Labels.getLabel("collection.remove.error_stillopen")));
            }
            else
            {
                getObjectCollectionService().removeCollection(((UICollectionQuery)query).getObjectCollection());
            }
        }
        if(query instanceof UISavedQuery)
        {
            getSavedQueryService().deleteQuery(((UISavedQuery)query).getSavedQuery());
        }
        ((AbstractNavigationAreaModel)getSectionModel()).removeQuery(query);
        update();
    }


    public void renameCollection(UICollectionQuery query, String label)
    {
        getObjectCollectionService().renameCollection(query.getObjectCollection(), label);
        ItemModel col = (ItemModel)UISessionUtils.getCurrentSession().getModelService().get(query.getObjectCollection().getPK());
        UISessionUtils.getCurrentSession().getModelService().detach(col);
        fireCollectionChanged(query.getObjectCollection());
    }


    public void publishCollection(UICollectionQuery query)
    {
        getObjectCollectionService().publishCollection(query.getObjectCollection());
        fireCollectionChanged(query.getObjectCollection());
    }


    public void renameSavedQuery(UISavedQuery query, String label)
    {
        getSavedQueryService().renameQuery(query.getSavedQuery(), label);
        fireSavedQueryChanged(query.getSavedQuery());
    }


    public void publishSavedQuery(UISavedQuery query)
    {
        getSavedQueryService().publishSavedQuery(query.getSavedQuery());
        fireSavedQueryChanged(query.getSavedQuery());
    }


    public void duplicateQuery(UIQuery query)
    {
        if(query instanceof UICollectionQuery)
        {
            getObjectCollectionService().cloneCollection(((UICollectionQuery)query).getObjectCollection(),
                            UISessionUtils.getCurrentSession().getUser());
        }
        update();
    }


    public Integer getSelectedIndex(String type)
    {
        return this.selectedIndexes.get(type);
    }


    public void setSelectedIndex(String type, int selectedIndex)
    {
        clearSelectedIndexes();
        this.selectedIndexes.put(type, Integer.valueOf(selectedIndex));
    }


    public void clearSelectedIndexes()
    {
        this.selectedIndexes.clear();
    }


    public void handlePasteOperation(String rawContent, UICollectionQuery collection)
    {
    }


    public void resetContext()
    {
        clearSelectedIndexes();
    }


    public String getLabel()
    {
        return null;
    }


    public boolean isPushInfoBox()
    {
        return this.pushInfoBox;
    }


    public void setPushInfoBox(boolean pushInfoBox)
    {
        this.pushInfoBox = pushInfoBox;
    }


    public void setPushInfoBoxEventClassName(String evtClassName)
    {
        this.pushInfoBoxEventClassName = evtClassName;
    }


    public String getPushInfoBoxEventClassName()
    {
        return this.pushInfoBoxEventClassName;
    }


    public void onCockpitEvent(CockpitEvent event)
    {
        super.onCockpitEvent(event);
        if(isPushInfoBox())
        {
            if(StringUtils.isBlank(getPushInfoBoxEventClassName()))
            {
                LOG.warn("Asynchronous update of info box enabled, but no event class specified. Disabling on-demand updating of info box...");
                this.pushInfoBox = false;
                updateInfoSlotContainer();
            }
            else
            {
                try
                {
                    Class<?> evtClass = Class.forName(getPushInfoBoxEventClassName());
                    if(evtClass.isAssignableFrom(event.getClass()))
                    {
                        updateInfoSlotContainer();
                    }
                }
                catch(ClassNotFoundException e)
                {
                    LOG.warn(e.getMessage(), e);
                }
            }
        }
    }
}
