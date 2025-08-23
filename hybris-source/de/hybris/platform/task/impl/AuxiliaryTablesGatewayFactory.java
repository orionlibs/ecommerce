package de.hybris.platform.task.impl;

import com.google.common.base.Suppliers;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.task.impl.gateways.AdditionalDatabaseData;
import de.hybris.platform.task.impl.gateways.DefaultSchedulerStateGateway;
import de.hybris.platform.task.impl.gateways.DefaultTasksQueueGateway;
import de.hybris.platform.task.impl.gateways.DefaultWorkerStateGateway;
import de.hybris.platform.task.impl.gateways.HanaSchedulerStateGateway;
import de.hybris.platform.task.impl.gateways.HanaTasksQueueGateway;
import de.hybris.platform.task.impl.gateways.HsqldbSchedulerStateGateway;
import de.hybris.platform.task.impl.gateways.HsqldbTasksQueueGateway;
import de.hybris.platform.task.impl.gateways.HsqldbWorkerStateGateway;
import de.hybris.platform.task.impl.gateways.MsSqlSchedulerStateGateway;
import de.hybris.platform.task.impl.gateways.MsSqlTasksQueueGateway;
import de.hybris.platform.task.impl.gateways.MsSqlWorkerStateGateway;
import de.hybris.platform.task.impl.gateways.MySqlSchedulerStateGateway;
import de.hybris.platform.task.impl.gateways.MySqlTasksQueueGateway;
import de.hybris.platform.task.impl.gateways.OracleSchedulerStateGateway;
import de.hybris.platform.task.impl.gateways.OracleTasksQueueGateway;
import de.hybris.platform.task.impl.gateways.OracleWorkerStateGateway;
import de.hybris.platform.task.impl.gateways.PostgresSchedulerStateGateway;
import de.hybris.platform.task.impl.gateways.PostgresTasksQueueGateway;
import de.hybris.platform.task.impl.gateways.PostgresWorkerStateGateway;
import de.hybris.platform.task.impl.gateways.SchedulerStateGateway;
import de.hybris.platform.task.impl.gateways.TasksQueueGateway;
import de.hybris.platform.task.impl.gateways.WorkerStateGateway;
import de.hybris.platform.util.Config;
import java.util.Optional;
import java.util.function.Supplier;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jdbc.core.JdbcTemplate;

public class AuxiliaryTablesGatewayFactory
{
    private JdbcTemplate jdbcTemplate;
    private TypeService typeService;
    private final Supplier<Optional<AdditionalDatabaseData>> additionalDataSupplier = (Supplier<Optional<AdditionalDatabaseData>>)Suppliers.memoize(() -> AdditionalDatabaseData.createAdditionalDatabaseData(getJdbcTemplate()));


    public TasksQueueGateway getTasksQueueGateway()
    {
        switch(null.$SwitchMap$de$hybris$platform$util$Config$DatabaseName[Config.getDatabaseName().ordinal()])
        {
            case 1:
                return (TasksQueueGateway)new HanaTasksQueueGateway(getJdbcTemplate(), this.typeService, this.additionalDataSupplier);
            case 2:
                return (TasksQueueGateway)new MySqlTasksQueueGateway(getJdbcTemplate(), this.typeService);
            case 3:
                return (TasksQueueGateway)new OracleTasksQueueGateway(getJdbcTemplate(), this.typeService);
            case 4:
                return (TasksQueueGateway)new MsSqlTasksQueueGateway(getJdbcTemplate(), this.typeService);
            case 5:
                return (TasksQueueGateway)new PostgresTasksQueueGateway(getJdbcTemplate(), this.typeService);
            case 6:
                return (TasksQueueGateway)new HsqldbTasksQueueGateway(getJdbcTemplate(), this.typeService);
        }
        return (TasksQueueGateway)new DefaultTasksQueueGateway(getJdbcTemplate(), this.typeService);
    }


    public SchedulerStateGateway getSchedulerStateGateway()
    {
        switch(null.$SwitchMap$de$hybris$platform$util$Config$DatabaseName[Config.getDatabaseName().ordinal()])
        {
            case 1:
                return (SchedulerStateGateway)new HanaSchedulerStateGateway(getJdbcTemplate());
            case 3:
                return (SchedulerStateGateway)new OracleSchedulerStateGateway(getJdbcTemplate());
            case 4:
                return (SchedulerStateGateway)new MsSqlSchedulerStateGateway(getJdbcTemplate());
            case 5:
                return (SchedulerStateGateway)new PostgresSchedulerStateGateway(getJdbcTemplate());
            case 6:
                return (SchedulerStateGateway)new HsqldbSchedulerStateGateway(getJdbcTemplate());
            case 2:
                return (SchedulerStateGateway)new MySqlSchedulerStateGateway(getJdbcTemplate());
        }
        return (SchedulerStateGateway)new DefaultSchedulerStateGateway(getJdbcTemplate());
    }


    public WorkerStateGateway getWorkerStateGateway()
    {
        switch(null.$SwitchMap$de$hybris$platform$util$Config$DatabaseName[Config.getDatabaseName().ordinal()])
        {
            case 3:
                return (WorkerStateGateway)new OracleWorkerStateGateway(getJdbcTemplate());
            case 5:
                return (WorkerStateGateway)new PostgresWorkerStateGateway(getJdbcTemplate());
            case 4:
                return (WorkerStateGateway)new MsSqlWorkerStateGateway(getJdbcTemplate());
            case 6:
                return (WorkerStateGateway)new HsqldbWorkerStateGateway(getJdbcTemplate());
        }
        return (WorkerStateGateway)new DefaultWorkerStateGateway(getJdbcTemplate());
    }


    @Required
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    public JdbcTemplate getJdbcTemplate()
    {
        return this.jdbcTemplate;
    }
}
