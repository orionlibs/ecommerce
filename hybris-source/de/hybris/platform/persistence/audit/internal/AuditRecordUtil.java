package de.hybris.platform.persistence.audit.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class AuditRecordUtil
{
    public static Collection<Object> buildCollectionForType(int collectionType)
    {
        return (collectionType == 1) ? new ArrayList() : new HashSet();
    }
}
