package de.hybris.platform.directpersistence.audit.dao;

import de.hybris.platform.core.PK;
import de.hybris.platform.persistence.audit.gateway.LinkAuditRecordCommand;
import de.hybris.platform.persistence.audit.gateway.TypeAuditRecordCommand;
import java.util.List;

@Deprecated(since = "ages", forRemoval = true)
public interface WriteAuditRecordsDAO
{
    void saveLinkAuditRecords(List<LinkAuditRecordCommand> paramList);


    void saveTypeAuditRecords(List<TypeAuditRecordCommand> paramList);


    int removeAuditRecordsForType(String paramString);


    int removeAuditRecordsForType(String paramString, PK paramPK);
}
