package de.hybris.platform.configurablebundlebackoffice.widgets.actions;

import com.google.common.collect.Maps;
import com.hybris.backoffice.widgets.actions.sync.SyncAction;
import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.collector.RelatedItemsCollector;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;

public class SyncTreeAction extends SyncAction
{
    @Resource(name = "configurableBundleRelatedItemsCollector")
    private RelatedItemsCollector configurableBundleRelatedItemsCollector;


    public boolean canPerform(ActionContext<Object> context)
    {
        return (hasValidPayload(context) && super.canPerform(context));
    }


    public ActionResult<List> perform(ActionContext<Object> context)
    {
        verifyContextData(context);
        context.setData(collectTreeItems(context));
        return super.perform(context);
    }


    protected List<ItemModel> collectTreeItems(ActionContext<Object> context)
    {
        if(context.getData() instanceof Collection)
        {
            Collection<ItemModel> items = (Collection<ItemModel>)context.getData();
            return (List<ItemModel>)items.stream().flatMap(itemModel -> collectTreeItems(itemModel).stream()).distinct()
                            .collect(Collectors.toList());
        }
        return collectTreeItems((ItemModel)context.getData());
    }


    protected void verifyContextData(ActionContext<Object> context)
    {
        if(!hasValidPayload(context))
        {
            throw new IllegalArgumentException("Sync tree action can only work for action context with ItemModel objects passed as payload");
        }
    }


    protected boolean hasValidPayload(ActionContext<Object> context)
    {
        Object data = context.getData();
        return (isItemModel(data) || isItemModelsCollection(data));
    }


    protected boolean isItemModelsCollection(Object data)
    {
        return (data instanceof Collection && ((Collection)data).stream().allMatch(this::isItemModel));
    }


    private boolean isItemModel(Object data)
    {
        return data instanceof ItemModel;
    }


    protected List<ItemModel> collectTreeItems(ItemModel item)
    {
        return getConfigurableBundleRelatedItemsCollector().collect(item, Maps.newHashMap());
    }


    protected RelatedItemsCollector getConfigurableBundleRelatedItemsCollector()
    {
        return this.configurableBundleRelatedItemsCollector;
    }


    public void setConfigurableBundleRelatedItemsCollector(RelatedItemsCollector relatedItemsCollector)
    {
        this.configurableBundleRelatedItemsCollector = relatedItemsCollector;
    }
}
