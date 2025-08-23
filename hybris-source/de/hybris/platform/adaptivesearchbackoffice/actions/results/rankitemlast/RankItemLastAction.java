package de.hybris.platform.adaptivesearchbackoffice.actions.results.rankitemlast;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import de.hybris.platform.adaptivesearch.model.AbstractAsConfigurableSearchConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AbstractAsConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AsPromotedItemModel;
import de.hybris.platform.adaptivesearch.services.AsConfigurationService;
import de.hybris.platform.adaptivesearchbackoffice.actions.results.AbstractResultAction;
import de.hybris.platform.adaptivesearchbackoffice.data.NavigationContextData;
import de.hybris.platform.adaptivesearchbackoffice.data.SearchContextData;
import de.hybris.platform.adaptivesearchbackoffice.facades.AsSearchConfigurationFacade;
import de.hybris.platform.adaptivesearchbackoffice.widgets.searchresultbrowser.DocumentModel;
import javax.annotation.Resource;

public class RankItemLastAction extends AbstractResultAction
{
    @Resource
    protected AsSearchConfigurationFacade asSearchConfigurationFacade;
    @Resource
    protected AsConfigurationService asConfigurationService;


    public ActionResult<Object> perform(ActionContext<DocumentModel> ctx)
    {
        DocumentModel data = (DocumentModel)ctx.getData();
        if(!isValidContextParams(ctx))
        {
            return new ActionResult("error");
        }
        NavigationContextData navigationContext = getNavigationContext(ctx);
        SearchContextData searchContext = getSearchContext(ctx);
        AbstractAsConfigurableSearchConfigurationModel searchConfiguration = this.asSearchConfigurationFacade.getOrCreateSearchConfiguration(navigationContext, searchContext);
        this.asConfigurationService.rankBeforeConfiguration((AbstractAsConfigurationModel)searchConfiguration, "promotedItems", null, new String[] {data
                        .getPromotedItemUid()});
        refreshSearchResults(ctx);
        return new ActionResult("success");
    }


    public boolean canPerform(ActionContext<DocumentModel> ctx)
    {
        DocumentModel data = (DocumentModel)ctx.getData();
        if(data == null || !data.isPromoted() || !data.isShowOnTop() || data.getPromotedItemUid() == null)
        {
            return false;
        }
        NavigationContextData navigationContext = getNavigationContext(ctx);
        SearchContextData searchContext = getSearchContext(ctx);
        AbstractAsConfigurableSearchConfigurationModel searchConfiguration = this.asSearchConfigurationFacade.getOrCreateSearchConfiguration(navigationContext, searchContext);
        return (searchConfiguration.getPromotedItems() != null && !searchConfiguration.getPromotedItems().isEmpty() &&
                        !data.getPromotedItemUid().equals(((AsPromotedItemModel)searchConfiguration.getPromotedItems().get(searchConfiguration.getPromotedItems().size() - 1)).getUid()) && data
                        .isFromSearchConfiguration());
    }
}
