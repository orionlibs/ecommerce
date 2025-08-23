package de.hybris.platform.cockpit.wizards.generic;

import de.hybris.platform.cockpit.model.advancedsearch.AdvancedSearchModel;
import de.hybris.platform.cockpit.model.advancedsearch.AdvancedSearchParameterContainer;
import de.hybris.platform.cockpit.model.advancedsearch.SearchField;
import de.hybris.platform.cockpit.model.advancedsearch.SearchFieldGroup;
import de.hybris.platform.cockpit.model.advancedsearch.UIAdvancedSearchView;
import de.hybris.platform.cockpit.model.advancedsearch.impl.AdvancedSearchHelper;
import de.hybris.platform.cockpit.model.advancedsearch.impl.AdvancedSearchView;
import de.hybris.platform.cockpit.model.advancedsearch.impl.DefaultAdvancedSearchModel;
import de.hybris.platform.cockpit.model.general.ListModel;
import de.hybris.platform.cockpit.model.general.MutableListModel;
import de.hybris.platform.cockpit.model.general.impl.DefaultListComponentModel;
import de.hybris.platform.cockpit.model.general.impl.DefaultListModel;
import de.hybris.platform.cockpit.model.listview.ColumnModelListener;
import de.hybris.platform.cockpit.model.listview.MutableColumnModel;
import de.hybris.platform.cockpit.model.listview.MutableTableModel;
import de.hybris.platform.cockpit.model.listview.TableModel;
import de.hybris.platform.cockpit.model.listview.UIListView;
import de.hybris.platform.cockpit.model.listview.impl.DefaultColumnModel;
import de.hybris.platform.cockpit.model.listview.impl.DefaultTableModel;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.misc.ComponentController;
import de.hybris.platform.cockpit.model.search.ExtendedSearchResult;
import de.hybris.platform.cockpit.model.search.Query;
import de.hybris.platform.cockpit.model.search.SearchParameterValue;
import de.hybris.platform.cockpit.model.search.SearchType;
import de.hybris.platform.cockpit.services.config.AdvancedSearchConfiguration;
import de.hybris.platform.cockpit.services.config.ListViewConfiguration;
import de.hybris.platform.cockpit.services.config.SearchFieldConfiguration;
import de.hybris.platform.cockpit.services.config.SearchFieldGroupConfiguration;
import de.hybris.platform.cockpit.services.config.UIComponentConfiguration;
import de.hybris.platform.cockpit.services.config.UIConfigurationService;
import de.hybris.platform.cockpit.services.config.impl.DefaultSearchFieldGroupConfiguration;
import de.hybris.platform.cockpit.services.config.impl.PropertySearchFieldConfiguration;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.search.SearchProvider;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.CreateContext;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.cockpit.wizards.Wizard;
import de.hybris.platform.cockpit.wizards.WizardPageController;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zhtml.Center;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Vbox;

public class AdvancedSearchPage extends AbstractGenericItemPage
{
    private static final Logger LOG = LoggerFactory.getLogger(AdvancedSearchPage.class);
    protected static final String LIST_VIEW_CONFIG_CODE = "listViewSelector";
    protected static final String ADVANCED_SEARCH_VIEW_CONFIG_CODE = "advancedSearch";
    protected static final String WIZARD_ADVANCED_SEARCH_PAGE = "wizardAdvancedSearchPage";
    protected static final String LIST_VIEW_SELECTOR_SCLASS = "listViewInSelectorContext";
    protected static final String MEDIA_MIME = "media.mime";
    protected static final String MIME_TYPES = "mimeTypes";
    protected static final String SEMICOLON = ";";
    private transient UIListView listView = null;
    private transient UIAdvancedSearchView advancedSearchComponent = null;
    private transient Component mainViewComponent = null;
    private transient ComponentController listViewController;
    private transient ComponentController advancedSearchController;
    private transient Div resultListContainer;
    private transient Paging paging;
    private Map<String, ? extends Object> parameters;
    private Object selectedValue;
    private TableModel tableModel;
    private DefaultAdvancedSearchModel advancedSearchModel;
    private TypeService typeService = null;
    private UIConfigurationService uiConfigurationService;
    private SearchProvider searchProvider;
    private ColumnModelListener columnModelListener;
    private ObjectType rootSearchType = null;
    private final String rootSearchStringType = null;
    private int pageSize = 15;
    private boolean multiple = false;
    private List<? extends Object> searchResult = new ArrayList();


    public AdvancedSearchPage()
    {
        this(null);
    }


    public AdvancedSearchPage(String pageTitle)
    {
        this(pageTitle, null, null);
    }


    public AdvancedSearchPage(String pageTitle, Wizard wizard, CreateContext createContext)
    {
        super(pageTitle, wizard);
        if(createContext != null)
        {
            setController((WizardPageController)new DefaultAdvancedSearchPageController(createContext.getSourceObject(), createContext
                            .getPropertyDescriptor(), createContext.isFireSearch()));
        }
        else
        {
            setController((WizardPageController)new DefaultAdvancedSearchPageController());
        }
    }


    public void setWidth(String width)
    {
        this.width = width;
    }


    public void setHeight(String height)
    {
        this.height = height;
    }


    public void setParameters(Map<String, ? extends Object> parameters)
    {
        this.parameters = parameters;
    }


    public Object getSelectedValue()
    {
        Object ret = null;
        if(isMultiple())
        {
            if(this.selectedValue instanceof Collection || this.selectedValue == null)
            {
                ret = this.selectedValue;
            }
            else
            {
                ret = Collections.singletonList(this.selectedValue);
            }
        }
        else if(this.selectedValue instanceof Collection)
        {
            ret = ((Collection)this.selectedValue).isEmpty() ? null : ((Collection)this.selectedValue).iterator().next();
        }
        else
        {
            ret = this.selectedValue;
        }
        return ret;
    }


    public void setSelectedValue(Object selectedValue)
    {
        this.selectedValue = selectedValue;
    }


    public String getRootSearchStringType()
    {
        return this.rootSearchStringType;
    }


    public void setRootSearchStringType(String rootSearchStringType)
    {
        if(this.rootSearchStringType == null || !this.rootSearchStringType.equals(rootSearchStringType))
        {
            this.rootSearchType = UISessionUtils.getCurrentSession().getTypeService().getObjectType(rootSearchStringType);
            initAdvanceModeModels();
        }
    }


    public List<? extends Object> getSearchResult()
    {
        return this.searchResult;
    }


    public int getPageSize()
    {
        return this.pageSize;
    }


    public void setPageSize(int size)
    {
        this.pageSize = size;
    }


    public TableModel getTableModel()
    {
        return this.tableModel;
    }


    public ObjectType getRootSearchType()
    {
        return this.rootSearchType;
    }


    public void setRootSearchType(ObjectType rootSearchType)
    {
        if(this.rootSearchType == null || !this.rootSearchType.equals(rootSearchType))
        {
            this.rootSearchType = rootSearchType;
            initAdvanceModeModels();
        }
    }


    public Component createRepresentationItself()
    {
        if(this.rootSearchType == null)
        {
            setRootSearchType(getWizard().getCurrentType());
        }
        clearCurrentSearchResult();
        clearPageComponents();
        loadViewComponents();
        this.pageContent.appendChild(this.mainViewComponent);
        return (Component)this.pageContainer;
    }


    protected Component loadMainViewCompoent()
    {
        setTitle("referenceselector.advanced.search.title");
        Vbox mainContainer = new Vbox();
        mainContainer.setWidth("100%");
        mainContainer.appendChild((Component)this.advancedSearchComponent);
        this.resultListContainer = new Div();
        this.resultListContainer.setSclass("wizardAdvancedSearchPage");
        mainContainer.appendChild((Component)this.resultListContainer);
        Hbox footer = new Hbox();
        footer.setWidth("100%");
        footer.setWidths("99%,none");
        Center centerAlign = new Center();
        centerAlign.appendChild((Component)pagingToolbar());
        footer.appendChild((Component)centerAlign);
        mainContainer.appendChild((Component)footer);
        return (Component)mainContainer;
    }


    protected HtmlBasedComponent pagingToolbar()
    {
        this.paging = new Paging();
        this.paging.addEventListener("onPaging", (EventListener)new Object(this));
        this.paging.setAutohide(false);
        this.paging.setDetailed(false);
        this.paging.setPageSize(getPageSize());
        this.paging.setVisible(false);
        return (HtmlBasedComponent)this.paging;
    }


    protected void loadViewComponents()
    {
        this.listView = loadListView();
        this.advancedSearchComponent = loadAdvancedSearchView();
        this.mainViewComponent = loadMainViewCompoent();
    }


    protected UIListView loadListView()
    {
        if(this.tableModel != null)
        {
            ((DefaultListModel)this.tableModel.getListComponentModel().getListModel()).clearAndAddAll(getSearchResult());
            this.listView = (UIListView)new Object(this);
            this.listView.setSclass("listViewInSelectorContext");
            this.listView.setModel(this.tableModel);
            this.listView.setHeight("250px");
            this.listView.setWidth("100%");
            if(this.listViewController != null)
            {
                this.listViewController.unregisterListeners();
            }
            this.listViewController = createListViewSelectorController(this.listView);
            this.listViewController.initialize();
        }
        return this.listView;
    }


    protected ComponentController createListViewSelectorController(UIListView listView)
    {
        return (ComponentController)new DefaultListViewSelectorController(this, listView);
    }


    protected void doSearch(ObjectTemplate objectType, AdvancedSearchParameterContainer parameterContainer, int currentPage)
    {
        List<SearchParameterValue> searchValueParameters = new ArrayList<>();
        List<List<SearchParameterValue>> orValues = new LinkedList<>();
        List<SearchType> searchTypes = Collections.singletonList(UISessionUtils.getCurrentSession().getSearchService()
                        .getSearchType(objectType));
        int offsetStart = currentPage * getPageSize();
        Query searchQuery = new Query(searchTypes, null, offsetStart, getPageSize());
        searchQuery.setContextParameter("objectTemplate", objectType);
        createSearchParameterValues(parameterContainer, searchValueParameters, orValues);
        searchQuery.setParameterValues(searchValueParameters);
        searchQuery.setParameterOrValues(orValues);
        searchQuery.setSortCriteria(Collections.singletonMap(parameterContainer.getSortProperty(),
                        Boolean.valueOf(parameterContainer.isSortAscending())));
        searchQuery.setExcludeSubTypes(parameterContainer.isExcludeSubtypes());
        ExtendedSearchResult searchResult = getSearchProvider().search(searchQuery);
        ((DefaultListModel)this.tableModel.getListComponentModel().getListModel()).clearAndAddAll(searchResult.getResult());
        setSearchResult(searchResult.getResult(), searchResult.getTotalCount());
    }


    public void setSearchResult(List<? extends Object> searchResult, int total)
    {
        if(this.searchResult != searchResult)
        {
            this.searchResult = searchResult;
            this.resultListContainer.appendChild((Component)this.listView);
            UITools.detachChildren((Component)this.resultListContainer);
            this.resultListContainer.appendChild((Component)this.listView);
            if(!searchResult.isEmpty())
            {
                this.paging.setVisible(true);
            }
            this.listView.updateItems();
            this.paging.setTotalSize(total);
        }
    }


    protected void createSearchParameterValues(AdvancedSearchParameterContainer parameterContainer, List<SearchParameterValue> paramValues, List<List<SearchParameterValue>> orValues)
    {
        AdvancedSearchHelper.createSearchParameterValues((AdvancedSearchModel)this.advancedSearchModel, parameterContainer, paramValues, orValues);
    }


    protected UIAdvancedSearchView loadAdvancedSearchView()
    {
        this.advancedSearchComponent = (UIAdvancedSearchView)new AdvancedSearchView();
        this.advancedSearchComponent.setModel((AdvancedSearchModel)this.advancedSearchModel);
        if(this.advancedSearchController != null)
        {
            this.advancedSearchController.unregisterListeners();
        }
        this.advancedSearchController = (ComponentController)new DefaultSelectorAdvancedSearchController(this, (AdvancedSearchModel)this.advancedSearchModel, this.advancedSearchComponent);
        this.advancedSearchController.initialize();
        return this.advancedSearchComponent;
    }


    public void initAdvanceModeModels()
    {
        this.advancedSearchModel = createAdvancedTableModel();
        SearchFieldGroup rootGroup = this.advancedSearchModel.getRootSearchFieldGroup();
        for(SearchField searchField : rootGroup.getSearchFields())
        {
            if(searchField.getName().equalsIgnoreCase("media.mime"))
            {
                StringBuilder propertyBuilder = new StringBuilder();
                String rawMimeTypes = (String)this.parameters.get("mimeTypes");
                if(StringUtils.isNotBlank(rawMimeTypes))
                {
                    for(String singleMimeType : rawMimeTypes.split(";"))
                    {
                        propertyBuilder.append(singleMimeType.trim()).append(";");
                    }
                    String mimeType = propertyBuilder.toString();
                    if(mimeType.endsWith(";"))
                    {
                        mimeType = StringUtils.substringBeforeLast(mimeType, ";");
                    }
                    this.advancedSearchModel.getParameterContainer().put(searchField,
                                    AdvancedSearchHelper.createSimpleConditionValue(mimeType));
                }
                break;
            }
        }
        this.tableModel = (TableModel)createDefaultTableModel();
        if(this.tableModel != null)
        {
            this.tableModel.getColumnComponentModel().addColumnModelListener(getColumnModelListener());
        }
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
                ObjectTemplate objectTemplate = getTypeService().getObjectTemplate(actualType.getCode());
                listViewConfig = getListViewConfiguration(objectTemplate, "listViewSelector");
            }
            catch(Exception e)
            {
                LOG.warn("Could not load list view configuration (Reason: '" + e.getMessage() + "').");
                return null;
            }
            DefaultListComponentModel defaultListComponentModel = new DefaultListComponentModel();
            defaultListComponentModel.setEditable(true);
            defaultListComponentModel.setSelectable(true);
            defaultListComponentModel.setActivatable(false);
            defaultListComponentModel.setMultiple(isMultiple());
            defaultListComponentModel.setForceRenderOnSelectionChanged(true);
            DefaultListModel defaultListModel = new DefaultListModel();
            defaultListComponentModel.setListModel((ListModel)defaultListModel);
            DefaultColumnModel defaultColumnModel = new DefaultColumnModel(listViewConfig);
            defaultTableModel1 = new DefaultTableModel((MutableListModel)defaultListComponentModel, (MutableColumnModel)defaultColumnModel);
        }
        return (MutableTableModel)defaultTableModel1;
    }


    public DefaultAdvancedSearchModel createAdvancedTableModel()
    {
        ObjectTemplate rootTemplate = getTypeService().getObjectTemplate(getRootSearchType().getCode());
        AdvancedSearchConfiguration config = getAdvancedSearchConfiguration(rootTemplate, "advancedSearch");
        addPredefinedFields(config);
        DefaultAdvancedSearchModel searchModel = new DefaultAdvancedSearchModel(config, "advancedSearch");
        addPredefinedValues(searchModel);
        return searchModel;
    }


    protected void addPredefinedFields(AdvancedSearchConfiguration config)
    {
        SearchFieldGroupConfiguration rootGroup = config.getRootGroupConfiguration();
        if(getWizard() != null && rootGroup instanceof DefaultSearchFieldGroupConfiguration)
        {
            Map<String, Object> initialValues = getWizard().getPredefinedValues();
            if(initialValues != null)
            {
                Map<String, SearchFieldConfiguration> searchFieldMap = getSearchFieldMap(rootGroup
                                .getAllSearchFieldConfigurations());
                for(Map.Entry<String, Object> entry : initialValues.entrySet())
                {
                    PropertyDescriptor propertyDescriptor = getTypeService().getPropertyDescriptor(entry.getKey());
                    SearchFieldConfiguration field = searchFieldMap.get(propertyDescriptor.getQualifier().toLowerCase());
                    if(field == null)
                    {
                        ((DefaultSearchFieldGroupConfiguration)rootGroup)
                                        .addSearchFieldConfiguration((SearchFieldConfiguration)new PropertySearchFieldConfiguration((PropertyDescriptor)UISessionUtils.getCurrentSession()
                                                        .getSearchService().getSearchDescriptor(propertyDescriptor)));
                        continue;
                    }
                    field.setVisible(true);
                }
            }
        }
    }


    protected Map<String, SearchFieldConfiguration> getSearchFieldMap(Collection<SearchFieldConfiguration> fields)
    {
        Map<String, SearchFieldConfiguration> ret = new HashMap<>();
        for(SearchFieldConfiguration field : fields)
        {
            ret.put(field.getPropertyDescriptor().getQualifier().toLowerCase(), field);
        }
        return ret;
    }


    protected void addPredefinedValues(DefaultAdvancedSearchModel searchModel)
    {
        if(getWizard() != null)
        {
            Map<String, Object> initialValues = getWizard().getPredefinedValues();
            if(initialValues != null)
            {
                for(Map.Entry<String, Object> entry : initialValues.entrySet())
                {
                    PropertyDescriptor propertyDescriptor = getTypeService().getPropertyDescriptor(entry.getKey());
                    SearchField field = searchModel.getSearchField(propertyDescriptor);
                    if(field != null)
                    {
                        try
                        {
                            searchModel.getParameterContainer().put(field,
                                            AdvancedSearchHelper.createSimpleConditionValue(TypeTools.container2Item(getTypeService(), entry
                                                            .getValue())));
                        }
                        catch(Exception e)
                        {
                            LOG.error("error prefilling condition value for search field [" + field + "]", e);
                        }
                    }
                }
            }
        }
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
            if(getRootSearchType() instanceof ObjectTemplate)
            {
                DefaultColumnModel columnModel = (DefaultColumnModel)getTableModel().getColumnComponentModel();
                ListViewConfiguration configuration = columnModel.getConfiguration();
                getUIConfigurationService().setLocalComponentConfiguration((UIComponentConfiguration)configuration,
                                UISessionUtils.getCurrentSession().getUser(), (ObjectTemplate)getRootSearchType(), "listViewSelector", ListViewConfiguration.class);
            }
        }
    }


    public BrowserModel getBrowserModel()
    {
        return UISessionUtils.getCurrentSession().getCurrentPerspective().getBrowserArea().getFocusedBrowser();
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


    public void clearCurrentSearchResult()
    {
        if(this.searchResult != null && !this.searchResult.isEmpty())
        {
            this.searchResult.clear();
        }
    }


    protected SearchProvider getSearchProvider()
    {
        if(this.searchProvider == null)
        {
            this.searchProvider = UISessionUtils.getCurrentSession().getSearchService().getSearchProvider();
        }
        return this.searchProvider;
    }


    protected UIConfigurationService getUIConfigurationService()
    {
        if(this.uiConfigurationService == null)
        {
            this.uiConfigurationService = (UIConfigurationService)SpringUtil.getBean("uiConfigurationService");
        }
        return this.uiConfigurationService;
    }


    protected TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = UISessionUtils.getCurrentSession().getTypeService();
        }
        return this.typeService;
    }


    public void initView(Wizard wizard, Component comp)
    {
        wizard.resize();
        super.initView(wizard, comp);
    }


    protected boolean isMultiple()
    {
        return (this.multiple || Boolean.TRUE.equals(Boolean.valueOf((String)getParameters().get("multiple"))));
    }


    public void setMultiple(boolean multiple)
    {
        this.multiple = multiple;
    }


    public Map<String, ? extends Object> getParameters()
    {
        if(this.parameters == null)
        {
            this.parameters = new HashMap<>();
        }
        return this.parameters;
    }


    public void fireSearch()
    {
        ((AdvancedSearchView)this.advancedSearchComponent).fireSearch();
    }
}
