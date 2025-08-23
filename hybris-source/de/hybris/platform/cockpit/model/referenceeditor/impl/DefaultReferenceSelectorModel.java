package de.hybris.platform.cockpit.model.referenceeditor.impl;

import de.hybris.platform.cockpit.model.advancedsearch.AdvancedSearchModel;
import de.hybris.platform.cockpit.model.advancedsearch.AdvancedSearchParameterContainer;
import de.hybris.platform.cockpit.model.advancedsearch.impl.AdvancedSearchHelper;
import de.hybris.platform.cockpit.model.advancedsearch.impl.DefaultAdvancedSearchModel;
import de.hybris.platform.cockpit.model.general.ListModel;
import de.hybris.platform.cockpit.model.general.MutableListModel;
import de.hybris.platform.cockpit.model.general.impl.DefaultListComponentModel;
import de.hybris.platform.cockpit.model.general.impl.DefaultListModel;
import de.hybris.platform.cockpit.model.listview.ColumnModelListener;
import de.hybris.platform.cockpit.model.listview.MutableColumnModel;
import de.hybris.platform.cockpit.model.listview.MutableTableModel;
import de.hybris.platform.cockpit.model.listview.impl.DefaultColumnModel;
import de.hybris.platform.cockpit.model.listview.impl.DefaultTableModel;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.referenceeditor.AbstractReferenceSelectorModel;
import de.hybris.platform.cockpit.model.referenceeditor.SelectorModel;
import de.hybris.platform.cockpit.model.search.ExtendedSearchResult;
import de.hybris.platform.cockpit.model.search.Query;
import de.hybris.platform.cockpit.model.search.SearchParameterValue;
import de.hybris.platform.cockpit.model.search.SearchType;
import de.hybris.platform.cockpit.services.config.AdvancedSearchConfiguration;
import de.hybris.platform.cockpit.services.config.ListViewConfiguration;
import de.hybris.platform.cockpit.services.config.UIComponentConfiguration;
import de.hybris.platform.cockpit.services.config.UIConfigurationService;
import de.hybris.platform.cockpit.services.label.LabelService;
import de.hybris.platform.cockpit.services.login.LoginService;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.search.SearchProvider;
import de.hybris.platform.cockpit.session.UISessionUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zkplus.spring.SpringUtil;

public class DefaultReferenceSelectorModel extends AbstractReferenceSelectorModel
{
    private static final Logger log = LoggerFactory.getLogger(DefaultReferenceSelectorModel.class);
    protected static final String LIST_VIEW_CONFIG_CODE = "listViewSelector";
    protected static final String ADVANCED_SEARCH_VIEW_CONFIG_CODE = "advancedSearch";
    private boolean multiple = false;
    private SelectorModel.Mode mode = SelectorModel.Mode.VIEW_MODE;
    private ObjectType rootType = null;
    private ObjectType rootSearchType = null;
    private final List<Object> items;
    private final List<Object> actualSelectedItems;
    private List<Object> temporaryItems;
    private final List<Object> actualSelectedTempItems;
    private final List<Object> notConfirmedItems;
    private List<? extends Object> autoCompleteResult;
    private List<? extends Object> searchResult;
    private int minAutoCompleteTextLength = 1;
    private int maxAutoCompleteResultSize = 20;
    private TypeService typeService = null;
    private ObjectType autocompletionSearchType = null;
    private Map<String, ? extends Object> parameters;
    private UIConfigurationService uiConfigurationService;
    protected MutableTableModel tableModel;
    protected DefaultAdvancedSearchModel advancedSearchMode;
    private int pageSize = 20;
    private int totalSize = -1;
    private LoginService loginService;
    private SearchProvider searchProvider;
    protected ColumnModelListener columnModelListener;


    public DefaultReferenceSelectorModel()
    {
        this(null);
    }


    public DefaultReferenceSelectorModel(ObjectType rootType)
    {
        this.rootType = rootType;
        this.items = new ArrayList();
        this.actualSelectedItems = new ArrayList();
        this.notConfirmedItems = new ArrayList();
        this.temporaryItems = new ArrayList();
        this.actualSelectedTempItems = new ArrayList();
        this.searchResult = new ArrayList();
        this.autoCompleteResult = new ArrayList();
    }


    public void setItems(List<Object> items)
    {
        if(this.items != items && !this.items.equals(items))
        {
            this.items.clear();
            if(items != null)
            {
                this.items.addAll(getTypeService().wrapItems(items));
            }
            fireItemsChanged();
        }
    }


    public void setMode(SelectorModel.Mode mode)
    {
        if(!this.mode.equals(mode))
        {
            this.mode = mode;
            fireModeChanged();
        }
    }


    public void setAutoCompleteResult(List<? extends Object> autoCompleteResult)
    {
        if(this.autoCompleteResult != autoCompleteResult)
        {
            this.autoCompleteResult = autoCompleteResult;
            fireAutoCompleteResultChanged();
        }
    }


    public void setTemporaryItems(List<Object> temporaryItems)
    {
        if(this.temporaryItems != temporaryItems)
        {
            this.temporaryItems = temporaryItems;
            fireTemporaryItemsChanged();
        }
    }


    public void setSearchResult(List<? extends Object> searchResult)
    {
        if(this.searchResult != searchResult)
        {
            this.searchResult = searchResult;
            fireSearchResultChanged();
        }
    }


    public void setMinAutoCompleteTextLength(int minAutoCompleteTextLength)
    {
        this.minAutoCompleteTextLength = minAutoCompleteTextLength;
    }


    public void setMaxAutoCompleteResultSize(int maxAutoCompleteResultSize)
    {
        this.maxAutoCompleteResultSize = maxAutoCompleteResultSize;
    }


    public void setPageSize(int pageSize)
    {
        this.pageSize = pageSize;
    }


    public int getPageSize()
    {
        return this.pageSize;
    }


    public List<? extends Object> getAutoCompleteResult()
    {
        if(this.autoCompleteResult == null)
        {
            this.autoCompleteResult = new ArrayList();
        }
        return this.autoCompleteResult;
    }


    @Deprecated
    public List<Object> getItems()
    {
        List<Object> ret = this.items;
        if(this.multiple)
        {
            if(this.items == null)
            {
                ret = new ArrayList();
            }
        }
        else if(this.items != null && !this.items.isEmpty())
        {
            ret = Collections.singletonList(this.items.get(0));
        }
        return ret;
    }


    public Object getValue()
    {
        Object<Object> ret = null;
        if(this.multiple)
        {
            ret = (Object<Object>)this.items;
        }
        else if(this.items != null && !this.items.isEmpty())
        {
            ret = (Object<Object>)this.items.get(0);
        }
        return ret;
    }


    public List<Object> getTemporaryItems()
    {
        if(this.temporaryItems == null)
        {
            this.temporaryItems = new ArrayList();
        }
        return this.temporaryItems;
    }


    public int getMinAutoCompleteTextLength()
    {
        return this.minAutoCompleteTextLength;
    }


    public int getMaxAutoCompleteResultSize()
    {
        return this.maxAutoCompleteResultSize;
    }


    public SelectorModel.Mode getMode()
    {
        return this.mode;
    }


    public ObjectType getAutocompleteSearchType()
    {
        return (this.autocompletionSearchType == null) ? getRootSearchType() : this.autocompletionSearchType;
    }


    public String getLabel()
    {
        return null;
    }


    public List<? extends Object> getSearchResult()
    {
        if(this.searchResult == null)
        {
            this.searchResult = new ArrayList();
        }
        return this.searchResult;
    }


    public boolean isMultiple()
    {
        return this.multiple;
    }


    public void setMultiple(boolean multiple)
    {
        this.multiple = multiple;
    }


    public boolean addItem(Object item)
    {
        if(item != null && !this.items.contains(item))
        {
            this.items.add(item);
            fireItemsChanged();
            return true;
        }
        return false;
    }


    public boolean addItemNotConfirmed(Object itemNotConfirmed)
    {
        if(itemNotConfirmed != null && !this.notConfirmedItems.contains(itemNotConfirmed))
        {
            this.notConfirmedItems.add(itemNotConfirmed);
            fireItemsNotConfirmedChanged();
            return true;
        }
        return false;
    }


    public boolean selectTemporaryItem(Object item)
    {
        if(item != null && !this.actualSelectedTempItems.contains(item))
        {
            if(!this.multiple)
            {
                this.actualSelectedTempItems.clear();
            }
            return this.actualSelectedTempItems.add(item);
        }
        return false;
    }


    public boolean selectItem(Object item)
    {
        if(item != null && !this.actualSelectedItems.contains(item))
        {
            return this.actualSelectedItems.add(item);
        }
        return false;
    }


    public boolean deselectItem(Object item)
    {
        if(item != null && this.actualSelectedItems.contains(item))
        {
            return this.actualSelectedItems.remove(item);
        }
        return false;
    }


    public boolean deselectTemporaryItem(Object item)
    {
        if(item != null && this.actualSelectedTempItems.contains(item))
        {
            return this.actualSelectedTempItems.remove(item);
        }
        return false;
    }


    public boolean deselectTemporaryItems()
    {
        this.actualSelectedTempItems.clear();
        return true;
    }


    public boolean addItems(List collection)
    {
        for(Object item : collection)
        {
            if(!this.items.contains(item))
            {
                this.items.add(item);
            }
        }
        fireItemsChanged();
        return false;
    }


    public boolean selectTemporaryItems(Collection collection)
    {
        for(Object item : collection)
        {
            if(!this.actualSelectedTempItems.contains(item))
            {
                this.actualSelectedTempItems.add(item);
            }
        }
        return true;
    }


    public boolean selectItems(Collection items)
    {
        if(items == null)
        {
            return false;
        }
        for(Object item : items)
        {
            if(!this.multiple)
            {
                this.actualSelectedItems.clear();
            }
            if(!this.actualSelectedItems.contains(item))
            {
                this.actualSelectedItems.add(item);
            }
        }
        return true;
    }


    public boolean addItemsNotConfirmed(Collection items)
    {
        if(items == null)
        {
            return false;
        }
        for(Object item : items)
        {
            if(!this.multiple)
            {
                this.notConfirmedItems.clear();
            }
            if(!this.notConfirmedItems.contains(item))
            {
                this.notConfirmedItems.add(item);
            }
        }
        fireItemsNotConfirmedChanged();
        return false;
    }


    public boolean removeItem(int index)
    {
        boolean ret = false;
        if(this.items.size() <= index)
        {
            ret = false;
        }
        ret = (this.items.remove(index) != null);
        if(ret)
        {
            fireItemsChanged();
        }
        return ret;
    }


    public boolean removeItemNotConfirmed(int index)
    {
        boolean ret = false;
        if(this.notConfirmedItems.size() <= index)
        {
            ret = false;
        }
        ret = (this.notConfirmedItems.remove(index) != null);
        if(ret)
        {
            fireItemsNotConfirmedChanged();
        }
        return ret;
    }


    public boolean moveItem(int fromIndex, int toIndex)
    {
        boolean ret = false;
        if(fromIndex < 0 || toIndex >= this.items.size())
        {
            return ret;
        }
        Object sourceItem = this.items.get(fromIndex);
        if(fromIndex < toIndex)
        {
            this.items.add(toIndex, sourceItem);
            this.items.remove(fromIndex);
            fireItemsChanged();
            ret = true;
        }
        else if(fromIndex > toIndex)
        {
            this.items.remove(fromIndex);
            this.items.add(toIndex, sourceItem);
            fireItemsChanged();
            ret = true;
        }
        return ret;
    }


    public boolean moveTemporaryItem(int fromIndex, int toIndex)
    {
        boolean ret = false;
        if(fromIndex < 0 || toIndex >= this.temporaryItems.size())
        {
            return ret;
        }
        Object sourceItem = this.temporaryItems.get(fromIndex);
        if(fromIndex < toIndex)
        {
            this.temporaryItems.add(toIndex, sourceItem);
            this.temporaryItems.remove(fromIndex);
            fireTemporaryItemsChanged();
            ret = true;
        }
        else if(fromIndex > toIndex)
        {
            this.temporaryItems.remove(fromIndex);
            this.temporaryItems.add(toIndex, sourceItem);
            fireTemporaryItemsChanged();
            ret = true;
        }
        return ret;
    }


    public boolean removeItems(Collection indexes)
    {
        for(Object index : indexes)
        {
            if(this.items.size() <= ((Integer)index).intValue())
            {
                return false;
            }
            if(!this.items.remove(index))
            {
                return false;
            }
        }
        return true;
    }


    public void clearItems()
    {
        if(this.items != null)
        {
            this.items.clear();
        }
    }


    public boolean setItem(int index, Object item)
    {
        boolean ret = false;
        if(this.items.size() <= index)
        {
            ret = false;
        }
        if(!this.items.contains(item))
        {
            this.items.add(index, item);
            fireItemsChanged();
            ret = true;
        }
        return ret;
    }


    public boolean addTemporaryItem(Object item)
    {
        boolean ret = false;
        ret = addTemporaryItemInner(item);
        if(ret)
        {
            fireTemporaryItemsChanged();
        }
        return ret;
    }


    private boolean addTemporaryItemInner(Object item)
    {
        boolean ret = false;
        if(!this.temporaryItems.contains(item))
        {
            if(!this.multiple)
            {
                this.temporaryItems.clear();
            }
            ret = this.temporaryItems.add(item);
        }
        return ret;
    }


    public boolean addTemporaryItems(Collection items)
    {
        int addedCorectly = 0;
        boolean ret = false;
        for(Object item : items)
        {
            if(addTemporaryItemInner(item))
            {
                addedCorectly++;
            }
        }
        if(addedCorectly == items.size())
        {
            fireTemporaryItemsChanged();
            ret = true;
        }
        return ret;
    }


    public boolean removeTemporaryItem(Object item)
    {
        boolean ret = false;
        if(this.temporaryItems.contains(item))
        {
            ret = this.temporaryItems.remove(item);
            if(ret)
            {
                fireTemporaryItemsChanged();
            }
        }
        return ret;
    }


    public boolean removeTemporaryItem(int index)
    {
        boolean ret = false;
        if(this.temporaryItems.size() > index)
        {
            ret = (this.temporaryItems.remove(index) != null);
            if(ret)
            {
                fireTemporaryItemsChanged();
            }
        }
        return ret;
    }


    public void clearTemporaryItems()
    {
        if(this.temporaryItems != null)
        {
            this.temporaryItems.clear();
        }
        fireTemporaryItemsChanged();
    }


    public int getTotalSize()
    {
        return this.totalSize;
    }


    public void setTotalSize(int totalSize)
    {
        this.totalSize = totalSize;
    }


    public void reset()
    {
        clearItems();
        cancel();
        fireTemporaryItemsChanged();
        fireItemsNotConfirmedChanged();
        fireItemsChanged();
        setMode(SelectorModel.Mode.VIEW_MODE);
    }


    public void cancel()
    {
        this.actualSelectedItems.clear();
        this.notConfirmedItems.clear();
        this.autoCompleteResult.clear();
        this.searchResult.clear();
        clearTemporaryItems();
        this.actualSelectedTempItems.clear();
        fireCancel();
    }


    public String getItemLabel(Object item)
    {
        String label = "";
        if(item instanceof TypedObject)
        {
            LabelService labelService = UISessionUtils.getCurrentSession().getLabelService();
            label = labelService.getObjectTextLabel((TypedObject)item);
        }
        else
        {
            log.warn("Can not get label for item since it is not a TypedObject.");
            if(item != null)
            {
                label = item.toString();
            }
        }
        return label;
    }


    public ObjectType getRootType()
    {
        return this.rootType;
    }


    public TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = (TypeService)SpringUtil.getBean("cockpitTypeService");
        }
        return this.typeService;
    }


    protected SearchProvider getSearchProvider()
    {
        if(this.searchProvider == null)
        {
            this.searchProvider = UISessionUtils.getCurrentSession().getSearchService().getSearchProvider();
        }
        return this.searchProvider;
    }


    public void setRootType(ObjectType rootType)
    {
        if((this.rootType != null && !this.rootType.equals(rootType)) || (this.rootType == null && rootType != null))
        {
            this.rootType = rootType;
            fireRootTypeChanged();
            if(this.rootSearchType == null)
            {
                fireRootSearchTypeChanged();
            }
        }
    }


    public ObjectType getRootSearchType()
    {
        return (this.rootSearchType == null) ? this.rootType : this.rootSearchType;
    }


    public void setRootSearchType(ObjectType rootSearchType)
    {
        if(this.rootSearchType != rootSearchType)
        {
            this.rootSearchType = rootSearchType;
            fireRootSearchTypeChanged();
        }
    }


    public void setAutocompleteSearchType(ObjectType autocompletionSearchType)
    {
        if(this.autocompletionSearchType != autocompletionSearchType)
        {
            this.autocompletionSearchType = autocompletionSearchType;
            fireRootSearchTypeChanged();
        }
    }


    public List getNotConfirmedItems()
    {
        return this.notConfirmedItems;
    }


    public List<Object> getActualSelectedTempItems()
    {
        return this.actualSelectedTempItems;
    }


    public List<Object> getActualSelectedItems()
    {
        return this.actualSelectedItems;
    }


    public void saveItems()
    {
        if(this.items != this.actualSelectedItems && !this.items.equals(this.actualSelectedItems))
        {
            this.items.clear();
            if(this.items != null)
            {
                this.items.addAll(this.actualSelectedItems);
            }
        }
        this.actualSelectedItems.clear();
        this.actualSelectedTempItems.clear();
        this.temporaryItems.clear();
        this.autoCompleteResult.clear();
        this.notConfirmedItems.clear();
        fireItemsChanged();
    }


    protected ListViewConfiguration getListViewConfiguration(ObjectTemplate objectTemplate, String code)
    {
        UIConfigurationService uiConfigService = getUIConfigurationService();
        return (ListViewConfiguration)uiConfigService.getComponentConfiguration(objectTemplate, code, ListViewConfiguration.class);
    }


    protected AdvancedSearchConfiguration getAdvancedSearchConfiguration(ObjectTemplate objectTemplate, String code)
    {
        return (AdvancedSearchConfiguration)getUIConfigurationService().getComponentConfiguration(objectTemplate, code, AdvancedSearchConfiguration.class);
    }


    protected UIConfigurationService getUIConfigurationService()
    {
        if(this.uiConfigurationService == null)
        {
            this.uiConfigurationService = (UIConfigurationService)SpringUtil.getBean("uiConfigurationService");
        }
        return this.uiConfigurationService;
    }


    protected LoginService getLoginService()
    {
        if(this.loginService == null)
        {
            this.loginService = (LoginService)SpringUtil.getBean("loginService");
        }
        return this.loginService;
    }


    public void initAdvanceModeModels()
    {
        this.advancedSearchMode = createAdvancedTableModel();
        this.tableModel = createDefaultTableModel();
        if(getTableModel() != null)
        {
            getTableModel().getColumnComponentModel().addColumnModelListener(getColumnModelListener());
        }
    }


    public void doSearch(ObjectTemplate objectType, AdvancedSearchParameterContainer parameterContainer, int currentPage)
    {
        List<SearchParameterValue> searchValueParameters = new ArrayList<>();
        List<List<SearchParameterValue>> orValues = new LinkedList<>();
        List<SearchType> searchTypes = Collections.singletonList(UISessionUtils.getCurrentSession().getSearchService().getSearchType(objectType));
        int offsetStart = currentPage * getPageSize();
        Query searchQuery = new Query(searchTypes, null, offsetStart, getPageSize());
        searchQuery.setContextParameter("objectTemplate", objectType);
        createSearchParameterValues(parameterContainer, searchValueParameters, orValues);
        searchQuery.setParameterValues(searchValueParameters);
        searchQuery.setParameterOrValues(orValues);
        searchQuery.setExcludeSubTypes(parameterContainer.isExcludeSubtypes());
        searchQuery.setSortCriteria(Collections.singletonMap(parameterContainer.getSortProperty(),
                        Boolean.valueOf(parameterContainer.isSortAscending())));
        ExtendedSearchResult searchResult = getSearchProvider().search(searchQuery);
        setTotalSize(searchResult.getTotalCount());
        ((DefaultListModel)this.tableModel.getListComponentModel().getListModel()).clearAndAddAll(searchResult.getResult());
        setSearchResult(searchResult.getResult());
    }


    protected void createSearchParameterValues(AdvancedSearchParameterContainer parameterContainer, List<SearchParameterValue> paramValues, List<List<SearchParameterValue>> orValues)
    {
        AdvancedSearchHelper.createSearchParameterValues(getAdvancedSearchModel(), parameterContainer, paramValues, orValues);
    }


    public DefaultAdvancedSearchModel createAdvancedTableModel()
    {
        ObjectTemplate rootTemplate = getTypeService().getObjectTemplate(getRootSearchType().getCode());
        return new DefaultAdvancedSearchModel(getAdvancedSearchConfiguration(rootTemplate, "advancedSearch"), "advancedSearch");
    }


    public MutableTableModel createDefaultTableModel()
    {
        DefaultTableModel defaultTableModel1;
        MutableTableModel defaultTableModel = null;
        ListViewConfiguration listViewConfig = null;
        ObjectTemplate actualType = getTypeService().getObjectTemplate(getRootSearchType().getCode());
        if(getListViewConfiguration(actualType, "listViewSelector") != null)
        {
            try
            {
                SearchType searchType = UISessionUtils.getCurrentSession().getSearchService().getSearchType(getRootSearchType());
                ObjectTemplate objectTemplate = getTypeService().getObjectTemplate(searchType.getCode());
                listViewConfig = getListViewConfiguration(objectTemplate, "listViewSelector");
            }
            catch(Exception e)
            {
                log.warn("Could not load list view configuration (Reason: '" + e.getMessage() + "').");
                return null;
            }
            DefaultListComponentModel defaultListComponentModel = new DefaultListComponentModel();
            defaultListComponentModel.setEditable(Boolean.TRUE.booleanValue());
            defaultListComponentModel.setSelectable(Boolean.TRUE.booleanValue());
            defaultListComponentModel.setActivatable(!isMultiple());
            defaultListComponentModel.setForceRenderOnSelectionChanged(Boolean.TRUE.booleanValue());
            DefaultListModel defaultListModel = new DefaultListModel();
            defaultListComponentModel.setListModel((ListModel)defaultListModel);
            DefaultColumnModel defaultColumnModel = new DefaultColumnModel(listViewConfig);
            defaultTableModel1 = new DefaultTableModel((MutableListModel)defaultListComponentModel, (MutableColumnModel)defaultColumnModel);
        }
        return (MutableTableModel)defaultTableModel1;
    }


    protected ColumnModelListener getColumnModelListener()
    {
        if(this.columnModelListener == null)
        {
            this.columnModelListener = (ColumnModelListener)new Object(this);
        }
        return this.columnModelListener;
    }


    protected void storeListViewConfiguration()
    {
        if(getTableModel() != null && getTableModel().getColumnComponentModel() instanceof DefaultColumnModel)
        {
            ObjectType rootType = getRootType();
            if(rootType instanceof ObjectTemplate)
            {
                DefaultColumnModel columnModel = (DefaultColumnModel)getTableModel().getColumnComponentModel();
                ListViewConfiguration configuration = columnModel.getConfiguration();
                getUIConfigurationService().setLocalComponentConfiguration((UIComponentConfiguration)configuration,
                                UISessionUtils.getCurrentSession().getUser(), (ObjectTemplate)getRootType(), "listViewSelector", ListViewConfiguration.class);
            }
        }
    }


    public void setParameters(Map<String, ? extends Object> parameters)
    {
        this.parameters = parameters;
    }


    public Map<String, ? extends Object> getParameters()
    {
        return this.parameters;
    }


    public MutableTableModel getTableModel()
    {
        return this.tableModel;
    }


    public AdvancedSearchModel getAdvancedSearchModel()
    {
        return (AdvancedSearchModel)this.advancedSearchMode;
    }
}
