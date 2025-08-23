package de.hybris.platform.commercewebservicescommons.dto.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "BaseOption", description = "Representation of a Base Option")
public class BaseOptionWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "variantType", value = "Variant type of base option")
    private String variantType;
    @ApiModelProperty(name = "options", value = "List of all variant options")
    private List<VariantOptionWsDTO> options;
    @ApiModelProperty(name = "selected", value = "Variant option selected")
    private VariantOptionWsDTO selected;


    public void setVariantType(String variantType)
    {
        this.variantType = variantType;
    }


    public String getVariantType()
    {
        return this.variantType;
    }


    public void setOptions(List<VariantOptionWsDTO> options)
    {
        this.options = options;
    }


    public List<VariantOptionWsDTO> getOptions()
    {
        return this.options;
    }


    public void setSelected(VariantOptionWsDTO selected)
    {
        this.selected = selected;
    }


    public VariantOptionWsDTO getSelected()
    {
        return this.selected;
    }
}
