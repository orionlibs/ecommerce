package de.hybris.platform.media.web;

import de.hybris.platform.media.services.MediaLocationHashService;

public class MediaFilterLogicContext
{
    private final Iterable<String> mediaContext;
    private final String resourcePath;
    private MediaLocationHashService.HashType hashType;


    public MediaFilterLogicContext(Iterable<String> mediaContext, String resourcePath)
    {
        this.mediaContext = mediaContext;
        this.resourcePath = resourcePath;
    }


    public Iterable<String> getMediaContext()
    {
        return this.mediaContext;
    }


    public MediaLocationHashService.HashType getHashType()
    {
        return this.hashType;
    }


    public void setHashType(MediaLocationHashService.HashType hashType)
    {
        this.hashType = hashType;
    }


    public String getResourcePath()
    {
        return this.resourcePath;
    }
}
