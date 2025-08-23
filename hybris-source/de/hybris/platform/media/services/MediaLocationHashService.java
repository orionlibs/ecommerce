package de.hybris.platform.media.services;

public interface MediaLocationHashService
{
    @Deprecated(since = "2011", forRemoval = true)
    void verifyHashForLocation(String paramString1, String paramString2, String paramString3);


    default HashType verifyHash(String hash, String folderQualifier, String location, long size, String mime)
    {
        verifyHashForLocation(hash, folderQualifier, location);
        return HashType.LOCATION;
    }


    @Deprecated(since = "2011", forRemoval = true)
    String createHashForLocation(String paramString1, String paramString2);


    default String createHash(String folderQualifier, String location, long size, String mime)
    {
        return createHashForLocation(folderQualifier, location);
    }
}
