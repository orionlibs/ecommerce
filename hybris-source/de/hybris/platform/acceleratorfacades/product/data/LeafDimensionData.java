package de.hybris.platform.acceleratorfacades.product.data;

import de.hybris.platform.commercefacades.product.data.PriceData;
import java.io.Serializable;

public class LeafDimensionData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String leafDimensionHeader;
    private String leafDimensionValue;
    private String leafDimensionData;
    private int sequence;
    private PriceData price;


    public void setLeafDimensionHeader(String leafDimensionHeader)
    {
        this.leafDimensionHeader = leafDimensionHeader;
    }


    public String getLeafDimensionHeader()
    {
        return this.leafDimensionHeader;
    }


    public void setLeafDimensionValue(String leafDimensionValue)
    {
        this.leafDimensionValue = leafDimensionValue;
    }


    public String getLeafDimensionValue()
    {
        return this.leafDimensionValue;
    }


    public void setLeafDimensionData(String leafDimensionData)
    {
        this.leafDimensionData = leafDimensionData;
    }


    public String getLeafDimensionData()
    {
        return this.leafDimensionData;
    }


    public void setSequence(int sequence)
    {
        this.sequence = sequence;
    }


    public int getSequence()
    {
        return this.sequence;
    }


    public void setPrice(PriceData price)
    {
        this.price = price;
    }


    public PriceData getPrice()
    {
        return this.price;
    }
}
