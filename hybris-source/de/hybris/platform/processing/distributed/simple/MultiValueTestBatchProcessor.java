package de.hybris.platform.processing.distributed.simple;

import com.google.common.base.Preconditions;
import de.hybris.platform.core.PK;
import de.hybris.platform.processing.model.SimpleBatchModel;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class MultiValueTestBatchProcessor implements SimpleBatchProcessor
{
    private static final Logger LOG = LoggerFactory.getLogger(MultiValueTestBatchProcessor.class);
    private ModelService modelService;


    public void process(SimpleBatchModel inputBatch)
    {
        List<List<?>> ctx = asList(inputBatch.getContext());
        ctx.stream().forEach(row -> {
            PK pkToRemove = row.get(0);
            String code = (String)row.get(1);
            this.modelService.remove(pkToRemove);
            LOG.info("Removed item with PK: {} and code: {}", pkToRemove, code);
        });
    }


    protected List<List<?>> asList(Object ctx)
    {
        Preconditions.checkState(ctx instanceof List, "ctx must be instance of List");
        return (List<List<?>>)ctx;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
