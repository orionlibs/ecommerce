package de.hybris.platform.task.impl.gateways;

import de.hybris.platform.persistence.property.JDBCValueMappings;
import de.hybris.platform.util.MessageFormatUtils;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;

public class DefaultSchedulerStateGateway extends DefaultBaseGateway implements SchedulerStateGateway
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSchedulerStateGateway.class);
    private static final String TABLE_NAME = "tasks_aux_scheduler";
    private static final String CREATE_TABLE = "CREATE TABLE {0} (ID VARCHAR(255), LAST_ACTIVITY_TS timestamp(3), VERSION INT, unique (ID))";
    private static final String INSERT_SCHEDULER_ROW = "INSERT INTO {0} (ID, LAST_ACTIVITY_TS, VERSION) VALUES(?, CURRENT_TIMESTAMP(3), ?)";
    private static final String UPDATE_SCHEDULER_ROW = "UPDATE {0} SET LAST_ACTIVITY_TS= ? WHERE ID = ? AND LAST_ACTIVITY_TS=?";
    private static final String DELETE_SCHEDULER_ROW = "DELETE FROM {0} WHERE ID = ?";
    private static final String UPDATE_SCHEDULER_ROW_BY_DURATION = "UPDATE {0} SET LAST_ACTIVITY_TS=CURRENT_TIMESTAMP(3) WHERE ID = ? AND DATEDIFF(LAST_ACTIVITY_TS, CURRENT_TIMESTAMP(3)) > ?";
    private static final String SELECT_SCHEDULER_TIMESTAMP = "SELECT LAST_ACTIVITY_TS, VERSION, CURRENT_TIMESTAMP(3) FROM {0}";
    private static final String SCHEDULER_ID = "scheduler";
    private static final JDBCValueMappings.ValueWriter<Date, ?> dateWriter = JDBCValueMappings.getJDBCValueWriter(Date.class);
    private static final JDBCValueMappings.ValueReader<Date, ?> dateReader = JDBCValueMappings.getJDBCValueReader(Date.class);
    private static final JDBCValueMappings.ValueReader<Integer, ?> intReader = JDBCValueMappings.getJDBCValueReader(Integer.class);
    private static final JDBCValueMappings.ValueWriter<Long, ?> longWriter = JDBCValueMappings.getJDBCValueWriter(Long.class);


    public DefaultSchedulerStateGateway(JdbcTemplate jdbcTemplate)
    {
        super(jdbcTemplate);
    }


    public String getTableName()
    {
        return getTenantAwareTableName("tasks_aux_scheduler");
    }


    public boolean insertSchedulerRow(Instant now, int version)
    {
        try
        {
            int insertRows = this.jdbcTemplate.update(getInsertSchedulerRowStatement(), new Object[] {"scheduler",
                            Integer.valueOf(version)});
            return (insertRows == 1);
        }
        catch(Exception e)
        {
            LOGGER.warn("Error while inserting scheduler row", e);
            return false;
        }
    }


    protected String getInsertSchedulerRowStatement()
    {
        return MessageFormatUtils.format("INSERT INTO {0} (ID, LAST_ACTIVITY_TS, VERSION) VALUES(?, CURRENT_TIMESTAMP(3), ?)", new Object[] {getTableName()});
    }


    public boolean updateSchedulerRow(Instant now, Instant oldTimestamp)
    {
        int updatedRows = this.jdbcTemplate.update(getUpdateSchedulerRowStatement(), preparedStatement -> {
            dateWriter.setValue(preparedStatement, 1, Date.from(now));
            preparedStatement.setString(2, "scheduler");
            dateWriter.setValue(preparedStatement, 3, Date.from(oldTimestamp));
        });
        return (updatedRows == 1);
    }


    public boolean updateSchedulerRow(Duration duration)
    {
        int updatedRows = this.jdbcTemplate.update(getUpdateSchedulerRowByDurationStatement(), preparedStatement -> {
            preparedStatement.setString(1, "scheduler");
            longWriter.setValue(preparedStatement, 2, Long.valueOf(duration.toMillis()));
        });
        return (updatedRows == 1);
    }


    public boolean deleteSchedulerRow()
    {
        try
        {
            int deletedRows = this.jdbcTemplate.update(geDeleteSchedulerRowStatement(), preparedStatement -> preparedStatement.setString(1, "scheduler"));
            return (deletedRows == 1);
        }
        catch(Exception e)
        {
            logExceptionDuringDelete(e);
            return false;
        }
    }


    private void logExceptionDuringDelete(Exception e)
    {
        LOGGER.info("Error while deleting scheduler row: {}", e.getMessage());
        LOGGER.debug("Error while deleting scheduler row", e);
    }


    private String getUpdateSchedulerRowStatement()
    {
        return MessageFormatUtils.format("UPDATE {0} SET LAST_ACTIVITY_TS= ? WHERE ID = ? AND LAST_ACTIVITY_TS=?", new Object[] {getTableName()});
    }


    protected String getUpdateSchedulerRowByDurationStatement()
    {
        return MessageFormatUtils.format("UPDATE {0} SET LAST_ACTIVITY_TS=CURRENT_TIMESTAMP(3) WHERE ID = ? AND DATEDIFF(LAST_ACTIVITY_TS, CURRENT_TIMESTAMP(3)) > ?", new Object[] {getTableName()});
    }


    private String geDeleteSchedulerRowStatement()
    {
        return MessageFormatUtils.format("DELETE FROM {0} WHERE ID = ?", new Object[] {getTableName()});
    }


    public Optional<SchedulerState> getSchedulerTimestamp()
    {
        List<SchedulerState> schedulerStates;
        try
        {
            schedulerStates = this.jdbcTemplate.query(getSelectSchedulerTimestampQuery(), (resultSet, i) -> new SchedulerState(((Date)dateReader.getValue(resultSet, 1)).toInstant(), ((Date)dateReader.getValue(resultSet, 3)).toInstant(), ((Integer)intReader.getValue(resultSet, 2)).intValue()));
        }
        catch(BadSqlGrammarException exception)
        {
            tryCreatingTableAndLogException(LOGGER, "Error while obtaining scheduler health check timestamp", (Exception)exception);
            return Optional.empty();
        }
        if(!schedulerStates.isEmpty())
        {
            return Optional.of(schedulerStates.get(0));
        }
        return Optional.empty();
    }


    protected String getCreateTableStatement()
    {
        return MessageFormatUtils.format("CREATE TABLE {0} (ID VARCHAR(255), LAST_ACTIVITY_TS timestamp(3), VERSION INT, unique (ID))", new Object[] {getTableName()});
    }


    protected String getSelectSchedulerTimestampQuery()
    {
        return MessageFormatUtils.format("SELECT LAST_ACTIVITY_TS, VERSION, CURRENT_TIMESTAMP(3) FROM {0}", new Object[] {getTableName()});
    }
}
