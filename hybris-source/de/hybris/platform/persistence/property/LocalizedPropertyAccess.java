package de.hybris.platform.persistence.property;

import de.hybris.platform.core.PK;
import java.util.Map;
import java.util.Set;

public interface LocalizedPropertyAccess extends PropertyAccess
{
    Map<String, Object> getAllProperties(PK paramPK);


    Object setProperty(String paramString, PK paramPK, Object paramObject);


    Object getProperty(String paramString, PK paramPK);


    Object removeProperty(String paramString, PK paramPK);


    Set<String> getPropertyNames(PK paramPK);
}
