package de.hybris.platform.impex.jalo.media;

import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.jalo.media.Media;

public interface MediaDataHandler
{
    void importData(Media paramMedia, String paramString) throws ImpExException;


    String exportData(Media paramMedia) throws ImpExException;


    void cleanUp();
}
