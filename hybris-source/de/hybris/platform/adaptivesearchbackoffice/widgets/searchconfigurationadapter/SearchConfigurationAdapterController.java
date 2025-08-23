package de.hybris.platform.adaptivesearchbackoffice.widgets.searchconfigurationadapter;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.components.Widgetslot;
import com.hybris.cockpitng.core.model.ValueObserver;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.util.DefaultWidgetController;
import de.hybris.platform.adaptivesearch.model.AbstractAsConfigurableSearchConfigurationModel;
import de.hybris.platform.adaptivesearchbackoffice.data.AbstractSearchRequestData;
import de.hybris.platform.adaptivesearchbackoffice.data.NavigationContextData;
import de.hybris.platform.adaptivesearchbackoffice.data.SearchContextData;
import de.hybris.platform.adaptivesearchbackoffice.data.SearchResultData;
import de.hybris.platform.adaptivesearchbackoffice.facades.AsSearchConfigurationFacade;
import de.hybris.platform.servicelayer.model.ModelService;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.zk.ui.select.annotation.WireVariable;

public class SearchConfigurationAdapterController extends DefaultWidgetController
{
    protected static final String AUTO_SAVE_ENABLED_PARAM = "autoSaveEnabled";
    protected static final String REFRESH_SEARCH_CONFIGURATION_IN_SOCKET = "refreshSearchConfiguration";
    protected static final String SEARCH_CONFIGURATION_OUT_SOCKET = "searchConfiguration";
    protected static final String REFRESH_SEARCH_OUT_SOCKET = "refreshSearch";
    protected static final String SEARCH_RESULT_KEY = "searchResult";
    protected static final String VALUE_CHANGED_OBSERVER_ID = "asSearchConfiguration.valueChanged";
    protected Widgetslot searchConfigurationEditor;
    @WireVariable
    protected transient ModelService modelService;
    @WireVariable
    protected transient AsSearchConfigurationFacade asSearchConfigurationFacade;


    @SocketEvent(socketId = "searchResult")
    public void onSearchResultChanged(SearchResultData searchResult)
    {
        NavigationContextData navigationContext = searchResult.getNavigationContext();
        SearchContextData searchContext = searchResult.getSearchContext();
        WidgetModel searchConfigurationModel = this.searchConfigurationEditor.getViewModel();
        searchConfigurationModel.removeObserver("asSearchConfiguration.valueChanged");
        if(StringUtils.isBlank(navigationContext.getCurrentSearchProfile()))
        {
            searchConfigurationModel.setValue("searchResult", null);
            sendOutput("searchConfiguration", null);
        }
        else
        {
            searchConfigurationModel.setValue("searchResult", searchResult);
            AbstractAsConfigurableSearchConfigurationModel searchConfiguration = this.asSearchConfigurationFacade.getOrCreateSearchConfiguration(navigationContext, searchContext);
            sendOutput("searchConfiguration", this.modelService.clone(searchConfiguration));
            sendOutput("searchConfiguration", searchConfiguration);
            boolean autoSaveEnabled = getWidgetSettings().getBoolean("autoSaveEnabled");
            if(autoSaveEnabled)
            {
                Object object = new Object(this);
                searchConfigurationModel.addObserver("valueChanged", (ValueObserver)object);
            }
        }
    }


    @SocketEvent(socketId = "refreshSearchConfiguration")
    public void onSearchConfigurationChanged(Object searchConfiguration)
    {
        sendOutput("refreshSearch", new Object());
    }


    @SocketEvent(socketId = "searchRequest")
    public void onSearchRequest(AbstractSearchRequestData request)
    {
        sendOutput("searchRequest", request);
    }
}
