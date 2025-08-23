package de.hybris.y2ysync.task.runner.internal;

import com.google.common.base.MoreObjects;
import de.hybris.platform.core.PK;
import java.util.Objects;

public class ImportScript
{
    private final String typeCode;
    private final String header;
    private final String content;
    private final PK mediaArchivePk;


    public ImportScript(String typeCode, String header, String content, PK mediaArchivePk)
    {
        Objects.requireNonNull(typeCode, "typeCode can't be null");
        Objects.requireNonNull(header, "header can't be null");
        Objects.requireNonNull(content, "content can't be null");
        this.typeCode = typeCode;
        this.header = header;
        this.content = content;
        this.mediaArchivePk = mediaArchivePk;
    }


    public String getTypeCode()
    {
        return this.typeCode;
    }


    public String getHeader()
    {
        return this.header;
    }


    public String getContent()
    {
        return this.content;
    }


    public PK getMediaArchivePk()
    {
        return this.mediaArchivePk;
    }


    public String toString()
    {
        return MoreObjects.toStringHelper(ImportScript.class).add("typeCode", this.typeCode).add("header", this.header)
                        .add("content", this.content).add("mediaArchivePk", this.mediaArchivePk).toString();
    }
}
