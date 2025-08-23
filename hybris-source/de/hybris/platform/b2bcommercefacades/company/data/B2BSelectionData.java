package de.hybris.platform.b2bcommercefacades.company.data;

import java.io.Serializable;
import java.util.List;

public class B2BSelectionData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String id;
    private boolean selected;
    private boolean active;
    private List<String> roles;
    private List<String> displayRoles;
    private String normalizedCode;


    public void setId(String id)
    {
        this.id = id;
    }


    public String getId()
    {
        return this.id;
    }


    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }


    public boolean isSelected()
    {
        return this.selected;
    }


    public void setActive(boolean active)
    {
        this.active = active;
    }


    public boolean isActive()
    {
        return this.active;
    }


    public void setRoles(List<String> roles)
    {
        this.roles = roles;
    }


    public List<String> getRoles()
    {
        return this.roles;
    }


    public void setDisplayRoles(List<String> displayRoles)
    {
        this.displayRoles = displayRoles;
    }


    public List<String> getDisplayRoles()
    {
        return this.displayRoles;
    }


    public void setNormalizedCode(String normalizedCode)
    {
        this.normalizedCode = normalizedCode;
    }


    public String getNormalizedCode()
    {
        return this.normalizedCode;
    }
}
