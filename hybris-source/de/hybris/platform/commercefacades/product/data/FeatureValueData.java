package de.hybris.platform.commercefacades.product.data;

import java.io.Serializable;

public class FeatureValueData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String value;


    public void setValue(String value)
    {
        this.value = value;
    }


    public String getValue()
    {
        return this.value;
    }
}
