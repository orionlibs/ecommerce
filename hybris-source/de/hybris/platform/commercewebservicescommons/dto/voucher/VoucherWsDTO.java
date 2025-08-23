package de.hybris.platform.commercewebservicescommons.dto.voucher;

import de.hybris.platform.commercewebservicescommons.dto.product.PriceWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.storesession.CurrencyWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value = "Voucher", description = "Representation of a Voucher")
public class VoucherWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "code", value = "The identifier of the Voucher. This is the first part of voucher code which holds first 3 letters, like: 123")
    private String code;
    @ApiModelProperty(name = "voucherCode", value = "Voucher code, is the holder for keeping specific occasional voucher related to business usage. It can be generated and looks like: 123-H8BC-Y3D5-34AL")
    private String voucherCode;
    @ApiModelProperty(name = "name", value = "Name of the voucher")
    private String name;
    @ApiModelProperty(name = "description", value = "Description of the voucher")
    private String description;
    @ApiModelProperty(name = "value", value = "Value of the voucher. Example of such value is: 15.0d")
    private Double value;
    @ApiModelProperty(name = "valueFormatted", value = "Formatted value of the voucher")
    private String valueFormatted;
    @ApiModelProperty(name = "valueString", value = "The value of the voucher to display. Example: 15.0%")
    private String valueString;
    @ApiModelProperty(name = "freeShipping", value = "Specifies if the order this voucher is applied to is shipped for free (true) or not (false). Defaults to false.")
    private Boolean freeShipping;
    @ApiModelProperty(name = "currency", value = "Currency of the voucher")
    private CurrencyWsDTO currency;
    @ApiModelProperty(name = "appliedValue", value = "Applied value when using this voucher")
    private PriceWsDTO appliedValue;


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getCode()
    {
        return this.code;
    }


    public void setVoucherCode(String voucherCode)
    {
        this.voucherCode = voucherCode;
    }


    public String getVoucherCode()
    {
        return this.voucherCode;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public String getDescription()
    {
        return this.description;
    }


    public void setValue(Double value)
    {
        this.value = value;
    }


    public Double getValue()
    {
        return this.value;
    }


    public void setValueFormatted(String valueFormatted)
    {
        this.valueFormatted = valueFormatted;
    }


    public String getValueFormatted()
    {
        return this.valueFormatted;
    }


    public void setValueString(String valueString)
    {
        this.valueString = valueString;
    }


    public String getValueString()
    {
        return this.valueString;
    }


    public void setFreeShipping(Boolean freeShipping)
    {
        this.freeShipping = freeShipping;
    }


    public Boolean getFreeShipping()
    {
        return this.freeShipping;
    }


    public void setCurrency(CurrencyWsDTO currency)
    {
        this.currency = currency;
    }


    public CurrencyWsDTO getCurrency()
    {
        return this.currency;
    }


    public void setAppliedValue(PriceWsDTO appliedValue)
    {
        this.appliedValue = appliedValue;
    }


    public PriceWsDTO getAppliedValue()
    {
        return this.appliedValue;
    }
}
