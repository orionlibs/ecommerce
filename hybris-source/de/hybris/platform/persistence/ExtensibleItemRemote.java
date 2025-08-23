package de.hybris.platform.persistence;

import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.persistence.property.EJBPropertyContainer;
import de.hybris.platform.persistence.property.TypeInfoMap;
import java.util.Map;
import java.util.Set;

public interface ExtensibleItemRemote extends ItemRemote
{
    void copyPropertiesFrom(ExtensibleItemRemote paramExtensibleItemRemote);


    long getPropertyTimestamp();


    TypeInfoMap getTypeInfoMap();


    Map<String, Object> getAllProperties();


    Set<String> getPropertyNames();


    void setAllProperties(EJBPropertyContainer paramEJBPropertyContainer) throws ConsistencyCheckException;


    Object setProperty(String paramString, Object paramObject);


    Object getProperty(String paramString);


    Object getPropertyRaw(String paramString);


    Object removeProperty(String paramString);
}
