package de.hybris.platform.persistence.audit.gateway.impl;

import com.google.common.collect.Maps;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.locking.ItemLockedForProcessingException;
import de.hybris.platform.directpersistence.DirectPersistenceUtils;
import de.hybris.platform.directpersistence.statement.backend.ServiceCol;
import de.hybris.platform.jdbcwrapper.HybrisDataSource;
import de.hybris.platform.persistence.audit.AuditScopeInvalidator;
import de.hybris.platform.persistence.audit.AuditType;
import de.hybris.platform.persistence.audit.gateway.AuditRecordCommand;
import de.hybris.platform.persistence.audit.gateway.AuditStorageUtils;
import de.hybris.platform.persistence.audit.gateway.LinkAuditRecordCommand;
import de.hybris.platform.persistence.audit.gateway.TypeAuditRecordCommand;
import de.hybris.platform.persistence.audit.internal.AuditEnablementService;
import de.hybris.platform.util.Config;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.JdbcUtils;

public class DefaultWriteAuditGateway extends AbstractWriteAuditGateway
{
    private JdbcTemplate jdbcTemplate;
    private AuditScopeInvalidator auditScopeInvalidator;
    private AuditEnablementService auditEnablementService;
    private static final int DEFAULT_AUDIT_BATCH_SIZE = 50;
    private static final String INSERT_TYPE_RECORD_STATEMENT = "INSERT INTO {0} (itempk, itemtypepk, timestamp, currenttimestamp, changinguser, payloadbefore, payloadafter, operationtype, context) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String INSERT_LINK_RECORD_STATEMENT = "INSERT INTO {0} (itempk, itemtypepk, timestamp, currenttimestamp, changinguser, payloadbefore, payloadafter, operationtype, sourcepk, targetpk, languagepk, context) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String DELETE_STATEMENT_FOR_TYPE = "DELETE FROM {0}";
    private static final String DELETE_STATEMENT_FOR_TYPE_AND_PK = "DELETE FROM {0} WHERE ITEMPK=?";
    private static final String SELECT_STATEMENT_FOR_TYPE = "SELECT DISTINCT ITEMPK FROM {0}";
    private static final String SELECT_SEALED_FOR_TYPE = "SELECT " + ServiceCol.SEALED + " FROM {0} WHERE " + ServiceCol.PK_STRING + "={1}";


    public void saveTypeAuditRecords(List<TypeAuditRecordCommand> cmdList)
    {
        saveAuditRecords((List)cmdList);
    }


    public void saveLinkAuditRecords(List<LinkAuditRecordCommand> cmdList)
    {
        saveAuditRecords((List)cmdList);
    }


    private void saveAuditRecords(List<? extends AuditRecordCommand> cmdList)
    {
        Connection conn = null;
        Map<String, PreparedStatement> pstmtMap = new HashMap<>();
        try
        {
            conn = getConnection();
            executePreparedStatementForCmd(conn, cmdList, pstmtMap);
        }
        catch(SQLException ex)
        {
            throw new IllegalStateException("Failed to persist audit record", ex);
        }
        finally
        {
            pstmtMap.entrySet().forEach(entry -> JdbcUtils.closeStatement((Statement)entry.getValue()));
            JdbcUtils.closeConnection(conn);
        }
    }


    protected int getWriteBatchSize()
    {
        int batchSize = Config.getInt("audit.write.jdbc.batch.size", 50);
        if(batchSize < 1)
        {
            batchSize = 50;
        }
        return batchSize;
    }


    private void executePreparedStatementForCmd(Connection conn, List<? extends AuditRecordCommand> cmdList, Map<String, PreparedStatement> pstmtMap)
    {
        int batchSize = getWriteBatchSize();
        String sql = null;
        try
        {
            int i = 0;
            List<AuditRecordCommand> onlySignificantCmdList = (List<AuditRecordCommand>)cmdList.stream().filter(cmd -> !isDifferenceInPayloadsInsignificant(cmd)).collect(Collectors.toList());
            Set<PreparedStatement> statementWithBatch = new HashSet<>();
            for(AuditRecordCommand cmd : onlySignificantCmdList)
            {
                i++;
                JDBCValueTranslator translator = createJDBCValueTranslator(cmd);
                sql = translator.getFinalQuery(cmd);
                PreparedStatement pstmt = createOrGetPreparedStmtFromCache(conn, pstmtMap, sql);
                fillStatement(pstmt, translator.generateParamsFromInstance(cmd));
                pstmt.addBatch();
                statementWithBatch.add(pstmt);
                if(checkIfExecuteBatch(batchSize, i, onlySignificantCmdList.size()))
                {
                    executeBatchForCurrentState(statementWithBatch);
                    statementWithBatch.clear();
                }
            }
        }
        catch(Exception ex)
        {
            throw new IllegalStateException(String.format("Failed to persist audit records (sql: %s)", new Object[] {sql}), ex);
        }
    }


    private static void executeBatchForCurrentState(Set<PreparedStatement> statementWithBatch) throws SQLException
    {
        for(PreparedStatement psmtToExecute : statementWithBatch)
        {
            psmtToExecute.executeBatch();
        }
    }


    private static boolean checkIfExecuteBatch(int batchSize, int currentIndex, int collectionSize)
    {
        return (currentIndex % batchSize == 0 || currentIndex == collectionSize);
    }


    private PreparedStatement createOrGetPreparedStmtFromCache(Connection conn, Map<String, PreparedStatement> pstmtMap, String query) throws SQLException
    {
        PreparedStatement pstmt;
        if(pstmtMap.containsKey(query))
        {
            pstmt = pstmtMap.get(query);
            pstmt.clearParameters();
        }
        else
        {
            pstmt = getPreparedStatement(conn, query);
            pstmtMap.put(query, pstmt);
        }
        return pstmt;
    }


    private static boolean isDifferenceInPayloadsInsignificant(AuditRecordCommand recCmd)
    {
        if(isNotTypeAuditRecordCmd(recCmd))
        {
            return false;
        }
        TypeAuditRecordCommand cmd = (TypeAuditRecordCommand)recCmd;
        if(cmd.getAuditType() != AuditType.MODIFICATION)
        {
            return false;
        }
        Map<String, Boolean> blacklistedAttributes = (Map<String, Boolean>)cmd.getContext().getOrDefault("blacklistedAttributes",
                        Collections.emptyMap());
        if(blacklistedAttributes.values().stream().anyMatch(aBoolean -> aBoolean.booleanValue()))
        {
            return false;
        }
        Map<String, Object> payloadBefore = Maps.filterKeys(cmd.getPayloadBefore(), s -> !Objects.equals(s, "modifiedtime"));
        Map<String, Object> payloadAfter = Maps.filterKeys(cmd.getPayloadAfter(), s -> !Objects.equals(s, "modifiedtime"));
        return payloadBefore.equals(payloadAfter);
    }


    private static boolean isNotTypeAuditRecordCmd(AuditRecordCommand recCmd)
    {
        return !(recCmd instanceof TypeAuditRecordCommand);
    }


    public int removeAuditRecordsForType(String type)
    {
        Objects.requireNonNull(type, "type is required");
        if(!getAuditEnablementService().isAuditEnabledForType(type))
        {
            return 0;
        }
        List<Long> pks = this.jdbcTemplate.queryForList(MessageFormat.format("SELECT DISTINCT ITEMPK FROM {0}", new Object[] {AuditStorageUtils.getAuditTableName(type)}), Long.class);
        for(Long pkLong : pks)
        {
            validateItemModel(type, PK.fromLong(pkLong.longValue()));
        }
        this.auditScopeInvalidator.clearCurrentAuditForType(type);
        return removeAuditRecords(template -> Integer.valueOf(template.update(MessageFormat.format("DELETE FROM {0}", new Object[] {AuditStorageUtils.getAuditTableName(type)}))));
    }


    public int removeAuditRecordsForType(String type, PK pk)
    {
        Objects.requireNonNull(type, "type is required");
        Objects.requireNonNull(pk, "pk is required");
        if(!getAuditEnablementService().isAuditEnabledForType(type))
        {
            return 0;
        }
        validateItemModel(type, pk);
        this.auditScopeInvalidator.clearCurrentAuditForPK(pk);
        return removeAuditRecords(template -> Integer.valueOf(template.update(MessageFormat.format("DELETE FROM {0} WHERE ITEMPK=?", new Object[] {AuditStorageUtils.getAuditTableName(type)}), new Object[] {pk.getLong()})));
    }


    private void validateItemModel(String type, PK pk)
    {
        List<Boolean> rows = this.jdbcTemplate.queryForList(MessageFormat.format(SELECT_SEALED_FOR_TYPE, new Object[] {DirectPersistenceUtils.getInfoMapForType(type)
                        .getItemTableName(), pk
                        .toString()}), Boolean.class);
        if(rows.isEmpty())
        {
            return;
        }
        if(rows.size() != 1)
        {
            throw new IllegalStateException("Multiple rows have been found for one pk: " + pk);
        }
        if(Boolean.TRUE.equals(rows.get(0)))
        {
            throw new ItemLockedForProcessingException("Item with PK: " + pk + " is locked for processing and its audit records cannot be removed");
        }
    }


    private Connection getConnection() throws SQLException
    {
        return getDataSource().getConnection();
    }


    private HybrisDataSource getDataSource()
    {
        return Registry.getCurrentTenant().getMasterDataSource();
    }


    private PreparedStatement getPreparedStatement(Connection con, String sqlQuery) throws SQLException
    {
        int resultSetType = Config.isHanaUsed() ? 1003 : 1004;
        return con.prepareStatement(sqlQuery, resultSetType, 1007);
    }


    private void fillStatement(PreparedStatement statement, List<Object> values) throws SQLException
    {
        if(values != null)
        {
            ArgumentPreparedStatementSetter argumentPreparedStatementSetter = new ArgumentPreparedStatementSetter(values.toArray());
            argumentPreparedStatementSetter.setValues(statement);
        }
    }


    private int removeAuditRecords(Function<JdbcTemplate, Integer> template)
    {
        return ((Integer)template.apply(this.jdbcTemplate)).intValue();
    }


    @Required
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }


    public void setAuditEnablementService(AuditEnablementService auditEnablementService)
    {
        this.auditEnablementService = auditEnablementService;
    }


    protected AuditEnablementService getAuditEnablementService()
    {
        if(this.auditEnablementService == null)
        {
            this
                            .auditEnablementService = (AuditEnablementService)Registry.getApplicationContext().getBean("auditingEnablementService", AuditEnablementService.class);
        }
        return this.auditEnablementService;
    }


    JDBCValueTranslator createJDBCValueTranslator(AuditRecordCommand cmd)
    {
        if(cmd instanceof TypeAuditRecordCommand)
        {
            return (JDBCValueTranslator)new TypeAuditJDBCValueTranslator(this);
        }
        if(cmd instanceof LinkAuditRecordCommand)
        {
            return (JDBCValueTranslator)new LinkAuditJDBCValueTranslator(this);
        }
        throw new IllegalStateException("Cmd type not supported");
    }


    @Required
    public void setAuditScopeInvalidator(AuditScopeInvalidator auditScopeInvalidator)
    {
        this.auditScopeInvalidator = auditScopeInvalidator;
    }
}
