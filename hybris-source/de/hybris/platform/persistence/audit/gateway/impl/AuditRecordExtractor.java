package de.hybris.platform.persistence.audit.gateway.impl;

import de.hybris.platform.persistence.audit.gateway.AuditRecord;
import de.hybris.platform.persistence.audit.gateway.AuditSearchQuery;
import de.hybris.platform.persistence.audit.gateway.JsonAuditRecord;
import de.hybris.platform.persistence.audit.gateway.SearchRule;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

public class AuditRecordExtractor implements ResultSetExtractor<List<JsonAuditRecord>>
{
    private static final Logger LOG = LoggerFactory.getLogger(AuditRecordExtractor.class);
    private final RowMapper<JsonAuditRecord> rowMapper;
    private final AuditSearchQuery searchQuery;
    private final String[] ruleValues;
    private int rowCounter = 0;


    public AuditRecordExtractor(AuditSearchQuery searchQuery, AuditRecordRowMapper rowMapper)
    {
        this.rowMapper = (RowMapper<JsonAuditRecord>)rowMapper;
        this.searchQuery = searchQuery;
        this.ruleValues = (String[])searchQuery.getPayloadSearchRules().stream().map(c -> c.getValue().toString()).toArray(x$0 -> new String[x$0]);
    }


    public List<JsonAuditRecord> extractData(ResultSet resultSet) throws SQLException
    {
        LOG.debug("Filtering audit records by payload search rules: {}", this.searchQuery
                        .getPayloadSearchRules());
        List<JsonAuditRecord> result = new ArrayList<>();
        this.rowCounter = 0;
        while(resultSet.next())
        {
            this.rowCounter++;
            if(!verifyIfResultSetRowShouldBeProcessed(resultSet))
            {
                continue;
            }
            JsonAuditRecord record = (JsonAuditRecord)this.rowMapper.mapRow(resultSet, this.rowCounter);
            if(verifyIfRecordShouldBeAddedToResult(record))
            {
                result.add(record);
            }
        }
        return result;
    }


    private boolean verifyIfResultSetRowShouldBeProcessed(ResultSet resultSet) throws SQLException
    {
        if(this.ruleValues.length == 0)
        {
            return true;
        }
        String payloadBefore = resultSet.getString("payloadbefore");
        String payloadAfter = resultSet.getString("payloadafter");
        return (payloadDoesContainSearchRulesValues(payloadBefore) || payloadDoesContainSearchRulesValues(payloadAfter));
    }


    private boolean verifyIfRecordShouldBeAddedToResult(JsonAuditRecord record)
    {
        if(this.ruleValues.length == 0)
        {
            return true;
        }
        return payloadEqualsAnyOfSearchRules((AuditRecord)record);
    }


    private boolean payloadDoesContainSearchRulesValues(String rawPayload)
    {
        return StringUtils.containsAny(rawPayload, (CharSequence[])this.ruleValues);
    }


    private boolean payloadEqualsAnyOfSearchRules(AuditRecord record)
    {
        List<SearchRule> payloadSearchRules = this.searchQuery.getPayloadSearchRules();
        return payloadSearchRules.stream()
                        .anyMatch(rule -> ruleMatchesRecord(record, rule));
    }


    private boolean ruleMatchesRecord(AuditRecord record, SearchRule rule)
    {
        if(!rule.getValue().equals(record.getAttributeBeforeOperation(rule
                        .getFieldName())))
        {
            if(rule.getValue()
                            .equals(record
                                            .getAttributeAfterOperation(rule
                                                            .getFieldName())))
                ;
            return false;
        }
    }


    public int getCountedRows()
    {
        return this.rowCounter;
    }
}
