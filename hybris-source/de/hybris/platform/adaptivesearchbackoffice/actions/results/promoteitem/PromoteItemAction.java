package de.hybris.platform.adaptivesearchbackoffice.actions.results.promoteitem;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import de.hybris.platform.adaptivesearch.model.AbstractAsConfigurableSearchConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AsPromotedItemModel;
import de.hybris.platform.adaptivesearchbackoffice.actions.results.AbstractResultAction;
import de.hybris.platform.adaptivesearchbackoffice.data.NavigationContextData;
import de.hybris.platform.adaptivesearchbackoffice.data.SearchContextData;
import de.hybris.platform.adaptivesearchbackoffice.facades.AsSearchConfigurationFacade;
import de.hybris.platform.adaptivesearchbackoffice.widgets.searchresultbrowser.DocumentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;

public class PromoteItemAction extends AbstractResultAction
{
    @Resource
    protected AsSearchConfigurationFacade asSearchConfigurationFacade;
    @Resource
    protected ModelService modelService;


    public ActionResult<Object> perform(ActionContext<DocumentModel> ctx)
    {
        if(!isValidContextParams(ctx))
        {
            return new ActionResult("error");
        }
        promoteResult(ctx);
        refreshSearchResults(ctx);
        return new ActionResult("success");
    }


    protected void promoteResult(ActionContext<DocumentModel> ctx)
    {
        NavigationContextData navigationContext = getNavigationContext(ctx);
        SearchContextData searchContext = getSearchContext(ctx);
        AbstractAsConfigurableSearchConfigurationModel searchConfiguration = this.asSearchConfigurationFacade.getOrCreateSearchConfiguration(navigationContext, searchContext);
        List<AsPromotedItemModel> newPromotedItems = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(searchConfiguration.getPromotedItems()))
        {
            newPromotedItems.addAll(searchConfiguration.getPromotedItems());
        }
        DocumentModel data = (DocumentModel)ctx.getData();
        ItemModel item = (ItemModel)this.modelService.get(data.getPk());
        AsPromotedItemModel newPromotedItem = new AsPromotedItemModel();
        newPromotedItem.setSearchConfiguration(searchConfiguration);
        newPromotedItem.setCatalogVersion(searchConfiguration.getCatalogVersion());
        newPromotedItem.setItem(item);
        newPromotedItems.add(newPromotedItem);
        searchConfiguration.setPromotedItems(newPromotedItems);
        this.modelService.save(searchConfiguration);
    }


    public boolean canPerform(ActionContext<DocumentModel> ctx)
    {
        DocumentModel data = (DocumentModel)ctx.getData();
        if(data == null || data.getPk() == null)
        {
            return false;
        }
        return (!data.isPromoted() && !data.isFromSearchConfiguration());
    }
}
