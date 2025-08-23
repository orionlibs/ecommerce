package de.hybris.platform.processengine.definition;

import com.google.common.base.Preconditions;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import java.util.Objects;

public final class ProcessDefinitionId
{
    private final String name;
    private final String version;


    public static ProcessDefinitionId of(BusinessProcessModel model)
    {
        Objects.requireNonNull(model);
        return new ProcessDefinitionId(model.getProcessDefinitionName(), model.getProcessDefinitionVersion());
    }


    public ProcessDefinitionId(String name)
    {
        this(name, null);
    }


    public ProcessDefinitionId(String name, String version)
    {
        this.name = Objects.<String>requireNonNull(name);
        this.version = version;
    }


    protected String getName()
    {
        return this.name;
    }


    protected String getVersion()
    {
        return this.version;
    }


    public boolean isActive()
    {
        return (this.version == null);
    }


    public boolean isHistorical()
    {
        return !isActive();
    }


    public ProcessDefinitionId getActiveId()
    {
        Preconditions.checkState((this.version != null), "ActiveId can't be obtained");
        return new ProcessDefinitionId(this.name);
    }


    public boolean equals(Object obj)
    {
        if(obj == this)
        {
            return true;
        }
        if(obj == null || !(obj instanceof ProcessDefinitionId))
        {
            return false;
        }
        ProcessDefinitionId other = (ProcessDefinitionId)obj;
        return (this.name.equals(other.name) && ((this.version == null) ? (other.version == null) : this.version.equals(other.version)));
    }


    public int hashCode()
    {
        int nameHash = this.name.hashCode();
        int versionHash = (this.version == null) ? 0 : this.version.hashCode();
        return nameHash ^ versionHash << 8;
    }


    public String toString()
    {
        return this.name + this.name;
    }
}
