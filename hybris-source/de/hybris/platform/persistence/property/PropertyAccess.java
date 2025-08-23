package de.hybris.platform.persistence.property;

import de.hybris.platform.jalo.ConsistencyCheckException;
import java.util.Map;
import java.util.Set;

public interface PropertyAccess
{
    public static final String BASE_QUALIFIER = "item.property";


    void registerOwner(PropertyOwner paramPropertyOwner);


    void dispose();


    Map getAllProperties();


    Object setProperty(String paramString, Object paramObject);


    Object getProperty(String paramString);


    Object removeProperty(String paramString);


    Set getPropertyNames();


    boolean hasModifiedProperties();


    void commitProperties() throws ConsistencyCheckException;


    void rollbackProperties() throws ConsistencyCheckException;
}
