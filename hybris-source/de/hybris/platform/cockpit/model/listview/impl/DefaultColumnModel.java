package de.hybris.platform.cockpit.model.listview.impl;

import de.hybris.platform.cockpit.components.listeditor.DefaultListSelectionModel;
import de.hybris.platform.cockpit.components.listeditor.ListSelectionModel;
import de.hybris.platform.cockpit.model.editor.UIEditor;
import de.hybris.platform.cockpit.model.general.ListModel;
import de.hybris.platform.cockpit.model.listview.CellRenderer;
import de.hybris.platform.cockpit.model.listview.ColumnDescriptor;
import de.hybris.platform.cockpit.model.listview.ColumnGroup;
import de.hybris.platform.cockpit.model.listview.DynamicColumnProvider;
import de.hybris.platform.cockpit.model.listview.ListViewMenuPopupBuilder;
import de.hybris.platform.cockpit.model.listview.ValueHandler;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.ColumnConfiguration;
import de.hybris.platform.cockpit.services.config.ColumnGroupConfiguration;
import de.hybris.platform.cockpit.services.config.ListViewConfiguration;
import de.hybris.platform.cockpit.services.config.MutableColumnConfiguration;
import de.hybris.platform.cockpit.services.config.impl.InlineItemCreateButtonColumn;
import de.hybris.platform.cockpit.services.config.impl.PropertyColumnConfiguration;
import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.zkoss.spring.SpringUtil;

public class DefaultColumnModel extends AbstractColumnModel
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultColumnModel.class);
    protected static final String DEFAULT_WIDTH = "100%";
    private DefaultColumnGroup rootColumnGroup = null;
    private final ListSelectionModel selectionModel = (ListSelectionModel)new DefaultListSelectionModel();
    private final List<ColumnDescriptor> visibleColumns = new ArrayList<>();
    private final Map<ColumnDescriptor, ColumnConfiguration> columnMap = new HashMap<>();
    private final Map<ColumnDescriptor, ColumnDescriptor> localizedCol2ColMap = new HashMap<>();
    private final Map<ColumnDescriptor, List<ColumnDescriptor>> col2LocalizedColsMap = new HashMap<>();
    private boolean sortAscending = false;
    private int sortedColumnIndex = -1;
    private final ListViewConfiguration config;
    private final List<DynamicColumnProvider> dynamicColumnProviders;
    private CommonI18NService commonI18nService;


    public DefaultColumnModel(ListViewConfiguration config)
    {
        if(config == null)
        {
            throw new IllegalArgumentException("List view configuration can not be null");
        }
        this.config = config;
        this.dynamicColumnProviders = config.getDynamicColumnProviders();
        loadConfiguration();
    }


    public ListViewConfiguration getConfiguration()
    {
        return this.config;
    }


    public ColumnConfiguration getColumnConfiguration(ColumnDescriptor columnDescriptor)
    {
        return this.columnMap.get(columnDescriptor);
    }


    public void setSelectedIndex(int index)
    {
        if(setSelectedIndexDirectly(index))
        {
            fireColumnSelectionChanged(this.selectionModel.getAllSelectedIndexes());
        }
    }


    public boolean setSelectedIndexDirectly(int index)
    {
        boolean changed = false;
        if(this.selectionModel != null)
        {
            if(index >= 0 && index < this.visibleColumns.size() && !this.selectionModel.isSelectedIndex(index) &&
                            getVisibleColumn(index).isSelectable())
            {
                this.selectionModel.setSelectionInterval(index, index);
                changed = true;
            }
        }
        return changed;
    }


    public void setSelectedIndexes(List<Integer> indexes)
    {
        if(setSelectedIndexesDirectly(indexes))
        {
            fireColumnSelectionChanged(this.selectionModel.getAllSelectedIndexes());
        }
    }


    public boolean setSelectedIndexesDirectly(List<Integer> indexes)
    {
        boolean changed = false;
        if((indexes == null || indexes.isEmpty()) && this.selectionModel != null && this.selectionModel
                        .getMaxSelectionIndex() >= 0)
        {
            this.selectionModel.clearSelection();
            changed = true;
        }
        else if(this.selectionModel != null && indexes != null)
        {
            this.selectionModel.clearSelection();
            for(Integer index : indexes)
            {
                if(index.intValue() >= 0 && index.intValue() < this.visibleColumns.size() &&
                                !this.selectionModel.isSelectedIndex(index.intValue()) &&
                                getVisibleColumn(index.intValue()).isSelectable())
                {
                    this.selectionModel.addSelectionInterval(index.intValue(), index.intValue());
                }
            }
            changed = true;
        }
        return changed;
    }


    public void setVisibleColumns(List<ColumnDescriptor> columns, boolean force)
    {
        if(force)
        {
            this.visibleColumns.clear();
            this.visibleColumns.addAll(columns);
            updateConfiguration();
            return;
        }
        boolean changed = false;
        this.visibleColumns.clear();
        for(ColumnDescriptor col : columns)
        {
            if(showColumnDirectly(col))
            {
                changed = true;
            }
        }
        if(changed)
        {
            fireColumnVisibilityChanged();
        }
    }


    public boolean hideColumn(int columnIndex)
    {
        boolean changed = false;
        int size = this.visibleColumns.size();
        if(columnIndex >= 0 && columnIndex < size && size > 1)
        {
            ColumnDescriptor colDescr = getVisibleColumns().get(columnIndex);
            if(colDescr instanceof DefaultColumnDescriptor)
            {
                this.visibleColumns.remove(columnIndex);
                ((DefaultColumnDescriptor)colDescr).setVisible(false);
                ColumnConfiguration colConfig = this.columnMap.get(colDescr);
                if(colConfig.isLocalized())
                {
                    if(getVisibleLocalizedColumns(colDescr).isEmpty())
                    {
                        ColumnDescriptor nonLocalizedCol = getNonLocalizedColumn(colDescr);
                        if(nonLocalizedCol instanceof DefaultColumnDescriptor)
                        {
                            ((DefaultColumnDescriptor)nonLocalizedCol).setVisible(false);
                        }
                    }
                }
                if(colConfig instanceof MutableColumnConfiguration)
                {
                    ((MutableColumnConfiguration)colConfig).setVisible(false);
                }
                fireColumnVisibilityChanged();
            }
            else
            {
                LOG.error("Can not hide column (Reason: Column descriptor is not of type 'DefaultColumnDescriptor')");
            }
        }
        return false;
    }


    public boolean showColumn(ColumnDescriptor column, Integer colIndex)
    {
        boolean changed = false;
        changed = showColumnDirectly(column);
        if(changed)
        {
            fireColumnVisibilityChanged(colIndex);
        }
        return changed;
    }


    protected boolean showColumnDirectly(ColumnDescriptor column)
    {
        boolean changed = false;
        if(!this.visibleColumns.contains(column) && (
                        getColumns().contains(column) || (this.localizedCol2ColMap.get(column) != null && getColumns()
                                        .contains(this.localizedCol2ColMap.get(column)))))
        {
            try
            {
                if(column instanceof DefaultColumnDescriptor && this.columnMap.containsKey(column))
                {
                    ColumnConfiguration columnConfig = this.columnMap.get(column);
                    if(columnConfig.isLocalized())
                    {
                        LanguageModel lang = column.getLanguage();
                        if(lang == null)
                        {
                            lang = getCommonI18NService().getLanguage(UISessionUtils.getCurrentSession().getGlobalDataLanguageIso());
                        }
                        ColumnDescriptor localizedCol = getLocalizedColumn(column, lang);
                        if(localizedCol == null)
                        {
                            LOG.warn("Can not show column (Reason: Could not get localized version of the specified column).");
                            return false;
                        }
                        ColumnDescriptor nonLocalizedCol = getNonLocalizedColumn(column);
                        if(nonLocalizedCol == null)
                        {
                            LOG.warn("Can not show column (Reason: Can not get non-localized version of the specified column).");
                        }
                        else
                        {
                            ((DefaultColumnDescriptor)nonLocalizedCol).setVisible(true);
                            ((DefaultColumnDescriptor)localizedCol).setVisible(true);
                            if(!this.visibleColumns.contains(localizedCol))
                            {
                                this.visibleColumns.add(localizedCol);
                            }
                            changed = true;
                        }
                    }
                    else
                    {
                        ((DefaultColumnDescriptor)column).setVisible(true);
                        this.visibleColumns.add(column);
                    }
                    if(columnConfig instanceof MutableColumnConfiguration)
                    {
                        ((MutableColumnConfiguration)columnConfig).setVisible(false);
                    }
                    changed = true;
                }
                else
                {
                    LOG.error("Can not show column (Reason: Column descriptor is not available or not of type 'DefaultColumnDescriptor')");
                }
            }
            catch(Exception e)
            {
                LOG.error("Could not show column '" + column + "' (Reason: " + e.getMessage() + ").", e);
            }
        }
        return changed;
    }


    public PropertyDescriptor getPropertyDescriptor(ColumnDescriptor colDescr)
    {
        PropertyDescriptor propDescr = null;
        if(colDescr != null)
        {
            ColumnConfiguration colConf = this.columnMap.get(colDescr);
            if(colConf instanceof PropertyColumnConfiguration)
            {
                propDescr = ((PropertyColumnConfiguration)colConf).getPropertyDescriptor();
            }
        }
        return propDescr;
    }


    public boolean moveColumn(int fromIndex, int toIndex)
    {
        boolean changed = false;
        int size = this.visibleColumns.size();
        if(size > 0 && fromIndex >= 0 && fromIndex < size && toIndex >= 0 && toIndex <= size)
        {
            ColumnDescriptor column = getVisibleColumns().get(fromIndex);
            if(fromIndex == size)
            {
                this.visibleColumns.remove(fromIndex);
                this.visibleColumns.add(size - 1, column);
            }
            else if(fromIndex < toIndex)
            {
                this.visibleColumns.add(toIndex, column);
                this.visibleColumns.remove(fromIndex);
            }
            else
            {
                this.visibleColumns.remove(fromIndex);
                this.visibleColumns.add(toIndex, column);
            }
            fireColumnMoved(fromIndex, toIndex);
            this.selectionModel.clearSelection();
            fireColumnSelectionChanged(Collections.EMPTY_LIST);
            changed = true;
        }
        return changed;
    }


    public int getSortedByColumnIndex()
    {
        return this.sortedColumnIndex;
    }


    public void setSortedColumnIndex(int columnIndex, boolean ascending)
    {
        if(this.sortedColumnIndex != columnIndex || this.sortAscending != ascending)
        {
            this.sortedColumnIndex = columnIndex;
            this.sortAscending = ascending;
            fireSortChanged(columnIndex, ascending);
        }
    }


    public void setSortAscending(boolean ascending)
    {
        if(this.sortAscending != ascending)
        {
            this.sortAscending = ascending;
            fireSortChanged(this.sortedColumnIndex, ascending);
        }
    }


    public boolean isSortAscending()
    {
        return this.sortAscending;
    }


    public int findColumn(ColumnDescriptor colDescr)
    {
        return getVisibleColumns().indexOf(colDescr);
    }


    public ColumnDescriptor getColumn(int columnIndex)
    {
        return getColumns().get(columnIndex);
    }


    public ColumnDescriptor getVisibleColumn(int columnIndex)
    {
        return (getVisibleColumns().size() > columnIndex) ? getVisibleColumns().get(columnIndex) : null;
    }


    public List<ColumnDescriptor> getColumns()
    {
        return this.rootColumnGroup.getAllColumns();
    }


    public ColumnGroup getRootColumnGroup()
    {
        return (ColumnGroup)this.rootColumnGroup;
    }


    public int getSelectedIndex()
    {
        return this.selectionModel.getMinSelectionIndex();
    }


    public List<Integer> getSelectedIndexes()
    {
        return this.selectionModel.getAllSelectedIndexes();
    }


    public List<ColumnDescriptor> getVisibleColumns()
    {
        return Collections.unmodifiableList(this.visibleColumns);
    }


    public List<ColumnDescriptor> getHiddenColumns()
    {
        List<ColumnDescriptor> hiddenCols = new ArrayList<>();
        for(ColumnDescriptor col : getColumns())
        {
            ColumnConfiguration colConfig = this.columnMap.get(col);
            if(colConfig != null)
            {
                if(colConfig.isLocalized())
                {
                    if(getVisibleLocalizedColumns(col).isEmpty() && !hiddenCols.contains(col))
                    {
                        hiddenCols.add(col);
                    }
                    continue;
                }
                if(!col.isVisible())
                {
                    hiddenCols.add(col);
                }
            }
        }
        return hiddenCols;
    }


    public boolean isColumnSelected(int columnIndex)
    {
        return this.selectionModel.isSelectedIndex(columnIndex);
    }


    public Object getValueAt(int columnIndex, TypedObject object)
    {
        Object value = null;
        try
        {
            ColumnDescriptor colDescr = getVisibleColumns().get(columnIndex);
            ColumnConfiguration colConf = this.columnMap.get(colDescr);
            ValueHandler valueHandler = colConf.getValueHandler();
            if(valueHandler == null)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Can not get value since no value handler has been set.");
                }
            }
            else if(colDescr.getLanguage() == null)
            {
                value = valueHandler.getValue(object);
            }
            else
            {
                value = valueHandler.getValue(object, colDescr.getLanguage().getIsocode());
            }
        }
        catch(IllegalArgumentException e)
        {
            LOG.error("Could not get cell value of column '" + columnIndex + "' for the object '" + object + "' (Reason: " + e
                            .getMessage() + ").", e);
            UICockpitPerspective perspective = UISessionUtils.getCurrentSession().getCurrentPerspective();
            perspective.handleItemRemoved(object);
        }
        catch(Exception e)
        {
            LOG.error("Could not get cell value of column '" + columnIndex + "' for the object '" + object + "' (Reason: " + e
                            .getMessage() + ").", e);
        }
        return value;
    }


    public void setValueAt(int columnIndex, TypedObject object, Object value) throws ValueHandlerException
    {
        ColumnDescriptor colDescr = getVisibleColumns().get(columnIndex);
        ColumnConfiguration colConf = this.columnMap.get(colDescr);
        ValueHandler valueHandler = colConf.getValueHandler();
        if(valueHandler == null)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Can not get value since no value handler has been set.");
            }
        }
        else if(colDescr.getLanguage() == null)
        {
            valueHandler.setValue(object, value);
        }
        else
        {
            valueHandler.setValue(object, value, colDescr.getLanguage().getIsocode());
        }
    }


    public UIEditor getCellEditor(int columnIndex)
    {
        UIEditor editor = null;
        try
        {
            editor = ((ColumnConfiguration)this.columnMap.get(getVisibleColumn(columnIndex))).getCellEditor();
        }
        catch(Exception e)
        {
            LOG.error("Could not get cell editor for column with index '" + columnIndex + "' (Reason: " + e.getMessage() + ").", e);
        }
        return editor;
    }


    public CellRenderer getCellRenderer(int columnIndex)
    {
        CellRenderer renderer = null;
        try
        {
            renderer = ((ColumnConfiguration)this.columnMap.get(getVisibleColumns().get(columnIndex))).getCellRenderer();
        }
        catch(Exception e)
        {
            LOG.error("Could not get cell renderer for column with index '" + columnIndex + "' (Reason: " + e.getMessage() + ").", e);
        }
        return renderer;
    }


    public CellRenderer getNewInlineItemRenderer(int columnIndex)
    {
        return (CellRenderer)new InlineItemRenderer();
    }


    public void addColumn(ColumnConfiguration columnConfig, DefaultColumnGroup group)
    {
        if(getRootColumnGroup().getAllColumnGroups().contains(group))
        {
            ColumnDescriptor colDescr = createAndMapColumn(columnConfig, false);
            group.addColumn(colDescr);
        }
        else
        {
            LOG.error("Could not add column configuration. Group not valid.");
        }
    }


    public ColumnDescriptor addColumnToRootGroup(ColumnConfiguration columnConfig)
    {
        ColumnDescriptor colDescr = createAndMapColumn(columnConfig, false);
        this.rootColumnGroup.addColumn(colDescr);
        addColumnToConfig(columnConfig);
        return colDescr;
    }


    private void addColumnToConfig(ColumnConfiguration columnConfig)
    {
        ColumnGroupConfiguration root = this.config.getRootColumnGroupConfiguration();
        if(root != null)
        {
            List<? extends ColumnConfiguration> allColumns = root.getAllColumnConfigurations();
            if(!allColumns.contains(columnConfig))
            {
                List<ColumnConfiguration> columns = new ArrayList<>(root.getColumnConfigurations());
                columns.add(columnConfig);
                root.setColumnConfigurations(columns);
            }
        }
    }


    public ColumnDescriptor addDynamicColumnToRootGroup(ColumnConfiguration columnConfig)
    {
        ColumnDescriptor colDescr = createAndMapColumn(columnConfig, true);
        this.rootColumnGroup.addColumn(colDescr);
        return colDescr;
    }


    public void addGroup(ColumnGroup group, DefaultColumnGroup parentGroup)
    {
        if(getRootColumnGroup().getAllColumnGroups().contains(parentGroup))
        {
            parentGroup.addColumnGroup(group);
        }
        else
        {
            LOG.error("Could not add column group. Parent group does not exist.");
        }
    }


    public List<ColumnDescriptor> getHiddenLocalizedColumns(ColumnDescriptor colDescr)
    {
        List<ColumnDescriptor> hiddenLocCols = getLocalizedColumns(colDescr);
        hiddenLocCols.removeAll(getVisibleLocalizedColumns(colDescr));
        return Collections.unmodifiableList(hiddenLocCols);
    }


    public List<ColumnDescriptor> getVisibleLocalizedColumns(ColumnDescriptor colDescr)
    {
        List<ColumnDescriptor> visibleLocCols = new ArrayList<>();
        List<ColumnDescriptor> localizedCols = getLocalizedColumns(colDescr);
        for(ColumnDescriptor locCol : localizedCols)
        {
            if(this.visibleColumns.contains(locCol))
            {
                visibleLocCols.add(locCol);
            }
        }
        return Collections.unmodifiableList(visibleLocCols);
    }


    public boolean isColumnLocalized(ColumnDescriptor colDescr)
    {
        boolean localized = false;
        ColumnConfiguration colConf = this.columnMap.get(colDescr);
        if(colConf != null && colConf.isLocalized())
        {
            localized = true;
        }
        return localized;
    }


    public ColumnDescriptor getLocalizedColumn(ColumnDescriptor colDescr, LanguageModel language)
    {
        ColumnDescriptor localizedCol = null;
        if(colDescr != null && language != null)
        {
            ColumnDescriptor nonLocalizedCol = getNonLocalizedColumn(colDescr);
            if(nonLocalizedCol != null && this.col2LocalizedColsMap.containsKey(nonLocalizedCol))
            {
                for(ColumnDescriptor locCol : this.col2LocalizedColsMap.get(nonLocalizedCol))
                {
                    if(language.equals(locCol.getLanguage()))
                    {
                        localizedCol = locCol;
                        break;
                    }
                }
            }
        }
        return localizedCol;
    }


    private void loadConfiguration()
    {
        ColumnGroupConfiguration rootConfigGroup = this.config.getRootColumnGroupConfiguration();
        this.rootColumnGroup = createColumnGroup(rootConfigGroup);
        Collections.sort(this.visibleColumns, (Comparator<? super ColumnDescriptor>)new Object(this));
        if(this.config.isAllowCreateInlineItems())
        {
            addColumn((ColumnConfiguration)new InlineItemCreateButtonColumn(), this.rootColumnGroup);
        }
    }


    private DefaultColumnGroup createColumnGroup(ColumnGroupConfiguration groupConfig)
    {
        DefaultColumnGroup group = null;
        if(groupConfig != null)
        {
            group = new DefaultColumnGroup(groupConfig);
            List<ColumnDescriptor> cols = new ArrayList<>();
            for(ColumnConfiguration colConf : groupConfig.getColumnConfigurations())
            {
                cols.add(createAndMapColumn(colConf, false));
            }
            group.setColumns(cols);
            List<ColumnGroup> subGroups = new ArrayList<>();
            for(ColumnGroupConfiguration subGroupConfig : groupConfig.getColumnGroupConfigurations())
            {
                subGroups.add(createColumnGroup(subGroupConfig));
            }
            group.setColumnGroups(subGroups);
        }
        return group;
    }


    private ColumnDescriptor createAndMapColumn(ColumnConfiguration columnConfig, boolean dynamic)
    {
        DefaultColumnDescriptor defaultColumnDescriptor;
        ColumnDescriptor colDescr = null;
        if(columnConfig != null)
        {
            defaultColumnDescriptor = columnConfig.getColumnDescriptor();
            defaultColumnDescriptor.setDynamic(dynamic);
            this.columnMap.put(defaultColumnDescriptor, columnConfig);
            if(columnConfig.isLocalized())
            {
                List<ColumnDescriptor> localizedCols = new ArrayList<>();
                LanguageModel currentLanguage = getCommonI18NService().getLanguage(
                                UISessionUtils.getCurrentSession().getGlobalDataLanguageIso());
                Set<LanguageModel> readableLanguages = UISessionUtils.getCurrentSession().getSystemService().getAllReadableLanguages();
                Set<LanguageModel> writeableLangs = UISessionUtils.getCurrentSession().getSystemService().getAllWriteableLanguages();
                for(LanguageModel lang : readableLanguages)
                {
                    DefaultColumnDescriptor localizedColDescr = columnConfig.getColumnDescriptor();
                    localizedColDescr.setDynamic(dynamic);
                    localizedColDescr.setLanguage(lang);
                    localizedColDescr.setName(localizedColDescr.getName() + " [" + localizedColDescr.getName() + "]");
                    localizedColDescr.setEditable((localizedColDescr.isEditable() && writeableLangs != null && writeableLangs
                                    .contains(lang)));
                    this.columnMap.put(localizedColDescr, columnConfig);
                    this.localizedCol2ColMap.put(localizedColDescr, defaultColumnDescriptor);
                    localizedCols.add(localizedColDescr);
                    if(localizedColDescr.isVisible())
                    {
                        if(CollectionUtils.isEmpty(columnConfig.getLanguages()))
                        {
                            if(lang.equals(currentLanguage))
                            {
                                this.visibleColumns.add(localizedColDescr);
                            }
                        }
                    }
                }
                this.col2LocalizedColsMap.put(defaultColumnDescriptor, localizedCols);
                if(columnConfig.getLanguages() != null)
                {
                    for(LanguageModel lang : columnConfig.getLanguages())
                    {
                        if(readableLanguages.contains(lang))
                        {
                            ColumnDescriptor localizedColDescr = getLocalizedColumn((ColumnDescriptor)defaultColumnDescriptor, lang);
                            localizedColDescr.setDynamic(dynamic);
                            if(localizedColDescr.isVisible())
                            {
                                this.visibleColumns.add(localizedColDescr);
                            }
                        }
                    }
                }
            }
            else
            {
                this.columnMap.put(defaultColumnDescriptor, columnConfig);
                if(defaultColumnDescriptor.isVisible())
                {
                    this.visibleColumns.add(defaultColumnDescriptor);
                }
            }
        }
        return (ColumnDescriptor)defaultColumnDescriptor;
    }


    protected List<ColumnDescriptor> getLocalizedColumns(ColumnDescriptor colDescr)
    {
        List<ColumnDescriptor> localizedCols = new ArrayList<>();
        if(colDescr != null && this.columnMap.containsKey(colDescr) && ((ColumnConfiguration)this.columnMap.get(colDescr)).isLocalized())
        {
            ColumnDescriptor nonLocalizedCol = getNonLocalizedColumn(colDescr);
            List<ColumnDescriptor> tmp = this.col2LocalizedColsMap.get(nonLocalizedCol);
            if(tmp != null)
            {
                localizedCols.addAll(tmp);
            }
        }
        return localizedCols;
    }


    protected ColumnDescriptor getNonLocalizedColumn(ColumnDescriptor colDescr)
    {
        ColumnDescriptor nonLocalizedColDescr = this.localizedCol2ColMap.get(colDescr);
        return (nonLocalizedColDescr == null) ? colDescr : nonLocalizedColDescr;
    }


    public String getColumnWidth(ColumnDescriptor colDescr)
    {
        return getColumnWidth(colDescr, Boolean.TRUE);
    }


    public String getColumnWidth(ColumnDescriptor colDescr, Boolean returnDefault)
    {
        String width = null;
        ColumnConfiguration colConf = this.columnMap.get(colDescr);
        if(colConf != null)
        {
            width = colConf.getWidth();
        }
        return (width == null && returnDefault.booleanValue()) ? "100%" : width;
    }


    protected void updateConfiguration()
    {
        DefaultColumnGroup rootGroup = (DefaultColumnGroup)getRootColumnGroup();
        for(ColumnDescriptor columnDesc : rootGroup.getAllColumns())
        {
            ColumnConfiguration columnConfig = getColumnConfiguration(columnDesc);
            columnConfig.setVisible(columnDesc.isVisible());
        }
        for(ColumnDescriptor columnDesc : getVisibleColumns())
        {
            ColumnConfiguration columnConfig = getColumnConfiguration(columnDesc);
            columnConfig.setLanguages(Collections.EMPTY_LIST);
        }
        int position = 1;
        for(ColumnDescriptor columnDesc : getVisibleColumns())
        {
            ColumnConfiguration columnConfig = getColumnConfiguration(columnDesc);
            if(columnDesc.getLanguage() != null)
            {
                List<LanguageModel> langs = new LinkedList<>();
                langs.addAll(columnConfig.getLanguages());
                langs.add(columnDesc.getLanguage());
                columnConfig.setLanguages(langs);
            }
            columnConfig.setPosition(position++);
        }
    }


    protected void fireColumnMoved(int fromIndex, int toIndex)
    {
        updateConfiguration();
        super.fireColumnMoved(fromIndex, toIndex);
    }


    protected void fireColumnVisibilityChanged()
    {
        updateConfiguration();
        super.fireColumnVisibilityChanged();
    }


    protected void fireColumnVisibilityChanged(Integer colIndex)
    {
        updateConfiguration();
        super.fireColumnVisibilityChanged(colIndex);
    }


    protected void fireSortChanged(int columnIndex, boolean ascending)
    {
        updateConfiguration();
        super.fireSortChanged(columnIndex, ascending);
    }


    public ListViewMenuPopupBuilder getMenuPopupBuilder()
    {
        return (ListViewMenuPopupBuilder)SpringUtil.getBean(getConfiguration().getHeaderPopupBean());
    }


    public void updateDynamicColumns(ListModel listModel)
    {
        clearDynamicColumns();
        for(DynamicColumnProvider provider : this.dynamicColumnProviders)
        {
            for(ColumnConfiguration columnConfiguration : provider.getDynamicColums(listModel))
            {
                addDynamicColumnToRootGroup(columnConfiguration);
            }
        }
        fireChanged();
    }


    private void clearDynamicColumns()
    {
        Iterator<ColumnDescriptor> visibleColumnsIt = this.visibleColumns.iterator();
        while(visibleColumnsIt.hasNext())
        {
            ColumnDescriptor descr = visibleColumnsIt.next();
            if(descr.isDynamic())
            {
                visibleColumnsIt.remove();
            }
        }
        clearDynamicsInMap(this.columnMap);
        clearDynamicsInMap(this.localizedCol2ColMap);
        clearDynamicsInMap(this.col2LocalizedColsMap);
        this.rootColumnGroup.clearDynamicColumns();
    }


    private void clearDynamicsInMap(Map map)
    {
        Iterator<Map.Entry> iterator = map.entrySet().iterator();
        while(iterator.hasNext())
        {
            Map.Entry entry = iterator.next();
            if(((ColumnDescriptor)entry.getKey()).isDynamic())
            {
                iterator.remove();
            }
        }
    }


    private CommonI18NService getCommonI18NService()
    {
        if(this.commonI18nService == null)
        {
            this.commonI18nService = (CommonI18NService)Registry.getApplicationContext().getBean("commonI18NService", CommonI18NService.class);
        }
        return this.commonI18nService;
    }
}
