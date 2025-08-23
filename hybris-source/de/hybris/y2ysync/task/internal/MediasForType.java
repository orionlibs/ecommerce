package de.hybris.y2ysync.task.internal;

import de.hybris.platform.core.PK;
import java.io.Serializable;
import java.util.List;

public class MediasForType implements Serializable
{
    private final String composedTypeCode;
    private final String impexHeader;
    private final String dataHubColumns;
    private final List<PK> mediaPks;
    private final String dataHubType;


    private MediasForType(String composedTypeCode, String impexHeader, String dataHubColumns, List<PK> mediaPks, String dataHubType)
    {
        this.composedTypeCode = composedTypeCode;
        this.impexHeader = impexHeader;
        this.dataHubColumns = dataHubColumns;
        this.mediaPks = mediaPks;
        this.dataHubType = dataHubType;
    }


    public String getComposedTypeCode()
    {
        return this.composedTypeCode;
    }


    public String getImpexHeader()
    {
        return this.impexHeader;
    }


    public List<PK> getMediaPks()
    {
        return this.mediaPks;
    }


    public String getDataHubColumns()
    {
        return this.dataHubColumns;
    }


    public String getDataHubType()
    {
        return this.dataHubType;
    }


    public static MediasForTypeBuilder builder()
    {
        return new MediasForTypeBuilder();
    }
}
