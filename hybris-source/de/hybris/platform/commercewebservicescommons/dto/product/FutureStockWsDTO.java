package de.hybris.platform.commercewebservicescommons.dto.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

@ApiModel(value = "FutureStock", description = "Representation of a Future Stock")
public class FutureStockWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "stock", value = "Stock information of future stock")
    private StockWsDTO stock;
    @ApiModelProperty(name = "date", value = "Date of future stock", example = "2056-12-31T09:00:00+0000")
    private Date date;
    @ApiModelProperty(name = "formattedDate", value = "Date of future stock expressed in text value", example = "31/12/2056")
    private String formattedDate;


    public void setStock(StockWsDTO stock)
    {
        this.stock = stock;
    }


    public StockWsDTO getStock()
    {
        return this.stock;
    }


    public void setDate(Date date)
    {
        this.date = date;
    }


    public Date getDate()
    {
        return this.date;
    }


    public void setFormattedDate(String formattedDate)
    {
        this.formattedDate = formattedDate;
    }


    public String getFormattedDate()
    {
        return this.formattedDate;
    }
}
