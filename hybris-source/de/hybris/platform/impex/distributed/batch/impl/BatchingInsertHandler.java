package de.hybris.platform.impex.distributed.batch.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.impex.jalo.header.StandardColumnDescriptor;
import de.hybris.platform.servicelayer.impex.ProcessMode;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class BatchingInsertHandler extends AbstractBatchingCRUDHandler
{
    public BatchingInsertHandler(ModelService modelService, UserService userService, SessionService sessionService)
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
            ItemModel itemModel = (ItemModel)getModelService().create(data.getTypeCode());
            Map<StandardColumnDescriptor, Object> currentModeValues = data.getCurrentModeValues();
            fillModel(itemModel, currentModeValues, ProcessMode.INSERT);
            if(data.isPartiallyUnresolved())
            {
                unresolved.add(data);
            }
            consumer.accept(itemModel, data, unresolved);
        }
        return unresolved;
    }


    protected Map<String, Object> getSessionAttributes(Collection<ItemModel> models)
    {
        boolean anyToCreate = models.stream().anyMatch(Objects::nonNull);
        if(anyToCreate)
        {
            return Collections.singletonMap("impex.creation", Boolean.TRUE);
        }
        return Collections.emptyMap();
    }
}
