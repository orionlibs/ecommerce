package de.hybris.platform.b2bwebservicescommons.dto.company;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "B2BUnitNode", description = "Representation of an organizational unit node")
public class B2BUnitNodeWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "id", value = "Identifier of the organizational unit represented by the node", required = true, example = "Pronto_Retail")
    private String id;
    @ApiModelProperty(name = "name", value = "Name of the organizational unit represented by the node", example = "Pronto Retail")
    private String name;
    @ApiModelProperty(name = "parent", value = "Unique identifier of organizational unit node's parent unit", example = "Pronto")
    private String parent;
    @ApiModelProperty(name = "active", value = "Boolean flag of whether organizational unit represented by the node is active", example = "true")
    private Boolean active;
    @ApiModelProperty(name = "children", value = "Child nodes of the organizational unit node")
    private List<B2BUnitNodeWsDTO> children;


    public void setId(String id)
    {
        this.id = id;
    }


    public String getId()
    {
        return this.id;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setParent(String parent)
    {
        this.parent = parent;
    }


    public String getParent()
    {
        return this.parent;
    }


    public void setActive(Boolean active)
    {
        this.active = active;
    }


    public Boolean getActive()
    {
        return this.active;
    }


    public void setChildren(List<B2BUnitNodeWsDTO> children)
    {
        this.children = children;
    }


    public List<B2BUnitNodeWsDTO> getChildren()
    {
        return this.children;
    }
}
