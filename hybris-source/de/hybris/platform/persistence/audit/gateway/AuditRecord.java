package de.hybris.platform.persistence.audit.gateway;

import de.hybris.platform.core.PK;
import de.hybris.platform.persistence.audit.AuditType;
import de.hybris.platform.persistence.audit.internal.AuditRecordInternal;
import java.util.Date;
import java.util.Map;

public interface AuditRecord extends AuditRecordInternal
{
    Long getVersion();


    PK getPk();


    String getType();


    PK getTypePk();


    String getChangingUser();


    AuditType getAuditType();


    Object getAttributeBeforeOperation(String paramString);


    Object getAttributeBeforeOperation(String paramString1, String paramString2);


    Object getAttributeAfterOperation(String paramString);


    Object getAttributeAfterOperation(String paramString1, String paramString2);


    Map<String, Object> getAttributesBeforeOperation();


    Map<String, Object> getAttributesAfterOperation();


    Map<String, Object> getAttributesBeforeOperation(String paramString);


    Map<String, Object> getAttributesAfterOperation(String paramString);


    Date getTimestamp();


    Date getCurrentTimestamp();


    default boolean isLink()
    {
        return false;
    }


    Map<String, Object> getContext();


    default Object getAttribute(String key)
    {
        if(AuditType.DELETION.equals(getAuditType()) || AuditType.CURRENT.equals(getAuditType()))
        {
            return getAttributeBeforeOperation(key);
        }
        return getAttributeAfterOperation(key);
    }


    default Object getAttribute(String key, String langIsoCode)
    {
        if(AuditType.DELETION.equals(getAuditType()) || AuditType.CURRENT.equals(getAuditType()))
        {
            return getAttributeBeforeOperation(key, langIsoCode);
        }
        return getAttributeAfterOperation(key, langIsoCode);
    }
}
