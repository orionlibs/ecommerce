package de.hybris.platform.mediaconversion.os;

import java.io.File;

public interface OsConfigurationService
{
    File retrieveOsDirectory(File paramFile);


    String retrieveOsConfigurationKey(String paramString) throws NoSuchConfigurationKeyException;


    String retrieveOsSpecificProperty(String paramString1, String paramString2);
}
