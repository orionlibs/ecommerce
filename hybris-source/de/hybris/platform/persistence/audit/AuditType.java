package de.hybris.platform.persistence.audit;

import java.util.Arrays;
import java.util.Optional;

public enum AuditType
{
    DELETION(0),
    CREATION(1),
    MODIFICATION(2),
    CURRENT(-1);
    int operationCode;


    AuditType(int operationCode)
    {
        this.operationCode = operationCode;
    }


    public int getOperationCode()
    {
        return this.operationCode;
    }


    public static AuditType toAuditType(long operationCode)
    {
        return toAuditType(Math.toIntExact(operationCode));
    }


    public static AuditType toAuditType(int operationCode)
    {
        Optional<AuditType> found = Arrays.<AuditType>stream(values()).filter(v -> (v.getOperationCode() == operationCode)).findFirst();
        if(found.isPresent())
        {
            return found.get();
        }
        throw new IllegalStateException("No AuditType value for operationCode: " + operationCode);
    }
}
