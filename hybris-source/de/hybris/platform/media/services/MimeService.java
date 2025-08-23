package de.hybris.platform.media.services;

import java.util.Set;

public interface MimeService
{
    String getFileExtensionFromMime(String paramString);


    String getBestExtensionFromMime(String paramString);


    String getMimeFromFileExtension(String paramString);


    String getMimeFromFirstBytes(byte[] paramArrayOfbyte);


    String getBestMime(String paramString1, byte[] paramArrayOfbyte, String paramString2);


    Set<String> getSupportedMimeTypes();


    boolean isZipRelatedMime(String paramString);
}
