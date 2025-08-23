package de.hybris.platform.adaptivesearchbackoffice.actions.results.excludeitem;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import de.hybris.platform.adaptivesearch.model.AbstractAsConfigurableSearchConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AsExcludedItemModel;
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

public class ExcludeItemAction extends AbstractResultAction
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
        excludeResult(ctx);
        refreshSearchResults(ctx);
        return new ActionResult("success");
    }


    protected void excludeResult(ActionContext<DocumentModel> ctx)
    {
        NavigationContextData navigationContext = getNavigationContext(ctx);
        SearchContextData searchContext = getSearchContext(ctx);
        AbstractAsConfigurableSearchConfigurationModel searchConfiguration = this.asSearchConfigurationFacade.getOrCreateSearchConfiguration(navigationContext, searchContext);
        List<AsExcludedItemModel> newExcludedItems = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(searchConfiguration.getExcludedItems()))
        {
            newExcludedItems.addAll(searchConfiguration.getExcludedItems());
        }
        ItemModel item = (ItemModel)this.modelService.get(((DocumentModel)ctx.getData()).getPk());
        AsExcludedItemModel newExcludedItem = new AsExcludedItemModel();
        newExcludedItem.setSearchConfiguration(searchConfiguration);
        newExcludedItem.setCatalogVersion(searchConfiguration.getCatalogVersion());
        newExcludedItem.setItem(item);
        newExcludedItems.add(newExcludedItem);
        searchConfiguration.setExcludedItems(newExcludedItems);
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
