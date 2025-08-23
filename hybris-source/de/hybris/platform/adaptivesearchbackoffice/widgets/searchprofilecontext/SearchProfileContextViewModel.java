package de.hybris.platform.adaptivesearchbackoffice.widgets.searchprofilecontext;

import com.hybris.backoffice.events.processes.ProcessFinishedEvent;
import com.hybris.backoffice.sync.facades.SynchronizationFacade;
import com.hybris.cockpitng.annotations.GlobalCockpitEvent;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.labels.LabelService;
import de.hybris.platform.adaptivesearch.context.AsSearchProfileContext;
import de.hybris.platform.adaptivesearch.context.impl.DefaultAsSearchProfileContext;
import de.hybris.platform.adaptivesearch.data.AsSearchConfigurationInfoData;
import de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel;
import de.hybris.platform.adaptivesearch.services.AsSearchConfigurationService;
import de.hybris.platform.adaptivesearch.services.AsSearchProfileService;
import de.hybris.platform.adaptivesearchbackoffice.data.NavigationContextData;
import de.hybris.platform.adaptivesearchbackoffice.data.SearchResultData;
import de.hybris.platform.adaptivesearchbackoffice.widgets.AbstractWidgetViewModel;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.event.events.AfterCronJobFinishedEvent;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.select.annotation.WireVariable;

public class SearchProfileContextViewModel extends AbstractWidgetViewModel
{
    protected static final String NAVIGATION_CONTEXT = "navigationContext";
    protected static final String SEARCH_PROFILE_INFO = "searchProfileInfo";
    protected static final String IN_SYNC = "inSync";
    protected static final String CATALOG_SYNC_JOB = "CatalogVersionSyncCronJob";
    @WireVariable
    protected ModelService modelService;
    @WireVariable
    protected SessionService sessionService;
    @WireVariable
    protected I18NService i18nService;
    @WireVariable
    protected CatalogVersionService catalogVersionService;
    @WireVariable
    protected AsSearchProfileService asSearchProfileService;
    @WireVariable
    protected AsSearchConfigurationService asSearchConfigurationService;
    @WireVariable
    protected LabelService labelService;
    @WireVariable
    protected SynchronizationFacade synchronizationFacade;
    private NavigationContextData navigationContext;
    private SearchProfileInfoModel searchProfileInfo;
    private boolean inSync;


    @SocketEvent(socketId = "searchResult")
    public void update(SearchResultData searchResult)
    {
        this.sessionService.executeInLocalView((SessionExecutionBody)new Object(this, searchResult));
    }


    protected void updateSearchProfileInfo(SearchResultData searchResult)
    {
        this.navigationContext = null;
        this.searchProfileInfo = null;
        if(searchResult == null || searchResult.getNavigationContext() == null || searchResult.getAsSearchResult() == null)
        {
            BindUtils.postNotifyChange(null, null, this, "searchProfileInfo");
            return;
        }
        this.navigationContext = searchResult.getNavigationContext();
        Optional<AbstractAsSearchProfileModel> searchProfileOptional = resolveSearchProfile();
        if(!searchProfileOptional.isPresent())
        {
            BindUtils.postNotifyChange(null, null, this, "searchProfileInfo");
            return;
        }
        AbstractAsSearchProfileModel searchProfile = searchProfileOptional.get();
        List<CatalogVersionModel> catalogVersions = searchResult.getAsSearchResult().getCatalogVersions();
        List<CategoryModel> categoryPath = searchResult.getAsSearchResult().getCategoryPath();
        DefaultAsSearchProfileContext defaultAsSearchProfileContext = DefaultAsSearchProfileContext.builder().withIndexConfiguration(this.navigationContext.getIndexConfiguration()).withIndexType(this.navigationContext.getIndexType()).withCatalogVersions(catalogVersions)
                        .withCategoryPath(categoryPath).build();
        AsSearchConfigurationInfoData searchConfigurationInfo = this.asSearchConfigurationService.getSearchConfigurationInfoForContext((AsSearchProfileContext)defaultAsSearchProfileContext, searchProfile);
        this.searchProfileInfo = new SearchProfileInfoModel();
        this.searchProfileInfo.setSearchConfigurationInfo(searchConfigurationInfo);
        this.searchProfileInfo.setSearchProfileLabel(buildSearchProfileLabel(searchProfile, searchConfigurationInfo));
        BindUtils.postNotifyChange(null, null, this, "navigationContext");
        BindUtils.postNotifyChange(null, null, this, "searchProfileInfo");
    }


    protected String buildSearchProfileLabel(AbstractAsSearchProfileModel searchProfile, AsSearchConfigurationInfoData searchConfigurationInfo)
    {
        return searchProfile.getCode() + " - " + searchProfile.getCode();
    }


    public void updateSyncStatus()
    {
        setInSync(false);
        Optional<AbstractAsSearchProfileModel> searchProfileOptional = resolveSearchProfile();
        if(searchProfileOptional.isPresent())
        {
            Optional<Boolean> isInSync = this.synchronizationFacade.isInSync((ItemModel)searchProfileOptional.get(), Collections.emptyMap());
            if(isInSync.isPresent())
            {
                setInSync(((Boolean)isInSync.get()).booleanValue());
            }
        }
        BindUtils.postNotifyChange(null, null, this, "inSync");
    }


    @Command
    public void synchronize()
    {
        Optional<AbstractAsSearchProfileModel> searchProfileOptional = resolveSearchProfile();
        if(searchProfileOptional.isPresent())
        {
            sendOutput("searchProfile", searchProfileOptional.get());
        }
    }


    @GlobalCockpitEvent(eventName = "com.hybris.backoffice.events.processes.ProcessFinishedEvent", scope = "application")
    public void processFinished(CockpitEvent cockpitEvent)
    {
        if(cockpitEvent.getData() instanceof ProcessFinishedEvent)
        {
            AfterCronJobFinishedEvent processEvent = (AfterCronJobFinishedEvent)((ProcessFinishedEvent)cockpitEvent.getData()).getProcessEvent();
            if(StringUtils.equals("CatalogVersionSyncCronJob", processEvent.getCronJobType()))
            {
                updateSyncStatus();
            }
        }
    }


    protected Optional<AbstractAsSearchProfileModel> resolveSearchProfile()
    {
        if(this.navigationContext == null || StringUtils.isBlank(this.navigationContext.getCurrentSearchProfile()))
        {
            return Optional.empty();
        }
        CatalogVersionModel catalogVersion = (this.navigationContext.getCatalogVersion() == null) ? null : this.catalogVersionService.getCatalogVersion(this.navigationContext.getCatalogVersion().getCatalogId(), this.navigationContext
                        .getCatalogVersion().getVersion());
        Optional<AbstractAsSearchProfileModel> searchProfileOptional = this.asSearchProfileService.getSearchProfileForCode(catalogVersion, this.navigationContext.getCurrentSearchProfile());
        if(searchProfileOptional.isPresent())
        {
            this.modelService.refresh(searchProfileOptional.get());
        }
        return searchProfileOptional;
    }


    public NavigationContextData getNavigationContext()
    {
        return this.navigationContext;
    }


    public void setNavigationContext(NavigationContextData navigationContext)
    {
        this.navigationContext = navigationContext;
    }


    public SearchProfileInfoModel getSearchProfileInfo()
    {
        return this.searchProfileInfo;
    }


    protected void setSearchProfileInfo(SearchProfileInfoModel searchProfileInfo)
    {
        this.searchProfileInfo = searchProfileInfo;
    }


    public boolean isInSync()
    {
        return this.inSync;
    }


    protected void setInSync(boolean isInSync)
    {
        this.inSync = isInSync;
    }
}
