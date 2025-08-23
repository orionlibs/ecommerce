package de.hybris.platform.mediaconversion.metadata;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.media.MediaModel;
import java.io.Serializable;

class MediaKey implements Serializable
{
    private static final long serialVersionUID = 8822477716521907096L;
    private final String code;
    private final CatalogVersionModel catalogVersion;


    MediaKey(MediaModel model)
    {
        this(model.getCode(), model.getCatalogVersion());
    }


    MediaKey(String code, CatalogVersionModel catalogVersion)
    {
        this.code = code;
        this.catalogVersion = catalogVersion;
    }


    String getCode()
    {
        return this.code;
    }


    CatalogVersionModel getCatalogVersion()
    {
        return this.catalogVersion;
    }
}
