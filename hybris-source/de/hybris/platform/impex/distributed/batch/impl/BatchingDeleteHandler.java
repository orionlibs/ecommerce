package de.hybris.platform.impex.distributed.batch.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;

public class BatchingDeleteHandler extends AbstractBatchingCRUDHandler
{
    public BatchingDeleteHandler(ModelService modelService, UserService userService, SessionService sessionService)
    {
        super(modelService, userService, sessionService);
    }


    protected Set<BatchData.ImportData> process(Collection<BatchData.ImportData> importDatas, AbstractBatchingCRUDHandler.TriConsumer<ItemModel, BatchData.ImportData, Set<BatchData.ImportData>> consumer)
    {
        Set<BatchData.ImportData> unresolved = new LinkedHashSet<>();
        for(BatchData.ImportData data : importDatas)
        {
            if(data.isUnresolved() || data.isUnrecoverable())
            {
                unresolved.add(data);
                continue;
            }
            List<ItemModel> existingItems = data.getExistingItems();
            if(CollectionUtils.isNotEmpty(existingItems))
            {
                for(ItemModel itemModel : existingItems)
                {
                    consumer.accept(itemModel, data, unresolved);
                }
            }
        }
        return unresolved;
    }


    protected void bulkCommit(Map<BatchData.ImportData, ItemModel> itemModels)
    {
        if(itemModels.isEmpty())
        {
            return;
        }
        Collection<ItemModel> toRemove = itemModels.values();
        getModelService().removeAll(toRemove);
    }


    protected void commit(BatchData.ImportData importData, ItemModel itemModel)
    {
        getModelService().remove(itemModel);
    }
}
