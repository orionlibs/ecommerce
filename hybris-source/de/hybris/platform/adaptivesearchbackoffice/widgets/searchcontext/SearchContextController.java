package de.hybris.platform.adaptivesearchbackoffice.widgets.searchcontext;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.i18n.CockpitLocaleService;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.util.DefaultWidgetController;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProvider;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProviderFactory;
import de.hybris.platform.adaptivesearchbackoffice.data.NavigationContextData;
import de.hybris.platform.adaptivesearchbackoffice.data.SearchContextData;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import java.util.Collection;
import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;

public class SearchContextController extends DefaultWidgetController
{
    protected static final String LANGUAGE_SELECTOR_ID = "languageSelector";
    protected static final String CURRENCY_SELECTOR_ID = "currencySelector";
    protected static final String ON_VALUE_CHANGED = "onValueChanged";
    protected static final String NAVIGATION_CONTEXT_KEY = "navigationContext";
    protected static final String SEARCH_CONTEXT_KEY = "searchContext";
    @WireVariable
    protected transient SessionService sessionService;
    protected transient I18NService i18nService;
    @WireVariable
    protected transient AsSearchProviderFactory asSearchProviderFactory;
    @WireVariable
    protected transient CockpitLocaleService cockpitLocaleService;
    @WireVariable
    protected transient LabelService labelService;
    protected Combobox languageSelector;
    protected Combobox currencySelector;
    private final ListModelList<LanguageModel> languagesModel = new ListModelList();
    private final ListModelList<CurrencyModel> currenciesModel = new ListModelList();


    public ListModelList<LanguageModel> getLanguagesModel()
    {
        return this.languagesModel;
    }


    public ListModelList<CurrencyModel> getCurrenciesModel()
    {
        return this.currenciesModel;
    }


    public NavigationContextData getNavigationContext()
    {
        return (NavigationContextData)getModel().getValue("navigationContext", NavigationContextData.class);
    }


    public void setNavigationContext(NavigationContextData navigationContext)
    {
        getModel().put("navigationContext", navigationContext);
    }


    public SearchContextData getSearchContext()
    {
        return (SearchContextData)getModel().getValue("searchContext", SearchContextData.class);
    }


    public void setSearchContext(SearchContextData searchContext)
    {
        getModel().put("searchContext", searchContext);
    }


    @SocketEvent(socketId = "navigationContext")
    public void updateSearchContext(NavigationContextData navigationContextData)
    {
        setNavigationContext(navigationContextData);
        SearchContextData searchContext = getSearchContext();
        updateSelectors(searchContext);
        sendSearchContext(searchContext);
    }


    public void initialize(Component component)
    {
        initializeSelectors();
        component.addEventListener("onCreate", event -> {
            SearchContextData searchContext = getSearchContext();
            updateSelectors(searchContext);
            sendSearchContext(searchContext);
        });
    }


    protected void initializeSelectors()
    {
        SearchContextData searchContext = new SearchContextData();
        searchContext.setCurrency(null);
        searchContext.setLanguage(null);
        setSearchContext(searchContext);
        this.languageSelector.setModel((ListModel)this.languagesModel);
        this.currencySelector.setModel((ListModel)this.currenciesModel);
    }


    protected void updateSelectors(SearchContextData searchContext)
    {
        this.sessionService.executeInLocalView((SessionExecutionBody)new Object(this, searchContext));
    }


    protected void sendSearchContext(SearchContextData searchContext)
    {
        if(searchContext != null)
        {
            SearchContextData clonedSearchContext = new SearchContextData();
            clonedSearchContext.setLanguage(searchContext.getLanguage());
            clonedSearchContext.setCurrency(searchContext.getCurrency());
            sendOutput("searchContext", clonedSearchContext);
        }
        else
        {
            sendOutput("searchContext", null);
        }
    }


    protected void updateLanguages(SearchContextData searchContext)
    {
        NavigationContextData navigationContext = getNavigationContext();
        List<LanguageModel> languages = findLanguages(navigationContext);
        if(!CollectionUtils.isEqualCollection(this.languagesModel.getInnerList(), languages))
        {
            this.languagesModel.clear();
            this.languagesModel.addAll(languages);
        }
        if(CollectionUtils.isEmpty((Collection)this.languagesModel))
        {
            searchContext.setLanguage(null);
            this.languagesModel.setSelection(Collections.emptyList());
            this.languageSelector.setDisabled(true);
        }
        else
        {
            if(this.languagesModel.isSelectionEmpty())
            {
                Optional<LanguageModel> sessionLanguage = getSessionLanguage();
                LanguageModel selected = sessionLanguage.isPresent() ? sessionLanguage.get() : (LanguageModel)this.languagesModel.get(0);
                this.languagesModel.setSelection(Collections.singletonList(selected));
                searchContext.setLanguage(selected.getIsoCode());
            }
            this.languageSelector.setDisabled(false);
        }
    }


    protected void updateCurrencies(SearchContextData searchContext)
    {
        NavigationContextData navigationContext = getNavigationContext();
        List<CurrencyModel> currencies = findCurrencies(navigationContext);
        if(!CollectionUtils.isEqualCollection(this.currenciesModel.getInnerList(), currencies))
        {
            this.currenciesModel.clear();
            this.currenciesModel.addAll(currencies);
        }
        if(CollectionUtils.isEmpty((Collection)this.currenciesModel))
        {
            searchContext.setCurrency(null);
            this.currenciesModel.setSelection(Collections.emptyList());
            this.currencySelector.setDisabled(true);
        }
        else
        {
            if(this.currenciesModel.isSelectionEmpty())
            {
                Optional<CurrencyModel> sessionCurrency = getSessionCurrency();
                CurrencyModel selected = sessionCurrency.isPresent() ? sessionCurrency.get() : (CurrencyModel)this.currenciesModel.get(0);
                this.currenciesModel.setSelection(Collections.singletonList(selected));
                searchContext.setCurrency(selected.getIsoCode());
            }
            this.currencySelector.setDisabled(false);
        }
    }


    @ViewEvent(componentID = "languageSelector", eventName = "onSelect")
    public void onLanguageChanged(SelectEvent<Comboitem, String> event)
    {
        SearchContextData searchContext = getSearchContext();
        String selectedLanguageIso = (String)((Comboitem)event.getReference()).getValue();
        if(searchContext != null && !Objects.equals(searchContext.getLanguage(), selectedLanguageIso))
        {
            searchContext.setLanguage(selectedLanguageIso);
            updateLanguages(searchContext);
            sendSearchContext(searchContext);
        }
    }


    @ViewEvent(componentID = "currencySelector", eventName = "onSelect")
    public void onCurrencyChanged(SelectEvent<Comboitem, String> event)
    {
        SearchContextData searchContext = getSearchContext();
        String selectedCurrencyIso = (String)((Comboitem)event.getReference()).getValue();
        if(searchContext != null && !Objects.equals(searchContext.getCurrency(), selectedCurrencyIso))
        {
            searchContext.setCurrency(selectedCurrencyIso);
            updateCurrencies(searchContext);
            sendSearchContext(searchContext);
        }
    }


    protected List<LanguageModel> findLanguages(NavigationContextData navigationContext)
    {
        if(navigationContext == null || navigationContext.getIndexConfiguration() == null || navigationContext
                        .getIndexType() == null)
        {
            return Collections.emptyList();
        }
        AsSearchProvider searchProvider = this.asSearchProviderFactory.getSearchProvider();
        List<LanguageModel> languages = searchProvider.getSupportedLanguages(navigationContext.getIndexConfiguration(), navigationContext.getIndexType());
        return (List<LanguageModel>)languages.stream().filter(this::isValidLanguage).map(this::convertLanguage).sorted(this::compareLanguages)
                        .collect(Collectors.toList());
    }


    protected boolean isValidLanguage(LanguageModel language)
    {
        return (language != null && StringUtils.isNotBlank(language.getIsocode()));
    }


    protected LanguageModel convertLanguage(LanguageModel source)
    {
        LanguageModel target = new LanguageModel();
        target.setName(this.labelService.getObjectLabel(source));
        target.setIsoCode(source.getIsocode());
        return target;
    }


    protected int compareLanguages(LanguageModel language1, LanguageModel language2)
    {
        return language1.getName().compareTo(language2.getName());
    }


    protected List<CurrencyModel> findCurrencies(NavigationContextData navigationContext)
    {
        if(navigationContext == null || navigationContext.getIndexConfiguration() == null || navigationContext
                        .getIndexType() == null)
        {
            return Collections.emptyList();
        }
        AsSearchProvider searchProvider = this.asSearchProviderFactory.getSearchProvider();
        List<CurrencyModel> currencies = searchProvider.getSupportedCurrencies(navigationContext.getIndexConfiguration(), navigationContext.getIndexType());
        return (List<CurrencyModel>)currencies.stream().filter(this::isValidCurrency).map(this::convertCurrency).sorted(this::compareCurrencies)
                        .collect(Collectors.toList());
    }


    protected boolean isValidCurrency(CurrencyModel currency)
    {
        return (currency != null && StringUtils.isNotBlank(currency.getIsocode()));
    }


    protected CurrencyModel convertCurrency(CurrencyModel source)
    {
        CurrencyModel target = new CurrencyModel();
        target.setName(this.labelService.getObjectLabel(source));
        target.setIsoCode(source.getIsocode());
        return target;
    }


    protected int compareCurrencies(CurrencyModel currency1, CurrencyModel currency2)
    {
        return currency1.getName().compareTo(currency2.getName());
    }


    protected Optional<LanguageModel> getSessionLanguage()
    {
        Locale locale = this.cockpitLocaleService.getCurrentLocale();
        return this.languagesModel.stream().filter(languageModel -> languageModel.getIsoCode().equals(locale.getLanguage())).findFirst();
    }


    protected Optional<CurrencyModel> getSessionCurrency()
    {
        Locale locale = this.cockpitLocaleService.getCurrentLocale();
        Currency currency = StringUtils.isBlank(locale.getCountry()) ? null : Currency.getInstance(locale);
        if(currency == null)
        {
            return Optional.empty();
        }
        return this.currenciesModel.stream().filter(currencyModel -> currencyModel.getIsoCode().equals(currency.getCurrencyCode()))
                        .findFirst();
    }
}
