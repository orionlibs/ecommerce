package de.hybris.platform.directpersistence;

public interface JaloAccessorsService
{
    boolean isSLDSafe(String paramString);


    boolean isSLDSafeForRead(String paramString);


    boolean isSLDSafeForWrite(String paramString);
}
