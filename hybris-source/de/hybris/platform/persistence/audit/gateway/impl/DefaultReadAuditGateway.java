package de.hybris.platform.persistence.audit.gateway.impl;

import de.hybris.platform.persistence.audit.gateway.AuditSearchQuery;
import de.hybris.platform.persistence.audit.gateway.AuditSqlQuery;
import de.hybris.platform.persistence.audit.gateway.AuditSqlQueryFactory;
import de.hybris.platform.persistence.audit.gateway.AuditStorageUtils;
import de.hybris.platform.persistence.audit.gateway.JsonAuditRecord;
import de.hybris.platform.persistence.audit.gateway.LinkAuditRecord;
import de.hybris.platform.persistence.audit.gateway.PayloadContent;
import de.hybris.platform.persistence.audit.payload.json.AuditPayload;
import de.hybris.platform.util.JsonUtilities;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class DefaultReadAuditGateway extends AbstractReadAuditGateway
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultReadAuditGateway.class);
    private static final String QUERY_TEMPLATE_FOR_PAYLOAD = "SELECT payloadbefore, payloadafter, context FROM {0} WHERE id=?";
    private static final int DB_FETCH_SIZE = 100;
    private JdbcTemplate jdbcTemplate;
    private AuditSqlQueryFactory auditSqlQueryFactory;


    public <T extends de.hybris.platform.persistence.audit.gateway.AuditRecord> Stream<T> search(AuditSearchQuery searchQuery)
    {
        Objects.requireNonNull(searchQuery, "AuditSearchQuery is required");
        AuditRecordRowMapper auditRecordRowMapper = new AuditRecordRowMapper(searchQuery.getTypeCode(), this::toAuditType, this::extractPayloadContent, this::queryForPayloadContentById);
        if(searchQuery.isLinkRelatedQuery())
        {
            return (Stream)searchForLinkAudits(searchQuery, auditRecordRowMapper);
        }
        return (Stream)searchForStandardAudits(searchQuery, auditRecordRowMapper);
    }


    private Stream<LinkAuditRecord> searchForLinkAudits(AuditSearchQuery searchQuery, AuditRecordRowMapper auditRecordRowMapper)
    {
        AuditSqlQuery query = this.auditSqlQueryFactory.createStandardSqlQuery(searchQuery);
        List<LinkAuditRecord> result = this.jdbcTemplate.query(query.getSqlQuery(), (RowMapper)new LinkAuditRecordRowMapper(searchQuery
                        .getLinkSide(), auditRecordRowMapper), query
                        .getQueryPrams());
        return result.stream();
    }


    private Stream<JsonAuditRecord> searchForStandardAudits(AuditSearchQuery searchQuery, AuditRecordRowMapper auditRecordRowMapper)
    {
        AuditSqlQuery query = this.auditSqlQueryFactory.createSqlQuery(searchQuery);
        AuditRecordExtractor recordExtractor = new AuditRecordExtractor(searchQuery, auditRecordRowMapper);
        AuditRecordQueryExecutor auditRecordQueryExecutor = new AuditRecordQueryExecutor(recordExtractor, this.jdbcTemplate, 100);
        return auditRecordQueryExecutor.executeQuery(query).stream();
    }


    private PayloadContent queryForPayloadContentById(String type, Long id)
    {
        LOG.debug("SoftReference must be gone since we are querying for audit payload for type: '{}' and id: {}", type, id);
        String query = MessageFormat.format("SELECT payloadbefore, payloadafter, context FROM {0} WHERE id=?", new Object[] {AuditStorageUtils.getAuditTableName(type)});
        return (PayloadContent)this.jdbcTemplate.queryForObject(query, (rs, rowNum) -> extractPayloadContent(rs), new Object[] {id});
    }


    private PayloadContent extractPayloadContent(ResultSet resultSet) throws SQLException
    {
        String rawPayloadBefore = resultSet.getString("payloadbefore");
        String rawPayloadAfter = resultSet.getString("payloadafter");
        String rawContext = resultSet.getString("context");
        AuditPayload payloadBefore = StringUtils.isEmpty(rawPayloadBefore) ? null : deserialize(rawPayloadBefore);
        AuditPayload payloadAfter = StringUtils.isEmpty(rawPayloadAfter) ? null : deserialize(rawPayloadAfter);
        Map<String, Object> context = StringUtils.isEmpty(rawContext) ? null : (Map<String, Object>)JsonUtilities.fromJson(rawContext, Map.class);
        return new PayloadContent(payloadBefore, payloadAfter, context);
    }


    @Required
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Required
    public void setAuditSqlQueryFactory(AuditSqlQueryFactory auditSqlQueryFactory)
    {
        this.auditSqlQueryFactory = auditSqlQueryFactory;
    }
}
