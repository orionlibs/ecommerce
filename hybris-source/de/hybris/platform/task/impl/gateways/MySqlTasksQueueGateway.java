package de.hybris.platform.task.impl.gateways;

import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.util.MessageFormatUtils;
import java.util.Collections;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;

public class MySqlTasksQueueGateway extends DefaultTasksQueueGateway
{
    private static final String CREATE_TABLE = "CREATE TABLE {0} (PK BIGINT PRIMARY KEY, RANGE_VALUE BIGINT, NODE_ID INT(11) NOT NULL DEFAULT -1, NODE_GROUP VARCHAR(255) NOT NULL DEFAULT ''{1}'', PROCESSING_TIME TIMESTAMP(0) NOT NULL DEFAULT NOW(), EXECUTION_TIME BIGINT, INDEX {0}_idx1(PROCESSING_TIME ASC), INDEX {0}_idx2(EXECUTION_TIME ASC, RANGE_VALUE ASC, PROCESSING_TIME ASC), INDEX {0}_idx3(NODE_ID, NODE_GROUP))";


    public MySqlTasksQueueGateway(JdbcTemplate jdbcTemplate, TypeService typeService)
    {
        super(jdbcTemplate, typeService);
    }


    protected String getCreateTableStatement()
    {
        return MessageFormatUtils.format(
                        "CREATE TABLE {0} (PK BIGINT PRIMARY KEY, RANGE_VALUE BIGINT, NODE_ID INT(11) NOT NULL DEFAULT -1, NODE_GROUP VARCHAR(255) NOT NULL DEFAULT ''{1}'', PROCESSING_TIME TIMESTAMP(0) NOT NULL DEFAULT NOW(), EXECUTION_TIME BIGINT, INDEX {0}_idx1(PROCESSING_TIME ASC), INDEX {0}_idx2(EXECUTION_TIME ASC, RANGE_VALUE ASC, PROCESSING_TIME ASC), INDEX {0}_idx3(NODE_ID, NODE_GROUP))",
                        new Object[] {getTableName(), getEmptyGroupValue()});
    }


    protected List<String> getCreateTableIndexStatements()
    {
        return Collections.emptyList();
    }
}
