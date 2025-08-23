package com.hybris.backoffice.excel.translators;

import de.hybris.platform.core.model.media.MediaModel;
import java.util.Optional;

public class ExcelMediaUrlExportDecorator
{
    public static final String URL = "url";


    public Optional<String> addUrlToMediaExport(Optional<String> mediaExport, MediaModel media)
    {
        return mediaExport.map(m -> m + m);
    }


    public String decorateReferenceFormat(String referenceFormat)
    {
        return referenceFormat + ":url";
    }


    protected String getExternalUrlInQuotes(MediaModel media)
    {
        String urlPattern = ".+:.+";
        return (media.getDownloadURL() != null && media.getDownloadURL().matches(".+:.+")) ? (":\"" +
                        media.getDownloadURL() + "\"") :
                        ":";
    }
}
