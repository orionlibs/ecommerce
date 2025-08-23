package de.hybris.platform.servicelayer.impex.impl;

import de.hybris.platform.impex.model.ImpExMediaModel;
import de.hybris.platform.servicelayer.impex.ImpExResource;

public class MediaBasedImpExResource implements ImpExResource
{
    private final ImpExMediaModel media;


    public MediaBasedImpExResource(ImpExMediaModel media)
    {
        this.media = media;
    }


    public ImpExMediaModel getMedia()
    {
        return this.media;
    }
}
