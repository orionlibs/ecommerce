package de.hybris.platform.impex.distributed.batch.impl;

import com.google.common.collect.ImmutableMap;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.impex.jalo.header.StandardColumnDescriptor;
import de.hybris.platform.servicelayer.impex.ProcessMode;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class BatchingUpdateHandler extends AbstractBatchingCRUDHandler
{
    public BatchingUpdateHandler(ModelService modelService, UserService userService, SessionService sessionService)
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
            if(data.hasExistingItems())
            {
                Map<StandardColumnDescriptor, Object> currentModeValues = data.getCurrentModeValues();
                for(ItemModel itemModel : data.getExistingItems())
                {
                    fillModel(itemModel, currentModeValues, ProcessMode.UPDATE);
                    consumer.accept(itemModel, data, unresolved);
                }
            }
            else
            {
                data.markUnresolved("Existing item not found");
                unresolved.add(data);
            }
            if(data.isPartiallyUnresolved())
            {
                unresolved.add(data);
            }
        }
        return unresolved;
    }


    protected Map<String, Object> getSessionAttributes(Collection<ItemModel> models)
    {
        Set<PK> pksToModify = (Set<PK>)models.stream().filter(Objects::nonNull).map(AbstractItemModel::getPk).filter(Objects::nonNull).collect(Collectors.toSet());
        if(pksToModify.isEmpty())
        {
            return Collections.emptyMap();
        }
        return (Map<String, Object>)ImmutableMap.of("impex.modification", Boolean.TRUE, "impex.modification.pk", pksToModify);
    }
}
