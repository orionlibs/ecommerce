package de.hybris.platform.persistence.audit.gateway.impl;

import de.hybris.platform.persistence.audit.gateway.AuditSqlQuery;
import de.hybris.platform.persistence.audit.gateway.JsonAuditRecord;
import de.hybris.platform.util.Config;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

public class AuditRecordQueryExecutor
{
    private final AuditRecordExtractor recordExtractor;
    private final JdbcTemplate jdbcTemplate;
    private final int jdbcFetchSize;


    public AuditRecordQueryExecutor(AuditRecordExtractor recordExtractor, JdbcTemplate jdbcTemplate, int jdbcFetchSize)
    {
        this.recordExtractor = recordExtractor;
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcFetchSize = jdbcFetchSize;
    }


    public List<JsonAuditRecord> executeQuery(AuditSqlQuery query)
    {
        if(!query.isQueryWithLimit())
        {
            return queryAndExtractRecords(query.getSqlQuery(), query.getQueryPrams(), this.recordExtractor);
        }
        return executeQueryWithLimit(query);
    }


    private List<JsonAuditRecord> executeQueryWithLimit(AuditSqlQuery query)
    {
        List<JsonAuditRecord> resultRecords = new ArrayList<>();
        do
        {
            List<JsonAuditRecord> auditRecordsChunk = queryAndExtractRecords(query.getSqlQuery(), query
                            .getQueryPrams(), this.recordExtractor);
            resultRecords.addAll(auditRecordsChunk);
            query.incrementOffset();
        }
        while(this.recordExtractor.getCountedRows() == query.getLimit());
        return resultRecords;
    }


    private List<JsonAuditRecord> queryAndExtractRecords(String query, Object[] params, AuditRecordExtractor auditRecordExtractor)
    {
        return (List<JsonAuditRecord>)this.jdbcTemplate.query(query, preparedStatement -> {
            for(int i = 0; i < params.length; i++)
            {
                Object o = params[i];
                preparedStatement.setObject(i + 1, o);
            }
            setPreparedStatementFetchSize(preparedStatement);
        } (ResultSetExtractor)auditRecordExtractor);
    }


    private void setPreparedStatementFetchSize(PreparedStatement stmt) throws SQLException
    {
        if(Config.isMySQLUsed())
        {
            stmt.setFetchSize(-2147483648);
        }
        else if(Config.isSQLServerUsed() || Config.isHanaUsed() || Config.isPostgreSQLUsed() || Config.isHSQLDBUsed())
        {
            stmt.setFetchSize(this.jdbcFetchSize);
        }
    }
}
