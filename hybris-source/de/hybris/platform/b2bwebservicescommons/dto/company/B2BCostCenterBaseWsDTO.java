package de.hybris.platform.b2bwebservicescommons.dto.company;

import de.hybris.platform.commercewebservicescommons.dto.storesession.CurrencyWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value = "B2BCostCenterBase", description = "Representation of a cost center. This base bean has no relationship fields to other Org Unit WsDTOs")
public class B2BCostCenterBaseWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "originalCode", value = "The original code of the cost center. Deprecated since 2005.", example = "Custom_Retail")
    private String originalCode;
    @ApiModelProperty(name = "name", value = "The name of the cost center", example = "Custom Retail")
    private String name;
    @ApiModelProperty(name = "active", value = "Indication of whether the cost center is active. Deprecated since 2005. Read-only, used for display purposes.", example = "true")
    private String active;
    @ApiModelProperty(name = "activeFlag", value = "Boolean flag of whether the cost center is active.", example = "true")
    private Boolean activeFlag;
    @ApiModelProperty(name = "code", value = "The code of the cost center", example = "Custom_Retail")
    private String code;
    @ApiModelProperty(name = "currency", value = "The currency of the cost center")
    private CurrencyWsDTO currency;


    @Deprecated(forRemoval = true)
    public void setOriginalCode(String originalCode)
    {
        this.originalCode = originalCode;
    }


    @Deprecated(forRemoval = true)
    public String getOriginalCode()
    {
        return this.originalCode;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    @Deprecated(forRemoval = true)
    public void setActive(String active)
    {
        this.active = active;
    }


    @Deprecated(forRemoval = true)
    public String getActive()
    {
        return this.active;
    }


    public void setActiveFlag(Boolean activeFlag)
    {
        this.activeFlag = activeFlag;
    }


    public Boolean getActiveFlag()
    {
        return this.activeFlag;
    }


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getCode()
    {
        return this.code;
    }


    public void setCurrency(CurrencyWsDTO currency)
    {
        this.currency = currency;
    }


    public CurrencyWsDTO getCurrency()
    {
        return this.currency;
    }
}
