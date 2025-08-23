package de.hybris.platform.processing.distributed.simple;

import de.hybris.platform.processing.distributed.DistributedProcessService;
import de.hybris.platform.processing.model.SimpleBatchModel;
import de.hybris.platform.scripting.engine.ScriptExecutable;
import de.hybris.platform.scripting.engine.ScriptingLanguagesService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import java.util.Objects;

public class SimpleScriptingDistributedProcessHandler extends SimpleAbstractDistributedProcessHandler
{
    protected final ScriptingLanguagesService scriptingLanguagesService;


    protected SimpleScriptingDistributedProcessHandler(ModelService modelService, FlexibleSearchService flexibleSearchService, DistributedProcessService distributedProcessService, ScriptingLanguagesService scriptingLanguagesService)
    {
        super(modelService, flexibleSearchService, distributedProcessService);
        this.scriptingLanguagesService = Objects.<ScriptingLanguagesService>requireNonNull(scriptingLanguagesService, "scriptingLanguagesService is required");
    }


    public void processBatch(SimpleBatchModel inputBatch)
    {
        ScriptExecutable executable = this.scriptingLanguagesService.getExecutableByURI(getScriptURI(inputBatch));
        SimpleBatchProcessor processor = (SimpleBatchProcessor)executable.getAsInterface(SimpleBatchProcessor.class);
        processor.process(inputBatch);
    }


    protected String getScriptURI(SimpleBatchModel inputBatch)
    {
        String scriptCode = Objects.<String>requireNonNull(inputBatch.getScriptCode());
        return "model://" + scriptCode;
    }
}
