package de.hybris.platform.scripting.engine.internal.impl;

import com.google.common.base.MoreObjects;
import de.hybris.platform.scripting.engine.internal.ScriptEngineType;
import java.util.Objects;

public class DefaultScriptEngineType implements ScriptEngineType
{
    private final String name;
    private final String fileExtension;
    private final String mime;
    private final boolean cached;


    public DefaultScriptEngineType(String name, String fileExtension, String mime)
    {
        this(name, fileExtension, mime, false);
    }


    public DefaultScriptEngineType(String name, String fileExtension, String mime, boolean cached)
    {
        this.name = name;
        this.fileExtension = fileExtension;
        this.mime = mime;
        this.cached = cached;
    }


    public String getName()
    {
        return this.name;
    }


    public String getFileExtension()
    {
        return this.fileExtension;
    }


    public String getMime()
    {
        return this.mime;
    }


    public boolean canBeCached()
    {
        return this.cached;
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }
        DefaultScriptEngineType that = (DefaultScriptEngineType)o;
        return (Objects.equals(this.name, that.name) && Objects.equals(this.fileExtension, that.fileExtension) &&
                        Objects.equals(this.mime, that.mime) && this.cached == that.cached);
    }


    public int hashCode()
    {
        return Objects.hash(new Object[] {this.name, this.fileExtension, this.mime, Boolean.valueOf(this.cached)});
    }


    public String toString()
    {
        return MoreObjects.toStringHelper(this).add("name", this.name).add("mime", this.mime).add("fileExtension", this.fileExtension)
                        .add("cached", this.cached).toString();
    }
}
