package de.hybris.platform.adaptivesearchbackoffice.widgets.maincontroller;

import com.hybris.cockpitng.annotations.GlobalCockpitEvent;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import de.hybris.platform.adaptivesearch.AsException;
import de.hybris.platform.adaptivesearch.context.AsSearchProfileContext;
import de.hybris.platform.adaptivesearch.data.AsFacetData;
import de.hybris.platform.adaptivesearch.data.AsSearchQueryData;
import de.hybris.platform.adaptivesearch.data.AsSearchResultData;
import de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel;
import de.hybris.platform.adaptivesearch.model.AsFacetRangeModel;
import de.hybris.platform.adaptivesearch.services.AsCategoryService;
import de.hybris.platform.adaptivesearch.services.AsSearchProfileActivationService;
import de.hybris.platform.adaptivesearch.services.AsSearchProfileService;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProvider;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProviderFactory;
import de.hybris.platform.adaptivesearchbackoffice.data.AbstractSearchRequestData;
import de.hybris.platform.adaptivesearchbackoffice.data.AsCategoryData;
import de.hybris.platform.adaptivesearchbackoffice.data.CatalogVersionData;
import de.hybris.platform.adaptivesearchbackoffice.data.FacetFiltersRequestData;
import de.hybris.platform.adaptivesearchbackoffice.data.FacetRequestData;
import de.hybris.platform.adaptivesearchbackoffice.data.FacetStateData;
import de.hybris.platform.adaptivesearchbackoffice.data.NavigationContextData;
import de.hybris.platform.adaptivesearchbackoffice.data.PaginationRequestData;
import de.hybris.platform.adaptivesearchbackoffice.data.SearchContextData;
import de.hybris.platform.adaptivesearchbackoffice.data.SearchRequestData;
import de.hybris.platform.adaptivesearchbackoffice.data.SearchResultData;
import de.hybris.platform.adaptivesearchbackoffice.data.SearchStateData;
import de.hybris.platform.adaptivesearchbackoffice.data.SortRequestData;
import de.hybris.platform.adaptivesearchbackoffice.facades.AsCategoryFacade;
import de.hybris.platform.adaptivesearchbackoffice.facades.AsSearchProfileContextFacade;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Label;

public class MainController extends DefaultWidgetController
{
    private static final Logger LOG = LoggerFactory.getLogger(MainController.class);
    protected static final String SOCKET_IN_SEARCH_TEXT = "searchText";
    protected static final String SOCKET_IN_REFRESH_SEARCH = "refreshSearch";
    protected static final String SOCKET_IN_PAGINATION_REQUEST = "paginationRequest";
    protected static final String SOCKET_OUT_CLEAR_QUERY = "clearQuery";
    protected static final String NAVIGATION_CONTEXT_KEY = "navigationContext";
    protected static final String SEARCH_CONTEXT_KEY = "searchContext";
    protected static final String SEARCH_STATE_KEY = "searchState";
    protected static final String SETTING_DEFAULT_PAGE_SIZE = "defaultPageSize";
    @WireVariable
    protected transient SessionService sessionService;
    @WireVariable
    protected transient I18NService i18nService;
    @WireVariable
    protected transient CatalogVersionService catalogVersionService;
    @WireVariable
    protected transient AsSearchProfileService asSearchProfileService;
    @WireVariable
    protected transient AsSearchProfileActivationService asSearchProfileActivationService;
    @WireVariable
    protected transient AsCategoryService asCategoryService;
    @WireVariable
    protected transient AsSearchProviderFactory asSearchProviderFactory;
    @WireVariable
    protected transient AsSearchProfileContextFacade asSearchProfileContextFacade;
    @WireVariable
    protected transient AsCategoryFacade asCategoryFacade;
    @Wire
    protected Label categoryBreadcrumbs;


    protected NavigationContextData getNavigationContext()
    {
        return (NavigationContextData)getModel().getValue("navigationContext", NavigationContextData.class);
    }


    protected void setNavigationContext(NavigationContextData navigationContext)
    {
        getModel().put("navigationContext", navigationContext);
    }


    protected SearchContextData getSearchContext()
    {
        return (SearchContextData)getModel().getValue("searchContext", SearchContextData.class);
    }


    protected void setSearchContext(SearchContextData searchContext)
    {
        getModel().put("searchContext", searchContext);
    }


    protected SearchStateData getSearchState()
    {
        return (SearchStateData)getModel().getValue("searchState", SearchStateData.class);
    }


    protected void setSearchState(SearchStateData searchState)
    {
        getModel().put("searchState", searchState);
    }


    public void initialize(Component comp)
    {
        setSearchState(createSearchState());
    }


    protected void search()
    {
        SearchResultData searchResult = createSearchResult();
        NavigationContextData navigationContext = getNavigationContext();
        SearchContextData searchContext = getSearchContext();
        if(navigationContext == null || searchContext == null)
        {
            return;
        }
        if(navigationContext.getIndexConfiguration() == null || navigationContext.getIndexType() == null)
        {
            sendOutput("searchResult", searchResult);
            return;
        }
        try
        {
            CatalogVersionModel catalogVersion = resolveCatalogVersion(navigationContext);
            List<AbstractAsSearchProfileModel> searchProfiles = resolveSearchProfiles(catalogVersion, navigationContext);
            this.asSearchProfileActivationService.setCurrentSearchProfiles(searchProfiles);
            List<CategoryModel> categoryPath = resolveCategoryPath(catalogVersion, navigationContext);
            this.asCategoryService.setCurrentCategoryPath(categoryPath);
            AsSearchProfileContext searchProfileContext = this.asSearchProfileContextFacade.createSearchProfileContext(navigationContext, searchContext);
            SearchStateData searchState = getSearchState();
            AsSearchQueryData searchQuery = searchState.getSearchQuery();
            AsSearchProvider searchProvider = this.asSearchProviderFactory.getSearchProvider();
            AsSearchResultData asSearchResult = searchProvider.search(searchProfileContext, searchQuery);
            modifySearchResult(asSearchResult, searchState);
            searchResult.setAsSearchResult(asSearchResult);
            searchQuery.setSort((asSearchResult.getCurrentSort() != null) ? asSearchResult.getCurrentSort().getCode() : null);
            sendOutput("searchResult", searchResult);
        }
        catch(AsException e)
        {
            LOG.error(e.getMessage(), (Throwable)e);
            sendOutput("searchResult", searchResult);
        }
        finally
        {
            this.asSearchProfileActivationService.clearCurrentSearchProfiles();
        }
    }


    protected void modifySearchResult(AsSearchResultData searchResult, SearchStateData searchState)
    {
        if(CollectionUtils.isNotEmpty(searchResult.getFacets()))
        {
            Map<String, FacetStateData> facetsState = searchState.getFacetsState();
            for(AsFacetData facet : searchResult.getFacets())
            {
                FacetStateData facetState = facetsState.get(facet.getIndexProperty());
                if(facetState != null)
                {
                    facet.setVisibility(facetState.getFacetVisibility());
                }
            }
        }
    }


    protected SearchStateData createSearchState()
    {
        AsSearchQueryData searchQuery = new AsSearchQueryData();
        searchQuery.setActivePage(0);
        searchQuery.setPageSize(getWidgetSettings().getInt("defaultPageSize"));
        searchQuery.setFacetValues(new HashMap<>());
        SearchStateData searchState = new SearchStateData();
        searchState.setSearchQuery(searchQuery);
        searchState.setFacetsState(new HashMap<>());
        return searchState;
    }


    protected SearchResultData createSearchResult()
    {
        SearchResultData searchResult = new SearchResultData();
        searchResult.setNavigationContext(getNavigationContext());
        searchResult.setSearchContext(getSearchContext());
        return searchResult;
    }


    protected CatalogVersionModel resolveCatalogVersion(NavigationContextData navigationContext)
    {
        if(navigationContext == null || navigationContext.getCatalogVersion() == null)
        {
            return null;
        }
        CatalogVersionData catalogVersion = navigationContext.getCatalogVersion();
        return this.catalogVersionService.getCatalogVersion(catalogVersion.getCatalogId(), catalogVersion.getVersion());
    }


    protected List<AbstractAsSearchProfileModel> resolveSearchProfiles(CatalogVersionModel catalogVersion, NavigationContextData navigationContext)
    {
        if(navigationContext == null || CollectionUtils.isEmpty(navigationContext.getSearchProfiles()))
        {
            return Collections.emptyList();
        }
        List<String> searchProfileCodes = navigationContext.getSearchProfiles();
        return (List<AbstractAsSearchProfileModel>)searchProfileCodes.stream().map(code -> (AbstractAsSearchProfileModel)this.asSearchProfileService.getSearchProfileForCode(catalogVersion, code).get())
                        .collect(Collectors.toList());
    }


    protected List<CategoryModel> resolveCategoryPath(CatalogVersionModel catalogVersion, NavigationContextData navigationContext)
    {
        if(navigationContext == null || navigationContext.getCategory() == null ||
                        CollectionUtils.isEmpty(navigationContext.getCategory().getPath()))
        {
            return Collections.emptyList();
        }
        List<String> categoryCodes = navigationContext.getCategory().getPath();
        return this.asCategoryService.buildCategoryPath(categoryCodes, List.of(catalogVersion), true);
    }


    @SocketEvent(socketId = "searchText")
    public void refreshSearchText(String searchText)
    {
        int currentPageSize = getSearchState().getSearchQuery().getPageSize();
        SearchStateData searchState = createSearchState();
        AsSearchQueryData searchQuery = searchState.getSearchQuery();
        searchQuery.setPageSize(currentPageSize);
        searchQuery.setQuery(searchText);
        setSearchState(searchState);
        search();
    }


    @SocketEvent(socketId = "refreshSearch")
    public void refreshSearch()
    {
        search();
    }


    @GlobalCockpitEvent(eventName = "objectsUpdated", scope = "session")
    public void handleObjectsUpdatedEvent(CockpitEvent event)
    {
        if(canHandleEvent(event))
        {
            refreshSearch();
        }
    }


    protected boolean canHandleEvent(CockpitEvent event)
    {
        Objects.requireNonNull(AsFacetRangeModel.class);
        return (event != null && CollectionUtils.isNotEmpty(event.getDataAsCollection()) && event.getDataAsCollection().stream().anyMatch(AsFacetRangeModel.class::isInstance));
    }


    @SocketEvent(socketId = "navigationContext")
    public void refreshNavigationContext(NavigationContextData navigationContext)
    {
        sendOutput("clearQuery", "");
        setNavigationContext(navigationContext);
        setSearchContext(null);
        this.sessionService.executeInLocalView((SessionExecutionBody)new Object(this, navigationContext));
        int currentPageSize = getSearchState().getSearchQuery().getPageSize();
        SearchStateData searchState = createSearchState();
        AsSearchQueryData searchQuery = searchState.getSearchQuery();
        if(currentPageSize != 0)
        {
            searchQuery.setPageSize(currentPageSize);
        }
        setSearchState(searchState);
        search();
    }


    protected void buildCategoryBreadcrumbs(NavigationContextData navigationContext)
    {
        CatalogVersionData catalogVersion = null;
        List<String> categoryPath = Collections.emptyList();
        if(navigationContext != null)
        {
            if(navigationContext.getCatalogVersion() != null)
            {
                catalogVersion = navigationContext.getCatalogVersion();
            }
            if(navigationContext.getCategory() != null)
            {
                categoryPath = navigationContext.getCategory().getPath();
            }
        }
        List<AsCategoryData> breadcrumbs = (catalogVersion == null) ? this.asCategoryFacade.buildCategoryBreadcrumbs(categoryPath) : this.asCategoryFacade.buildCategoryBreadcrumbs(catalogVersion.getCatalogId(), catalogVersion.getVersion(), categoryPath);
        this.categoryBreadcrumbs.setValue(breadcrumbs.stream().map(AsCategoryData::getName).collect(Collectors.joining(" / ")));
    }


    @SocketEvent(socketId = "searchContext")
    public void refreshSearchContext(SearchContextData searchContext)
    {
        SearchContextData currentSearchContext = getSearchContext();
        if(!Objects.equals(currentSearchContext, searchContext))
        {
            setSearchContext(searchContext);
            search();
        }
    }


    @SocketEvent(socketId = "paginationRequest")
    public void refreshPagination(PaginationRequestData request)
    {
        processPaginationRequest(request);
    }


    @SocketEvent(socketId = "searchRequest")
    public void searchRequest(AbstractSearchRequestData request)
    {
        if(request instanceof SearchRequestData)
        {
            processSearchRequest((SearchRequestData)request);
        }
        else if(request instanceof PaginationRequestData)
        {
            processPaginationRequest((PaginationRequestData)request);
        }
        else if(request instanceof FacetRequestData)
        {
            processFacetRequest((FacetRequestData)request);
        }
        else if(request instanceof FacetFiltersRequestData)
        {
            processFacetFiltersRequest((FacetFiltersRequestData)request);
        }
        else if(request instanceof SortRequestData)
        {
            processSortRequest((SortRequestData)request);
        }
    }


    protected void processSearchRequest(SearchRequestData request)
    {
        search();
    }


    protected void processPaginationRequest(PaginationRequestData request)
    {
        SearchStateData searchState = getSearchState();
        AsSearchQueryData searchQuery = searchState.getSearchQuery();
        searchQuery.setActivePage(request.getActivePage());
        searchQuery.setPageSize(request.getPageSize());
        search();
    }


    protected void processFacetRequest(FacetRequestData request)
    {
        SearchStateData searchState = getSearchState();
        Map<String, FacetStateData> facetsState = searchState.getFacetsState();
        FacetStateData facetState = new FacetStateData();
        facetState.setFacetVisibility(request.getFacetVisibility());
        facetsState.put(request.getIndexProperty(), facetState);
    }


    protected void processFacetFiltersRequest(FacetFiltersRequestData request)
    {
        SearchStateData searchState = getSearchState();
        AsSearchQueryData searchQuery = searchState.getSearchQuery();
        String key = request.getIndexProperty();
        if(CollectionUtils.isEmpty(request.getValues()))
        {
            searchQuery.getFacetValues().remove(key);
        }
        else
        {
            Set<String> values = new HashSet<>(request.getValues());
            searchQuery.getFacetValues().put(key, values);
        }
        search();
    }


    protected void processSortRequest(SortRequestData request)
    {
        SearchStateData searchState = getSearchState();
        AsSearchQueryData searchQuery = searchState.getSearchQuery();
        searchQuery.setSort(request.getSort());
        search();
    }
}
