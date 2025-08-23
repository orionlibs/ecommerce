package de.hybris.platform.y2ysync.backoffice.actions;

import com.hybris.backoffice.navigation.TreeNodeSelector;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchInitContext;
import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.CockpitConfigurationService;
import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.core.config.impl.DefaultConfigContext;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.AdvancedSearch;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.FieldType;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.y2ysync.model.Y2YSyncCronJobModel;
import javax.annotation.Resource;

public class FindY2YSyncMediaAction extends AbstractComponentWidgetAdapterAware implements CockpitAction<Y2YSyncCronJobModel, Object>
{
    private static final String FINDER_OUTPUT_SOCKET = "finderOutput";
    private static final String NODE_OUTPUT_SOCKET = "nodeOutput";
    private static final String SYNC_IMPEX_MEDIA_TYPECODE = "SyncImpExMedia";
    private static final String HMC_TYPENODE_SYNCIMPEXMEDIA = "hmc_typenode_syncimpexmedia";
    private static final String EXPORT_CRON_JOB = "exportCronJob";
    private static final String JOB_LOG_SEARCH_EDITOR_NAME = "SyncImpExMedia-advanced-search";
    @Resource
    FlexibleSearchService flexibleSearchService;
    @Resource
    CockpitConfigurationService cockpitConfigurationService;


    public ActionResult<Object> perform(ActionContext<Y2YSyncCronJobModel> actionContext)
    {
        Y2YSyncCronJobModel syncCronJob = (Y2YSyncCronJobModel)actionContext.getData();
        if(syncCronJob == null)
        {
            return new ActionResult("error");
        }
        AdvancedSearchData searchData = new AdvancedSearchData();
        searchData.setTypeCode("SyncImpExMedia");
        searchData.setGlobalOperator(ValueComparisonOperator.AND);
        AdvancedSearchInitContext initContext = createSearchContext(searchData, syncCronJob);
        sendOutput("finderOutput", initContext);
        sendOutput("nodeOutput", new TreeNodeSelector("hmc_typenode_syncimpexmedia", false));
        return new ActionResult("success");
    }


    private AdvancedSearchInitContext createSearchContext(AdvancedSearchData searchData, Y2YSyncCronJobModel latestCronJob)
    {
        AdvancedSearch config = loadAdvancedSearchConfiguration("SyncImpExMedia-advanced-search");
        config.setDisableSimpleSearch(Boolean.valueOf(true));
        FieldType principalAssigned = new FieldType();
        principalAssigned.setName("exportCronJob");
        principalAssigned.setOperator(ValueComparisonOperator.EQUALS.getOperatorCode());
        searchData.addCondition(principalAssigned, ValueComparisonOperator.EQUALS, latestCronJob);
        return new AdvancedSearchInitContext(searchData, config);
    }


    private AdvancedSearch loadAdvancedSearchConfiguration(String name)
    {
        DefaultConfigContext context = new DefaultConfigContext(name, "SyncImpExMedia");
        try
        {
            return (AdvancedSearch)this.cockpitConfigurationService.loadConfiguration((ConfigContext)context, AdvancedSearch.class);
        }
        catch(CockpitConfigurationException cce)
        {
            throw new SystemException("Failed to read cockpit configuration", cce);
        }
    }


    public boolean canPerform(ActionContext<Y2YSyncCronJobModel> actionContext)
    {
        return true;
    }


    public boolean needsConfirmation(ActionContext<Y2YSyncCronJobModel> actionContext)
    {
        return false;
    }


    public String getConfirmationMessage(ActionContext<Y2YSyncCronJobModel> actionContext)
    {
        return null;
    }
}
