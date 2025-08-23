package de.hybris.platform.platformbackoffice.widgets.compare.model;

import java.util.Objects;
import java.util.Set;

public class ClassificationDescriptor
{
    private final String code;
    private final String name;
    private final CatalogDescriptor catalogDescriptor;
    private final Set<FeatureDescriptor> features;


    public ClassificationDescriptor(String code, String name, CatalogDescriptor catalogDescriptor, Set<FeatureDescriptor> features)
    {
        this.code = code;
        this.name = name;
        this.catalogDescriptor = catalogDescriptor;
        this.features = features;
        this.features.forEach(feature -> {
            feature.setClassificationCode(code);
            feature.setCanRead(catalogDescriptor.canRead());
            feature.setCanWrite(catalogDescriptor.canWrite());
        });
    }


    public String getCode()
    {
        return this.code;
    }


    public String getName()
    {
        return this.name;
    }


    public CatalogDescriptor getCatalogDescriptor()
    {
        return this.catalogDescriptor;
    }


    public Set<FeatureDescriptor> getFeatures()
    {
        return this.features;
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }
        ClassificationDescriptor that = (ClassificationDescriptor)o;
        return (Objects.equals(this.code, that.code) && Objects.equals(this.name, that.name) &&
                        Objects.equals(this.catalogDescriptor, that.catalogDescriptor));
    }


    public int hashCode()
    {
        return Objects.hash(new Object[] {this.code, this.name, this.catalogDescriptor});
    }
}
