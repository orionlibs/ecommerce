package de.hybris.platform.patches.actions.data;

import de.hybris.platform.patches.Patch;
import de.hybris.platform.patches.organisation.ImportOrganisationUnit;
import java.util.HashMap;
import java.util.Map;

public class PatchActionData
{
    protected String name;
    protected Map<PatchActionDataOption, Object> options;
    protected ImportOrganisationUnit organisationUnit;
    protected Patch patch;


    public PatchActionData(String actionName, Patch patch)
    {
        this.patch = patch;
        this.name = actionName;
        this.options = new HashMap<>();
    }


    public String getName()
    {
        return this.name;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public Object getOption(PatchActionDataOption option)
    {
        return getOptions().get(option);
    }


    public String getStringOption(PatchActionDataOption option)
    {
        return (String)getOptions().get(option);
    }


    public Boolean getBooleanOption(PatchActionDataOption option)
    {
        return (Boolean)getOptions().get(option);
    }


    public void setOption(PatchActionDataOption option, Object value)
    {
        getOptions().put(option, value);
    }


    public Map<PatchActionDataOption, Object> getOptions()
    {
        return this.options;
    }


    public void setOptions(Map<PatchActionDataOption, Object> options)
    {
        this.options = options;
    }


    public ImportOrganisationUnit getOrganisationUnit()
    {
        return this.organisationUnit;
    }


    public void setOrganisationUnit(ImportOrganisationUnit organisationUnit)
    {
        this.organisationUnit = organisationUnit;
    }


    public Patch getPatch()
    {
        return this.patch;
    }


    public void setPatch(Patch patch)
    {
        this.patch = patch;
    }
}
