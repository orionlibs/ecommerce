package de.hybris.platform.commercewebservicescommons.dto.storesession;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "CurrencyList", description = "Representation of a Currency List")
public class CurrencyListWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "currencies", value = "List of currencies")
    private List<CurrencyWsDTO> currencies;


    public void setCurrencies(List<CurrencyWsDTO> currencies)
    {
        this.currencies = currencies;
    }


    public List<CurrencyWsDTO> getCurrencies()
    {
        return this.currencies;
    }
}
