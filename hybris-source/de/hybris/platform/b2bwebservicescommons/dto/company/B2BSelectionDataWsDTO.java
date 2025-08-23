package de.hybris.platform.b2bwebservicescommons.dto.company;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "B2BSelectionData", description = "Representation of object selection data")
public class B2BSelectionDataWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "id", value = "The ID of the selected object", example = "Retail_2K")
    private String id;
    @ApiModelProperty(name = "selected", value = "If the this object was selected", example = "true")
    private boolean selected;
    @ApiModelProperty(name = "active", value = "If this object is active", example = "false")
    private boolean active;
    @ApiModelProperty(name = "roles", value = "roles")
    private List<String> roles;
    @ApiModelProperty(name = "displayRoles", value = "display roles")
    private List<String> displayRoles;
    @ApiModelProperty(name = "normalizedCode", value = "The normalized code")
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
