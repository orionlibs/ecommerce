package de.hybris.platform.adaptivesearchbackoffice.widgets.navigationcontext;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.widgets.controller.collapsiblecontainer.CollapsibleContainerState;
import de.hybris.platform.adaptivesearch.data.AsIndexConfigurationData;
import de.hybris.platform.adaptivesearch.data.AsIndexTypeData;
import de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProvider;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProviderFactory;
import de.hybris.platform.adaptivesearchbackoffice.data.CatalogVersionData;
import de.hybris.platform.adaptivesearchbackoffice.data.CategoryData;
import de.hybris.platform.adaptivesearchbackoffice.data.NavigationContextData;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;

public class NavigationContextController extends DefaultWidgetController
{
    protected static final String INDEX_CONFIGURATION_SELECTOR_ID = "indexConfigurationSelector";
    protected static final String INDEX_TYPE_SELECTOR_ID = "indexTypeSelector";
    protected static final String CATALOG_VERSION_SELECTOR_ID = "catalogVersionSelector";
    protected static final String SEARCH_PROFILE_SELECTOR_ID = "searchProfileSelector";
    protected static final String ON_VALUE_CHANGED = "onValueChanged";
    protected static final String CATEGORY_IN_SOCKET = "category";
    protected static final String COLLAPSE_STATE_OUT_SOCKET = "collapseState";
    protected static final String NAVIGATION_CONTEXT_KEY = "navigationContext";
    protected static final String INDEX_TYPE_KEY = "indexType";
    protected static final String CATALOG_VERSION_KEY = "catalogVersion";
    protected static final String SEARCH_PROFILE_CREATE_WIZARD_CTX = "create-wizard-with-parent";
    @WireVariable
    protected transient SessionService sessionService;
    @WireVariable
    protected transient I18NService i18nService;
    @WireVariable
    protected transient CatalogVersionService catalogVersionService;
    @WireVariable
    protected transient AsSearchProviderFactory asSearchProviderFactory;
    @WireVariable
    protected transient LabelService labelService;
    protected Combobox indexConfigurationSelector;
    protected Combobox indexTypeSelector;
    protected Combobox catalogVersionSelector;
    protected Editor searchProfileSelector;
    private final ListModelList<IndexConfigurationModel> indexConfigurationsModel = new ListModelList();
    private final ListModelList<IndexTypeModel> indexTypesModel = new ListModelList();
    private final ListModelList<CatalogVersionModel> catalogVersionsModel = new ListModelList();


    public ListModelList<IndexConfigurationModel> getIndexConfigurationsModel()
    {
        return this.indexConfigurationsModel;
    }


    public ListModelList<IndexTypeModel> getIndexTypesModel()
    {
        return this.indexTypesModel;
    }


    public ListModelList<CatalogVersionModel> getCatalogVersionsModel()
    {
        return this.catalogVersionsModel;
    }


    public NavigationContextData getNavigationContext()
    {
        return (NavigationContextData)getModel().getValue("navigationContext", NavigationContextData.class);
    }


    public void setNavigationContext(NavigationContextData navigationContext)
    {
        getModel().put("navigationContext", navigationContext);
    }


    public void initialize(Component component)
    {
        initializeSelectors();
        component.addEventListener("onCreate", event -> {
            NavigationContextData navigationContext = getNavigationContext();
            updateSelectors(navigationContext);
            sendNavigationContext(navigationContext);
        });
    }


    protected void initializeSelectors()
    {
        NavigationContextData navigationContext = new NavigationContextData();
        navigationContext.setIndexConfiguration(null);
        navigationContext.setIndexType(null);
        navigationContext.setCatalogVersion(null);
        navigationContext.setSearchProfiles(null);
        navigationContext.setCurrentSearchProfile(null);
        setNavigationContext(navigationContext);
        this.indexConfigurationSelector.setModel((ListModel)this.indexConfigurationsModel);
        this.indexTypeSelector.setModel((ListModel)this.indexTypesModel);
        this.catalogVersionSelector.setModel((ListModel)this.catalogVersionsModel);
        this.searchProfileSelector.setValue(null);
    }


    protected void updateSelectors(NavigationContextData navigationContext)
    {
        this.sessionService.executeInLocalView((SessionExecutionBody)new Object(this, navigationContext));
    }


    protected void updateIndexConfigurations(NavigationContextData navigationContext)
    {
        Set<IndexConfigurationModel> selection = this.indexConfigurationsModel.getSelection();
        IndexConfigurationModel selectedIndexConfiguration = selection.isEmpty() ? null : selection.iterator().next();
        List<IndexConfigurationModel> indexConfigurations = findIndexConfigurations();
        indexConfigurations.add(0, (IndexConfigurationModel)null);
        if(!CollectionUtils.isEqualCollection(this.indexConfigurationsModel.getInnerList(), indexConfigurations))
        {
            this.indexConfigurationsModel.clear();
            this.indexConfigurationsModel.addAll(indexConfigurations);
        }
        if(CollectionUtils.isEmpty((Collection)this.indexConfigurationsModel))
        {
            navigationContext.setIndexConfiguration(null);
            this.indexConfigurationsModel.setSelection(Collections.emptyList());
        }
        else if(StringUtils.isBlank(navigationContext.getIndexConfiguration()) ||
                        !this.indexConfigurationsModel.contains(selectedIndexConfiguration))
        {
            selectedIndexConfiguration = indexConfigurations.get(0);
            navigationContext
                            .setIndexConfiguration((selectedIndexConfiguration == null) ? null : selectedIndexConfiguration.getCode());
            this.indexConfigurationsModel.setSelection(Collections.singletonList(selectedIndexConfiguration));
        }
    }


    protected void updateIndexTypes(NavigationContextData navigationContext)
    {
        Set<IndexTypeModel> selection = this.indexTypesModel.getSelection();
        IndexTypeModel selectedIndexType = selection.isEmpty() ? null : selection.iterator().next();
        List<IndexTypeModel> indexTypes = findIndexTypes(navigationContext.getIndexConfiguration());
        if(!CollectionUtils.isEqualCollection(this.indexTypesModel.getInnerList(), indexTypes))
        {
            this.indexTypesModel.clear();
            this.indexTypesModel.addAll(indexTypes);
        }
        if(CollectionUtils.isEmpty((Collection)this.indexTypesModel))
        {
            navigationContext.setIndexType(null);
            this.indexTypesModel.setSelection(Collections.emptyList());
            this.indexTypeSelector.setDisabled(true);
        }
        else if(StringUtils.isBlank(navigationContext.getIndexConfiguration()) || !this.indexTypesModel.contains(selectedIndexType))
        {
            selectedIndexType = indexTypes.get(0);
            navigationContext.setIndexType(selectedIndexType.getCode());
            this.indexTypesModel.setSelection(Collections.singletonList(selectedIndexType));
            this.indexTypeSelector.setDisabled(false);
        }
    }


    protected void updateCatalogVersions(NavigationContextData navigationContext)
    {
        Set<CatalogVersionModel> selection = this.catalogVersionsModel.getSelection();
        CatalogVersionModel selectedCatalogVersion = selection.isEmpty() ? null : selection.iterator().next();
        List<CatalogVersionModel> catalogVersions = findCatalogVersions(navigationContext.getIndexConfiguration(), navigationContext
                        .getIndexType());
        if(!CollectionUtils.isEqualCollection(this.catalogVersionsModel.getInnerList(), catalogVersions))
        {
            this.catalogVersionsModel.clear();
            this.catalogVersionsModel.addAll(catalogVersions);
        }
        if(CollectionUtils.isEmpty((Collection)this.catalogVersionsModel))
        {
            navigationContext.setCatalogVersion(null);
            this.catalogVersionsModel.setSelection(Collections.emptyList());
            this.catalogVersionSelector.setDisabled(true);
        }
        else if(navigationContext.getCatalogVersion() == null || !this.catalogVersionsModel.contains(selectedCatalogVersion))
        {
            Optional<CatalogVersionModel> activeCatalogVersion = catalogVersions.stream().filter(CatalogVersionModel::isActive).findFirst();
            selectedCatalogVersion = activeCatalogVersion.isPresent() ? activeCatalogVersion.get() : catalogVersions.get(0);
            navigationContext.setCatalogVersion(selectedCatalogVersion.getCatalogVersion());
            this.catalogVersionsModel.setSelection(Collections.singletonList(selectedCatalogVersion));
            this.catalogVersionSelector.setDisabled(false);
        }
    }


    protected void updateSearchProfiles(NavigationContextData navigationContext)
    {
        CatalogVersionData catalogVersionData = navigationContext.getCatalogVersion();
        CatalogVersionModel catalogVersion = (catalogVersionData == null) ? null : this.catalogVersionService.getCatalogVersion(catalogVersionData.getCatalogId(), catalogVersionData.getVersion());
        AbstractAsSearchProfileModel searchProfile = (AbstractAsSearchProfileModel)this.searchProfileSelector.getValue();
        if(searchProfile == null || !Objects.equals(searchProfile.getIndexType(), navigationContext.getIndexType()) ||
                        !Objects.equals(searchProfile.getCatalogVersion(), catalogVersion))
        {
            navigationContext.setSearchProfiles(Collections.emptyList());
            navigationContext.setCurrentSearchProfile(null);
            Object parentObject = createParentObject(navigationContext);
            this.searchProfileSelector.setAttribute("parentObject", parentObject);
            this.searchProfileSelector.setValue(null);
            this.searchProfileSelector.getParameters().put("configurableFlowConfigCtx", "create-wizard-with-parent");
            this.searchProfileSelector.getParameters().put("referenceSearchCondition_indexType", navigationContext.getIndexType());
            this.searchProfileSelector.getParameters().put("referenceSearchCondition_catalogVersion", catalogVersion);
            this.searchProfileSelector.reload();
        }
        this.searchProfileSelector.setReadOnly((navigationContext.getIndexConfiguration() == null));
    }


    protected Object createParentObject(NavigationContextData navigationContext)
    {
        String indexType = navigationContext.getIndexType();
        CatalogVersionModel catalogVersion = resolveCatalogVersion(navigationContext
                        .getCatalogVersion());
        Map<String, Object> parentObject = new HashMap<>();
        parentObject.put("indexType", indexType);
        parentObject.put("catalogVersion", catalogVersion);
        return parentObject;
    }


    protected void sendNavigationContext(NavigationContextData navigationContext)
    {
        sendOutput("navigationContext", navigationContext);
    }


    protected void collapseNavigationContext()
    {
        sendOutput("collapseState", new CollapsibleContainerState(Boolean.FALSE, Boolean.TRUE, Boolean.FALSE));
    }


    @ViewEvent(componentID = "indexConfigurationSelector", eventName = "onSelect")
    public void onIndexConfigurationChanged(SelectEvent<Comboitem, String> event)
    {
        NavigationContextData navigationContext = getNavigationContext();
        String newIndexConfiguration = (String)((Comboitem)event.getReference()).getValue();
        if(navigationContext != null && !Objects.equals(navigationContext.getIndexConfiguration(), newIndexConfiguration))
        {
            navigationContext.setIndexConfiguration(newIndexConfiguration);
            updateSelectors(navigationContext);
            sendNavigationContext(navigationContext);
        }
    }


    @ViewEvent(componentID = "indexTypeSelector", eventName = "onSelect")
    public void onIndexTypeChanged(SelectEvent<Comboitem, String> event)
    {
        NavigationContextData navigationContext = getNavigationContext();
        String newIndexType = (String)((Comboitem)event.getReference()).getValue();
        if(navigationContext != null && !Objects.equals(navigationContext.getIndexType(), newIndexType))
        {
            navigationContext.setIndexType(newIndexType);
            updateSelectors(navigationContext);
            sendNavigationContext(navigationContext);
        }
    }


    @ViewEvent(componentID = "catalogVersionSelector", eventName = "onSelect")
    public void onCatalogVersionChanged(SelectEvent<Comboitem, CatalogVersionData> event)
    {
        NavigationContextData navigationContext = getNavigationContext();
        CatalogVersionData newCatalogVersion = (CatalogVersionData)((Comboitem)event.getReference()).getValue();
        if(navigationContext != null && !Objects.equals(navigationContext.getCatalogVersion(), newCatalogVersion))
        {
            navigationContext.setCatalogVersion(newCatalogVersion);
            updateSelectors(navigationContext);
            sendNavigationContext(navigationContext);
        }
    }


    @ViewEvent(componentID = "searchProfileSelector", eventName = "onValueChanged")
    public void onSearchProfileChanged()
    {
        NavigationContextData navigationContext = getNavigationContext();
        AbstractAsSearchProfileModel newSearchProfile = (AbstractAsSearchProfileModel)this.searchProfileSelector.getValue();
        String newSearchProfileCode = (newSearchProfile != null) ? newSearchProfile.getCode() : null;
        if(navigationContext != null && !Objects.equals(navigationContext.getCurrentSearchProfile(), newSearchProfileCode))
        {
            if(newSearchProfile == null)
            {
                navigationContext.setSearchProfiles(Collections.emptyList());
                navigationContext.setCurrentSearchProfile(null);
            }
            else
            {
                navigationContext.setSearchProfiles(Collections.singletonList(newSearchProfile.getCode()));
                navigationContext.setCurrentSearchProfile(newSearchProfile.getCode());
                collapseNavigationContext();
            }
            updateSelectors(navigationContext);
            sendNavigationContext(navigationContext);
        }
    }


    @SocketEvent(socketId = "category")
    public void onCategoryChanged(CategoryData category)
    {
        NavigationContextData navigationContext = getNavigationContext();
        if(navigationContext != null && !Objects.equals(navigationContext.getCategory(), category))
        {
            navigationContext.setCategory(category);
            sendNavigationContext(navigationContext);
        }
    }


    protected List<IndexConfigurationModel> findIndexConfigurations()
    {
        AsSearchProvider searchProvider = this.asSearchProviderFactory.getSearchProvider();
        List<AsIndexConfigurationData> indexConfigurations = searchProvider.getIndexConfigurations();
        return (List<IndexConfigurationModel>)indexConfigurations.stream().filter(this::isValidIndexConfiguration).map(this::convertIndexConfiguration)
                        .sorted(this::compareIndexConfigurations).collect(Collectors.toList());
    }


    protected boolean isValidIndexConfiguration(AsIndexConfigurationData indexConfiguration)
    {
        return (indexConfiguration != null && StringUtils.isNotBlank(indexConfiguration.getCode()));
    }


    protected IndexConfigurationModel convertIndexConfiguration(AsIndexConfigurationData source)
    {
        String name = (source.getName() != null) ? source.getName() : source.getCode();
        IndexConfigurationModel target = new IndexConfigurationModel();
        target.setCode(source.getCode());
        target.setName(name);
        return target;
    }


    protected int compareIndexConfigurations(IndexConfigurationModel indexConfiguration1, IndexConfigurationModel indexConfiguration2)
    {
        return indexConfiguration1.getName().compareTo(indexConfiguration2.getName());
    }


    protected List<IndexTypeModel> findIndexTypes(String indexConfiguration)
    {
        if(indexConfiguration == null)
        {
            return Collections.emptyList();
        }
        AsSearchProvider searchProvider = this.asSearchProviderFactory.getSearchProvider();
        List<AsIndexTypeData> indexTypes = searchProvider.getIndexTypes(indexConfiguration);
        return (List<IndexTypeModel>)indexTypes.stream().filter(this::isValidIndexType).map(this::convertIndexType).sorted(this::compareIndexTypes)
                        .collect(Collectors.toList());
    }


    protected boolean isValidIndexType(AsIndexTypeData indexType)
    {
        return (indexType != null && StringUtils.isNotBlank(indexType.getCode()));
    }


    protected IndexTypeModel convertIndexType(AsIndexTypeData source)
    {
        String name = (source.getName() != null) ? source.getName() : source.getCode();
        IndexTypeModel target = new IndexTypeModel();
        target.setCode(source.getCode());
        target.setName(name);
        return target;
    }


    protected int compareIndexTypes(IndexTypeModel indexType1, IndexTypeModel indexType2)
    {
        return indexType1.getName().compareTo(indexType2.getName());
    }


    protected List<CatalogVersionModel> findCatalogVersions(String indexConfiguration, String indexType)
    {
        if(indexConfiguration == null || indexType == null)
        {
            return Collections.emptyList();
        }
        AsSearchProvider searchProvider = this.asSearchProviderFactory.getSearchProvider();
        List<CatalogVersionModel> catalogVersions = searchProvider.getSupportedCatalogVersions(indexConfiguration, indexType);
        return (List<CatalogVersionModel>)catalogVersions.stream().filter(this::isValidCatalogVersion).map(this::convertCatalogVersion)
                        .sorted(this::compareCatalogVersions).collect(Collectors.toList());
    }


    protected boolean isValidCatalogVersion(CatalogVersionModel catalogVersion)
    {
        return (catalogVersion != null && StringUtils.isNotBlank(catalogVersion.getVersion()) && catalogVersion.getCatalog() != null &&
                        StringUtils.isNotBlank(catalogVersion.getCatalog().getId()));
    }


    protected CatalogVersionModel convertCatalogVersion(CatalogVersionModel source)
    {
        CatalogVersionModel target = new CatalogVersionModel();
        CatalogVersionData catalogVersionData = new CatalogVersionData();
        catalogVersionData.setCatalogId(source.getCatalog().getId());
        catalogVersionData.setVersion(source.getVersion());
        target.setCatalogVersion(catalogVersionData);
        target.setActive(BooleanUtils.toBoolean(source.getActive()));
        target.setName(this.labelService.getObjectLabel(source));
        return target;
    }


    protected int compareCatalogVersions(CatalogVersionModel catalogVersion1, CatalogVersionModel catalogVersion2)
    {
        return catalogVersion1.getName().compareTo(catalogVersion2.getName());
    }


    protected CatalogVersionModel resolveCatalogVersion(CatalogVersionData catalogVersion)
    {
        if(catalogVersion == null)
        {
            return null;
        }
        return this.catalogVersionService.getCatalogVersion(catalogVersion.getCatalogId(), catalogVersion.getVersion());
    }
}
