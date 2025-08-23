package de.hybris.platform.servicelayer.impex.impl;

import de.hybris.platform.impex.model.ImportBatchModel;
import de.hybris.platform.processing.model.BatchModel;
import de.hybris.platform.processing.model.DistributedProcessModel;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ImportServiceHelper
{
    private static final Logger LOG = LoggerFactory.getLogger(ImportServiceHelper.class);


    public static void clearImportBatchContent(ModelService modelService, DistributedProcessModel distributedProcessModel)
    {
        Collection<BatchModel> batches = distributedProcessModel.getBatches();
        Set<String> importContentCodes = new HashSet<>();
        Objects.requireNonNull(ImportBatchModel.class);
        Objects.requireNonNull(ImportBatchModel.class);
        batches.stream().filter(ImportBatchModel.class::isInstance).map(ImportBatchModel.class::cast)
                        .forEach(importBatch -> tryToRemoveImportBatchContent(modelService, importContentCodes, importBatch));
    }


    private static void tryToRemoveImportBatchContent(ModelService modelService, Set<String> importContentCodes, ImportBatchModel importBatch)
    {
        String importContentCode = importBatch.getImportContentCode();
        try
        {
            if(!importContentCodes.contains(importContentCode) && importBatch.getImportBatchContent() != null)
            {
                modelService.remove(importBatch.getImportBatchContent());
                importContentCodes.add(importContentCode);
            }
        }
        catch(IllegalStateException ise)
        {
            LOG.debug("ImportBatchContent with code {} could not be removed due to error: {}", importContentCode, ise
                            .getMessage());
        }
    }
}
