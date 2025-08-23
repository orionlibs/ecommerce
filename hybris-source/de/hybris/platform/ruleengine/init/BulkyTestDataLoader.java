package de.hybris.platform.ruleengine.init;

import com.google.common.base.Stopwatch;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.ruleengine.concurrency.RuleEngineTaskProcessor;
import de.hybris.platform.ruleengine.concurrency.TaskExecutionFuture;
import de.hybris.platform.ruleengine.concurrency.TaskResult;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.List;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BulkyTestDataLoader<T extends ItemModel> extends ServicelayerTest
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BulkyTestDataLoader.class);
    private static String PRE_DESTROY_TOUT_PARAM = "ruleengine.test.dataload.task.predestroytimeout";
    @Resource
    private ModelService modelService;
    @Resource
    private ConfigurationService configurationService;
    @Resource
    private RuleEngineTaskProcessor<T, TaskResult> defaultRuleEngineTaskProcessor;
    protected Stopwatch stopwatch = Stopwatch.createUnstarted();


    public void loadData(List<T> itemModels)
    {
        long predestroyTimeout = getConfigurationService().getConfiguration().getLong(PRE_DESTROY_TOUT_PARAM, 10000L);
        this.stopwatch.start();
        TaskExecutionFuture<TaskResult> taskExecutionFuture = this.defaultRuleEngineTaskProcessor.execute(itemModels, itemsPartition -> this.modelService.saveAll(itemsPartition), predestroyTimeout);
        taskExecutionFuture.waitForTasksToFinish();
        LOGGER.info("Loading bulky data ({} items partitioned in N threads) finished in [{}]", Integer.valueOf(itemModels.size()), this.stopwatch
                        .stop().toString());
    }


    protected ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }
}
