package de.hybris.platform.persistence.audit.gateway.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.persistence.audit.gateway.AuditRecord;
import de.hybris.platform.persistence.audit.gateway.JsonAuditRecord;
import de.hybris.platform.persistence.audit.gateway.LinkAuditRecord;
import de.hybris.platform.persistence.property.JDBCValueMappings;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

class LinkAuditRecordRowMapper implements RowMapper<LinkAuditRecord>
{
    private static final JDBCValueMappings.ValueReader<PK, ?> PK_VALUE_READER = JDBCValueMappings.getInstance()
                    .getValueReader(PK.class);
    private final LinkAuditRecord.LinkSide parentSide;
    private final AuditRecordRowMapper auditRecordRowMapper;


    public LinkAuditRecordRowMapper(LinkAuditRecord.LinkSide parentLinkSide, AuditRecordRowMapper auditRecordRowMapper)
    {
        this.auditRecordRowMapper = auditRecordRowMapper;
        this.parentSide = parentLinkSide;
    }


    public LinkAuditRecord mapRow(ResultSet resultSet, int i) throws SQLException
    {
        JsonAuditRecord jsonAuditRecord = this.auditRecordRowMapper.mapRow(resultSet, i);
        PK sourcePk = (PK)PK_VALUE_READER.getValue(resultSet, "SOURCEPK");
        PK targetPk = (PK)PK_VALUE_READER.getValue(resultSet, "TARGETPK");
        return LinkAuditRecord.builder()
                        .withAuditRecord((AuditRecord)jsonAuditRecord)
                        .withParentSide(this.parentSide)
                        .withSourcePk(sourcePk)
                        .withTargetPk(targetPk)
                        .build();
    }
}
