package de.hybris.platform.commercefacades.product.data;

import java.io.Serializable;
import java.util.List;

public class ProductReferencesData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<ProductReferenceData> references;


    public void setReferences(List<ProductReferenceData> references)
    {
        this.references = references;
    }


    public List<ProductReferenceData> getReferences()
    {
        return this.references;
    }
}
