package de.hybris.platform.media;

public interface MediaSource<SOURCE>
{
    Long getDataPk();


    Long getMediaPk();


    String getLocation();


    String getLocationHash();


    String getMime();


    String getInternalUrl();


    String getRealFileName();


    String getFolderQualifier();


    Long getSize();


    SOURCE getSource();
}
