package de.hybris.platform.hac.data.dto;

import de.hybris.platform.core.initialization.SystemSetupCollectorResult;

public class PatchData
{
    private final String hash;
    private final String description;
    private final String process;
    private final boolean required;
    private final String name;


    public static PatchData from(SystemSetupCollectorResult data)
    {
        return new PatchData(data);
    }


    private PatchData(SystemSetupCollectorResult data)
    {
        this.hash = data.getHash();
        this.description = data.getDescription();
        this.process = data.getProcess().name();
        this.required = data.isRequired();
        this.name = data.getName();
    }


    public String getHash()
    {
        return this.hash;
    }


    public String getDescription()
    {
        return this.description;
    }


    public String getProcess()
    {
        return this.process;
    }


    public boolean isRequired()
    {
        return this.required;
    }


    public String getName()
    {
        return this.name;
    }
}
