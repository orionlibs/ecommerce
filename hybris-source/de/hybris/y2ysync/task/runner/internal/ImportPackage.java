package de.hybris.y2ysync.task.runner.internal;

import de.hybris.platform.catalog.model.CatalogUnawareMediaModel;

public class ImportPackage
{
    private final CatalogUnawareMediaModel mediaData;
    private final CatalogUnawareMediaModel mediaBinaries;


    public ImportPackage(CatalogUnawareMediaModel mediaData, CatalogUnawareMediaModel mediaBinaries)
    {
        this.mediaData = mediaData;
        this.mediaBinaries = mediaBinaries;
    }


    public CatalogUnawareMediaModel getMediaData()
    {
        return this.mediaData;
    }


    public CatalogUnawareMediaModel getMediaBinaries()
    {
        return this.mediaBinaries;
    }
}
