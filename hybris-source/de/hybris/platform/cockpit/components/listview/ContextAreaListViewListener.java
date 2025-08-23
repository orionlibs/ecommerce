package de.hybris.platform.cockpit.components.listview;

import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.impl.ItemChangedEvent;
import de.hybris.platform.cockpit.model.general.MutableListModel;
import de.hybris.platform.cockpit.model.listview.MutableTableModel;
import de.hybris.platform.cockpit.model.listview.UIListView;
import de.hybris.platform.cockpit.model.listview.impl.DefaultBrowserListViewListener;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.BaseConfiguration;
import de.hybris.platform.cockpit.services.config.InitialPropertyConfiguration;
import de.hybris.platform.cockpit.services.config.UIConfigurationService;
import de.hybris.platform.cockpit.services.meta.PropertyService;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.cockpit.services.values.ValueHandlerNotPersistedException;
import de.hybris.platform.cockpit.session.AdvancedBrowserModel;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.AbstractPageableBrowserModel;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.cockpit.util.UITools;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Messagebox;

public class ContextAreaListViewListener extends DefaultBrowserListViewListener
{
    private static final Logger LOG = LoggerFactory.getLogger(ContextAreaListViewListener.class);
    private boolean creating = false;
    private final List<Integer> editableColumns = new ArrayList<>();
    private int editIndex = 0;
    private int editRowIndex = 0;
    protected final UIListView view;
    private TypeService typeService;
    private UIConfigurationService uiConfigService;
    private PropertyService propertyService;


    public ContextAreaListViewListener(BrowserModel browser, MutableTableModel model, UIListView view)
    {
        super(browser, model);
        this.view = view;
    }


    protected boolean isCreatingLine(int rowIndex)
    {
        boolean ret = false;
        try
        {
            List<? extends TypedObject> elements = this.model.getListComponentModel().getListModel().getElements();
            ret = (elements.size() == rowIndex + 1 && UISessionUtils.getCurrentSession().getModelService().isNew(((TypedObject)elements.get(rowIndex)).getObject()));
        }
        catch(Exception e)
        {
            LOG.error(e.getMessage(), e);
        }
        return ret;
    }


    public void changeCellValue(int columnIndex, int rowIndex, Object data)
    {
        if(isCreatingItem() && this.editRowIndex >= 0 && !this.editableColumns.isEmpty())
        {
            if(this.editIndex < this.editableColumns.size() && this.editableColumns.get(this.editIndex) != null && ((Integer)this.editableColumns
                            .get(this.editIndex)).intValue() == columnIndex)
            {
                LOG.info("CREATION MODE: change cell value (" + columnIndex + ", " + rowIndex + "): '" + (
                                (data == null) ? null : data.toString()) + "'");
                if(++this.editIndex < this.editableColumns.size())
                {
                    this.view.editCellAt(((Integer)this.editableColumns.get(this.editIndex)).intValue(), this.editRowIndex);
                }
                else
                {
                    setCreatingItem(false);
                }
            }
        }
        else
        {
            try
            {
                this.model.setValueAt(columnIndex, rowIndex, data);
            }
            catch(ValueHandlerNotPersistedException e)
            {
                if(!isCreatingLine(rowIndex))
                {
                    LOG.error(e.getMessage(), (Throwable)e);
                }
            }
            catch(ValueHandlerException e)
            {
                LOG.error("Could not set the value '" + data + "' for the cell in column '" + columnIndex + "' (Reason: " + e
                                .getMessage() + ").", (Throwable)e);
            }
        }
    }


    public void setCreatingItem(boolean creating)
    {
        this.creating = creating;
        this.editIndex = 0;
        if(this.editableColumns.isEmpty())
        {
            LOG.warn("Can not create item (Reason: Column indexes have not been specified).");
        }
        else if(this.editRowIndex >= 0)
        {
            this.view.editCellAt(((Integer)this.editableColumns.get(this.editIndex)).intValue(), this.editRowIndex);
        }
        else
        {
            LOG.warn("Can not create item (Reason: Row index not specified).");
        }
    }


    public boolean isCreatingItem()
    {
        return this.creating;
    }


    public void setEditIndexes(List<Integer> editableColumns, int rowIndex)
    {
        this.editRowIndex = rowIndex;
        this.editableColumns.clear();
        if(editableColumns != null)
        {
            this.editableColumns.addAll(editableColumns);
            Collections.sort(this.editableColumns);
        }
    }


    public void activate(List<Integer> indexes)
    {
        MutableListModel listComponentModel = this.model.getListComponentModel();
        List<? extends TypedObject> elements = listComponentModel.getListModel().getElements();
        List<TypedObject> activeItems = new ArrayList<>();
        for(Integer index : indexes)
        {
            if(index.intValue() >= 0 && index.intValue() < elements.size())
            {
                activeItems.add(elements.get(index.intValue()));
            }
        }
        if(!activeItems.isEmpty())
        {
            TypedObject activeItem = activeItems.iterator().next();
            if(UISessionUtils.getCurrentSession().getModelService().isNew(activeItem.getObject()))
            {
                TypedObject rootItem = ((AdvancedBrowserModel)this.browser.getArea().getFocusedBrowser()).getContextRootItem();
                ObjectTemplate template = getTypeService().getObjectTemplate(activeItem.getType().getCode());
                BaseConfiguration baseConfiguration = (BaseConfiguration)getUiConfigurationService().getComponentConfiguration(template, "base", BaseConfiguration.class);
                InitialPropertyConfiguration initialPropertyConfig = baseConfiguration.getInitialPropertyConfiguration(
                                getTypeService().getObjectTemplate(rootItem.getType().getCode()), getTypeService());
                Map<String, Object> initialValues = TypeTools.getAllAttributes(activeItem);
                Map<String, Object> configValues = new HashMap<>();
                if(initialPropertyConfig != null)
                {
                    Map<String, Object> configValuesTemp = initialPropertyConfig.getInitialProperties(rootItem, getTypeService());
                    if(configValuesTemp != null)
                    {
                        for(Map.Entry<String, Object> entry : configValuesTemp.entrySet())
                        {
                            PropertyDescriptor propertyDescriptor = getTypeService().getPropertyDescriptor(entry.getKey());
                            if(propertyDescriptor.isLocalized())
                            {
                                Map<String, Object> locMap = new HashMap<>(1);
                                locMap.put(UISessionUtils.getCurrentSession().getGlobalDataLanguageIso(), entry.getValue());
                                configValues.put(entry.getKey(), locMap);
                                continue;
                            }
                            configValues.put(entry.getKey(), entry.getValue());
                        }
                    }
                }
                this.browser.getArea().getPerspective()
                                .createItemInPopupEditor((ObjectType)template, mergeMaps(initialValues, configValues), (BrowserModel)this.browser, false);
            }
            else
            {
                super.activate(indexes);
            }
        }
    }


    protected UIConfigurationService getUiConfigurationService()
    {
        if(this.uiConfigService == null)
        {
            this.uiConfigService = UISessionUtils.getCurrentSession().getUiConfigurationService();
        }
        return this.uiConfigService;
    }


    protected PropertyService getPropertyService()
    {
        if(this.propertyService == null)
        {
            this.propertyService = (PropertyService)SpringUtil.getBean("cockpitPropertyService");
        }
        return this.propertyService;
    }


    protected TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = UISessionUtils.getCurrentSession().getTypeService();
        }
        return this.typeService;
    }


    private Map mergeMaps(Map<?, ?> map1, Map<?, ?> map2)
    {
        Map<Object, Object> retval = new HashMap<>(calcCapacity(map1.size() + map2.size()));
        retval.putAll(map1);
        retval.putAll(map2);
        return retval;
    }


    private final int calcCapacity(int size)
    {
        return (size * 4 + 3) / 3;
    }


    public void move(int fromIndex, int toIndex)
    {
        changeCollection(Collections.singleton(Integer.valueOf(fromIndex)), toIndex, false);
    }


    public void remove(Collection<Integer> indexes)
    {
        changeCollection(indexes, -1, true);
    }


    private void changeCollection(Collection<Integer> indexes, int toIndex, boolean remove)
    {
        TypedObject rootItem = this.browser.getContextRootItem();
        PropertyDescriptor propertyDescriptor = this.browser.getContextRootTypePropertyDescriptor();
        String cockpitParameter = UITools.getCockpitParameter("default.browserContextArea.allowChanges",
                        Executions.getCurrent());
        if((cockpitParameter == null || "true".equalsIgnoreCase(cockpitParameter.trim())) && propertyDescriptor != null && rootItem != null)
        {
            if(propertyDescriptor.isLocalized())
            {
                LOG.warn("Localized collections not yet supported, reordering ignored.");
            }
            else if(UISessionUtils.getCurrentSession().getUiAccessRightService()
                            .isWritable((ObjectType)rootItem.getType(), rootItem, propertyDescriptor, false))
            {
                try
                {
                    int pagingOffset = 0;
                    if(this.browser instanceof AbstractPageableBrowserModel && ((AbstractPageableBrowserModel)this.browser)
                                    .getContextItemsPageIndex() > 0)
                    {
                        pagingOffset = ((AbstractPageableBrowserModel)this.browser).getContextItemsPageIndex() * ((AbstractPageableBrowserModel)this.browser).getContextItemsPageSize();
                    }
                    List<TypedObject> contextItems = new ArrayList<>(this.browser.getContextItems());
                    removeNotPersistedItems(contextItems);
                    if(remove)
                    {
                        boolean isPartOf = getPropertyService().isPartof(propertyDescriptor);
                        if(isPartOf &&
                                        Messagebox.show(Labels.getLabel("dialog.removeItems.partof.message"),
                                                        Labels.getLabel("dialog.confirmRemove.title"), 48, "z-msgbox z-msgbox-exclamation") != 16)
                        {
                            return;
                        }
                        if(!isPartOf &&
                                        Messagebox.show(Labels.getLabel("dialog.removeItems.message"),
                                                        Labels.getLabel("dialog.confirmRemove.title"), 48, null) != 16)
                        {
                            return;
                        }
                        int[] sortedIntegerArray = getSortedIntegerArray(indexes);
                        for(int i = sortedIntegerArray.length - 1; i >= 0; i--)
                        {
                            contextItems.remove(sortedIntegerArray[i] + pagingOffset);
                        }
                    }
                    else
                    {
                        contextItems.add(toIndex + pagingOffset, contextItems
                                        .remove(((Integer)indexes.iterator().next()).intValue() + pagingOffset));
                    }
                    TypeTools.multiEdit(propertyDescriptor, null, Collections.singletonList(rootItem), contextItems);
                    UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new ItemChangedEvent(this, rootItem,
                                    Collections.singletonList(propertyDescriptor)));
                    this.browser.setContextItems(rootItem, contextItems);
                }
                catch(Exception e)
                {
                    LOG.error("Could not change collection.", e);
                }
            }
        }
    }


    private void removeNotPersistedItems(List<TypedObject> items)
    {
        Iterator<TypedObject> iterator = items.iterator();
        while(iterator.hasNext())
        {
            if(UISessionUtils.getCurrentSession().getModelService().isNew(((TypedObject)iterator.next()).getObject()))
            {
                iterator.remove();
            }
        }
    }


    private int[] getSortedIntegerArray(Collection<Integer> coll)
    {
        int[] ret = new int[coll.size()];
        int index = 0;
        for(Integer integer : coll)
        {
            ret[index] = integer.intValue();
            index++;
        }
        Arrays.sort(ret);
        return ret;
    }
}
