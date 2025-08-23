package de.hybris.platform.commercefacades.product.data;

import java.io.Serializable;
import java.util.Collection;

public class ClassificationData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String code;
    private String name;
    private Collection<FeatureData> features;


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getCode()
    {
        return this.code;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setFeatures(Collection<FeatureData> features)
    {
        this.features = features;
    }


    public Collection<FeatureData> getFeatures()
    {
        return this.features;
    }
}
