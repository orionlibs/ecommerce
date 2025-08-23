package de.hybris.platform.persistence.audit.gateway;

import de.hybris.platform.core.PK;
import java.util.List;

public interface WriteAuditGateway
{
    void saveLinkAuditRecords(List<LinkAuditRecordCommand> paramList);


    void saveTypeAuditRecords(List<TypeAuditRecordCommand> paramList);


    int removeAuditRecordsForType(String paramString);


    int removeAuditRecordsForType(String paramString, PK paramPK);
}
