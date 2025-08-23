package de.hybris.platform.persistence.audit.gateway;

import de.hybris.platform.directpersistence.DirectPersistenceUtils;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuditStorageUtils
{
    private static final Logger LOG = LoggerFactory.getLogger(AuditStorageUtils.class);


    public static String getAuditTableName(String type)
    {
        String auditTableName = DirectPersistenceUtils.getInfoMapForType(type).getAuditTableName();
        if(auditTableName == null)
        {
            LOG.error("No audit table name found for type: " + type + ". Most probably the type is declared as abstract. Please use non-abstract types only.");
            throw new SystemException("Type " + type + " has no audit table.");
        }
        return auditTableName;
    }
}
