package de.hybris.platform.commercefacades.product.data;

import de.hybris.platform.catalog.enums.ProductReferenceTypeEnum;
import de.hybris.platform.commerceservices.product.data.ReferenceData;

public class ProductReferenceData extends ReferenceData<ProductReferenceTypeEnum, ProductData>
{
    private Boolean preselected;


    public void setPreselected(Boolean preselected)
    {
        this.preselected = preselected;
    }


    public Boolean getPreselected()
    {
        return this.preselected;
    }
}
