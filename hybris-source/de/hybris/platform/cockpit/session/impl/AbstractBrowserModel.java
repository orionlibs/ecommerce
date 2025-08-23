package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.components.notifier.Notification;
import de.hybris.platform.cockpit.err.CockpitMarkAllException;
import de.hybris.platform.cockpit.model.listview.MutableColumnModel;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.BrowserFilter;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.BrowserModelListener;
import de.hybris.platform.cockpit.session.UIBrowserArea;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.cockpit.util.UITools;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;

public abstract class AbstractBrowserModel implements BrowserModel
{
    private static final Logger LOG = LoggerFactory.getLogger(AbstractBrowserModel.class);
    private UIBrowserArea browserArea = null;
    private String label = null;
    private String extendedLabel = null;
    protected ObjectTemplate rootType = null;
    private BrowserFilter browserFilter = null;
    private final Set<BrowserFilter> availableBrowserFilters = new LinkedHashSet<>();
    private final transient List<Integer> selectedIndexes = new ArrayList<>();
    private boolean allMarked = false;
    protected final transient List<BrowserModelListener> browserListeners = new ArrayList<>();
    private int totalCount;
    protected MutableColumnModel cacheView = null;
    private BrowserFilter browserFilterFixed;
    public static final String VIEWMODE_LIST = "LIST";
    public static final String VIEWMODE_GRID = "GRID";


    public boolean isAdvancedHeaderDropdownSticky()
    {
        return false;
    }


    public boolean isAdvancedHeaderDropdownVisible()
    {
        return false;
    }


    public boolean isDuplicatable()
    {
        return true;
    }


    public void focus()
    {
        if(getArea() == null)
        {
            LOG.warn("Can not give browser focus since no browser area has been set.");
        }
        else
        {
            getArea().setFocusedBrowser(this);
        }
    }


    public boolean isFocused()
    {
        boolean focused = false;
        if(getArea() != null)
        {
            focused = equals(getArea().getFocusedBrowser());
        }
        return focused;
    }


    public void setArea(UIBrowserArea area)
    {
        this.browserArea = area;
    }


    public UIBrowserArea getArea()
    {
        return this.browserArea;
    }


    public void setExtendedLabel(String label)
    {
        this.extendedLabel = label;
    }


    public String getExtendedLabel()
    {
        String extLabel = this.label;
        if(this.extendedLabel != null && this.extendedLabel.length() > 0)
        {
            extLabel = this.extendedLabel;
        }
        return extLabel;
    }


    public void setLabel(String label)
    {
        this.label = label;
    }


    public String getLabel()
    {
        return this.label;
    }


    public void setRootType(ObjectTemplate rootType)
    {
        if((this.rootType == null && rootType != null) || !this.rootType.equals(rootType))
        {
            this.rootType = rootType;
            fireRootTypeChanged();
        }
    }


    public ObjectTemplate getRootType()
    {
        if(this.rootType == null)
        {
            if(getItems() != null && !getItems().isEmpty())
            {
                this
                                .rootType = UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate(getItem(0).getType().getCode());
            }
            else
            {
                this.rootType = UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate("Item");
            }
        }
        return this.rootType;
    }


    public List<Integer> getSelectedIndexes()
    {
        return Collections.unmodifiableList(this.selectedIndexes);
    }


    public void setSelectedIndexes(List<Integer> selectedIndexes)
    {
        this.selectedIndexes.clear();
        if(selectedIndexes != null)
        {
            this.selectedIndexes.addAll(selectedIndexes);
        }
        fireSelectionChanged();
    }


    public void addBrowserModelListener(BrowserModelListener listener)
    {
        if(!this.browserListeners.contains(listener) && listener != null)
        {
            this.browserListeners.add(listener);
        }
    }


    public void removeBrowserModelListener(BrowserModelListener listener)
    {
        if(this.browserListeners.contains(listener))
        {
            this.browserListeners.remove(listener);
        }
    }


    public BrowserFilter getBrowserFilterFixed()
    {
        return this.browserFilterFixed;
    }


    public void clearSelection()
    {
        if(this.selectedIndexes != null && !this.selectedIndexes.isEmpty())
        {
            this.selectedIndexes.clear();
        }
    }


    public abstract void collapse();


    public abstract boolean isCollapsed();


    public abstract void updateItems();


    public abstract TypedObject getItem(int paramInt);


    public abstract List<TypedObject> getItems();


    public List<TypedObject> getSelectedItems()
    {
        List<TypedObject> ret = new ArrayList<>();
        if(isAllMarked())
        {
            return ret;
        }
        for(Integer i : this.selectedIndexes)
        {
            try
            {
                ret.add(getItems().get(i.intValue()));
            }
            catch(IndexOutOfBoundsException ioobe)
            {
                LOG.error("Result list and selection list are not consistent", ioobe);
            }
        }
        return ret;
    }


    public void setTotalCount(int totalCount)
    {
        this.totalCount = totalCount;
    }


    public int getTotalCount()
    {
        return this.totalCount;
    }


    public boolean isAllMarked()
    {
        return this.allMarked;
    }


    public void setAllMarked(boolean allMarked) throws CockpitMarkAllException
    {
        Integer defaultMaxMultiEditItems = Integer.valueOf(UITools.getCockpitParameter("default.maxMultiEditItems",
                        Executions.getCurrent()));
        if((allMarked && getTotalCount() <= defaultMaxMultiEditItems.intValue()) || !allMarked)
        {
            this.allMarked = allMarked;
        }
        else
        {
            Notification notification = new Notification(Labels.getLabel("multiedit.select.maxItemsReached", (Object[])new Integer[] {defaultMaxMultiEditItems}));
            BaseUICockpitPerspective basePerspective = (BaseUICockpitPerspective)getArea().getPerspective();
            if(basePerspective.getNotifier() != null)
            {
                basePerspective.getNotifier().setNotification(notification);
            }
            throw new CockpitMarkAllException("MarkAll: User selected more than allowed number of items.");
        }
    }


    public void multiEdit(PropertyDescriptor propertyDesc, List<TypedObject> objects, Object value)
    {
        multiEdit(propertyDesc, UISessionUtils.getCurrentSession().getSystemService().getCurrentLanguage().getIsocode(), objects, value);
    }


    public void multiEdit(PropertyDescriptor propertyDesc, String languageIso, List<TypedObject> objects, Object value)
    {
        List<String> errors = TypeTools.multiEdit(propertyDesc, languageIso, objects, value);
        if(!errors.isEmpty())
        {
            Notification notification = new Notification(errors);
            notification.setMessage(Labels.getLabel("multiedit.error.count", (Object[])new Integer[] {Integer.valueOf(errors.size())}));
            getArea().getPerspective().getNotifier().setDialogNotification(notification);
        }
    }


    public abstract Object clone() throws CloneNotSupportedException;


    protected void fireSelectionChanged()
    {
        for(BrowserModelListener bl : this.browserListeners)
        {
            bl.selectionChanged(this);
        }
    }


    protected void fireItemsChanged()
    {
        for(BrowserModelListener bl : this.browserListeners)
        {
            bl.itemsChanged(this);
        }
    }


    protected void fireChanged()
    {
        for(BrowserModelListener bl : this.browserListeners)
        {
            bl.changed(this);
        }
    }


    protected void fireRootTypeChanged()
    {
        for(BrowserModelListener bl : this.browserListeners)
        {
            bl.rootTypeChanged(this);
        }
    }


    public boolean hasStatusBar()
    {
        return false;
    }


    public void onShow()
    {
    }


    public void onHide()
    {
    }


    public void onClose()
    {
    }


    public MutableColumnModel getCacheView()
    {
        return this.cacheView;
    }


    public void setCacheView(MutableColumnModel cacheView)
    {
        this.cacheView = cacheView;
    }


    public void setBrowserFilter(BrowserFilter browserFilter)
    {
        this.browserFilter = browserFilter;
    }


    public BrowserFilter getBrowserFilter()
    {
        return this.browserFilter;
    }


    public Set<BrowserFilter> getAvailableBrowserFilters()
    {
        return this.availableBrowserFilters;
    }


    public void setBrowserFilterFixed(BrowserFilter browserFilterFixed)
    {
        this.browserFilterFixed = browserFilterFixed;
        this.browserFilter = browserFilterFixed;
    }
}
