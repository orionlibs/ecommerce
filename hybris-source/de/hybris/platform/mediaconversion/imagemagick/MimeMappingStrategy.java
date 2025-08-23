package de.hybris.platform.mediaconversion.imagemagick;

public interface MimeMappingStrategy
{
    String fileExtensionForMimeType(String paramString);


    String determineMimeTypeByFileName(String paramString);
}
