package de.hybris.platform.directpersistence;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class DefaultSLDUnsafeTypesProvider implements SLDUnsafeTypesProvider
{
    private final Map<String, UnsafeTypeInfo> unsafeTypesMap;


    public DefaultSLDUnsafeTypesProvider(Collection<UnsafeTypeInfo> unsafeTypes)
    {
        this.unsafeTypesMap = (Map<String, UnsafeTypeInfo>)unsafeTypes.stream().collect(Collectors.toMap(i -> i.getTypeCode(), i -> i));
    }


    public boolean isSLDSafe(String typeCode, String attribute, boolean read, boolean ignoreMarkedMethods)
    {
        UnsafeTypeInfo info = this.unsafeTypesMap.get(typeCode);
        if(info == null)
        {
            return true;
        }
        if(ignoreMarkedMethods)
        {
            return read ? (!info.isUnsafeToReadIgnoringMarked(attribute)) : (!info.isUnsafeToWriteIgnoringMarked(attribute));
        }
        return read ? (!info.isUnsafeToRead(attribute)) : (!info.isUnsafeToWrite(attribute));
    }


    public boolean isSLDSafe(String typeCode, boolean read, boolean ignoreMarkedMethods)
    {
        UnsafeTypeInfo info = this.unsafeTypesMap.get(typeCode);
        if(info == null)
        {
            return true;
        }
        if(ignoreMarkedMethods)
        {
            return read ? (!info.isUnsafeToReadIgnoringMarked()) : (!info.isUnsafeToWriteIgnoringMarked());
        }
        return read ? (!info.isUnsafeToRead()) : (!info.isUnsafeToWrite());
    }


    public boolean isSLDSafe(String typeCode, boolean ignoreMarkedMethods)
    {
        UnsafeTypeInfo info = this.unsafeTypesMap.get(typeCode);
        return (info == null || (ignoreMarkedMethods && !info.isUnsafeIgnoringMarked()));
    }


    public UnsafeTypeInfo getInfo(String type)
    {
        return this.unsafeTypesMap.get(type);
    }


    public Collection<UnsafeTypeInfo> getAllUnsafeTypes()
    {
        return this.unsafeTypesMap.values();
    }
}
