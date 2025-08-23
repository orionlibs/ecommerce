package de.hybris.platform.platformbackoffice.classification.exception;

import de.hybris.platform.core.model.product.ProductModel;
import org.apache.commons.lang.ObjectUtils;

public class FeatureNotFoundException extends RuntimeException
{
    public FeatureNotFoundException(String qualifier, ProductModel productModel)
    {
        super(String.format("Feature [%s] not found on [%s].", new Object[] {qualifier, ObjectUtils.toString(productModel, "null")}));
    }
}
