package de.hybris.platform.persistence.audit.internal;

import de.hybris.platform.core.PK;

public interface AuditRecordInternal
{
    PK getPk();


    String getType();


    Object getAttribute(String paramString);


    Object getAttribute(String paramString1, String paramString2);


    Object getAttributeBeforeOperation(String paramString);


    Object getAttributeBeforeOperation(String paramString1, String paramString2);


    Object getAttributeAfterOperation(String paramString);


    Object getAttributeAfterOperation(String paramString1, String paramString2);
}
