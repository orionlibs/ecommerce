package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.components.contentbrowser.MainAreaComponentFactory;
import de.hybris.platform.cockpit.model.listview.MutableTableModel;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.ListViewConfiguration;
import de.hybris.platform.cockpit.services.config.UIConfigurationService;
import de.hybris.platform.cockpit.services.config.UIRole;
import de.hybris.platform.cockpit.session.AdvancedBrowserModel;
import de.hybris.platform.cockpit.session.AdvancedBrowserModelListener;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.BrowserModelListener;
import de.hybris.platform.cockpit.session.UIBrowserArea;
import de.hybris.platform.cockpit.session.UISession;
import de.hybris.platform.cockpit.session.UISessionUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractAdvancedBrowserModel extends AbstractBrowserModel implements AdvancedBrowserModel
{
    private static final Logger LOG = LoggerFactory.getLogger(AbstractAdvancedBrowserModel.class);
    public static final String LIST_VIEW_CONFIG_CODE = "listViewContentBrowserContext";
    private final transient List<Integer> selectedContextIndexes = new ArrayList<>();
    protected final transient List<TypedObject> contextItems = new ArrayList<>();
    private CompareModel compareModel;
    private ObjectTemplate contextRootType = null;
    private MutableTableModel tableModel = null;
    protected String viewMode = null;
    private boolean itemsMovable = false;
    private boolean itemsRemovable = false;
    private MutableTableModel contextTableModel = null;
    private boolean contextVisible = false;
    private String contextViewMode = "LIST";
    private TypedObject contextRootItem = null;
    private PropertyDescriptor contextRootTypePropertyDescriptor = null;
    private boolean contextItemsMovable = false;
    private boolean contextItemsRemovable = false;
    private Map<String, String> contextInitialValueMapping;


    public AbstractAdvancedBrowserModel()
    {
        this(UISessionUtils.getCurrentSession());
    }


    public AbstractAdvancedBrowserModel(UISession currentSession)
    {
        if(currentSession != null && currentSession.getCurrentPerspective() != null && currentSession
                        .getCurrentPerspective().getBrowserArea() != null)
        {
            UIRole currentRole = currentSession.getUiConfigurationService().getSessionRole();
            if(currentRole != null)
            {
                String mode = (String)currentSession.getCurrentPerspective().getBrowserArea().getDefaultBrowserViewMapping().get(currentRole.getName());
                if(mode != null)
                {
                    this.viewMode = mode;
                }
            }
        }
    }


    public void doDrop(TypedObject item, BrowserModel sourceBrowser)
    {
        Set<TypedObject> draggedItems = new HashSet<>();
        draggedItems.add(item);
        if(sourceBrowser.getSelectedIndexes().size() > 1)
        {
            draggedItems.addAll(sourceBrowser.getSelectedItems());
        }
        fireItemsDropped(draggedItems);
    }


    public ObjectTemplate getLastType()
    {
        return getRootType();
    }


    public TypedObject getActiveItem()
    {
        TypedObject activeItem = null;
        if(getArea() == null)
        {
            LOG.warn("Can not get active item (Reason: No browser area has been set).");
        }
        else if(getArea().getPerspective() == null)
        {
            LOG.warn("Can not get active item (Reason: No perspective has been set).");
        }
        else
        {
            activeItem = getArea().getPerspective().getActiveItem();
        }
        return activeItem;
    }


    public void setContextRootTypePropertyDescriptor(PropertyDescriptor descriptor)
    {
        this.contextRootTypePropertyDescriptor = descriptor;
    }


    public PropertyDescriptor getContextRootTypePropertyDescriptor()
    {
        return this.contextRootTypePropertyDescriptor;
    }


    public ObjectTemplate getContextRootType()
    {
        return this.contextRootType;
    }


    public MutableTableModel getContextTableModel()
    {
        return this.contextTableModel;
    }


    public String getContextViewMode()
    {
        return this.contextViewMode;
    }


    public List<Integer> getSelectedContextIndexes()
    {
        return Collections.unmodifiableList(this.selectedContextIndexes);
    }


    public TypedObject getContextRootItem()
    {
        return this.contextRootItem;
    }


    public MutableTableModel getTableModel()
    {
        return this.tableModel;
    }


    public String getViewMode()
    {
        return this.viewMode;
    }


    public List<? extends MainAreaComponentFactory> getAvailableViewModes()
    {
        return Collections.EMPTY_LIST;
    }


    public boolean isContextItemsMovable()
    {
        return this.contextItemsMovable;
    }


    public boolean isContextItemsRemovable()
    {
        return this.contextItemsRemovable;
    }


    public boolean isContextVisible()
    {
        return this.contextVisible;
    }


    public boolean isItemsMovable()
    {
        return this.itemsMovable;
    }


    public boolean isItemsRemovable()
    {
        return this.itemsRemovable;
    }


    public void setActiveItem(TypedObject activeItem)
    {
        if((getActiveItem() == null && activeItem != null) || (
                        getActiveItem() != null && !getActiveItem().equals(activeItem)))
        {
            fireItemActivated(activeItem);
        }
    }


    public void setContextItemsMovable(boolean movable)
    {
        this.contextItemsMovable = movable;
    }


    public void setContextItemsRemovable(boolean removable)
    {
        this.contextItemsRemovable = removable;
    }


    public void setContextRootType(ObjectTemplate contextRootType)
    {
        this.contextRootType = contextRootType;
    }


    public void setContextTableModel(MutableTableModel contextTableModel)
    {
        this.contextTableModel = contextTableModel;
    }


    public void setContextViewMode(String viewMode)
    {
        if((this.contextViewMode == null && viewMode != null) || !this.contextViewMode.equals(viewMode))
        {
            this.contextViewMode = viewMode;
            fireContextViewModeChanged();
        }
    }


    public void setContextVisible(boolean contextVisible)
    {
        if(this.contextVisible != contextVisible)
        {
            this.contextVisible = contextVisible;
            fireContextVisibilityChanged();
        }
    }


    public void setContextVisibleDirect(boolean contextVisible)
    {
        this.contextVisible = contextVisible;
    }


    public void setItemsMovable(boolean movable)
    {
        this.itemsMovable = movable;
    }


    public void setItemsRemovable(boolean removable)
    {
        this.itemsRemovable = removable;
    }


    public void setSelectedContextIndexes(List<Integer> selectedContextIndexes)
    {
        this.selectedContextIndexes.clear();
        if(selectedContextIndexes != null)
        {
            this.selectedContextIndexes.addAll(selectedContextIndexes);
        }
        fireContextSelectionChanged();
    }


    public void setTableModel(MutableTableModel tableModel)
    {
        this.tableModel = tableModel;
    }


    public void setViewMode(String viewMode)
    {
        if((this.viewMode == null && viewMode != null) || !this.viewMode.equals(viewMode))
        {
            this.compareModel = new CompareModel(getSelectedItems());
            this.viewMode = viewMode;
            fireViewModeChanged();
        }
    }


    public void setContextItems(TypedObject item, Collection<TypedObject> contextItems, ObjectTemplate asType)
    {
        setContextItemsDirectly(item, contextItems);
        if(asType != null)
        {
            this.contextRootType = asType;
        }
        fireContextItemsChanged(false);
    }


    public void setContextItems(TypedObject item, Collection<TypedObject> contextItems)
    {
        ObjectTemplate type = this.contextRootType;
        setContextItemsDirectly(item, contextItems);
        fireContextItemsChanged(false);
        if(type != null && !type.equals(this.contextRootType))
        {
            fireContextRootTypeChanged();
        }
    }


    public void setContextItemsDirectly(TypedObject item, Collection<TypedObject> contextItems)
    {
        this.contextRootItem = item;
        if(this.contextItems != null && !this.contextItems.equals(contextItems))
        {
            setSelectedContextIndexes(null);
            if(getContextTableModel() != null && getContextTableModel().getListComponentModel() != null)
            {
                getContextTableModel().getListComponentModel().setSelectedIndexes(null);
            }
        }
        this.contextItems.clear();
        if(contextItems != null)
        {
            this.contextItems.addAll(contextItems);
        }
        ObjectTemplate type = null;
        if(!this.contextItems.isEmpty() && !isPossibleAddContextSubTypes())
        {
            type = UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate(((TypedObject)this.contextItems.get(0)).getType().getCode());
            this.contextRootType = type;
        }
    }


    protected boolean isPossibleAddContextSubTypes()
    {
        boolean ret = false;
        UIBrowserArea browserArea = getArea();
        if(browserArea instanceof AbstractBrowserArea)
        {
            ret = ((AbstractBrowserArea)browserArea).isPossibleAddContextSubTypes();
        }
        return ret;
    }


    public List<TypedObject> getContextItems()
    {
        AdvancedBrowserModel focusedBrowser = (AdvancedBrowserModel)UISessionUtils.getCurrentSession().getCurrentPerspective().getBrowserArea().getFocusedBrowser();
        List<TypedObject> contextItems = new ArrayList<>(this.contextItems);
        if(getContextRootType() != null && focusedBrowser.getContextRootItem() != null)
        {
            ListViewConfiguration listConfig = getListViewConfiguration(UISessionUtils.getCurrentSession().getTypeService()
                            .getObjectTemplate(getContextRootType().getCode()), "listViewContentBrowserContext");
            if(listConfig.isAllowCreateInlineItems() &&
                            isContextPropertyWritable() &&
                            UISessionUtils.getCurrentSession().getSystemService()
                                            .checkPermissionOn(getContextRootType().getBaseType().getCode(), "create"))
            {
                Object model = UISessionUtils.getCurrentSession().getModelService().create(getContextRootType().getBaseType().getCode());
                TypedObject typedObject = UISessionUtils.getCurrentSession().getTypeService().wrapItem(model);
                contextItems.add(typedObject);
            }
        }
        return contextItems;
    }


    protected boolean isContextPropertyWritable()
    {
        ObjectTemplate contextRootType = getContextRootType();
        PropertyDescriptor contextPropertyDescriptor = getContextRootTypePropertyDescriptor();
        if(contextRootType != null && contextPropertyDescriptor != null)
        {
            boolean canWrite = true;
            TypedObject typedObject = getContextRootItem();
            if(typedObject == null)
            {
                canWrite = UISessionUtils.getCurrentSession().getUiAccessRightService().isWritable(
                                UISessionUtils.getCurrentSession().getTypeService()
                                                .getObjectTypeFromPropertyQualifier(contextPropertyDescriptor.getQualifier()), contextPropertyDescriptor, false);
            }
            else
            {
                canWrite = UISessionUtils.getCurrentSession().getUiAccessRightService().isWritable((ObjectType)typedObject.getType(), typedObject, contextPropertyDescriptor, false);
            }
            if(!canWrite)
            {
                return false;
            }
        }
        return true;
    }


    public void collapse()
    {
        setContextVisible(false);
    }


    public boolean isCollapsed()
    {
        return !isContextVisible();
    }


    public void addBrowserModelListener(BrowserModelListener listener)
    {
        if(listener instanceof AdvancedBrowserModelListener)
        {
            super.addBrowserModelListener(listener);
        }
        else
        {
            LOG.warn("Listener not registered (Reason: Not of type 'AdvancedBrowserModelListener').");
        }
    }


    protected void fireItemActivated(TypedObject item)
    {
        for(BrowserModelListener listener : this.browserListeners)
        {
            if(listener instanceof AdvancedBrowserModelListener)
            {
                ((AdvancedBrowserModelListener)listener).itemActivated(item);
            }
        }
    }


    protected void fireViewModeChanged()
    {
        for(BrowserModelListener listener : this.browserListeners)
        {
            if(listener instanceof AdvancedBrowserModelListener)
            {
                ((AdvancedBrowserModelListener)listener).viewModeChanged(this);
            }
        }
    }


    protected void fireContextViewModeChanged()
    {
        for(BrowserModelListener listener : this.browserListeners)
        {
            if(listener instanceof AdvancedBrowserModelListener)
            {
                ((AdvancedBrowserModelListener)listener).contextViewModeChanged(this);
            }
        }
    }


    protected void fireContextRootTypeChanged()
    {
        for(BrowserModelListener listener : this.browserListeners)
        {
            if(listener instanceof AdvancedBrowserModelListener)
            {
                ((AdvancedBrowserModelListener)listener).contextRootTypeChanged(this);
            }
        }
    }


    protected void fireContextSelectionChanged()
    {
        for(BrowserModelListener listener : this.browserListeners)
        {
            if(listener instanceof AdvancedBrowserModelListener)
            {
                ((AdvancedBrowserModelListener)listener).contextSelectionChanged(this);
            }
        }
    }


    protected void fireContextVisibilityChanged()
    {
        for(BrowserModelListener listener : this.browserListeners)
        {
            if(listener instanceof AdvancedBrowserModelListener)
            {
                ((AdvancedBrowserModelListener)listener).contextVisibilityChanged(this);
            }
        }
    }


    protected void fireContextItemsChanged(boolean cleanContextHeader)
    {
        for(BrowserModelListener listener : this.browserListeners)
        {
            if(listener instanceof DefaultSearchContextBrowserModelListener)
            {
                ((DefaultSearchContextBrowserModelListener)listener).contextItemsChanged(this, cleanContextHeader);
                continue;
            }
            if(listener instanceof AdvancedBrowserModelListener)
            {
                ((AdvancedBrowserModelListener)listener).contextItemsChanged(this);
            }
        }
    }


    protected void fireItemsDropped(Collection<TypedObject> items)
    {
        for(BrowserModelListener listener : this.browserListeners)
        {
            if(listener instanceof AdvancedBrowserModelListener)
            {
                ((AdvancedBrowserModelListener)listener).itemsDropped(this, items);
            }
        }
    }


    public Map<String, String> getContextInitialValueMapping()
    {
        return this.contextInitialValueMapping;
    }


    public void setContextInitialValueMapping(Map<String, String> contextInitialValueMapping)
    {
        this.contextInitialValueMapping = contextInitialValueMapping;
    }


    protected ListViewConfiguration getListViewConfiguration(ObjectTemplate objectTemplate, String code)
    {
        UIConfigurationService uiConfigService = UISessionUtils.getCurrentSession().getUiConfigurationService();
        return (ListViewConfiguration)uiConfigService.getComponentConfiguration(objectTemplate, code, ListViewConfiguration.class);
    }


    public void setCompareModel(CompareModel compareModel)
    {
        this.compareModel = compareModel;
    }


    public CompareModel getCompareModel()
    {
        if(this.compareModel == null)
        {
            this.compareModel = new CompareModel(getSelectedItems());
        }
        return this.compareModel;
    }
}
