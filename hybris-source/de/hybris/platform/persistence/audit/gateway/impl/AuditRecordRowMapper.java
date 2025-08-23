package de.hybris.platform.persistence.audit.gateway.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.persistence.audit.AuditType;
import de.hybris.platform.persistence.audit.gateway.JsonAuditRecord;
import de.hybris.platform.persistence.audit.gateway.PayloadContent;
import de.hybris.platform.persistence.property.JDBCValueMappings;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.function.BiFunction;
import java.util.function.LongFunction;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

public class AuditRecordRowMapper implements RowMapper<JsonAuditRecord>
{
    private static final JDBCValueMappings.ValueReader<Long, ?> LONG_VALUE_READER = JDBCValueMappings.getInstance()
                    .getValueReader(Long.class);
    private static final JDBCValueMappings.ValueReader<PK, ?> PK_VALUE_READER = JDBCValueMappings.getInstance()
                    .getValueReader(PK.class);
    private static final JDBCValueMappings.ValueReader<Date, ?> DATE_VALUE_READER = JDBCValueMappings.getInstance()
                    .getValueReader(Date.class);
    private final String typeCode;
    private final LongFunction<AuditType> longToAuditType;
    private final ResultSetExtractor<PayloadContent> payloadContentExtractor;
    private final BiFunction<String, Long, PayloadContent> payloadContentProvider;


    public AuditRecordRowMapper(String typeCode, LongFunction<AuditType> longToAuditType, ResultSetExtractor<PayloadContent> payloadContentExtractor, BiFunction<String, Long, PayloadContent> payloadContentProvider)
    {
        this.typeCode = typeCode;
        this.longToAuditType = longToAuditType;
        this.payloadContentExtractor = payloadContentExtractor;
        this.payloadContentProvider = payloadContentProvider;
    }


    public JsonAuditRecord mapRow(ResultSet resultSet, int i) throws SQLException
    {
        Long id = (Long)LONG_VALUE_READER.getValue(resultSet, "ID");
        PK itemPk = (PK)PK_VALUE_READER.getValue(resultSet, "ITEMPK");
        PK itemTypePk = (PK)PK_VALUE_READER.getValue(resultSet, "ITEMTYPEPK");
        String changingUser = resultSet.getString("changinguser");
        AuditType operationType = this.longToAuditType.apply(((Long)LONG_VALUE_READER.getValue(resultSet, "operationtype")).longValue());
        Date timestamp = (Date)DATE_VALUE_READER.getValue(resultSet, "timestamp");
        Date currentTimestamp = (Date)DATE_VALUE_READER.getValue(resultSet, "currenttimestamp");
        return JsonAuditRecord.builder()
                        .withId(id)
                        .withPk(itemPk)
                        .withType(this.typeCode)
                        .withTypePk(itemTypePk)
                        .withChangingUser(changingUser)
                        .withAuditType(operationType)
                        .withTimestamp(timestamp)
                        .withCurrentTimestamp(currentTimestamp)
                        .withPayloadContent((PayloadContent)this.payloadContentExtractor.extractData(resultSet))
                        .withPayloadContentProvider(this.payloadContentProvider)
                        .build();
    }
}
