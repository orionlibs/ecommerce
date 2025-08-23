package de.hybris.platform.directpersistence.audit.dao.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.directpersistence.audit.dao.WriteAuditRecordsDAO;
import de.hybris.platform.persistence.audit.gateway.LinkAuditRecordCommand;
import de.hybris.platform.persistence.audit.gateway.TypeAuditRecordCommand;
import de.hybris.platform.persistence.audit.gateway.WriteAuditGateway;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

@Deprecated(since = "ages", forRemoval = true)
public class DefaultWriteAuditRecordsDAO implements WriteAuditRecordsDAO
{
    private WriteAuditGateway writeAuditGateway;


    public void saveLinkAuditRecords(List<LinkAuditRecordCommand> cmdList)
    {
        this.writeAuditGateway.saveLinkAuditRecords(cmdList);
    }


    public void saveTypeAuditRecords(List<TypeAuditRecordCommand> cmdList)
    {
        this.writeAuditGateway.saveTypeAuditRecords(cmdList);
    }


    public int removeAuditRecordsForType(String type)
    {
        return this.writeAuditGateway.removeAuditRecordsForType(type);
    }


    public int removeAuditRecordsForType(String type, PK pk)
    {
        return this.writeAuditGateway.removeAuditRecordsForType(type, pk);
    }


    @Required
    public void setWriteAuditGateway(WriteAuditGateway writeAuditGateway)
    {
        this.writeAuditGateway = writeAuditGateway;
    }
}
