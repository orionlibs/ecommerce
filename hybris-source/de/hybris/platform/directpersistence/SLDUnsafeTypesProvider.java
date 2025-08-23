package de.hybris.platform.directpersistence;

import java.util.Collection;

public interface SLDUnsafeTypesProvider
{
    boolean isSLDSafe(String paramString1, String paramString2, boolean paramBoolean1, boolean paramBoolean2);


    boolean isSLDSafe(String paramString, boolean paramBoolean1, boolean paramBoolean2);


    boolean isSLDSafe(String paramString, boolean paramBoolean);


    UnsafeTypeInfo getInfo(String paramString);


    Collection<UnsafeTypeInfo> getAllUnsafeTypes();
}
