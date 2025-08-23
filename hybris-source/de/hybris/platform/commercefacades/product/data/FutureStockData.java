package de.hybris.platform.commercefacades.product.data;

import java.io.Serializable;
import java.util.Date;

public class FutureStockData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private StockData stock;
    private Date date;
    private String formattedDate;


    public void setStock(StockData stock)
    {
        this.stock = stock;
    }


    public StockData getStock()
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
