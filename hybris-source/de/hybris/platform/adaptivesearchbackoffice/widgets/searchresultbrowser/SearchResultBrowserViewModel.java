package de.hybris.platform.adaptivesearchbackoffice.widgets.searchresultbrowser;

import com.hybris.cockpitng.annotations.SocketEvent;
import de.hybris.platform.adaptivesearch.data.AbstractAsBoostItemConfiguration;
import de.hybris.platform.adaptivesearch.data.AbstractAsItemConfiguration;
import de.hybris.platform.adaptivesearch.data.AsConfigurationHolder;
import de.hybris.platform.adaptivesearch.data.AsDocumentData;
import de.hybris.platform.adaptivesearch.data.AsPromotedItem;
import de.hybris.platform.adaptivesearch.data.AsSearchProfileResult;
import de.hybris.platform.adaptivesearch.data.AsSearchResultData;
import de.hybris.platform.adaptivesearch.data.AsSortData;
import de.hybris.platform.adaptivesearch.model.AbstractAsConfigurableSearchConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AbstractAsConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AbstractAsSearchConfigurationModel;
import de.hybris.platform.adaptivesearch.services.AsConfigurationService;
import de.hybris.platform.adaptivesearchbackoffice.common.HTMLSanitizer;
import de.hybris.platform.adaptivesearchbackoffice.data.NavigationContextData;
import de.hybris.platform.adaptivesearchbackoffice.data.PaginationRequestData;
import de.hybris.platform.adaptivesearchbackoffice.data.SearchContextData;
import de.hybris.platform.adaptivesearchbackoffice.data.SearchResultData;
import de.hybris.platform.adaptivesearchbackoffice.data.SortRequestData;
import de.hybris.platform.adaptivesearchbackoffice.facades.AsSearchConfigurationFacade;
import de.hybris.platform.adaptivesearchbackoffice.widgets.AbstractWidgetViewModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.DependsOn;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.ListModelList;

public class SearchResultBrowserViewModel extends AbstractWidgetViewModel
{
    protected static final int DEFAULT_PAGE_SIZE = 20;
    protected static final String RESULT_SCLASS = "yas-result";
    protected static final String PROMOTED_SCLASS = "yas-promoted";
    protected static final String HIGHLIGHT_SCLASS = "yas-highlighted";
    protected static final String SHOW_ON_TOP_SCLASS = "yas-show-on-top";
    protected static final String IN_SEARCH_RESULT_SCLASS = "yas-in-search-result";
    protected static final String PAGINATION_REQUEST_OUT_SOCKET = "paginationRequest";
    protected static final String REFRESH_SEARCH_OUT_SOCKET = "refreshSearch";
    protected static final String SETTING_PAGE_SIZES = "pageSizes";
    protected static final String SEARCH_RESULT_KEY = "searchResult";
    @WireVariable
    protected CommonI18NService commonI18NService;
    @WireVariable
    protected AsConfigurationService asConfigurationService;
    @WireVariable
    protected AsSearchConfigurationFacade asSearchConfigurationFacade;
    private int activePage;
    private int pageSize;
    private int resultCount;
    private final ListModelList<Integer> pageSizes = new ListModelList();
    private final ListModelList<SortModel> sorts = new ListModelList();
    private boolean resultActionsEnabled;
    private final ListModelList<DocumentModel> promotedItems = new ListModelList();
    private final ListModelList<DocumentModel> defaultResults = new ListModelList();


    @DependsOn({"searchResult"})
    public int getActivePage()
    {
        return this.activePage;
    }


    public void setActivePage(int activePage)
    {
        this.activePage = activePage;
    }


    @DependsOn({"searchResult"})
    public int getPageSize()
    {
        return this.pageSize;
    }


    public void setPageSize(int pageSize)
    {
        this.pageSize = pageSize;
    }


    @DependsOn({"searchResult"})
    public int getResultCount()
    {
        return this.resultCount;
    }


    public void setResultCount(int resultCount)
    {
        this.resultCount = resultCount;
    }


    @DependsOn({"searchResult"})
    public ListModelList<Integer> getPageSizes()
    {
        return this.pageSizes;
    }


    @DependsOn({"searchResult"})
    public ListModelList<SortModel> getSorts()
    {
        return this.sorts;
    }


    @DependsOn({"searchResult"})
    public boolean isResultActionsEnabled()
    {
        return this.resultActionsEnabled;
    }


    public void setResultActionsEnabled(boolean resultActionsEnabled)
    {
        this.resultActionsEnabled = resultActionsEnabled;
    }


    @DependsOn({"searchResult"})
    public ListModelList<DocumentModel> getPromotedItems()
    {
        return this.promotedItems;
    }


    @DependsOn({"searchResult"})
    public ListModelList<DocumentModel> getDefaultResults()
    {
        return this.defaultResults;
    }


    public SearchResultData getSearchResult()
    {
        return (SearchResultData)getModel().getValue("searchResult", SearchResultData.class);
    }


    protected void setSearchResult(SearchResultData searchResult)
    {
        getModel().put("searchResult", searchResult);
    }


    public NavigationContextData getNavigationContext()
    {
        SearchResultData searchResult = getSearchResult();
        if(searchResult == null)
        {
            return null;
        }
        return searchResult.getNavigationContext();
    }


    public SearchContextData getSearchContext()
    {
        SearchResultData searchResult = getSearchResult();
        if(searchResult == null)
        {
            return null;
        }
        return searchResult.getSearchContext();
    }


    @Init
    public void init()
    {
        this.pageSize = 20;
    }


    @SocketEvent(socketId = "searchResult")
    public void onSearchResultChanged(SearchResultData searchResult)
    {
        setSearchResult(searchResult);
        populatePagination(searchResult);
        populatePageSizes(searchResult);
        populateSorts(searchResult);
        populateResults(searchResult);
        BindUtils.postNotifyChange(null, null, this, "searchResult");
    }


    @Command
    public void changePage(@BindingParam("activePage") int activePage, @BindingParam("pageSize") int pageSize)
    {
        PaginationRequestData paginationRequest = new PaginationRequestData();
        paginationRequest.setActivePage(activePage);
        paginationRequest.setPageSize(pageSize);
        sendOutput("paginationRequest", paginationRequest);
    }


    @Command
    public void changeSort(@BindingParam("sort") String sort)
    {
        SortRequestData searchRequest = new SortRequestData();
        searchRequest.setSort(sort);
        sendOutput("searchRequest", searchRequest);
    }


    @Command
    public void dropPromotedItem(@BindingParam("draggedResult") DocumentModel draggedResult, @BindingParam("targetResult") DocumentModel targetResult)
    {
        if(draggedResult == null || targetResult == null)
        {
            return;
        }
        NavigationContextData navigationContext = getNavigationContext();
        SearchContextData searchContext = getSearchContext();
        if(navigationContext == null || searchContext == null)
        {
            return;
        }
        AbstractAsConfigurableSearchConfigurationModel searchConfiguration = this.asSearchConfigurationFacade.getOrCreateSearchConfiguration(navigationContext, searchContext);
        String targetUid = targetResult.getPromotedItemUid();
        String uid = draggedResult.getPromotedItemUid();
        if(targetResult.getIndex() < draggedResult.getIndex())
        {
            this.asConfigurationService.rankBeforeConfiguration((AbstractAsConfigurationModel)searchConfiguration, "promotedItems", targetUid, new String[] {uid});
        }
        else
        {
            this.asConfigurationService.rankAfterConfiguration((AbstractAsConfigurationModel)searchConfiguration, "promotedItems", targetUid, new String[] {uid});
        }
        refreshSearchResults();
    }


    public void refreshSearchResults()
    {
        sendOutput("refreshSearch", null);
    }


    protected void populatePagination(SearchResultData searchResult)
    {
        if(searchResult == null || searchResult.getAsSearchResult() == null)
        {
            this.activePage = 0;
            this.resultCount = 0;
        }
        else
        {
            this.activePage = searchResult.getAsSearchResult().getActivePage();
            this.pageSize = searchResult.getAsSearchResult().getPageSize();
            this.resultCount = searchResult.getAsSearchResult().getResultCount();
        }
    }


    protected void populatePageSizes(SearchResultData searchResult)
    {
        this.pageSizes.clear();
        this.pageSizes.clearSelection();
        if(searchResult == null || searchResult.getAsSearchResult() == null)
        {
            return;
        }
        String configuredPageSizes = getWidgetSettings().getString("pageSizes");
        if(StringUtils.isNotBlank(configuredPageSizes))
        {
            this.pageSizes.addAll((Collection)Arrays.<String>stream(configuredPageSizes.split(",")).map(Integer::valueOf).collect(Collectors.toList()));
        }
        AsSearchResultData asSearchResult = searchResult.getAsSearchResult();
        this.pageSizes.setSelection(Collections.singletonList(Integer.valueOf(asSearchResult.getPageSize())));
    }


    protected void populateSorts(SearchResultData searchResult)
    {
        this.sorts.clear();
        this.sorts.clearSelection();
        if(searchResult == null || searchResult.getAsSearchResult() == null)
        {
            return;
        }
        AsSearchResultData asSearchResult = searchResult.getAsSearchResult();
        if(asSearchResult.getAvailableSorts() == null)
        {
            return;
        }
        List<SortModel> availableSorts = (List<SortModel>)asSearchResult.getAvailableSorts().stream().filter(this::isValidSort).map(this::convertSort).collect(Collectors.toList());
        this.sorts.addAll(availableSorts);
        String sortCode = (asSearchResult.getCurrentSort() != null) ? asSearchResult.getCurrentSort().getCode() : null;
        if(StringUtils.isNotBlank(sortCode))
        {
            Optional<SortModel> selectedSort = availableSorts.stream().filter(sort -> StringUtils.equals(sort.getCode(), sortCode)).findFirst();
            if(selectedSort.isPresent())
            {
                this.sorts.setSelection(Collections.singletonList(selectedSort.get()));
            }
        }
    }


    protected boolean isValidSort(AsSortData sort)
    {
        return (sort != null && StringUtils.isNotBlank(sort.getCode()));
    }


    protected SortModel convertSort(AsSortData source)
    {
        String name = StringUtils.isNotBlank(source.getName()) ? source.getName() : source.getCode();
        SortModel target = new SortModel();
        target.setCode(source.getCode());
        target.setName(name);
        return target;
    }


    protected void populateResults(SearchResultData searchResult)
    {
        this.resultActionsEnabled = false;
        this.promotedItems.clear();
        this.defaultResults.clear();
        if(!canPopulateResults(searchResult))
        {
            return;
        }
        AsSearchResultData asSearchResult = searchResult.getAsSearchResult();
        AsSearchProfileResult searchProfileResult = asSearchResult.getSearchProfileResult();
        Optional<AbstractAsSearchConfigurationModel> searchConfiguration = resolveSearchConfiguration(searchResult);
        this.resultActionsEnabled = searchConfiguration.isPresent();
        int index = 0;
        for(AsDocumentData document : asSearchResult.getResults())
        {
            Float score = document.getScore();
            PK pk = document.getPk();
            AsConfigurationHolder<AsPromotedItem, AbstractAsBoostItemConfiguration> promotedItemHolder = (AsConfigurationHolder<AsPromotedItem, AbstractAsBoostItemConfiguration>)searchProfileResult.getPromotedItems().get(pk);
            boolean promoted = isPromoted(asSearchResult, promotedItemHolder);
            String promotedItemUid = promoted ? ((AsPromotedItem)promotedItemHolder.getConfiguration()).getUid() : null;
            Set<String> tags = document.getTags();
            boolean highlighted = CollectionUtils.emptyIfNull(tags).contains("highlighted");
            boolean showOnTop = CollectionUtils.emptyIfNull(tags).contains("promoted");
            boolean fromSearchProfile = (promoted && isConfigurationFromSearchProfile((AbstractAsItemConfiguration)promotedItemHolder.getConfiguration(), searchResult.getNavigationContext()));
            boolean fromSearchConfiguration = (promoted && searchConfiguration.isPresent() && isConfigurationFromSearchConfiguration((AbstractAsItemConfiguration)promotedItemHolder.getConfiguration(), searchConfiguration.get()));
            boolean override = (fromSearchConfiguration && CollectionUtils.isNotEmpty(promotedItemHolder.getReplacedConfigurations()));
            boolean overrideFromSearchProfile = (override && isConfigurationFromSearchProfile(promotedItemHolder
                            .getReplacedConfigurations().get(0), searchResult.getNavigationContext()));
            DocumentModel result = new DocumentModel();
            result.setIndex(index);
            result.setScore(score);
            result.setPk(pk);
            result.setDocument(document);
            result.setPromoted(promoted);
            result.setHighlight(highlighted);
            result.setShowOnTop(showOnTop);
            result.setPromotedItemUid(promotedItemUid);
            result.setFromSearchProfile(fromSearchProfile);
            result.setFromSearchConfiguration(fromSearchConfiguration);
            result.setOverride(override);
            result.setOverrideFromSearchProfile(overrideFromSearchProfile);
            result.setStyleClass(buildResultStyleClass(result));
            if(result.isShowOnTop())
            {
                this.promotedItems.add(result);
            }
            else
            {
                this.defaultResults.add(result);
            }
            index++;
        }
    }


    protected boolean canPopulateResults(SearchResultData searchResult)
    {
        if(searchResult == null || searchResult.getNavigationContext() == null || searchResult.getSearchContext() == null)
        {
            return false;
        }
        return (searchResult.getAsSearchResult() != null &&
                        CollectionUtils.isNotEmpty(searchResult.getAsSearchResult().getResults()));
    }


    protected Optional<AbstractAsSearchConfigurationModel> resolveSearchConfiguration(SearchResultData searchResult)
    {
        NavigationContextData navigationContext = searchResult.getNavigationContext();
        SearchContextData searchContext = searchResult.getSearchContext();
        if(navigationContext == null || StringUtils.isBlank(navigationContext.getCurrentSearchProfile()) || searchContext == null)
        {
            return Optional.empty();
        }
        AbstractAsConfigurableSearchConfigurationModel abstractAsConfigurableSearchConfigurationModel = this.asSearchConfigurationFacade.getOrCreateSearchConfiguration(searchResult.getNavigationContext(), searchResult.getSearchContext());
        if(!(abstractAsConfigurableSearchConfigurationModel instanceof AbstractAsConfigurableSearchConfigurationModel))
        {
            return Optional.empty();
        }
        return (Optional)Optional.of(abstractAsConfigurableSearchConfigurationModel);
    }


    protected boolean isPromoted(AsSearchResultData asSearchResult, AsConfigurationHolder<AsPromotedItem, AbstractAsBoostItemConfiguration> promotedItemHolder)
    {
        return (promotedItemHolder != null && promotedItemHolder.getConfiguration() != null);
    }


    protected boolean isConfigurationFromSearchProfile(AbstractAsItemConfiguration configuration, NavigationContextData navigationContext)
    {
        if(configuration == null || navigationContext == null)
        {
            return false;
        }
        return StringUtils.equals(navigationContext.getCurrentSearchProfile(), configuration.getSearchProfileCode());
    }


    protected boolean isConfigurationFromSearchConfiguration(AbstractAsItemConfiguration configuration, AbstractAsSearchConfigurationModel searchConfiguration)
    {
        if(configuration == null || searchConfiguration == null)
        {
            return false;
        }
        return StringUtils.equals(searchConfiguration.getUid(), configuration.getSearchConfigurationUid());
    }


    protected String buildResultStyleClass(DocumentModel document)
    {
        StringJoiner styleClass = new StringJoiner(" ");
        styleClass.add("yas-result");
        if(document.isPromoted())
        {
            styleClass.add("yas-promoted");
        }
        if(document.isHighlight())
        {
            styleClass.add("yas-highlighted");
        }
        if(document.isShowOnTop())
        {
            styleClass.add("yas-show-on-top");
        }
        if(document.isFromSearchProfile())
        {
            styleClass.add("yas-from-search-profile");
        }
        if(document.isFromSearchConfiguration())
        {
            styleClass.add("yas-from-search-configuration");
        }
        if(document.isOverride())
        {
            styleClass.add("yas-override");
        }
        if(document.isOverrideFromSearchProfile())
        {
            styleClass.add("yas-override-from-search-profile");
        }
        return styleClass.toString();
    }


    public String sanitizeHtml(String value)
    {
        if(StringUtils.isBlank(value))
        {
            return value;
        }
        return HTMLSanitizer.sanitizeHTML(value);
    }


    public String formatCurrency(Number value)
    {
        if(value == null)
        {
            return "";
        }
        SearchContextData searchContext = getSearchContext();
        NumberFormat currencyFormat = createCurrencyFormat(searchContext);
        return currencyFormat.format(value);
    }


    protected NumberFormat createCurrencyFormat(SearchContextData searchContext)
    {
        CurrencyModel currency = getCurrency(searchContext);
        Locale locale = getLocale(searchContext);
        DecimalFormat currencyFormat = (DecimalFormat)NumberFormat.getCurrencyInstance(locale);
        adjustDigits(currencyFormat, currency);
        adjustSymbol(currencyFormat, currency);
        return currencyFormat;
    }


    protected CurrencyModel getCurrency(SearchContextData searchContext)
    {
        if(searchContext == null || StringUtils.isBlank(searchContext.getCurrency()))
        {
            return this.commonI18NService.getCurrentCurrency();
        }
        return this.commonI18NService.getCurrency(searchContext.getCurrency());
    }


    protected Locale getLocale(SearchContextData searchContext)
    {
        if(searchContext == null || StringUtils.isBlank(searchContext.getLanguage()))
        {
            return this.commonI18NService.getLocaleForIsoCode(this.commonI18NService.getCurrentLanguage().getIsocode());
        }
        return this.commonI18NService.getLocaleForIsoCode(searchContext.getLanguage());
    }


    protected DecimalFormat adjustDigits(DecimalFormat format, CurrencyModel currencyModel)
    {
        int tempDigits = (currencyModel.getDigits() == null) ? 0 : currencyModel.getDigits().intValue();
        int digits = Math.max(0, tempDigits);
        format.setMaximumFractionDigits(digits);
        format.setMinimumFractionDigits(digits);
        if(digits == 0)
        {
            format.setDecimalSeparatorAlwaysShown(false);
        }
        return format;
    }


    protected DecimalFormat adjustSymbol(DecimalFormat format, CurrencyModel currencyModel)
    {
        String symbol = currencyModel.getSymbol();
        if(symbol != null)
        {
            DecimalFormatSymbols symbols = format.getDecimalFormatSymbols();
            String iso = currencyModel.getIsocode();
            boolean changed = false;
            if(!iso.equalsIgnoreCase(symbols.getInternationalCurrencySymbol()))
            {
                symbols.setInternationalCurrencySymbol(iso);
                changed = true;
            }
            if(!symbol.equals(symbols.getCurrencySymbol()))
            {
                symbols.setCurrencySymbol(symbol);
                changed = true;
            }
            if(changed)
            {
                format.setDecimalFormatSymbols(symbols);
            }
        }
        return format;
    }
}
