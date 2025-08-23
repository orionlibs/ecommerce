package de.hybris.deltadetection.jobs;

import de.hybris.deltadetection.ItemChangeDTO;
import de.hybris.deltadetection.model.ScriptChangeConsumptionJobModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.scripting.engine.ScriptExecutable;
import de.hybris.platform.scripting.engine.ScriptExecutionResult;
import de.hybris.platform.scripting.engine.ScriptingLanguagesService;
import java.io.StringWriter;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class ScriptChangeConsumptionJobPerformable extends AbstractChangeProcessorJobPerformable
{
    private static final Logger LOG = Logger.getLogger(ScriptChangeConsumptionJobPerformable.class.getName());
    private ScriptingLanguagesService scriptingLanguagesService;


    void init(Map<String, Object> ctx, CronJobModel cronjob)
    {
        ScriptChangeConsumptionJobModel job = (ScriptChangeConsumptionJobModel)cronjob.getJob();
        String scriptURI = job.getScriptURI();
        ScriptExecutable executable = this.scriptingLanguagesService.getExecutableByURI(scriptURI);
        ctx.put("executable", executable);
        ctx.put("cronjob", cronjob);
        ctx.put("log", LOG);
        super.init(ctx, cronjob);
    }


    boolean processChange(ItemChangeDTO change, Map<String, Object> ctx)
    {
        ctx.put("change", change);
        ScriptExecutable executable = (ScriptExecutable)ctx.get("executable");
        ScriptExecutionResult executionResult = executable.execute(ctx);
        boolean result = true;
        if(executionResult != null)
        {
            if(executionResult.getScriptResult() instanceof Boolean)
            {
                result = ((Boolean)executionResult.getScriptResult()).booleanValue();
            }
            LOG.info(((StringWriter)executionResult.getOutputWriter()).getBuffer().toString());
            LOG.info("### Finished executing script, returned script result = " + executionResult.getScriptResult() + " ###");
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Continue Processing changes: " + result);
        }
        return result;
    }


    @Required
    public void setScriptingLanguagesService(ScriptingLanguagesService scriptingLanguagesService)
    {
        this.scriptingLanguagesService = scriptingLanguagesService;
    }
}
