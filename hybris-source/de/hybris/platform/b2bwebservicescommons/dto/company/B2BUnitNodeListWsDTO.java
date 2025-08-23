package de.hybris.platform.b2bwebservicescommons.dto.company;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "B2BUnitNodeList", description = "Representation of an organizational unit node list")
public class B2BUnitNodeListWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "unitNodes", value = "List of Unit Nodes", required = true)
    private List<B2BUnitNodeWsDTO> unitNodes;


    public void setUnitNodes(List<B2BUnitNodeWsDTO> unitNodes)
    {
        this.unitNodes = unitNodes;
    }


    public List<B2BUnitNodeWsDTO> getUnitNodes()
    {
        return this.unitNodes;
    }
}
