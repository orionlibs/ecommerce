package de.hybris.platform.task.impl;

import com.google.common.collect.Iterables;
import de.hybris.platform.core.PK;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskService;
import de.hybris.platform.task.impl.gateways.AuxTaskGatewayDeadlockTest;
import de.hybris.platform.task.impl.gateways.TasksQueueGateway;
import de.hybris.platform.task.impl.gateways.WorkerStateGateway;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.util.MessageFormatUtils;
import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.tuple.Pair;
import org.assertj.core.api.AbstractThrowableAssert;
import org.assertj.core.api.Assertions;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;

public class AuxiliaryTablesTaskProviderTestHelper
{
    private final TaskService taskService;
    private final JdbcTemplate jdbcTemplate;
    private final Set<PK> tasks = new HashSet<>();
    private boolean taskEngineWasRunningBefore;
    private final TypeService typeService;
    private final AuxiliaryTablesGatewayFactory auxiliaryTablesGatewayFactory;
    private final ModelService modelService;


    @Deprecated
    public AuxiliaryTablesTaskProviderTestHelper(TaskService taskService, JdbcTemplate jdbcTemplate)
    {
        this(taskService, jdbcTemplate, null, null, null);
    }


    public AuxiliaryTablesTaskProviderTestHelper(TaskService taskService, JdbcTemplate jdbcTemplate, TypeService typeService, AuxiliaryTablesGatewayFactory auxiliaryTablesGatewayFactory, ModelService modelService)
    {
        this.taskService = taskService;
        this.jdbcTemplate = jdbcTemplate;
        this.typeService = typeService;
        this.auxiliaryTablesGatewayFactory = auxiliaryTablesGatewayFactory;
        this.modelService = modelService;
    }


    public void disableTaskEngine()
    {
        if(this.taskService.getEngine().isRunning())
        {
            this.taskEngineWasRunningBefore = true;
            this.taskService.getEngine().stop();
        }
        Assertions.assertThat(this.taskService.getEngine().isRunning()).isFalse();
    }


    public void enableTaskEngine()
    {
        if(this.taskEngineWasRunningBefore)
        {
            this.taskService.getEngine().start();
        }
    }


    public void assertTableExists(String tableName)
    {
        Pair<Integer, Integer> testInt = (Pair<Integer, Integer>)this.jdbcTemplate.queryForObject(
                        MessageFormatUtils.format("SELECT 1, COUNT(*) FROM {0}", new Object[] {tableName}), (resultSet, i) -> Pair.of(Integer.valueOf(resultSet.getInt(1)), Integer.valueOf(resultSet.getInt(2))));
        Assertions.assertThat((Comparable)testInt).isNotNull();
        Assertions.assertThat((Integer)testInt.getLeft()).isEqualTo(1);
    }


    public void assertTableNotExists(String tableName)
    {
        LoggerFactory.getLogger(AuxiliaryTablesTaskProviderTestHelper.class)
                        .info("table {} should not be present in DB", tableName);
        ((AbstractThrowableAssert)Assertions.assertThatThrownBy(() -> this.jdbcTemplate.execute(MessageFormatUtils.format("SELECT * FROM {0}", new Object[] {tableName}))).as("table %s should not exist", new Object[] {tableName})).isInstanceOf(BadSqlGrammarException.class);
    }


    public void prepareTasks(int numberOfTasks, String nodeGroup)
    {
        int batchSize = 1000;
        int i;
        for(i = 0; i < numberOfTasks; i += 1000)
        {
            prepareTasksInTx(numberOfTasks, 1000, i, nodeGroup);
        }
    }


    private void prepareTasksInTx(int numberOfTasks, int batchSize, int batchNumber, String nodeGroup)
    {
        try
        {
            Transaction.current().execute(() -> {
                AuxTaskGatewayDeadlockTest.LOGGER.info("Preparing tasks, batch {}", Integer.valueOf(batchNumber));
                for(int j = 0; j < batchSize; j++)
                {
                    if(j + batchNumber >= numberOfTasks)
                    {
                        break;
                    }
                    TaskModel task = (TaskModel)this.modelService.create(TaskModel.class);
                    task.setRunnerBean("passthroughRunner");
                    task.setNodeGroup(nodeGroup);
                    this.modelService.save(task);
                    this.tasks.add(task.getPk());
                }
                AuxTaskGatewayDeadlockTest.LOGGER.info("Prepared tasks, batch {}", Integer.valueOf(batchNumber));
                return null;
            });
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }


    public void cleanUpTasks()
    {
        Iterables.partition(this.tasks, 1000).forEach(pks -> {
            try
            {
                AuxTaskGatewayDeadlockTest.LOGGER.info("removing tasks, {}", Integer.valueOf(pks.size()));
                Transaction.current().execute(());
            }
            catch(Exception e)
            {
                AuxTaskGatewayDeadlockTest.LOGGER.warn(e.getMessage(), e);
            }
        });
    }


    public long copyTasksToAuxQueue(TasksQueueGateway tasksQueueGateway, int rangeStart, int rangeEnd)
    {
        TestAuxiliarySchedulerRole testAuxiliarySchedulerRole = new TestAuxiliarySchedulerRole();
        testAuxiliarySchedulerRole.setGatewayFactory(this.auxiliaryTablesGatewayFactory);
        testAuxiliarySchedulerRole.setTypeService(this.typeService);
        String tasksQuery = testAuxiliarySchedulerRole.getTasksQuery(rangeStart, rangeEnd);
        String expiredTasksQuery = testAuxiliarySchedulerRole.getExpiredTasksQuery(rangeStart, rangeEnd);
        return tasksQueueGateway.addTasks(tasksQuery, expiredTasksQuery, Instant.now(), rangeStart, rangeEnd);
    }


    public WorkerStateGateway.WorkerState getWorkerState(int nodeId)
    {
        return new WorkerStateGateway.WorkerState(nodeId, Duration.ZERO, false,
                        Set.of());
    }


    public WorkerStateGateway.WorkerState getWorkerState(int nodeId, String... nodeGroup)
    {
        return new WorkerStateGateway.WorkerState(nodeId, Duration.ZERO, false,
                        Set.of(nodeGroup));
    }
}
